public class PlatformInfo {
    public static void main(String[] args) {
        System.out.println("=== Java Platform Information ===");

        String javaVersion = System.getProperty("java.version");
        String osName = System.getProperty("os.name");
        int processors = Runtime.getRuntime().availableProcessors();
        long maxHeap = Runtime.getRuntime().maxMemory() / (1024 * 1024);
        long freeHeap = Runtime.getRuntime().freeMemory() / (1024 * 1024);

        System.out.println("Java Version  : " + javaVersion);
        System.out.println("OS Name       : " + osName);
        System.out.println("Processors    : " + processors);
        System.out.println("Max Heap (MB) : " + maxHeap);
        System.out.println("Free Heap (MB): " + freeHeap);
    }
}
