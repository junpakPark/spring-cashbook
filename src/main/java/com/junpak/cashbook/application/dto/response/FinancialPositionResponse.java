package com.junpak.cashbook.application.dto.response;

import com.junpak.cashbook.domain.FinancialPosition;

public record FinancialPositionResponse(
	int cash,
	int cardPayable,
	int payable
) {

	public static FinancialPositionResponse from(FinancialPosition financialPosition) {
		return new FinancialPositionResponse(
			financialPosition.cash().getAmount().intValue(),
			financialPosition.cardPayable().getAmount().intValue(),
			financialPosition.payable().getAmount().intValue()
		);
	}
}
