package com.kopo.kangseo;

import java.io.*;
import java.util.*;

public class quiz {

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());

		int rabbit_num = Integer.parseInt(st.nextToken());
		int fox_x = Integer.parseInt(st.nextToken());
		int fox_y = Integer.parseInt(st.nextToken());
		int count = 0;

		for (int i = 0; i < rabbit_num; i++) {
			int rabbit_x = Integer.parseInt(st.nextToken());
			int rabbit_y = Integer.parseInt(st.nextToken());

			if (rabbit_x == fox_x || rabbit_y == fox_y || fox_x - rabbit_x == fox_y - rabbit_y) {
				count++;
			}

		}

		System.out.print(count);

	}

}
