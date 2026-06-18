package com.kopo.user.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.kopo.user.vo.UserVO;

@Repository("userDaoJDBC")
public class UserDaoImplJDBC implements UserDao {

	// DB 작업을 쉽게 도와주는 객체 (JDBC 추상화)
	private JdbcTemplate jdbcTemplate;

	// DataSource(DB 연결 정보)를 주입받아서 JdbcTemplate 생성
	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	// DB 조회 결과(ResultSet)를 UserVO 객체로 변환하는 클래스 spring
	class UserMapper implements RowMapper<UserVO> {

		// 한 행(row)을 UserVO 객체로 매핑
		public UserVO mapRow(ResultSet rs, int rowNum) throws SQLException {

			// 빈 UserVO 객체 생성
			UserVO user = new UserVO();

			// DB 컬럼 값을 꺼내서 객체에 세팅
			user.setUserId(rs.getString("userid"));
			user.setName(rs.getString("name"));
			user.setGender(rs.getString("gender"));
			user.setCity(rs.getString("city"));

			// 완성된 객체 반환
			return user;
		}
	}

	@Override
	// 사용자 추가 메서드
	public void insert(UserVO user) {
		// 사용자 데이터를 삽입하는 SQL 쿼리
		String SQL = "insert into users " + "(userid, name, gender, city) " + "values (?, ?, ?, ?)";
		// update:
		// INSERT, UPDATE, DELETE 같은 데이터 변경 쿼리 실행
		jdbcTemplate.update(SQL, user.getUserId(), // userid 값
				user.getName(), // name 값
				user.getGender(), // gender 값
				user.getCity()
		// city 값
		);
		// 콘솔에 입력된 사용자 정보 출력 (디버깅용)
		System.out.println("Record UserId=" + user.getUserId() + " Name=" + user.getName() + " Gender=" + user.getGender() + " City=" + user.getCity());
	}

	@Override
	public UserVO read(String id) {
		return null;
	}

	@Override
	public List<UserVO> readAll() {
		return null;
	}

	@Override
	public void update(UserVO user) {
	}

	@Override
	public void delete(String id) {
	}
}