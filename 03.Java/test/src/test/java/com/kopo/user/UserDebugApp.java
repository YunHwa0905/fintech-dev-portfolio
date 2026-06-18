package com.kopo.user;

import static org.junit.Assert.*;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;

import com.kopo.user.dao.UserDao;
import com.kopo.user.vo.UserVO;

public class UserDebugApp {
	public static void main(String[] args) {
		// 1. 스프링 컨테이너(설정파일) 로드
		// 톰캣 실행시servlet-context.xml 이 자동으로 불러와지나
		// 지금은 일반java 앱이라 servlet-context 수동 불러와야함
		// datasource는 db정보라 불러오기!
		ApplicationContext context = new GenericXmlApplicationContext("classpath:spring/datasource.xml",
				"file:src/main/webapp/WEB-INF/spring/beans.xml");
		
		UserDao dao = (UserDao) context.getBean("userDao");
		
		// insert 테스트
		UserVO newUser = new UserVO();
		newUser.setName("테스터");
		newUser.setCity("서울");
		newUser.setGender("female");
		newUser.setUserId("haiteam");
		
		dao.insert(newUser);
	}
}
