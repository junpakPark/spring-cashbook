package com.junpak.cashbook.domain;

import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
class PriceTest {

	@Test
	void 가격은_0원이_될_수_있다() {
		final Price price = Price.from(0);

		assertThat(price.getAmount()).isEqualTo(BigDecimal.ZERO);
	}

	@Test
	void 가격은_음수가_될_수_없다() {
		assertThatThrownBy(() -> Price.from(-1))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage("가격은 음수가 될 수 없습니다.");
	}

	@Test
	void 가격이_같으면_같은_값이다() {
		final Price price = Price.from(1_000);
		final Price same = Price.from(1_000);

		assertThat(price).isEqualTo(same);
		assertThat(price.hashCode()).isEqualTo(same.hashCode());
	}

	@Test
	void 가격이_다르면_다른_값이다() {
		final Price price = Price.from(1_000);
		final Price other = Price.from(2_000);

		assertThat(price).isNotEqualTo(other);
	}

	@Test
	void 회계_금액으로_변환한다() {
		final Price price = Price.from(1_000);

		final Money money = price.toMoney();

		assertThat(money).isEqualTo(Money.from(1_000));
	}
}
