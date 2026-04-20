package com.kopo.kangseo;


class Singleton {

    // 1. 유일한 인스턴스 생성
    private static final Singleton instance = new Singleton();

    // 2. 외부에서 생성 못하게 막기
    private Singleton() {}

    // 3. 접근 메서드
    public static Singleton getInstance() {
        return instance;
    }
}


public class SingletonEx {
	public static void main(String[] args) {
		 Singleton s1 = Singleton.getInstance();
	     Singleton s2 = Singleton.getInstance();
	
	     System.out.println(s1 == s2); // true (같은 객체)
	}
}