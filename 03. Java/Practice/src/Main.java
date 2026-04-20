import java.util.*;

public class Main {

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		
		System.out.println("첫 번째 숫자: ");
		int a = sc.nextInt();
		System.out.println("두 번째 숫자: ");
		int b = sc.nextInt();
		
		System.out.println("덧셈: " + (a+b));
		System.out.println("나눗셈: " + ((double) a/b));
		
		System.out.println("국어: ");
		int kor = sc.nextInt();
		System.out.println("영어: ");
		int eng = sc.nextInt();
		System.out.println("수학: ");
		int math = sc.nextInt();
		
		double avg = (kor+eng+math) / 3.0;
		System.out.printf("평균: %.2f\n", avg);

	}

}
