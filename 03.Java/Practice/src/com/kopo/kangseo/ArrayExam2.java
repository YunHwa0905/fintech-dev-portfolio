package com.kopo.kangseo;

import java.util.*;

public class ArrayExam2 {

	public static void main(String[] args) {
		int[][] arr = new int[5][5];

		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < 5; j++) {
				arr[i][j] = j;
			}
		}

		for (int i = 0; i < 5; i++) {
			System.out.printf("(%d,%d)", i, arr[i][2]);
		}
		System.out.println("");
		
		for (int i = 0; i < 5; i++) {
			System.out.printf("(%d,%d)", 2, arr[2][i]);
		}
		System.out.println("");
		System.out.println("");

		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < 5; j++) {
				if (i == j) {
					System.out.printf("(%d,%d)", i, arr[i][j]);
				}
			}
		}
		
		System.out.println("");
		
		for (int i=4; i>=0; i--) {
			for(int j=0; j<5; j++) {
				if(i+j == 4) {
					System.out.printf("(%d,%d)", i, arr[i][j]);
				}
			}
		}

	}

}
