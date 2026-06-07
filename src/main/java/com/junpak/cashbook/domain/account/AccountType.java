package com.junpak.cashbook.domain.account;

import com.junpak.cashbook.domain.Money;

public enum AccountType {
	ASSET(EntrySide.DEBIT),
	LIABILITY(EntrySide.CREDIT),
	EQUITY(EntrySide.CREDIT),
	REVENUE(EntrySide.CREDIT),
	EXPENSE(EntrySide.DEBIT);

	private final EntrySide increaseSide;

	AccountType(EntrySide increaseSide) {
		this.increaseSide = increaseSide;
	}

	Money calculateNetChange(Money debit, Money credit) {
		return increaseSide.calculateNetChange(debit, credit);
	}

	Money calculateDebitChange(Money amount) {
		return increaseSide.calculateDebitChange(amount);
	}

	Money calculateCreditChange(Money amount) {
		return increaseSide.calculateCreditChange(amount);
	}
}
