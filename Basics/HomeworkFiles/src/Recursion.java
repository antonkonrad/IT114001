import java.util.Scanner;

public class Recursion {
	static int number = 0;
	static int total = 0;
	static int j;
	static Scanner scan = new Scanner(System.in);
	static int inputnumber;

	public static int sum(int num) {
		if (num > 0) {
			return num + sum(num - 1);
		}
		return 0;
	}

	public static void main(String[] args) {
		System.out.println(sum(10));

		System.out.println("Please pick a number: ");
		inputnumber = scan.nextInt();

		for (int i = 1; i > 0 && i != inputnumber + 1; i++) {
			number += 1;
			if (number > 0) {
				total += number;
				if (number == inputnumber) {
					System.out.println(total);
				}
			}

		}
	}
}