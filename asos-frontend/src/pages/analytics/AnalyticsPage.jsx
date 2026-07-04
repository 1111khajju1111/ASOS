import { useEffect, useMemo, useState } from 'react';
import { LineChart, Line, XAxis, YAxis, Tooltip, ResponsiveContainer, CartesianGrid } from 'recharts';
import Topbar from '../../components/layout/Topbar';
import Loader from '../../components/common/Loader';
import EmptyState from '../../components/common/EmptyState';
import Modal from '../../components/common/Modal';
import ErrorBanner from '../../components/common/ErrorBanner';
import { analyticsApi } from '../../api/analytics';

const MODULE_COLOR = 'var(--analytics)';

export default function AnalyticsPage() {
  const [loading, setLoading] = useState(true);
  const [metrics, setMetrics] = useState([]);
  const [showModal, setShowModal] = useState(false);
  const [selectedMetric, setSelectedMetric] = useState(null);

  const load = async () => {
    const data = await analyticsApi.listMetrics();
    setMetrics(data);
    setLoading(false);
  };

  useEffect(() => {
    load();
  }, []);

  const metricNames = useMemo(() => [...new Set(metrics.map((m) => m.metricName))], [metrics]);

  const activeMetricName = selectedMetric || metricNames[0];

  const chartData = useMemo(() => {
    return metrics
      .filter((m) => m.metricName === activeMetricName)
      .slice()
      .reverse()
      .map((m) => ({ period: m.period, value: Number(m.metricValue) }));
  }, [metrics, activeMetricName]);

  if (loading) return <Loader />;

  return (
    <div style={{ '--module-color': MODULE_COLOR }}>
      <Topbar
        eyebrow="Analytics Agent"
        title="Analytics"
        subtitle="KPIs, growth metrics, and founder dashboard numbers"
        actions={
          <button className="btn btn-primary btn-sm" onClick={() => setShowModal(true)}>
            + Metric
          </button>
        }
      />

      <div className="app-content">
        {metrics.length === 0 ? (
          <div className="card">
            <EmptyState
              icon="▤"
              title="No metrics recorded yet"
              description="Log a KPI (revenue, signups, active users…) to start tracking growth over time."
              action={
                <button className="btn btn-primary btn-sm" onClick={() => setShowModal(true)}>
                  Record a metric
                </button>
              }
            />
          </div>
        ) : (
          <>
            <div className="card" style={{ marginBottom: 24 }}>
              <div className="flex-between" style={{ marginBottom: 16 }}>
                <div className="section-title" style={{ marginBottom: 0 }}>
                  {activeMetricName}
                </div>
                <select value={activeMetricName} onChange={(e) => setSelectedMetric(e.target.value)}>
                  {metricNames.map((name) => (
                    <option key={name} value={name}>
                      {name}
                    </option>
                  ))}
                </select>
              </div>
              <ResponsiveContainer width="100%" height={260}>
                <LineChart data={chartData}>
                  <CartesianGrid stroke="var(--border)" vertical={false} />
                  <XAxis dataKey="period" stroke="var(--ink-faint)" fontSize={12} />
                  <YAxis stroke="var(--ink-faint)" fontSize={12} />
                  <Tooltip contentStyle={{ borderRadius: 10, border: '1px solid var(--border)' }} />
                  <Line type="monotone" dataKey="value" stroke="var(--analytics)" strokeWidth={2.5} dot={{ r: 3 }} />
                </LineChart>
              </ResponsiveContainer>
            </div>

            <div className="card">
              <div className="section-title">All metrics</div>
              <table className="data-table">
                <thead>
                  <tr>
                    <th>Metric</th>
                    <th>Value</th>
                    <th>Unit</th>
                    <th>Period</th>
                  </tr>
                </thead>
                <tbody>
                  {metrics.map((m) => (
                    <tr key={m.id}>
                      <td>{m.metricName}</td>
                      <td className="mono">{Number(m.metricValue).toLocaleString()}</td>
                      <td className="text-muted">{m.unit || '—'}</td>
                      <td className="text-muted">{m.period}</td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          </>
        )}
      </div>

      {showModal && (
        <MetricModal
          onClose={() => setShowModal(false)}
          onSaved={() => {
            setShowModal(false);
            load();
          }}
        />
      )}
    </div>
  );
}

function MetricModal({ onClose, onSaved }) {
  const [form, setForm] = useState({ metricName: '', metricValue: '', unit: '', period: '' });
  const [error, setError] = useState('');
  const [submitting, setSubmitting] = useState(false);

  const handleChange = (e) => setForm({ ...form, [e.target.name]: e.target.value });

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setSubmitting(true);
    try {
      await analyticsApi.recordMetric({ ...form, metricValue: Number(form.metricValue) });
      onSaved();
    } catch (err) {
      setError(err.message);
    } finally {
      setSubmitting(false);
    }
  };

  return (
    <Modal title="Record a metric" onClose={onClose}>
      <ErrorBanner message={error} />
      <form onSubmit={handleSubmit}>
        <div className="field">
          <label>Metric name</label>
          <input name="metricName" required value={form.metricName} onChange={handleChange} placeholder="e.g. MRR, Signups, DAU" />
        </div>
        <div className="field-row">
          <div className="field">
            <label>Value</label>
            <input type="number" step="0.01" name="metricValue" required value={form.metricValue} onChange={handleChange} />
          </div>
          <div className="field">
            <label>Unit</label>
            <input name="unit" value={form.unit} onChange={handleChange} placeholder="e.g. USD, users" />
          </div>
        </div>
        <div className="field">
          <label>Period</label>
          <input type="date" name="period" required value={form.period} onChange={handleChange} />
        </div>
        <button className="btn btn-primary" style={{ width: '100%' }} disabled={submitting}>
          {submitting ? 'Saving…' : 'Record metric'}
        </button>
      </form>
    </Modal>
  );
}
