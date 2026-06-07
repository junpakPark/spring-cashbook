package com.junpak.cashbook.domain.transaction;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import com.junpak.cashbook.domain.Money;

@SuppressWarnings("NonAsciiCharacters")
class TransactionTest {

	private static final String DETAIL = "거래 내용";
	private static final Money AMOUNT = Money.from(1_000);
	private static final LocalDateTime TRANSACTION_DATE = LocalDateTime.of(2026, 6, 7, 16, 30);

	@Test
	void 거래_유형은_필수이다() {
		assertThatThrownBy(() -> new Transaction(DETAIL, null, PaymentMethod.CASH, AMOUNT, TRANSACTION_DATE))
			.isInstanceOf(NullPointerException.class)
			.hasMessage("거래 유형은 필수입니다.");
	}

	@Test
	void 결제_수단은_필수이다() {
		assertThatThrownBy(() -> new Transaction(DETAIL, TransactionType.EXPENSE, null, AMOUNT, TRANSACTION_DATE))
			.isInstanceOf(NullPointerException.class)
			.hasMessage("결제 수단은 필수입니다.");
	}

	@Test
	void 거래_내용은_필수이다() {
		assertThatThrownBy(() -> new Transaction(null, TransactionType.EXPENSE, PaymentMethod.CASH, AMOUNT,
			TRANSACTION_DATE))
			.isInstanceOf(NullPointerException.class)
			.hasMessage("거래 내용은 필수입니다.");
	}

	@Test
	void 거래_금액은_필수이다() {
		assertThatThrownBy(() -> new Transaction(DETAIL, TransactionType.EXPENSE, PaymentMethod.CASH, null,
			TRANSACTION_DATE))
			.isInstanceOf(NullPointerException.class)
			.hasMessage("거래 금액은 필수입니다.");
	}

	@Test
	void 거래_일시는_필수이다() {
		assertThatThrownBy(() -> new Transaction(DETAIL, TransactionType.EXPENSE, PaymentMethod.CASH, AMOUNT, null))
			.isInstanceOf(NullPointerException.class)
			.hasMessage("거래일시는 필수입니다.");
	}

	@ParameterizedTest
	@CsvSource({
		"CASH, INCOME",
		"CASH, EXPENSE",
		"CASH, SECONDHAND_SALE",
		"CARD, EXPENSE",
		"CREDIT, EXPENSE",
	})
	void 허용된_결제수단이면_거래를_생성한다(
		final PaymentMethod method,
		final TransactionType type
	) {
		final Transaction transaction = new Transaction(DETAIL, type, method, AMOUNT, TRANSACTION_DATE);

		SoftAssertions.assertSoftly(softly -> {
			assertThat(transaction.getDetail()).isEqualTo(DETAIL);
			assertThat(transaction.getTransactionType()).isEqualTo(type);
			assertThat(transaction.getPaymentMethod()).isEqualTo(method);
			assertThat(transaction.getAmount()).isEqualTo(AMOUNT);
			assertThat(transaction.getTransactionDate()).isEqualTo(TRANSACTION_DATE);
		});

	}

	@ParameterizedTest
	@CsvSource({
		"CARD, INCOME",
		"CARD, SECONDHAND_SALE",
		"CREDIT, INCOME",
		"CREDIT, SECONDHAND_SALE",
	})
	void 허용되지_않는_결제수단이면_거래를_생성할_수_없다(
		final PaymentMethod method,
		final TransactionType type
	) {
		assertThatThrownBy(() -> new Transaction(DETAIL, type, method, AMOUNT, TRANSACTION_DATE))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage("해당 거래 유형에서 사용할 수 없는 결제수단입니다.");
	}

}
