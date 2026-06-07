package com.junpak.cashbook.domain;

import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
class MoneyTest {

	@Test
	void 금액은_0원이_될_수_있다() {
		final Money money = Money.from(0);

		assertThat(money.getAmount()).isEqualTo(BigDecimal.ZERO);
	}

	@Test
	void 금액은_음수가_될_수_있다() {
		final Money money = Money.from(-1);

		assertThat(money.getAmount()).isEqualByComparingTo(BigDecimal.valueOf(-1));
	}

	@Test
	void 금액이_같으면_같은_값이다() {
		final Money money = Money.from(1_000);
		final Money same = Money.from(1_000);

		assertThat(money).isEqualTo(same);
		assertThat(money.hashCode()).isEqualTo(same.hashCode());
	}

	@Test
	void 금액이_다르면_다른_값이다() {
		final Money money = Money.from(1_000);
		final Money other = Money.from(2_000);

		assertThat(money).isNotEqualTo(other);
	}

	@Test
	void 금액을_더할_수_있다() {
		final Money money = Money.from(1_000);
		final Money other = Money.from(2_000);

		final Money result = money.add(other);

		assertThat(result.getAmount()).isEqualByComparingTo(BigDecimal.valueOf(3_000));
	}

	@Test
	void 금액을_뺄_수_있다() {
		final Money money = Money.from(1_000);
		final Money other = Money.from(2_000);

		final Money result = money.subtract(other);

		assertThat(result.getAmount()).isEqualByComparingTo(BigDecimal.valueOf(-1_000));
	}

	@Test
	void 금액의_부호를_뒤집을_수_있다() {
		final Money money = Money.from(1_000);

		final Money result = money.negate();

		assertThat(result.getAmount()).isEqualByComparingTo(BigDecimal.valueOf(-1_000));
	}

	@Test
	void 금액이_0원인지_확인한다() {
		assertThat(Money.ZERO.isZero()).isTrue();
		assertThat(Money.from(1).isZero()).isFalse();
	}
}
