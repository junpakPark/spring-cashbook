package com.junpak.cashbook.domain.journal;

import static com.junpak.cashbook.domain.journal.Account.*;
import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;

import com.junpak.cashbook.domain.Money;

@SuppressWarnings("NonAsciiCharacters")
class JournalTest {

	private static final Long TRANSACTION_ID = 1L;
	private static final Money AMOUNT = Money.from(1_000);
	private static final LocalDateTime TRANSACTION_DATE = LocalDateTime.of(2026, 6, 7, 16, 30);

	@Test
	void 분개를_생성한다() {
		final JournalAccounts journalAccounts = new JournalAccounts(EXPENSE, CASH);

		final Journal journal = new Journal(TRANSACTION_ID, journalAccounts, AMOUNT, TRANSACTION_DATE);

		SoftAssertions.assertSoftly(softly -> {
			softly.assertThat(journal.getTransactionId()).isEqualTo(TRANSACTION_ID);
			softly.assertThat(journal.getJournalAccounts()).isEqualTo(journalAccounts);
			softly.assertThat(journal.getAmount()).isEqualTo(AMOUNT);
			softly.assertThat(journal.getTransactionDate()).isEqualTo(TRANSACTION_DATE);
		});
	}

	@Test
	void 분개_일시는_필수이다() {
		final JournalAccounts journalAccounts = new JournalAccounts(EXPENSE, CASH);

		assertThatThrownBy(() -> new Journal(TRANSACTION_ID, journalAccounts, AMOUNT, null))
			.isInstanceOf(NullPointerException.class)
			.hasMessage("분개일시는 필수입니다.");
	}
}
