package com.junpak.cashbook.domain.account;

import static com.junpak.cashbook.domain.account.Account.*;
import static org.assertj.core.api.Assertions.*;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;

import com.junpak.cashbook.domain.Money;
import com.junpak.cashbook.domain.journal.JournalAccounts;

@SuppressWarnings("NonAsciiCharacters")
class AccountTest {

	@Test
	void 계정은_회계_유형을_가진다() {
		SoftAssertions.assertSoftly(softly -> {
			softly.assertThat(CASH.getAccountType()).isEqualTo(AccountType.ASSET);
			softly.assertThat(CARD_PAYABLE.getAccountType()).isEqualTo(AccountType.LIABILITY);
			softly.assertThat(PAYABLE.getAccountType()).isEqualTo(AccountType.LIABILITY);
			softly.assertThat(INCOME.getAccountType()).isEqualTo(AccountType.REVENUE);
			softly.assertThat(OTHER_INCOME.getAccountType()).isEqualTo(AccountType.REVENUE);
			softly.assertThat(EXPENSE.getAccountType()).isEqualTo(AccountType.EXPENSE);
		});
	}

	@Test
	void 차변_정상_계정은_차변에서_증가하고_대변에서_감소한다() {
		final Money amount = Money.from(1_000);

		SoftAssertions.assertSoftly(softly -> {
			softly.assertThat(CASH.calculateNetChange(new JournalAccounts(CASH, INCOME), amount))
				.isEqualTo(Money.from(1_000));
			softly.assertThat(CASH.calculateNetChange(new JournalAccounts(EXPENSE, CASH), amount))
				.isEqualTo(Money.from(-1_000));
		});
	}

	@Test
	void 대변_정상_계정은_대변에서_증가하고_차변에서_감소한다() {
		final Money amount = Money.from(1_000);

		SoftAssertions.assertSoftly(softly -> {
			softly.assertThat(CARD_PAYABLE.calculateNetChange(new JournalAccounts(EXPENSE, CARD_PAYABLE), amount))
				.isEqualTo(Money.from(1_000));
			softly.assertThat(CARD_PAYABLE.calculateNetChange(new JournalAccounts(CARD_PAYABLE, CASH), amount))
				.isEqualTo(Money.from(-1_000));
		});
	}

	@Test
	void 계정이_포함되지_않은_분개는_순변동이_없다() {
		final Money amount = Money.from(1_000);

		final Money netChange = PAYABLE.calculateNetChange(new JournalAccounts(EXPENSE, CASH), amount);

		assertThat(netChange).isEqualTo(Money.ZERO);
	}

	@Test
	void 차변과_대변_합계로_계정의_순변동을_계산한다() {
		final Money debit = Money.from(2_000);
		final Money credit = Money.from(500);

		SoftAssertions.assertSoftly(softly -> {
			softly.assertThat(EXPENSE.calculateNetChange(debit, credit)).isEqualTo(Money.from(1_500));
			softly.assertThat(INCOME.calculateNetChange(debit, credit)).isEqualTo(Money.from(-1_500));
		});
	}

	@Test
	void 분개_계정은_필수이다() {
		final Money amount = Money.from(1_000);

		assertThatThrownBy(() -> CASH.calculateNetChange((JournalAccounts)null, amount))
			.isInstanceOf(NullPointerException.class)
			.hasMessage("분개 계정은 필수입니다.");
	}

	@Test
	void 분개_금액은_필수이다() {
		final JournalAccounts accounts = new JournalAccounts(CASH, INCOME);

		assertThatThrownBy(() -> CASH.calculateNetChange(accounts, null))
			.isInstanceOf(NullPointerException.class)
			.hasMessage("금액은 필수입니다.");
	}

	@Test
	void 차변_합계는_필수이다() {
		final Money credit = Money.from(1_000);

		assertThatThrownBy(() -> CASH.calculateNetChange((Money)null, credit))
			.isInstanceOf(NullPointerException.class)
			.hasMessage("차변 합계는 필수입니다.");
	}

	@Test
	void 대변_합계는_필수이다() {
		final Money debit = Money.from(1_000);

		assertThatThrownBy(() -> CASH.calculateNetChange(debit, null))
			.isInstanceOf(NullPointerException.class)
			.hasMessage("대변 합계는 필수입니다.");
	}
}
