package util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class CommonUtil {

	private static Scanner sc = new Scanner(System.in);
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	//InputUtil
	//정수 입력 받기
	public static int getInt(String message) {
		System.out.print(message);
		while(true) {
			try {
				return Integer.parseInt(sc.nextLine().trim());
			} catch (NumberFormatException e) {
				System.out.print("[알림] 숫자를 입력해주세요: ");
			}
		}
	}
	
	//문자열 입력 받기
	public static String getString(String message) {
		System.out.print(message);
		return sc.nextLine().trim();
	}
	
	//Y/N 확인 입력 받기
	public static boolean getConfirm(String message) {
		System.out.print(message);
		String input = sc.nextLine().trim();
		return input.equalsIgnoreCase("Y");
	}
	
	//Scanner 종료
	public static void closeScanner() {
		if(sc != null) {
			sc.close();
		}
	}
	
	//outputUtil
	//날짜 포맷팅
	public static String formatDate(Date date) {
		if(date ==null) {
			return "";
		}
		return sdf.format(date);
	}
	
	//문자열 자르기 (출력 정렬용 - 리스트)
	public static String truncate(String str, int maxLength) {
		if(str == null) {
			return "";
		}
		if(str.length() <= maxLength) {
			return str;
		}
		return str.substring(0, maxLength - 2) + "..";
	}
}
