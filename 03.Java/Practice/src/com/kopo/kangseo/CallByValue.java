package com.kopo.kangseo;


public class CallByValue {
	static void change(int x) {
		x = 100; // 복사본 x만 변경
	}

	public static void main(String[] args) {
		int a = 10;
		change(a);
		System.out.println(a); // 여전히 10
	}
}