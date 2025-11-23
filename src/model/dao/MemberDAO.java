package model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import model.dto.MemberDTO;
import util.DBUtil;

public class MemberDAO {
	
	//모든 회원 조회
	//특정 회원 조회(내정보 조회, 잔액조회)
	//특정 회원 수정(정보 수정)
	//특정 회원 삭제(회원 탈퇴)
	
	/*
	 * 트랜잭션처리
	 * */
	//포인트 업데이트(트랜잭션)
	public int updatePoint(Connection conn, int userId, int amount) throws SQLException {
		PreparedStatement st = null;
		
		String sql = "update members set point_balance = point_balance + ?, updated_at = sysdate "
				+ "where user_id = ?";
		
		int result = 0;
		
		try {
			st = conn.prepareStatement(sql);
			st.setInt(1, amount); //양수면 충전, 음수면 인출
			st.setInt(2, userId);
			
			result =  st.executeUpdate();
			
		}  finally {
			DBUtil.dbDisconnect(null, st, null);
		}
		return result;
	}
	
	//특정 회원 조회 (트랜잭션)
	public MemberDTO selectById(Connection conn, int userId) throws SQLException {
		PreparedStatement st = null;
		ResultSet rs = null;
		
		String sql = "select * from members where user_id = ?";
		
		MemberDTO member = null;
		
		try {
			conn = DBUtil.dbConnect();
			st = conn.prepareStatement(sql);
			st.setInt(1, userId);
			rs = st.executeQuery();
			
			if(rs.next()) {
				member = makeMember(rs);
			}
			
		}  finally {
			DBUtil.dbDisconnect(null, st, rs);
		}
		
		return member;
	} 
	
	
	//회원 삭제(탈퇴)
	public int delete(int userId) {
		Connection conn = null;
		PreparedStatement st = null;
			
		String sql = "delete from members where user_id = ?";
			
		int result = 0;
			
		try {
			conn = DBUtil.dbConnect();
			st = conn.prepareStatement(sql);
			st.setInt(1, userId);
				
			result = st.executeUpdate();
				
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.dbDisconnect(conn, st, null);
		}
		return result;
	}
	
	//회원 정보 수정 (비밀번호)
	public int update(MemberDTO member) {
		Connection conn = null;
		PreparedStatement st = null;
		
		String sql = "update members set password = ?, updated_at = sysdate "
				+ "where user_id = ?";
		
		int result = 0;
		
		try {
			conn = DBUtil.dbConnect();
			st = conn.prepareStatement(sql);
			st.setString(1, member.getPassword());
			st.setInt(2, member.getUser_id());
			
			result = st.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.dbDisconnect(conn, st, null);
		}
		return result;
	}
	
	//회원 등록(회원가입)
	public int insert(MemberDTO member) {
		Connection conn = null;
		PreparedStatement st = null;
		
		String sql = "insert into members(user_id, username, password, point_balance, created_at) "
				+ "values(seq_member.nextval, ?, ?, 0, sysdate)";
		
		int result = 0;
		
		try {
			conn = DBUtil.dbConnect();
			st = conn.prepareStatement(sql);
			st.setString(1, member.getUsername());
			st.setString(2, member.getPassword());
			
			result = st.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.dbDisconnect(conn, st, null);
		}
		return result;
	}
	
	//특정 회원 조회(username - 로그인용)
	public MemberDTO selectByUsername(String username) {
		Connection conn = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		
		String sql = "select * from members where username = ?";
		
		MemberDTO member = null;
		
		try {
			conn = DBUtil.dbConnect();
			st = conn.prepareStatement(sql);
			st.setString(1, username);
			rs = st.executeQuery();
			
			if(rs.next()) {
				member = makeMember(rs);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.dbDisconnect(conn, st, rs);
		}
		
		return member;
	}
	
	//특정 회원 조회(ID)
	public MemberDTO selectById(int user_id) {
		Connection conn = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		
		String sql = "select * from members where user_id = ?";
		
		MemberDTO member = null;
		
		try {
			conn = DBUtil.dbConnect();
			st = conn.prepareStatement(sql);
			st.setInt(1, user_id);
			rs = st.executeQuery();
			
			if(rs.next()) {
				member = makeMember(rs);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.dbDisconnect(conn, st, rs);
		}
		
		return member;
	}
	
	//모든 회원 조회
	public List<MemberDTO> selectAll() {
		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;
		
		String sql = "select * from members";
		
		List<MemberDTO> memberList = new ArrayList<>();
		
		try {
			conn = DBUtil.dbConnect();
			st = conn.createStatement();
			rs = st.executeQuery(sql);
			
			while(rs.next()) {
				MemberDTO member = makeMember(rs);
				memberList.add(member);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.dbDisconnect(conn, st, rs);
		}
		
		return memberList;
	}

	//DB로부터 온 데이터로 객체 생성
	private MemberDTO makeMember(ResultSet rs) throws SQLException {
		MemberDTO member = new MemberDTO();
		
		member.setUser_id(rs.getInt("user_id"));
		member.setUsername(rs.getString("username"));
		member.setPassword(rs.getString("password"));
		member.setPoint_balance(rs.getInt("point_balance"));
		member.setCreated_at(rs.getDate("created_at"));
		member.setUpdated_at(rs.getDate("updated_at"));
		
		return member;
	}
	
}
