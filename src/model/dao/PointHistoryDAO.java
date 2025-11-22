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
	
	//포인트 내역 조회(개인-종류별)
	public List<PointHistoryDTO> selectByType(TransactionType transactionType) {
		Connection conn = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		
		String sql = "select p.*, m.username as username "
				+ "from point_history p "
				+ "join members m on p.user_id = m.user_id "
				+ "where transaction_type = ? "
				+ "order by created_at desc";
		
		List<PointHistoryDTO> PointHistoryList = new ArrayList<>();
		
		try {
			conn = DBUtil.dbConnect();
			st = conn.prepareStatement(sql);
			st.setString(1,transactionType.getCode());
			rs = st.executeQuery();
			
			while(rs.next()) {
				PointHistoryDTO pointHistory = makePointHistory(rs);
				PointHistoryList.add(pointHistory);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.dbDisconnect(conn, st, rs);
		}
		
		return PointHistoryList;
	}
	
	//포인트 내역 조회(개인-전체)
	public List<PointHistoryDTO> selectAll() {
		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;
		
		String sql = "select p.*, m.username as username "
				+ "from point_history p "
				+ "join members m on p.user_id = m.user_id "
				+ "order by created_at desc";
		
		List<PointHistoryDTO> PointHistoryList = new ArrayList<>();
		
		try {
			conn = DBUtil.dbConnect();
			st = conn.createStatement();
			rs = st.executeQuery(sql);
			
			while(rs.next()) {
				PointHistoryDTO pointHistory = makePointHistory(rs);
				PointHistoryList.add(pointHistory);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.dbDisconnect(conn, st, rs);
		}
		
		return PointHistoryList;
	}

	private PointHistoryDTO makePointHistory(ResultSet rs) throws SQLException {

		PointHistoryDTO pointHistory = new PointHistoryDTO();
		
		pointHistory.setHistory_id(rs.getInt("history_id"));
		pointHistory.setUser_id(rs.getInt("user_id"));
		pointHistory.setTransaction_type(TransactionType.fromCode(rs.getString("transaction_type")));
		pointHistory.setAmount(rs.getInt("amount"));
		pointHistory.setBalance_after(rs.getInt("balance_after"));
		pointHistory.setTransaction_id(rs.getInt("transaction_id"));
		pointHistory.setCreated_at(rs.getDate("created_at"));
		pointHistory.setDescription(rs.getString("description"));
		pointHistory.setUsername(rs.getString("username"));
		
		return null;
	}
	
}
