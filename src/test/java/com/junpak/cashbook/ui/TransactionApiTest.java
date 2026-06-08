package com.junpak.cashbook.ui;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import com.junpak.cashbook.domain.transaction.PaymentMethod;
import com.junpak.cashbook.domain.transaction.TransactionType;

@SuppressWarnings("NonAsciiCharacters")
class TransactionApiTest extends ApiTest {

	private static final LocalDateTime JUNE_1 = LocalDateTime.of(2026, 6, 1, 10, 0);
	private static final LocalDateTime JUNE_2 = LocalDateTime.of(2026, 6, 2, 10, 0);

	@Test
	void 매매_기록() {
		final var request = TransactionSteps.매매기록요청_생성();
		final var response = TransactionSteps.매매기록요청(request);

		SoftAssertions.assertSoftly(softly -> {
			assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
			assertThat(response.header("Location")).matches("/transactions/\\d+");
		});
	}

	@Test
	void 잔액이_음수가_되는_매매는_기록할_수_없다() {
		TransactionSteps.매매기록요청(
			TransactionSteps.매매기록요청_생성(TransactionType.INCOME, PaymentMethod.CASH, 1_000, JUNE_1)
		);

		final var response = TransactionSteps.매매기록요청(
			TransactionSteps.매매기록요청_생성(TransactionType.EXPENSE, PaymentMethod.CASH, 1_001, JUNE_2)
		);
		final var financialPosition = FinancialReportSteps.재무상태조회요청();

		SoftAssertions.assertSoftly(softly -> {
			softly.assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
			softly.assertThat(response.jsonPath().getString("message"))
				.isEqualTo("재무상태 잔액은 음수가 될 수 없습니다.");
			softly.assertThat(financialPosition.jsonPath().getInt("cash")).isEqualTo(1_000);
		});
	}

	@Test
	void 매매_취소() {
		TransactionSteps.매매기록요청(TransactionSteps.매매기록요청_생성()).header("Location");
		final Long transactionId = 1L;

		final var request = TransactionSteps.매매취소요청_생성();
		final var response = TransactionSteps.매매취소요청(request, transactionId);

		assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
	}

}
