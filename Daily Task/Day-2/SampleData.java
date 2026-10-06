public class SampleData {
    public static void main(String[] args) {
        String[] studentNames = {"Ravi", "Priya", "Amit", "Sneha", "Karan"};
        int[] roomNumbers = {101, 102, 103, 104, 105};
        int[] bedCounts = {4, 3, 4, 2, 3};
        double[] dailyElectricityUnits = {12.5, 10.0, 15.3, 8.7, 11.2};
        long[] studentIds = {2025001L, 2025002L, 2025003L, 2025004L, 2025005L};

        System.out.println("=== " + Constants.HOSTEL_NAME + " Sample Data ===");
        System.out.println("Rooms configured: " + Constants.MAX_ROOMS);

        for (int i = 0; i < studentNames.length; i++) {
            char block = (studentIds[i] % 2 == 0) ? 'A' : 'B';
            String occupancy = (bedCounts[i] >= 4) ? "Full" : "Available";
            System.out.println(
                "ID: " + studentIds[i]
                + " | Name: " + studentNames[i]
                + " | Room: " + block + "-" + roomNumbers[i]
                + " | Beds: " + bedCounts[i]
                + " | Status: " + occupancy
            );
        }
    }
}
