package controller;

import java.util.List;

import model.dto.TransactionDTO;
import service.TransactionService;
import util.CommonUtil;
import view.TransactionView;

public class TransactionController {

	private TransactionService transactionService = new TransactionService();
	private int currentUserId;
	
	public TransactionController(int userId) {
		this.currentUserId = userId;
	}
	
	public void start() {
		while(true) {
			TransactionView.showTransactionMenu();
			int choice = CommonUtil.getInt("선택 >> ");
			
			switch(choice) {
				case 1 -> { viewPurchaseHistory(); }
				case 2 -> { viewSaleHistory(); }
				case 0 -> { return; }
				default -> { System.out.println("[알림] 잘못된 선택입니다."); }
			}
		}
	}

	//2
	private void viewSaleHistory() {
		List<TransactionDTO> saleList = transactionService.getSaleHistory(currentUserId);
		TransactionView.printTransactionList(saleList, "판매");
	}

	//1
	private void viewPurchaseHistory() {
		List<TransactionDTO> purchaseList = transactionService.getPurchaseHistory(currentUserId);
		TransactionView.printTransactionList(purchaseList, "구매");
	}
}
