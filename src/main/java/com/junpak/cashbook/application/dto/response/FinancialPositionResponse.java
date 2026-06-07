package com.junpak.cashbook.application.dto.response;

import com.junpak.cashbook.domain.Money;

public record FinancialPositionResponse(
	int cash,
	int cardPayable,
	int payable
) {

	public static FinancialPositionResponse of(Money cash, Money cardPayable, Money payable) {
		return new FinancialPositionResponse(
			cash.getAmount().intValue(),
			cardPayable.getAmount().intValue(),
			payable.getAmount().intValue()
		);
	}
}
