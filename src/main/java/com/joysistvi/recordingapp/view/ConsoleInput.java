package com.joysistvi.recordingapp.view;

import java.util.Scanner;

// View(Presentation Tier) -> single shared Scanner for all console input
public class ConsoleInput {

    private static final Scanner scanner = new Scanner(System.in);

    public static String readString(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }

    public static int readInt(String prompt) {
        System.out.print(prompt);
        try {
            return Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            return -1; // invalid number -> guard clause will catch it
        }
    }
}