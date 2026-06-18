package com.kopo.user.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kopo.common.paging.Criteria;
import com.kopo.user.dao.UserDao;
import com.kopo.user.dao.UserMapper;
import com.kopo.user.vo.UserVO;

//Service 구현 클래스
//실제 비즈니스 로직을 처리하는 계층 (Controller DAO 사이 역할)
@Service("userService")
public class UserServiceImpl implements UserService {
	// DAO 객체 자동 주입 (Spring이 알아서 생성해서 넣어줌)
	@Autowired
	UserMapper userdao;

	// 사용자 등록
	// Controller에서 받은 UserVO를 DAO에 전달하여 DB에 저장
	@Override
	public void insertUser(UserVO user) {
		userdao.insertUser(user);
	}

	// 전체 사용자 조회
	// DAO에서 모든 사용자 목록을 가져와 반환
	@Override
	public List<UserVO> getUserList() {
		return userdao.selectUserList();
	}

	// 사용자 삭제
	// userid를 기준으로 DAO에 삭제 요청
	@Override
	public void deleteUser(String id) {
		userdao.deleteUser(id);
	}

	// 사용자 단건 조회
	// userid를 기준으로 사용자 한 명 조회
	@Override
	public UserVO getUser(String id) {
		return userdao.selectUserById(id);
	}

	// 사용자 정보 수정
	// 전달받은 UserVO 정보로 기존 데이터 업데이트
	@Override
	public void updateUser(UserVO user) {
		userdao.updateUser(user);
	}

	@Override
	public List<UserVO> getUserListWithPaging(Criteria cri) throws Exception {
		// 매퍼에게 주문서(Criteria)를 던져서 잘려진 10개의 리스트를 리턴
		return userdao.selectUserListWithPaging(cri);
	}

	@Override
	public int getTotalUserCount() throws Exception {
		// 매퍼에게 전체 데이터 개수가 몇 개인지 확인
		return userdao.totalCount();
	}

}
