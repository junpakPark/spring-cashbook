package com.junpak.cashbook.presentation;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.junpak.cashbook.application.FinancialReportReader;
import com.junpak.cashbook.application.dto.request.FinancialReportCondition;
import com.junpak.cashbook.application.dto.response.FinancialPositionResponse;
import com.junpak.cashbook.application.dto.response.ProfitAndLossResponse;
import com.junpak.cashbook.application.dto.response.TrialBalanceResponse;

@RestController
public class FinancialReportController {

	private final FinancialReportReader financialReportReader;

	public FinancialReportController(FinancialReportReader financialReportReader) {
		this.financialReportReader = financialReportReader;
	}

	@GetMapping("/financial-position")
	public FinancialPositionResponse getFinancialPosition(FinancialReportCondition condition) {
		return financialReportReader.getFinancialPosition(condition);
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
