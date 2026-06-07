package com.junpak.cashbook.application.dto.request;

import java.time.LocalDateTime;

import com.junpak.cashbook.domain.Money;
import com.junpak.cashbook.domain.transaction.PaymentMethod;
import com.junpak.cashbook.domain.transaction.Transaction;
import com.junpak.cashbook.domain.transaction.TransactionType;

public record RecordTransactionRequest(
	String detail,
	TransactionType transactionType,
	PaymentMethod paymentMethod,
	int amount,
	LocalDateTime transactionDate
) {

	public Transaction toTransaction() {
		return new Transaction(detail, transactionType, paymentMethod, Money.from(amount), transactionDate);
	}
}
