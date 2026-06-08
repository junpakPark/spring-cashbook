package com.junpak.cashbook.application.dto.request;

import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;

import com.junpak.cashbook.domain.journal.Journal;

public record FinancialReportCondition(
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
	LocalDateTime until,
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
	LocalDateTime from,
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
	LocalDateTime to
) {

	public static FinancialReportCondition from(Journal journal) {
		return new FinancialReportCondition(journal.getTransactionDate(), null, null);
	}
}
