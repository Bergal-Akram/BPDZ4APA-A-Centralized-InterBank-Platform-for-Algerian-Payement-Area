package com.Bank.BPDZ.Service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.Bank.BPDZ.DTO.Pacs002DTO;
import com.Bank.BPDZ.DTO.Pacs003DTO;
import com.Bank.BPDZ.DTO.Pacs004DTO;
import com.Bank.BPDZ.DTO.Pacs008DTO;
import com.Bank.BPDZ.DTO.Pacs009DTO;
import com.Bank.BPDZ.DTO.PacsXmlGenerator;
import com.Bank.BPDZ.DTO.PacsXmlParser;
import com.Bank.BPDZ.Entity.BPDZDir;
import com.Bank.BPDZ.Entity.BPDZmt;
import com.Bank.BPDZ.Repository.RepositoryBpdzCptbcdca;
import com.Bank.BPDZ.Repository.RepositoryBpdzDir;
import com.Bank.BPDZ.Repository.RepositoryBpdzFraude;
import com.Bank.BPDZ.Repository.RepositoryBpdzHbi;
import com.Bank.BPDZ.Repository.RepositoryBpdzMandat;
import com.Bank.BPDZ.Repository.RepositoryBpdzPret;
import com.Bank.BPDZ.Repository.RepositoryBpdzPta;

import com.Bank.BPDZ.Repository.RepositoryBpdzmt;

import jakarta.transaction.Transactional;
@Service
public class SavePacsService {
	@Autowired
	private RepositoryBpdzHbi repositoryBpdzHbi;
    @Autowired
    private RepositoryBpdzDir repositoryBpdzDir;
    @Autowired
    private RepositoryBpdzCptbcdca repositoryBpdzCptbcdca;
    @Autowired
    private RepositoryBpdzMandat repositoryBpdzMandat;
    @Autowired
    private RepositoryBpdzFraude repositoryBpdzFraude;
    @Autowired
    private RepositoryBpdzPret repositoryBpdzPret;
    @Autowired
    private RepositoryBpdzPta repositoryBpdzPta;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private RepositoryBpdzmt repositoryBpdzmt;
   
    @Autowired
    private ServiceHabi serviceHabi ;
    @Autowired
    private ServiceMADU serviceMADU ;
    @Autowired
    private PacsXmlGenerator pacsXmlGenerator;
    @Autowired
    private PacsXmlParser pacsXmlParser;
    private final double taxRate = 0.05;
    private  PacsXmlGenerator xmlGenerator = new PacsXmlGenerator();
    //---------------------------------------------------------------savePacs.008--------------------------------------------------------------------------
    @Transactional
    public BPDZmt savePacs008(Pacs008DTO pacs008DTO) throws Exception  {
        // Find banks by BIC
        Optional<BPDZDir> senderBank = Optional.ofNullable(repositoryBpdzDir.findByBic(pacs008DTO.getSenderBic()));
        Optional<BPDZDir> receiverBank = Optional.ofNullable(repositoryBpdzDir.findByBic(pacs008DTO.getReceiverBic()));
        BPDZmt movement = new BPDZmt();
        if (senderBank.isPresent()) {
           movement.setBanqueEmetteur(senderBank.get());
        }
        if (receiverBank.isPresent()) {
             movement.setBanqueRecepteur(receiverBank.get());}
        // Generate XML
        try {
            String xmlContent = xmlGenerator.generatePacs008Xml(pacs008DTO);
            movement.setXmlFile(xmlContent);
        } catch (Exception e) {
        	 movement.setStatutMouvement("91");
            throw new RuntimeException("Failed to generate PACS.008 XML", e);
        }
      // Create and save movement     
        movement.setStatutMouvement("99"); // Initial status
        movement.setNatureMouvement("PACS.008 ");
        movement.setTypeMessage("Pacs.008");
        movement.setModeTransmission(pacs008DTO.getTransmissionMode());
        movement.setDateCreation(LocalDateTime.now());
        movement.setInformationsMessage(buildPacs008Info(pacs008DTO));
             
        return repositoryBpdzmt.save(movement);
    }
    private String buildPacs008Info(Pacs008DTO dto) {
        return String.format(
            "Instruction ID: %s, EndToEnd ID: %s, Amount: %s %s, From: %s (%s), To: %s (%s)",
            dto.getInstructionId(),
            dto.getEndToEndId(),
            dto.getAmount(),
            dto.getCurrency(),
            dto.getDebtorName(),
            dto.getSenderBic(),
            dto.getCreditorName(),
            dto.getReceiverBic()
        );
    }
    //---------------------------------------------------------------save pacs.009------------------------------------------------------------------------
    public BPDZmt savePacs009(Pacs009DTO pacs009DTO) throws Exception {
        // Find banks by BIC
        Optional<BPDZDir> senderBank = Optional.ofNullable(repositoryBpdzDir.findByBic(pacs009DTO.getSenderBic()));
        Optional<BPDZDir> receiverBank = Optional.ofNullable(repositoryBpdzDir.findByBic(pacs009DTO.getReceiverBic()));

        // Create movement first since we need it for bank assignment
        BPDZmt movement = new BPDZmt();
        movement.setStatutMouvement("99"); // Initial status
        movement.setNatureMouvement("PACS.009");
        movement.setTypeMessage("Pacs.009");
        movement.setModeTransmission(pacs009DTO.getTransmissionMode());
        movement.setDateCreation(LocalDateTime.now());
        
        // Set banks if found
        senderBank.ifPresent(movement::setBanqueEmetteur);
        receiverBank.ifPresent(movement::setBanqueRecepteur);

        // Generate XML after banks are set
        String xmlContent;
        try {
            xmlContent = xmlGenerator.generatePacs009Xml(pacs009DTO);
        } catch (Exception e) {
        	 movement.setStatutMouvement("91");
            throw new RuntimeException("Failed to generate PACS.009 XML", e);
        }

        // Set remaining fields
        movement.setInformationsMessage(buildPacs009Info(pacs009DTO));
        movement.setXmlFile(xmlContent);
        
        return repositoryBpdzmt.save(movement);
    }

    private String buildPacs009Info(Pacs009DTO dto) {
        return String.format(
            "Message ID: %s | Instruction ID: %s | Amount: %s %s | Settlement: %s%n" +
            "Debtor: %s (%s) | Creditor: %s (%s)",
            dto.getMessageId(),
            dto.getInstructionId(),
            dto.getAmount(),
            dto.getCurrency(),
            dto.getSettlementMethod(),
            dto.getDebtorName(),
            dto.getSenderBic(),
            dto.getCreditorName(),
            dto.getReceiverBic()
        );
    }
    //------------------------------------------------------------save Pacs.003-------------------------------------------------------------------------
    public BPDZmt savePacs003(Pacs003DTO pacs003DTO) {
        // Find banks by BIC
        Optional<BPDZDir> instructingAgentBank = Optional.ofNullable(repositoryBpdzDir.findByBic(pacs003DTO.getInstructingAgentBic()));
        Optional<BPDZDir> instructedAgentBank = Optional.ofNullable(repositoryBpdzDir.findByBic(pacs003DTO.getInstructedAgentBic()));

        // Create and populate movement entity
        BPDZmt movement = new BPDZmt();
        movement.setStatutMouvement("99"); // Initial status
        movement.setNatureMouvement("PACS.003");
        movement.setTypeMessage("Pacs.003");
        movement.setModeTransmission(pacs003DTO.getTransmissionMode());
        movement.setDateCreation(LocalDateTime.now());
        movement.setInformationsMessage(buildPacs003Info(pacs003DTO));

        // Set bank relationships
        instructingAgentBank.ifPresent(movement::setBanqueEmetteur);
        instructedAgentBank.ifPresent(movement::setBanqueRecepteur);

        // Generate and set XML
        try {
            String xmlContent = xmlGenerator.generatePacs003Xml(pacs003DTO);
            movement.setXmlFile(xmlContent);
        } catch (Exception e) {
        	 movement.setStatutMouvement("91");
            throw new RuntimeException("Failed to generate PACS.003 XML", e);
        }

        return repositoryBpdzmt.save(movement);
    }

    private String buildPacs003Info(Pacs003DTO dto) {
        return String.format(
            "Message ID: %s | Transactions: %d | Total Amount: %s %s%n" +
            "Instructing Agent: %s | Instructed Agent: %s%n" +
            "EndToEnd ID: %s | Amount: %s %s%n" +
            "Debtor: %s (%s) | Creditor: %s (%s)%n" +
            "Remittance Info: %s",
            dto.getMessageId(),
            dto.getNumberOfTransactions(),
            dto.getControlSum(),
            dto.getCurrency(),
            dto.getInstructingAgentBic(),
            dto.getInstructedAgentBic(),
            dto.getEndToEndId(),
            dto.getAmount(),
            dto.getCurrency(),
            dto.getDebtorName(),
            dto.getDebtorIban(),
            dto.getCreditorName(),
            dto.getCreditorIban(),
            dto.getRemittanceInfo()
        );
    }
//---------------------------------------------------------save Pacs.004---------------------------------------------------------------------------------

    @Transactional
    public BPDZmt savePacs004(Pacs004DTO pacs004DTO) {
        // Find banks by BIC
        Optional<BPDZDir> instructingAgentBank = Optional.ofNullable(repositoryBpdzDir.findByBic(pacs004DTO.getInstructingAgentBic()));
        Optional<BPDZDir> instructedAgentBank = Optional.ofNullable(repositoryBpdzDir.findByBic(pacs004DTO.getInstructedAgentBic()));

        // Create and populate movement entity
        BPDZmt movement = new BPDZmt();
        movement.setStatutMouvement("99"); // Initial status
        movement.setNatureMouvement("PACS.004");
        movement.setTypeMessage("Pacs.004");
        movement.setModeTransmission(pacs004DTO.getTransmissionMode());
        movement.setDateCreation(LocalDateTime.now());
       

        // Set bank relationships
        instructingAgentBank.ifPresent(movement::setBanqueEmetteur);
        instructedAgentBank.ifPresent(movement::setBanqueRecepteur);

        // Generate and set XML
        try {
            String xmlContent = xmlGenerator.generatePacs004Xml(pacs004DTO);
            movement.setInformationsMessage(buildPacs004Info(pacs004DTO));
            movement.setXmlFile(xmlContent);
        } catch (Exception e) {
        	 movement.setStatutMouvement("91");
            throw new RuntimeException("Failed to generate PACS.004 XML", e);
        }

        return repositoryBpdzmt.save(movement);
    }

    private String buildPacs004Info(Pacs004DTO dto) {
        return String.format(
            "Return ID: %s | Original Msg: %s | Reason: %s (%s)%n" +
            "Amount: %s %s | Original Amount: %s %s%n" +
            "Debtor: %s (%s) | Creditor: %s (%s)%n" +
            "Instructing Agent: %s | Instructed Agent: %s%n" +
            "Remittance Info: %s",
            dto.getReturnId(),
            dto.getOriginalMessageId(),
            dto.getReturnReasonCode(),
            dto.getAdditionalInfo(),
            dto.getReturnedAmount(),
            dto.getCurrency(),
            dto.getReturnedInstructedAmount(),
            dto.getCurrency(),
            dto.getDebtorName(),
            dto.getDebtorIban(),
            dto.getCreditorName(),
            dto.getCreditorIban(),
            dto.getInstructingAgentBic(),
            dto.getInstructedAgentBic(),
            dto.getRemittanceInformation()
        );
    }
//-------------------------------------------------------------save Pacs.002--------------------------------------------------------------------------------
    public BPDZmt savePacs002(Pacs002DTO dto) throws Exception {
        // Look up banks involved, if applicable (e.g. debtor/creditor banks by BIC if available)
       // Optional<BPDZDir> senderBank = Optional.ofNullable(repositoryBpdzDir.findByBic(dto.getSenderBic()));
       // Optional<BPDZDir> receiverBank = Optional.ofNullable(repositoryBpdzDir.findByBic(dto.getReceiverBic()));

        // Create the movement object
        BPDZmt movement = new BPDZmt();
        movement.setStatutMouvement("99"); // Initial status
        movement.setNatureMouvement("PACS.002");
        movement.setTypeMessage("Pacs.002");
        
        movement.setDateCreation(LocalDateTime.now());

        // Set banks if found
       // senderBank.ifPresent(movement::setBanqueEmetteur);
        //receiverBank.ifPresent(movement::setBanqueRecepteur);

        // Generate XML
        String xmlContent;
        try {
            xmlContent = xmlGenerator.generatePacs002(dto);
        } catch (Exception e) {
        	 movement.setStatutMouvement("91");
            throw new RuntimeException("Failed to generate PACS.002 XML", e);
        }

        // Set movement information
        movement.setInformationsMessage(buildPacs002Info(dto));
        movement.setXmlFile(xmlContent);

        return repositoryBpdzmt.save(movement);
    }
    public String buildPacs002Info(Pacs002DTO dto) {
        return String.format(
            "Original Msg ID: %s | EndToEnd ID: %s | Status: %s | Reason: %s - %s | Amount: %s %s",
            dto.getOriginalMessageId(),
            dto.getOriginalEndToEndId(),
            dto.getStatus(),
            dto.getReasonCode(),
            dto.getReasonText(),
            dto.getOriginalAmount(),
            dto.getOriginalCurrency()
        );
    }
    

}
