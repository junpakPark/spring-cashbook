package com.junpak.cashbook.domain.account;

import com.junpak.cashbook.domain.Money;

enum EntrySide {
	DEBIT,
	CREDIT;

	Money calculateNetChange(Money debit, Money credit) {
		return calculateDebitChange(debit).add(calculateCreditChange(credit));
	}

	Money calculateDebitChange(Money amount) {
		if (this == DEBIT) {
			return amount;
		}
		return amount.negate();
	}

	Money calculateCreditChange(Money amount) {
		if (this == CREDIT) {
			return amount;
		}
		return amount.negate();
	}
}
