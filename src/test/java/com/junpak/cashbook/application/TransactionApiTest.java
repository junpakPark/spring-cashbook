package com.junpak.cashbook.application;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import com.junpak.cashbook.ApiTest;
import com.junpak.cashbook.application.dto.request.RecordTransactionRequest;
import com.junpak.cashbook.domain.transaction.PaymentMethod;
import com.junpak.cashbook.domain.transaction.TransactionType;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

@SuppressWarnings("NonAsciiCharacters")
class TransactionApiTest extends ApiTest {

	@Test
	void 매매_기록() {
		final RecordTransactionRequest request = new RecordTransactionRequest(
			"월급 입금",
			TransactionType.INCOME,
			PaymentMethod.CASH,
			3_500_000,
			LocalDateTime.now()
		);

		final ExtractableResponse<Response> response = RestAssured.given().log().all()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.body(request)
			.when().post("/transactions")
			.then().log().all()
			.extract();

		SoftAssertions.assertSoftly(softly -> {
			assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
			assertThat(response.header("Location")).isEqualTo("/transactions/1");
		});

	}
}