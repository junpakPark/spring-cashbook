package com.junpak.cashbook.domain;

import static com.junpak.cashbook.domain.account.Account.*;
import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.junpak.cashbook.domain.account.Account;
import com.junpak.cashbook.domain.journal.Journal;
import com.junpak.cashbook.domain.journal.JournalAccounts;

@SuppressWarnings("NonAsciiCharacters")
class FinancialPositionTest {

	private static final LocalDateTime TRANSACTION_DATE = LocalDateTime.of(2026, 6, 1, 10, 0);

	@Test
	void 현금_잔액이_0이_되는_분개는_적용할_수_있다() {
		final FinancialPosition financialPosition = new FinancialPosition(Money.from(1_000), Money.ZERO, Money.ZERO);
		final Journal journal = journal(EXPENSE, CASH, 1_000);

		assertThatCode(() -> financialPosition.validateApplicable(FinancialPosition.from(List.of(journal))))
			.doesNotThrowAnyException();
	}

	@Test
	void 현금_잔액이_음수가_되는_분개는_적용할_수_없다() {
		final FinancialPosition financialPosition = new FinancialPosition(Money.from(1_000), Money.ZERO, Money.ZERO);
		final Journal journal = journal(EXPENSE, CASH, 1_001);

		assertThatThrownBy(() -> financialPosition.validateApplicable(FinancialPosition.from(List.of(journal))))
			.isInstanceOf(IllegalStateException.class)
			.hasMessage("재무상태 잔액은 음수가 될 수 없습니다.");
	}

	@Test
	void 카드미지급금_잔액이_음수가_되는_분개는_적용할_수_없다() {
		final FinancialPosition financialPosition = new FinancialPosition(Money.from(10_000), Money.from(3_000),
			Money.ZERO);
		final Journal journal = journal(CARD_PAYABLE, CASH, 3_001);

		assertThatThrownBy(() -> financialPosition.validateApplicable(FinancialPosition.from(List.of(journal))))
			.isInstanceOf(IllegalStateException.class)
			.hasMessage("재무상태 잔액은 음수가 될 수 없습니다.");
	}

	@Test
	void 미지급금_잔액이_음수가_되는_분개는_적용할_수_없다() {
		final FinancialPosition financialPosition = new FinancialPosition(Money.from(10_000), Money.ZERO,
			Money.from(3_000));
		final Journal journal = journal(PAYABLE, CASH, 3_001);

		assertThatThrownBy(() -> financialPosition.validateApplicable(FinancialPosition.from(List.of(journal))))
			.isInstanceOf(IllegalStateException.class)
			.hasMessage("재무상태 잔액은 음수가 될 수 없습니다.");
	}

	private Journal journal(Account debit, Account credit, int amount) {
		return new Journal(1L, new JournalAccounts(debit, credit), Money.from(amount), TRANSACTION_DATE);
	}
}
