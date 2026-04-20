import java.util.*;

public class function3 {

	/*
	 * public static void main(String[] args) {
	 * 
	 * 
	 * Scanner sc = new Scanner(System.in); System.out.print("숫자: "); int n =
	 * sc.nextInt(); // 5 입력 System.out.println(n);
	 * 
	 * 
	 * Scanner sc = new Scanner(System.in); System.out.print("숫자: "); int a =
	 * sc.nextInt(); System.out.print("숫자: "); int b = sc.nextInt();
	 * 
	 * System.out.println("더하기: " + sum(a,b)); System.out.println("빼기: " +
	 * minus(a,b)); System.out.println("곱하기: " + multi(a,b));
	 * System.out.println("나누기: " + div(a,b));
	 * 
	 * }
	 * 
	 * public static int sum(int a, int b) { return (a+b); }
	 * 
	 * public static int minus(int a, int b) { return (a-b); }
	 * 
	 * public static int multi(int a, int b) { return (a*b); }
	 * 
	 * public static int div(int a, int b) { return (a/b); }
	 */

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);

		System.out.print("첫 번째 숫자: ");
		int i = sc.nextInt();

		System.out.print("두 번째 숫자: ");
		int j = sc.nextInt();

		System.out.println("덧셈: " + calculate(i, j, '+'));
		System.out.println("뺄셈: " + calculate(i, j, '-'));
		System.out.println("곱셈: " + calculate(i, j, '*'));
		System.out.println("나눗셈: " + calculate(i, j, '/'));
	}

	public static double calculate(int a, int b, char op) {
		switch (op) {
		case '+':
			return a + b;
		case '-':
			return a - b;
		case '*':
			return a * b;
		case '/':
			return (double) a / b;
		default:
			System.out.println("잘못된 연산자");
			return 0;
		}
	}
}
