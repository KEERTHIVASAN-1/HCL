package monitoring;

import java.util.ArrayList;
import java.util.List;

public class HeapUsageDemo {
    private static final int BLOCK_COUNT = 4;
    private static final int BLOCK_SIZE_BYTES = 1024 * 1024;

    private HeapUsageDemo() {
    }

    public static void main(String[] args) {
        List<byte[]> retainedBlocks = new ArrayList<>(BLOCK_COUNT);
        MemorySnapshot.capture().print("Before retaining arrays");

        for (int i = 0; i < BLOCK_COUNT; i++) {
            byte[] block = new byte[BLOCK_SIZE_BYTES];
            block[0] = (byte) i;
            retainedBlocks.add(block);
        }

        MemorySnapshot.capture().print("After retaining "
                + retainedBlocks.size() + " one-MiB arrays");
    }
}
