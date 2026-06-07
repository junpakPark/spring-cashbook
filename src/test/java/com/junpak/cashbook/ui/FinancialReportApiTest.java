package com.junpak.cashbook.ui;

import java.time.LocalDateTime;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import com.junpak.cashbook.domain.transaction.PaymentMethod;
import com.junpak.cashbook.domain.transaction.TransactionType;

@SuppressWarnings("NonAsciiCharacters")
class FinancialReportApiTest extends ApiTest {

	private static final LocalDateTime JUNE_1 = LocalDateTime.of(2026, 6, 1, 10, 0);
	private static final LocalDateTime JUNE_2 = LocalDateTime.of(2026, 6, 2, 10, 0);
	private static final LocalDateTime JUNE_3 = LocalDateTime.of(2026, 6, 3, 10, 0);
	private static final LocalDateTime JUNE_4 = LocalDateTime.of(2026, 6, 4, 10, 0);
	private static final LocalDateTime JUNE_5 = LocalDateTime.of(2026, 6, 5, 10, 0);

	@Test
	void 재무상태를_전체_누적으로_조회한다() {
		TransactionSteps.매매기록요청(TransactionSteps.매매기록요청_생성(TransactionType.INCOME, PaymentMethod.CASH, 10_000, JUNE_1));
		TransactionSteps.매매기록요청(TransactionSteps.매매기록요청_생성(TransactionType.EXPENSE, PaymentMethod.CASH, 2_000, JUNE_2));
		TransactionSteps.매매기록요청(TransactionSteps.매매기록요청_생성(TransactionType.EXPENSE, PaymentMethod.CARD, 3_000, JUNE_3));

		final var response = FinancialReportSteps.재무상태조회요청();

		SoftAssertions.assertSoftly(softly -> {
			softly.assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
			softly.assertThat(response.jsonPath().getInt("cash")).isEqualTo(8_000);
			softly.assertThat(response.jsonPath().getInt("cardPayable")).isEqualTo(3_000);
		});
	}

	@Test
	void 재무상태를_특정_시점까지의_누적으로_조회한다() {
		TransactionSteps.매매기록요청(TransactionSteps.매매기록요청_생성(TransactionType.INCOME, PaymentMethod.CASH, 10_000, JUNE_1));
		TransactionSteps.매매기록요청(TransactionSteps.매매기록요청_생성(TransactionType.EXPENSE, PaymentMethod.CARD, 3_000, JUNE_2));
		TransactionSteps.매매기록요청(TransactionSteps.매매기록요청_생성(TransactionType.CARD_BILL_PAYMENT, PaymentMethod.CASH, 1_000,
			JUNE_3));

		final var response = FinancialReportSteps.재무상태조회요청(JUNE_2.plusHours(1));

		SoftAssertions.assertSoftly(softly -> {
			softly.assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
			softly.assertThat(response.jsonPath().getInt("cash")).isEqualTo(10_000);
			softly.assertThat(response.jsonPath().getInt("cardPayable")).isEqualTo(3_000);
		});
	}

	@Test
	void 손익을_기간으로_조회한다() {
		TransactionSteps.매매기록요청(TransactionSteps.매매기록요청_생성(TransactionType.INCOME, PaymentMethod.CASH, 10_000, JUNE_1));
		TransactionSteps.매매기록요청(TransactionSteps.매매기록요청_생성(TransactionType.SECONDHAND_SALE, PaymentMethod.CASH, 2_000,
			JUNE_2));
		TransactionSteps.매매기록요청(TransactionSteps.매매기록요청_생성(TransactionType.EXPENSE, PaymentMethod.CARD, 4_000, JUNE_3));
		TransactionSteps.매매기록요청(TransactionSteps.매매기록요청_생성(TransactionType.EXPENSE, PaymentMethod.CASH, 1_000, JUNE_5));

		final var response = FinancialReportSteps.손익조회요청(JUNE_2, JUNE_3);

		SoftAssertions.assertSoftly(softly -> {
			softly.assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
			softly.assertThat(response.jsonPath().getInt("totalIncome")).isEqualTo(2_000);
			softly.assertThat(response.jsonPath().getInt("netProfit")).isEqualTo(-2_000);
		});
	}

	@Test
	void 시산표를_전체_누적으로_조회한다() {
		TransactionSteps.매매기록요청(TransactionSteps.매매기록요청_생성(TransactionType.INCOME, PaymentMethod.CASH, 10_000, JUNE_1));
		TransactionSteps.매매기록요청(TransactionSteps.매매기록요청_생성(TransactionType.EXPENSE, PaymentMethod.CASH, 3_000, JUNE_2));
		TransactionSteps.매매기록요청(TransactionSteps.매매기록요청_생성(TransactionType.EXPENSE, PaymentMethod.CARD, 4_000, JUNE_3));
		TransactionSteps.매매기록요청(TransactionSteps.매매기록요청_생성(TransactionType.CARD_BILL_PAYMENT, PaymentMethod.CASH, 1_000,
			JUNE_4));

		final var response = FinancialReportSteps.시산표조회요청();

		SoftAssertions.assertSoftly(softly -> {
			softly.assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
			softly.assertThat(response.jsonPath().getInt("totalDebit")).isEqualTo(18_000);
			softly.assertThat(response.jsonPath().getInt("totalCredit")).isEqualTo(18_000);
			softly.assertThat(response.jsonPath().getInt("accounts.find { it.account == 'CASH' }.netChange"))
				.isEqualTo(6_000);
		});
	}

	@Test
	void 손익을_시작일시부터_조회한다() {
		TransactionSteps.매매기록요청(TransactionSteps.매매기록요청_생성(TransactionType.INCOME, PaymentMethod.CASH, 10_000, JUNE_1));
		TransactionSteps.매매기록요청(TransactionSteps.매매기록요청_생성(TransactionType.SECONDHAND_SALE, PaymentMethod.CASH, 2_000,
			JUNE_2));
		TransactionSteps.매매기록요청(TransactionSteps.매매기록요청_생성(TransactionType.EXPENSE, PaymentMethod.CARD, 4_000, JUNE_3));
		TransactionSteps.매매기록요청(TransactionSteps.매매기록요청_생성(TransactionType.EXPENSE, PaymentMethod.CASH, 1_000, JUNE_5));

		final var response = FinancialReportSteps.손익조회요청(JUNE_2);

		SoftAssertions.assertSoftly(softly -> {
			softly.assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
			softly.assertThat(response.jsonPath().getInt("totalIncome")).isEqualTo(2_000);
			softly.assertThat(response.jsonPath().getInt("netProfit")).isEqualTo(-3_000);
		});
	}

}
