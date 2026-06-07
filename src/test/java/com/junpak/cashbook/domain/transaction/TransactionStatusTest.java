package com.junpak.cashbook.domain.transaction;

import static com.junpak.cashbook.domain.transaction.TransactionStatus.*;
import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
class TransactionStatusTest {

	@Test
	void 활성_상태는_취소할_수_있다() {
		assertThatCode(ACTIVE::validateCancelable)
			.doesNotThrowAnyException();
	}

	@Test
	void 취소_상태는_다시_취소할_수_없다() {
		assertThatThrownBy(CANCELED::validateCancelable)
			.isInstanceOf(IllegalStateException.class)
			.hasMessage("취소된 매매는 다시 취소할 수 없습니다.");
	}
}
