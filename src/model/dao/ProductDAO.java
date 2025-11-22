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
	
	//판매중인 상품만 조회
	public List<ProductDTO> selectByStatus(ProductStatus status) {
		Connection conn = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		
		String sql = "select p.*, m.usrename as seller_name "
					+ "from products p "
					+ "join members m on p.seller_id = m.user_id "
					+ "where status = ? "
					+ "order by created_at desc";
		
		List<ProductDTO> productList = new ArrayList<>();
		
		try {
			conn = DBUtil.dbConnect();
			st = conn.prepareStatement(sql);
			st.setString(1, status.getCode());
			rs = st.executeQuery();
			
			while(rs.next()) {
				ProductDTO product = makeProduct(rs);
				if(product.getStatus() == ProductStatus.SALE) {
					productList.add(product);
				}
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
		
		String sql = "select p.*, m.usrename as seller_name "
				+ "from products p "
				+ "join members m on p.seller_id = m.user_id "
				+ "where product_id = ? "
				+ "order by created_at desc";
		
		ProductDTO product = null;
		
		try {
			conn = DBUtil.dbConnect();
			st = conn.prepareStatement(sql);
			st.setInt(1,productId);
			rs = st.executeQuery();
			
			while(rs.next()) {
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
		
		String sql = "select p.*, m.usrename as seller_name "
				+ "from products p "
				+ "join members m on p.seller_id = m.user_id "
				+ "order by created_at desc";
		
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
		product.setUpdated_at(rs.getDate("updeated_at"));
		product.setSeller_name(rs.getString("seller_name"));
		
		return product;
	}
}
