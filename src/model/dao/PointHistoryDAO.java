package model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import model.dto.PointHistoryDTO;
import model.dto.TransactionType;
import util.DBUtil;

public class PointHistoryDAO {

	//포인트 내역 조회(개인-전체)
	//포인트 내역 조회(개인-종류별)
	
	/*
	 * 트랜잭션 처리
	 * */
	//포인트 내역 추가(트랜잭션)
	public int insert(Connection conn, int userId, TransactionType transactionType, int amount, int balance_after, Integer transactionId, String description) throws SQLException {
		PreparedStatement st = null;
		
		String sql = "insert into point_history (history_id, user_id, transaction_type, amount, balance_after, transaction_id, created_at, description) "
				+ "values (seq_point_history.nextval, ?, ?, ?, ?, ?, sysdate, ?)";
		
		int result = 0;
		
		try {
			st = conn.prepareStatement(sql);
			st.setInt(1, userId);
			st.setString(2,transactionType.getCode());
			st.setInt(3, amount);
			st.setInt(4, balance_after);
			
			if(transactionId != null) {
				st.setInt(5, transactionId);
			} else {
				st.setNull(5, java.sql.Types.INTEGER);
			}
			
			st.setString(6, description);
			
			result = st.executeUpdate();
			
		} finally {
			DBUtil.dbDisconnect(null, st, null);
		}
		return result;
	}
	
	//특정 사용자의 포인트 내역 조회(종류별)
	public List<PointHistoryDTO> selectByUserIdAndType(int userId, TransactionType transactionType) {
		Connection conn = null;
		PreparedStatement st = null;
		ResultSet rs = null;
			
		String sql = "select p.*, m.username as username "
				+ "from point_history p "
				+ "join members m on p.user_id = m.user_id "
				+ "where p.user_id = ? and transaction_type = ? "
				+ "order by created_at desc";
			
		List<PointHistoryDTO> pointHistoryList = new ArrayList<>();
			
		try {
			conn = DBUtil.dbConnect();
			st = conn.prepareStatement(sql);
			st.setInt(1, userId);
			st.setString(2, transactionType.getCode());
			rs = st.executeQuery();
				
			while(rs.next()) {
				PointHistoryDTO pointHistory = makePointHistory(rs);
				pointHistoryList.add(pointHistory);
			}
				
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.dbDisconnect(conn, st, rs);
		}
			
		return pointHistoryList;
	}
	
	//특정 사용자의 포인트 내역 조회(전체)
	public List<PointHistoryDTO> selectByUserId(int userId) {
		Connection conn = null;
		PreparedStatement st = null;
		ResultSet rs = null;
			
		String sql = "select p.*, m.username as username "
				+ "from point_history p "
				+ "join members m on p.user_id = m.user_id "
				+ "where p.user_id = ? "
				+ "order by created_at desc";
			
		List<PointHistoryDTO> pointHistoryList = new ArrayList<>();
			
		try {
			conn = DBUtil.dbConnect();
			st = conn.prepareStatement(sql);
			st.setInt(1, userId);
			rs = st.executeQuery();
				
			while(rs.next()) {
				PointHistoryDTO pointHistory = makePointHistory(rs);
				pointHistoryList.add(pointHistory);
			}
				
		} catch (SQLException e) {
				e.printStackTrace();
		} finally {
				DBUtil.dbDisconnect(conn, st, rs);
		}
			
		return pointHistoryList;
	}

	private PointHistoryDTO makePointHistory(ResultSet rs) throws SQLException {

		PointHistoryDTO pointHistory = new PointHistoryDTO();
		
		pointHistory.setHistory_id(rs.getInt("history_id"));
		pointHistory.setUser_id(rs.getInt("user_id"));
		pointHistory.setTransaction_type(TransactionType.fromCode(rs.getString("transaction_type")));
		pointHistory.setAmount(rs.getInt("amount"));
		pointHistory.setBalance_after(rs.getInt("balance_after"));
		
		int transactionId = rs.getInt("transaction_id");
		if(!rs.wasNull()) {
			pointHistory.setTransaction_id(transactionId);
		}
		
		pointHistory.setCreated_at(rs.getDate("created_at"));
		pointHistory.setDescription(rs.getString("description"));
		pointHistory.setUsername(rs.getString("username"));
		
		return pointHistory;
	}
	
}
