# Day 4 — OOP Concepts + IDE & Debugging

## OOP Concepts Covered

### Classes / Fields / Methods
- Each class below has private fields, public methods to read or change data.

### Constructors + this() chaining + this keyword
- `BankAccount()` → `this(0)` → `this(accountNumber, holder, balance)`
- Model classes (Student/Room/Bed): 2-arg constructor uses `this.id = id` to disambiguate field vs parameter.

### Access modifiers (4)
- `private`: fields (id, name, balance, ...) — encapsulated. Access only via class.
- default (no keyword): classes visible within same package.
- `protected`: (not used in Day 4 — covered when inheritance comes. Keep in mind.)
- `public`: methods, constructors, classes. Callable from anywhere.

### Encapsulation
- `balance`, `accountNumber`, `holder` are all `private`. Changed only via `deposit()`/`withdraw()`, which validate input.

### Static vs Instance
- `static int accountCount`: single counter shared across ALL BankAccount objects.
- instance (`balance`, `holder`, `accountNumber`): each object has its own copy.

### equals() / hashCode()
- Overridden together. Two accounts are equal when `accountNumber` matches.

### Packages
- `com.hclhostel.model` — data holders (Student/Room/Bed)
- `com.hclhostel.service` — business logic (BankAccount with validation)
- `com.hclhostel.app` — entry point (Day4Main)

## Debugger Skills (Day 4)

Breakpoints, conditional breakpoint, logpoint, Step Over/Into/Out, Watch, Call Stack, Hot Code Replace.

## How to Run

```
cd "Daily Task/Day-4"
mvn clean package -q
java -jar target/day4-oop-debug-1.0.0.jar
```

## How to Debug + Find the Bug (step-by-step)

1. Open `Day4Main.java` in VS Code / IntelliJ with Java debugger.
2. Open `service/BankAccount.java`. Put breakpoint inside `withdraw()` on line `balance -= (amount - 1);`.
3. Add **conditional breakpoint**: right-click the red dot, condition: `accountNumber == 102` (stops only for Priya's account).
4. Add **Watch** in Variables pane: `this.balance` and `amount`.
5. Debug Day4Main → execution pauses at withdraw() for Priya.
6. Note values *before* step over: balance=6000, amount=800. Expected result after correct withdraw: 6000 - 800 = 5200.
7. Press **Step Over (F10/F6)**. Check Watch: balance = ???
   - If wrong number appears: you found the bug. Compare to expected (5200).
8. Inspect **Call Stack** pane to see: `withdraw()` called by `main()`.
9. **Hot Code Replace**: While still paused on this thread, edit the buggy line (see "Bug Location" below), save.
   - VS Code/IntelliJ will pop up "Hot code replace succeeded" if supported.
10. Continue (F5/F9). The fixed balance will now be used. Rerun → value will match expected 5200.

Take screenshots of:
- (A) Conditional breakpoint dialog showing condition `accountNumber == 102`.
- (B) Watch pane showing balance/amount values + post-step buggy balance.
- (C) After HCR/fix, Watch showing corrected balance and Call Stack pane visible.

## Bug Location (LOOK HERE only AFTER trying to find it yourself)

**File:** `src/main/java/com/hclhostel/service/BankAccount.java`
**Method:** `withdraw(double amount)`
**Buggy line:** `balance -= (amount - 1);`
**Issue:** subtracts `amount - 1` instead of `amount`. Every withdrawal under-counts by 1 rupee; after N withdrawals the account retains an extra N rupees.
**Fix:** `balance -= amount;`

**Numerical proof:**
- Priya starts 5000, deposit 1000 → 6000, then withdraw 800.
- Expected correct: 6000 - 800 = 5200.
- Buggy result: 6000 - (800 - 1) = 6000 - 799 = 5201.
- So if program prints `bal=5201.0` → that's the bug caught by debugger.
