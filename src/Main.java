
import java.util.Scanner;

import controller.MemberController;
import controller.PointController;
import controller.ProductController;
import controller.TransactionController;
import model.dto.MemberDTO;
import util.CommonUtil;

public class Main {
	
	private static Scanner sc = new Scanner(System.in);

	public static void main(String[] args) {
		
		System.out.println("=================================================");
		System.out.println("   ANABADA 중고거래 플랫폼에 오신 것을 환영합니다!");
		System.out.println("=================================================");
		
		MemberController memberController = new MemberController();
		
		while(true) {
			//1.로그인/회원가입
			MemberDTO currentUser = memberController.AuthMenu();
			
			//0. 사용 유저가 없으면 종료
			if(currentUser == null) {
				System.out.println("\n프로그램을 종료합니다.");
				System.out.println("이용해 주셔서 감사합니다!");
				sc.close();
				return;
			}
			
			//2.메인 메뉴 실행 (로그아웃 -> false)
			boolean exit = runMainMenu(currentUser, memberController);
			
			//회원탈퇴로 인한 종료
			if(exit) {
				continue;
			}
		}
	}

	//메인 메뉴 실행 메서드
	private static boolean runMainMenu(MemberDTO currentUser, MemberController memberController) {
		
		int userId = currentUser.getUser_id();
		String username = currentUser.getUsername();
		
		ProductController productController = new ProductController(userId);
		PointController pointController = new PointController(userId);
		TransactionController transactionController = new TransactionController(userId);
		
		while(true) {
			showMainMenu(username);
			int job = CommonUtil.getInt("선택>> ");
			
			switch(job) {
				case 1 -> { productController.start(); }
				case 2 -> { pointController.start(); }
				case 3 -> { transactionController.start(); }
				case 4 -> { 
					boolean deleted = memberController.MemberInfoMenu(userId);
					if(deleted) {
						return true; //회원 탈퇴
					}
				}
				case 5 -> { 
					if(logout()) {
						return false; //로그아웃
					}
				}
				default -> { System.out.println("[알림] 잘못된 선택입니다. 다시 선택해주세요."); }
			}
		}
	}

	private static boolean logout() {
		System.out.print("로그아웃 하시겠습니까? (Y/N): ");
		String input = sc.nextLine().trim();
		
		if(input.equalsIgnoreCase("Y")) {
			System.out.println("[알림] 로그아웃 되었습니다.");
			return true;
		}
		
		return false;
	}

	//메인 메뉴 출력 메서드
	private static void showMainMenu(String username) {
		System.out.println("\n================================");
		System.out.println("     " + username + "님, 환영합니다!");
		System.out.println("================================");
		System.out.println("1. 상품(구매/판매)");
		System.out.println("2. 포인트 관리");
		System.out.println("3. 거래내역");
		System.out.println("4. 내 정보");
		System.out.println("5. 로그아웃");
		System.out.println("================================");
	}

}
