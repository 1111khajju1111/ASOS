export default function ErrorBanner({ message }) {
  if (!message) return null;
  return <div className="auth-error">{message}</div>;
}
