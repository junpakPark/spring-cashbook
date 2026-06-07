package com.junpak.cashbook.application.dto.request;

import java.time.LocalDateTime;

import com.junpak.cashbook.domain.Money;
import com.junpak.cashbook.domain.transaction.PaymentMethod;
import com.junpak.cashbook.domain.transaction.Transaction;
import com.junpak.cashbook.domain.transaction.TransactionType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public record RecordTransactionRequest(
	@NotBlank
	String detail,
	@NotNull
	TransactionType transactionType,
	@NotNull
	PaymentMethod paymentMethod,
	@PositiveOrZero
	int amount,
	@NotNull
	LocalDateTime transactionDate
) {

	public Transaction toTransaction() {
		return new Transaction(detail, transactionType, paymentMethod, Money.from(amount), transactionDate);
	}
}
