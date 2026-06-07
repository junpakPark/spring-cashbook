package com.junpak.cashbook.application;

import com.junpak.cashbook.application.dto.request.FinancialReportCondition;
import com.junpak.cashbook.application.dto.response.FinancialPositionResponse;
import com.junpak.cashbook.application.dto.response.ProfitAndLossResponse;
import com.junpak.cashbook.application.dto.response.TrialBalanceResponse;

public interface FinancialReportReader {

	FinancialPositionResponse getFinancialPosition(FinancialReportCondition condition);

	ProfitAndLossResponse getProfitsAndLosses(FinancialReportCondition condition);

	TrialBalanceResponse getTrialBalance(FinancialReportCondition condition);
}
