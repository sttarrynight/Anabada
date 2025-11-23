package view;

import java.util.List;

import model.dto.PointHistoryDTO;
import util.CommonUtil;

public class PointHistoryView {
	
	//포인트 관리 메뉴
	public static void showPointMenu() {
		System.out.println("\n========== 포인트 관리 ==========");
		System.out.println("1. 잔액 조회");
		System.out.println("2. 포인트 충전");
		System.out.println("3. 포인트 인출");
		System.out.println("4. 포인트 내역 조회");
		System.out.println("0. 이전 메뉴");
		System.out.println("================================");
	}
	
	//포인트 내역 서브 메뉴
	public static void showPointHistoryMenu() {
		System.out.println("\n========== 포인트 내역 조회 ==========");
		System.out.println("1. 전체 내역");
		System.out.println("2. 충전 내역");
		System.out.println("3. 인출 내역");
		System.out.println("4. 구매 내역");
		System.out.println("5. 판매 내역");
		System.out.println("0. 이전 메뉴");
		System.out.println("======================================");
	}
	
	//포인트 내역 목록
	public static void printPointHistoryList(List<PointHistoryDTO> pointHistoryList) {
		if(pointHistoryList == null || pointHistoryList.isEmpty()) {
			System.out.println("\n[알림] 조회된 포인트 내역이 없습니다.");
			return;
		}
		
		System.out.println("\n========== 포인트 내역 ==========");
		System.out.printf("%-5s %-10s %-10s %-12s %-20s %-20s%n", 
				"ID", "거래유형", "금액", "거래 후 잔액", "날짜", "설명");
		System.out.println("---------------------------------------------------------------------------------");
		
		for(PointHistoryDTO history : pointHistoryList) {
			System.out.printf("%-5d %-10s %,10d %,12d %-20s %-20s%n",
					history.getHistory_id(),
					history.getTransaction_type().getDescription(),
					history.getAmount(),
					history.getBalance_after(),
					CommonUtil.formatDate(history.getCreated_at()),
					CommonUtil.truncate(history.getDescription() != null ? history.getDescription() : "", 20));
		}
		
		System.out.println("==================================================================================");
		System.out.println("총 " + pointHistoryList.size() + "건의 내역");
	}
}
