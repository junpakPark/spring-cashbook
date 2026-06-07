package com.junpak.cashbook.domain;

import java.util.Objects;

import org.springframework.stereotype.Component;

import com.junpak.cashbook.domain.journal.Account;
import com.junpak.cashbook.domain.journal.Journal;
import com.junpak.cashbook.domain.journal.JournalAccounts;
import com.junpak.cashbook.domain.transaction.PaymentMethod;
import com.junpak.cashbook.domain.transaction.Transaction;
import com.junpak.cashbook.domain.transaction.TransactionType;

@Component
public class TransactionJournalizer {

	private TransactionJournalizer() {
	}

	public Journal journalize(final Transaction transaction) {
		JournalAccounts journalAccounts = resolveJournalAccounts(transaction.getTransactionType(),
			resolveMethodAccount(transaction.getPaymentMethod()));

		return new Journal(transaction.getId(), journalAccounts, transaction.getAmount());
	}

	private JournalAccounts resolveJournalAccounts(TransactionType type, Account methodAccount) {
		return switch (Objects.requireNonNull(type, "거래 유형은 필수입니다.")) {
			case INCOME -> new JournalAccounts(methodAccount, Account.INCOME);
			case SECONDHAND_SALE -> new JournalAccounts(methodAccount, Account.OTHER_INCOME);
			case EXPENSE -> new JournalAccounts(Account.EXPENSE, methodAccount);
			case REFUND -> throw new UnsupportedOperationException("환불 기능은 아직 구현되지 않았습니다.");
		};
	}

	private Account resolveMethodAccount(PaymentMethod method) {
		return switch (Objects.requireNonNull(method, "결제 수단은 필수입니다.")) {
			case CASH -> Account.CASH;
			case CARD -> Account.CARD_PAYABLE;
			case CREDIT -> Account.PAYABLE;
		};
	}
}
