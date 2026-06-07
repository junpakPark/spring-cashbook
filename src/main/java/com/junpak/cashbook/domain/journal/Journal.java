package com.junpak.cashbook.domain.journal;

import com.junpak.cashbook.domain.Money;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(uniqueConstraints = @UniqueConstraint(
	name = "uk_journal_transaction",
	columnNames = "transaction_id"
))
public class Journal {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private Long transactionId;
	private JournalAccounts journalAccounts;
	@Embedded
	private Money amount;

	public Journal(Long transactionId, JournalAccounts journalAccounts, Money amount) {
		this.transactionId = transactionId;
		this.journalAccounts = journalAccounts;
		this.amount = amount;
	}
}
