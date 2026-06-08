package com.junpak.cashbook.presentation;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.junpak.cashbook.application.FinancialReportReader;
import com.junpak.cashbook.application.dto.request.FinancialReportCondition;
import com.junpak.cashbook.application.dto.response.FinancialPositionResponse;
import com.junpak.cashbook.application.dto.response.ProfitAndLossResponse;
import com.junpak.cashbook.application.dto.response.TrialBalanceResponse;
import com.junpak.cashbook.domain.FinancialPosition;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class FinancialReportController {

	private final FinancialReportReader financialReportReader;

	@GetMapping("/financial-position")
	public ResponseEntity<FinancialPositionResponse> getFinancialPosition(FinancialReportCondition condition) {
		FinancialPosition financialPosition = financialReportReader.getFinancialPosition(condition);

		return ResponseEntity.ok(FinancialPositionResponse.from(financialPosition));
	}

	@GetMapping("/profits-and-losses")
	public ResponseEntity<ProfitAndLossResponse> getProfitsAndLosses(FinancialReportCondition condition) {
		return ResponseEntity.ok(financialReportReader.getProfitsAndLosses(condition));
	}

	@GetMapping("/trial-balance")
	public ResponseEntity<TrialBalanceResponse> getTrialBalance(FinancialReportCondition condition) {
		return ResponseEntity.ok(financialReportReader.getTrialBalance(condition));
	}
}
