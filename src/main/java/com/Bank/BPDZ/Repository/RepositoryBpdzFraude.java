package com.Bank.BPDZ.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.Bank.BPDZ.Entity.BPDZFraude;

@Repository
public interface RepositoryBpdzFraude extends JpaRepository<BPDZFraude, Long> {

	Optional<BPDZFraude> findByInformationInterdite(String info);

}
