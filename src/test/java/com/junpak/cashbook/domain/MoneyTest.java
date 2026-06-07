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
	void 금액은_음수가_될_수_없다() {
		assertThatThrownBy(() -> Money.from(-1))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage("금액은 음수가 될 수 없습니다.");
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
}
