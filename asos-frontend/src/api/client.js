const BASE_URL = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080/api';

async function request(path, { method = 'GET', body } = {}) {
  const headers = { 'Content-Type': 'application/json' };

  const response = await fetch(`${BASE_URL}${path}`, {
    method,
    headers,
    credentials: 'include', // send the ASOS_SESSION cookie on every request
    body: body !== undefined ? JSON.stringify(body) : undefined,
  });

  if (response.status === 204) return null;

  let data = null;
  const text = await response.text();
  if (text) {
    try {
      data = JSON.parse(text);
    } catch {
      data = text;
    }
  }

  if (!response.ok) {
    const message = (data && data.message) || `Request failed with status ${response.status}`;
    const error = new Error(message);
    error.status = response.status;
    error.fieldErrors = data && data.fieldErrors;
    throw error;
  }

  return data;
}

export const api = {
  get: (path, opts) => request(path, { ...opts, method: 'GET' }),
  post: (path, body, opts) => request(path, { ...opts, method: 'POST', body }),
  put: (path, body, opts) => request(path, { ...opts, method: 'PUT', body }),
  patch: (path, body, opts) => request(path, { ...opts, method: 'PATCH', body }),
  del: (path, opts) => request(path, { ...opts, method: 'DELETE' }),
};
