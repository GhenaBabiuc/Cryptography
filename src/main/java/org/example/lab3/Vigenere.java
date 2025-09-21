package org.example.lab3;

import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Map;
import java.util.Scanner;

public class Vigenere {
    private static final String ALPHABET = "AĂÂBCDEFGHIÎJKLMNOPQRSȘTȚUVWXYZ";
    private static final int ALPHABET_SIZE = 31;

    private Map<Character, Integer> charToIndex;
    private Map<Integer, Character> indexToChar;

    public Vigenere() {
        charToIndex = new HashMap<>();
        indexToChar = new HashMap<>();

        for (int i = 0; i < ALPHABET.length(); i++) {
            charToIndex.put(ALPHABET.charAt(i), i);
            indexToChar.put(i, ALPHABET.charAt(i));
        }
    }

    private String normalizeText(String text) {
        StringBuilder result = new StringBuilder();

        for (char c : text.toUpperCase().toCharArray()) {
            if (ALPHABET.indexOf(c) != -1) {
                result.append(c);
            }
        }

        return result.toString();
    }

    private boolean validateInput(String text) {
        for (char c : text.toCharArray()) {
            char upper = Character.toUpperCase(c);
            if (ALPHABET.indexOf(upper) == -1 && !Character.isWhitespace(c)) {
                return false;
            }
        }
        return true;
    }

    public String encrypt(String plaintext, String key) {
        if (key.length() < 7) {
            throw new IllegalArgumentException("Key must have at least 7 characters!");
        }

        if (!validateInput(plaintext)) {
            throw new IllegalArgumentException("Message contains invalid characters! Use only Romanian alphabet letters (A-Z, Ă, Â, Î, Ș, Ț).");
        }

        if (!validateInput(key)) {
            throw new IllegalArgumentException("Key contains invalid characters! Use only Romanian alphabet letters (A-Z, Ă, Â, Î, Ș, Ț).");
        }

        String normalizedText = normalizeText(plaintext);
        String normalizedKey = normalizeText(key);

        if (normalizedKey.isEmpty()) {
            throw new IllegalArgumentException("Key must contain at least one valid Romanian letter!");
        }

        StringBuilder ciphertext = new StringBuilder();

        for (int i = 0; i < normalizedText.length(); i++) {
            char messageChar = normalizedText.charAt(i);
            char keyChar = normalizedKey.charAt(i % normalizedKey.length());

            int messageValue = charToIndex.get(messageChar);
            int keyValue = charToIndex.get(keyChar);

            int encryptedValue = (messageValue + keyValue) % ALPHABET_SIZE;

            char encryptedChar = indexToChar.get(encryptedValue);
            ciphertext.append(encryptedChar);
        }

        return ciphertext.toString();
    }

    public String decrypt(String ciphertext, String key) {
        if (key.length() < 7) {
            throw new IllegalArgumentException("Key must have at least 7 characters!");
        }

        if (!validateInput(ciphertext)) {
            throw new IllegalArgumentException("Ciphertext contains invalid characters! Use only Romanian alphabet letters (A-Z, Ă, Â, Î, Ș, Ț).");
        }

        if (!validateInput(key)) {
            throw new IllegalArgumentException("Key contains invalid characters! Use only Romanian alphabet letters (A-Z, Ă, Â, Î, Ș, Ț).");
        }

        String normalizedCipher = normalizeText(ciphertext);
        String normalizedKey = normalizeText(key);

        if (normalizedKey.isEmpty()) {
            throw new IllegalArgumentException("Key must contain at least one valid Romanian letter!");
        }

        StringBuilder plaintext = new StringBuilder();

        for (int i = 0; i < normalizedCipher.length(); i++) {
            char cipherChar = normalizedCipher.charAt(i);
            char keyChar = normalizedKey.charAt(i % normalizedKey.length());

            int cipherValue = charToIndex.get(cipherChar);
            int keyValue = charToIndex.get(keyChar);

            int decryptedValue = (cipherValue - keyValue + ALPHABET_SIZE) % ALPHABET_SIZE;

            char decryptedChar = indexToChar.get(decryptedValue);
            plaintext.append(decryptedChar);
        }

        return plaintext.toString();
    }

    public void printEncodingTable() {
        System.out.println("\nRomanian Alphabet Encoding Table:");
        System.out.println("-----------------------------------");

        int columns = 8;
        for (int i = 0; i < ALPHABET_SIZE; i++) {
            System.out.printf("%c=%2d  ", ALPHABET.charAt(i), i);
            if ((i + 1) % columns == 0 || i == ALPHABET_SIZE - 1) {
                System.out.println();
            }
        }
        System.out.println("-----------------------------------");
    }

    public void showProcessDetails(String text, String key, boolean isEncryption) {
        String normalizedText = normalizeText(text);
        String normalizedKey = normalizeText(key);

        if (normalizedText.isEmpty() || normalizedKey.isEmpty()) {
            return;
        }

        System.out.println("\n" + (isEncryption ? "Encryption" : "Decryption") + " Process Details:");
        System.out.println("-------------------------------------------------------");
        System.out.println("Position | Char | Value | Key | K-Val | Result | R-Val");
        System.out.println("---------|------|-------|-----|-------|--------|------");

        for (int i = 0; i < Math.min(normalizedText.length(), 10); i++) {
            char textChar = normalizedText.charAt(i);
            char keyChar = normalizedKey.charAt(i % normalizedKey.length());
            int textValue = charToIndex.get(textChar);
            int keyValue = charToIndex.get(keyChar);

            int resultValue;
            if (isEncryption) {
                resultValue = (textValue + keyValue) % ALPHABET_SIZE;
            } else {
                resultValue = (textValue - keyValue + ALPHABET_SIZE) % ALPHABET_SIZE;
            }

            char resultChar = indexToChar.get(resultValue);

            System.out.printf("   %2d    |  %c   |  %2d   |  %c  |  %2d   |   %c    |  %2d%n",
                    i, textChar, textValue, keyChar, keyValue, resultChar, resultValue);
        }

        if (normalizedText.length() > 10) {
            System.out.println("... (showing first 10 characters only)");
        }
        System.out.println("-------------------------------------------------------\n");
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Vigenere vigenere = new Vigenere();

        System.out.println("---------------------------------------------");
        System.out.println("   VIGENERE CIPHER FOR ROMANIAN LANGUAGE");
        System.out.println("---------------------------------------------");
        System.out.println("Romanian alphabet (31 letters): " + ALPHABET);
        System.out.println("Encoding: A=0, Ă=1, Â=2, ..., Z=30");
        System.out.println();

        while (true) {
            System.out.println("\nChoose operation:");
            System.out.println("1. Encrypt message");
            System.out.println("2. Decrypt message");
            System.out.println("3. Show encoding table");
            System.out.println("4. Exit");
            System.out.print("Your choice (1-4): ");

            int choice;
            try {
                choice = scanner.nextInt();
                scanner.nextLine();
            } catch (InputMismatchException e) {
                scanner.nextLine();
                System.out.println("Invalid option! Please enter 1, 2, 3 or 4.");
                continue;
            }

            if (choice == 4) {
                System.out.println("Goodbye!");
                break;
            }

            if (choice == 3) {
                vigenere.printEncodingTable();
                continue;
            }

            if (choice != 1 && choice != 2) {
                System.out.println("Invalid option! Please choose 1, 2, 3 or 4.");
                continue;
            }

            // Get key
            System.out.print("\nEnter key (minimum 7 characters): ");
            String key = scanner.nextLine();

            if (key.length() < 7) {
                System.out.println("ERROR: Key must have at least 7 characters!");
                continue;
            }

            if (!vigenere.validateInput(key)) {
                System.out.println("ERROR: Key contains invalid characters!");
                System.out.println("Valid range: Romanian alphabet letters (A-Z, Ă, Â, Î, Ș, Ț)");
                System.out.println("Spaces will be removed automatically.");
                continue;
            }

            try {
                if (choice == 1) {
                    System.out.print("Enter message to encrypt: ");
                    String plaintext = scanner.nextLine();

                    if (!vigenere.validateInput(plaintext)) {
                        System.out.println("ERROR: Message contains invalid characters!");
                        System.out.println("Valid range: Romanian alphabet letters (A-Z, Ă, Â, Î, Ș, Ț)");
                        System.out.println("Spaces will be removed automatically.");
                        continue;
                    }

                    String encrypted = vigenere.encrypt(plaintext, key);

                    System.out.println("\n--- ENCRYPTION RESULT ---");
                    System.out.println("Original message: " + plaintext);
                    System.out.println("Normalized message: " + vigenere.normalizeText(plaintext));
                    System.out.println("Key (normalized): " + vigenere.normalizeText(key));
                    System.out.println("Encrypted message: " + encrypted);

                    System.out.print("\nShow encryption details? (y/n): ");
                    String showDetails = scanner.nextLine();
                    if (showDetails.toLowerCase().startsWith("y")) {
                        vigenere.showProcessDetails(plaintext, key, true);
                    }

                } else {
                    System.out.print("Enter ciphertext to decrypt: ");
                    String ciphertext = scanner.nextLine();

                    if (!vigenere.validateInput(ciphertext)) {
                        System.out.println("ERROR: Ciphertext contains invalid characters!");
                        System.out.println("Valid range: Romanian alphabet letters (A-Z, Ă, Â, Î, Ș, Ț)");
                        System.out.println("Spaces will be removed automatically.");
                        continue;
                    }

                    String decrypted = vigenere.decrypt(ciphertext, key);

                    System.out.println("\n--- DECRYPTION RESULT ---");
                    System.out.println("Ciphertext: " + ciphertext);
                    System.out.println("Normalized ciphertext: " + vigenere.normalizeText(ciphertext));
                    System.out.println("Key (normalized): " + vigenere.normalizeText(key));
                    System.out.println("Decrypted message: " + decrypted);

                    System.out.print("\nShow decryption details? (y/n): ");
                    String showDetails = scanner.nextLine();
                    if (showDetails.toLowerCase().startsWith("y")) {
                        vigenere.showProcessDetails(ciphertext, key, false);
                    }
                }

            } catch (IllegalArgumentException e) {
                System.out.println("ERROR: " + e.getMessage());
            }
        }

        scanner.close();
    }
}
