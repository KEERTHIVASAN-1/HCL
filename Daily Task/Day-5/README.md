# Day 5 — Inheritance, Polymorphism, Git Branching/Merging

## 1. Java Concepts

| Concept | Where |
|---|---|
| Abstract class | `Payment` (abstract, `pay(double)` abstract, concrete `pay()` + `summary()`) |
| Interface | `Refundable` (one method), `AllocationStrategy` (one method) |
| Abstract vs Interface | Payment = partial implementation (shared fields, `summary()`). Interfaces = pure contract, no state. |
| Subclasses | `CardPayment`, `UPIPayment`, `CashPayment` all `extends Payment` |
| `implements Refundable` | Card + UPI are refundable; Cash is not (shows where interface applies) |
| Method overloading | `CardPayment.pay(double)` + `CardPayment.pay(double, String cvv)` same name different params |
| Method overriding @Override | `pay()`, `summary()`, `refund()`, `User.role()`, `toString()` overrides in Warden/Admin/Student, strategy `chooseRoom` |
| `super` keyword | Subclass constructors call `super(txnId, amount)`; CardPayment.summary calls `super.summary()` |
| Runtime polymorphism | `Payment[] payments = {Card, UPI, Cash}`; loop and call `p.pay()` + `p.summary()` → correct override runs. Same for `AllocationStrategy` → 2 impls. |

## 2. Hostel OOP

- `BaseEntity (abstract)` → common `id`, `createdAt`. Parent of `Student`, `Room`, `Bed`.
- `User` → `Warden extends User`, `Admin extends User`. Override `role()`. `toString()` uses `role()` so runtime polymorphism prints `[WARDEN] priya`.
- Strategy interface: `AllocationStrategy.chooseRoom(List<Room>)` with 2 impls `LowestFloorStrategy`, `MostVacantStrategy`. Hostel uses strategy pattern for future allocation logic (Story 2, room allocation).

## 3. How to Run

```bash
cd "Daily Task/Day-5"
mvn clean package -q
java -jar target/day5-inheritance-polymorphism-1.0.0.jar
```

## 4. Git Branching & Merging (commands YOU run step by step)

### Initial state: on `main`, clean status.

#### 4a. Fast-forward merge (feature branch, no main commits in between)
```bash
git checkout -b feature/day5-oop
# (write/edit Day-5 files now; or if done, commit below)
git add "Daily Task/Day-5/"
git commit -m "Day 5: Inheritance (Payment + BaseEntity + Role hierarchy) + Strategy"
git checkout main
git merge --ff-only feature/day5-oop    # fast-forward (no merge commit if clean)
git log --oneline -n 3
```

#### 4b. Three-way merge (main has moved forward after branch)
```bash
git checkout main
# make 1 small commit on main (e.g. add line to Project/README.md or Day-5 README)
echo "" >> "Daily Task/Day-5/README.md"
echo "## 4a. Fast-forward merge notes" >> "Daily Task/Day-5/README.md"
git add "Daily Task/Day-5/README.md"
git commit -m "docs: add section heading to day5 README (main)"

git checkout -b feature/day5-bonus
# edit SAME file on branch (README.md) — but different lines, so Git auto 3-way merges usually
echo "## 5. Future work: Add FeePayment service subclassing Payment" >> "Daily Task/Day-5/README.md"
git add "Daily Task/Day-5/README.md"
git commit -m "docs: add future work note on branch"

git checkout main
git merge feature/day5-bonus -m "Merge branch 'feature/day5-bonus' into main (3-way merge commit)"
git log --oneline --graph -n 6
# → 3-way merge commit shown in graph
```

#### 4c. Merge CONFLICT demo (edit same line on 2 branches, must resolve manually)
```bash
git checkout main
# Create a conflict file in Day-5 called CONFLICT.md (for training only)
$line = "Hostel strategy preferred: LowestFloor (Warden default)."
Set-Content "Daily Task/Day-5/CONFLICT.md" $line
git add "Daily Task/Day-5/CONFLICT.md"
git commit -m "docs: add conflict demo file with main line"

git checkout -b feature/day5-conflict
# OVERWRITE same line on branch
Set-Content "Daily Task/Day-5/CONFLICT.md" "Hostel strategy preferred: MostVacant (Student rush default)."
git add "Daily Task/Day-5/CONFLICT.md"
git commit -m "docs: change strategy line on branch (to cause conflict)"

# Go back to main and TRY to merge → will CONFLICT
git checkout main
git merge feature/day5-conflict
# Expected Git output: Auto-merging ... CONFLICT (content): Merge conflict in Daily Task/Day-5/CONFLICT.md
# Automatic merge failed; fix conflicts and then commit the result.
```

Resolve the conflict MANUALLY (YOU edit and save the file):
Open `Daily Task/Day-5/CONFLICT.md` — Git will show markers:
```
<<<<<<< HEAD
Hostel strategy preferred: LowestFloor (Warden default).
=======
Hostel strategy preferred: MostVacant (Student rush default).
>>>>>>> feature/day5-conflict
```
Change the ENTIRE content to one final clean line you pick (or combine). Example:
```
Hostel strategy default: LowestFloor; switch to MostVacant during admissions rush.
```
Save file. Then run to confirm and finish merge:
```bash
git status
git diff    # should show no <<<, ===, >>> markers remaining
git add "Daily Task/Day-5/CONFLICT.md"
git commit -m "Resolve merge conflict in strategy preference doc"
git log --oneline --graph -n 6
# Verify no conflict markers: gc "Daily Task/Day-5/CONFLICT.md"
Select-String -Path "Daily Task/Day-5/CONFLICT.md" -Pattern '<<<|===|>>>'  # must output NOTHING (no matches = clean)
```

#### 4d. Rebase demo (linearize commits instead of merging)
```bash
# make 2 commits on main first
git checkout main
echo "- notes on strategy pattern" >> "Daily Task/Day-5/README.md"
git add "Daily Task/Day-5/README.md"
git commit -m "docs: notes line 1 main"
echo "- notes on payment hierarchy" >> "Daily Task/Day-5/README.md"
git add "Daily Task/Day-5/README.md"
git commit -m "docs: notes line 2 main"

# create topic branch off an earlier commit (before the 2 new main commits)
git checkout -b feature/day5-rebase HEAD~4
echo "## 6. Git rebase demo" >> "Daily Task/Day-5/README.md"
git add "Daily Task/Day-5/README.md"
git commit -m "docs: rebase demo heading on feature"

git log --oneline feature/day5-rebase -n 3
git log --oneline main -n 5   # main is AHEAD

# REBASE the feature branch on top of main
git rebase main feature/day5-rebase
# if tiny conflict on README, resolve same way, then: git add README.md ; git rebase --continue

git log --oneline feature/day5-rebase -n 5   # linear, feature commit sits on top of main

# Optional: fast-forward merge the rebased feature into main
git checkout main
git merge --ff-only feature/day5-rebase
git log --oneline -n 5
```

**Merge vs Rebase difference:**
- `git merge` — keeps both history lines + produces a merge commit. Honest, non-destructive.
- `git rebase` — rewrites feature commits on top of main, produces a STRAIGHT LINE of commits. Cleaner history, but you *lose* the true "when was this branch created" timeline. Rule: Rebase your *own private* feature branches. NEVER rebase a branch others are working on (shared branches).

## 5. GitHub Tag for Pull Request

```bash
git tag -a "day-5:inheritance-polymorphism" -m "Day 5: inheritance + polymorphism + payment + strategy + role + git branching"
git log --oneline -n 2
git tag -l day-5
# verify

# After git remote origin set (if pushing):
# git push origin main
# git push origin "day-5:inheritance-polymorphism"
# Then open PR comparing main to your branch on GitHub UI.
```

## 6. Screenshots (3)
1. **UML sketch (hand-drawn or simple PNG)** → boxes: BaseEntity (abstract, id, createdAt) with arrows to Student/Room/Bed; Payment abstract with 3 subclasses; Refundable dashed arrow to Card/UPIPayment; User with Warden/Admin; AllocationStrategy interface arrow to 2 impls. Label relations: `extends`, `implements`.
2. **Merge conflict resolution** → VS Code merge editor open showing <<< HEAD vs === vs >>> marker lines + final saved file after resolution + `git status` showing clean.
3. **Pull request screen on GitHub** (after push) → PR title "Day 5: Inheritance & Polymorphism", Description lists "Feature branch feature/day5-oop. Added Payment hierarchy, Role hierarchy, Strategy pattern, BaseEntity, Git tag day-5:inheritance-polymorphism."
