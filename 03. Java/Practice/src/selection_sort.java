
public class selection_sort {

	public static void main(String[] args) {
		int[] array = {3,6,75,32,543,7,56};
		
		for(int i=0; i<array.length; i++) {
			for(int j=i+1; j<array.length; j++) {
				if(array[i]>array[j]) {
					int temp = array[i];
					array[i] = array[j];
					array[j] = temp;
				}
			}
		}
		
		for(int i=0; i<array.length; i++) {
			if( i != array.length-1) {
				System.out.print(array[i] + " ");
			}else {
				System.out.print(array[i]);
			}
		}

	}

}
