package com.kopo.kangseo;

import java.util.Arrays;

public class ArrayExample {

	public static void main(String[] args) {
		// 1. 참조 (Reference)
		int[] a = {1, 2, 3};
		int[] b = a; // 주소 복사 (얕은 복사)
		b[0] = 10;
		System.out.println(a[0]); // 10 (a도 바뀜)
		// 2. 깊은 복사 (Deep Copy)
		int[] c = new int[3];
		// a배열 0번부터 c배열 0번으로 3개 복사
		System.arraycopy(a, 0, c, 0, 3);
		c[0] = 999;
		System.out.println(a[0]); // 10 (a 영향 없음)
		// 3. 정렬 (Sort)
		int[] arr = {5, 1, 9, 3};
		Arrays.sort(arr); // [1, 3, 5, 9]
		System.out.println(Arrays.toString(arr));
		System.out.println(c[0]);
		
		int[][] src = {{1,2,3}, {4,5,6}};
		int[][] dest = new int[2][4];
		
		for(int i=0; i<src.length; i++) {
			System.arraycopy(src[i],0,dest[i],0,src[i].length);
		}
		
		System.out.println(Arrays.deepToString(dest));
	}

}