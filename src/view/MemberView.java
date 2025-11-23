package view;

import model.dto.MemberDTO;
import util.CommonUtil;

public class MemberView {
	
	//로그인, 회원가입 메뉴
	public static void showAuthMenu() {
		System.out.println("\n========== 로그인/회원가입 ==========");
		System.out.println("1. 로그인");
		System.out.println("2. 회원가입");
		System.out.println("0. 종료");
		System.out.println("====================================");
	}
	
	//내정보 메뉴
	public static void showMemberInfoMenu() {
		System.out.println("\n========== 내정보 관리 ==========");
		System.out.println("1. 정보 조회");
		System.out.println("2. 비밀번호 수정");
		System.out.println("3. 회원 탈퇴");
		System.out.println("0. 이전 메뉴");
		System.out.println("================================");
	}
	
	//회원 정보 출력
	public static void printMemberInfo(MemberDTO member) {
		
		if(member == null) {
			System.out.println("[알림] 회원 정보를 찾을 수 없습니다.");
			return;
		}
		
		System.out.println("\n========== 회원 정보 ==========");
		System.out.println("아이디: " + member.getUsername());
		System.out.println("포인트 잔액: " + member.getPoint_balance() + "원");
		System.out.println("가입일: " + CommonUtil.formatDate(member.getCreated_at()));
		
		if(member.getUpdated_at() != null) {
			System.out.println("최근 수정일: " + CommonUtil.formatDate(member.getUpdated_at()));
		}
		
		System.out.println("===============================");
	}
	
}
