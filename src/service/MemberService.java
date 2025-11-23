package service;

import model.dao.MemberDAO;
import model.dto.MemberDTO;

public class MemberService {
	
	private MemberDAO memberDAO = new MemberDAO();
	
	//회원가입
	public boolean register(String username, String password) {
		
		//1.아이디(username) 중복체크
		MemberDTO existMember = memberDAO.selectByUsername(username);
		if(existMember != null) {
			System.out.println("[알림] 이미 존재하는 아이디입니다.");
			return false;
		}
		
		//2.회원등록
		MemberDTO newMember = new MemberDTO();
		newMember.setUsername(username);
		newMember.setPassword(password);
		
		int result = memberDAO.insert(newMember);
		
		if(result > 0) {
			System.out.println("[알림] 회원가입이 완료되었습니다.");
			return true;
		} else {
			System.out.println("[알림] 회원가입에 실패하였습니다.");
			return false;
		}
	}
	
	//로그인
	public MemberDTO login(String username, String password) {
		
		MemberDTO member = memberDAO.selectByUsername(username);
		
		if(member == null) {
			System.out.println("[알림] 존재하지 않는 아이디입니다.");
			return null;
		}
		
		if(!member.getPassword().equals(password)) {
			System.out.println("[알림] 비밀번호가 일치하지 않습니다.");
			return null;
		}
		
		System.out.println("[알림] 로그인 성공! 환영합니다, " + member.getUsername() + "님!");
		return member;
	}
	
	//회원정보 조회
	public MemberDTO getMemberInfo(int userId) {
		return memberDAO.selectById(userId);
	}
	
	//비밀번호 수정
	public boolean updatePassword(int userId, String currentPassword, String newPassword) {
		
		//1.현재 비밀번호 확인
		MemberDTO member = memberDAO.selectById(userId);
		
		if(!member.getPassword().equals(currentPassword)) {
			System.out.println("[알림] 현재 비밀번호가 일치하지 않습니다.");
			return false;
		}
		
		//2.새 비밀번호로 변경
		member.setPassword(newPassword);
		int result = memberDAO.update(member);
		
		if(result > 0) {
			System.out.println("[알림] 비밀번호가 변경되었습니다.");
			return true;
		} else {
			System.out.println("[알림] 비밀번호 변경에 실패했습니다.");
			return false;
		}
	}
	
	//회원 탈퇴
	public boolean deleteMember(int userId, String password) {
		
		//1. 비밀번호 확인
		MemberDTO member = memberDAO.selectById(userId);
		
		if(!member.getPassword().equals(password)) {
			System.out.println("[알림] 비밀번호가 일치하지 않습니다.");
			return false;
		}
		
		//2. 회원 삭제
		int result = memberDAO.delete(userId);
		
		if(result > 0) {
			System.out.println("[알림] 회원 탈퇴가 완료되었습니다.");
			return true;
		} else {
			System.out.println("[알림] 회원 탈퇴에 실패했습니다.");
			return false;
		}
	}
	
	//잔액 조회
	public int getPointBalance(int userId) {
		MemberDTO member = memberDAO.selectById(userId);
		return member != null ? member.getPoint_balance() : 0;
	}
}
