package com.kopo.hkcode.product;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class ProductController {

	@Autowired
	private ProductService productService;

	@PostMapping("/getMaxQty")
	public Map getMaxQty(@RequestParam("regionid") String regionid, @RequestParam("product") String product) {
		Map response = new HashMap<>();
		try {
			String result = productService.getMaxQtyByRegion(regionid, product);
			response.put("regionId", regionid);
			response.put("product", product);
			response.put("maxQty", result);
			response.put("status", "success");
		} catch (Exception e) {
			response.put("status", "error");
			response.put("message", e.getMessage());
		}
		return response;
	}
}