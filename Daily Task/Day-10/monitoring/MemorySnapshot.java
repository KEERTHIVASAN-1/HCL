package monitoring;

public record MemorySnapshot(long usedBytes, long committedBytes, long maxBytes) {
    public static MemorySnapshot capture() {
        Runtime runtime = Runtime.getRuntime();
        long usedBytes = runtime.totalMemory() - runtime.freeMemory();
        return new MemorySnapshot(usedBytes, runtime.totalMemory(), runtime.maxMemory());
    }

    public void print(String label) {
        System.out.printf("%s: used %.2f MiB, committed %.2f MiB, maximum %.2f MiB%n",
                label, toMiB(usedBytes), toMiB(committedBytes), toMiB(maxBytes));
    }

    private static double toMiB(long bytes) {
        return bytes / (1024.0 * 1024.0);
    }
}
