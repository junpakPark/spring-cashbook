package com.junpak.cashbook.application.dto.request;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotNull;

public record CancelTransactionRequest(
	@NotNull
	LocalDateTime cancelDate
) {
}
