export default function Topbar({ eyebrow, title, subtitle, actions }) {
  return (
    <div className="topbar">
      <div>
        {eyebrow && <div className="topbar-eyebrow">{eyebrow}</div>}
        <div className="topbar-title">{title}</div>
        {subtitle && <div className="topbar-sub">{subtitle}</div>}
      </div>
      {actions && <div>{actions}</div>}
    </div>
  );
}
