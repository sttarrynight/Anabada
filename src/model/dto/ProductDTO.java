package model.dto;

import java.sql.Date;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class ProductDTO {

	@NonNull
	private Integer product_id; //Pk
	@NonNull
	private Integer seller_id; //Fk
	@NonNull
	private String title;
	private String description;
	@NonNull
	private Integer price;
	@NonNull
	private ProductStatus status; //'SALE','SOLD'
	@NonNull
	private Date created_at;
	private Date updated_at;
	
	
	//Join용 필드
	private String seller_name; // from MEMBERS
}
