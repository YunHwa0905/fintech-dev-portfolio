public class ExampleArray {

	public static void main(String[] args) {
	
		// 1. 선언 + 생성 (공간만 확보)
		// int형 공간 5개 (0으로 자동 초기화)
		int[] arr1 = new int[5];
		
		// 2. 선언 + 초기화 (값 지정)
		int[] arr2 = {10, 20, 30, 40, 50};
		
		// 3. 값 접근 및 변경
		System.out.println(arr2[0]); // 10
		arr2[0] = 100;
		System.out.println(arr2[0]); // 100
		
		// 4. 배열 길이 (length)
		// 인덱스 범위: 0 ~ (length - 1)
		System.out.println(arr2.length); // 5
		// 주의: 인덱스 범위 초과시 예외 발생!
		// arr2[5] -> ArrayIndexOutOfBoundsException
		
		

	}

}