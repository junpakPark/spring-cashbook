package com.junpak.cashbook.ui;

import java.time.LocalDateTime;

import org.springframework.http.MediaType;

import com.junpak.cashbook.application.dto.request.CancelTransactionRequest;
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

	public static RecordTransactionRequest 매매기록요청_생성(
		TransactionType type,
		PaymentMethod method,
		int amount,
		LocalDateTime transactionDate
	) {
		return new RecordTransactionRequest(
			"거래 내용",
			type,
			method,
			amount,
			transactionDate
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

	public static CancelTransactionRequest 매매취소요청_생성() {
		return new CancelTransactionRequest(
			LocalDateTime.of(2026, 6, 8, 11, 20)
		);
	}

	public static ExtractableResponse<Response> 매매취소요청(CancelTransactionRequest request, Long transactionId) {
		return RestAssured.given().log().all()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.body(request)
			.when().post("/transactions/{transactionId}/cancel", transactionId)
			.then().log().all()
			.extract();
	}
}
