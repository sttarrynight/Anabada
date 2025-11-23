package controller;

import java.util.List;

import model.dto.PointHistoryDTO;
import model.dto.TransactionType;
import service.MemberService;
import service.PointService;
import util.CommonUtil;
import view.PointHistoryView;

public class PointController {

	private PointService pointService = new PointService();
	private MemberService memberService = new MemberService();
	private int currentUserId;
	
	public PointController(int userId) {
		this.currentUserId = userId;
	}
	
	public void start() {
		while(true) {
			PointHistoryView.showPointMenu();
			int job = CommonUtil.getInt("선택 >> ");
			
			switch(job) {
				case 1 -> { viewBalance(); }
				case 2 -> { chargePoint(); }
				case 3 -> { withdrawPoint(); }
				case 4 -> { viewPointHistory(); }
				case 0 -> { return; }
				default -> { System.out.println("[알림] 잘못된 선택입니다."); }
			}
		}
	}

	//4
	private void viewPointHistory() {
		while(true) {
			PointHistoryView.showPointHistoryMenu();
			int job = CommonUtil.getInt("선택 >> ");
			
			List<PointHistoryDTO> historyList = null;
			
			switch(job) {
				case 1 -> { historyList = pointService.getPointHistory(currentUserId); }
				case 2 -> { historyList = pointService.getPointHistoryByType(currentUserId, TransactionType.CHARGE); }
				case 3 -> { historyList = pointService.getPointHistoryByType(currentUserId, TransactionType.WITHDRAW); }
				case 4 -> { historyList = pointService.getPointHistoryByType(currentUserId, TransactionType.PURCHASE); }
				case 5 -> { historyList = pointService.getPointHistoryByType(currentUserId, TransactionType.SALE); }
				case 0 -> { return; }
				default -> { System.out.println("[알림] 잘못된 선택입니다."); continue; }
			}
			
			if(historyList != null) {
				PointHistoryView.printPointHistoryList(historyList);
			}
		}
	}

	//3
	private void withdrawPoint() {
		System.out.println("\n========== 포인트 인출 ==========");
		int balance = memberService.getPointBalance(currentUserId);
		System.out.println("현재 잔액: " + balance + "원");
		
		int amount = CommonUtil.getInt("인출할 금액: ");
		
		if(amount <= 0) {
			System.out.println("[알림] 인출 금액은 0보다 커야 합니다.");
			return;
		}
		
		if(amount > balance) {
			System.out.println("[알림] 잔액이 부족합니다.");
			return;
		}
		
		boolean confirm = CommonUtil.getConfirm(amount + "원을 인출하시겠습니까? (Y/N): ");
		if(!confirm) {
			System.out.println("[알림] 인출을 취소했습니다.");
			return;
		}
		
		pointService.withdrawPoint(currentUserId, amount);
	}

	//2
	private void chargePoint() {
		System.out.println("\n========== 포인트 충전 ==========");
		int amount = CommonUtil.getInt("충전할 금액: ");
		
		if(amount <= 0) {
			System.out.println("[알림] 충전 금액은 0보다 커야 합니다.");
			return;
		}
		
		boolean confirm = CommonUtil.getConfirm(amount + "원을 충전하시겠습니까? (Y/N): ");
		if(!confirm) {
			System.out.println("[알림] 충전을 취소했습니다.");
			return;
		}
		
		pointService.chargePoint(currentUserId, amount);
	}

	//1
	private void viewBalance() {
		int balance = memberService.getPointBalance(currentUserId);
		System.out.println("\n========== 잔액 조회 ==========");
		System.out.println("현재 포인트 잔액: " + balance + "원");
		System.out.println("==============================");
	}
}
