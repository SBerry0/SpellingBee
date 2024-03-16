import java.io.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

/**
 * Spelling Bee by Sohum Berry
 *
 * This program accepts an input of letters. It prints to an output file
 * all English words that can be generated from those letters.
 *
 * For example: if the user inputs the letters "doggo" the program will generate:
 * do
 * dog
 * doggo
 * go
 * god
 * gog
 * gogo
 * goo
 * good
 *
 * It utilizes recursion to generate the strings, mergesort to sort them, and
 * binary search to find them in a dictionary.
 *
 * @author Zach Blick, [ADD YOUR NAME HERE]
 *
 * Written on March 5, 2023 for CS2 @ Menlo School
 *
 * DO NOT MODIFY MAIN OR ANY OF THE METHOD HEADERS.
 */
public class SpellingBee {

    private String letters;
    private ArrayList<String> words;
    public static final int DICTIONARY_SIZE = 143091;
    public static final String[] DICTIONARY = new String[DICTIONARY_SIZE];
    public ArrayList<Integer> numbers;

    public SpellingBee(String letters) {
        this.letters = letters;
        this.numbers = new ArrayList<>();
        words = new ArrayList<String>();
    }

    public void generate() {
        makeWord(letters, "");
    }

    // Recursively generating every combination of the given letters
    public void makeWord(String input, String creation) {
        for (int i = 0; i < (input.isEmpty() ? 1 : input.length()); i++) {
            if (!creation.isEmpty())
                words.add(creation);
            if (input.isEmpty()) return;
            makeWord(input.substring(0, i) + input.substring(i+1), creation + input.charAt(i));
        }
    }

    // Sorting an ArrayList of Strings
    public void sort() {
        // Copy the words ArrayList to an Array to sort
        String[] wordArray = new String[words.size()];
        for (int i = 0; i < words.size(); i++) {
            wordArray[i] = words.get(i);
        }
        wordArray = mergeSort(wordArray, 0, wordArray.length - 1);
        // Copy the Array back into an ArrayList
        words = new ArrayList<>(Arrays.asList(wordArray));
        for (String i : words) {
            System.out.println(i);
        }
    }

    // Break down an array, then rebuild it to sort it
    public String[] mergeSort(String[] array, int start, int end) {
        if (start >= end) {
            return new String[] {array[start]};
        }
        int mid = (start+end)/2;

        String[] arr1 = mergeSort(array, start, mid);
        String[] arr2 = mergeSort(array, mid+1, end);
        return merge(arr1, arr2);
    }

    // Merge two arrays into one sorted array of Strings
    public String[] merge(String[] arr1, String[] arr2) {
        int i = 0, j = 0;
        String[] out = new String[arr1.length+arr2.length];
        while (i < arr1.length && j < arr2.length) {
            if (arr1[i].compareTo(arr2[j]) < 0) {
                out[i+j] = arr1[i++];
            } else {
                out[i+j] = arr2[j++];
            }
        }
        while (i < arr1.length) {
            out[i+j] = arr1[i++];
        }
        while (j < arr2.length) {
            out[j+i] = arr2[j++];
        }
        return out;
    }

    // Removes duplicates from the sorted list.
    public void removeDuplicates() {
        int i = 0;
        while (i < words.size() - 1) {
            String word = words.get(i);
            if (word.equals(words.get(i + 1)))
                words.remove(i + 1);
            else
                i++;
        }
    }

    // Removes words that aren't found in the Dictionary
    public void checkWords() {
        for (int i = 0; i < words.size(); i++) {
            String word = words.get(i);
            if (!found(word)) {
                words.remove(i--);
            }
        }
    }

    // Proxy function for Binary Search
    public boolean found(String word) {
        return found(word, 0, DICTIONARY_SIZE);
    }

    // Binary Search
    public boolean found(String word, int start, int end) {
        if (start >= end) {
            return false;
        }
        int mid = (start + end) / 2;

        if (word.compareTo(DICTIONARY[mid]) == 0) {
            return true;
        }
        if (word.compareTo(DICTIONARY[mid]) < 0) {
            return found(word, start, mid);
        }
        return found(word, mid+1, end);
    }

    // Prints all valid words to wordList.txt
    public void printWords() throws IOException {
        File wordFile = new File("Resources/wordList.txt");
        BufferedWriter writer = new BufferedWriter(new FileWriter(wordFile, false));
        for (String word : words) {
            writer.append(word);
            writer.newLine();
        }
        writer.close();
    }

    public ArrayList<String> getWords() {
        return words;
    }

    public void setWords(ArrayList<String> words) {
        this.words = words;
    }

    public SpellingBee getBee() {
        return this;
    }

    public static void loadDictionary() {
        Scanner s;
        File dictionaryFile = new File("Resources/dictionary.txt");
        try {
            s = new Scanner(dictionaryFile);
        } catch (FileNotFoundException e) {
            System.out.println("Could not open dictionary file.");
            return;
        }
        int i = 0;
        while(s.hasNextLine()) {
            DICTIONARY[i++] = s.nextLine();
        }
    }

    public static void main(String[] args) {

        // Prompt for letters until given only letters.
        Scanner s = new Scanner(System.in);
        String letters;
        do {
            System.out.print("Enter your letters: ");
            letters = s.nextLine();
        }
        while (!letters.matches("[a-zA-Z]+"));

        // Load the dictionary
        SpellingBee.loadDictionary();

        // Generate and print all valid words from those letters.
        SpellingBee sb = new SpellingBee(letters);
        sb.generate();
        sb.sort();
//        sb.bubbleSort();
        sb.removeDuplicates();
        sb.checkWords();
        try {
            sb.printWords();
        } catch (IOException e) {
            System.out.println("Could not write to output file.");
        }
        s.close();
    }
}
