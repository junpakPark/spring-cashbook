package com.junpak.cashbook.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.junpak.cashbook.application.dto.request.RecordTransactionRequest;
import com.junpak.cashbook.domain.transaction.Transaction;
import com.junpak.cashbook.domain.transaction.TransactionRepository;

@Service
@Transactional
public class TransactionService {

	private final TransactionRepository transactionRepository;

	public TransactionService(TransactionRepository transactionRepository) {
		this.transactionRepository = transactionRepository;
	}

	public Long recordTransaction(RecordTransactionRequest command) {
		Transaction transaction = transactionRepository.save(command.toTransaction());

		return transaction.getId();
	}

}
