package com.junpak.cashbook.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.junpak.cashbook.application.dto.request.RecordTransactionRequest;
import com.junpak.cashbook.domain.TransactionJournalizer;
import com.junpak.cashbook.domain.journal.JournalRepository;
import com.junpak.cashbook.domain.transaction.Transaction;
import com.junpak.cashbook.domain.transaction.TransactionRepository;

@Service
@Transactional
public class TransactionService {

	private final TransactionRepository transactionRepository;
	private final JournalRepository journalRepository;
	private final TransactionJournalizer journalizer;

	public TransactionService(
		TransactionRepository transactionRepository,
		JournalRepository journalRepository,
		TransactionJournalizer journalizer
	) {
		this.transactionRepository = transactionRepository;
		this.journalRepository = journalRepository;
		this.journalizer = journalizer;
	}

	public Long recordTransaction(RecordTransactionRequest command) {
		Transaction transaction = transactionRepository.save(command.toTransaction());
		journalRepository.save(journalizer.journalize(transaction));

		return transaction.getId();
	}

}
