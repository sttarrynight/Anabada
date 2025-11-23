package view;

import java.util.List;

import model.dto.ProductDTO;
import util.CommonUtil;

public class ProductView {
	
	//상품 관리 메뉴
	public static void showProductMenu() {
		System.out.println("\n========== 상품 관리 ==========");
		System.out.println("1. 판매중인 상품 조회");
		System.out.println("2. 상품 상세 조회");
		System.out.println("3. 상품 구매");
		System.out.println("4. 상품 등록");
		System.out.println("5. 내 상품 조회");
		System.out.println("6. 내 상품 수정");
		System.out.println("7. 내 상품 삭제");
		System.out.println("8. 모든 상품 조회 (판매완료 포함)");
		System.out.println("0. 이전 메뉴");
		System.out.println("==============================");
	}
	
	//상품 목록
	public static void printProductList(List<ProductDTO> productList) {
		if(productList == null || productList.isEmpty()) {
			System.out.println("\n조회된 상품이 없습니다.");
			return;
		}
		
		System.out.println("\n========== 상품 목록 ==========");
		System.out.printf("%-5s %-15s %-10s %-10s %-10s%n", "ID", "상품명", "가격", "판매자", "상태");
		System.out.println("-------------------------------------------------------------");
		
		for(ProductDTO product : productList) {
			System.out.printf("%-5d %-15s %-10d %-10s %-10s%n",
					product.getProduct_id(),
					CommonUtil.truncate(product.getTitle(), 15),
					product.getPrice(),
					product.getSeller_name(),
					product.getStatus().getDescription());
		}
		
		System.out.println("=================================================================");
		System.out.println("총 " + productList.size() + "개의 상품");
	}
	
	//상품 상세 정보
	public static void printProductDetail(ProductDTO product) {
		if(product == null) {
			System.out.println("\n조회된 상품이 없습니다.");
			return;
		}
		
		System.out.println("\n========== 상품 상세 정보 ==========");
		System.out.println("상품 ID: " + product.getProduct_id());
		System.out.println("상품명: " + product.getTitle());
		System.out.println("설명: " + (product.getDescription() != null ? product.getDescription() : "설명 없음"));
		System.out.println("가격: " + product.getPrice() + "원");
		System.out.println("판매자: " + product.getSeller_name());
		System.out.println("상태: " + product.getStatus().getDescription());
		System.out.println("등록일: " + CommonUtil.formatDate(product.getCreated_at()));
		
		if(product.getUpdated_at() != null) {
			System.out.println("수정일: " + CommonUtil.formatDate(product.getUpdated_at()));
		}
		
		System.out.println("====================================");
	}
	
}
