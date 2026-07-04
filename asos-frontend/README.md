# ASOS Frontend — Autonomous Startup Operating System

React + Vite dashboard for ASOS. Talks to the Spring Boot backend (manual `HttpSession` cookie auth, no Spring Security, no JWT).

## Stack

- React 18 + Vite
- React Router v6
- **Three.js + @react-three/fiber + @react-three/drei** — the 3D ambient background
- **Framer Motion** — page transitions, modal animation, card entrance
- Recharts (Analytics KPI chart)
- Plain CSS with a design-token system (`src/index.css`) — no Tailwind/UI kit

## Design system

### The multi-agent color rail
Each backend "agent" module gets its own accent color, used consistently for that
module's nav dot, page header, primary buttons, and card borders:

| Module | Color |
|---|---|
| Shell / CEO agent | Violet `#6C4CF1` |
| Finance | Emerald `#0EA372` |
| Hiring | Cobalt `#2F6FF0` |
| Legal | Orchid `#B0459A` |
| Marketing | Orange `#FF6B35` |
| Analytics | Cyan `#00AEC7` |
| Approvals | Amber `#F5A623` |

Type: **Space Grotesk** (display/headings), **Inter** (body/UI), **JetBrains Mono** (numbers — money, KPIs, dates) — loaded via Google Fonts in `index.html`.

### The 3D signature: a living multi-agent field
`src/components/background/AgentField.jsx` renders a fixed, full-viewport Three.js scene behind the entire app: a glowing violet icosahedron (the CEO agent core) orbited by six smaller spheres, one per module, each colored to match that module's accent and drifting on its own elliptical path. It's a literal picture of "specialized AI agents coordinated by a central orchestration layer" — the same line from the product brief — rendered as ambient motion rather than illustrated as a diagram.

The scene sits at low opacity behind glassmorphic surfaces (frosted, semi-transparent cards/sidebar/modals with `backdrop-filter: blur(...)`), so it reads as atmosphere rather than a distraction from the data. It's lazy-loaded (`React.lazy`) so Three.js doesn't block the initial page render — it streams in just after the functional UI is interactive.

## Setup

```bash
npm install
cp .env.example .env
# edit .env if your backend isn't on localhost:8080
npm run dev
```

App runs at `http://localhost:5173`. Make sure the backend is running at the URL in `VITE_API_BASE_URL` (default `http://localhost:8080/api`) and that its CORS config (`app.cors.allowed-origins` in the backend's `application.yml`) includes `http://localhost:5173`.

## Structure

```
src/
├── api/              # fetch wrappers, one file per backend module
├── components/
│   ├── background/
│   │   └── AgentField.jsx   # the 3D ambient scene
│   ├── layout/       # Sidebar, Topbar, AppShell (handles route-transition animation)
│   └── common/       # Modal, Loader, EmptyState, StatusBadge, ErrorBanner, FadeIn
├── context/
│   └── AuthContext.jsx   # founder session; validated via GET /api/auth/me on load
├── pages/
│   ├── auth/         # Login, Register
│   ├── dashboard/    # Founder overview
│   ├── finance/      # Budget records + expenses
│   ├── hiring/       # Job postings + candidate pipeline
│   ├── legal/        # NDAs, contracts, compliance docs
│   ├── marketing/    # Campaigns
│   ├── analytics/    # KPI chart + metric log
│   └── approvals/    # Founder approval workflow
└── routes/
    └── ProtectedRoute.jsx   # redirects to /login if session isn't valid
```

## Auth flow (session cookie, not JWT)

- The backend sets an `ASOS_SESSION` cookie on `POST /api/auth/register` or `/login`. The frontend never sees or stores a token — every `fetch` call in `src/api/client.js` is made with `credentials: 'include'` so the browser sends the cookie automatically.
- On app load, `AuthContext` calls `GET /api/auth/me`; a `401` means "not signed in" and the user is redirected to `/login`.
- `logout()` calls `POST /api/auth/logout`, which invalidates the session server-side, then clears local state.
- Because this relies on a shared-domain cookie, local dev works out of the box (`localhost:5173` ↔ `localhost:8080` share the `localhost` cookie jar). In production, frontend and backend should share a top-level domain, or the cookie needs `SameSite=None; Secure` and HTTPS on both ends.

## What's next

- Wire founder-facing "AI recommendations" / n8n-triggered notifications once those are exposed by the backend.
- Add pagination if candidate/expense/campaign lists grow large.
- Swap the mono `${...}.toLocaleString()` money formatting for a proper currency/locale setting if you support multiple currencies.
- The Three.js chunk is ~220 KB gzipped, loaded lazily and in parallel — fine for a dashboard app, but if you ever server-render or care about Lighthouse scores, consider gating it further behind a "reduce motion" / low-power check.

