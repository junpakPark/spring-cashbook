package com.junpak.cashbook.domain.journal;

import java.util.Objects;

import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class JournalAccounts {

	@Enumerated(value = EnumType.STRING)
	private Account debit;
	@Enumerated(value = EnumType.STRING)
	private Account credit;

	public JournalAccounts(final Account debit, final Account credit) {
		validate(debit, credit);
		this.debit = debit;
		this.credit = credit;
	}

	private void validate(Account debit, Account credit) {
		Objects.requireNonNull(debit, "차변 계정이 누락되었습니다.");
		Objects.requireNonNull(credit, "대변 계정이 누락되었습니다");
		if (debit == credit) {
			throw new IllegalArgumentException("차변과 대변 계정은 같을 수 없습니다.");
		}
	}

	public JournalAccounts reverse() {
		return new JournalAccounts(credit, debit);
	}
}
