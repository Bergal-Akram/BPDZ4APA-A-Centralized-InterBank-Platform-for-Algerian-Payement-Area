package com.Bank.BPDZ.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.Bank.BPDZ.DTO.Pacs008DTO;
import com.Bank.BPDZ.DTO.Pacs009DTO;
import com.Bank.BPDZ.DTO.PacsXmlGenerator;
import com.Bank.BPDZ.DTO.PacsXmlParser;
import com.Bank.BPDZ.Entity.BPDZDir;
import com.Bank.BPDZ.Entity.BPDZPmtA;
import com.Bank.BPDZ.Entity.BPDZmt;
import com.Bank.BPDZ.Repository.RepositoryBpdzDir;
import com.Bank.BPDZ.Repository.RepositoryBpdzPta;
import com.Bank.BPDZ.Repository.RepositoryBpdzmt;



import jakarta.transaction.Transactional;

@Service
public class ServiceSaveArchive {
	
	@Autowired
	RepositoryBpdzmt repositoryBpdzmt;
	@Autowired
	RepositoryBpdzPta repositoryBpdzPta;
	@Autowired
	RepositoryBpdzDir repositoryBpdzDir;
	@Autowired
	PacsXmlGenerator pacsXmlGenerator;
	@Autowired
	PacsXmlParser pacsXmlParser;
	
	
	@Scheduled(cron = "0 0 1 * * *")
    @Transactional
    public void archiveProcessedMovements() {
        // Get all processed movements (status != '99')
        List<BPDZmt> processedMovements = repositoryBpdzmt.findByStatutMouvementNot("99");
        
        for (BPDZmt movement : processedMovements) {
            // Convert to archive entity
            BPDZPmtA archive = convertToArchive(movement);
            
            // Save to archive
            repositoryBpdzPta.save(archive);
            
            // Delete from original table
            repositoryBpdzmt.delete(movement);
        }
    }

    private BPDZPmtA convertToArchive(BPDZmt movement) {
        BPDZPmtA archive = new BPDZPmtA();
        
        // Copy all fields
        archive.setStatutMouvement(movement.getStatutMouvement());
        archive.setNatureMouvement(movement.getNatureMouvement());
        archive.setBanqueEmetteur(movement.getBanqueEmetteur());
        archive.setBanqueRecepteur(movement.getBanqueRecepteur());
        archive.setTypeMessage(movement.getTypeMessage());
        archive.setInformationsMessage(movement.getInformationsMessage());
        archive.setModeTransmission(movement.getModeTransmission());
        archive.setXmlFile(movement.getXmlFile());
        archive.setDateCreation(movement.getDateCreation());
        archive.setDateReception(movement.getDateReception());
        archive.setDateTraitement(movement.getDateTraitement());
        
        // Set archive timestamp to now
        archive.setDateArchived(LocalDateTime.now());
        
        return archive;
    }
//--------------------------------------------------------------------openning Blance---------------------------------------------------------------------------------
    @Scheduled(cron = "0 0 3 1 * *")
    @Transactional
    public void generateMonthlyCamt053Statements() {
        LocalDate statementDate = LocalDate.now().minusMonths(1); // Previous month
        String monthYear = statementDate.format(DateTimeFormatter.ofPattern("yyyy-MM"));
        
        List<BPDZDir> allBanks = repositoryBpdzDir.findAll();
        
        for (BPDZDir bank : allBanks) {
            try {
                generateBankStatement(bank, statementDate, monthYear);
            } catch (Exception e) {
                throw new RuntimeException("Failed to generate CAMT.053 for bank " + bank.getBic(), e);
            }
        }
    }
    
    private void generateBankStatement(BPDZDir bank, LocalDate statementDate, String monthYear) throws Exception {
        // Get opening balance (from previous statement)
        
        
        // Get all transactions for this bank for the statement period
        List<BPDZPmtA> transactions = repositoryBpdzPta.findByBankAndPeriod(
            bank.getId(),
            statementDate.withDayOfMonth(1),
            statementDate.withDayOfMonth(statementDate.lengthOfMonth())
        );
        
        // Calculate closing balance
        BigDecimal closingBalance;
        BigDecimal openingBalance = calculateOpeningBalance(BigDecimal.ZERO, transactions);
        closingBalance = calculateClosingBalance(openingBalance, transactions);
        
        // Generate CAMT.053 XML
        String camt053Xml = pacsXmlGenerator.generateStatement(
            bank,
            openingBalance,
            closingBalance,
            transactions,
            statementDate
        );
        
        // Save the statement
        saveBankStatement(bank, monthYear, camt053Xml);
    }

    private BigDecimal calculateOpeningBalance(BigDecimal closingBalance, List<BPDZPmtA> transactions) throws Exception {
        BigDecimal balance = closingBalance;
        // Reverse transactions to calculate opening balance
        for (BPDZPmtA tx : transactions) {
        	 String xml=tx.getXmlFile();
         	String pacsType = pacsXmlParser.detectPacsType(xml);
         	BigDecimal amount=null ;
         	
         	if ("pacs009".equals(pacsType)) {
                 Pacs009DTO dto = pacsXmlParser.parsePacs009Xml(xml);
                 amount = dto.getAmount();  // ✅ assign amount
                 balance = balance.subtract(amount);
                
             } else if ("pacs008".equals(pacsType)) {
                 Pacs008DTO dto = pacsXmlParser.parsePacs008Xml(xml);
                 amount = dto.getAmount();  // ✅ assign amount
                 balance = balance.subtract(amount);
                
             }
                    	
          
        }
        return balance;
    }

    private BigDecimal calculateClosingBalance(BigDecimal openingBalance, List<BPDZPmtA> transactions) throws Exception {
        BigDecimal balance = openingBalance;
        for (BPDZPmtA tx : transactions) {
       	 String xml=tx.getXmlFile();
        	String pacsType = pacsXmlParser.detectPacsType(xml);
        	BigDecimal amount=null ;
        	
        	if ("pacs009".equals(pacsType)) {
                Pacs009DTO dto = pacsXmlParser.parsePacs009Xml(xml);
                amount = dto.getAmount();  // ✅ assign amount
                balance = balance.add(amount);
               
            } else if ("pacs008".equals(pacsType)) {
                Pacs008DTO dto = pacsXmlParser.parsePacs008Xml(xml);
                amount = dto.getAmount();  // ✅ assign amount
                balance = balance.add(amount);
               
            }
                   	
         
       }
        return balance;
    }

    private void saveBankStatement(BPDZDir bank, String monthYear, String xmlContent) {
    // Create new BPDZmt movement
    BPDZmt movement = new BPDZmt();
    
    // Set movement properties
    movement.setStatutMouvement("99"); // Initial status
    movement.setNatureMouvement("CAMT.053");
    movement.setTypeMessage("Camt.053");
    movement.setModeTransmission("RTGS"); // Or whatever transmission mode is appropriate
    movement.setDateCreation(LocalDateTime.now());
    movement.setInformationsMessage("Bank statement for " + bank.getBic() + " - " + monthYear);
    
    // Set bank (assuming this is the emitting bank)
    movement.setBanqueEmetteur(bank);
    
    // Set XML content
    movement.setXmlFile(xmlContent);
    
     // Save to repository
    repositoryBpdzmt.save(movement);
       


        String filename = String.format("CAMT053_%s_%s.xml", bank.getBic(), monthYear);
        //  here u have to add the logic of sending it to the web we test by it
    }
}

