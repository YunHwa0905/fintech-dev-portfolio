package com.kopo.hkcode.annot;

import static org.junit.Assert.*;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;

public class HelloAnotTest {

	@Test
	public void helloTest() {
		// 1. 스프링 컨테이너(공장) 구동
		ApplicationContext context = new GenericXmlApplicationContext("file:src/main/webapp/WEB-INF/spring/beans_annot.xml");
		// 2. 공장에서 조립된 'hello' 부품 꺼내기
		// 별도로 이름을 안줬으니 클래스명 첫글자를 소문자로 한 'hello'가 기본 ID입니다.
		Hello hello = (Hello) context.getBean("hello");
		// 3. 실행 및 확인
		hello.print();
	}

}
