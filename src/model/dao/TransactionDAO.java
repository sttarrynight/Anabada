package model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import model.dto.TransactionDTO;
import util.DBUtil;

public class TransactionDAO {
	
	//모든 거래내역 조회
	//구매내역 조회
	//판매내역 조회
	
		//판매내역 조회
		public List<TransactionDTO> selectBySellerId(int userId) {
			Connection conn = null;
			PreparedStatement st = null;
			ResultSet rs = null;
			
			String sql = "select t.transaction_id, t.product_id, t.buyer_id, t.seller_id, "
					+ "t.amount, t.transaction_date, "
					+ "nvl(p.title, '삭제된 상품') as product_title, "
					+ "nvl(buyer.username, '탈퇴한 회원') as buyer_name, "
					+ "nvl(seller.username, '탈퇴한 회원') as seller_name "
					+ "from transactions t "
					+ "left join products p on t.product_id = p.product_id "
					+ "left join members buyer on t.buyer_id = buyer.user_id "
					+ "left join members seller on t.seller_id = seller.user_id "
					+ "where t.seller_id = ? "
					+ "order by t.transaction_date desc";
			
			List<TransactionDTO> TransactionList = new ArrayList<>();
			
			try {
				conn = DBUtil.dbConnect();
				st = conn.prepareStatement(sql);
				st.setInt(1, userId);
				rs = st.executeQuery();
				
				while(rs.next()) {
					TransactionDTO transaction = makeTransaction(rs);
					TransactionList.add(transaction);
				}
				
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				DBUtil.dbDisconnect(conn, st, rs);
			}
			return TransactionList;
		}
	
		//구매내역 조회
		public List<TransactionDTO> selectByBuyerId(int userId) {
			Connection conn = null;
			PreparedStatement st = null;
			ResultSet rs = null;
			
			String sql = "select t.transaction_id, t.product_id, t.buyer_id, t.seller_id, "
					+ "t.amount, t.transaction_date, "
					+ "nvl(p.title, '삭제된 상품') as product_title, "
					+ "nvl(buyer.username, '탈퇴한 회원') as buyer_name, "
					+ "nvl(seller.username, '탈퇴한 회원') as seller_name "
					+ "from transactions t "
					+ "left join products p on t.product_id = p.product_id "
					+ "left join members buyer on t.buyer_id = buyer.user_id "
					+ "left join members seller on t.seller_id = seller.user_id "
					+ "where t.buyer_id = ? "
					+ "order by t.transaction_date desc";
			
			List<TransactionDTO> TransactionList = new ArrayList<>();
			
			try {
				conn = DBUtil.dbConnect();
				st = conn.prepareStatement(sql);
				st.setInt(1, userId);
				rs = st.executeQuery();
				
				while(rs.next()) {
					TransactionDTO transaction = makeTransaction(rs);
					TransactionList.add(transaction);
				}
				
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				DBUtil.dbDisconnect(conn, st, rs);
			}
			return TransactionList;
		}
		
		//모든 거래내역 조회
		public List<TransactionDTO> selectAll() {
			Connection conn = null;
			Statement st = null;
			ResultSet rs = null;
			
			String sql = "select t.transaction_id, t.product_id, t.buyer_id, t.seller_id, "
					+ "t.amount, t.transaction_date, "
					+ "nvl(p.title, '삭제된 상품') as product_title, "
					+ "nvl(buyer.username, '탈퇴한 회원') as buyer_name, "
					+ "nvl(seller.username, '탈퇴한 회원') as seller_name "
					+ "from transactions t "
					+ "left join products p on t.product_id = p.product_id "
					+ "left join members buyer on t.buyer_id = buyer.user_id "
					+ "left join members seller on t.seller_id = seller.user_id"
					+ "order by t.transaction_date desc";
			
			List<TransactionDTO> TransactionList = new ArrayList<>();
			
			try {
				conn = DBUtil.dbConnect();
				st = conn.createStatement();
				rs = st.executeQuery(sql);
				
				while(rs.next()) {
					TransactionDTO transaction = makeTransaction(rs);
					TransactionList.add(transaction);
				}
				
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				DBUtil.dbDisconnect(conn, st, rs);
			}
			return TransactionList;
		}

		private TransactionDTO makeTransaction(ResultSet rs) throws SQLException {
			TransactionDTO transaction = new TransactionDTO();
			
			transaction.setTransaction_id(rs.getInt("transaction_id"));
			transaction.setProduct_id(rs.getInt("product_id"));
			transaction.setBuyer_id(rs.getInt("buyer_id"));
			transaction.setSeller_id(rs.getInt("seller_id"));
			transaction.setAmount(rs.getInt("amount"));
			transaction.setTransaction_date(rs.getDate("transaction_date"));
			
			transaction.setBuyer_name(rs.getString("buyer_name"));;
			transaction.setSeller_name(rs.getString("seller_name"));
			transaction.setProduct_title(rs.getString("product_title"));
			
			return transaction;
		}

}
