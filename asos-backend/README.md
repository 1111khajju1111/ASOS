# ASOS Backend — Autonomous Startup Operating System

Spring Boot backend for ASOS. Built per project constraints:

- **No Lombok** — all entities/DTOs use explicit constructors, getters, and setters.
- **No Spring Security** — authentication is manual: a server-side `HttpSession` (the standard servlet container session, cookie-based via `ASOS_SESSION`) set by `AuthController` on login/register, password hashing via `jBCrypt`, and a plain `OncePerRequestFilter` (part of `spring-web`, not Spring Security) that checks the session on every `/api/**` request except `/api/auth/register` and `/api/auth/login`.
- **PostgreSQL on Aiven** via Spring Data JPA.

## Tech Stack

| Layer | Choice |
|---|---|
| Backend | Spring Boot 3.3.4, Java 17 |
| Auth | Manual HttpSession (cookie) + jBCrypt |
| DB | PostgreSQL (Aiven), Spring Data JPA / Hibernate |
| Build | Maven |

## Project Structure

```
src/main/java/com/asos/backend/
├── entity/          # JPA entities (Founder, FinanceRecord, Expense, JobPosting,
│                       Candidate, LegalDocument, MarketingCampaign, AnalyticsMetric,
│                       ApprovalRequest) + enums
├── repository/      # Spring Data JPA repositories
├── dto/             # Request/response DTOs, grouped by module
├── service/         # Business logic per module
├── controller/      # REST controllers per module
├── security/        # SessionAuthFilter, @CurrentFounderId annotation + resolver
├── util/            # PasswordUtil (jBCrypt)
├── config/          # FilterConfig (registers JwtAuthFilter), WebConfig (CORS, arg resolver)
└── exception/       # Custom exceptions + GlobalExceptionHandler
```

## 1. Set up Aiven PostgreSQL

1. Go to your Aiven console → your PostgreSQL service → **Overview** tab → "Connection Information".
2. Grab: **Host**, **Port**, **Database name** (usually `defaultdb`), **User** (usually `avnadmin`), **Password**.
3. Either edit `src/main/resources/application.yml` directly, or (recommended) set environment variables so you never commit real credentials:

```bash
export DB_HOST=your-service-host.aivencloud.com
export DB_PORT=12345
export DB_NAME=defaultdb
export DB_USERNAME=avnadmin
export DB_PASSWORD=your-aiven-password

export CORS_ALLOWED_ORIGINS=http://localhost:5173
```

Aiven requires SSL — `application.yml` already appends `?sslmode=require` to the JDBC URL.

## 2. Build & run

```bash
mvn clean install
mvn spring-boot:run
```

The API starts on `http://localhost:8080`. Hibernate is set to `ddl-auto: update`, so tables are created automatically on first run.

## 3. Auth flow

Session cookie is set automatically by the server on register/login — the client just needs to send cookies with every request (`credentials: 'include'` in `fetch`, or `-c`/`-b` cookie-jar flags in curl).

```bash
# Register (creates the session cookie in cookies.txt)
curl -c cookies.txt -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{"fullName":"Ram","email":"ram@example.com","password":"password123","companyName":"The Vault"}'

# Login
curl -c cookies.txt -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"ram@example.com","password":"password123"}'

# Use the session cookie on every other endpoint:
curl -b cookies.txt http://localhost:8080/api/auth/me

# Sign out (invalidates the session server-side)
curl -b cookies.txt -X POST http://localhost:8080/api/auth/logout
```

## 4. Endpoint map

| Module | Base path | Notes |
|---|---|---|
| Auth | `/api/auth` | `register`, `login`, `logout`, `me` (public except `logout`/`me`) |
| Finance | `/api/finance` | `records` (budget/burn/runway), `expenses` |
| Hiring | `/api/hiring` | `jobs`, `jobs/{id}/candidates` |
| Legal | `/api/legal` | `documents` (NDA/contract/compliance) |
| Marketing | `/api/marketing` | `campaigns` |
| Analytics | `/api/analytics` | `metrics` (KPIs, optional `?metricName=` filter) |
| Approvals | `/api/approvals` | `POST` create, `GET ?pendingOnly=true`, `PATCH /{id}/decision` |

Every module scopes data to the authenticated founder automatically — the founder id is pulled out of the JWT by `JwtAuthFilter` and injected into controller methods via `@CurrentFounderId Long founderId`.

## 5. Notes on the finance calculations

- **Burn rate** = `totalExpenses / months between periodStart and periodEnd` (minimum 1 month), i.e. average monthly spend.
- **Runway (months)** = `cashBalance / burnRate`. If burn rate is 0, runway is returned as `-1` to signal "undefined/infinite" — handle that sentinel value on the frontend.

## 6. What's intentionally left for you to wire up next

- n8n workflow triggers (e.g. call n8n webhooks from `ApprovalService` when a request is approved) — not included since n8n setup is separate infra.
- HuggingFace Inference API calls for the CEO/agent "intelligence" layer — the modules here are the CRUD/data backbone the AI agents would read from and write to.
- Docker/Render/Vercel deployment config.

## Security notes for production

- Set `server.servlet.session.cookie.secure: true` once served over HTTPS (required for `SameSite=None` if frontend and backend end up on different top-level domains).
- Default in-memory HTTP sessions don't survive a server restart or scale across multiple instances. For production with more than one backend instance, back sessions with Redis via `spring-session-data-redis` (still no Spring Security needed — Spring Session is a separate, unrelated project).
- `ddl-auto: update` is convenient for development; switch to `validate` and use a migration tool (Flyway/Liquibase) before production.
