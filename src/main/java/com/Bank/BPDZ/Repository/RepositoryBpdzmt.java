package com.Bank.BPDZ.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.Bank.BPDZ.Entity.BPDZDir;

import com.Bank.BPDZ.Entity.BPDZmt;

@Repository
public interface RepositoryBpdzmt extends JpaRepository<BPDZmt,Long>{
	List<BPDZmt> findByBanqueEmetteurOrBanqueRecepteur(BPDZDir banqueEmetteur, BPDZDir banqueRecepteur);

	List<BPDZmt> findByStatutMouvementNot(String string);

}
