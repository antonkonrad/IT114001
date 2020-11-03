
public class JavaLoopsHW {
	public static void main(String[] args) {

		int[] intArray = new int[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };
		for (int i = 0; i < intArray.length; i++) {
			if (intArray[i] % 2 == 0) {
				System.out.println(intArray[i]);
			}
			// To Achieve this output i looked back at my notes in cs113 on
			// how to print out even numbers and used that logic with
			// a basic array and for loop to print out the desired content
			// i made sure i did it in a compact amount of lines for simplicity sake
		}
	}
}