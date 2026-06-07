package com.junpak.cashbook.domain.account;

import static com.junpak.cashbook.domain.account.AccountType.*;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;

import com.junpak.cashbook.domain.Money;

@SuppressWarnings("NonAsciiCharacters")
class AccountTypeTest {

	@Test
	void 차변에서_증가하는_계정_유형은_차변에서_대변을_뺀다() {
		final Money debit = Money.from(2_000);
		final Money credit = Money.from(500);

		SoftAssertions.assertSoftly(softly -> {
			softly.assertThat(ASSET.calculateNetChange(debit, credit)).isEqualTo(Money.from(1_500));
			softly.assertThat(EXPENSE.calculateNetChange(debit, credit)).isEqualTo(Money.from(1_500));
		});
	}

	@Test
	void 대변에서_증가하는_계정_유형은_대변에서_차변을_뺀다() {
		final Money debit = Money.from(2_000);
		final Money credit = Money.from(500);

		SoftAssertions.assertSoftly(softly -> {
			softly.assertThat(LIABILITY.calculateNetChange(debit, credit)).isEqualTo(Money.from(-1_500));
			softly.assertThat(EQUITY.calculateNetChange(debit, credit)).isEqualTo(Money.from(-1_500));
			softly.assertThat(REVENUE.calculateNetChange(debit, credit)).isEqualTo(Money.from(-1_500));
		});
	}
}
