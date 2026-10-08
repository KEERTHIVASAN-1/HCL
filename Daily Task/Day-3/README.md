# Day 3 — Control Flow + Maven Fundamentals

## Hostel Scenario
**Hostel Front Desk** — A Warden console menu. Log in with PIN, view free beds, find a bed in a specific block, check mess attendance.

## Files
```
Daily Task/Day-3/
  pom.xml
  src/main/java/com/hclhostel/desk/
    HostelFrontDesk.java      ← main class, all control flow
    Bed.java                  ← block/room/bed POJO
    Student.java              ← name + present flag POJO
  src/main/resources/
    application.properties    ← hostel.env (filtered by Maven profile)
  src/test/java/ (placeholder)
```

## Control Flow Concepts Used
| Concept | Where |
|---|---|
| `do-while` | PIN login (runs at least once, up to 3 attempts) |
| `while(true)` | Top-level main menu loop |
| `switch` | Menu option 1/2/3/4/`default` |
| `if/else` | PIN format validation, block letter (A/B/C) check, free-bed detection |
| classic `for` | `for (int i = 0; i < beds.length; i++)` — inventory loops |
| enhanced `for` | `for (Student s : todayStudents)` in mess attendance; also `for (String block : blocks)` in buildBedInventory |
| `continue` | Skip print when a bed is occupied in "View Available Beds"; skip summary line path for ABSENT students |
| `break` | Stop searching in "Find First Free Bed" once found; break out of PIN loop on success |
| labelled `break MAIN_MENU` | When user types `!` in "Find Block" sub-prompt → jump back to top MENU label |

## Maven Concepts Used
| Concept | Notes |
|---|---|
| Standard structure | `src/main/java`, `src/main/resources`, `src/test/java` |
| Coordinates | `com.hclhostel:day3-frontdesk:1.0.0` |
| Properties | `maven.compiler.release=21`, `hostel.env` |
| Resource filtering | `hostel.env=${hostel.env}` replaced at package time by profile value |
| Lifecycle | `mvn clean package` → validate → compile → test → package |
| Profiles | `-Pdev` (default): `hostel.env=dev`; `-Pprod`: `hostel.env=prod` |
| maven-jar-plugin | Sets `Main-Class` manifest so JAR is runnable with `java -jar` |
| Dependency scopes (to learn now, no demo today) | `compile` (default), `test` (JUnit), `provided` (servlet API), `runtime` (JDBC driver) |

## How to Run
```bash
cd "Daily Task/Day-3"
mvn clean package -Pdev
java -jar target/day3-frontdesk-1.0.0.jar
```
Correct PIN: `9999`

Try packaging with prod profile too:
```bash
mvn clean package -Pprod
java -jar target/day3-frontdesk-1.0.0.jar
```
Startup banner will say `[DEV]` or `[PROD]` depending on profile.

## Connect to Hostel Project
- Same PIN/login pattern will guard Warden endpoints later (Spring Security).
- Bed-inventory data structures (Block/Room/Bed) map 1:1 to Hostel Project Feature 1.
- Mess attendance enhanced-for is prototype for Feature 4 attendance list.
- Dev/prod profiles pattern reused for local vs deployed DB later.
