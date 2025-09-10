package org.example;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class CaesarCipher {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Map<Integer, Character> alphabet = new HashMap<>();
        Map<Character, Integer> reverseAlphabet = new HashMap<>();
        boolean menu = true;
        int key;

        for (int i = 0; i <= 25; i++) {
            char letter = (char) (i + 'A');
            alphabet.put(i, letter);
            reverseAlphabet.put(letter, i);
        }

        while (menu) {
            System.out.println("Caesar Cipher");
            System.out.println("1. Encrypt");
            System.out.println("2. Decrypt");
            System.out.println("3. Exit");
            System.out.print("Choose operation (1-3): ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1 -> {
                    while (true) {
                        System.out.print("Enter key (1-25): ");
                        key = scanner.nextInt();
                        scanner.nextLine();

                        if (validateKey(key)) {
                            break;
                        } else {
                            System.out.println("Invalid key. Please enter a value between 1 and 25 inclusive.");
                        }
                    }

                    String message;

                    while (true) {
                        System.out.print("Enter message to encrypt: ");
                        message = scanner.nextLine();

                        if (validateMessage(message)) {
                            break;
                        } else {
                            System.out.println("Invalid message. Please enter only letters (A-Z, a-z).");
                        }
                    }

                    String result = encrypt(message, key, alphabet, reverseAlphabet);
                    System.out.println("Encrypted message: " + result);
                }

                case 2 -> {
                    while (true) {
                        System.out.print("Enter key (1-25): ");
                        key = scanner.nextInt();
                        scanner.nextLine();

                        if (validateKey(key)) {
                            break;
                        } else {
                            System.out.println("Invalid key. Please enter a value between 1 and 25 inclusive.");
                        }
                    }

                    String message;

                    while (true) {
                        System.out.print("Enter message to decrypt: ");
                        message = scanner.nextLine();

                        if (validateMessage(message)) {
                            break;
                        } else {
                            System.out.println("Invalid message. Please enter only letters (A-Z, a-z).");
                        }
                    }

                    String result = decrypt(message, key, alphabet, reverseAlphabet);
                    System.out.println("Decrypted message: " + result);
                }

                case 3 -> {
                    System.out.println("Goodbye!");
                    menu = false;
                }

                default -> System.out.println("Invalid choice. Please enter 1, 2, or 3.");
            }
        }
        scanner.close();
    }

    public static boolean validateKey(int key) {
        return key >= 1 && key <= 25;
    }

    public static boolean validateMessage(String message) {
        if (message == null || message.trim().isEmpty()) {
            return false;
        }
        String cleanMessage = message.replaceAll("\\s+", "");
        return cleanMessage.matches("[a-zA-Z]+");
    }

    public static String encrypt(String message, int key, Map<Integer, Character> alphabet, Map<Character, Integer> reverseAlphabet) {
        StringBuilder encryptedMessage = new StringBuilder();
        message = message.toUpperCase().replaceAll("\\s+", "");

        for (int i = 0; i < message.length(); i++) {
            char ch = message.charAt(i);
            int position = reverseAlphabet.get(ch);
            int newPosition = (position + key) % 26;
            char encryptedChar = alphabet.get(newPosition);
            encryptedMessage.append(encryptedChar);
        }

        return encryptedMessage.toString();
    }

    public static String decrypt(String message, int key, Map<Integer, Character> alphabet, Map<Character, Integer> reverseAlphabet) {
        StringBuilder decryptedMessage = new StringBuilder();
        message = message.toUpperCase().replaceAll("\\s+", "");

        for (int i = 0; i < message.length(); i++) {
            char ch = message.charAt(i);
            int position = reverseAlphabet.get(ch);
            int newPosition = (position - key + 26) % 26;
            char decryptedChar = alphabet.get(newPosition);
            decryptedMessage.append(decryptedChar);
        }

        return decryptedMessage.toString();
    }
}
