package com.junpak.cashbook.application;

import java.util.List;

import org.springframework.stereotype.Component;

import com.junpak.cashbook.application.dto.request.FinancialReportCondition;
import com.junpak.cashbook.domain.FinancialPosition;
import com.junpak.cashbook.domain.journal.Journal;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class FinancialPositionValidator {

	private final FinancialReportReader reader;

	public void validate(Journal journal) {
		FinancialPosition changedPosition = FinancialPosition.from(List.of(journal));
		FinancialPosition position = reader.getFinancialPosition(FinancialReportCondition.from(journal));

		position.validateApplicable(changedPosition);
	}
}
