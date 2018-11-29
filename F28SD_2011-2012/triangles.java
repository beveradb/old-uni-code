import java.io.*;
import java.util.Scanner;
public class triangles {
	// Empty constructor
	public triangles(){}
	// Everything is in the main method for easier measurement of complexity
	public static void main(String[] args) throws IOException {
		// Load input file
		File inputFile = new File("sizes.dat");
		// Open File
		Scanner inputFileScanner = new Scanner(inputFile);
		// Parse the first Integer
		int triangleCount = Integer.parseInt(inputFileScanner.nextLine());
		// Multidimensional array to store info about all triangles (3 side lengths and position of hypotenuse)
		int[][] trianglesArray = new int[triangleCount][4];
		// Ensure we actually have some triangles
		if(triangleCount <= 0) { inputFileScanner.close(); System.out.println("No triangles!"); System.exit(0); }
		// Init vars
		int hypotenuse = 0;
		// For each triangle in the input file:
		for(int currentLine=0; currentLine<triangleCount; currentLine++){
			// Load triangle side lengths
			String triangleData = inputFileScanner.nextLine();
			// Delete spaces
			triangleData = triangleData.replaceAll(" *", "");
			// Ensure triangle data line has 3 sides
			if(triangleData.length()!=3) {
				System.out.println("Triangle number "+currentLine+" is not a triangle!\b");
				continue;
			}
			// Reset hypotenuse for new triangle
			hypotenuse = 0;
			// Go through each character on the line
			for(int currentCharacter=0; currentCharacter<3; currentCharacter++) {
				// Parse the side length to a int from the data
				int triangleSide = Integer.parseInt(triangleData.substring(currentCharacter,currentCharacter+1));
				// If the side described at the current position is the largest side:
				if(triangleSide > hypotenuse){
					// Update the hypotenuse value in case there is a larger side to come
					hypotenuse = triangleSide;
					// Store current position as the hypotenuse
					trianglesArray[currentLine][3] = currentCharacter;
				}
				// Store current side value as int in main triangles array
				trianglesArray[currentLine][currentCharacter] = triangleSide;
			}
		}
		// We should now have all triangle data in the Array ready for calculations - close the input file.
		inputFileScanner.close();
		// Open output file (results.dat) for writing to
		PrintWriter outputFile = new PrintWriter(new BufferedWriter(new FileWriter("results.dat")));
		// Loop through all triangles in array
		for(int triangleCounter=0; triangleCounter<trianglesArray.length; triangleCounter++) {
			// Get sides
			int A = trianglesArray[triangleCounter][0];
			int B = trianglesArray[triangleCounter][1];
			int C = trianglesArray[triangleCounter][2];
			// String to store conclusion about triangle type
			String TriType;
			
			// Complicated maths-y stuff here
			int MinVal;
			if (A<B) {
				if (A<C) MinVal = A;
				else MinVal = C;
			} else {
				if (B<C) MinVal = B;
				else MinVal = C;
			}
			int MaxVal;
			int negA = -A; int negB = -B; int negC = -C;
			if (negA<negB) {
				if (negA<negC) MaxVal = negA;
				else MaxVal = negC;
			} else {
				if (negB<negC) MaxVal = negB;
				else MaxVal = negC;
			}
			MaxVal = -MaxVal;
			int MidVal = A+B+C - MinVal - MaxVal;	
			// Final deductions of triangle type based on minimum and maximum side lengths
			if (MinVal<1 || MinVal+MidVal<=MaxVal) {
				TriType= "Not a triangle";
			} else {
				if (MinVal==MidVal && MaxVal==MidVal) {
					TriType= "Equilateral";
				} else {
					if (MinVal==MidVal || MaxVal==MidVal) {
						TriType="Isosceles";
					} else {
						TriType="Scalene";
					}
				}
				// a^2+b^2=c^2, hooray for high school maths!
				if (MinVal*MinVal + MidVal*MidVal == MaxVal*MaxVal) {
					TriType = TriType + ", Right";
				}
			}
			// Print results of check to file
			outputFile.println("Triangle "+triangleCounter+" ("+A+","+B+","+C+") : "+TriType );
		}
		// Write output to file
		outputFile.close();
		// Be courteous
		System.out.println("Thanks for using the triangle checker. Please see file results.dat for the output of this run.");
		// Quit
		System.exit(0);
	}
}
