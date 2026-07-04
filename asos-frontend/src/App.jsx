import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import { Suspense, lazy } from 'react';
import { AuthProvider } from './context/AuthContext';
import ProtectedRoute from './routes/ProtectedRoute';

const AgentField = lazy(() => import('./components/background/AgentField'));

import LoginPage from './pages/auth/LoginPage';
import RegisterPage from './pages/auth/RegisterPage';
import DashboardPage from './pages/dashboard/DashboardPage';
import FinancePage from './pages/finance/FinancePage';
import HiringPage from './pages/hiring/HiringPage';
import LegalPage from './pages/legal/LegalPage';
import MarketingPage from './pages/marketing/MarketingPage';
import AnalyticsPage from './pages/analytics/AnalyticsPage';
import ApprovalsPage from './pages/approvals/ApprovalsPage';

export default function App() {
  return (
    <BrowserRouter>
      <Suspense fallback={null}>
        <AgentField />
      </Suspense>
      <AuthProvider>
        <Routes>
          <Route path="/login" element={<LoginPage />} />
          <Route path="/register" element={<RegisterPage />} />

          <Route path="/" element={<ProtectedRoute><DashboardPage /></ProtectedRoute>} />
          <Route path="/finance" element={<ProtectedRoute><FinancePage /></ProtectedRoute>} />
          <Route path="/hiring" element={<ProtectedRoute><HiringPage /></ProtectedRoute>} />
          <Route path="/legal" element={<ProtectedRoute><LegalPage /></ProtectedRoute>} />
          <Route path="/marketing" element={<ProtectedRoute><MarketingPage /></ProtectedRoute>} />
          <Route path="/analytics" element={<ProtectedRoute><AnalyticsPage /></ProtectedRoute>} />
          <Route path="/approvals" element={<ProtectedRoute><ApprovalsPage /></ProtectedRoute>} />

          <Route path="*" element={<Navigate to="/" replace />} />
        </Routes>
      </AuthProvider>
    </BrowserRouter>
  );
}
