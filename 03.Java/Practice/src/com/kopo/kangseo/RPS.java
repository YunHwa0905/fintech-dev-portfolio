package com.kopo.kangseo;

import java.util.*;

class winCount {
	int userWin = 0;
	int comWin = 0;
	int draw = 0;

}

public class RPS {

	static Scanner sc = new Scanner(System.in);

	public static void main(String[] args) {
		Random r = new Random();
		winCount wc = new winCount();
		int count = 1;

		while (true) {
			System.out.println(count + "번째 게임");
			String input = getValidInput();

			if (input.equals("q")) {
				double rate = (double) wc.userWin / (count - 1) * 100;

				System.out.println("--------------------------");
				System.out.println("게임 종료");
				System.out.println("🏆게임 결과🏆");
				System.out.println("사용자 승리: " + wc.userWin + "회");
				System.out.println("컴퓨터 승리: " + wc.comWin + "회");
				System.out.println("무승부: " + wc.draw + "회");
				System.out.printf("승률: %.0f%%", rate);
				break;
			} else {
				count++;
			}

			int com = r.nextInt(3);
			System.out.println("COM: " + getRps(com));

			int user = Integer.parseInt(input);
			System.out.println("USER: " + getRps(user));
			checkWinner(com, user, wc);
			System.out.println("");
			System.out.println("");

		}

	}

	public static String getValidInput() {
		while (true) {
			System.out.print("0, 1, 2 중 하나를 입력하세요(종료를 원하시면 q를 입력하세요): ");
			String input = sc.nextLine();

			if (input.equals("0") || input.equals("1") || input.equals("2") || input.equals("q")) {
				return input;
			} else {
				System.out.println("입력이 잘못되었습니다.");
				System.out.println("");
			}
		}
	}

	public static String getRps(int a) {
		switch (a) {
		case 0:
			return "✌️";
		case 1:
			return "✊";
		case 2:
			return "🖐️";
		default:
			return "";
		}
	}

	public static void checkWinner(int a, int b, winCount wc) {
		if (a == b) {
			wc.draw++;
			System.out.println("무승부");
		} else if ((a == 0 && b == 1) || (a == 1 && b == 2) || (a == 2 && b == 0)) {
			wc.userWin++;
			System.out.println("사용자 승리");
		} else {
			wc.comWin++;
			System.out.println("컴퓨터 승리");
		}
	}

}
