package com.kopo.common.paging;

import static org.junit.Assert.*;

import org.junit.Test;

public class CriteriaTest {
	
	@Test
	public void testPageStartCalculation() {
		// 1. 아직 구현되지 않았거나 텅 빈 Criteria 객체를 생성했다고 가정합니다.
		Criteria cri = new Criteria();

		// 2. 테스트 조건 설정: "2페이지를 보고 싶고, 10개씩 볼 거야"
		cri.setPage(1);
		cri.setPerPageNum(9);

		// 3. 기대값 정의: 2페이지라면 MySQL LIMIT 절에 들어갈 시작 행 번호는 무조건 '10'이어야 함.
		int expectedOffset =0;

		// 4. 단언(Assert): 실제 계산되어 나온 값이 기대했던 10과 일치하는지 확인합니다.
		System.out.println("[테스트 디버깅] 계산된 시작 행 번호: " + cri.getPageStart());
		assertEquals(expectedOffset, cri.getPageStart());
	}
}
