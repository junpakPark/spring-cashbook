package com.junpak.cashbook.domain;

import static com.junpak.cashbook.domain.transaction.TransactionType.*;
import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.junpak.cashbook.domain.journal.Account;
import com.junpak.cashbook.domain.journal.Journal;
import com.junpak.cashbook.domain.transaction.PaymentMethod;
import com.junpak.cashbook.domain.transaction.Transaction;
import com.junpak.cashbook.domain.transaction.TransactionType;

@SpringBootTest
@SuppressWarnings("NonAsciiCharacters")
class TransactionJournalizerTest {

	private static final String DETAIL = "거래 내용";
	private static final Money AMOUNT = Money.from(1_000);
	private static final LocalDateTime TRANSACTION_DATE = LocalDateTime.of(2026, 6, 7, 16, 30);

	@Autowired
	private TransactionJournalizer sut;

	@ParameterizedTest
	@CsvSource({
		"CASH, INCOME, CASH, INCOME",
		"CASH, EXPENSE, EXPENSE, CASH",
		"CARD, EXPENSE, EXPENSE, CARD_PAYABLE",
		"CREDIT, EXPENSE, EXPENSE, PAYABLE",
		"CASH, SECONDHAND_SALE, CASH, OTHER_INCOME",
	})
	void 거래를_분개한다(
		final PaymentMethod method,
		final TransactionType type,
		final Account expectedDebit,
		final Account expectedCredit
	) {
		final Transaction transaction = new Transaction(DETAIL, type, method, AMOUNT, TRANSACTION_DATE);
		final Journal journal = sut.journalize(transaction);

		SoftAssertions.assertSoftly(softly -> {
			softly.assertThat(journal.getAmount()).isEqualTo(AMOUNT);
			softly.assertThat(journal.getJournalAccounts().getDebit()).isEqualTo(expectedDebit);
			softly.assertThat(journal.getJournalAccounts().getCredit()).isEqualTo(expectedCredit);
		});
	}

}
