package com.junpak.cashbook.domain.journal;

import lombok.Getter;

@Getter
public enum Account {
	CASH("현금"),
	CARD_PAYABLE("카드미지급금"),
	PAYABLE("미지급금"),

	INCOME("수입"),
	OTHER_INCOME("잡수익"),
	EXPENSE("지출");

	private final String description;

	Account(String description) {
		this.description = description;
	}

}
