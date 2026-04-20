import java.util.Arrays;

public class array {

	public static void main(String[] args) {
		int[] num = {10, 11, 12, 13, 14, 15};
		String[] weekDay = {"월", "화", "수", "목", "금", "토", "일"};
		
		for(int i=0; i<num.length; i++) {
			System.out.println(num[i]);
		}
		for(int i=0; i<weekDay.length; i++) {
			System.out.println(weekDay[i]);
		}
		
		Arrays.sort(num);
		
		// 2. new 키워드로 크기 지정(기본값으로 초기화)
		int[] arr = new int[5];      // 0으로 초기화됨
		String[] str = new String[7]; // null로 초기화됨
	}

}
