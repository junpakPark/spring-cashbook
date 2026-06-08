package com.junpak.cashbook.domain;

import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import com.junpak.cashbook.domain.account.Account;
import com.junpak.cashbook.domain.journal.Journal;

public record FinancialPosition(
	Money cash,
	Money cardPayable,
	Money payable
) {

	public FinancialPosition {
		Objects.requireNonNull(cash, "현금 잔액은 필수입니다.");
		Objects.requireNonNull(cardPayable, "카드미지급금 잔액은 필수입니다.");
		Objects.requireNonNull(payable, "미지급금 잔액은 필수입니다.");
	}

	public static FinancialPosition from(List<Journal> journals) {
		return new FinancialPosition(
			Account.CASH.sumNetChangeAmount(journals),
			Account.CARD_PAYABLE.sumNetChangeAmount(journals),
			Account.PAYABLE.sumNetChangeAmount(journals)
		);
	}

	public void validateApplicable(FinancialPosition other) {
		boolean hasNegativeBalance = Stream.of(
			cash.add(other.cash),
			cardPayable.add(other.cardPayable),
			payable.add(other.payable)
		).anyMatch(Money::isNegative);

		if (hasNegativeBalance) {
			throw new IllegalStateException("재무상태 잔액은 음수가 될 수 없습니다.");
		}
	}

}
