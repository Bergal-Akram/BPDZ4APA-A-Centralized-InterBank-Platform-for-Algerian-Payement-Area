package com.Bank.BPDZ.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.Bank.BPDZ.Entity.BPDZDir;

@Repository
public interface RepositoryBpdzDir extends JpaRepository<BPDZDir, Long> {
	
	@Query("SELECT d FROM BPDZDir d WHERE " +
		       "d.nomBanque LIKE %:keyword% OR " +
		       "d.bic LIKE %:keyword% OR " +
		       "d.codeBanque LIKE %:keyword%")
		List<BPDZDir> searchByKeyword(@Param("keyword") String keyword);
	
	public BPDZDir findByBic(String bic);
	boolean existsByBic(String bic);
	boolean existsByCodeBanque(String codeBanque);
	boolean existsByMail(String mail);
	boolean existsByTel(String tel);
	boolean existsByNomBanque(String nomBanque);

}
