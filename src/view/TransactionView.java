package view;

import java.util.List;

import model.dto.TransactionDTO;
import util.CommonUtil;

public class TransactionView {
	
	//거래내역 관리 메뉴
	public static void showTransactionMenu() {
		System.out.println("\n========== 거래내역 관리 ==========");
		System.out.println("1. 구매 내역 조회");
		System.out.println("2. 판매 내역 조회");
		System.out.println("0. 이전 메뉴");
		System.out.println("==================================");
	}
	
	//거래내역 목록
	public static void printTransactionList(List<TransactionDTO> transactionList, String title) {
		if(transactionList == null || transactionList.isEmpty()) {
			System.out.println("\n[알림] 조회된 " + title + " 내역이 없습니다.");
			return;
		}
		
		System.out.println("\n========== " + title + " 내역 ==========");
		System.out.printf("%-5s %-15s %-10s %-10s %-10s %-20s%n", 
				"ID", "상품명", "금액", "구매자", "판매자", "거래일");
		System.out.println("-------------------------------------------------------------------------------");
		
		for(TransactionDTO transaction : transactionList) {
			System.out.printf("%-5d %-12s %,-11d %-10s %-10s %-20s%n",
					transaction.getTransaction_id(),
					CommonUtil.truncate(transaction.getProduct_title(), 15),
					transaction.getAmount(),
					transaction.getBuyer_name(),
					transaction.getSeller_name(),
					CommonUtil.formatDate(transaction.getTransaction_date()));
		}
		
		System.out.println("===============================================================================");
		System.out.println("총 " + transactionList.size() + "건의 거래");
	}
	
}
