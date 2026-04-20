package com.smart;
import com.kopo.Hello;

public class HelloTest {
    public static void main(String[] args) {
        Hello h = new Hello();
        System.out.println("=== 다른 패키지에서 접근 ===");

        h.publicMethod();           // ○ 가능
        // h.protectedMethod();    // ○ 불가
        // h.defaultMethod();      // ○ 불가
        // h.privateMethod();      // ○ 당연히 불가
    }
    
    
}