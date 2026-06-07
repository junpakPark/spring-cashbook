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

	public static final Money ZERO = new Money(BigDecimal.ZERO);

	private BigDecimal amount;

	public Money(BigDecimal amount) {
		this.amount = Objects.requireNonNull(amount, "금액은 필수입니다.");
	}

	public static Money from(int amount) {
		return new Money(BigDecimal.valueOf(amount));
	}

	public Money add(Money other) {
		Objects.requireNonNull(other, "더할 금액은 필수입니다.");
		return new Money(amount.add(other.amount));
	}

	public Money subtract(Money other) {
		Objects.requireNonNull(other, "뺄 금액은 필수입니다.");
		return new Money(amount.subtract(other.amount));
	}

	public Money negate() {
		return new Money(amount.negate());
	}

	public boolean isZero() {
		return amount.compareTo(BigDecimal.ZERO) == 0;
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
