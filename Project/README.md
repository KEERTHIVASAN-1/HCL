# Hostel Management System

> HCL Java Full Stack Training Project
> Built gradually: Day by Day, concept by concept.

## Roles
- **Student** — Applies for out-pass, views room, pays fees, logs complaints
- **Warden** — Approves out-pass, allocates rooms, manages occupancy
- **Admin** — Manages blocks, staff, fees dashboard
- **Maintenance Staff** — Resolves complaints

## 8 User Stories (Day 1)

| # | Feature | User Story | Story Points | Definition of Done |
|---|---|---|---|---|
| 1 | Block/Room/Bed inventory | As an Admin, I want to add Blocks, Rooms, and Beds so that hostel inventory is visible in the system. | 5 | Block/Room/Bed data stored correctly; list page shows all with availability status; duplicate room numbers rejected.|
| 2 | Room allocation | As a Warden, I want to allocate a bed to a Student so each student has a confirmed room. | 3 | Allocation records student + bed + date; same bed cannot be allocated twice; allocation page shows status. |
| 3 | Out-pass | As a Student, I want to apply for an out-pass so I can request permission to leave the hostel. | 3 | Student fills date + reason; Warden sees pending list; approve/reject button works; student sees status. |
| 4 | Mess attendance and billing | As a Warden, I want to mark daily mess attendance and generate a monthly bill so students are billed correctly. | 8 | Daily attendance marked per student; monthly bill = days × rate; bill PDF/print available; totals match math. |
| 5 | Maintenance complaints | As a Student, I want to raise a maintenance complaint so issues in the room get fixed. | 5 | Student creates complaint (room + description + category); status = Pending; Staff marks In-Progress → Resolved; timestamps visible. |
| 6 | Visitor log | As a Warden, I want to log visitors so I know who entered and left the hostel. | 3 | Visitor entry stores name, student to visit, in-time, out-time; list filterable by date; out-time updatable. |
| 7 | Fee tracking | As an Admin, I want to track fee payments so I can see who has paid and who hasn't. | 5 | Student has fee amount + due date; payment recorded (date + amount); paid/unpaid filter; balance calculates correctly. |
| 8 | Occupancy dashboard | As a Warden, I want to see an occupancy dashboard so I know how many beds are free/occupied per block. | 3 | Dashboard shows counts per block; percentages display correctly; page refresh shows latest data. |

## Sprint Plan (Sample)
- **Sprint Length**: 2 weeks
- **Sprint Goal**: Complete stories 1, 2, 3 so that room allocation flow works end-to-end.
- **Standup**: Daily 10:00 AM
- **Retrospective**: End of each Friday

## Progress Tracking
| Day | Concepts Applied | Project Progress |
|---|---|---|
| Day 1 | JVM/JRE/JDK, Agile User Stories | 8 User stories written with DoD |
| Day 2 | Data Types, Arrays, Constants, Git | Constants + Sample data created |
