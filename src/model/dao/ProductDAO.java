package model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import model.dto.ProductDTO;
import model.dto.ProductStatus;
import util.DBUtil;

public class ProductDAO {

	//모든 상품 조회
	//판매중인 상품 조회(STATUS)
	//특정 상품 조회(ID)
	//특정 상품 수정
	//특정 상품 삭제
	
	/*
	 * 트랜잭션 처리
	 * */
	//특정 상품 상태 변경(트랜잭션)
	public int updateStatus(Connection conn, int productId, ProductStatus status) throws SQLException {
		PreparedStatement st = null;
		
		String sql = "update products set status = ?, updated_at = sysdate "
				+ "where product_id = ?";
		
		int result = 0;
		
		try {
			conn = DBUtil.dbConnect();
			st = conn.prepareStatement(sql);
			st.setString(1, status.getCode());
			st.setInt(2, productId);
			
			result = st.executeUpdate();
			
		} finally {
			DBUtil.dbDisconnect(null, st, null);
		}
		return result;
	}
	
	//특정 상품 조회 (트랜잭션)
	public ProductDTO selectById(Connection conn, int productId) throws SQLException {
		PreparedStatement st = null;
		ResultSet rs = null;
		
		String sql = "select p.*, m.username as seller_name "
				+ "from products p "
				+ "join members m on p.seller_id = m.user_id "
				+ "where product_id = ? ";
		
		ProductDTO product = null;
		
		try {
			conn = DBUtil.dbConnect();
			st = conn.prepareStatement(sql);
			st.setInt(1,productId);
			rs = st.executeQuery();
			
			if(rs.next()) {
				product = makeProduct(rs);
			}
			
		} finally {
			DBUtil.dbDisconnect(null, st, rs);
		}
		return product;
	}
	
	//상품 삭제
	public int delete(int productId) {
		Connection conn = null;
		PreparedStatement st = null;
		
		String sql = "delete from products where product_id = ?";
		
		int result = 0;
		
		try {
			conn = DBUtil.dbConnect();
			st = conn.prepareStatement(sql);
			st.setInt(1, productId);
			
			result = st.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.dbDisconnect(conn, st, null);
		}
		return result;
	}
	
	//상품 수정
	public int update(ProductDTO product) {
		Connection conn = null;
		PreparedStatement st = null;
		
		String sql = "update products set title = ?, description = ?, price = ?, updated_at = sysdate "
				+ "where product_id = ?";
		
		int result = 0;
		
		try {
			conn = DBUtil.dbConnect();
			st = conn.prepareStatement(sql);
			st.setString(1, product.getTitle());
			st.setString(2, product.getDescription());
			st.setInt(3, product.getPrice());
			st.setInt(4, product.getProduct_id());
			
			result = st.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.dbDisconnect(conn, st, null);
		}
		return result;
	}
	
	//상품 등록
	public int insert(ProductDTO product) {
		Connection conn = null;
		PreparedStatement st = null;
		
		String sql = "insert into products (product_id, seller_id, title, description, price, status, created_at) "
				+ "values(seq_product.nextval, ?, ?, ?, ?, ?, sysdate)";
		
		int result = 0;
		
		try {
			conn = DBUtil.dbConnect();
			st = conn.prepareStatement(sql);
			st.setInt(1, product.getSeller_id());
			st.setString(2, product.getTitle());
			st.setString(3, product.getDescription());
			st.setInt(4, product.getPrice());
			st.setString(5, product.getStatus().getCode());
			
			result = st.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.dbDisconnect(conn, st, null);
		}
		return result;
	}
	
	//특정 판매자의 특정 상태 상품 조회
	public List<ProductDTO> selectBySellerIdAndStatus(int sellerId, ProductStatus status) {
		Connection conn = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		
		String sql = "select p.*, m.username as seller_name "
					+ "from products p "
					+ "join members m on p.seller_id = m.user_id "
					+ "where p.seller_id = ? "
					+ "and p.status = ? "
					+ "order by nvl(p.updated_at,p.created_at) desc";
		
		List<ProductDTO> productList = new ArrayList<>();
		
		try {
			conn = DBUtil.dbConnect();
			st = conn.prepareStatement(sql);
			st.setInt(1, sellerId);
			st.setString(2,status.getCode());
			rs = st.executeQuery();
			
			while(rs.next()) {
				ProductDTO product = makeProduct(rs);
				productList.add(product);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.dbDisconnect(conn, st, rs);
		}
		return productList;
	}
	
	//특정 판매자의 상품 조회
	public List<ProductDTO> selectBySellerId(int sellerId) {
		Connection conn = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		
		String sql = "select p.*, m.username as seller_name "
					+ "from products p "
					+ "join members m on p.seller_id = m.user_id "
					+ "where p.seller_id = ? "
					+ "order by nvl(p.updated_at,p.created_at) desc";
		
		List<ProductDTO> productList = new ArrayList<>();
		
		try {
			conn = DBUtil.dbConnect();
			st = conn.prepareStatement(sql);
			st.setInt(1, sellerId);
			rs = st.executeQuery();
			
			while(rs.next()) {
				ProductDTO product = makeProduct(rs);
				productList.add(product);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.dbDisconnect(conn, st, rs);
		}
		return productList;
	}
	
	//판매중인 상품만 조회
	public List<ProductDTO> selectByStatus(ProductStatus status) {
		Connection conn = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		
		String sql = "select p.*, m.username as seller_name "
					+ "from products p "
					+ "join members m on p.seller_id = m.user_id "
					+ "where p.status = ? "
					+ "order by nvl(p.updated_at,p.created_at) desc";
		
		List<ProductDTO> productList = new ArrayList<>();
		
		try {
			conn = DBUtil.dbConnect();
			st = conn.prepareStatement(sql);
			st.setString(1, status.getCode());
			rs = st.executeQuery();
			
			while(rs.next()) {
				ProductDTO product = makeProduct(rs);
				productList.add(product);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.dbDisconnect(conn, st, rs);
		}
		return productList;
	}
	
	//특정 상품 조회
	public ProductDTO selectById(int productId) {
		Connection conn = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		
		String sql = "select p.*, m.username as seller_name "
				+ "from products p "
				+ "join members m on p.seller_id = m.user_id "
				+ "where product_id = ? ";
		
		ProductDTO product = null;
		
		try {
			conn = DBUtil.dbConnect();
			st = conn.prepareStatement(sql);
			st.setInt(1,productId);
			rs = st.executeQuery();
			
			if(rs.next()) {
				product = makeProduct(rs);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.dbDisconnect(conn, st, rs);
		}
		return product;
	}
	
	//모든 상품 조회
	public List<ProductDTO> selectAll() {
		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;
		
		String sql = "select p.*, m.username as seller_name "
				+ "from products p "
				+ "join members m on p.seller_id = m.user_id "
				+ "order by p.created_at desc";
		
		List<ProductDTO> productList = new ArrayList<>();
		
		try {
			conn = DBUtil.dbConnect();
			st = conn.createStatement();
			rs = st.executeQuery(sql);
			
			while(rs.next()) {
				ProductDTO product = makeProduct(rs);
				productList.add(product);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.dbDisconnect(conn, st, rs);
		}
		return productList;
	}

	private ProductDTO makeProduct(ResultSet rs) throws SQLException {
		ProductDTO product = new ProductDTO();
		
		product.setProduct_id(rs.getInt("product_id"));
		product.setSeller_id(rs.getInt("seller_id"));
		product.setTitle(rs.getString("title"));
		product.setDescription(rs.getString("description"));
		product.setPrice(rs.getInt("price"));
		product.setStatus(ProductStatus.fromCode(rs.getString("status"))); //String -> ENUM
		product.setCreated_at(rs.getDate("created_at"));
		product.setUpdated_at(rs.getDate("updated_at"));
		product.setSeller_name(rs.getString("seller_name"));
		
		return product;
	}
}
