package com.kopo.hkcode.pojo;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;

public class HelloController {
   public static void main(String[] args) {
// 1. 현재 beans.xml 위치에 맞게 경로 수정
      ApplicationContext context = new GenericXmlApplicationContext("file:src/main/webapp/WEB-INF/spring/beans_product.xml");
// 2. Hello Bean 가져오기 (일단 객체를 꺼낸 뒤 Hello 타입으로 바꿈) 뒤쪽에 있는 형태로 향후 바꿈
      Hello hello = (Hello) context.getBean("hello");
// 결과1: hello에 설정된 property인 name에 설정된 값으로 출력
      System.out.println(hello.sayHello());
// 결과2: hello에 매핑된 print -> 콘솔프린터에 파라미터값이 hello ... 출력됨
      hello.print();
      Printer printer = context.getBean("consolePrinter", Printer.class);
//결과3: beanid가 print로 설정된 부분에 매핑됨
      printer.print("test");
// 3. 싱글톤 확인 
      Hello hello2 = context.getBean("hello", Hello.class);
      System.out.println("둘이 같은 객체인가요? " + (hello == hello2));
   }
}