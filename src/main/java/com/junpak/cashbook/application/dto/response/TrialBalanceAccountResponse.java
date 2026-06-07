package com.junpak.cashbook.application.dto.response;

import com.junpak.cashbook.domain.Money;
import com.junpak.cashbook.domain.account.Account;

public record TrialBalanceAccountResponse(
	String account,
	String description,
	int debit,
	int credit,
	int netChange
) {

	public static TrialBalanceAccountResponse of(Account account, Money debit, Money credit) {
		Money netChange = account.calculateNetChange(debit, credit);
		return new TrialBalanceAccountResponse(
			account.name(),
			account.getDescription(),
			debit.getAmount().intValue(),
			credit.getAmount().intValue(),
			netChange.getAmount().intValue()
		);
	}

}
