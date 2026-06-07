package com.junpak.cashbook.domain.journal;

import java.time.LocalDateTime;
import java.util.Objects;

import com.junpak.cashbook.domain.Money;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Journal {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private Long transactionId;
	private JournalAccounts journalAccounts;
	@Embedded
	private Money amount;
	private LocalDateTime transactionDate;

	public Journal(Long transactionId, JournalAccounts journalAccounts, Money amount, LocalDateTime transactionDate) {
		this.transactionId = transactionId;
		this.journalAccounts = journalAccounts;
		this.amount = amount;
		this.transactionDate = Objects.requireNonNull(transactionDate, "분개일시는 필수입니다.");
	}
}
