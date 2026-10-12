# Day 6 — Exception Handling + AI-Assisted Debugging

## Files
- `exception/InvalidQuantityException` — **unchecked** (extends `RuntimeException`) — rule 1 (qty > 0)
- `exception/InsufficientStockException` — **checked** (extends `Exception`) — rule 2 (qty <= stock)
- `service/SupplyOrderService` — one method `processOrder(item, qty) throws InsufficientStockException`
- `app/Day6Main` — runner demonstrating try/catch/finally, multi-catch, exception chaining, recovery

## Concept Map
| Concept | Where |
|---|---|
| `throw` | `SupplyOrderService.processOrder` throws both exceptions |
| `throws` | method signature declares checked `InsufficientStockException` |
| Checked vs unchecked | `InsufficientStockException extends Exception` (checked, must be caught or declared) vs `InvalidQuantityException extends RuntimeException` (unchecked, optional) |
| `try / catch / finally` | main loop around `processOrder` |
| multi-catch | `catch (InvalidQuantityException \| NumberFormatException e)` |
| exception chaining (cause) | `new RuntimeException("Order failed for " + item, e)` — original `e` preserved as cause |
| finally audit | prints timestamp + attempted item/qty every iteration |
| recovery | outer `while` loop `continue`s after caught errors; only the chaining `RuntimeException` (unchecked) propagates and stops the program (intentional — to show cause chain) |

## Run + Test
```
mvn clean package -q
java -jar target/day6-exception-handling-1.0.0.jar
```

Test cases (type one at a time):
- `Bedsheet` then `3` → `Order OK. Remaining Bedsheet: 7` (valid)
- `Soap` then `0` → `Bad input: Quantity must be > 0, got 0` (unchecked, caught, recovery)
- `Bucket` then `99` → program prints `[AUDIT ...]` then throws `RuntimeException: Order failed for Bucket` (chained, unchecked propagation)
- `Soap` then `abc` → `Invalid number: For input string: "abc"` then `continue` (recovery)
- `quit` → `Bye.`

## Two Stack Traces for Copilot Comparison

### Trace A — InvalidQuantityException (unchecked)
```
Exception in thread "main" com.hclhostel.exception.InvalidQuantityException: Quantity must be > 0, got -5
    at com.hclhostel.service.SupplyOrderService.processOrder(SupplyOrderService.java:15)
    at com.hclhostel.app.Day6Main.main(Day6Main.java:27)
```
- Type/message: `InvalidQuantityException: Quantity must be > 0, got -5`. **Confirmed fact**: this is our custom unchecked exception thrown at line 15.
- Likely source: `SupplyOrderService.java:15` — the `if (qty <= 0) throw new InvalidQuantityException(...)`.
- Root cause: caller passed qty = -5.
- Fix: validate caller input so qty is always positive before calling `processOrder`, or handle the exception in a `catch`.
- Assumption: the main method did NOT wrap the call in try/catch (otherwise trace would show the catch handler, not a crash).

### Trace B — InsufficientStockException wrapped (checked → chained unchecked)
```
Exception in thread "main" java.lang.RuntimeException: Order failed for Bucket
    at com.hclhostel.app.Day6Main.main(Day6Main.java:33)
Caused by: com.hclhostel.exception.InsufficientStockException: Only 2 Bucket in stock, requested 99
    at com.hclhostel.service.SupplyOrderService.processOrder(SupplyOrderService.java:19)
    at com.hclhostel.app.Day6Main.main(Day6Main.java:27)
```
- Type/message: top-level `RuntimeException: Order failed for Bucket`. **Confirmed fact**: thrown by our chaining code at Day6Main.java:33 inside the `catch (InsufficientStockException e)` block.
- Likely source: `Day6Main.java:33` — `throw new RuntimeException("Order failed for " + item, e);`.
- Root cause: `Bucket` stock is 2, requested 99 → checked `InsufficientStockException` thrown at service line 19, caught and re-wrapped.
- Fix: either order qty <= available stock, or catch the `RuntimeException` at a higher level to recover.
- **Exception chaining proof**: the `Caused by:` section shows the original checked exception with its full stack — the cause was preserved (`e` passed as 2nd constructor arg).
- Assumption: caller did not catch the wrapping `RuntimeException`.

## Compare with Copilot
Paste traces A and B into Copilot Chat. Record:
- Did Copilot correctly identify the exception type?
- Did Copilot point to the correct source line?
- Did Copilot mention exception chaining (`Caused by`) in Trace B?
- Did Copilot confuse checked vs unchecked?
