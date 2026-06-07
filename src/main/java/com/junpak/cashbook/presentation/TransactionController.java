package com.junpak.cashbook.presentation;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.junpak.cashbook.application.TransactionService;
import com.junpak.cashbook.application.dto.request.RecordTransactionRequest;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

	private final TransactionService transactionService;

	public TransactionController(TransactionService transactionService) {
		this.transactionService = transactionService;
	}

	@PostMapping
	public ResponseEntity<Void> recordTransaction(@RequestBody RecordTransactionRequest request) {
		final Long transactionId = transactionService.recordTransaction(request);

		return ResponseEntity.created(URI.create("/transactions/" + transactionId)).build();
	}
}
