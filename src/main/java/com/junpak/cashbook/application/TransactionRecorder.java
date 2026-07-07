package com.junpak.cashbook.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.junpak.cashbook.application.dto.request.RecordTransactionRequest;
import com.junpak.cashbook.domain.TransactionJournalizer;
import com.junpak.cashbook.domain.journal.Journal;
import com.junpak.cashbook.domain.journal.JournalRepository;
import com.junpak.cashbook.domain.transaction.Transaction;
import com.junpak.cashbook.domain.transaction.TransactionRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class TransactionRecorder {

	private final TransactionRepository transactionRepository;
	private final JournalRepository journalRepository;
	private final TransactionJournalizer journalizer;
	private final FinancialPositionValidator financialPositionValidator;

	public Long record(RecordTransactionRequest request) {
		Transaction transaction = transactionRepository.save(request.toTransaction());

		Journal journal = journalizer.journalize(transaction);
		financialPositionValidator.validate(journal);
		journalRepository.save(journal);

		return transaction.getId();
	}
}
