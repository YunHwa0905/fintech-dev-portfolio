package com.kopo.user;

import static org.junit.Assert.*;

import java.sql.Connection;

import javax.sql.DataSource;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;

public class jdbcTestUser {

	public static void main(String[] args) {
		ApplicationContext context = new GenericXmlApplicationContext("classpath:spring/datasource.xml");
		
		System.out.println("의존성 주입");
		
		DataSource dataSource = (DataSource) context.getBean("dataSource");
		
		System.out.println("연결 확인");
		try(Connection conn = dataSource.getConnection()){
			System.out.println("DB 연결 성공!");
			System.out.println("연결된 커넥션 객체: " + conn);
		}catch(Exception e) {
			System.out.println("DB 연결 실패...");
			System.out.println("에러 원인: " + e.getMessage());
			e.printStackTrace();
		}
	}

}
