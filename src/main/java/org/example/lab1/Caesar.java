package org.example.lab1;

import java.util.LinkedHashSet;
import java.util.Set;

public class Caesar {
    private static final String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    public String encrypt(String message, int key) {
        StringBuilder encryptedMessage = new StringBuilder();
        message = message.toUpperCase().replaceAll("\\s+", "");

        for (char ch : message.toCharArray()) {
            int pos = ALPHABET.indexOf(ch);
            int newPos = (pos + key) % 26;
            encryptedMessage.append(ALPHABET.charAt(newPos));
        }

        return encryptedMessage.toString();
    }

    public String decrypt(String message, int key) {
        StringBuilder decryptedMessage = new StringBuilder();
        message = message.toUpperCase().replaceAll("\\s+", "");

        for (char ch : message.toCharArray()) {
            int pos = ALPHABET.indexOf(ch);
            int newPos = (pos - key + 26) % 26;
            decryptedMessage.append(ALPHABET.charAt(newPos));
        }

        return decryptedMessage.toString();
    }

    private String createSubstitutionAlphabet(String key) {
        key = key.toUpperCase().replaceAll("\\s+", "");
        Set<Character> uniqueChars = new LinkedHashSet<>();

        for (char ch : key.toCharArray()) {
            if (ch >= 'A' && ch <= 'Z') {
                uniqueChars.add(ch);
            }
        }

        for (char ch : ALPHABET.toCharArray()) {
            uniqueChars.add(ch);
        }

        StringBuilder newAlphabet = new StringBuilder();
        for (char ch : uniqueChars) {
            newAlphabet.append(ch);
        }

        return newAlphabet.toString();
    }

    public String encryptTwoKeys(String message, int key1, String key2) {
        String substitutionAlphabet = createSubstitutionAlphabet(key2);
        StringBuilder encryptedMessage = new StringBuilder();
        message = message.toUpperCase().replaceAll("\\s+", "");

        for (char ch : message.toCharArray()) {
            int pos = substitutionAlphabet.indexOf(ch);
            int newPos = (pos + key1) % 26;
            encryptedMessage.append(substitutionAlphabet.charAt(newPos));
        }

        return encryptedMessage.toString();
    }

    public String decryptTwoKeys(String message, int key1, String key2) {
        String substitutionAlphabet = createSubstitutionAlphabet(key2);
        StringBuilder decryptedMessage = new StringBuilder();
        message = message.toUpperCase().replaceAll("\\s+", "");

        for (char ch : message.toCharArray()) {
            int pos = substitutionAlphabet.indexOf(ch);
            int newPos = (pos - key1 + 26) % 26;
            decryptedMessage.append(substitutionAlphabet.charAt(newPos));
        }

        return decryptedMessage.toString();
    }

    public String[] bruteForceSingle(String cipher) {
        String[] results = new String[26];
        for (int k = 1; k <= 25; k++) {
            results[k] = decrypt(cipher, k);
        }
        return results;
    }

    public String[] bruteForceTwoWhenKeywordKnown(String cipher, String key2) {
        String[] results = new String[26];
        for (int k = 1; k <= 25; k++) {
            results[k] = decryptTwoKeys(cipher, k, key2);
        }
        return results;
    }
}
