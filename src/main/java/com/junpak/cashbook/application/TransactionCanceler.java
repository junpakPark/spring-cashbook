package com.junpak.cashbook.application;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.junpak.cashbook.application.dto.request.CancelTransactionRequest;
import com.junpak.cashbook.domain.TransactionJournalizer;
import com.junpak.cashbook.domain.journal.Journal;
import com.junpak.cashbook.domain.journal.JournalRepository;
import com.junpak.cashbook.domain.transaction.Transaction;
import com.junpak.cashbook.domain.transaction.TransactionRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class TransactionCanceler {

	private final TransactionRepository transactionRepository;
	private final JournalRepository journalRepository;
	private final TransactionJournalizer journalizer;

	public void cancel(Long transactionId, CancelTransactionRequest request) {
		Transaction transaction = transactionRepository.findById(transactionId)
			.orElseThrow(() -> new IllegalArgumentException("해당하는 거래를 찾을 수 없습니다."));

		List<Journal> journals = journalRepository.findByTransactionId(transaction.getId());
		Journal canceledJournal = journalizer.cancel(journals, request.cancelDate());
		journalRepository.save(canceledJournal);

		transaction.cancel(request.cancelDate());
	}
}
