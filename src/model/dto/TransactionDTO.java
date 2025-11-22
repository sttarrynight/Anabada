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
public class TransactionDTO {

	@NonNull
	private Integer transaction_id; //Pk
	private Integer product_id; //Fk -> 상품 삭제 시, null로 변경
	private Integer buyer_id; //Fk -> 회원 탈퇴 시, null로 변경
	private Integer seller_id; //Fk -> 회원 탈퇴 시, null로 변경
	@NonNull
	private Integer amount;
	@NonNull
	private Date transaction_date;
	
	//join
	private String buyer_name;
	private String seller_name;
	private String product_title;
}
