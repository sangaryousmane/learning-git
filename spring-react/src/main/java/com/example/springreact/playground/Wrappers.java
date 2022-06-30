package com.example.springreact.playground;

import java.util.Scanner;

public class Wrappers {

    private static final Scanner SCANNER = new Scanner(System.in);

    public static void main(String[] args) {
//        String word = "Four Score and seven years ago.";
//        stringManipulation(word);

//        System.out.println("Laye Ousmane Sangary".substring(5, 13));
//        isStartWith();

        stringBuilder();
    }

    static String letterCheck(String value) {
        String status = "";
        char letter = value.charAt(0);


        if (Character.isDigit(letter)) {
            status = "This is a digit";
        }
        if (Character.isLetter(letter)) {
            status = "This is a letter";
        }
        if (Character.isLowerCase(letter)) {
            status = "This is a lowercase";
        }
        if (Character.isLowerCase(letter)) {
            status = "This is alphabetic";
        }
        return status;
    }

    static void amountOfUpperCase(String word) {
        int totalUpperCase = 0, totalLowerCase = 0;
        for (int i = 0; i < word.length(); i++) {
            if (Character.isUpperCase(word.charAt(i)))
                totalUpperCase++;

            if (Character.isLowerCase(word.charAt(i)))
                totalLowerCase++;
        }
        System.out.println("Number of lower case letters are: " + totalLowerCase);
        System.out.println("Number of upper case letters are: " + totalUpperCase);
    }

    static void stringManipulation(String word) {

        if (word.endsWith("ago.")) {
            System.out.println("The sentence ends with ago");
        }
        else {
            System.out.println("The sentence doesn't end with ago");
        }
    }

    static void isStartWith(){
        String[] people = {
                "Cutshaw, Will", "Davis, George",
                "Davis, Jenny", "Russert, Phil",
                "Russell, Cindy", "Setzer, Charles",
                "Smathers, Holly", "Smith, Chris",
                "Smith, Brad", "Williams, Jean",
                "Ousmane Sangary", "Francis Wesley"};

        System.out.println("Please enter the beginning characters of any name.");
        String lookUp=SCANNER.nextLine();

        for (String person : people) {
            if (person.startsWith(lookUp)) {
                System.out.println(person);
                System.out.println(person.substring(8));
            }
        }

        SCANNER.close();
        System.exit(0);

    }

    static boolean isAnArgument(String obj){
        return obj.endsWith("ger") || obj.endsWith("Ger");
    }

    static void stringBuilder(){
        StringBuffer city=new StringBuffer("Asheville");
        StringBuilder builder=new StringBuilder("Game");
        System.out.println(builder.append(" is going to school in China..."));
        builder.insert(4, " Gradle");
        System.out.println(city.append(" is located west copenhagen."));
        city.insert(9, " is a city that");
        System.out.println(city);
        city.delete(9, 24);
        System.out.println(city);

    }
}

/*
Leading and trailing white space characters in trim():
one of the use of the trim() is to remove any leading or trailing spaces
the user might enter while inputting data.

The String class has several overloaded versions of the valueOf() method that
accepts the value of any primitive data type as its argument and returns the
string representation of the value.
 */
