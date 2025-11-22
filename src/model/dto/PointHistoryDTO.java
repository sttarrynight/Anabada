package model.dto;

import java.sql.Date;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@NoArgsConstructor
@RequiredArgsConstructor
@Getter @Setter @ToString
public class PointHistoryDTO {

	@NonNull
	private Integer history_id; //Pk
	@NonNull
	private Integer user_id; //Fk
	@NonNull
	private TransactionType transaction_type; // 'CHARGE','WITHDRAW','PURCHASE','SALE'
	@NonNull
	private Integer amount;
	@NonNull
	private Integer balance_after;
	private Integer transaction_id; //Fk -> 포인트 충전/인출은 거래가 아님
	@NonNull
	private Date created_at;
	private String description;
	
	private String username;
}
