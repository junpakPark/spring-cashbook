package com.junpak.cashbook.domain.journal;

import static com.junpak.cashbook.domain.journal.Account.*;
import static org.assertj.core.api.Assertions.*;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
class JournalAccountsTest {

	@Test
	void 차변과_대변_계정을_생성한다() {
		final JournalAccounts journalAccounts = new JournalAccounts(CASH, INCOME);

		SoftAssertions.assertSoftly(softly -> {
			softly.assertThat(journalAccounts.getDebit()).isEqualTo(CASH);
			softly.assertThat(journalAccounts.getCredit()).isEqualTo(INCOME);
		});
	}

	@Test
	void 차변_계정은_필수이다() {
		assertThatThrownBy(() -> new JournalAccounts(null, INCOME))
			.isInstanceOf(NullPointerException.class)
			.hasMessage("차변 계정이 누락되었습니다.");
	}

	@Test
	void 대변_계정은_필수이다() {
		assertThatThrownBy(() -> new JournalAccounts(CASH, null))
			.isInstanceOf(NullPointerException.class)
			.hasMessage("대변 계정이 누락되었습니다");
	}

	@Test
	void 차변과_대변_계정은_같을_수_없다() {
		assertThatThrownBy(() -> new JournalAccounts(CASH, CASH))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage("차변과 대변 계정은 같을 수 없습니다.");
	}

	@Test
	void 차변과_대변을_반대로_뒤집는다() {
		final JournalAccounts journalAccounts = new JournalAccounts(EXPENSE, CASH);

		final JournalAccounts reversed = journalAccounts.reverse();

		SoftAssertions.assertSoftly(softly -> {
			softly.assertThat(reversed.getDebit()).isEqualTo(CASH);
			softly.assertThat(reversed.getCredit()).isEqualTo(EXPENSE);
		});
	}
}
