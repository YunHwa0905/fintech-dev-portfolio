package com.kopo.user.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kopo.common.paging.Criteria;
import com.kopo.common.paging.PageMaker;
import com.kopo.user.service.UserService;
import com.kopo.user.vo.UserVO;

@Controller
public class UserController {
	@Autowired
	private UserService userService;

	// --- [방식 2] API 연동 (현대적 방식) ---
	@RequestMapping(value = "/getUserList.api")
	@ResponseBody // 객체를 JSON으로 자동 변환해줌
	public List<UserVO> getUserListAPI() {
		// JSP 이동 없이 데이터(JSON)만 바로 리턴!
		return userService.getUserList();
	}

	// --- [방식 1] JSP 연동 (페이징 기능 추가 버전) --
	@RequestMapping(value = "/userList.do", method = RequestMethod.GET)
	public String getUserListJSP(Criteria cri, Model model) throws Exception {

		// 중요 포인트: 파라미터 자리에 'Criteria cri'를 선언해 주면,
		// 스프링이 주소창의 ?page=2 같은 값을 알아서 수집해서 cri 객체 안에 쏙 넣어줍니다.
		// 주소창에 아무것도 없으면 기본 생성자 규칙에 의해 자동으로 1페이지, 10개 세팅이 됩니다.
		// 1. 서비스 호출해서 '전체 데이터'가 아닌 '해당 페이지의 10개 데이터만' 가져오기
		List<UserVO> userList = userService.getUserListWithPaging(cri);

		// 2. 모델에 담아서 JSP로 전달 (기존과 똑같은 이름으로 담아서 화면 깨짐을 최소화합니다)
		model.addAttribute("userList", userList);

		// ================= [페이징 계산기 세팅 구간 추가] =================
		// 3. 하단 [이전] [1] [2] [3] [다음] 버튼 제어용 계산기 가동
		PageMaker pageMaker = new PageMaker();
		pageMaker.setCri(cri); // 현재 몇 페이지를 요청했는지 주문서 주입

		// DB에 물어봐서 가상 데이터가 아닌 진짜 전체 데이터 개수를 긁어와서 세팅!
		int totalCount = userService.getTotalUserCount();
		pageMaker.setTotalCount(totalCount); // 이 주입이 일어나는 순간 하단 버튼 수학 공식이 싹 돌아감

		// 4. 계산이 끝난 똑똑한 계산기 객체도 상자(Model)에 담아 JSP로 함께 토스합니다.
		model.addAttribute("pageMaker", pageMaker);

		// ===================================================================
		// 5. /WEB-INF/views/userList.jsp로 이동 (이동하는 위치는 기존과 동일!)
		return "userList";
	}

}
