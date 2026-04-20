package quiz;

public class array {
	public static void main(String[] args) {
		int[] arr = { 1, 3, 3, 4, 5, 2, 6, 7, 6, 7 };
		int[] result = new int[10];
		int count = 0;
		boolean numIs = true;

		System.out.println("중복 제거 과정");

		// 이중 for문을 활용하여 중복 제거
		for (int i = 0; i < arr.length; i++) {
			for (int j = 0; j < i + 1; j++) {
				numIs = true;

				// 중복이 있다면 numIs를 flase로  하고 멈추기
				if (arr[i] == result[j]) {
					numIs = false;
					break;
				}
			}

			// numIs로 중복이라면 추가하지 않고 중복이 아니면 result에 추가
			if (numIs) {
				result[count++] = arr[i];
				System.out.println(arr[i] + " 추가됨");
			} else {
				System.out.println(arr[i] + " 중복이라 제거됨");
			}
		}

		System.out.println();

		// 버블 정렬을 이용하여 오름차순으로 정리
		for (int i = 0; i < count - 1; i++) {
			for (int j = 1; j < count - i; j++) {
				
				// 만약 앞 수가 뒤보다 크다면 swap 적용
				if (result[j - 1] > result[j]) {
					System.out.println("swap 발생: " + result[j - 1] + " <-> " + result[j]);

					int temp = result[j];
					result[j] = result[j - 1];
					result[j - 1] = temp;
				}
			}
		}
	}
}
