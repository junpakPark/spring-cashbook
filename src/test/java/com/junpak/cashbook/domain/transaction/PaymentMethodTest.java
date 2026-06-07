package com.junpak.cashbook.domain.transaction;

import static com.junpak.cashbook.domain.transaction.PaymentMethod.*;
import static com.junpak.cashbook.domain.transaction.TransactionType.*;
import static org.assertj.core.api.Assertions.*;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@SuppressWarnings("NonAsciiCharacters")
class PaymentMethodTest {

	@ParameterizedTest
	@ValueSource(strings = {"INCOME", "EXPENSE", "SECONDHAND_SALE"})
	void 현금은_모든_거래_유형에_허용된다(TransactionType type) {
		assertThat(CASH.isAllowedFor(type)).isTrue();
	}

	@Test
	void 카드는_지출에만_허용된다() {
		SoftAssertions.assertSoftly(softly -> {
			assertThat(CARD.isAllowedFor(EXPENSE)).isTrue();
			assertThat(CARD.isAllowedFor(INCOME)).isFalse();
			assertThat(CARD.isAllowedFor(SECONDHAND_SALE)).isFalse();
		});
	}

	@Test
	void 외상은_지출에만_허용된다() {
		SoftAssertions.assertSoftly(softly -> {
			assertThat(CREDIT.isAllowedFor(EXPENSE)).isTrue();
			assertThat(CREDIT.isAllowedFor(INCOME)).isFalse();
			assertThat(CREDIT.isAllowedFor(SECONDHAND_SALE)).isFalse();
		});
	}
}
