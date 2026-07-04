import { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import Topbar from '../../components/layout/Topbar';
import Loader from '../../components/common/Loader';
import StatusBadge from '../../components/common/StatusBadge';
import FadeIn from '../../components/common/FadeIn';
import { useAuth } from '../../context/AuthContext';
import { financeApi } from '../../api/finance';
import { hiringApi } from '../../api/hiring';
import { marketingApi } from '../../api/marketing';
import { approvalsApi } from '../../api/approvals';

export default function DashboardPage() {
  const { founder } = useAuth();
  const [loading, setLoading] = useState(true);
  const [data, setData] = useState({
    latestFinance: null,
    openJobs: 0,
    activeCampaigns: 0,
    pendingApprovals: [],
  });

  useEffect(() => {
    let cancelled = false;

    async function load() {
      try {
        const [records, jobs, campaigns, pending] = await Promise.all([
          financeApi.listRecords(),
          hiringApi.listJobs(),
          marketingApi.listCampaigns(),
          approvalsApi.list(true),
        ]);

        if (cancelled) return;

        setData({
          latestFinance: records[0] || null,
          openJobs: jobs.filter((j) => j.status === 'OPEN').length,
          activeCampaigns: campaigns.filter((c) => c.status === 'ACTIVE').length,
          pendingApprovals: pending,
        });
      } finally {
        if (!cancelled) setLoading(false);
      }
    }

    load();
    return () => {
      cancelled = true;
    };
  }, []);

  if (loading) return <Loader />;

  const { latestFinance, openJobs, activeCampaigns, pendingApprovals } = data;

  return (
    <div>
      <Topbar
        eyebrow="Founder Dashboard"
        title={`Welcome back, ${founder?.fullName?.split(' ')[0] || ''}`}
        subtitle={founder?.companyName ? `${founder.companyName} — operations overview` : 'Operations overview'}
      />
      <div className="app-content">
        <FadeIn>
        <div className="card-grid">
          <div className="stat-card" style={{ '--module-color': 'var(--finance)' }}>
            <div className="stat-card-label">Cash runway</div>
            <div className="stat-card-value accent mono">
              {latestFinance
                ? latestFinance.runwayMonths >= 0
                  ? `${latestFinance.runwayMonths} mo`
                  : '∞'
                : '—'}
            </div>
          </div>
          <div className="stat-card" style={{ '--module-color': 'var(--finance)' }}>
            <div className="stat-card-label">Monthly burn</div>
            <div className="stat-card-value accent mono">
              {latestFinance ? `$${Number(latestFinance.burnRate).toLocaleString()}` : '—'}
            </div>
          </div>
          <div className="stat-card" style={{ '--module-color': 'var(--hiring)' }}>
            <div className="stat-card-label">Open roles</div>
            <div className="stat-card-value accent mono">{openJobs}</div>
          </div>
          <div className="stat-card" style={{ '--module-color': 'var(--marketing)' }}>
            <div className="stat-card-label">Active campaigns</div>
            <div className="stat-card-value accent mono">{activeCampaigns}</div>
          </div>
        </div>
        </FadeIn>

        <FadeIn delay={0.1}>
        <div className="card" style={{ '--module-color': 'var(--warn)' }}>
          <div className="flex-between" style={{ marginBottom: 14 }}>
            <div className="section-title" style={{ marginBottom: 0 }}>
              Pending approvals
            </div>
            <Link to="/approvals" className="btn btn-ghost btn-sm">
              View all
            </Link>
          </div>

          {pendingApprovals.length === 0 ? (
            <p className="text-faint">Nothing waiting on your sign-off right now.</p>
          ) : (
            <table className="data-table">
              <thead>
                <tr>
                  <th>Module</th>
                  <th>Request</th>
                  <th>Status</th>
                </tr>
              </thead>
              <tbody>
                {pendingApprovals.slice(0, 5).map((a) => (
                  <tr key={a.id}>
                    <td>{a.module}</td>
                    <td>{a.title}</td>
                    <td>
                      <StatusBadge status={a.status} />
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          )}
        </div>
        </FadeIn>
      </div>
    </div>
  );
}
