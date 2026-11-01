package com.Bank.BPDZ.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.Bank.BPDZ.Entity.BPDZHabi;

@Repository
public interface RepositoryBpdzHbi extends JpaRepository<BPDZHabi, Long> {
	
	public BPDZHabi findByLogin(String login);
	public BPDZHabi findByid(Long id);
	
	

}
