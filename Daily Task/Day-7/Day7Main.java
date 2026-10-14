import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.PriorityQueue;
import java.util.Set;

public class Day7Main {
    private record Request(String studentId, String book, boolean faculty, boolean overdue) {}

    public static void main(String[] args) {
        // A List keeps ordered catalogue entries and supports access by index.
        List<String> catalogue = new ArrayList<>(List.of(
                "Java Basics", "Collections in Java", "Spring Boot Introduction"));
        System.out.println("Catalogue book at index 1: " + catalogue.get(1));

        // A Set keeps unique IDs; LinkedHashSet also remembers insertion order.
        // HashSet has no ordering guarantee; TreeSet keeps unique values sorted.
        LinkedHashSet<String> studentIds = new LinkedHashSet<>();
        // A Queue represents work waiting to be handled; ArrayDeque is FIFO here.
        ArrayDeque<Request> waitlist = new ArrayDeque<>();
        addToWaitlist(studentIds, waitlist,
                new Request("S101", "Java Basics", false, false));
        addToWaitlist(studentIds, waitlist,
                new Request("S102", "Collections in Java", false, false));
        addToWaitlist(studentIds, waitlist,
                new Request("S101", "Spring Boot Introduction", false, false));

        System.out.println("\nUnique waitlisted IDs (in insertion order): " + studentIds);
        System.out.println("Normal FIFO waitlist (for-each):");
        for (Request request : waitlist) {
            System.out.println("  " + request.studentId() + " - " + request.book());
        }
        System.out.println("FIFO processing order:");
        while (!waitlist.isEmpty()) {
            Request next = waitlist.pollFirst();
            System.out.println("  " + next.studentId() + " - " + next.book());
        }

        // Unlike ArrayDeque's FIFO order, PriorityQueue follows its comparator.
        // Faculty gets priority 0 and regular students get priority 1.
        // Equal-priority requests have no guaranteed order.
        PriorityQueue<Request> priorityWaitlist = new PriorityQueue<>(
                Comparator.comparingInt(request -> request.faculty() ? 0 : 1));
        priorityWaitlist.add(new Request("S201", "Java Basics", false, false));
        priorityWaitlist.add(new Request("F101", "Collections in Java", true, false));
        priorityWaitlist.add(new Request("S202", "Spring Boot Introduction", false, false));
        System.out.println("\nPriority processing order (faculty first):");
        while (!priorityWaitlist.isEmpty()) {
            Request next = priorityWaitlist.poll();
            String type = next.faculty() ? "Faculty" : "Regular";
            System.out.println("  " + type + " " + next.studentId() + " - " + next.book());
        }

        // for-each reads simply; Iterator.remove safely deletes during traversal.
        // Directly changing the list during an Iterator loop can cause
        // ConcurrentModificationException; use that Iterator's remove method.
        List<Request> loans = new ArrayList<>(List.of(
                new Request("S301", "Java Basics", false, true),
                new Request("S302", "Collections in Java", false, false),
                new Request("S303", "Spring Boot Introduction", false, true)));
        Iterator<Request> loanIterator = loans.iterator();
        while (loanIterator.hasNext()) {
            Request loan = loanIterator.next();
            if (loan.overdue()) {
                System.out.println("\nRemoving overdue item: " + loan.studentId()
                        + " - " + loan.book());
                loanIterator.remove();
            }
        }
        System.out.println("Remaining loans: " + loans.size());

        // ListIterator can traverse a List in either direction.
        System.out.println("\nCatalogue with ListIterator:");
        ListIterator<String> bookIterator = catalogue.listIterator();
        while (bookIterator.hasNext()) {
            System.out.println("  " + bookIterator.nextIndex() + ": " + bookIterator.next());
        }
        System.out.println("Catalogue in reverse:");
        while (bookIterator.hasPrevious()) {
            System.out.println("  " + bookIterator.previous());
        }

        runTimingExperiment();
    }

    private static void addToWaitlist(Set<String> ids, ArrayDeque<Request> waitlist,
                                      Request request) {
        if (ids.add(request.studentId())) {
            waitlist.addLast(request);
            System.out.println("Added student " + request.studentId());
        } else {
            System.out.println("Duplicate student ID rejected: " + request.studentId());
        }
    }

    private static void runTimingExperiment() {
        // ArrayList indexed get is typically fast; LinkedList must walk nodes.
        // Front insertion shifts ArrayList items, while LinkedList links a node.
        // Results depend on size, hardware, JVM warm-up, and what operation is measured.
        int size = 8_000;
        List<Integer> arrayList = new ArrayList<>();
        List<Integer> linkedList = new LinkedList<>();
        for (int i = 0; i < size; i++) {
            arrayList.add(i);
            linkedList.add(i);
        }

        long checksum = 0;
        long start = System.nanoTime();
        for (int i = 0; i < size; i++) {
            checksum += arrayList.get(i);
        }
        long arrayGetNanos = System.nanoTime() - start;

        start = System.nanoTime();
        for (int i = 0; i < size; i++) {
            checksum += linkedList.get(i);
        }
        long linkedGetNanos = System.nanoTime() - start;

        List<Integer> arrayInsert = new ArrayList<>();
        List<Integer> linkedInsert = new LinkedList<>();
        start = System.nanoTime();
        for (int i = 0; i < size; i++) {
            arrayInsert.add(0, i);
        }
        long arrayInsertNanos = System.nanoTime() - start;

        start = System.nanoTime();
        for (int i = 0; i < size; i++) {
            linkedInsert.add(0,i);
        }
        long linkedInsertNanos = System.nanoTime() - start;

        System.out.println("\nTiming experiment (" + size + " items; one run; milliseconds):");
        System.out.printf("  ArrayList indexed get(i): %.3f ms%n", arrayGetNanos / 1_000_000.0);
        System.out.printf("  LinkedList indexed get(i): %.3f ms%n", linkedGetNanos / 1_000_000.0);
        System.out.printf("  ArrayList insert at front: %.3f ms%n", arrayInsertNanos / 1_000_000.0);
        System.out.printf("  LinkedList insert at front: %.3f ms%n", linkedInsertNanos / 1_000_000.0);
        System.out.println("  Checksum (keeps indexed-read results used): " + checksum);
    }
}
