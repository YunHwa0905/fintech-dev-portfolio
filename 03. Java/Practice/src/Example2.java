
public class Example2 {

	public static void main(String[] args) {
		
		// 1. 기본 논리 연산
		boolean a = (5 == 5 || 4 < 1); // true
		boolean b = (5 == 5 && 4 < 1); // false
		boolean c = !(5 == 5); // false
		System.out.println(a);
		System.out.println(b);
		
		// 2. 단락 평가 (중요!)
		// 앞이 false이므로 뒤(10/0)는 실행 안 됨
		if (false && (10 / 0 > 1)) {
			System.out.println("실행 안됨");
		} else {
			System.out.println("에러 없음");
		}
	}
}
