package controller;

import java.util.List;

import model.dto.ProductDTO;
import service.ProductService;
import service.TransactionService;
import util.CommonUtil;
import view.ProductView;

public class ProductController {

	private ProductService productService = new ProductService();
	private TransactionService transactionService = new TransactionService();
	private int currentUserId;
	
	public ProductController(int userId) {
		this.currentUserId = userId;
	}
	
	public void start() {
		while(true) {
			ProductView.showProductMenu();
			int job = CommonUtil.getInt("선택 >> ");
			
			switch(job) {
				case 1 -> { viewProductsOnSale(); }
				case 2 -> { viewProductDetail(); }
				case 3 -> { purchaseProduct(); }
				case 4 -> { registerProduct(); }
				case 5 -> { viewMyProducts(); }
				case 6 -> { updateProduct(); }
				case 7 -> { deleteProduct(); }
				case 8 -> { viewAllProducts(); }
				case 0 -> {return;}
				default -> {System.out.println("[알림] 잘못된 선택입니다.");}
			}
		}
	}

	//8
	private void viewAllProducts() {
		List<ProductDTO> productList = productService.getAllProducts();
		ProductView.printProductList(productList);
	}

	//7
	private void deleteProduct() {
		int productId = CommonUtil.getInt("\n삭제할 상품 ID (0: 취소): ");
		if(productId == 0) {
			System.out.println("[알림] 삭제를 취소했습니다.");
			return;
		}
		
		boolean confirm = CommonUtil.getConfirm("**정말 삭제하시겠습니까? (Y/N): ");
		if(!confirm) {
			System.out.println("[알림] 삭제를 취소했습니다.");
			return;
		}
		productService.deleteProduct(productId, currentUserId);
	}

	//6
	private void updateProduct() {
		int productId = CommonUtil.getInt("\n수정할 상품 ID (0: 취소): ");
		if(productId == 0) {
			System.out.println("[알림] 수정을 취소했습니다.");
			return;
		}
		
		System.out.println("\n========== 상품 수정 ==========");
		String title = CommonUtil.getString("새 상품명: ");
		String description = CommonUtil.getString("새 상품 설명: ");
		int price = CommonUtil.getInt("새 가격: ");
		productService.updateProduct(productId, currentUserId, title, description, price);
	}

	//5
	private void viewMyProducts() {
		System.out.println("\n1. 판매중인 상품만  2. 전체 상품");
		int choice = CommonUtil.getInt("선택 >> ");
		
		List<ProductDTO> productList;
		if(choice == 1) {
			productList = productService.getMyProductsOnSale(currentUserId);
		} else {
			productList = productService.getMyProducts(currentUserId);
		}
		ProductView.printProductList(productList);
	}

	//4
	private void registerProduct() {
		System.out.println("\n========== 상품 등록 ==========");
		String title = CommonUtil.getString("상품명: ");
		String description = CommonUtil.getString("상품 설명: ");
		int price = CommonUtil.getInt("가격: ");
		productService.registerProduct(currentUserId, title, description, price);
	}
	
	//3
	private void purchaseProduct() {
		int productId = CommonUtil.getInt("\n구매할 상품 ID (0: 취소): ");
		if(productId == 0) {
			System.out.println("[알림] 구매를 취소했습니다.");
			return;
		}
		
		ProductDTO product = productService.getProductDetail(productId);
		if(product == null) return;
		
		ProductView.printProductDetail(product);
		
		boolean confirm = CommonUtil.getConfirm("\n**이 상품을 구매하시겠습니까? (Y/N): ");
		if(!confirm) {
			System.out.println("[알림] 구매를 취소했습니다.");
			return;
		}
		
		transactionService.purchaseProduct(currentUserId, productId);
	}
	
	//2
	private void viewProductDetail() {
		int productId = CommonUtil.getInt("조회할 상품 ID: ");
		ProductDTO product = productService.getProductDetail(productId);
		ProductView.printProductDetail(product);
	}
	
	//1
	private void viewProductsOnSale() {
		List<ProductDTO> productList = productService.getProductsOnSale();
		ProductView.printProductList(productList);
	}
}
