package com.junpak.cashbook.domain.transaction;

public enum TransactionStatus {
	ACTIVE, CANCELED
	;

	public void validateCancelable() {
		if (this == CANCELED) {
			throw new IllegalStateException("취소된 매매는 다시 취소할 수 없습니다.");
		}
	}
}
