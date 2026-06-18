package com.kopo.stock.service;

import java.util.List;

import com.kopo.stock.vo.StockVO;

//(Service 계층: Controller와 DAO 사이에서 동작)
public interface StockService {
	//사용자 등록
	//UserVO 객체에 담긴 사용자 정보를 DB에 저장
	public void insertStock(StockVO stock);

	//전체 사용자 조회
	//DB에 있는 모든 사용자 정보를 List 형태로 반환
	public List<StockVO> getStockList();

	//사용자 삭제
	//특정 userid를 기준으로 해당 사용자 삭제
	public void deleteStock(String ticker);

	//사용자 단건 조회
	//userid를 기준으로 한 명의 사용자 정보 반환
	public StockVO getStock(String ticker);

	//사용자 정보 수정
	//UserVO 객체에 담긴 값으로 기존 사용자 정보 업데이트
	public void updateStock(StockVO stock);
}
