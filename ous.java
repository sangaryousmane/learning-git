package com.object.oriented.ousmane;

import java.util.Scanner;

interface Swan{
	    int calculate(int number);
}

class IntCalculate implements Swan{

	    public int calculate(int number){

		            return number * number;
			        }
}

public class AbsInt {

	    public static void main(String[] args) {

		            Scanner scanner=new Scanner(System.in);

			            //This is an Inner class
				    //        IntCalculate calcul=new IntCalculate(){
				    //                    public int calculate(int n){
				    //                                    return n * n;
				    //                                                }
				    //                                                        };
				    //
				    //                                                            System.out.println("ENTER YOUR DESIRE INTEGER");
				    //                                                                int user=scanner.nextInt();
				    //
				    //                                                                            System.out.println("The squared is= " + calcul.calculate(user));
				    //                                                                                }
				    //                                                                                }
				    //
				    //
				    //
				    //
