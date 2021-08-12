import java.util.Locale;
import java.util.Scanner;

public class CityGame {
	    public static void main(String[] args) {

		            int totalStudent=23;

			            Scanner scanner=new Scanner(System.in);
				            System.out.println("Please enter the amount of males in CST2");
					            double Males=scanner.nextDouble();

						            System.out.println("Please enter the amount of females in CST2");
							            double Females=scanner.nextDouble();

								            System.out.println("The percent of Males is: " + (Males/totalStudent)*100);
									            System.out.println("The percent of females is: " + (Females/totalStudent)*100);


										            String name="Ibrahim Sangary";
											            String city="Nanchang";
												            String school="Jiangxi University of Technology";

													            System.out.println("My name lenght is: " +name.length());
														            System.out.println("My City name in upper and lowercase is: Uppercase: " +city.toUpperCase()+"\n " +
																	                    "In Lowercase is: " +city.toLowerCase());
															            System.out.println("My school name is in manipulation is: " + school.trim());
																            scanner.close();

																	        }
}
