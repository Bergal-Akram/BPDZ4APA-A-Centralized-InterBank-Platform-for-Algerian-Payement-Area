package com.Bank.BPDZ.Service;

import jakarta.transaction.Transactional;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;

import org.antlr.runtime.BufferedTokenStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.Bank.BPDZ.DTO.Pacs002DTO;
import com.Bank.BPDZ.DTO.Pacs003DTO;
import com.Bank.BPDZ.DTO.Pacs004DTO;
import com.Bank.BPDZ.DTO.Pacs008DTO;
import com.Bank.BPDZ.DTO.Pacs009DTO;
import com.Bank.BPDZ.DTO.PacsXmlGenerator;
import com.Bank.BPDZ.DTO.PacsXmlParser;
import com.Bank.BPDZ.Entity.BPDZCptBcDCA;
import com.Bank.BPDZ.Entity.BPDZDir;
import com.Bank.BPDZ.Entity.BPDZHabi;
import com.Bank.BPDZ.Entity.BPDZPret;

import com.Bank.BPDZ.Entity.BPDZmt;
import com.Bank.BPDZ.Repository.RepositoryBpdzCptbcdca;
import com.Bank.BPDZ.Repository.RepositoryBpdzDir;
import com.Bank.BPDZ.Repository.RepositoryBpdzFraude;
import com.Bank.BPDZ.Repository.RepositoryBpdzHbi;
import com.Bank.BPDZ.Repository.RepositoryBpdzMandat;
import com.Bank.BPDZ.Repository.RepositoryBpdzPret;
import com.Bank.BPDZ.Repository.RepositoryBpdzPta;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import com.Bank.BPDZ.Repository.RepositoryBpdzmt;

import java.io.StringWriter;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.xml.sax.InputSource;


@Service
public class PacsService {

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
    private SavePacsService savePacsService ;
    @Autowired
    private PacsXmlGenerator pacsXmlGenerator;
    @Autowired
    private PacsXmlParser pacsXmlParser;
    private final double taxRate = 0.05;
    
   
    //------------------------------------------------------------jeutContablePacs-------------------------------------------------------------------------------   
    public String jeutContablePacs (BigDecimal cach ,BigDecimal totalDebit,BigDecimal tax,BPDZCptBcDCA courantReciver
    		                        ,BPDZCptBcDCA courantSender,BPDZCptBcDCA reserveSender ) {
         BPDZDir senderBank = courantSender.getBanque();
    	 Optional<BPDZCptBcDCA> compteplatform= repositoryBpdzCptbcdca.findById(1L);
         BigDecimal amount = courantSender.getSolde().subtract(totalDebit);
         BigDecimal amountt = reserveSender.getSolde().subtract(totalDebit);
         if (amount.compareTo(BigDecimal.ZERO) >= 0) {
        	 courantSender.setSolde(amount);
        	 courantReciver.setSolde(courantReciver.getSolde().add(cach));
        	 compteplatform.get().setSolde(compteplatform.get().getSolde().add(tax));       	 
        	 repositoryBpdzCptbcdca.save(courantSender);
        	 repositoryBpdzCptbcdca.save(courantReciver);
        	 repositoryBpdzCptbcdca.save(compteplatform.get());
        	 System.out.println("return courant");
        	 return "courant";
         }
         else {
        	 if(amountt.compareTo(BigDecimal.ZERO) >= 0) {
        		 reserveSender.setSolde(amountt);
        		 courantReciver.setSolde(courantReciver.getSolde().add(cach));
        		 compteplatform.get().setSolde(compteplatform.get().getSolde().add(tax));
        		 repositoryBpdzCptbcdca.save(reserveSender);
            	 repositoryBpdzCptbcdca.save(courantReciver);
            	 repositoryBpdzCptbcdca.save(compteplatform.get());
            	 System.out.println("return reserve");
            	 return "reserve";}
        	 else {
        		 if (senderBank.getPret()==true) {
        	             // Inject double the amount as loan
        	             BigDecimal loanAmount = cach.multiply(BigDecimal.valueOf(2));
        	             reserveSender.setSolde(reserveSender.getSolde().add(loanAmount));
        	             repositoryBpdzCptbcdca.save(reserveSender);
        	             // Create and save loan record in BPDZPret
        	             BPDZPret loan = new BPDZPret();
        	             loan.setBanque(senderBank);  // Sender bank ID (the one receiving the loan)
        	             loan.setMontantPret(loanAmount);
        	             loan.setDatePret(LocalDate.now());  // Current date as loan date
        	             repositoryBpdzPret.save(loan);
        	             // Save loan record in BPDZPret
        	             reserveSender.setSolde(reserveSender.getSolde().subtract(totalDebit));
        	        	 courantReciver.setSolde(courantReciver.getSolde().add(cach));
        	        	 compteplatform.get().setSolde(compteplatform.get().getSolde().subtract(loanAmount).add(tax));
        	        	 repositoryBpdzCptbcdca.save(reserveSender);
                    	 repositoryBpdzCptbcdca.save(courantReciver);
                    	 repositoryBpdzCptbcdca.save(compteplatform.get());
                    	 System.out.println("return pret");
                    	 return "pret";}
              	 }
              }
          return null;
      }         
    //------------------------------------------------------------processPayment-------------------------------------------------------------------------------   
    public void processPayment(BPDZmt originalMovement ,BPDZHabi habi) throws Exception {
        // --- FRAUD CONTROL PHASE (NEW) ---
    	String xml = originalMovement.getXmlFile();
    	String pacsType = pacsXmlParser.detectPacsType(xml);
    	 BigDecimal amount;
    	 String dbtName=null;
    	 String crtName=null;
    	 
    	if ("pacs009".equals(pacsType)) {
            Pacs009DTO dto = pacsXmlParser.parsePacs009Xml(xml);
            amount = dto.getAmount();  // ✅ assign amount
            dbtName=dto.getDebtorName();
            crtName=dto.getCreditorName();
        } else if ("pacs008".equals(pacsType)) {
            Pacs008DTO dto = pacsXmlParser.parsePacs008Xml(xml);
            amount = dto.getAmount();  // ✅ assign amount
        } else if ("pacs003".equals(pacsType)) {
            Pacs003DTO dto = pacsXmlParser.parsePacs003Xml(xml);

            if (!repositoryBpdzMandat.existsByBanque(repositoryBpdzDir.findByBic(dto.getInstructedAgentBic()))) {
                // 1. Generate the rejection (e.g., save to DB, notify user, etc.)
                generateRejection(originalMovement, "MD01", "Mandate ID not found in system");
                // 2. Then stop the execution by throwing an exception
                throw new Exception("MD01"); // Or a custom exception class
            }
            amount = dto.getAmount();
        }
            else if ("pacs004".equals(pacsType)) {
            Pacs004DTO dto = pacsXmlParser.parsePacs004Xml(xml);
            amount = dto.getReturnedAmount();  // ✅ assign amount
        }else {
            throw new IllegalArgumentException("Unknown PACS type in XML");
        }
        String fraudCheckResult = serviceMADU.fraudCheck(originalMovement);
        
        BPDZDir senderBank = null;
        BPDZDir receiverBank = null;

        if (originalMovement != null &&
            originalMovement.getBanqueEmetteur() != null &&
            originalMovement.getBanqueEmetteur().getBic() != null) {
            senderBank = repositoryBpdzDir.findByBic(originalMovement.getBanqueEmetteur().getBic());
        }

        if (originalMovement != null &&
            originalMovement.getBanqueRecepteur() != null &&
            originalMovement.getBanqueRecepteur().getBic() != null) {
            receiverBank = repositoryBpdzDir.findByBic(originalMovement.getBanqueRecepteur().getBic());
        }
        if (fraudCheckResult!=null) {
            generateRejection(originalMovement, "FR01", fraudCheckResult);
            originalMovement.setStatutMouvement("00");
       	    repositoryBpdzmt.save(originalMovement);
           
        }   
        else{
        	if ((senderBank==null||receiverBank==null||!senderBank.getBic().equals(habi.getBanque().getBic())
                   ))
        		    {
        		    originalMovement.setStatutMouvement("93");  		
            	    repositoryBpdzmt.save(originalMovement);
        	 generateRejection(originalMovement, "FF01", "Missing or invalid bank references");
              }
        	
        	 else if( validateAmount(amount)) {
             BigDecimal cach = amount;
             BigDecimal tax = calculateTax(cach);
             BigDecimal totalDebit = cach.add(tax);
             BPDZCptBcDCA  courantReciver = repositoryBpdzCptbcdca.findByBanqueAndNatureCompteIgnoreCase(receiverBank, "Courant");
             BPDZCptBcDCA  reserveSender = repositoryBpdzCptbcdca.findByBanqueAndNatureCompteIgnoreCase(senderBank, "Reserve");
             BPDZCptBcDCA  reserveReciver = repositoryBpdzCptbcdca.findByBanqueAndNatureCompteIgnoreCase(receiverBank, "Reserve");
             BPDZCptBcDCA  courantSender = repositoryBpdzCptbcdca.findByBanqueAndNatureCompteIgnoreCase(senderBank, "Courant");
             
             String state;

             if ("pacs004".equals(pacsType)) {
                 state = jeutContablePacs(cach, totalDebit, tax, courantSender, courantReciver, reserveReciver);
             } else {
                 state = jeutContablePacs(cach, totalDebit, tax, courantReciver, courantSender, reserveSender);
             }

             if ("courant".equals(state)) { 
            	 sendWebHock(originalMovement);
            	 originalMovement.setStatutMouvement("00");         	 
            	 repositoryBpdzmt.save(originalMovement);
                 generateAcceptance(originalMovement, "ACCP", "Payment processed from current account");          
             }
             // 2. Try Reserve Account 
             else if ("reserve".equals(state)){
            	 originalMovement.setStatutMouvement("00"); 
            	 sendWebHock(originalMovement);
            	 repositoryBpdzmt.save(originalMovement);
                 generateAcceptance(originalMovement, "ACCP", "Payment processed from reserve account");            
             }
              // 3. Check Automatic Loan
             else if ("pret".equals(state)){  
            	 originalMovement.setStatutMouvement("02");  
            	 sendWebHock(originalMovement);
            	 repositoryBpdzmt.save(originalMovement);
            	 System.out.println("state pret");
                 generateAcceptance(originalMovement, "PART", "Payment processed with automatic loan");   
             } 
                // 4. Reject Payment
             else { System.out.println("state null");
            	 generateRejection(originalMovement, "AM04", "Insufficient funds in all accounts");} 
        	 }      
        }    
}
    //----------------------------------------------------------------calculate-------------------------------------------------------------------
    public BigDecimal calculateTax(BigDecimal amount) {
        return amount.multiply(BigDecimal.valueOf(taxRate));
    }
    private boolean validateAmount(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            return false;
        }
        return true;
    }
//--------------------------------------------------------------------------------------------------------------------------------------
private void generateAcceptance(BPDZmt originalMovement, String status, String message)throws Exception {
	 System.out.println("start generateAcceptance");
    Pacs002DTO response = new Pacs002DTO();
    String xml = originalMovement.getXmlFile();
   	String pacsType = pacsXmlParser.detectPacsType(xml);
    if ("pacs009".equals(pacsType)) {
        Pacs009DTO dto = pacsXmlParser.parsePacs009Xml(xml);
        response.setOriginalAmount(dto.getAmount());
        response.setOriginalCurrency(dto.getCurrency());
        response.setOriginalMessageId(dto.getInstructionId());
        response.setOriginalEndToEndId(dto.getEndToEndId());
        // ✅ assign amount
    } else if ("pacs008".equals(pacsType)) {
        Pacs008DTO dto = pacsXmlParser.parsePacs008Xml(xml);
        response.setOriginalAmount(dto.getAmount());
        response.setOriginalCurrency(dto.getCurrency());
        response.setOriginalMessageId(dto.getInstructionId());
        response.setOriginalEndToEndId(dto.getEndToEndId());
          // ✅ assign amount
    }else if ("pacs003".equals(pacsType)) {
        Pacs003DTO dto = pacsXmlParser.parsePacs003Xml(xml);
        response.setOriginalAmount(dto.getAmount());
        response.setOriginalCurrency(dto.getCurrency());
        response.setOriginalMessageId(dto.getMessageId());
        response.setOriginalEndToEndId(dto.getEndToEndId());
        // ✅ assign amount
    } else if ("pacs004".equals(pacsType)) {
        Pacs004DTO dto = pacsXmlParser.parsePacs004Xml(xml);
        response.setOriginalAmount(dto.getReturnedAmount());
        response.setOriginalCurrency(dto.getCurrency());
        response.setOriginalEndToEndId(dto.getOriginalEndToEndId());
        response.setOriginalMessageId(dto.getOriginalMessageId());
       
    }else {
        throw new IllegalArgumentException("Unknown PACS type in XML");
    }
	
   
    response.setStatus(status);
    response.setAdditionalInformation(message);
    response.setAdditionalInformation("null");
  

    
    String responseXml = pacsXmlGenerator.generatePacs002(response);
    savePacs002Movement(originalMovement,response, responseXml, "90", "00");
    System.out.println(" end generateAcceptance");
}
//generate pacs 002 refu
//----------------------------------------------------------------------------------------------------
private void generateRejection(BPDZmt originalMovement, String reasonCode, String reason)throws Exception {
    Pacs002DTO rejection = new Pacs002DTO();
    String xml = originalMovement.getXmlFile();
	String pacsType = pacsXmlParser.detectPacsType(xml);
	
	if ("pacs009".equals(pacsType)) {
        Pacs009DTO dto = pacsXmlParser.parsePacs009Xml(xml);
        rejection.setOriginalAmount(dto.getAmount());
        rejection.setOriginalCurrency(dto.getCurrency());
        rejection.setOriginalMessageId(dto.getInstructionId());
        // ✅ assign amount
    } else if ("pacs008".equals(pacsType)) {
        Pacs008DTO dto = pacsXmlParser.parsePacs008Xml(xml);
        rejection.setOriginalAmount(dto.getAmount());
        rejection.setOriginalCurrency(dto.getCurrency());
        rejection.setOriginalMessageId(dto.getInstructionId());
          // ✅ assign amount
    }else if ("pacs003".equals(pacsType)) {
        Pacs003DTO dto = pacsXmlParser.parsePacs003Xml(xml);
        rejection.setOriginalAmount(dto.getAmount());
        rejection.setOriginalCurrency(dto.getCurrency());
        rejection.setOriginalMessageId(dto.getMessageId());
        // ✅ assign amount
    } else if ("pacs004".equals(pacsType)) {
        Pacs004DTO dto = pacsXmlParser.parsePacs004Xml(xml);
        rejection.setOriginalAmount(dto.getReturnedAmount());
        rejection.setOriginalCurrency(dto.getCurrency());
        rejection.setOriginalMessageId(dto.getOriginalMessageId());
       
    }else {
        throw new IllegalArgumentException("Unknown PACS type in XML");
    }
	
    rejection.setStatus("RJCT");
    rejection.setReasonCode(reasonCode);
    rejection.setReasonText(reason);
    
    
    String rejectionXml = pacsXmlGenerator.generatePacs002(rejection);
    //savePacs002Movement(originalMovement, rejectionXml, "RJCT", "REJECTED");
    savePacs002Movement(originalMovement,rejection, rejectionXml, "91", "Pacs.002");
    System.out.println("kkkkkkkkkkkkkkkkkkkkkkkkkkkkkk");
}
//accept
//----------------------------------------------------------------------------------------------------
private void savePacs002Movement(BPDZmt originalMovement,Pacs002DTO dto, String xmlContent, String status, String nature)throws Exception {
	  System.out.println("start save ");
    BPDZmt responseMovement = new BPDZmt();
    responseMovement.setTypeMessage("Pacs.002");
    responseMovement.setStatutMouvement(status);
    responseMovement.setNatureMouvement(nature);
    responseMovement.setXmlFile(xmlContent);
    responseMovement.setInformationsMessage(savePacsService.buildPacs002Info(dto));
    responseMovement.setBanqueEmetteur(originalMovement.getBanqueRecepteur()); // Reversed
    responseMovement.setBanqueRecepteur(originalMovement.getBanqueEmetteur());
    responseMovement.setDateCreation(LocalDateTime.now());
    responseMovement.setDateTraitement(LocalDateTime.now());
    
    repositoryBpdzmt.save(responseMovement);
    sendWebHock(responseMovement);
    System.out.println("end save ");
}
//-----------------------------------------------------------download pacs------------------------------------------------------------------------------
public ResponseEntity<Resource> generateXmlDownload(String xmlContent, String filename) {
    try {
        // Convert XML string to bytes
        byte[] xmlBytes = xmlContent.getBytes(StandardCharsets.UTF_8);
        ByteArrayResource resource = new ByteArrayResource(xmlBytes);

        // Ensure file name ends with .xml
        if (!filename.toLowerCase().endsWith(".xml")) {
            filename += ".xml";
        }

        // Build headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_XML);
        headers.setContentDisposition(ContentDisposition
                .attachment()
                .filename(filename)
                .build());

        // Return file download
        return ResponseEntity.ok()
                .headers(headers)
                .body(resource);

    } catch (Exception e) {
        return ResponseEntity.internalServerError().build();
    }
} 
//---------------------------------------------------------------------check validate mandat-------------------------------------------------------------------------------


//----------------------------------------------------------------------send webHock-----------------------------------------------------------------------------
public void sendWebHock (BPDZmt mouv) {
	 String xmlData = mouv.getXmlFile();
     // Replace with your actual webhook.site URL
     String url = "https://webhook.site/e20bd08b-3cba-4510-92f0-b2467cca102a"; // ← Use your real one here
     // Prepare headershttps://webhook.site/#!/view/e20bd08b-3cba-4510-92f0-b2467cca102a/bc668753-6d30-4bf5-9763-66bd1beb1a44/1
     HttpHeaders headers = new HttpHeaders();
     headers.setContentType(MediaType.APPLICATION_XML);

     // Create the HTTP request
     HttpEntity<String> request = new HttpEntity<>(xmlData, headers);

     // Send the POST request
     RestTemplate restTemplate = new RestTemplate();
     ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
	
}
}