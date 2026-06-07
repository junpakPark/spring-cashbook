package com.junpak.cashbook.application.dto.response;

import com.junpak.cashbook.domain.Money;

public record ProfitAndLossResponse(
	int income,
	int otherIncome,
	int totalIncome,
	int expense,
	int netProfit
) {

	public static ProfitAndLossResponse of(Money income, Money otherIncome, Money expense) {
		final Money totalIncome = income.add(otherIncome);
		final Money netProfit = totalIncome.subtract(expense);

		return new ProfitAndLossResponse(
			income.getAmount().intValue(),
			otherIncome.getAmount().intValue(),
			totalIncome.getAmount().intValue(),
			expense.getAmount().intValue(),
			netProfit.getAmount().intValue()
		);
	}
}
