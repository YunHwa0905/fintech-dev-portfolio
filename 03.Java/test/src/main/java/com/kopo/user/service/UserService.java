package com.kopo.user.service;

import java.util.List;

import com.kopo.common.paging.Criteria;
import com.kopo.user.vo.UserVO;

//(Service 계층: Controller와 DAO 사이에서 동작)
public interface UserService {
	// 사용자 등록
	// UserVO 객체에 담긴 사용자 정보를 DB에 저장
	public void insertUser(UserVO user);

	// 전체 사용자 조회
	// DB에 있는 모든 사용자 정보를 List 형태로 반환
	public List<UserVO> getUserList();

	// 사용자 삭제
	// 특정 userid를 기준으로 해당 사용자 삭제
	public void deleteUser(String id);

	// 사용자 단건 조회
	// userid를 기준으로 한 명의 사용자 정보 반환
	public UserVO getUser(String id);

	// 사용자 정보 수정
	// UserVO 객체에 담긴 값으로 기존 사용자 정보 업데이트
	public void updateUser(UserVO user);

	// [추가] 페이징 처리된 회원 목록 조회
	List<UserVO> getUserListWithPaging(Criteria cri) throws Exception;

	// [추가] 전체 회원 수 조회
	int getTotalUserCount() throws Exception;
}
