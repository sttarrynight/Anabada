package service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import model.dao.MemberDAO;
import model.dao.PointHistoryDAO;
import model.dto.MemberDTO;
import model.dto.PointHistoryDTO;
import model.dto.TransactionType;
import util.DBUtil;

public class PointService {

	private MemberDAO memberDAO = new MemberDAO();
	private PointHistoryDAO pointHistoryDAO = new PointHistoryDAO();
	
	//포인트 충전 (트랜잭션 처리)
	public boolean chargePoint(int userId, int amount) {
		
		//유효성 검증
		if(amount <= 0) {
			System.out.println("[알림] 충전 금액은 0보다 커야 합니다.");
			return false;
		}
		
		//하나의 DB흐름을 위한 Connection
		Connection conn = null;
		
		try {
			conn = DBUtil.dbConnect();
			conn.setAutoCommit(false);
			
			//1.현재 잔액 조회
			MemberDTO member = memberDAO.selectById(conn,userId);
			if(member == null) {
				throw new Exception("[알림] 사용자를 찾을 수 없습니다.");
			}
			
			int currentBalance = member.getPoint_balance();
			int newBalance = member.getPoint_balance() + amount;
			
			//2.포인트 증가
			memberDAO.updatePoint(conn, userId, amount);
			
			//3.포인트 내역 추가
			String description = amount + "원 충전";
			pointHistoryDAO.insert(conn, userId, TransactionType.CHARGE, amount, newBalance, null, description);
			
			conn.commit(); //커밋
			System.out.println("[알림] " + amount + "원이 충전되었습니다. 현재 잔액: " + newBalance + "원");
			return true;
			
		} catch (Exception e) {
			if(conn != null) {
				try {
					conn.rollback(); //롤백
				} catch(SQLException ex) {
					ex.printStackTrace();
				}
			}
			System.out.println("[알림] 포인트 충전에 실패했습니다: " + e.getMessage());
			return false;
		} finally {
			DBUtil.dbDisconnect(conn, null, null);
		}
	}
	
	//포인트 인출(트랜잭션 처리)
	public boolean withdrawPoint(int userId, int amount) {
		
		//유효성 검증
		if(amount <= 0) {
			System.out.println("[알림] 인출 금액은 0보다 커야 합니다.");
			return false;
		}
		
		//하나의 DB흐름을 위한 Connection
		Connection conn = null;
		
		try {
			conn = DBUtil.dbConnect();
			conn.setAutoCommit(false);
			
			//1.현재 잔액 조회
			MemberDTO member = memberDAO.selectById(conn,userId);
			if(member == null) {
				throw new Exception("[알림] 사용자를 찾을 수 없습니다.");
			}
			
			int currentBalance = member.getPoint_balance();
			
			//2.잔액 부족 체크
			if(currentBalance < amount) {
				throw new Exception("[알림] 잔액이 부족합니다. 현재 잔액: " + currentBalance + "원");
			}
			
			int newBalance = member.getPoint_balance() - amount;
			
			//3.포인트 감소
			memberDAO.updatePoint(conn, userId, -amount);
			
			//3.포인트 내역 추가
			String description = amount + "원 인출";
			pointHistoryDAO.insert(conn, userId, TransactionType.WITHDRAW, -amount, newBalance, null, description);
			
			conn.commit(); //커밋
			System.out.println("[알림] " + amount + "원이 인출되었습니다. 현재 잔액: " + newBalance + "원");
			return true;
			
		} catch (Exception e) {
			if(conn != null) {
				try {
					conn.rollback(); //롤백
				} catch(SQLException ex) {
					ex.printStackTrace();
				}
			}
			System.out.println("[알림] 포인트 인출에 실패했습니다: " + e.getMessage());
			return false;
		} finally {
			DBUtil.dbDisconnect(conn, null, null);
		}
	}

	//포인트 내역 조회(전체)
	public List<PointHistoryDTO> getPointHistory(int userId) {
		return pointHistoryDAO.selectByUserId(userId);
	}
	
	//포인트 내역 조회(종류별)
	public List<PointHistoryDTO> getPointHistoryByType(int userId, TransactionType transactionType) {
		return pointHistoryDAO.selectByUserIdAndType(userId, transactionType);
	}
}
