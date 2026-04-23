package com.kopo.kangseo;

class CallByReference{
	static void change(int[] arr) {
			arr[0] = 100; // 내부 값 변경
	}
	public static void main(String[] args) {
			int[] a = {10, 20, 30};
			change(a);
			System.out.println(a[0]);
	}


}