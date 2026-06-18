package com.kopo.hkcode.product;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

import javax.sql.DataSource;

public class ProductService {
	// 실무 스타일: final 키워드로 안정성 확보
	private final DataSource dataSource;
	private final List<String> managerList;

	// 생성자 주입: 스프링이 이 생성자를 통해 부품을 꽉 끼워줍니다.
	public ProductService(DataSource dataSource, List<String> managerList) {
		this.dataSource = dataSource;
		this.managerList = managerList;
	}

	// 지역 최대 QTY 조회 로직
	public String getMaxQtyByRegion(String regionId, String product) {
		String result = "";
		String sql = "SELECT MAX(QTY) FROM kopo_channel_seasonality_new WHERE regionid = ? AND PRODUCT = ?";
		try (Connection conn = dataSource.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, regionId);
			pstmt.setString(2, product);
			try (ResultSet rs = pstmt.executeQuery()) {
				if (rs.next()) {
					int maxQty = rs.getInt(1);
					result = "지역 [" + regionId + "], 제품군 [" + product + "]의 최대 판매량은 " + maxQty + "개 입니다.";
				}
			}
		} catch (Exception e) {
			result = "DB 조회 에러 발생: " + e.getMessage();
			e.printStackTrace();
		}
		if (managerList != null && !managerList.isEmpty()) {
			result += " (조회 담당자: " + managerList.get(0) + ")";
		}
		return result;
	}
}
