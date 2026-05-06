package com.kopo.score;

import java.util.Comparator;

class Student{
	String name;
	int korean, english, math;
	
	public Student(String name, int k, int e, int m) {
		this.name = name;
		this.korean = k;
		this.english = e;
		this.math = m;
	}
	
	public int getTotal() {
		return korean + english + math;
	}
	
	public double getAvg() {
		return getTotal() / 3.0;
	}
	
	@Override
	public String toString() {
		return name + " " + korean + " " + english + " " + math + " " + "총점: " + getTotal();
	}
}

class KoreanComparator implements Comparator<Student>{
	public int compare(Student a, Student b) {
		return b.korean - a.korean;
	}
}

class EnglishComparator implements Comparator<Student>{
	public int compare(Student a, Student b) {
		return b.english - a.english;
	}
}

class MathComparator implements Comparator<Student>{
	public int compare(Student a, Student b) {
		return b.math - a.math;
	}
}

class TotalComparator implements Comparator<Student>{
	public int compare(Student a, Student b) {
		return b.getTotal() - a.getTotal();
	}
}
public class data {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
