package com.hclhostel.desk;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Scanner;

public class HostelFrontDesk {

    private static final String CORRECT_PIN = "9999";
    private static final int MAX_PIN_ATTEMPTS = 3;

    public static void main(String[] args) throws IOException {
        String env = loadEnv();
        System.out.println("=== HCL Training Hostel — Front Desk [" + env.toUpperCase() + "] ===");
        System.out.println();

        Scanner sc = new Scanner(System.in);

        Bed[] beds = buildBedInventory();
        Student[] todayStudents = buildTodayStudents();

        boolean wardenLoggedIn = wardenLogin(sc);
        if (!wardenLoggedIn) {
            System.out.println("Too many wrong PIN attempts. Exiting.");
            sc.close();
            return;
        }
        System.out.println("✅ Login OK. Welcome, Warden.");
        System.out.println();

        MAIN_MENU:
        while (true) {
            System.out.println("[ MENU ]");
            System.out.println("1. View Available Beds (all blocks)");
            System.out.println("2. Find First Free Bed in a Block");
            System.out.println("3. View Today's Mess Attendance");
            System.out.println("4. Logout");
            System.out.print("Choose an option (or type ! to stay here): ");

            String raw = sc.next();
            if ("!".equals(raw)) {
                System.out.println("⚠️  Emergency stay on main menu.");
                System.out.println();
                continue;
            }

            int option;
            try {
                option = Integer.parseInt(raw);
            } catch (NumberFormatException ex) {
                System.out.println("Invalid input. Please enter a number 1-4.");
                System.out.println();
                continue;
            }

            switch (option) {
                case 1:
                    System.out.println();
                    System.out.println("--- Available Beds (all blocks) ---");
                    for (int i = 0; i < beds.length; i++) {
                        Bed b = beds[i];
                        if (b.isOccupied()) {
                            continue;
                        }
                        System.out.println("  " + b.label() + "  FREE");
                    }
                    System.out.println();
                    break;

                case 2:
                    System.out.print("Block letter (A/B/C, or ! for menu): ");
                    String blockInput = sc.next().toUpperCase();
                    if ("!".equals(blockInput)) {
                        System.out.println("⚠️  Returning to main menu.");
                        System.out.println();
                        break MAIN_MENU;
                    }
                    if (!blockInput.equals("A") && !blockInput.equals("B") && !blockInput.equals("C")) {
                        System.out.println("Invalid block. Try A, B, or C.");
                        System.out.println();
                        break;
                    }
                    System.out.println("🔍 Scanning Block " + blockInput + "...");
                    boolean found = false;
                    for (int i = 0; i < beds.length; i++) {
                        Bed b = beds[i];
                        if (!b.getBlock().equals(blockInput)) {
                            continue;
                        }
                        if (!b.isOccupied()) {
                            System.out.println("   First free bed found: " + b.label());
                            found = true;
                            break;
                        }
                    }
                    if (!found) {
                        System.out.println("   No free beds in Block " + blockInput + ".");
                    }
                    System.out.println();
                    break;

                case 3:
                    System.out.println();
                    System.out.println("--- Today's Mess Attendance ---");
                    int presentCount = 0;
                    for (Student s : todayStudents) {
                        String mark = s.isPresent() ? "PRESENT" : "ABSENT";
                        if (s.isPresent()) {
                            presentCount++;
                        } else {
                            System.out.print("  ");
                            System.out.printf("%-8s: %s%n", s.getName(), mark);
                            continue;
                        }
                        System.out.print("  ");
                        System.out.printf("%-8s: %s%n", s.getName(), mark);
                    }
                    System.out.println("Total present: " + presentCount + " / " + todayStudents.length);
                    System.out.println();
                    break;

                case 4:
                    System.out.println("👋 Logging out.");
                    sc.close();
                    return;

                default:
                    System.out.println("Invalid option. Choose 1-4.");
                    System.out.println();
                    break;
            }
        }
    }

    private static boolean wardenLogin(Scanner sc) {
        int attempts = 0;
        boolean ok = false;
        do {
            attempts++;
            int left = MAX_PIN_ATTEMPTS - attempts;
            System.out.print("Warden PIN (4 digits; attempts left: " + (left + 1) + "): ");
            String pin = sc.next();

            if ("!".equals(pin)) {
                System.out.println("Emergency: cancelled login.");
                return false;
            }

            if (pin.length() != 4 || !pin.chars().allMatch(Character::isDigit)) {
                System.out.println("PIN must be exactly 4 digits.");
                continue;
            }

            if (CORRECT_PIN.equals(pin)) {
                ok = true;
                break;
            } else {
                System.out.println("Incorrect PIN.");
            }
        } while (!ok && attempts < MAX_PIN_ATTEMPTS);
        return ok;
    }

    private static Bed[] buildBedInventory() {
        String[] blocks = {"A", "B"};
        int roomsPerBlock = 3;
        int bedsPerRoom = 2;
        Bed[] beds = new Bed[blocks.length * roomsPerBlock * bedsPerRoom];
        int idx = 0;
        boolean[] occupiedFlags = {
            false, true,  false, false, true,  false,
            true,  true,  false, true,  false, false
        };
        for (String block : blocks) {
            for (int r = 1; r <= roomsPerBlock; r++) {
                for (int b = 1; b <= bedsPerRoom; b++) {
                    int roomNo = 100 + r + (block.equals("B") ? 10 : 0);
                    beds[idx] = new Bed(block, roomNo, b, occupiedFlags[idx]);
                    idx++;
                }
            }
        }
        return beds;
    }

    private static Student[] buildTodayStudents() {
        return new Student[] {
            new Student("Ravi",    true),
            new Student("Priya",   false),
            new Student("Amit",    true),
            new Student("Sneha",   true),
            new Student("Karan",   false)
        };
    }

    private static String loadEnv() throws IOException {
        Properties p = new Properties();
        try (InputStream in = HostelFrontDesk.class.getClassLoader().getResourceAsStream("application.properties")) {
            if (in != null) p.load(in);
        }
        return p.getProperty("hostel.env", "unknown");
    }
}
