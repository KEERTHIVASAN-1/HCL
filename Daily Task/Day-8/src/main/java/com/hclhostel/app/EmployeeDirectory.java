package com.hclhostel.app;

import com.hclhostel.model.BadEmployee;
import com.hclhostel.model.Employee;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class EmployeeDirectory {
    private static final Comparator<Employee> BY_SALARY_DESC =
            Comparator.comparingDouble(Employee::getSalary).reversed()
                    .thenComparing(Employee::getName);
    private static final Comparator<Employee> BY_DEPARTMENT_THEN_SALARY =
            Comparator.comparing(Employee::getDepartment)
                    .thenComparingDouble(Employee::getSalary);

    public static void main(String[] args) {
        List<Employee> employees = List.of(
                new Employee("E104", "Maya", "Engineering", 92000),
                new Employee("E101", "Arun", "Support", 61000),
                new Employee("E103", "Zara", "Engineering", 105000),
                new Employee("E102", "Dev", "Sales", 78000),
                new Employee("E106", "Isha", "Sales", 84000),
                new Employee("E105", "Liam", "Support", 72000));

        // HashMap gives direct lookup by employee ID without scanning the list.
        Map<String, Employee> employeeById = new HashMap<>();
        // merge adds one to the department count for every employee.
        Map<String, Integer> departmentCounts = new HashMap<>();
        for (Employee employee : employees) {
            employeeById.put(employee.getId(), employee);
            departmentCounts.merge(employee.getDepartment(), 1, Integer::sum);
        }

        System.out.println("Employee lookup E103: " + employeeById.get("E103"));
        System.out.println("\nDepartment counts (HashMap): " + departmentCounts);

        // TreeMap sorts report keys by department name.
        Map<String, Integer> departmentReport = new TreeMap<>(departmentCounts);
        System.out.println("\nDepartment report (TreeMap):");
        departmentReport.forEach((department, count) ->
                System.out.println("  " + department + ": " + count));

        // LinkedHashMap preserves the order in which these entries are added.
        Map<String, String> insertionOrderExample = new LinkedHashMap<>();
        insertionOrderExample.put("E104", "Maya");
        insertionOrderExample.put("E101", "Arun");
        insertionOrderExample.put("E103", "Zara");
        System.out.println("\nLinkedHashMap insertion order: " + insertionOrderExample.keySet());

        List<Employee> byId = new ArrayList<>(employees);
        byId.sort(null); // null uses Employee's natural Comparable ordering.
        System.out.println("\nNatural order (employee ID):");
        byId.forEach(System.out::println);

        List<Employee> bySalary = new ArrayList<>(employees);
        bySalary.sort(BY_SALARY_DESC);
        System.out.println("\nSalary descending, then name ascending:");
        bySalary.forEach(System.out::println);

        List<Employee> byDepartment = new ArrayList<>(employees);
        byDepartment.sort(BY_DEPARTMENT_THEN_SALARY);
        System.out.println("\nDepartment, then salary ascending:");
        byDepartment.forEach(System.out::println);

        System.out.println("\nTop 5 salaries:");
        bySalary.stream().limit(5).forEach(System.out::println);

        demonstrateEqualsAndHashCode();
    }

    private static void demonstrateEqualsAndHashCode() {
        // INTENTIONALLY INCORRECT: equals uses ID, but hashCode differs per object.
        Map<BadEmployee, String> incorrectMap = new HashMap<>();
        BadEmployee firstBad = new BadEmployee("E200");
        BadEmployee equivalentBad = new BadEmployee("E200");
        incorrectMap.put(firstBad, "Directory entry");
        System.out.println("\nIncorrect equals/hashCode example:");
        System.out.println("  Objects equal by ID: " + firstBad.equals(equivalentBad));
        System.out.println("  Map lookup with equal ID: " + incorrectMap.get(equivalentBad));

        // Employee implements equals and hashCode consistently using its ID.
        Map<Employee, String> correctMap = new HashMap<>();
        Employee first = new Employee("E200", "Noah", "Finance", 68000);
        Employee equivalent = new Employee("E200", "Noah", "Finance", 68000);
        correctMap.put(first, "Directory entry");
        System.out.println("\nCorrect equals/hashCode example:");
        System.out.println("  Objects equal by ID: " + first.equals(equivalent));
        System.out.println("  Map lookup with equal ID: " + correctMap.get(equivalent));
    }

}
