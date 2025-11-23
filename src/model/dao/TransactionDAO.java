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
	
		/*
		 * 트랜잭션 처리
		 * */
		//거래내역 추가(트랜잭션) - 생성된 ID 반환
		public int insert(Connection conn, int productId, int buyerId, int sellerId, int amount) throws SQLException {
			PreparedStatement st = null;
			ResultSet rs = null;
			
			String sql = "insert into transactions (transaction_id, product_id, buyer_id, seller_id, amount, transaction_date) "
					+ "values (seq_transaction.nextval, ?, ?, ?, ?, sysdate)";
			
			int transactionId = 0;
			
			try {
				st = conn.prepareStatement(sql, new String[] {"transaction_id"});
				st.setInt(1, productId);
				st.setInt(2, buyerId);
				st.setInt(3, sellerId);
				st.setInt(4, amount);
				
				st.executeUpdate();
				//생성된 transaction_id 반환
				rs = st.getGeneratedKeys();
				
				if(rs.next()) {
					transactionId = rs.getInt(1);
				} else {
					throw new SQLException("[알림] 거래내역 생성 실패: ID를 가져올 수 없습니다.");
				}
				
			} finally {
				DBUtil.dbDisconnect(null, st, rs);
			}
			
			return transactionId;
		}
		
	
		//판매내역 조회(개인)
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
	
		//구매내역 조회(개인)
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
		
		//모든 거래내역 조회(개인)
		public List<TransactionDTO> selectAll(int userId) {
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
					+ "where t.buyer_id = ? or t.seller_id = ? "
					+ "order by t.transaction_date desc";
			
			List<TransactionDTO> TransactionList = new ArrayList<>();
			
			try {
				conn = DBUtil.dbConnect();
				st = conn.prepareStatement(sql);
				st.setInt(1, userId);
				st.setInt(2, userId);
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

		private TransactionDTO makeTransaction(ResultSet rs) throws SQLException {
			TransactionDTO transaction = new TransactionDTO();
			
			transaction.setTransaction_id(rs.getInt("transaction_id"));
			
			int productId = rs.getInt("product_id");
			if(!rs.wasNull()) {
				transaction.setProduct_id(productId);
			}
			
			int buyerId = rs.getInt("buyer_id");
			if(!rs.wasNull()) {
				transaction.setBuyer_id(buyerId);
			}

			int sellerId = rs.getInt("seller_id");
			if(!rs.wasNull()) {
				transaction.setSeller_id(sellerId);
			}
			
			transaction.setAmount(rs.getInt("amount"));
			transaction.setTransaction_date(rs.getDate("transaction_date"));
			
			transaction.setBuyer_name(rs.getString("buyer_name"));;
			transaction.setSeller_name(rs.getString("seller_name"));
			transaction.setProduct_title(rs.getString("product_title"));
			
			return transaction;
		}

}
