import { api } from './client';

export const legalApi = {
  listDocuments: () => api.get('/legal/documents'),
  createDocument: (payload) => api.post('/legal/documents', payload),
  updateDocument: (id, payload) => api.put(`/legal/documents/${id}`, payload),
  deleteDocument: (id) => api.del(`/legal/documents/${id}`),
};
