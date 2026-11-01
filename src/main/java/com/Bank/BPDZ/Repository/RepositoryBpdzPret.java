package com.Bank.BPDZ.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.Bank.BPDZ.Entity.BPDZHabi;
import com.Bank.BPDZ.Entity.BPDZPret;

@Repository
public interface RepositoryBpdzPret extends JpaRepository<BPDZPret, Long> {
	Optional<BPDZPret> findByAgentAndMontantPretAndDatePret(BPDZHabi agent, BigDecimal montantPret, LocalDate datePret);


}
