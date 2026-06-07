package com.junpak.cashbook.domain.transaction;

import lombok.Getter;

@Getter
public enum TransactionType {
	INCOME("수입"),
	EXPENSE("지출"),
	SECONDHAND_SALE("중고 판매"),
	CARD_BILL_PAYMENT("카드 대금 납부"),
	PAYABLE_REPAYMENT("외상 상환"),
	;

	private final String description;

	TransactionType(String description) {
		this.description = description;
	}
}
