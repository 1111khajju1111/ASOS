import { useEffect, useState } from 'react';
import Topbar from '../../components/layout/Topbar';
import Loader from '../../components/common/Loader';
import EmptyState from '../../components/common/EmptyState';
import Modal from '../../components/common/Modal';
import ErrorBanner from '../../components/common/ErrorBanner';
import StatusBadge from '../../components/common/StatusBadge';
import { legalApi } from '../../api/legal';

const MODULE_COLOR = 'var(--legal)';
const DOC_TYPES = ['NDA', 'CONTRACT', 'COMPLIANCE_NOTE', 'AGREEMENT_TEMPLATE'];
const DOC_STATUSES = ['DRAFT', 'FINAL', 'ARCHIVED'];

export default function LegalPage() {
  const [loading, setLoading] = useState(true);
  const [documents, setDocuments] = useState([]);
  const [showModal, setShowModal] = useState(false);
  const [editing, setEditing] = useState(null);

  const load = async () => {
    const data = await legalApi.listDocuments();
    setDocuments(data);
    setLoading(false);
  };

  useEffect(() => {
    load();
  }, []);

  const handleDelete = async (id) => {
    await legalApi.deleteDocument(id);
    load();
  };

  if (loading) return <Loader />;

  return (
    <div style={{ '--module-color': MODULE_COLOR }}>
      <Topbar
        eyebrow="Legal Agent"
        title="Legal"
        subtitle="NDAs, contracts, compliance notes and risk tracking"
        actions={
          <button className="btn btn-primary btn-sm" onClick={() => setShowModal(true)}>
            + Document
          </button>
        }
      />

      <div className="app-content">
        <div className="card">
          <div className="section-title">Documents</div>
          {documents.length === 0 ? (
            <EmptyState
              icon="§"
              title="No legal documents yet"
              description="Draft an NDA, contract, or compliance note to keep track of risk."
              action={
                <button className="btn btn-primary btn-sm" onClick={() => setShowModal(true)}>
                  Draft a document
                </button>
              }
            />
          ) : (
            <table className="data-table">
              <thead>
                <tr>
                  <th>Title</th>
                  <th>Type</th>
                  <th>Status</th>
                  <th></th>
                </tr>
              </thead>
              <tbody>
                {documents.map((d) => (
                  <tr key={d.id}>
                    <td>{d.title}</td>
                    <td className="text-muted">{d.type.replace('_', ' ')}</td>
                    <td>
                      <StatusBadge status={d.status} />
                    </td>
                    <td>
                      <div className="tag-row">
                        <button className="btn btn-ghost btn-sm" onClick={() => setEditing(d)}>
                          Edit
                        </button>
                        <button className="btn btn-danger btn-sm" onClick={() => handleDelete(d.id)}>
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
        <DocumentModal
          document={editing}
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

function DocumentModal({ document, onClose, onSaved }) {
  const isEdit = Boolean(document);
  const [form, setForm] = useState(
    document || { title: '', type: 'NDA', status: 'DRAFT', content: '', riskNotes: '' }
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
        await legalApi.updateDocument(document.id, form);
      } else {
        await legalApi.createDocument(form);
      }
      onSaved();
    } catch (err) {
      setError(err.message);
    } finally {
      setSubmitting(false);
    }
  };

  return (
    <Modal title={isEdit ? 'Edit document' : 'New legal document'} onClose={onClose}>
      <ErrorBanner message={error} />
      <form onSubmit={handleSubmit}>
        <div className="field">
          <label>Title</label>
          <input name="title" required value={form.title} onChange={handleChange} placeholder="e.g. Mutual NDA — Acme Corp" />
        </div>
        <div className="field-row">
          <div className="field">
            <label>Type</label>
            <select name="type" value={form.type} onChange={handleChange}>
              {DOC_TYPES.map((t) => (
                <option key={t} value={t}>
                  {t.replace('_', ' ')}
                </option>
              ))}
            </select>
          </div>
          <div className="field">
            <label>Status</label>
            <select name="status" value={form.status} onChange={handleChange}>
              {DOC_STATUSES.map((s) => (
                <option key={s} value={s}>
                  {s}
                </option>
              ))}
            </select>
          </div>
        </div>
        <div className="field">
          <label>Content</label>
          <textarea name="content" rows={4} value={form.content} onChange={handleChange} />
        </div>
        <div className="field">
          <label>Risk notes</label>
          <textarea name="riskNotes" rows={2} value={form.riskNotes} onChange={handleChange} placeholder="Optional" />
        </div>
        <button className="btn btn-primary" style={{ width: '100%' }} disabled={submitting}>
          {submitting ? 'Saving…' : isEdit ? 'Save changes' : 'Create document'}
        </button>
      </form>
    </Modal>
  );
}
