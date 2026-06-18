package com.kopo.user.dao;

import java.util.List;

import com.kopo.user.vo.UserVO;

public interface UserDao {
	// 사용자정보추가(Create)
	public void insert(UserVO user);
	// 전체사용자조회(Read- 전체) 객체를여러개담는리스트(통)
	public List<UserVO> readAll();
	// 사용자정보수정(Update)
	public void update(UserVO user);
	// 사용자삭제(Delete)
	public void delete(String id);
	// 특정사용자조회(Read- 단건)
	public UserVO read(String id);
}