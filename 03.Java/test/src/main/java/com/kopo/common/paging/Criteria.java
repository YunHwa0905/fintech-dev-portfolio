package com.kopo.common.paging;

public class Criteria {
	
	// 사용자가 요청한 현재 페이지 번호
	private int page;
	// 한 페이지당 보여줄 데이터 개수
	private int perPageNum; 
	
	// 기본 생성자: 아무 인자 없이 호출되면 기본적으로 1페이지, 10개씩 보기를 세팅
	public Criteria() {
		this.page = 1;
		this.perPageNum = 10;
	}

	// 데이터 방어 조치: 사용자가 실수나 악의적으로 0 이하의 페이지를 넣으면 1페이지로 고정
	public void setPage(int page) {
		if (page <= 0) {
			this.page = 1;
			return;
		}
		this.page = page;
	}

	// 데이터 방어 조치
	public void setPerPageNum(int perPageNum) {
		if (perPageNum <= 0 || perPageNum > 100) {
			this.perPageNum = 10;
			return;
		}
		this.perPageNum = perPageNum;
	}

	public int getPage() {
		return page;
	}

	public int getPerPageNum() {
		return perPageNum;
	}

	public int getPageStart() {
		return (this.page - 1) * this.perPageNum;
	}
}
