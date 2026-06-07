package com.junpak.cashbook.application.dto.response;

import java.util.List;

public record TrialBalanceResponse(
	List<TrialBalanceAccountResponse> accounts,
	int totalDebit,
	int totalCredit
) {

	public static TrialBalanceResponse from(List<TrialBalanceAccountResponse> accounts) {
		return new TrialBalanceResponse(
			accounts,
			accounts.stream().mapToInt(TrialBalanceAccountResponse::debit).sum(),
			accounts.stream().mapToInt(TrialBalanceAccountResponse::credit).sum()
		);
	}
}
