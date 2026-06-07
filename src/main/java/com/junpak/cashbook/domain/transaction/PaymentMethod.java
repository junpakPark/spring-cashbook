package com.junpak.cashbook.domain.transaction;

import lombok.Getter;

@Getter
public enum PaymentMethod {
	CASH("현금"),
	CARD("카드"),
	CREDIT("외상"),
	;

	private final String description;

	PaymentMethod(String description) {
		this.description = description;
	}

	public boolean isAllowedFor(final TransactionType type) {
		if (this == CASH) {
			return true;
		}
		return type == TransactionType.EXPENSE;
	}
}
