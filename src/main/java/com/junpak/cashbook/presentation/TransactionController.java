package com.junpak.cashbook.presentation;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.junpak.cashbook.application.TransactionCanceler;
import com.junpak.cashbook.application.TransactionRecorder;
import com.junpak.cashbook.application.dto.request.CancelTransactionRequest;
import com.junpak.cashbook.application.dto.request.RecordTransactionRequest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/transactions")
public class TransactionController {

	private final TransactionRecorder transactionRecorder;
	private final TransactionCanceler transactionCanceler;

	@PostMapping
	public ResponseEntity<Void> recordTransaction(@Valid @RequestBody RecordTransactionRequest request) {
		final Long transactionId = transactionRecorder.record(request);

		return ResponseEntity.created(URI.create("/transactions/" + transactionId)).build();
	}

	@PostMapping("/{transactionId}/cancel")
	public ResponseEntity<Void> cancelTransaction(
		@PathVariable Long transactionId,
		@Valid @RequestBody CancelTransactionRequest request
	) {
		transactionCanceler.cancel(transactionId, request);

		return ResponseEntity.noContent().build();
	}
}
