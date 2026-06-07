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
		if (amount < 0) {
			throw new IllegalArgumentException("금액은 음수가 될 수 없습니다.");
		}
		return new Money(BigDecimal.valueOf(amount));
	}

	@Override
	public boolean equals(Object object) {
		if (this == object) {
			return true;
		}
		if (object == null || getClass() != object.getClass()) {
			return false;
		}
		final Money money = (Money)object;
		return Objects.equals(amount, money.amount);
	}

	@Override
	public int hashCode() {
		return Objects.hash(amount);
	}
}
