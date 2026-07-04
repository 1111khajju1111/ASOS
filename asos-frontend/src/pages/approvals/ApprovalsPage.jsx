import { useEffect, useState } from 'react';
import Topbar from '../../components/layout/Topbar';
import Loader from '../../components/common/Loader';
import EmptyState from '../../components/common/EmptyState';
import StatusBadge from '../../components/common/StatusBadge';
import { approvalsApi } from '../../api/approvals';

const MODULE_COLOR = 'var(--warn)';

export default function ApprovalsPage() {
  const [loading, setLoading] = useState(true);
  const [approvals, setApprovals] = useState([]);
  const [pendingOnly, setPendingOnly] = useState(true);

  const load = async () => {
    setLoading(true);
    const data = await approvalsApi.list(pendingOnly);
    setApprovals(data);
    setLoading(false);
  };

  useEffect(() => {
    load();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [pendingOnly]);

  const handleDecision = async (id, status) => {
    await approvalsApi.decide(id, status);
    load();
  };

  return (
    <div style={{ '--module-color': MODULE_COLOR }}>
      <Topbar
        eyebrow="Approval Workflow"
        title="Approvals"
        subtitle="Founder sign-off on high-risk actions across every module"
        actions={
          <div className="tag-row">
            <button
              className={`btn btn-sm ${pendingOnly ? 'btn-primary' : 'btn-ghost'}`}
              onClick={() => setPendingOnly(true)}
            >
              Pending
            </button>
            <button
              className={`btn btn-sm ${!pendingOnly ? 'btn-primary' : 'btn-ghost'}`}
              onClick={() => setPendingOnly(false)}
            >
              All
            </button>
          </div>
        }
      />

      <div className="app-content">
        <div className="card">
          {loading ? (
            <Loader />
          ) : approvals.length === 0 ? (
            <EmptyState
              icon="✓"
              title={pendingOnly ? 'Nothing pending' : 'No approval requests yet'}
              description={
                pendingOnly
                  ? 'Requests raised by your agents for high-risk actions will show up here.'
                  : 'Approval requests from any module will appear here.'
              }
            />
          ) : (
            <table className="data-table">
              <thead>
                <tr>
                  <th>Module</th>
                  <th>Request</th>
                  <th>Requested</th>
                  <th>Status</th>
                  <th></th>
                </tr>
              </thead>
              <tbody>
                {approvals.map((a) => (
                  <tr key={a.id}>
                    <td>
                      <span className="badge badge-neutral">{a.module}</span>
                    </td>
                    <td>
                      <div>{a.title}</div>
                      {a.description && <div className="text-faint">{a.description}</div>}
                    </td>
                    <td className="text-muted">{new Date(a.requestedAt).toLocaleDateString()}</td>
                    <td>
                      <StatusBadge status={a.status} />
                    </td>
                    <td>
                      {a.status === 'PENDING' && (
                        <div className="tag-row">
                          <button className="btn btn-primary btn-sm" onClick={() => handleDecision(a.id, 'APPROVED')}>
                            Approve
                          </button>
                          <button className="btn btn-danger btn-sm" onClick={() => handleDecision(a.id, 'REJECTED')}>
                            Reject
                          </button>
                        </div>
                      )}
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          )}
        </div>
      </div>
    </div>
  );
}
