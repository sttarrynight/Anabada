package model.dto;

import lombok.Getter;

@Getter
public enum TransactionType {

	CHARGE("CHARGE","충전"), 
	WITHDRAW("WITHDRAW","인출"), 
	PURCHASE("PURCHASE","구매"), 
	SALE("SALE","판매");

	private final String code;
    private final String description;
	
	TransactionType(String code, String description) {
		this.code = code;
		this.description = description;
	}
	
	public static TransactionType fromCode(String code) {
        for (TransactionType type : TransactionType.values()) {
            if (type.getCode().equals(code)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown transaction type: " + code);
    }
}
