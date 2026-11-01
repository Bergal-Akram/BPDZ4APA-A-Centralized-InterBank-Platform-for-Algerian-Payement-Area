package com.Bank.BPDZ.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.Bank.BPDZ.Entity.BPDZCptBcDCA;
import com.Bank.BPDZ.Entity.BPDZDir;

@Repository
public interface RepositoryBpdzCptbcdca extends JpaRepository<BPDZCptBcDCA, Long >{
	
	List<BPDZCptBcDCA> findAllByBanque(BPDZDir banque);

	BPDZCptBcDCA findByNumeroCompte(String numeroCompte);
	boolean existsByBanqueAndNatureCompte(BPDZDir banque, String natureCompte);
	BPDZCptBcDCA findByBanque(BPDZDir receiverBank);
	BPDZCptBcDCA findByBanqueAndNatureCompteIgnoreCase(BPDZDir banque, String natureCompte);

}
