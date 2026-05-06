package com.kopo.score;

import java.util.*;

public class Main {

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		
		List<Student> list = new ArrayList<>();
		
		list.add(new Student("홍길동", 90, 80, 70));
        list.add(new Student("김철수", 85, 95, 90));
        list.add(new Student("이순신", 100, 100, 100));
        list.add(new Student("강감찬", 75, 88, 92));
        list.add(new Student("유관순", 88, 72, 85));
        list.add(new Student("장보고", 90, 80, 70));

		/*
		 * Collections.sort(list, new KoreanComparator());
		 * System.out.println("\n[국어 기준 정렬]"); list.forEach(System.out::println);
		 * 
		 * Collections.sort(list, new EnglishComparator());
		 * System.out.println("\n[영어 기준 정렬]"); list.forEach(System.out::println);
		 * 
		 * Collections.sort(list, new MathComparator());
		 * System.out.println("\n[수학 기준 정렬]"); list.forEach(System.out::println);
		 * 
		 * Collections.sort(list, new TotalComparator());
		 * System.out.println("\n[총점 기준 정렬]"); list.forEach(System.out::println);
		 */
        
        // ===== 람다 방식 =====
        System.out.println("\n===== 람다 방식 =====");

        Collections.sort(list, (a, b) -> b.korean - a.korean);
        System.out.println("\n[국어 기준 정렬]");
        list.forEach(System.out::println);

        Collections.sort(list, (a, b) -> b.english - a.english);
        System.out.println("\n[영어 기준 정렬]");
        list.forEach(System.out::println);

        Collections.sort(list, (a, b) -> b.math - a.math);
        System.out.println("\n[수학 기준 정렬]");
        list.forEach(System.out::println);

        Collections.sort(list, (a, b) -> b.getTotal() - a.getTotal());
        System.out.println("\n[총점 기준 정렬]");
        list.forEach(System.out::println);
        
     // ===== 3. 정렬 방향 선택 =====
        System.out.println("\n========== 정렬 방향 선택 ==========");
        System.out.print("정렬 기준 입력 (korean / english / math / total): ");
        String criteria = sc.nextLine().trim();

        System.out.print("정렬 방향 입력 (asc / desc): ");
        String direction = sc.nextLine().trim();
        boolean ascending = direction.equals("asc");

        printSortedWithDirection(list, criteria, ascending);

        // ===== 4. 상위 N명 출력 =====
        System.out.println("\n========== 상위 N명 출력 ==========");
        System.out.print("상위 몇 명 출력할까요? (숫자 입력): ");
        int n = sc.nextInt();
        sc.nextLine();

        printTopN(list, n);

        // ===== 5. 등수 매기기 =====
        System.out.println("\n========== 등수 매기기 (동점 처리) ==========");
        printRank(list);

        sc.close();
    }

    static void printSorted(List<Student> list, Comparator<Student> comp, String label) {
        Collections.sort(list, comp);
        System.out.println("\n" + label);
        list.forEach(System.out::println);
    }

    static void printSortedWithDirection(List<Student> list, String criteria, boolean ascending) {
        Comparator<Student> comp;

        switch (criteria) {
            case "korean"  -> comp = Comparator.comparingInt(s -> s.korean);
            case "english" -> comp = Comparator.comparingInt(s -> s.english);
            case "math"    -> comp = Comparator.comparingInt(s -> s.math);
            default        -> comp = Comparator.comparingInt(Student::getTotal);
        }

        if (!ascending) comp = comp.reversed();

        list.sort(comp);
        System.out.println("\n[" + criteria + " 기준 " + (ascending ? "오름차순" : "내림차순") + " 정렬]");
        list.forEach(System.out::println);
    }

    static void printTopN(List<Student> list, int n) {
        list.sort((a, b) -> b.getTotal() - a.getTotal());

        if (n >= list.size()) {
            System.out.println("입력한 수(" + n + ")가 전체 인원 이상입니다. 총 인원은 " + list.size() + "명입니다.");
            System.out.println("\n[전체 학생 출력]");
            list.forEach(System.out::println);
        } else {
            System.out.println("\n[총점 기준 상위 " + n + "명]");
            list.stream()
                .limit(n)
                .forEach(System.out::println);
        }
    }

    static void printRank(List<Student> list) {
        list.sort((a, b) -> b.getTotal() - a.getTotal());
        System.out.println("\n[등수 (동점 처리 포함)]");

        int rank = 1;
        for (int i = 0; i < list.size(); i++) {
            if (i > 0 && list.get(i).getTotal() != list.get(i - 1).getTotal()) {
                rank = i + 1;
            }
            System.out.println(rank + "등 " + list.get(i));
        }
    }

}
