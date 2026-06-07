package com.junpak.cashbook.domain;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.junpak.cashbook.domain.account.Account;
import com.junpak.cashbook.domain.journal.Journal;
import com.junpak.cashbook.domain.journal.JournalAccounts;
import com.junpak.cashbook.domain.transaction.PaymentMethod;
import com.junpak.cashbook.domain.transaction.Transaction;
import com.junpak.cashbook.domain.transaction.TransactionType;

@SpringBootTest
@SuppressWarnings("NonAsciiCharacters")
class TransactionJournalizerTest {

	private static final String DETAIL = "거래 내용";
	private static final Price AMOUNT = Price.from(1_000);
	private static final Money JOURNAL_AMOUNT = Money.from(1_000);
	private static final LocalDateTime TRANSACTION_DATE = LocalDateTime.of(2026, 6, 7, 16, 30);
	private static final LocalDateTime CANCEL_DATE = LocalDateTime.of(2026, 6, 8, 11, 20);

	@Autowired
	private TransactionJournalizer sut;

	@ParameterizedTest
	@CsvSource({
		"CASH, INCOME, CASH, INCOME",
		"CASH, EXPENSE, EXPENSE, CASH",
		"CARD, EXPENSE, EXPENSE, CARD_PAYABLE",
		"CREDIT, EXPENSE, EXPENSE, PAYABLE",
		"CASH, SECONDHAND_SALE, CASH, OTHER_INCOME",
		"CASH, CARD_BILL_PAYMENT, CARD_PAYABLE, CASH",
		"CASH, PAYABLE_REPAYMENT, PAYABLE, CASH",
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
			softly.assertThat(journal.getAmount()).isEqualTo(JOURNAL_AMOUNT);
			softly.assertThat(journal.getJournalAccounts().getDebit()).isEqualTo(expectedDebit);
			softly.assertThat(journal.getJournalAccounts().getCredit()).isEqualTo(expectedCredit);
			softly.assertThat(journal.getTransactionDate()).isEqualTo(TRANSACTION_DATE);
		});
	}

	@Test
	void 취소_분개를_생성한다() {
		final Journal journal = new Journal(1L, new JournalAccounts(Account.EXPENSE, Account.CASH), JOURNAL_AMOUNT,
			TRANSACTION_DATE);

		final Journal canceledJournal = sut.cancel(List.of(journal), CANCEL_DATE);

		SoftAssertions.assertSoftly(softly -> {
			softly.assertThat(canceledJournal.getTransactionId()).isEqualTo(1L);
			softly.assertThat(canceledJournal.getAmount()).isEqualTo(JOURNAL_AMOUNT);
			softly.assertThat(canceledJournal.getTransactionDate()).isEqualTo(CANCEL_DATE);
			softly.assertThat(canceledJournal.getJournalAccounts().getDebit()).isEqualTo(Account.CASH);
			softly.assertThat(canceledJournal.getJournalAccounts().getCredit()).isEqualTo(Account.EXPENSE);
		});
	}

	@Test
	void 분개_목록은_필수이다() {
		assertThatThrownBy(() -> sut.cancel(null, CANCEL_DATE))
			.isInstanceOf(NullPointerException.class)
			.hasMessage("분개 목록은 필수입니다.");
	}

	@Test
	void 취소_일시는_필수이다() {
		final Journal journal = new Journal(1L, new JournalAccounts(Account.EXPENSE, Account.CASH), JOURNAL_AMOUNT,
			TRANSACTION_DATE);

		assertThatThrownBy(() -> sut.cancel(List.of(journal), null))
			.isInstanceOf(NullPointerException.class)
			.hasMessage("취소일시는 필수입니다.");
	}

	@Test
	void 취소할_분개가_없으면_취소_분개를_생성할_수_없다() {
		assertThatThrownBy(() -> sut.cancel(List.of(), CANCEL_DATE))
			.isInstanceOf(IllegalStateException.class)
			.hasMessage("해당하는 분개가 없습니다.");
	}

	@Test
	void 이미_취소_분개가_생성된_거래는_취소할_수_없다() {
		final Journal journal = new Journal(1L, new JournalAccounts(Account.EXPENSE, Account.CASH), JOURNAL_AMOUNT,
			TRANSACTION_DATE);
		final Journal canceledJournal = journal.cancel(CANCEL_DATE);

		assertThatThrownBy(() -> sut.cancel(List.of(journal, canceledJournal), CANCEL_DATE))
			.isInstanceOf(IllegalStateException.class)
			.hasMessage("이미 취소 분개가 생성된 거래입니다.");
	}
}
