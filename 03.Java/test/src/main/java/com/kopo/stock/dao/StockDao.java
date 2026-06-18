package com.kopo.stock.dao;

import java.util.List;

import com.kopo.stock.vo.StockVO;

public interface StockDao {
	// 사용자정보추가(Create)
	public void insert(StockVO stock);
	// 전체사용자조회(Read- 전체) 객체를여러개담는리스트(통)
	public List<StockVO> readAll();
	// 사용자정보수정(Update)
	public void update(StockVO stock);
	// 사용자삭제(Delete)
	public void delete(String ticker);
	// 특정사용자조회(Read- 단건)
	public StockVO read(String ticker);
}