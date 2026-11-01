package com.Bank.BPDZ.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.Bank.BPDZ.Entity.BPDZPmtA;

@Repository
public interface RepositoryBpdzPta extends JpaRepository<BPDZPmtA, Long> {
	
	  @Query("SELECT a FROM BPDZPmtA a WHERE " +
	           "(a.banqueEmetteur.id = :bankId OR a.banqueRecepteur.id = :bankId) AND " +
	           "a.dateArchived BETWEEN :startDate AND :endDate " +
	           "ORDER BY a.dateArchived")
	    List<BPDZPmtA> findByBankAndPeriod(
	        @Param("bankId") Long bankId,
	        @Param("startDate") LocalDate startDate,
	        @Param("endDate") LocalDate endDate
	    );

}
