package com.shopme.admin.category.util;

import org.springframework.boot.autoconfigure.session.StoreType;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Scanner;

public class p {

    static int[] pendingArray = new int[10];
    private final int number;

    public p(int n) {
        this.number = n;
    }

    public static void main(String[] args) throws FileNotFoundException {
        executeSequence();
    }

    private static void executeSequence() {
        int[] tests = { 87, 75, 98, 100, 82 };
        int results=sequentialSearch(tests, 100);

        System.out.println(results);
    }

    private static void writeToFile() throws FileNotFoundException {
        pendingArray = new int[]{10, 20, 30, 40, 50, 60, 70, 80, 90, 100};
        PrintWriter writer = new PrintWriter("values.txt");

        for (int j : pendingArray) {
            writer.println(j);
        }
        writer.close();
    }


    static void readAndWrite() throws FileNotFoundException {
        int index = 0;
        File file = new File("values.txt");
        Scanner input = new Scanner(file);

        // The input.hasNext() is called to make sure there's a value remaining in the file.
        while (input.hasNext() && index < pendingArray.length) {
            pendingArray[index] = input.nextInt();
            index++;
        }
        System.out.println(Arrays.toString(pendingArray));
        input.close();

    }

    // We can also return arrays from methods
    static String[] getDoubleArrays() {
        String[] strings = {"Mercury”, “Venus”, “Earth”,“Mars"};

        return strings;
    }

    static void createP(p[] pArray) {

        for (int i = 0; i < pArray.length; i++) {
            pArray[i] = new p(5);
        }
    }

    static int sequentialSearch(int[] array, int value) {
        int index;   // Loop Control Variable
        int element; // Element the value is found at
        boolean found; // Flag indicating search results

        index = 0; // Starting point of the search
        element = -1; // Default Value
        found = false; // Default value

        while (!(found) && index < array.length){
            if (array[index] == value){
                found=true;
                element=index;
            }
            index++;
        }
        return element;
    }

    static void twoDimensionalArrays(){
        int [][]  _2DArrays={{1, 2, 3, 4}, {5, 6, 7, 8, 13}, {9, 10, 11,12}};
        System.out.println("The number of rows is: " + _2DArrays.length);

        for (int i=0; i<_2DArrays.length; i++){
            System.out.println("The number of columns in row: "+i+ " is "+_2DArrays[i].length);
        }
    }

    static void traversingThroughA2DArray(String input){
        int [][]  _2DArrays={{1, 2, 3, 4}, {5, 6, 7, 8}, {9, 10, 11,12}};
        int total=0;


        if (input.equalsIgnoreCase("traverse")) {
            // Looping through a 2D array with nested loops.
            for (int row = 0; row < _2DArrays.length; row++) {
                for (int col = 0; col < _2DArrays[row].length; col++) {
                    System.out.println(_2DArrays[row][col]);
                }
            }
        }
        else if(input.equalsIgnoreCase("sum")){
            for (int row=0; row<_2DArrays.length; row++){
                for (int col=0; col<_2DArrays[row].length; col++){
                    total +=_2DArrays[row][col];
                }
            }
            System.out.println("The total sum is: " + total);
        }
    }
}

// -1 is return when the search value isn't found in the array.
