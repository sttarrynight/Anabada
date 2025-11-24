package service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import model.dao.MemberDAO;
import model.dao.PointHistoryDAO;
import model.dao.ProductDAO;
import model.dao.TransactionDAO;
import model.dto.MemberDTO;
import model.dto.ProductDTO;
import model.dto.ProductStatus;
import model.dto.TransactionDTO;
import model.dto.TransactionType;
import util.DBUtil;

public class TransactionService {

	private MemberDAO memberDAO = new MemberDAO();
	private ProductDAO productDAO = new ProductDAO();
	private TransactionDAO transactionDAO = new TransactionDAO();
	private PointHistoryDAO pointHistoryDAO = new PointHistoryDAO();
	
	//상품 구매(트랜잭션 처리)
	public boolean purchaseProduct(int buyerId, int productId) {
		
		//하나의 DB 흐름을 위한 Connection
		Connection conn = null;
		
		try {
			conn = DBUtil.dbConnect();
			conn.setAutoCommit(false);
			
			//1.상품 정보 조회
			ProductDTO product = productDAO.selectById(conn, productId);
			if(product == null) {
				throw new Exception("존재하지 않는 상품입니다.");
			}
			
			//2.상품 상태 확인
			if(product.getStatus() == ProductStatus.SOLD) {
				throw new Exception("이미 판매완료된 상품입니다.");
			}
			
			//3.본인 상품 구매 방지
			if(product.getSeller_id() == buyerId) {
				throw new Exception("본인의 상품은 구매할 수 없습니다.");
			}
			
			//4.구매자/판매자 정보 조회
			MemberDTO buyer = memberDAO.selectById(conn, buyerId);
			MemberDTO seller = memberDAO.selectById(conn, product.getSeller_id());
			
			if(buyer == null || seller == null) {
				throw new Exception("사용자 정보를 찾을 수 없습니다.");
			}
			
			int price = product.getPrice();
			
			//5.구매자 잔액 확인
			if(buyer.getPoint_balance() < price) {
				throw new Exception("잔액이 부족합니다. 상품 금액: " + price + "원, 현재 잔액: " + buyer.getPoint_balance() + "원");
			}
			
			//6.구매자 포인트 차감
			memberDAO.updatePoint(conn, buyerId, -price);
			int buyerNewBalance = buyer.getPoint_balance() - price;
			
			//7.판매자 포인트 증가
			memberDAO.updatePoint(conn, product.getSeller_id(), price);
			int sellerNewBalance = seller.getPoint_balance() + price;
			
			//8.상품 상태 변경(판매중 -> 판매완료)
			productDAO.updateStatus(conn, productId, ProductStatus.SOLD);
			
			//9.거래내역 생성
			int transactionId = transactionDAO.insert(conn, productId, buyerId, product.getSeller_id(), price);
			
			//10.구매자 포인트 내역 생성
			String buyerDescription = "[구매] " + product.getTitle();
			pointHistoryDAO.insert(conn, buyerId, TransactionType.PURCHASE, price, buyerNewBalance, transactionId, buyerDescription);
			
			//11.판매자 포인트 내역 생성
			String sellerDescription = "[판매] " + product.getTitle();
			pointHistoryDAO.insert(conn, product.getSeller_id(), TransactionType.SALE, price, sellerNewBalance, transactionId, sellerDescription);
			
			//모든 작업 성공 시 커밋
			conn.commit();
			
			System.out.println("=================================");
			System.out.println("구매가 완료되었습니다!");
			System.out.println("상품명: " + product.getTitle());
			System.out.println("결제 금액: " + price + "원");
			System.out.println("남은 잔액: " + buyerNewBalance + "원");
			System.out.println("=================================");
			
			return true;
			
		} catch(Exception e) {
			if(conn != null) {
				try {
					conn.rollback(); //오류 발생 시 롤백
					System.out.println("[알림] 거래가 취소되었습니다.");
				} catch (SQLException ex) {
					ex.printStackTrace();
				}
			}
			System.out.println("[알림] 구매 실패: " + e.getMessage());
			return false;
		} finally {
			DBUtil.dbDisconnect(conn, null, null);
		}
	}
	
	//구매 내역 조회
	public List<TransactionDTO> getPurchaseHistory(int buyerId) {
		return transactionDAO.selectByBuyerId(buyerId);
	}
	
	//판매 내역 조회
	public List<TransactionDTO> getSaleHistory(int sellerId) {
		return transactionDAO.selectBySellerId(sellerId);
	}
	
	//모든 거래 내역 조회
	public List<TransactionDTO> getAllHistory(int userId) {
		return transactionDAO.selectAll(userId);
	}
}
