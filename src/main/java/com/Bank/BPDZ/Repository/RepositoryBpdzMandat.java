package com.Bank.BPDZ.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.Bank.BPDZ.Entity.BPDZDir;
import com.Bank.BPDZ.Entity.BPDZMandat;

@Repository
public interface RepositoryBpdzMandat extends JpaRepository<BPDZMandat, Long>{

	boolean existsByBanque(BPDZDir banque);

}
