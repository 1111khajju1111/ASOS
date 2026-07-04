const STATUS_MAP = {
  // job / general
  OPEN: 'success',
  DRAFT: 'neutral',
  CLOSED: 'neutral',
  ACTIVE: 'success',
  PLANNED: 'brand',
  COMPLETED: 'success',
  CANCELLED: 'neutral',
  // candidate
  APPLIED: 'brand',
  SCREENING: 'brand',
  INTERVIEW: 'warn',
  OFFERED: 'warn',
  HIRED: 'success',
  REJECTED: 'danger',
  // legal
  FINAL: 'success',
  ARCHIVED: 'neutral',
  // approvals
  PENDING: 'warn',
  APPROVED: 'success',
};

export default function StatusBadge({ status }) {
  const variant = STATUS_MAP[status] || 'neutral';
  return <span className={`badge badge-${variant}`}>{formatLabel(status)}</span>;
}

function formatLabel(status) {
  if (!status) return '';
  return status
    .toLowerCase()
    .split('_')
    .map((w) => w[0].toUpperCase() + w.slice(1))
    .join(' ');
}
