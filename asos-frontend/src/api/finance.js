import { api } from './client';

export const financeApi = {
  listRecords: () => api.get('/finance/records'),
  createRecord: (payload) => api.post('/finance/records', payload),
  listExpenses: () => api.get('/finance/expenses'),
  addExpense: (payload) => api.post('/finance/expenses', payload),
  deleteExpense: (id) => api.del(`/finance/expenses/${id}`),
};
