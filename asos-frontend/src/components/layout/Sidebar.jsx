import { NavLink } from 'react-router-dom';
import { useAuth } from '../../context/AuthContext';

const NAV_ITEMS = [
  { to: '/', label: 'Dashboard', icon: '◆', color: 'var(--brand)', end: true },
  { to: '/finance', label: 'Finance', icon: '$', color: 'var(--finance)' },
  { to: '/hiring', label: 'Hiring', icon: '◎', color: 'var(--hiring)' },
  { to: '/legal', label: 'Legal', icon: '§', color: 'var(--legal)' },
  { to: '/marketing', label: 'Marketing', icon: '▲', color: 'var(--marketing)' },
  { to: '/analytics', label: 'Analytics', icon: '▤', color: 'var(--analytics)' },
  { to: '/approvals', label: 'Approvals', icon: '✓', color: 'var(--warn)' },
];

export default function Sidebar() {
  const { founder, logout } = useAuth();

  return (
    <aside className="sidebar">
      <div className="sidebar-brand">
        <div className="sidebar-brand-mark">A</div>
        <div>
          <div className="sidebar-brand-text">ASOS</div>
          <div className="sidebar-brand-sub">Startup OS</div>
        </div>
      </div>

      <nav className="sidebar-nav">
        {NAV_ITEMS.map((item) => (
          <NavLink
            key={item.to}
            to={item.to}
            end={item.end}
            className={({ isActive }) => `sidebar-link${isActive ? ' active' : ''}`}
          >
            <span className="sidebar-dot" style={{ '--dot-color': item.color }} />
            {item.label}
          </NavLink>
        ))}
      </nav>

      <div className="sidebar-footer">
        <div className="sidebar-founder">
          <strong>{founder?.fullName}</strong>
          {founder?.companyName || founder?.email}
        </div>
        <button className="sidebar-logout" onClick={logout}>
          Sign out
        </button>
      </div>
    </aside>
  );
}
