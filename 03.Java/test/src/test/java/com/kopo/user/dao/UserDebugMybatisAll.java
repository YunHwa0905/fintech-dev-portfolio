package com.kopo.user.dao;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import com.kopo.user.vo.UserVO;

public class UserDebugMybatisAll {
	public static void main(String[] args) {
		// 1. 스프링 설정 파일 로드 (SqlSessionFactory 설정이 포함된 datasource.xml 필수!)
		ApplicationContext context = new GenericXmlApplicationContext("classpath:spring/datasource.xml", 
			    "classpath:config/datasource.xml",   
			    "file:src/main/webapp/WEB-INF/spring/servlet-context.xml");
		
		// 또는 bean 이름으로
		UserMapper dao = context.getBean(UserMapper.class);
		
		// 2. 서비스의 메서드를 호출합니다.
		System.out.println("=== MyBatis 서비스 통합 테스트 시작 ===");
		
		UserVO newUser = new UserVO();
		newUser.setUserId("test_user");
		newUser.setName("이윤화");
		newUser.setCity("서울");
		
		try {
		    dao.insertUser(newUser);
		    System.out.println("-> [성공] 사용자 등록 완료");
		} catch (Exception e) {
		    System.out.println("등록 실패: " + e.getMessage());
		}
		
		UserVO user = dao.selectUserById("hkcode");
		
		if (user != null) {
			System.out.println("-> [조회] 이름: " + user.getName() + ", 도시: " + user.getCity());
		}
		
		UserVO updateUser = new UserVO();
		updateUser.setUserId("hkcode");   
		updateUser.setName("테스트");      
		updateUser.setCity("경기"); 
		
		try {
		    dao.updateUser(updateUser);
		    System.out.println("-> [수정] 수정된 이름: " + updateUser.getName() + ", 수정된 도시: " + updateUser.getCity());
		} catch (Exception e) {
		    System.out.println("업데이트 실패: " + e.getMessage());
		}
				
				
		// 4. 전체 목록 조회 테스트
		System.out.println("\n=== 전체 사용자 목록 ===");
		List<UserVO> list = dao.selectUserList();
		
		for (UserVO u : list) {
			System.out.println("ID: " + u.getUserId() + "   | 이름: " + u.getName() + "   | 도시: " + u.getCity());
		}
		
		try {
		    dao.deleteUser("이윤화");  // id 값 넣으면 됨
		    System.out.println("\n-> [성공] 테스트용 사용자 삭제 완료");
		} catch (Exception e) {
		    System.out.println("삭제 실패: " + e.getMessage());
		}
		
		System.out.println("\n=== MyBatis 서비스 통합 테스트 종료 ===");
		
	}
}
