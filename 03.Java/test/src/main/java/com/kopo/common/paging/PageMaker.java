package com.kopo.common.paging;

public class PageMaker {
	private Criteria cri;
	private int totalCount; // 전체 데이터 개수
	private int startPage;
	// 화면에 보여질 시작 페이지 번호
	private int endPage;
	private boolean prev;
	private boolean next;
	// 화면에 보여질 끝 페이지 번호
	// 이전 버튼 활성화 여부
	// 다음 버튼 활성화 여부
	private int displayPageNum = 5; // 하단에 한 번에 보여줄 페이지 버튼 개수 (예: 5개씩 끊어보기)

	public void setCri(Criteria cri) {
		this.cri = cri;
	}

	// 핵심 로직: 외부에서 totalCount가 주입되는 순간 모든 페이징 공식을 가동합니다.
	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
		calcData();
	}

	private void calcData() {
		// 1. 끝 페이지 번호 계산 (현재 페이지 기준 올림 처리 후 단위를 곱함)
		// 공식: Math.ceil(현재페이지 / 보여줄버튼수) * 보여줄버튼수
		this.endPage = (int) (Math.ceil(cri.getPage() / (double) displayPageNum) * displayPageNum);
		// 2. 시작 페이지 번호 계산
		this.startPage = (this.endPage - displayPageNum) + 1;
		// 3. 전체 데이터를 기반으로 한 실제 최종 끝 페이지 번호 계산 (예외 처리)
		// 데이터가 125개면 13페이지가 진짜 끝인데, 위 공식대로라면 15가 나오므로 보정해줌.
		int tempEndPage = (int) (Math.ceil(totalCount / (double) cri.getPerPageNum()));
		if (this.endPage > tempEndPage) {
			this.endPage = tempEndPage;
		}
		// 4. 이전 버튼 활성화 여부 판단
		if (this.startPage == 1) {
			this.prev = false;
		} else {
			this.prev = true;
		}
		// 5. 다음 버튼 활성화 여부 판단
		if (this.endPage * cri.getPerPageNum() >= totalCount) {
			this.next = false;
		} else {
			this.next = true;
		}
	}

	// 화면(JSP)에서 데이터를 꺼내 쓸 수 있도록 Getter 메서드만 오픈합니다.
	public int getStartPage() {
		return startPage;
	}

	public int getEndPage() {
		return endPage;
	}

	public boolean isPrev() {
		return prev;
	}

	public boolean isNext() {
		return next;
	}

	public Criteria getCri() {
		return cri;
	}
}
