package com.kopo.kangseo;


abstract class Shape {
	String color;

	abstract double area(); // 1. 추상 메소드: 구현 {} 없음

	void setColor(String c) {
		this.color = c;
	} // 일반 메소드 포함 가능
}

class Rect extends Shape {
	double w, h;

	Rect(double w, double h) {
		this.w = w;
		this.h = h;
	}

	@Override
	double area() { // 2. 부모의 추상 메소드 필수 구현
		return w * h;
	}
	
	 @Override
    public String toString() {
        return " User{w=' " + w + "', h=" + h + "}";
    }
}

public class abstractExample {
	public static void main(String[] args) {
		Rect s = new Rect(10, 20); // 다형성 활용 가능
		Rect s2 = new Rect(10, 20); // 다형성 활용 가능
		
		System.out.println(s.area());
		System.out.println(s.toString());
		System.out.println(s.hashCode());
		System.out.println(s2.toString());
		System.out.println(s2.hashCode());
		
		System.out.println(s.toString());
	}
}