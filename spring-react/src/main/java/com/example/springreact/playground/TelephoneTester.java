package com.example.springreact.playground;

import lombok.extern.slf4j.Slf4j;

import java.util.Scanner;

@Slf4j
public class TelephoneTester {

    static Scanner SCANNER=new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println(wordCounter("Hello world"));
    }

    private static void telephoneNumberFormatting() {
        String phoneNumber;


        System.out.println("Please enter an unformatted telephone number: ");
        phoneNumber=SCANNER.nextLine();
        System.out.println("FORMATTED: " + Telephone.format(phoneNumber));


        System.out.println("Enter a telephone number formatted as ");
        System.out.print("(XXX)XXX-XXXX: ");
        phoneNumber=SCANNER.nextLine();

        System.out.println("UNFORMATTED: " + Telephone.Unformatted(phoneNumber));
    }

    // #1
    static String backwardString(String input){
        String reverse = " ";
       for (int i=0; i<input.length(); i++){
           char letter=input.charAt(i);
           reverse=letter + reverse;

       }
       return reverse;
    }

    // #2
    static int wordCounter(String word) {
        String[] splitter = word.split("\\s+");
        return splitter.length;
    }

}
