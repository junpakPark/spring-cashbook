package com.junpak.cashbook.presentation;

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
	public FinancialPositionResponse getFinancialPosition(FinancialReportCondition condition) {
		FinancialPosition financialPosition = financialReportReader.getFinancialPosition(condition);

		return FinancialPositionResponse.from(financialPosition);
	}

	@GetMapping("/profits-and-losses")
	public ProfitAndLossResponse getProfitsAndLosses(FinancialReportCondition condition) {
		return financialReportReader.getProfitsAndLosses(condition);
	}

	@GetMapping("/trial-balance")
	public TrialBalanceResponse getTrialBalance(FinancialReportCondition condition) {
		return financialReportReader.getTrialBalance(condition);
	}
}
