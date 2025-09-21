package org.example.lab3;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class Playfair {
    private static final String ALPHABET = "AĂÂBCDEFGHIÎJKLMNOPQRSȘTȚUVWXYZ";
    private static final int MATRIX_SIZE = 6;
    private char[][] matrix;
    private Map<Character, int[]> charPositions;

    public Playfair() {
        matrix = new char[MATRIX_SIZE][MATRIX_SIZE];
        charPositions = new HashMap<>();
    }

    private void generateMatrix(String key) {
        key = normalizeText(key).toUpperCase();
        Set<Character> used = new LinkedHashSet<>();

        for (char c : key.toCharArray()) {
            if (ALPHABET.indexOf(c) != -1) {
                used.add(c);
            }
        }

        for (char c : ALPHABET.toCharArray()) {
            used.add(c);
        }

        for (char c = '0'; used.size() < 36 && c <= '9'; c++) {
            used.add(c);
        }

        int idx = 0;
        for (char c : used) {
            int row = idx / MATRIX_SIZE;
            int col = idx % MATRIX_SIZE;
            matrix[row][col] = c;
            charPositions.put(c, new int[]{row, col});
            idx++;
        }
    }

    private String normalizeText(String text) {
        StringBuilder result = new StringBuilder();
        for (char c : text.toUpperCase().toCharArray()) {
            if (ALPHABET.indexOf(c) != -1) {
                result.append(c);
            } else if (Character.isLowerCase(text.charAt(text.toUpperCase().indexOf(c)))) {
                char upper = Character.toUpperCase(c);
                if (ALPHABET.indexOf(upper) != -1) {
                    result.append(upper);
                }
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

    private List<String> prepareText(String text) {
        text = normalizeText(text);
        List<String> pairs = new ArrayList<>();

        for (int i = 0; i < text.length(); i += 2) {
            if (i + 1 < text.length()) {
                char first = text.charAt(i);
                char second = text.charAt(i + 1);

                if (first == second) {
                    pairs.add(String.valueOf(first) + 'X');
                    i--;
                } else {
                    pairs.add(String.valueOf(first) + second);
                }
            } else {
                pairs.add(String.valueOf(text.charAt(i)) + 'X');
            }
        }

        return pairs;
    }

    private String encryptPair(String pair) {
        char first = pair.charAt(0);
        char second = pair.charAt(1);

        int[] pos1 = charPositions.get(first);
        int[] pos2 = charPositions.get(second);

        if (pos1 == null || pos2 == null) {
            return pair;
        }

        char enc1, enc2;

        if (pos1[0] == pos2[0]) {
            enc1 = matrix[pos1[0]][(pos1[1] + 1) % MATRIX_SIZE];
            enc2 = matrix[pos2[0]][(pos2[1] + 1) % MATRIX_SIZE];
        } else if (pos1[1] == pos2[1]) {
            enc1 = matrix[(pos1[0] + 1) % MATRIX_SIZE][pos1[1]];
            enc2 = matrix[(pos2[0] + 1) % MATRIX_SIZE][pos2[1]];
        } else {
            enc1 = matrix[pos1[0]][pos2[1]];
            enc2 = matrix[pos2[0]][pos1[1]];
        }

        return String.valueOf(enc1) + enc2;
    }

    private String decryptPair(String pair) {
        char first = pair.charAt(0);
        char second = pair.charAt(1);

        int[] pos1 = charPositions.get(first);
        int[] pos2 = charPositions.get(second);

        if (pos1 == null || pos2 == null) {
            return pair;
        }

        char dec1, dec2;

        if (pos1[0] == pos2[0]) {
            dec1 = matrix[pos1[0]][(pos1[1] - 1 + MATRIX_SIZE) % MATRIX_SIZE];
            dec2 = matrix[pos2[0]][(pos2[1] - 1 + MATRIX_SIZE) % MATRIX_SIZE];
        } else if (pos1[1] == pos2[1]) {
            dec1 = matrix[(pos1[0] - 1 + MATRIX_SIZE) % MATRIX_SIZE][pos1[1]];
            dec2 = matrix[(pos2[0] - 1 + MATRIX_SIZE) % MATRIX_SIZE][pos2[1]];
        } else {
            dec1 = matrix[pos1[0]][pos2[1]];
            dec2 = matrix[pos2[0]][pos1[1]];
        }

        return String.valueOf(dec1) + dec2;
    }

    public String encrypt(String plaintext, String key) {
        if (key.length() < 7) {
            throw new IllegalArgumentException("Key must have at least 7 characters!");
        }

        if (!validateInput(plaintext)) {
            throw new IllegalArgumentException("Text contains invalid characters! Use only Romanian alphabet letters (A-Z, Ă, Â, Î, Ș, Ț).");
        }

        generateMatrix(key);
        List<String> pairs = prepareText(plaintext);
        StringBuilder result = new StringBuilder();

        for (String pair : pairs) {
            result.append(encryptPair(pair));
        }

        return result.toString();
    }

    public String decrypt(String ciphertext, String key) {
        if (key.length() < 7) {
            throw new IllegalArgumentException("Key must have at least 7 characters!");
        }

        if (!validateInput(ciphertext)) {
            throw new IllegalArgumentException("Ciphertext contains invalid characters! Use only Romanian alphabet letters (A-Z, Ă, Â, Î, Ș, Ț).");
        }

        generateMatrix(key);
        ciphertext = normalizeText(ciphertext);
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < ciphertext.length(); i += 2) {
            if (i + 1 < ciphertext.length()) {
                String pair = String.valueOf(ciphertext.charAt(i)) + ciphertext.charAt(i + 1);
                result.append(decryptPair(pair));
            }
        }

        return result.toString();
    }

    public void printMatrix() {
        System.out.println("\nPlayfair Matrix:");
        System.out.println("==================");
        for (int i = 0; i < MATRIX_SIZE; i++) {
            for (int j = 0; j < MATRIX_SIZE; j++) {
                System.out.print(matrix[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println("==================\n");
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Playfair playfair = new Playfair();

        System.out.println("==============================================");
        System.out.println("   PLAYFAIR ALGORITHM FOR ROMANIAN LANGUAGE");
        System.out.println("==============================================");
        System.out.println("Supported alphabet: " + ALPHABET);
        System.out.println();

        while (true) {
            System.out.println("\nChoose operation:");
            System.out.println("1. Encrypt");
            System.out.println("2. Decrypt");
            System.out.println("3. Exit");
            System.out.print("Your choice (1-3): ");

            int choice;
            try {
                choice = scanner.nextInt();
                scanner.nextLine();
            } catch (InputMismatchException e) {
                scanner.nextLine();
                System.out.println("Invalid option! Please enter 1, 2 or 3.");
                continue;
            }

            if (choice == 3) {
                System.out.println("Goodbye!");
                break;
            }

            if (choice != 1 && choice != 2) {
                System.out.println("Invalid option! Please choose 1, 2 or 3.");
                continue;
            }

            System.out.print("\nEnter key (minimum 7 characters): ");
            String key = scanner.nextLine();

            if (key.length() < 7) {
                System.out.println("ERROR: Key must have at least 7 characters!");
                continue;
            }

            if (!playfair.validateInput(key)) {
                System.out.println("ERROR: Key contains invalid characters!");
                System.out.println("Use only Romanian alphabet letters: A-Z, Ă, Â, Î, Ș, Ț");
                continue;
            }

            try {
                if (choice == 1) {
                    System.out.print("Enter message to encrypt: ");
                    String plaintext = scanner.nextLine();

                    String encrypted = playfair.encrypt(plaintext, key);

                    playfair.printMatrix();

                    System.out.println("Original text (normalized): " + playfair.normalizeText(plaintext));
                    System.out.println("Encrypted text: " + encrypted);
                    System.out.println("\nNote: To add spaces in the encrypted text according");
                    System.out.println("to Romanian language logic, do this manually.");

                } else {
                    System.out.print("Enter ciphertext to decrypt: ");
                    String ciphertext = scanner.nextLine();

                    String decrypted = playfair.decrypt(ciphertext, key);

                    playfair.printMatrix();

                    System.out.println("Ciphertext: " + playfair.normalizeText(ciphertext));
                    System.out.println("Decrypted text: " + decrypted);
                    System.out.println("\nNote: 'X' characters may have been added automatically between");
                    System.out.println("duplicate letters or at the end. Add spaces manually");
                    System.out.println("according to the message meaning in Romanian.");
                }

            } catch (IllegalArgumentException e) {
                System.out.println("ERROR: " + e.getMessage());
            }
        }

        scanner.close();
    }
}
