package com.kopo.common.paging;

import static org.junit.Assert.*;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner; // 추가
import com.kopo.user.dao.UserMapper;
import com.kopo.user.vo.UserVO;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:config/datasource.xml", // "web.xml 대신 직접불러오기"
		"file:src/main/webapp/WEB-INF/spring/beans_product.xml" })
public class UserMapperTest {
	@Autowired
	private UserMapper userdao;

	@Test
	public void testPagingListAndCount() throws Exception {
		// 1. 2페이지 요청 환경 가상 세팅 (10개씩 보기)
		Criteria cri = new Criteria();
		cri.setPage(1);
		cri.setPerPageNum(10);
		System.out.println("====== [DB 테스트] 1페이지 조회 시작 ======");

		// 2. 실제 DB 쿼리 가동
		List<UserVO> list = userdao.selectUserListWithPaging(cri);
		int total = userdao.totalCount();

		// 3. 콘솔 디버깅 출력
		System.out.println("-> DB에 있는 전체 회원 수: " + total + "개");
		System.out.println("-> 1페이지에 잘려 나온 데이터 개수: " + list.size() + "개");
		for (UserVO user : list) {
			System.out.println("회원ID: " + user.getUserId() + " | 이름: " + user.getName());
		}
		System.out.println("====== [DB 테스트] 성공 ======");
	}
}