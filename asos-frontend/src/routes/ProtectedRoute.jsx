import { Navigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import Loader from '../components/common/Loader';
import AppShell from '../components/layout/AppShell';

export default function ProtectedRoute({ children }) {
  const { founder, loading } = useAuth();

  if (loading) return <Loader label="Loading ASOS…" />;
  if (!founder) return <Navigate to="/login" replace />;

  return <AppShell>{children}</AppShell>;
}
