package com.junpak.cashbook.ui;

import java.time.LocalDateTime;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

public class FinancialReportSteps {

	public static ExtractableResponse<Response> 재무상태조회요청() {
		return RestAssured.given().log().all()
			.when().get("/financial-position")
			.then().log().all()
			.extract();
	}

	public static ExtractableResponse<Response> 재무상태조회요청(LocalDateTime until) {
		return RestAssured.given().log().all()
			.queryParam("until", until.toString())
			.when().get("/financial-position")
			.then().log().all()
			.extract();
	}

	public static ExtractableResponse<Response> 손익조회요청() {
		return RestAssured.given().log().all()
			.when().get("/profits-and-losses")
			.then().log().all()
			.extract();
	}

	public static ExtractableResponse<Response> 손익조회요청(LocalDateTime from) {
		return RestAssured.given().log().all()
			.queryParam("from", from.toString())
			.when().get("/profits-and-losses")
			.then().log().all()
			.extract();
	}

	public static ExtractableResponse<Response> 손익조회요청(LocalDateTime from, LocalDateTime to) {
		return RestAssured.given().log().all()
			.queryParam("from", from.toString())
			.queryParam("to", to.toString())
			.when().get("/profits-and-losses")
			.then().log().all()
			.extract();
	}

	public static ExtractableResponse<Response> 시산표조회요청() {
		return RestAssured.given().log().all()
			.when().get("/trial-balance")
			.then().log().all()
			.extract();
	}

	public static ExtractableResponse<Response> 시산표조회요청(LocalDateTime from, LocalDateTime to) {
		return RestAssured.given().log().all()
			.queryParam("from", from.toString())
			.queryParam("to", to.toString())
			.when().get("/trial-balance")
			.then().log().all()
			.extract();
	}
}
