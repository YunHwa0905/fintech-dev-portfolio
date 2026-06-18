package com.kopo.hkcode.product;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;

public class JdbcTest {
	public static void main(String[] args) {
		ApplicationContext context = new GenericXmlApplicationContext("file:src/main/webapp/WEB-INF/spring/beans.xml");

		ProductService productService = (ProductService) context.getBean("productService");

		String callResult = productService.getMaxQtyByRegion("A41","PRODUCT51");
		System.out.println(callResult);

	}
}
