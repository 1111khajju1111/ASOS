import { api } from './client';

export const marketingApi = {
  listCampaigns: () => api.get('/marketing/campaigns'),
  createCampaign: (payload) => api.post('/marketing/campaigns', payload),
  updateCampaign: (id, payload) => api.put(`/marketing/campaigns/${id}`, payload),
  deleteCampaign: (id) => api.del(`/marketing/campaigns/${id}`),
};
