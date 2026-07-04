import { useEffect, useState } from 'react';
import Topbar from '../../components/layout/Topbar';
import Loader from '../../components/common/Loader';
import EmptyState from '../../components/common/EmptyState';
import Modal from '../../components/common/Modal';
import ErrorBanner from '../../components/common/ErrorBanner';
import StatusBadge from '../../components/common/StatusBadge';
import { hiringApi } from '../../api/hiring';

const MODULE_COLOR = 'var(--hiring)';
const JOB_STATUSES = ['DRAFT', 'OPEN', 'CLOSED'];
const CANDIDATE_STATUSES = ['APPLIED', 'SCREENING', 'INTERVIEW', 'OFFERED', 'HIRED', 'REJECTED'];

export default function HiringPage() {
  const [loading, setLoading] = useState(true);
  const [jobs, setJobs] = useState([]);
  const [showJobModal, setShowJobModal] = useState(false);
  const [activeJob, setActiveJob] = useState(null);

  const load = async () => {
    const data = await hiringApi.listJobs();
    setJobs(data);
    setLoading(false);
  };

  useEffect(() => {
    load();
  }, []);

  const handleDeleteJob = async (id) => {
    await hiringApi.deleteJob(id);
    load();
  };

  if (loading) return <Loader />;

  return (
    <div style={{ '--module-color': MODULE_COLOR }}>
      <Topbar
        eyebrow="Hiring Agent"
        title="Hiring"
        subtitle="Job postings, candidate pipeline, and recruitment tracking"
        actions={
          <button className="btn btn-primary btn-sm" onClick={() => setShowJobModal(true)}>
            + Job posting
          </button>
        }
      />

      <div className="app-content">
        <div className="card">
          <div className="section-title">Job postings</div>
          {jobs.length === 0 ? (
            <EmptyState
              icon="◎"
              title="No job postings yet"
              description="Create a role to start tracking candidates against it."
              action={
                <button className="btn btn-primary btn-sm" onClick={() => setShowJobModal(true)}>
                  Create job posting
                </button>
              }
            />
          ) : (
            <table className="data-table">
              <thead>
                <tr>
                  <th>Title</th>
                  <th>Department</th>
                  <th>Status</th>
                  <th>Candidates</th>
                  <th></th>
                </tr>
              </thead>
              <tbody>
                {jobs.map((j) => (
                  <tr key={j.id}>
                    <td>{j.title}</td>
                    <td className="text-muted">{j.department || '—'}</td>
                    <td>
                      <StatusBadge status={j.status} />
                    </td>
                    <td className="mono">{j.candidateCount}</td>
                    <td>
                      <div className="tag-row">
                        <button className="btn btn-ghost btn-sm" onClick={() => setActiveJob(j)}>
                          Candidates
                        </button>
                        <button className="btn btn-danger btn-sm" onClick={() => handleDeleteJob(j.id)}>
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

      {showJobModal && (
        <JobModal
          onClose={() => setShowJobModal(false)}
          onSaved={() => {
            setShowJobModal(false);
            load();
          }}
        />
      )}

      {activeJob && (
        <CandidatesModal
          job={activeJob}
          onClose={() => setActiveJob(null)}
          onChanged={load}
        />
      )}
    </div>
  );
}

function JobModal({ onClose, onSaved }) {
  const [form, setForm] = useState({ title: '', department: '', description: '', status: 'DRAFT' });
  const [error, setError] = useState('');
  const [submitting, setSubmitting] = useState(false);

  const handleChange = (e) => setForm({ ...form, [e.target.name]: e.target.value });

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setSubmitting(true);
    try {
      await hiringApi.createJob(form);
      onSaved();
    } catch (err) {
      setError(err.message);
    } finally {
      setSubmitting(false);
    }
  };

  return (
    <Modal title="New job posting" onClose={onClose}>
      <ErrorBanner message={error} />
      <form onSubmit={handleSubmit}>
        <div className="field">
          <label>Title</label>
          <input name="title" required value={form.title} onChange={handleChange} placeholder="e.g. Founding Engineer" />
        </div>
        <div className="field">
          <label>Department</label>
          <input name="department" value={form.department} onChange={handleChange} placeholder="e.g. Engineering" />
        </div>
        <div className="field">
          <label>Description</label>
          <textarea name="description" rows={3} value={form.description} onChange={handleChange} />
        </div>
        <div className="field">
          <label>Status</label>
          <select name="status" value={form.status} onChange={handleChange}>
            {JOB_STATUSES.map((s) => (
              <option key={s} value={s}>
                {s}
              </option>
            ))}
          </select>
        </div>
        <button className="btn btn-primary" style={{ width: '100%' }} disabled={submitting}>
          {submitting ? 'Saving…' : 'Create job'}
        </button>
      </form>
    </Modal>
  );
}

function CandidatesModal({ job, onClose, onChanged }) {
  const [candidates, setCandidates] = useState([]);
  const [loading, setLoading] = useState(true);
  const [showAdd, setShowAdd] = useState(false);
  const [error, setError] = useState('');

  const load = async () => {
    const data = await hiringApi.listCandidates(job.id);
    setCandidates(data);
    setLoading(false);
  };

  useEffect(() => {
    load();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [job.id]);

  const updateStatus = async (candidate, status) => {
    try {
      await hiringApi.updateCandidate(job.id, candidate.id, { ...candidate, status });
      load();
      onChanged();
    } catch (err) {
      setError(err.message);
    }
  };

  return (
    <Modal title={`Candidates — ${job.title}`} onClose={onClose}>
      <ErrorBanner message={error} />

      {!showAdd && (
        <button className="btn btn-ghost btn-sm" style={{ marginBottom: 16 }} onClick={() => setShowAdd(true)}>
          + Add candidate
        </button>
      )}

      {showAdd && (
        <AddCandidateForm
          jobId={job.id}
          onCancel={() => setShowAdd(false)}
          onAdded={() => {
            setShowAdd(false);
            load();
            onChanged();
          }}
        />
      )}

      {loading ? (
        <Loader />
      ) : candidates.length === 0 ? (
        <p className="text-faint">No candidates yet for this role.</p>
      ) : (
        <div style={{ display: 'flex', flexDirection: 'column', gap: 10 }}>
          {candidates.map((c) => (
            <div key={c.id} className="card" style={{ padding: 14 }}>
              <div className="flex-between">
                <div>
                  <strong>{c.name}</strong>
                  <div className="text-faint">{c.email}</div>
                </div>
                <select value={c.status} onChange={(e) => updateStatus(c, e.target.value)}>
                  {CANDIDATE_STATUSES.map((s) => (
                    <option key={s} value={s}>
                      {s}
                    </option>
                  ))}
                </select>
              </div>
            </div>
          ))}
        </div>
      )}
    </Modal>
  );
}

function AddCandidateForm({ jobId, onCancel, onAdded }) {
  const [form, setForm] = useState({ name: '', email: '', resumeUrl: '', status: 'APPLIED', notes: '' });
  const [error, setError] = useState('');
  const [submitting, setSubmitting] = useState(false);

  const handleChange = (e) => setForm({ ...form, [e.target.name]: e.target.value });

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setSubmitting(true);
    try {
      await hiringApi.addCandidate(jobId, form);
      onAdded();
    } catch (err) {
      setError(err.message);
    } finally {
      setSubmitting(false);
    }
  };

  return (
    <form onSubmit={handleSubmit} className="card" style={{ marginBottom: 16, padding: 16 }}>
      <ErrorBanner message={error} />
      <div className="field-row">
        <div className="field">
          <label>Name</label>
          <input name="name" required value={form.name} onChange={handleChange} />
        </div>
        <div className="field">
          <label>Email</label>
          <input type="email" name="email" required value={form.email} onChange={handleChange} />
        </div>
      </div>
      <div className="field">
        <label>Resume URL</label>
        <input name="resumeUrl" value={form.resumeUrl} onChange={handleChange} placeholder="Optional" />
      </div>
      <div className="tag-row">
        <button className="btn btn-primary btn-sm" disabled={submitting}>
          {submitting ? 'Adding…' : 'Add candidate'}
        </button>
        <button type="button" className="btn btn-ghost btn-sm" onClick={onCancel}>
          Cancel
        </button>
      </div>
    </form>
  );
}
