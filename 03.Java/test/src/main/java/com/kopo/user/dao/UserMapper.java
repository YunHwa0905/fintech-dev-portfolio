package com.kopo.user.dao;

import java.util.List;

import com.kopo.common.paging.Criteria;
import com.kopo.user.vo.UserVO;

public interface UserMapper {
	UserVO selectUserById(String id);

	List<UserVO> selectUserList();

	void insertUser(UserVO userVO);

	void updateUser(UserVO userVO);

	void deleteUser(String id);

	// [추가] 1. 페이징 처리된 회원 목록 조회 (Criteria의 pageStart, perPageNum을 활용)
	List<UserVO> selectUserListWithPaging(Criteria cri);

	// [추가] 2. 하단 페이징 블록 계산용 전체 회원 수 조회
	int totalCount();
}
