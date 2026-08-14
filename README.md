# Medical Representative Field Force Platform

Production-oriented MR/SFA platform with Spring Boot 3, PostgreSQL, JWT authentication, dynamic database-backed RBAC, React, Redux Toolkit, Material UI, email notifications, Swagger, Flyway migrations, and Docker.

## Sample Accounts

Both seeded accounts use password `Password123!`.

- Admin: company `MRX`, username `admin`
- MR: company `MRX`, username `mr`

## Run Locally

```bash
docker compose up --build
```

- Frontend: http://localhost:3000
- Backend API: http://localhost:8080
- Swagger UI: http://localhost:8080/swagger-ui/index.html
- MailHog inbox: http://localhost:8025

## Architecture

- `backend/src/main/java/com/mrsystem/auth`: JWT, refresh token, forgot/reset password, change password.
- `backend/src/main/java/com/mrsystem/security`: stateless security and permission authorities.
- `backend/src/main/java/com/mrsystem/domain`: users, RBAC, territories, doctors, hospitals, pharmacies, visits.
- `backend/src/main/java/com/mrsystem/dashboard`: admin and MR dashboard APIs.
- `backend/src/main/resources/db/migration`: complete PostgreSQL schema and seed data.
- `frontend/src/Auth/LoginPage.js`: existing login page preserved and wired to backend auth.
- `frontend/src/Dashboard/UserDashboard.js`: permission-aware Material UI dashboard shell.

Future roles are created by inserting rows into `roles`, linking permissions through `role_permissions`, and assigning through `user_roles`. Backend authorization checks permissions, not fixed role names.
# Medical_Sales_Rep
