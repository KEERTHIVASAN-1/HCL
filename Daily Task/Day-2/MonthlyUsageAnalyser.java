public class MonthlyUsageAnalyser {
    public static void main(String[] args) {
        final int TOTAL_DAYS = Constants.DAYS_IN_MONTH;

        int[] dailyElectricity = {12, 15, 10, 14, 18, 22, 11, 13, 16, 20, 17, 9, 12, 14, 19, 21, 15, 13, 11, 16, 18, 14, 12, 10, 8, 13, 15, 17, 19, 20};
        int[] dailyWater = {5, 6, 4, 7, 8, 9, 5, 6, 7, 8, 6, 4, 5, 7, 8, 9, 6, 5, 4, 7, 8, 6, 5, 4, 3, 6, 7, 8, 9, 7};
        long[] messAttendance = {80, 85, 90, 88, 92, 78, 82, 86, 89, 91, 87, 84, 88, 90, 93, 89, 85, 82, 80, 86, 88, 91, 87, 83, 81, 85, 89, 92, 90, 88};

        int totalElectricity = 0;
        int totalWater = 0;
        long totalMessAttendance = 0;
        int peakElectricity = 0;
        int lowestWater = Integer.MAX_VALUE;

        for (int i = 0; i < TOTAL_DAYS; i++) 
        {
            totalElectricity += dailyElectricity[i];
            totalWater += dailyWater[i];
            totalMessAttendance += messAttendance[i];
            if (dailyElectricity[i] > peakElectricity) 
                {
                    peakElectricity = dailyElectricity[i];
                }
            if (dailyWater[i] < lowestWater) 
                {
                    lowestWater = dailyWater[i];
                }
        }

        double avgElectricity = (double) totalElectricity / TOTAL_DAYS;
        double avgWater = (double) totalWater / TOTAL_DAYS;
        double avgMessAttendance = (double) totalMessAttendance / TOTAL_DAYS;

        double electricityBill = totalElectricity * Constants.ELECTRICITY_RATE_PER_UNIT;
        double waterBill = totalWater * Constants.WATER_RATE_PER_KL;
        long totalBill = (long) (electricityBill + waterBill);

        String messStatus = (avgMessAttendance >= 85) ? "Good" : (avgMessAttendance >= 75 ? "Average" : "Needs Attention");

        System.out.println("=== " + Constants.HOSTEL_NAME + " Monthly Usage Report ===");
        System.out.println("Period       : " + TOTAL_DAYS + " days");
        System.out.println("");
        System.out.println("Electricity:");
        System.out.println("  Total units  : " + totalElectricity + " units");
        System.out.println("  Daily avg  : " + String.format("%.2f", avgElectricity) + " units");
        System.out.println("  Peak day   : " + peakElectricity + " units");
        System.out.println("  Bill       : Rs. " + String.format("%.2f", electricityBill));
        System.out.println("");
        System.out.println("Water:");
        System.out.println("  Total KL   : " + totalWater + " KL");
        System.out.println("  Daily avg  : " + String.format("%.2f", avgWater) + " KL");
        System.out.println("  Lowest day : " + lowestWater + " KL");
        System.out.println("  Bill       : Rs. " + String.format("%.2f", waterBill));
        System.out.println("");
        System.out.println("Mess:");
        System.out.println("  Total meals : " + totalMessAttendance + " (stored as long)");
        System.out.println("  Daily avg  : " + String.format("%.0f", avgMessAttendance) + " students");
        System.out.println("  Status     : " + messStatus);
        System.out.println("");
        System.out.println("Combined Utility Bill (cast to long): Rs. " + totalBill);
    }
}
