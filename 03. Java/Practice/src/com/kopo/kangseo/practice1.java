package com.kopo.kangseo;

import java.util.Arrays;

public class practice1 {

	public static void main(String[] args) {
		/*
		 * // TODO Auto-generated method stub int sum =0; for(int i=1; i<=100; i++) {
		 * sum += i; }
		 * 
		 * System.out.println(sum);
		 */
		
		System.out.println(myFunc(1));
		
		System.out.println(factorial(5));
		
		System.out.println(fibo(3));
	}
	
	public static int myFunc(int i) {
		if(i == 100) return 100;
		return i + myFunc(++i);
	}
	
	public static int factorial(int i) {
		if( i == 1) return 1;
		return i*factorial(--i);
	}
	
	public static int fibo(int i) {
		if(i<1) return i;
		return fibo(i-1) + fibo(i-2);
	}

}
