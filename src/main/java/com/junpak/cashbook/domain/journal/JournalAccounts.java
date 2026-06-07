package com.junpak.cashbook.domain.journal;

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
		this.debit = debit;
		this.credit = credit;
	}

}
