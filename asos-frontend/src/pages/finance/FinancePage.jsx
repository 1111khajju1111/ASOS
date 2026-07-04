import { useEffect, useState } from 'react';
import Topbar from '../../components/layout/Topbar';
import Loader from '../../components/common/Loader';
import EmptyState from '../../components/common/EmptyState';
import Modal from '../../components/common/Modal';
import ErrorBanner from '../../components/common/ErrorBanner';
import { financeApi } from '../../api/finance';

const MODULE_COLOR = 'var(--finance)';

export default function FinancePage() {
  const [loading, setLoading] = useState(true);
  const [records, setRecords] = useState([]);
  const [expenses, setExpenses] = useState([]);
  const [showRecordModal, setShowRecordModal] = useState(false);
  const [showExpenseModal, setShowExpenseModal] = useState(false);

  const load = async () => {
    const [r, e] = await Promise.all([financeApi.listRecords(), financeApi.listExpenses()]);
    setRecords(r);
    setExpenses(e);
    setLoading(false);
  };

  useEffect(() => {
    load();
  }, []);

  const handleDeleteExpense = async (id) => {
    await financeApi.deleteExpense(id);
    load();
  };

  if (loading) return <Loader />;

  return (
    <div style={{ '--module-color': MODULE_COLOR }}>
      <Topbar
        eyebrow="Finance Agent"
        title="Finance"
        subtitle="Budget, burn rate, runway and expense tracking"
        actions={
          <div className="tag-row">
            <button className="btn btn-ghost btn-sm" onClick={() => setShowExpenseModal(true)}>
              + Expense
            </button>
            <button className="btn btn-primary btn-sm" onClick={() => setShowRecordModal(true)}>
              + Finance record
            </button>
          </div>
        }
      />

      <div className="app-content">
        <div className="card" style={{ marginBottom: 24 }}>
          <div className="section-title">Budget & runway history</div>
          {records.length === 0 ? (
            <EmptyState
              icon="$"
              title="No finance records yet"
              description="Log a period's budget, expenses, and cash balance to see burn rate and runway."
              action={
                <button className="btn btn-primary btn-sm" onClick={() => setShowRecordModal(true)}>
                  Add finance record
                </button>
              }
            />
          ) : (
            <table className="data-table">
              <thead>
                <tr>
                  <th>Period</th>
                  <th>Budget</th>
                  <th>Expenses</th>
                  <th>Cash balance</th>
                  <th>Burn rate / mo</th>
                  <th>Runway</th>
                </tr>
              </thead>
              <tbody>
                {records.map((r) => (
                  <tr key={r.id}>
                    <td>
                      {r.periodStart} → {r.periodEnd}
                    </td>
                    <td className="mono">${Number(r.totalBudget).toLocaleString()}</td>
                    <td className="mono">${Number(r.totalExpenses).toLocaleString()}</td>
                    <td className="mono">${Number(r.cashBalance).toLocaleString()}</td>
                    <td className="mono">${Number(r.burnRate).toLocaleString()}</td>
                    <td className="mono">{r.runwayMonths >= 0 ? `${r.runwayMonths} mo` : '∞'}</td>
                  </tr>
                ))}
              </tbody>
            </table>
          )}
        </div>

        <div className="card">
          <div className="section-title">Expenses</div>
          {expenses.length === 0 ? (
            <EmptyState
              icon="◦"
              title="No expenses logged"
              description="Track spend by category to feed your burn rate calculations."
            />
          ) : (
            <table className="data-table">
              <thead>
                <tr>
                  <th>Date</th>
                  <th>Category</th>
                  <th>Description</th>
                  <th>Amount</th>
                  <th></th>
                </tr>
              </thead>
              <tbody>
                {expenses.map((e) => (
                  <tr key={e.id}>
                    <td>{e.expenseDate}</td>
                    <td>{e.category}</td>
                    <td className="text-muted">{e.description || '—'}</td>
                    <td className="mono">${Number(e.amount).toLocaleString()}</td>
                    <td>
                      <button className="btn btn-danger btn-sm" onClick={() => handleDeleteExpense(e.id)}>
                        Delete
                      </button>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          )}
        </div>
      </div>

      {showRecordModal && (
        <RecordModal
          onClose={() => setShowRecordModal(false)}
          onSaved={() => {
            setShowRecordModal(false);
            load();
          }}
        />
      )}

      {showExpenseModal && (
        <ExpenseModal
          onClose={() => setShowExpenseModal(false)}
          onSaved={() => {
            setShowExpenseModal(false);
            load();
          }}
        />
      )}
    </div>
  );
}

function RecordModal({ onClose, onSaved }) {
  const [form, setForm] = useState({
    periodStart: '',
    periodEnd: '',
    totalBudget: '',
    totalExpenses: '',
    cashBalance: '',
  });
  const [error, setError] = useState('');
  const [submitting, setSubmitting] = useState(false);

  const handleChange = (e) => setForm({ ...form, [e.target.name]: e.target.value });

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setSubmitting(true);
    try {
      await financeApi.createRecord({
        ...form,
        totalBudget: Number(form.totalBudget),
        totalExpenses: Number(form.totalExpenses),
        cashBalance: Number(form.cashBalance),
      });
      onSaved();
    } catch (err) {
      setError(err.message);
    } finally {
      setSubmitting(false);
    }
  };

  return (
    <Modal title="New finance record" onClose={onClose}>
      <ErrorBanner message={error} />
      <form onSubmit={handleSubmit}>
        <div className="field-row">
          <div className="field">
            <label>Period start</label>
            <input type="date" name="periodStart" required value={form.periodStart} onChange={handleChange} />
          </div>
          <div className="field">
            <label>Period end</label>
            <input type="date" name="periodEnd" required value={form.periodEnd} onChange={handleChange} />
          </div>
        </div>
        <div className="field">
          <label>Total budget ($)</label>
          <input type="number" step="0.01" min="0" name="totalBudget" required value={form.totalBudget} onChange={handleChange} />
        </div>
        <div className="field">
          <label>Total expenses ($)</label>
          <input type="number" step="0.01" min="0" name="totalExpenses" required value={form.totalExpenses} onChange={handleChange} />
        </div>
        <div className="field">
          <label>Cash balance ($)</label>
          <input type="number" step="0.01" min="0" name="cashBalance" required value={form.cashBalance} onChange={handleChange} />
        </div>
        <button className="btn btn-primary" style={{ width: '100%' }} disabled={submitting}>
          {submitting ? 'Saving…' : 'Save record'}
        </button>
      </form>
    </Modal>
  );
}

function ExpenseModal({ onClose, onSaved }) {
  const [form, setForm] = useState({ category: '', description: '', amount: '', expenseDate: '' });
  const [error, setError] = useState('');
  const [submitting, setSubmitting] = useState(false);

  const handleChange = (e) => setForm({ ...form, [e.target.name]: e.target.value });

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setSubmitting(true);
    try {
      await financeApi.addExpense({ ...form, amount: Number(form.amount) });
      onSaved();
    } catch (err) {
      setError(err.message);
    } finally {
      setSubmitting(false);
    }
  };

  return (
    <Modal title="Log an expense" onClose={onClose}>
      <ErrorBanner message={error} />
      <form onSubmit={handleSubmit}>
        <div className="field">
          <label>Category</label>
          <input name="category" required value={form.category} onChange={handleChange} placeholder="e.g. Software, Payroll" />
        </div>
        <div className="field">
          <label>Description</label>
          <input name="description" value={form.description} onChange={handleChange} placeholder="Optional" />
        </div>
        <div className="field-row">
          <div className="field">
            <label>Amount ($)</label>
            <input type="number" step="0.01" min="0.01" name="amount" required value={form.amount} onChange={handleChange} />
          </div>
          <div className="field">
            <label>Date</label>
            <input type="date" name="expenseDate" required value={form.expenseDate} onChange={handleChange} />
          </div>
        </div>
        <button className="btn btn-primary" style={{ width: '100%' }} disabled={submitting}>
          {submitting ? 'Saving…' : 'Add expense'}
        </button>
      </form>
    </Modal>
  );
}
