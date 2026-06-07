package com.junpak.cashbook.domain.journal;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface JournalRepository extends JpaRepository<Journal, Long> {
	List<Journal> findByTransactionId(Long transactionId);
}
