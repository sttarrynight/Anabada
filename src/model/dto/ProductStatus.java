package model.dto;

import lombok.Getter;

@Getter
public enum ProductStatus {

	SALE("SALE", "판매중"), 
	SOLD("SOLD", "판매완료");
	
	 private final String code;      // DB에 저장되는 값
	 private final String description;  // 화면에 표시되는 값

	ProductStatus(String code, String description) {
		this.code = code;
        this.description = description;
	}
	
	// DB 값을 Enum으로 변환
    public static ProductStatus fromCode(String code) {
        for (ProductStatus status : ProductStatus.values()) {
            if (status.getCode().equals(code)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown status code: " + code);
    }
}
