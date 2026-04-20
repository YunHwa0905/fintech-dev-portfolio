import java.util.*;

public class random {

	public static void main(String[] args) {

		
		Random rand = new Random();
        int num = rand.nextInt(22) + 1; // 1 ~ 22
        System.out.println(num);
        
	}

}