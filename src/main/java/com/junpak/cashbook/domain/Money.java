package com.junpak.cashbook.domain;

import java.math.BigDecimal;
import java.util.Objects;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Money {

	private BigDecimal amount;

	private Money(BigDecimal amount) {
		this.amount = Objects.requireNonNull(amount, "금액은 필수입니다.");
	}

	public static Money from(int amount) {
		return new Money(BigDecimal.valueOf(amount));
	}
}
