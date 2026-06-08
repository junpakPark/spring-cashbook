package com.junpak.cashbook.infrastructure;

import static com.junpak.cashbook.domain.journal.QJournal.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.junpak.cashbook.application.FinancialReportReader;
import com.junpak.cashbook.application.dto.request.FinancialReportCondition;
import com.junpak.cashbook.application.dto.response.FinancialPositionResponse;
import com.junpak.cashbook.application.dto.response.ProfitAndLossResponse;
import com.junpak.cashbook.application.dto.response.TrialBalanceAccountResponse;
import com.junpak.cashbook.application.dto.response.TrialBalanceResponse;
import com.junpak.cashbook.domain.account.Account;
import com.junpak.cashbook.domain.journal.Journal;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class QueryDslFinancialReportReader implements FinancialReportReader {

	private final JPAQueryFactory queryFactory;

	@Override
	public FinancialPositionResponse getFinancialPosition(FinancialReportCondition condition) {
		final List<Journal> journals = queryFactory.selectFrom(journal)
			.where(until(condition))
			.fetch();

		return FinancialPositionResponse.of(
			Account.CASH.sumNetChangeAmount(journals),
			Account.CARD_PAYABLE.sumNetChangeAmount(journals),
			Account.PAYABLE.sumNetChangeAmount(journals)
		);
	}

	@Override
	public ProfitAndLossResponse getProfitsAndLosses(FinancialReportCondition condition) {
		final List<Journal> journals = queryFactory.selectFrom(journal)
			.where(period(condition.from(), condition.to()))
			.fetch();

		return ProfitAndLossResponse.of(
			Account.INCOME.sumNetChangeAmount(journals),
			Account.OTHER_INCOME.sumNetChangeAmount(journals),
			Account.EXPENSE.sumNetChangeAmount(journals)
		);
	}

	@Override
	public TrialBalanceResponse getTrialBalance(FinancialReportCondition condition) {
		final List<Journal> journals = queryFactory.selectFrom(journal)
			.where(period(condition.from(), condition.to()))
			.fetch();

		List<TrialBalanceAccountResponse> responses = Arrays.stream(Account.values())
			.map(account -> TrialBalanceAccountResponse.of(account, account.sumDebitAmount(journals),
				account.sumCreditAmount(journals)))
			.toList();

		return TrialBalanceResponse.from(responses);
	}

	private BooleanExpression until(FinancialReportCondition condition) {
		if (Objects.isNull(condition.until())) {
			return null;
		}
		return journal.transactionDate.loe(condition.until());
	}

	private BooleanExpression period(LocalDateTime from, LocalDateTime to) {
		final BooleanExpression fromCondition = from(from);
		final BooleanExpression toCondition = to(to);

		if (Objects.isNull(fromCondition)) {
			return toCondition;
		}
		if (Objects.isNull(toCondition)) {
			return fromCondition;
		}
		return fromCondition.and(toCondition);
	}

	private BooleanExpression from(LocalDateTime from) {
		if (Objects.isNull(from)) {
			return null;
		}
		return journal.transactionDate.goe(from);
	}

	private BooleanExpression to(LocalDateTime to) {
		if (Objects.isNull(to)) {
			return null;
		}
		return journal.transactionDate.loe(to);
	}

}
