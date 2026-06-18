package com.kopo.stock.dao;

import java.util.List;

import com.kopo.stock.vo.StockVO;

public interface StockMapper {
    StockVO selectStockById(String ticker);
    List<StockVO> selectStockList();
    void insertStock(StockVO stock);
    void updateStock(StockVO stock);
    void deleteStock(String ticker);
}
