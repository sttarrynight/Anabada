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
public class MemberDTO {

	@NonNull
	private Integer user_id; //PK
	@NonNull
	private String username;
	@NonNull
	private String password;
	@NonNull
	private Integer point_balance;
	@NonNull
	private Date created_at;
	private Date updated_at;
	
}
