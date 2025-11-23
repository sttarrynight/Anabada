package controller;

import model.dto.MemberDTO;
import service.MemberService;
import util.CommonUtil;
import view.MemberView;

public class MemberController {

	private MemberService memberService = new MemberService();
	private int currentUserId;
	
	
	//로그인, 회원가입
	public MemberDTO AuthMenu() {
		
		while(true) {
			MemberView.showAuthMenu();
			int job = CommonUtil.getInt("작업 선택 >> ");
			
			switch(job) {
			case 1 -> {
				MemberDTO member = login();
				if(member != null) {
					this.currentUserId = member.getUser_id();
					return member;
				}
			}
			case 2 -> {
				register();
			}
			case 0 -> {return null;}
			default -> {System.out.println("[알림] 잘못된 선택입니다.");}
			}
		}
	}
	
	//내 정보 관리
	public boolean MemberInfoMenu(int userId) {
		
		while(true) {
			MemberView.showMemberInfoMenu();
			int job = CommonUtil.getInt("작업 선택 >> ");
			
			switch(job) {
			case 1 -> {viewMemberInfo();}
			case 2 -> {updatePassword();}
			case 3 -> {
				if(deleteMember()) {
					return true;
				}
			}
			case 0 -> {return false;}
			default -> {System.out.println("[알림] 잘못된 선택입니다.");}
			}
		}
	}

	private boolean deleteMember() {
		System.out.println("\n========== 회원 탈퇴 ==========");
		System.out.println("[알림] 회원 탈퇴 시 모든 데이터가 삭제됩니다.");
		
		boolean confirm = CommonUtil.getConfirm("** 정말 탈퇴하시겠습니까? (Y/N): ");
		if(!confirm) {
			System.out.println("[알림] 탈퇴를 취소했습니다.");
			return false;
		}
		
		String password = CommonUtil.getString("비밀번호 확인: ");
		return memberService.deleteMember(currentUserId, password);
	}

	private void updatePassword() {
		System.out.println("\n========== 비밀번호 수정 ==========");
		String currentPassword = CommonUtil.getString("현재 비밀번호: ");
		String newPassword = CommonUtil.getString("새 비밀번호: ");
		String newPasswordConfirm = CommonUtil.getString("새 비밀번호 확인: ");
		
		if(!newPassword.equals(newPasswordConfirm)) {
			System.out.println("[알림] 새 비밀번호가 일치하지 않습니다.");
			return;
		}
		
		memberService.updatePassword(currentUserId, currentPassword, newPassword);
	}

	//내정보 조회
	private void viewMemberInfo() {
		MemberDTO member = memberService.getMemberInfo(currentUserId);
		MemberView.printMemberInfo(member);
	}

	//회원가입
	private void register() {
		System.out.println("\n========== 회원가입 ==========");
		String username = CommonUtil.getString("아이디: ");
		String password = CommonUtil.getString("비밀번호: ");
		String passwordConfirm = CommonUtil.getString("비밀번호 확인: ");
		
		if(!password.equals(passwordConfirm)) {
			System.out.println("비밀번호가 일치하지 않습니다.");
			return;
		}
		
		memberService.register(username, password);
	}

	//로그인
	private MemberDTO login() {
		System.out.println("\n========== 로그인 ==========");
		String username = CommonUtil.getString("아이디: ");
		String password = CommonUtil.getString("비밀번호: ");
		return memberService.login(username, password);
	}
}
