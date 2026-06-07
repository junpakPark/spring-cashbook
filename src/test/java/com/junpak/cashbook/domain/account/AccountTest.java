package com.junpak.cashbook.domain.account;

import static com.junpak.cashbook.domain.account.Account.*;
import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;

import com.junpak.cashbook.domain.Money;
import com.junpak.cashbook.domain.journal.Journal;
import com.junpak.cashbook.domain.journal.JournalAccounts;

@SuppressWarnings("NonAsciiCharacters")
class AccountTest {

	private static final LocalDateTime TRANSACTION_DATE = LocalDateTime.of(2026, 6, 7, 16, 30);

	@Test
	void 차변_정상_계정은_차변에서_증가하고_대변에서_감소한다() {
		SoftAssertions.assertSoftly(softly -> {
			softly.assertThat(CASH.sumNetChangeAmount(List.of(journal(CASH, INCOME, 1_000))))
				.isEqualTo(Money.from(1_000));
			softly.assertThat(CASH.sumNetChangeAmount(List.of(journal(EXPENSE, CASH, 1_000))))
				.isEqualTo(Money.from(-1_000));
		});
	}

	@Test
	void 대변_정상_계정은_대변에서_증가하고_차변에서_감소한다() {
		SoftAssertions.assertSoftly(softly -> {
			softly.assertThat(CARD_PAYABLE.sumNetChangeAmount(List.of(journal(EXPENSE, CARD_PAYABLE, 1_000))))
				.isEqualTo(Money.from(1_000));
			softly.assertThat(CARD_PAYABLE.sumNetChangeAmount(List.of(journal(CARD_PAYABLE, CASH, 1_000))))
				.isEqualTo(Money.from(-1_000));
		});
	}

	@Test
	void 계정이_포함되지_않은_분개는_순변동이_없다() {
		final Money netChange = PAYABLE.sumNetChangeAmount(List.of(journal(EXPENSE, CASH, 1_000)));

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
	void 계정의_순변동_합계를_계산한다() {
		final List<Journal> journals = List.of(
			journal(CASH, INCOME, 1_000),
			journal(EXPENSE, CASH, 500),
			journal(CARD_PAYABLE, CASH, 300),
			journal(EXPENSE, PAYABLE, 200)
		);

		SoftAssertions.assertSoftly(softly -> {
			softly.assertThat(CASH.sumNetChangeAmount(journals)).isEqualTo(Money.from(200));
			softly.assertThat(INCOME.sumNetChangeAmount(journals)).isEqualTo(Money.from(1_000));
			softly.assertThat(PAYABLE.sumNetChangeAmount(journals)).isEqualTo(Money.from(200));
		});
	}

	@Test
	void 계정의_차변_합계를_계산한다() {
		final List<Journal> journals = List.of(
			journal(CASH, INCOME, 1_000),
			journal(EXPENSE, CASH, 500),
			journal(CASH, OTHER_INCOME, 700)
		);

		final Money debitAmount = CASH.sumDebitAmount(journals);

		assertThat(debitAmount).isEqualTo(Money.from(1_700));
	}

	@Test
	void 계정의_대변_합계를_계산한다() {
		final List<Journal> journals = List.of(
			journal(CASH, INCOME, 1_000),
			journal(EXPENSE, CASH, 500),
			journal(CARD_PAYABLE, CASH, 300)
		);

		final Money creditAmount = CASH.sumCreditAmount(journals);

		assertThat(creditAmount).isEqualTo(Money.from(800));
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

	@Test
	void 순변동_합계_계산시_분개_목록은_필수이다() {
		assertThatThrownBy(() -> CASH.sumNetChangeAmount(null))
			.isInstanceOf(NullPointerException.class)
			.hasMessage("분개 목록은 필수입니다.");
	}

	@Test
	void 차변_합계_계산시_분개_목록은_필수이다() {
		assertThatThrownBy(() -> CASH.sumDebitAmount(null))
			.isInstanceOf(NullPointerException.class)
			.hasMessage("분개 목록은 필수입니다.");
	}

	@Test
	void 대변_합계_계산시_분개_목록은_필수이다() {
		assertThatThrownBy(() -> CASH.sumCreditAmount(null))
			.isInstanceOf(NullPointerException.class)
			.hasMessage("분개 목록은 필수입니다.");
	}

	private Journal journal(Account debit, Account credit, int amount) {
		return new Journal(1L, new JournalAccounts(debit, credit), Money.from(amount), TRANSACTION_DATE);
	}

	@Test
	void 계정은_설명을_가진다() {
		SoftAssertions.assertSoftly(softly -> {
			softly.assertThat(CASH.getDescription()).isEqualTo("현금");
			softly.assertThat(CARD_PAYABLE.getDescription()).isEqualTo("카드미지급금");
			softly.assertThat(PAYABLE.getDescription()).isEqualTo("미지급금");
			softly.assertThat(INCOME.getDescription()).isEqualTo("수입");
			softly.assertThat(OTHER_INCOME.getDescription()).isEqualTo("잡수익");
			softly.assertThat(EXPENSE.getDescription()).isEqualTo("지출");
		});
	}

}
