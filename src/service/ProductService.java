package service;

import java.util.List;

import model.dao.ProductDAO;
import model.dto.ProductDTO;
import model.dto.ProductStatus;

public class ProductService {

	private ProductDAO productDAO = new ProductDAO();
	
	//판매중인 상품 조회
	public List<ProductDTO> getProductsOnSale() {
		return productDAO.selectByStatus(ProductStatus.SALE);
	}
	
	//모든 상품 조회(판매완료 포함)
	public List<ProductDTO> getAllProducts() {
		return productDAO.selectAll();
	}
	
	//특정 상품 상세 조회
	public ProductDTO getProductDetail(int productId) {
		return productDAO.selectById(productId);
	}
	
	//내 상품 조회(전체)
	public List<ProductDTO> getMyProducts(int sellerId) {
		return productDAO.selectBySellerId(sellerId);
	}
	
	//내 상품 조회(판매중)
	public List<ProductDTO> getMyProductsOnSale(int sellerId) {
		return productDAO.selectBySellerIdAndStatus(sellerId, ProductStatus.SALE);
	}
	
	//상품 등록
	public boolean registerProduct(int sellerId, String title, String description, int price) {
		
		//유효성 검증
		if(title == null || title.trim().isEmpty()) {
			System.out.println("[알림] 상품명을 입력해주세요.");
			return false;
		}
		
		if(price <= 0) {
			System.out.println("[알림] 가격은 0보다 커야합니다.");
		}
		
		//상품등록
		ProductDTO product = new ProductDTO();
		product.setSeller_id(sellerId);
		product.setTitle(title);
		product.setDescription(description);
		product.setPrice(price);
		product.setStatus(ProductStatus.SALE);
		
		int result = productDAO.insert(product);
		
		if(result > 0) {
			System.out.println("[알림] 상품이 등록되었습니다.");
			return true;
		} else {
			System.out.println("[알림] 상품 등록에 실패했습니다.");
			return false;
		}
	}
	
	//상품 수정
	public boolean updateProduct(int productId, int userId, String title, String description, int price) {
		
		//1.상품 존재 여부 확인
		ProductDTO product = productDAO.selectById(productId);
		if(product == null) {
			System.out.println("[알림] 존재하지 않는 상품입니다.");
			return false;
		}
		
		//2.본인 상품인지 확인
		if(product.getSeller_id() != userId) {
			System.out.println("[알림] 본인의 상품만 수정할 수 있습니다.");
			return false;
		}
		
		//3.판매완료된 상품은 수정 불가
		if(product.getStatus() == ProductStatus.SOLD) {
			System.out.println("판매완료된 상품은 수정할 수 없습니다.");
			return false;
		}
		
		//4.유효성 검증
		if(title == null || title.trim().isEmpty()) {
			System.out.println("[알림] 상품명을 입력해주세요.");
			return false;
		}
		
		if(price <= 0) {
			System.out.println("[알림] 가격은 0보다 커야 합니다.");
			return false;
		}
		
		//5.상품 수정
		product.setTitle(title);
		product.setDescription(description);
		product.setPrice(price);
		
		int result = productDAO.update(product);
		
		if(result > 0) {
			System.out.println("[알림] 상품이 수정되었습니다.");
			return true;
		} else {
			System.out.println("[알림] 상품 수정에 실패했습니다.");
			return false;
		}
	}
	
	//상품 삭제
	public boolean deleteProduct(int productId, int userId) {
		
		// 1. 상품 존재 여부 확인
		ProductDTO product = productDAO.selectById(productId);
		if(product == null) {
			System.out.println("[알림] 존재하지 않는 상품입니다.");
			return false;
		}
		
		// 2. 본인 상품인지 확인
		if(product.getSeller_id() != userId) {
			System.out.println("[알림] 본인의 상품만 삭제할 수 있습니다.");
			return false;
		}
		
		// 3. 판매완료된 상품은 삭제 불가 (거래내역 보존 위해)
		if(product.getStatus() == ProductStatus.SOLD) {
			System.out.println("[알림] 판매완료된 상품은 삭제할 수 없습니다.");
			return false;
		}
		
		// 4. 상품 삭제
		int result = productDAO.delete(productId);
		
		if(result > 0) {
			System.out.println("[알림] 상품이 삭제되었습니다.");
			return true;
		} else {
			System.out.println("[알림] 상품 삭제에 실패했습니다.");
			return false;
		}
	}
}
