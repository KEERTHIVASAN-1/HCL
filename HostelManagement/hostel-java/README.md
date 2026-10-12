# Hostel Management System — Java Foundation

This is a separate Spring Boot implementation for the first project review. It
does not replace or modify the existing application. It currently includes
role-based login, dashboards, student and room creation/listing, and bed
allocation backed by MongoDB.

## Requirements

- Java 21 or newer
- Maven 3.9 or newer
- MongoDB running locally or a MongoDB Atlas connection string

From PowerShell, run commands in this directory:

```powershell
mvn test
mvn spring-boot:run
```

Open `http://localhost:8080`. The application listens on port 8080 by default.

## MongoDB

By default, the application connects to the local database
`mongodb://localhost:27017/hostel_java`. Start MongoDB before starting the
application. To use another local database or MongoDB Atlas, set `MONGODB_URI`
in the environment before running Maven:

```powershell
$env:MONGODB_URI = "mongodb://localhost:27017/hostel_java"
# Or set this to your private Atlas URI; do not commit credentials.
mvn spring-boot:run
```

MongoDB is the only configured database. If it is unavailable, startup and
database-backed requests fail; the application does not switch databases.
Spring Data creates the annotated unique indexes at startup. These protect
usernames, student IDs, student emails, room numbers, and active allocation
uniqueness (one allocation per student and one allocation per room/bed).

## Demo accounts

On startup, missing demo users are created with BCrypt-hashed passwords. The
defaults are intended only for local development:

| Role | Username | Development password |
|---|---|---|
| Admin | `admin` | `admin123` |
| Warden | `warden` | `warden123` |
| Student | `student1@hostel.local` | `student123` |
| Maintenance Staff | `maintenance` | `maint123` |

Configure credentials before startup with `HOSTEL_ADMIN_USER`,
`HOSTEL_ADMIN_PASSWORD`, `HOSTEL_WARDEN_USER`, `HOSTEL_WARDEN_PASSWORD`,
`HOSTEL_STUDENT_USER`, `HOSTEL_STUDENT_PASSWORD`, `HOSTEL_MAINT_USER`, and
`HOSTEL_MAINT_PASSWORD`. New student accounts use
`HOSTEL_STUDENT_DEFAULT_PASSWORD` (default: `student123`). Provide production
values through a private environment/secret manager and never use the
development defaults in production. Demo user creation is idempotent; changing
an environment password does not reset an already-created user's password.

## Login pages and dashboards

| Role | Login | Dashboard |
|---|---|---|
| Admin | `/admin/login` | `/admin/dashboard` |
| Warden | `/warden/login` | `/warden/dashboard` |
| Student | `/student/login` | `/student/dashboard` |

Maintenance Staff is seeded as a role but has no first-review login page or
dashboard. Spring Security enforces the role at the server routes. Login forms
also require the account role to match the login page, and state-changing forms
use Spring Security CSRF protection.

## First-review routes

- `GET /admin/students`, `GET /admin/students/new`, `POST /admin/students`
- `GET /admin/rooms`, `GET /admin/rooms/new`, `POST /admin/rooms`
- `GET /admin/allocations`, `GET /admin/allocations/new`, `POST /admin/allocations`
- `GET /warden/dashboard`, `GET /warden/allocations/new`, `POST /warden/allocations`
- `GET /student/dashboard`
- `POST /perform_login`, `POST /logout`

Student creation generates a student ID when none is provided and creates a
student login using the email as username. A new student account receives the
configured default student password. Admin and warden can allocate a student
to an available numbered bed. Unique MongoDB indexes close the race between
simultaneous allocation requests.

## Review walkthrough

1. Start MongoDB and then run `mvn spring-boot:run`.
2. Visit `/admin/login`; log in as `admin` / `admin123`.
3. Add a student with a unique email and note the generated student ID.
4. Add a room with a block and positive bed capacity.
5. Open **Allocate Bed**, select the student, room, and one available bed.
6. Return to the dashboard and room list to show updated occupancy.
7. Attempt the same bed allocation again; it is rejected by validation or the
   MongoDB unique index.
8. Log out, then use `/warden/login` and `/student/login` to show the respective
   dashboards. A student sees only the profile linked to their own account.
9. Try a role mismatch (for example, admin credentials on the student login);
   the request is rejected and the user remains logged out.

## Request flow

The browser submits a server-rendered HTML form. Spring Security validates the
session, CSRF token, credentials, and role before routing an authorized request
to a Spring MVC controller. The controller delegates business rules to a Java
service, which reads or writes through a Spring Data MongoDB repository.
Thymeleaf renders the returned model as HTML for the browser.

## Deliberately deferred

Out-passes, visitor logs, mess attendance/billing, maintenance complaints,
fees, transfers, audit history, and occupancy charts are not implemented in
this first-review foundation. The maintenance role is seeded for future use.
The project currently has no automated test source files; verify flows using
the review walkthrough and add automated tests as the next development step.
