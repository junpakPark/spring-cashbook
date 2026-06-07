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
public class Price {

	private BigDecimal amount;

	private Price(BigDecimal amount) {
		this.amount = Objects.requireNonNull(amount, "가격은 필수입니다.");
	}

	public static Price from(int amount) {
		if (amount < 0) {
			throw new IllegalArgumentException("가격은 음수가 될 수 없습니다.");
		}
		return new Price(BigDecimal.valueOf(amount));
	}

	public Money toMoney() {
		return new Money(amount);
	}

	@Override
	public boolean equals(Object object) {
		if (this == object) {
			return true;
		}
		if (object == null || getClass() != object.getClass()) {
			return false;
		}
		final Price price = (Price)object;
		return Objects.equals(amount, price.amount);
	}

	@Override
	public int hashCode() {
		return Objects.hash(amount);
	}
}
