package com.kopo.user.dao;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import com.kopo.user.vo.UserVO;

public class UserDebugMybatis {
	public static void main(String[] args) {
		// 1. 스프링 설정 파일 로드 (SqlSessionFactory 설정이 포함된 datasource.xml 필수!)
		ApplicationContext context = new GenericXmlApplicationContext("classpath:spring/datasource.xml", 
			    "classpath:config/datasource.xml",   
			    "file:src/main/webapp/WEB-INF/spring/servlet-context.xml");
		
		// 또는 bean 이름으로
		UserMapper dao = context.getBean(UserMapper.class);
		
		// 2. 서비스의 메서드를 호출합니다.
		System.out.println("=== 서비스 호출 테스트 ===");
		UserVO user = dao.selectUserById("hkcode");
		if (user != null) {
			System.out.println("이름: " + user.getName());
			System.out.println("도시: " + user.getCity());
		}
		
		// 4. 전체 목록 조회 테스트
		System.out.println("\n=== 전체 목록 조회 테스트 ===");
		List<UserVO> list = dao.selectUserList();
		
		for (UserVO u : list) {
			System.out.println("ID: " + u.getUserId() + ", Name: " + u.getName());
		}
	}
}
