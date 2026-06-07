package com.junpak.cashbook.domain.transaction;

import java.time.LocalDateTime;
import java.util.Objects;

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
		TransactionType type,
		PaymentMethod method,
		Money amount,
		LocalDateTime transactionDate
	) {
		validate(type, method);
		this.detail = Objects.requireNonNull(detail, "거래 내용은 필수입니다.");
		this.transactionType = type;
		this.paymentMethod = method;
		this.amount = Objects.requireNonNull(amount, "거래 금액은 필수입니다.");;
		this.transactionDate = Objects.requireNonNull(transactionDate, "거래일시는 필수입니다.");
	}

	private void validate(TransactionType type, PaymentMethod method) {
		Objects.requireNonNull(type, "거래 유형은 필수입니다.");
		Objects.requireNonNull(method, "결제 수단은 필수입니다.");
		if (!method.isAllowedFor(type)) {
			throw new IllegalArgumentException("해당 거래 유형에서 사용할 수 없는 결제수단입니다.");
		}

	}
}
