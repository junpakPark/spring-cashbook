package com.junpak.cashbook.ui;

import static org.assertj.core.api.Assertions.*;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

@SuppressWarnings("NonAsciiCharacters")
class TransactionApiTest extends ApiTest {

	@Test
	void 매매_기록() {
		final var request = TransactionSteps.매매기록요청_생성();
		final var response = TransactionSteps.매매기록요청(request);

		SoftAssertions.assertSoftly(softly -> {
			assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
			assertThat(response.header("Location")).matches("/transactions/\\d+");
		});
	}

}
