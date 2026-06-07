package com.junpak.cashbook.domain.transaction;

import lombok.Getter;

@Getter
public enum TransactionType {
	INCOME("수입"),
	EXPENSE("지출"),
	SECONDHAND_SALE("중고 판매"),
	REFUND("환불"),
	;

	private final String description;

	TransactionType(String description) {
		this.description = description;
	}
}
