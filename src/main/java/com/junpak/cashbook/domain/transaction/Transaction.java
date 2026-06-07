package com.junpak.cashbook.domain.transaction;

import java.time.LocalDateTime;

import com.junpak.cashbook.domain.Money;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Transaction {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String detail;
	@Enumerated(value = EnumType.STRING)
	private TransactionType transactionType;
	@Enumerated(value = EnumType.STRING)
	private PaymentMethod paymentMethod;
	private LocalDateTime transactionDate;
	private Money amount;

	public Transaction(
		String detail,
		TransactionType transactionType,
		PaymentMethod paymentMethod,
		Money amount,
		LocalDateTime transactionDate
	) {
		this.detail = detail;
		this.transactionType = transactionType;
		this.paymentMethod = paymentMethod;
		this.amount = amount;
		this.transactionDate = transactionDate;
	}
}
