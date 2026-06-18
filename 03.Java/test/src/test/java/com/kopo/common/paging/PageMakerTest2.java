package com.kopo.common.paging;

import org.junit.Test;
import static org.junit.Assert.*;

public class PageMakerTest2 {

	@Test
	public void testLastPageBoundary() {
		Criteria cri = new Criteria();
		cri.setPage(12); // 12페이지 요청
		cri.setPerPageNum(10);
		
		PageMaker pm = new PageMaker();
		
		pm.setCri(cri);
		pm.setTotalCount(125); // 전체 데이터 125개
		
		System.out.println("--- [경계선 테스트] 12페이지 이동 시 ---");
		System.out.println("[디버깅] 시작 페이지: " + pm.getStartPage()); // 예상값: 11
		System.out.println("[디버깅] 끝 페이지: " + pm.getEndPage());
		// 예상값: 13 (15가 아니어야 함!)
		
		System.out.println("[디버깅] 이전 버튼 여부: " + pm.isPrev()); // 예상값: True
		System.out.println("[디버깅] 다음 버튼 여부: " + pm.isNext()); // 예상값: false
		
		assertEquals(11, pm.getStartPage());
		assertEquals(13, pm.getEndPage()); // 15가 아니라 13으로 예외처리가 잘 되었는지 검증
		assertTrue(pm.isPrev());
		// 마지막 블록이므로 다음 버튼이 꺼져야 함
		assertFalse(pm.isNext());
	}
	// 마지막 블록이므로 다음 버튼이 꺼져야 함

}