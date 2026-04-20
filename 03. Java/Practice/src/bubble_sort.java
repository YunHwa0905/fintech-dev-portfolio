
public class bubble_sort {

	public static void main(String[] args) {
		int[] array = {3,6,75,32,543,7,56};
		
		for(int i=0; i< array.length-1; i++) {
			for(int j=1; j<array.length-i; j++) {
				if(array[j-1]>array[j]) {
					int temp = array[j-1];
					array[j-1] = array[j];
					array[j] = temp;
				}
			}
		}
		
		for(int i=0; i<array.length; i++) {
			if(i != array.length-1) {
				System.out.print(array[i] + " ");
			}else {
				System.out.print(array[i]);
			}
		}
	}

}
