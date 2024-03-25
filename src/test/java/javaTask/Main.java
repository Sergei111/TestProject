package javaTask;

import java.util.ArrayList;
import java.util.List;

public class Main {
    //here's the Java method to find all of the longest strings in a given array of strings
    public static List<String> findLongestStrings(String[] strings) {
        List<String> longestStrings = new ArrayList<>();
        int maxLength = 0;
        for (String str : strings) {
            int length = str.length();
            if (length > maxLength) {
                maxLength = length;
                longestStrings.clear();
                longestStrings.add(str);
            } else if (length == maxLength) {
                longestStrings.add(str);
            }
        }
        return longestStrings;
    }
    public static void main(String[] args) {
        String[] input = {"cat", "dog", "red", "is", "am"};
        List<String> longestStrings = findLongestStrings(input);
        System.out.println("Longest strings: " + longestStrings);
    }
}