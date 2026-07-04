import { api } from './client';

export const approvalsApi = {
  list: (pendingOnly) => api.get(`/approvals${pendingOnly ? '?pendingOnly=true' : ''}`),
  create: (payload) => api.post('/approvals', payload),
  decide: (id, status) => api.patch(`/approvals/${id}/decision`, { status }),
};
