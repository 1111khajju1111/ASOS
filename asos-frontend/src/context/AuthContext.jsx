import { createContext, useContext, useEffect, useState, useCallback } from 'react';
import { authApi } from '../api/auth';

const AuthContext = createContext(null);

export function AuthProvider({ children }) {
  const [founder, setFounder] = useState(null);
  const [loading, setLoading] = useState(true);

  // On load, ask the backend if our session cookie is still valid.
  const loadProfile = useCallback(async () => {
    try {
      const profile = await authApi.me();
      setFounder(profile);
    } catch {
      setFounder(null);
    } finally {
      setLoading(false);
    }
  }, []);

  useEffect(() => {
    loadProfile();
  }, [loadProfile]);

  const login = async (payload) => {
    const res = await authApi.login(payload);
    setFounder({
      id: res.founderId,
      fullName: res.fullName,
      email: res.email,
      companyName: res.companyName,
    });
    return res;
  };

  const register = async (payload) => {
    const res = await authApi.register(payload);
    setFounder({
      id: res.founderId,
      fullName: res.fullName,
      email: res.email,
      companyName: res.companyName,
    });
    return res;
  };

  const logout = async () => {
    try {
      await authApi.logout();
    } finally {
      setFounder(null);
    }
  };

  return (
    <AuthContext.Provider value={{ founder, loading, login, register, logout }}>
      {children}
    </AuthContext.Provider>
  );
}

export function useAuth() {
  const ctx = useContext(AuthContext);
  if (!ctx) throw new Error('useAuth must be used within AuthProvider');
  return ctx;
}
