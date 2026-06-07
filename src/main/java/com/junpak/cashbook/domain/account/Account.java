package com.junpak.cashbook.domain.account;

import java.util.List;
import java.util.Objects;

import com.junpak.cashbook.domain.Money;
import com.junpak.cashbook.domain.journal.Journal;
import com.junpak.cashbook.domain.journal.JournalAccounts;

public enum Account {
	CASH("현금", AccountType.ASSET),
	CARD_PAYABLE("카드미지급금", AccountType.LIABILITY),
	PAYABLE("미지급금", AccountType.LIABILITY),

	INCOME("수입", AccountType.REVENUE),
	OTHER_INCOME("잡수익", AccountType.REVENUE),
	EXPENSE("지출", AccountType.EXPENSE);

	private final String description;
	private final AccountType accountType;

	Account(String description, AccountType accountType) {
		this.description = description;
		this.accountType = accountType;
	}

	public Money calculateNetChange(Money debit, Money credit) {
		Objects.requireNonNull(debit, "차변 합계는 필수입니다.");
		Objects.requireNonNull(credit, "대변 합계는 필수입니다.");

		return accountType.calculateNetChange(debit, credit);
	}

	public Money sumNetChangeAmount(List<Journal> journals) {
		Objects.requireNonNull(journals, "분개 목록은 필수입니다.");

		return journals.stream()
			.map(journal -> calculateNetChange(journal.getJournalAccounts(), journal.getAmount()))
			.reduce(Money.ZERO, Money::add);
	}

	public Money sumDebitAmount(List<Journal> journals) {
		Objects.requireNonNull(journals, "분개 목록은 필수입니다.");

		return journals.stream()
			.filter(journal -> this == journal.getJournalAccounts().getDebit())
			.map(Journal::getAmount)
			.reduce(Money.ZERO, Money::add);
	}

	public Money sumCreditAmount(List<Journal> journals) {
		Objects.requireNonNull(journals, "분개 목록은 필수입니다.");

		return journals.stream()
			.filter(journal -> this == journal.getJournalAccounts().getCredit())
			.map(Journal::getAmount)
			.reduce(Money.ZERO, Money::add);
	}

	private Money calculateNetChange(JournalAccounts accounts, Money amount) {
		Objects.requireNonNull(accounts, "분개 계정은 필수입니다.");
		Objects.requireNonNull(amount, "금액은 필수입니다.");

		if (this == accounts.getDebit()) {
			return accountType.calculateDebitChange(amount);
		}
		if (this == accounts.getCredit()) {
			return accountType.calculateCreditChange(amount);
		}
		return Money.ZERO;
	}

	public String getDescription() {
		return description;
	}

}
