package com.junpak.cashbook.ui;

import java.time.LocalDateTime;

import org.springframework.http.MediaType;

import com.junpak.cashbook.application.dto.request.RecordTransactionRequest;
import com.junpak.cashbook.domain.transaction.PaymentMethod;
import com.junpak.cashbook.domain.transaction.TransactionType;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

public class TransactionSteps {

	public static RecordTransactionRequest 매매기록요청_생성() {
		return new RecordTransactionRequest(
			"월급 입금",
			TransactionType.INCOME,
			PaymentMethod.CASH,
			3_500_000,
			LocalDateTime.now()
		);
	}

	public static ExtractableResponse<Response> 매매기록요청(RecordTransactionRequest request) {
		return RestAssured.given().log().all()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.body(request)
			.when().post("/transactions")
			.then().log().all()
			.extract();
	}
}
