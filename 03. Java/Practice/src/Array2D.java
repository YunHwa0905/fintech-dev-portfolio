
public class Array2D {

	public static void main(String[] args) {
		/*
		 * // 1. 값으로 초기화 (2행 3열) int[][] arr = { { 10, 33, 44 }, // 0번 행 (index 0) { 27,
		 * 5, 98 } // 1번 행 (index 1) };
		 * 
		 * // 2. new 키워드로 크기 지정 (3행 4열) int[][] grid = new int[3][4]; // 모든 요소 0으로 초기화
		 */

		int[][] arr = { { 10, 33, 44 }, { 27, 5, 98 } }; // 2행 3열 배열
		// i는 행(Row) 인덱스: 0부터 arr.length(2) 미만까지
		for (int i = 0; i < arr.length; i++) {
			// j는 열(Column) 인덱스: 0부터 arr[i].length(3) 미만까지
			for (int j = 0; j < arr[i].length; j++) {
				System.out.print(arr[i][j] + " ");
			}
			System.out.println(); // 한 행이 끝나면 줄바꿈
		}

	}

}
