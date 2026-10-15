import java.util.ArrayList;
import java.util.List;

public class MemoryLab {
    private static final int STRING_LENGTH = 5_000;
    private static final int STRING_RUNS = 3;

    private MemoryLab() {
    }

    public static void main(String[] args) {
        if (args.length != 1) {
            printUsage();
            return;
        }

        switch (args[0]) {
            case "stack" -> runStackExperiment();
            case "heap" -> runHeapExperiment();
            case "strings" -> runStringExperiment();
            case "gc" -> runGcExperiment();
            case "integer" -> runIntegerExperiment();
            default -> printUsage();
        }
    }

    private static void printUsage() {
        System.out.println("Choose one experiment: stack, heap, strings, gc, integer");
    }

    private static void runStackExperiment() {
        System.out.println("Each recursive call creates a stack frame; deep recursion "
                + "can exhaust this thread's stack.");
        try {
            recurse(0);
        } catch (StackOverflowError error) {
            System.out.println("Controlled StackOverflowError after "
                    + recursiveCalls + " recursive calls.");
            System.out.println("Catching this error is only for this isolated demonstration.");
        }
    }

    private static long recursiveCalls;

    private static void recurse(long depth) {
        recursiveCalls = depth + 1;
        recurse(depth + 1);
    }

    private static void runHeapExperiment() {
        List<byte[]> retainedBlocks = new ArrayList<>();
        byte[] reportingReserve = new byte[2 * 1024 * 1024];
        reportingReserve[0] = 1;
        long allocatedMiB = 0;
        try {
            while (true) {
                byte[] block = new byte[1024 * 1024];
                block[0] = 1;
                retainedBlocks.add(block);
                allocatedMiB++;
            }
        } catch (OutOfMemoryError error) {
            reportingReserve = null;
            retainedBlocks.clear();
            System.out.println("Controlled OutOfMemoryError after retaining "
                    + allocatedMiB + " MiB of byte arrays.");
            System.out.println("-Xmx64m limits the Java heap, not total JVM process memory.");
            System.out.println("Catching this error is only for this isolated demonstration.");
        }
    }

    private static void runStringExperiment() {
        System.out.println("Same input size for both methods: " + STRING_LENGTH + " characters.");
        System.out.println("Repeated immutable String concatenation may create many "
                + "intermediate String objects.");
        for (int run = 1; run <= STRING_RUNS; run++) {
            long start = System.nanoTime();
            String concatenated = "";
            for (int i = 0; i < STRING_LENGTH; i++) {
                concatenated += "x";
            }
            long concatenationNanos = System.nanoTime() - start;

            start = System.nanoTime();
            StringBuilder builder = new StringBuilder(STRING_LENGTH);
            for (int i = 0; i < STRING_LENGTH; i++) {
                builder.append("x");
            }
            String built = builder.toString();
            long builderNanos = System.nanoTime() - start;

            System.out.printf("Run %d: String += %.3f ms; StringBuilder %.3f ms; "
                            + "lengths %d/%d%n",
                    run, concatenationNanos / 1_000_000.0,
                    builderNanos / 1_000_000.0, concatenated.length(), built.length());
        }
        System.out.println("Times depend on JDK, machine, JIT optimization, and workload.");
    }

    private static void runGcExperiment() {
        List<byte[]> recentBlocks = new ArrayList<>();
        for (int i = 0; i < 256; i++) {
            byte[] block = new byte[1024 * 1024];
            block[0] = (byte) i;
            recentBlocks.add(block);
            if (recentBlocks.size() > 8) {
                recentBlocks.remove(0);
            }
        }
        System.out.println("Allocated 256 MiB over time and retained the most recent "
                + recentBlocks.size() + " one-MiB arrays.");
        System.out.println("Read the JVM's [gc] log messages for observed collection events.");
    }

    private static void runIntegerExperiment() {
        Integer cachedFirst = Integer.valueOf(100);
        Integer cachedSecond = Integer.valueOf(100);
        Integer outsideFirst = Integer.valueOf(1000);
        Integer outsideSecond = Integer.valueOf(1000);

        System.out.println("Within common cache (100): == " + (cachedFirst == cachedSecond)
                + ", equals() " + cachedFirst.equals(cachedSecond));
        System.out.println("Outside common cache (1000): == "
                + (outsideFirst == outsideSecond)
                + ", equals() " + outsideFirst.equals(outsideSecond));
        System.out.println("For wrappers, == compares references and equals() compares values.");
        System.out.println("Caching beyond the standard range can depend on JVM settings.");
    }
}
