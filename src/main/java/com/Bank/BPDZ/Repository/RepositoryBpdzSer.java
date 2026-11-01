package com.Bank.BPDZ.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.Bank.BPDZ.Entity.BPDZSer;

@Repository
public interface RepositoryBpdzSer extends JpaRepository<BPDZSer, Long>{

}
