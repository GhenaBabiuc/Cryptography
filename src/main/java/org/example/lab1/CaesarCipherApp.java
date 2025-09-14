package org.example.lab1;

import java.util.Scanner;

public class CaesarCipherApp {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Caesar caesar = new Caesar();
        boolean menu = true;
        int key1;
        String key2;

        while (menu) {
            System.out.println("\nCaesar Cipher");
            System.out.println("1. Encrypt (one key)");
            System.out.println("2. Decrypt (one key)");
            System.out.println("3. Encrypt (two keys)");
            System.out.println("4. Decrypt (two keys)");
            System.out.println("5. Brute-force (one key)");
            System.out.println("6. Brute-force (two keys)");
            System.out.println("7. Exit");
            System.out.print("Choose operation (1-7): ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1 -> {
                    key1 = getKey(scanner);
                    String message = getMessage(scanner);
                    String result = caesar.encrypt(message, key1);
                    System.out.println("Encrypted message: " + result);
                }
                case 2 -> {
                    key1 = getKey(scanner);
                    String message = getMessage(scanner);
                    String result = caesar.decrypt(message, key1);
                    System.out.println("Decrypted message: " + result);
                }
                case 3 -> {
                    key1 = getKey(scanner);
                    key2 = getKeyTwo(scanner);
                    String message = getMessage(scanner);
                    String result = caesar.encryptTwoKeys(message, key1, key2);
                    System.out.println("Encrypted message: " + result);
                }
                case 4 -> {
                    key1 = getKey(scanner);
                    key2 = getKeyTwo(scanner);
                    String message = getMessage(scanner);
                    String result = caesar.decryptTwoKeys(message, key1, key2);
                    System.out.println("Decrypted message: " + result);
                }
                case 5 -> {
                    System.out.print("Enter cipher to brute-force (letters only): ");
                    String cipher = getMessage(scanner);
                    String[] candidates = caesar.bruteForceSingle(cipher);
                    System.out.println("Brute-force results (key -> candidate):");
                    for (int k = 1; k <= 25; k++) {
                        System.out.printf("%2d -> %s%n", k, candidates[k]);
                    }
                }
                case 6 -> {
                    System.out.println("Brute-force two-keys (KEY2 known).");
                    System.out.print("Enter cipher to brute-force (letters only): ");
                    String cipher = getMessage(scanner);
                    key2 = getKeyTwo(scanner);
                    String[] candidates = caesar.bruteForceTwoWhenKeywordKnown(cipher, key2);
                    System.out.println("Brute-force results (key1 -> candidate):");
                    for (int k = 1; k <= 25; k++) {
                        System.out.printf("%2d -> %s%n", k, candidates[k]);
                    }
                }
                case 7 -> {
                    System.out.println("Goodbye!");
                    menu = false;
                }
                default -> System.out.println("Invalid choice. Please enter 1..7.");
            }
        }
        scanner.close();
    }

    private static int getKey(Scanner scanner) {
        while (true) {
            System.out.print("Enter key1 (1-25): ");
            int key = scanner.nextInt();
            scanner.nextLine();
            if (validateKey(key)) {
                return key;
            } else {
                System.out.println("Invalid key. Please enter a value between 1 and 25 inclusive.");
            }
        }
    }

    private static String getKeyTwo(Scanner scanner) {
        while (true) {
            System.out.print("Enter key2 (at least 7 letters): ");
            String key = scanner.nextLine();
            if (validateKeyTwo(key)) {
                return key;
            } else {
                System.out.println("Invalid key. Please enter at least 7 letters.");
            }
        }
    }

    private static String getMessage(Scanner scanner) {
        while (true) {
            System.out.print("Enter message: ");
            String message = scanner.nextLine();
            if (validateMessage(message)) {
                return message;
            } else {
                System.out.println("Invalid message. Please enter only letters (A-Z, a-z).");
            }
        }
    }

    public static boolean validateKey(int key) {
        return key >= 1 && key <= 25;
    }

    public static boolean validateKeyTwo(String key) {
        return key != null && key.replaceAll("\\s+", "").length() >= 7 && key.matches("[a-zA-Z\\s]+");
    }

    public static boolean validateMessage(String message) {
        if (message == null || message.trim().isEmpty()) {
            return false;
        }
        String cleanMessage = message.replaceAll("\\s+", "");
        return cleanMessage.matches("[a-zA-Z]+");
    }
}
