package com.junpak.cashbook.application;

import com.junpak.cashbook.application.dto.request.FinancialReportCondition;
import com.junpak.cashbook.application.dto.response.ProfitAndLossResponse;
import com.junpak.cashbook.application.dto.response.TrialBalanceResponse;
import com.junpak.cashbook.domain.FinancialPosition;

public interface FinancialReportReader {

	FinancialPosition getFinancialPosition(FinancialReportCondition condition);

	ProfitAndLossResponse getProfitsAndLosses(FinancialReportCondition condition);

	TrialBalanceResponse getTrialBalance(FinancialReportCondition condition);
}
