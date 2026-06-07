package com.junpak.cashbook.domain;

import java.time.LocalDateTime;
import java.util.List;
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
		Account methodAccount = resolveMethodAccount(transaction.getPaymentMethod());
		JournalAccounts journalAccounts = resolveJournalAccounts(transaction.getTransactionType(), methodAccount);

		return new Journal(
			transaction.getId(),
			journalAccounts,
			transaction.getAmount().toMoney(),
			transaction.getTransactionDate()
		);
	}

	private JournalAccounts resolveJournalAccounts(TransactionType type, Account methodAccount) {
		return switch (Objects.requireNonNull(type, "거래 유형은 필수입니다.")) {
			case INCOME -> new JournalAccounts(methodAccount, Account.INCOME);
			case SECONDHAND_SALE -> new JournalAccounts(methodAccount, Account.OTHER_INCOME);
			case EXPENSE -> new JournalAccounts(Account.EXPENSE, methodAccount);
			case CARD_BILL_PAYMENT -> new JournalAccounts(Account.CARD_PAYABLE, Account.CASH);
			case PAYABLE_REPAYMENT -> new JournalAccounts(Account.PAYABLE, Account.CASH);
		};
	}

	private Account resolveMethodAccount(PaymentMethod method) {
		return switch (Objects.requireNonNull(method, "결제 수단은 필수입니다.")) {
			case CASH -> Account.CASH;
			case CARD -> Account.CARD_PAYABLE;
			case CREDIT -> Account.PAYABLE;
		};
	}

	public Journal cancel(List<Journal> journals, LocalDateTime cancelTime) {
		Objects.requireNonNull(journals, "분개 목록은 필수입니다.");
		Objects.requireNonNull(cancelTime, "취소일시는 필수입니다.");
		if (journals.isEmpty()) {
			throw new IllegalStateException("해당하는 분개가 없습니다.");
		}
		if (journals.size() > 1) {
			throw new IllegalStateException("이미 취소 분개가 생성된 거래입니다.");
		}
		Journal journal = journals.getFirst();
		return journal.cancel(cancelTime);
	}
}
