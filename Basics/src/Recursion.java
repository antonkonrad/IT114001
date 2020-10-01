public class Recursion {
	static int number = 0;
	static int total = 0;
	static int j;

	public static int sum(int num) {
		if (num > 0) {
			return num + sum(num - 1);
		}
		return 0;
	}

	public static void main(String[] args) {
		System.out.println(sum(10));

		for (int i = 1; i > 0 && i != 11; i++) {
			number += 1;
			if (number > 0) {
				total += number;
				if (number == 10) {
					System.out.println(total);
				}
			}

		}
	}
}