import { api } from './client';

export const analyticsApi = {
  listMetrics: (metricName) =>
    api.get(`/analytics/metrics${metricName ? `?metricName=${encodeURIComponent(metricName)}` : ''}`),
  recordMetric: (payload) => api.post('/analytics/metrics', payload),
};
