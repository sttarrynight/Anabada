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
	
	//특정 회원 조회
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
			
			while(rs.next()) {
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
