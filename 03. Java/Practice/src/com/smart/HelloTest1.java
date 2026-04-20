package com.smart;
import com.kopo.Hello;

class Child extends Hello{
	public void test() {
		System.out.println("== 상속 받은 경우 ==");
		publicMethod(); // ○ 가능
		protectedMethod(); // ○ 가능
		// defaultMethod();  ○ 불가
		// privateMethod();  ○ 불가
	}
}

public class HelloTest1 {
	public static void main(String[] args) {
		Child c = new Child();
		c.test();
	}

}
