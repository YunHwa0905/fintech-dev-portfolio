
public class Example5 {

	public static void main(String[] args) {
		
		while(조건문이 참일 동안) {
			// 이 문장이 수행됨
		}
		
		int i = 0;
		int sum = 0;
		
		while(i<=100) {
             sum += i;
             i++;
		}
		
		System.out.println(sum);
		
		for(;;) {  // while(true)
			if(i>100) break;
			sum += i;
            i++;
		}
		
		System.out.println(sum);
		
		while(true) {  // while(true)
			if(i>100) break;
			sum += i;
            i++;
		}
		
		System.out.println(sum);

		do {
			if(i>100) break;
			sum += i;
            i++;
		}while(true);

	}

}
