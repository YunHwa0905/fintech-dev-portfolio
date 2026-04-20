public class Hello {
	/*
	 * 전역변수 클래스 내에서 어디서든 사용 가능
	 */
	static int age1 = 3;

	public static void main(String[] args) {

		/*
		 * 지역변수 함수 내에서만 사용 가능 원시 자료형
		 */
		int age = 100;
		double _abc = 3.14;
		float f = 3.145f;
		boolean b = true; // or false
		char myChar = '가';
		char myChar2 = 0xAC00;

		int a = 10;
		double bb = a;
		double c = 10.45;
		int d = (int) c;
		
		float p = 1.1f;
		double t = 1.1;

		// 래퍼런스 타입의 자료형
		String str = "abc";

		System.out.println(age1);
		System.out.println(age);
		System.out.println(_abc);
		System.out.println(f);
		System.out.println(str);
		System.out.println(b);
		System.out.println(myChar);
		System.out.println(myChar2);
		System.out.println(bb);
		System.out.println(d);
		
		System.out.printf("float: %.20f\n", p);
		System.out.printf("double: %.20f\n", t);
		

	}

}
