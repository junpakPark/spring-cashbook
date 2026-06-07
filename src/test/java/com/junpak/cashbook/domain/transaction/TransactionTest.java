package com.junpak.cashbook.domain.transaction;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import com.junpak.cashbook.domain.Price;

@SuppressWarnings("NonAsciiCharacters")
class TransactionTest {

	private static final String DETAIL = "거래 내용";
	private static final Price AMOUNT = Price.from(1_000);
	private static final LocalDateTime TRANSACTION_DATE = LocalDateTime.of(2026, 6, 7, 16, 30);
	private static final LocalDateTime CANCEL_DATE = LocalDateTime.of(2026, 6, 8, 11, 20);

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
		"CASH, CARD_BILL_PAYMENT",
		"CASH, PAYABLE_REPAYMENT",
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
			assertThat(transaction.getStatus()).isEqualTo(TransactionStatus.ACTIVE);
			assertThat(transaction.getCancelDate()).isNull();
		});

	}

	@ParameterizedTest
	@CsvSource({
		"CARD, INCOME",
		"CARD, SECONDHAND_SALE",
		"CARD, CARD_BILL_PAYMENT",
		"CARD, PAYABLE_REPAYMENT",
		"CREDIT, INCOME",
		"CREDIT, SECONDHAND_SALE",
		"CREDIT, CARD_BILL_PAYMENT",
		"CREDIT, PAYABLE_REPAYMENT",
	})
	void 허용되지_않는_결제수단이면_거래를_생성할_수_없다(
		final PaymentMethod method,
		final TransactionType type
	) {
		assertThatThrownBy(() -> new Transaction(DETAIL, type, method, AMOUNT, TRANSACTION_DATE))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage("해당 거래 유형에서 사용할 수 없는 결제수단입니다.");
	}

	@Test
	void 거래를_취소한다() {
		final Transaction transaction = new Transaction(DETAIL, TransactionType.EXPENSE, PaymentMethod.CASH, AMOUNT,
			TRANSACTION_DATE);

		transaction.cancel(CANCEL_DATE);

		SoftAssertions.assertSoftly(softly -> {
			softly.assertThat(transaction.getStatus()).isEqualTo(TransactionStatus.CANCELED);
			softly.assertThat(transaction.getCancelDate()).isEqualTo(CANCEL_DATE);
		});
	}

	@Test
	void 취소_일시는_필수이다() {
		final Transaction transaction = new Transaction(DETAIL, TransactionType.EXPENSE, PaymentMethod.CASH, AMOUNT,
			TRANSACTION_DATE);

		assertThatThrownBy(() -> transaction.cancel(null))
			.isInstanceOf(NullPointerException.class)
			.hasMessage("취소일시는 필수입니다.");
		assertThat(transaction.getStatus()).isEqualTo(TransactionStatus.ACTIVE);
	}

	@Test
	void 취소된_거래는_다시_취소할_수_없다() {
		final Transaction transaction = new Transaction(DETAIL, TransactionType.EXPENSE, PaymentMethod.CASH, AMOUNT,
			TRANSACTION_DATE);
		transaction.cancel(CANCEL_DATE);

		assertThatThrownBy(() -> transaction.cancel(CANCEL_DATE.plusDays(1)))
			.isInstanceOf(IllegalStateException.class)
			.hasMessage("취소된 매매는 다시 취소할 수 없습니다.");
	}

}
