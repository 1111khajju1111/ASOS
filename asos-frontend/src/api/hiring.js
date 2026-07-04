import { api } from './client';

export const hiringApi = {
  listJobs: () => api.get('/hiring/jobs'),
  createJob: (payload) => api.post('/hiring/jobs', payload),
  updateJob: (id, payload) => api.put(`/hiring/jobs/${id}`, payload),
  deleteJob: (id) => api.del(`/hiring/jobs/${id}`),
  listCandidates: (jobId) => api.get(`/hiring/jobs/${jobId}/candidates`),
  addCandidate: (jobId, payload) => api.post(`/hiring/jobs/${jobId}/candidates`, payload),
  updateCandidate: (jobId, candidateId, payload) =>
    api.put(`/hiring/jobs/${jobId}/candidates/${candidateId}`, payload),
};
