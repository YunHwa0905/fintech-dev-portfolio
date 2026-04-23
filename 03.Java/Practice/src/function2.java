
public class function2 {

	public static void main(String[] args) {
		System.out.println(add(2,3));
		System.out.println(isAdult(20));
	}
	
	// 1. 더하기 기능 (입력:정수2개, 출력:합)
	public static int add(int a, int b) {
		return a + b;
	}

	// 2. 성인 여부 (입력:나이, 출력:참/거짓)
	public static boolean isAdult(int age) {
		return age >= 18;
	}

	// 3. 메시지 출력 (입력:문자열, 출력:없음)
	public static void printMsg(String msg) {
		System.out.println(msg);
	}

}
