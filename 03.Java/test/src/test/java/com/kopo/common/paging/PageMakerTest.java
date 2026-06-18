package com.kopo.common.paging;

import org.junit.Test;
import static org.junit.Assert.*;

public class PageMakerTest {
	@Test
	public void testPageCalculation() {
		// 1. 기준 주문서 세팅: 3페이지, 10개씩 보기
		Criteria cri = new Criteria();
		cri.setPage(3);
		cri.setPerPageNum(10);

		// 2. PageMaker 객체 생성 (아직 만들지 않아 컴파일 에러 발생)
		PageMaker pm = new PageMaker();
		pm.setCri(cri);

		// 3. 전체 데이터 수 주입 (가상의 데이터 수를 테스트함)
		pm.setTotalCount(125);

		// 4. 검증(Assert): 내가 설계한 수학 공식이 맞는지 단언문으로 확인
		System.out.println("[디버깅] 시작 페이지: " + pm.getStartPage()); // 예상값: 1
		System.out.println("[디버깅] 끝 페이지: " + pm.getEndPage());

		// 예상값: 5 (1번째 묶으
		System.out.println("[디버깅] 이전 버튼 여부: " + pm.isPrev()); // 예상값: false
		System.out.println("[디버깅] 다음 버튼 여부: " + pm.isNext()); // 예상값: true
		assertEquals(1, pm.getStartPage());
		assertEquals(5, pm.getEndPage());
		assertFalse(pm.isPrev());
		assertTrue(pm.isNext());

	}

}