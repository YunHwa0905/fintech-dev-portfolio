package com.kopo.stock.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kopo.stock.service.StockService;
import com.kopo.stock.vo.StockVO;


@Controller
public class StockController {
	@Autowired
	private StockService stockService;

// --- [방식 1] JSP 연동 (전통적 방식) ---
	@RequestMapping(value = "/stockList.do", method = RequestMethod.GET)
	public String getStockListJSP(Model model) {
// 1. 서비스 호출해서 데이터 가져오기
		List<StockVO> stockList = stockService.getStockList();
// 2. 모델에 담아서 JSP로 전달
		model.addAttribute("stockList", stockList);
// 3. /WEB-INF/views/userList.jsp로 이동
		return "stockList";
	}

// --- [방식 2] API 연동 (현대적 방식) ---
	@RequestMapping(value = "/getStockList.api")
	@ResponseBody // 객체를 JSON으로 자동 변환해줌
	public List<StockVO> getStockListAPI() {
// JSP 이동 없이 데이터(JSON)만 바로 리턴!
		return stockService.getStockList();
	}
}
