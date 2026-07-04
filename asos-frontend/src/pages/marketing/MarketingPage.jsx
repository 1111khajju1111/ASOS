import { useEffect, useState } from 'react';
import Topbar from '../../components/layout/Topbar';
import Loader from '../../components/common/Loader';
import EmptyState from '../../components/common/EmptyState';
import Modal from '../../components/common/Modal';
import ErrorBanner from '../../components/common/ErrorBanner';
import StatusBadge from '../../components/common/StatusBadge';
import { marketingApi } from '../../api/marketing';

const MODULE_COLOR = 'var(--marketing)';
const CAMPAIGN_TYPES = ['EMAIL', 'SOCIAL_MEDIA', 'PRODUCT_LAUNCH', 'GTM_STRATEGY'];
const CAMPAIGN_STATUSES = ['PLANNED', 'ACTIVE', 'COMPLETED', 'CANCELLED'];

export default function MarketingPage() {
  const [loading, setLoading] = useState(true);
  const [campaigns, setCampaigns] = useState([]);
  const [showModal, setShowModal] = useState(false);
  const [editing, setEditing] = useState(null);

  const load = async () => {
    const data = await marketingApi.listCampaigns();
    setCampaigns(data);
    setLoading(false);
  };

  useEffect(() => {
    load();
  }, []);

  const handleDelete = async (id) => {
    await marketingApi.deleteCampaign(id);
    load();
  };

  if (loading) return <Loader />;

  return (
    <div style={{ '--module-color': MODULE_COLOR }}>
      <Topbar
        eyebrow="Marketing & GTM Agent"
        title="Marketing"
        subtitle="Campaigns, product launches, and go-to-market plans"
        actions={
          <button className="btn btn-primary btn-sm" onClick={() => setShowModal(true)}>
            + Campaign
          </button>
        }
      />

      <div className="app-content">
        <div className="card">
          <div className="section-title">Campaigns</div>
          {campaigns.length === 0 ? (
            <EmptyState
              icon="▲"
              title="No campaigns yet"
              description="Plan an email, social, or launch campaign to fill your GTM calendar."
              action={
                <button className="btn btn-primary btn-sm" onClick={() => setShowModal(true)}>
                  Plan a campaign
                </button>
              }
            />
          ) : (
            <table className="data-table">
              <thead>
                <tr>
                  <th>Title</th>
                  <th>Type</th>
                  <th>Scheduled</th>
                  <th>Status</th>
                  <th></th>
                </tr>
              </thead>
              <tbody>
                {campaigns.map((c) => (
                  <tr key={c.id}>
                    <td>{c.title}</td>
                    <td className="text-muted">{c.type.replace('_', ' ')}</td>
                    <td className="text-muted">{c.scheduledDate || '—'}</td>
                    <td>
                      <StatusBadge status={c.status} />
                    </td>
                    <td>
                      <div className="tag-row">
                        <button className="btn btn-ghost btn-sm" onClick={() => setEditing(c)}>
                          Edit
                        </button>
                        <button className="btn btn-danger btn-sm" onClick={() => handleDelete(c.id)}>
                          Delete
                        </button>
                      </div>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          )}
        </div>
      </div>

      {(showModal || editing) && (
        <CampaignModal
          campaign={editing}
          onClose={() => {
            setShowModal(false);
            setEditing(null);
          }}
          onSaved={() => {
            setShowModal(false);
            setEditing(null);
            load();
          }}
        />
      )}
    </div>
  );
}

function CampaignModal({ campaign, onClose, onSaved }) {
  const isEdit = Boolean(campaign);
  const [form, setForm] = useState(
    campaign || { title: '', type: 'EMAIL', status: 'PLANNED', content: '', scheduledDate: '' }
  );
  const [error, setError] = useState('');
  const [submitting, setSubmitting] = useState(false);

  const handleChange = (e) => setForm({ ...form, [e.target.name]: e.target.value });

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setSubmitting(true);
    try {
      if (isEdit) {
        await marketingApi.updateCampaign(campaign.id, form);
      } else {
        await marketingApi.createCampaign(form);
      }
      onSaved();
    } catch (err) {
      setError(err.message);
    } finally {
      setSubmitting(false);
    }
  };

  return (
    <Modal title={isEdit ? 'Edit campaign' : 'New campaign'} onClose={onClose}>
      <ErrorBanner message={error} />
      <form onSubmit={handleSubmit}>
        <div className="field">
          <label>Title</label>
          <input name="title" required value={form.title} onChange={handleChange} placeholder="e.g. Product Hunt launch" />
        </div>
        <div className="field-row">
          <div className="field">
            <label>Type</label>
            <select name="type" value={form.type} onChange={handleChange}>
              {CAMPAIGN_TYPES.map((t) => (
                <option key={t} value={t}>
                  {t.replace('_', ' ')}
                </option>
              ))}
            </select>
          </div>
          <div className="field">
            <label>Status</label>
            <select name="status" value={form.status} onChange={handleChange}>
              {CAMPAIGN_STATUSES.map((s) => (
                <option key={s} value={s}>
                  {s}
                </option>
              ))}
            </select>
          </div>
        </div>
        <div className="field">
          <label>Scheduled date</label>
          <input type="date" name="scheduledDate" value={form.scheduledDate || ''} onChange={handleChange} />
        </div>
        <div className="field">
          <label>Content</label>
          <textarea name="content" rows={4} value={form.content} onChange={handleChange} placeholder="Campaign copy, notes, or brief" />
        </div>
        <button className="btn btn-primary" style={{ width: '100%' }} disabled={submitting}>
          {submitting ? 'Saving…' : isEdit ? 'Save changes' : 'Create campaign'}
        </button>
      </form>
    </Modal>
  );
}
