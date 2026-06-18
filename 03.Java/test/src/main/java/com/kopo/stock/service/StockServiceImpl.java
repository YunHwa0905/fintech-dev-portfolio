package com.kopo.stock.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kopo.stock.dao.StockMapper;
import com.kopo.stock.service.StockService;
import com.kopo.stock.vo.StockVO;

//Service 구현 클래스
//실제 비즈니스 로직을 처리하는 계층 (Controller DAO 사이 역할)
@Service("stockService")
public class StockServiceImpl implements StockService {
	// DAO 객체 자동 주입 (Spring이 알아서 생성해서 넣어줌)
	@Autowired
	StockMapper stockdao;

	// 사용자 등록
	// Controller에서 받은 UserVO를 DAO에 전달하여 DB에 저장
	@Override
	public void insertStock(StockVO stock) {
		stockdao.insertStock(stock);
	}

	// 전체 사용자 조회
	// DAO에서 모든 사용자 목록을 가져와 반환
	@Override
	public List<StockVO> getStockList() {
		return stockdao.selectStockList();
	}

	// 사용자 삭제
	// userid를 기준으로 DAO에 삭제 요청
	@Override
	public void deleteStock(String ticker) {
		stockdao.deleteStock(ticker);
	}

	// 사용자 단건 조회
	// userid를 기준으로 사용자 한 명 조회
	@Override
	public StockVO getStock(String ticker) {
		return stockdao.selectStockById(ticker);
	}

	// 사용자 정보 수정
	// 전달받은 UserVO 정보로 기존 데이터 업데이트
	@Override
	public void updateStock(StockVO stock) {
		stockdao.updateStock(stock);
	}

}
