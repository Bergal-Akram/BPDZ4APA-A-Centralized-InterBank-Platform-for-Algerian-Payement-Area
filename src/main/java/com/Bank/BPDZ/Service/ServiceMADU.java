package com.Bank.BPDZ.Service;

import java.io.StringReader;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.w3c.dom.NodeList;

import com.Bank.BPDZ.DTO.Pacs003DTO;
import com.Bank.BPDZ.DTO.Pacs004DTO;
import com.Bank.BPDZ.DTO.Pacs008DTO;
import com.Bank.BPDZ.DTO.Pacs009DTO;
import com.Bank.BPDZ.DTO.PacsXmlParser;
import com.Bank.BPDZ.Entity.BPDZCptBcDCA;
import com.Bank.BPDZ.Entity.BPDZDir;
import com.Bank.BPDZ.Entity.BPDZFraude;
import com.Bank.BPDZ.Entity.BPDZHabi;
import com.Bank.BPDZ.Entity.BPDZMandat;
import com.Bank.BPDZ.Entity.BPDZPret;
import com.Bank.BPDZ.Entity.BPDZSer;
import com.Bank.BPDZ.Entity.BPDZmt;
import com.Bank.BPDZ.Repository.RepositoryBpdzCptbcdca;
import com.Bank.BPDZ.Repository.RepositoryBpdzDir;
import com.Bank.BPDZ.Repository.RepositoryBpdzFraude;
import com.Bank.BPDZ.Repository.RepositoryBpdzHbi;
import com.Bank.BPDZ.Repository.RepositoryBpdzMandat;
import com.Bank.BPDZ.Repository.RepositoryBpdzPret;
import com.Bank.BPDZ.Repository.RepositoryBpdzPta;
import com.Bank.BPDZ.Repository.RepositoryBpdzSer;
import com.Bank.BPDZ.Repository.RepositoryBpdzmt;

import jakarta.transaction.Transactional;

import org.xml.sax.InputSource;



@Service
public class ServiceMADU {
	    @Autowired
     	private PacsXmlParser pacsXmlParser;
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
	    private RepositoryBpdzSer repositoryBpdzSer;
		
	    public boolean deleteHabi(Long id) {
	        if (repositoryBpdzHbi.existsById(id)) {
	            repositoryBpdzHbi.deleteById(id);
	            return true; // ✅ Deleted successfully
	        } else {
	            return false; // ❌ Not found
	        }
	    }

public boolean deletedir(Long id) {
	
	if (repositoryBpdzDir.existsById(id)) {
		repositoryBpdzDir.deleteById(id);
        return true; // ✅ Deleted successfully
    } else {
        return false; // ❌ Not found
    }
}
public boolean deleteCompte(Long id) {
	if (repositoryBpdzCptbcdca.existsById(id)) {
		repositoryBpdzCptbcdca.deleteById(id);
        return true; // ✅ Deleted successfully
    } else {
        return false; // ❌ Not found
    }
}

public boolean deleteService(Long id) {
	if (repositoryBpdzSer.existsById(id)) {
		repositoryBpdzSer.deleteById(id);
        return true; // ✅ Deleted successfully
    } else {
        return false; // ❌ Not found
    }
}
public boolean deleteFraud(Long id) {
	
	if (repositoryBpdzFraude.existsById(id)) {
		repositoryBpdzFraude.deleteById(id);
        return true; // ✅ Deleted successfully
    } else {
        return false; // ❌ Not found
    }
}
public boolean deleteMondat(Long id) {
	if (repositoryBpdzMandat.existsById(id)) {
		repositoryBpdzMandat.deleteById(id);
        return true; // ✅ Deleted successfully
    } else {
        return false; // ❌ Not found
    }
}
public boolean deletePret(Long id) {
	if (repositoryBpdzPret.existsById(id)) {
		repositoryBpdzPret.deleteById(id);
        return true; // ✅ Deleted successfully
    } else {
        return false; // ❌ Not found
    }}
	public boolean deletemovment(Long id) {
		if (repositoryBpdzmt.existsById(id)) {
			repositoryBpdzmt.deleteById(id);
	        return true; // ✅ Deleted successfully
	    } else {
	        return false; // ❌ Not found
	    }
}
	
//-----------------------------------------------------------------------------------------------------

	public void addPret(BPDZPret pret, Long agentId, String bic) {
	    BPDZHabi agent = repositoryBpdzHbi.findById(agentId)
	        .orElseThrow(() -> new RuntimeException("Agent not found"));

	    BPDZDir banque = repositoryBpdzDir.findByBic(bic);
	    if (banque == null) {
	        throw new RuntimeException("Banque not found for BIC: " + bic);
	    }

	    // Check if pret already exists for this agent, amount, and date
	    Optional<BPDZPret> existingPret = repositoryBpdzPret
	        .findByAgentAndMontantPretAndDatePret(agent, pret.getMontantPret(), pret.getDatePret());

	    if (existingPret.isPresent()) {
	        throw new IllegalArgumentException("Un prêt similaire existe déjà pour cet agent à cette date.");
	    }

	    pret.setAgent(agent);
	    pret.setBanque(banque);

	    repositoryBpdzPret.save(pret);
	}

	public void addFraud(BPDZFraude fraude) {
	    // Basic null checks
	    if (fraude.getInformationInterdite() == null || fraude.getInformationInterdite().isBlank()) {
	        throw new IllegalArgumentException("Information interdite ne peut pas être vide.");
	    }

	    if (fraude.getTypeInformation() == null || fraude.getTypeInformation().isBlank()) {
	        throw new IllegalArgumentException("Type d'information est requis.");
	    }

	    // Normalize input (trim and uppercase if needed)
	    //trim :Removes leading and trailing spaces from user input.
	    String info = fraude.getInformationInterdite().trim();
	    String type = fraude.getTypeInformation().trim().toUpperCase();
	    fraude.setInformationInterdite(info);
	    fraude.setTypeInformation(type);

	    // Set default date if not provided
	    if (fraude.getDateInterdiction() == null) {
	        fraude.setDateInterdiction(LocalDate.now());
	    }

	    // Check for duplicates
	    Optional<BPDZFraude> existingFraud = repositoryBpdzFraude
	        .findByInformationInterdite(info);
	    if (existingFraud.isPresent()) {
	        throw new IllegalArgumentException("Cette information est déjà interdite.");
	    }

	    // Save
	    repositoryBpdzFraude.save(fraude);
	}

	public BPDZMandat addMandat(BPDZMandat mandat,String bic, Long compteId) {
        BPDZDir banque = repositoryBpdzDir.findByBic(bic);
        if (bic==null) {
        		throw new IllegalArgumentException("BIC must not be null or empty");}

        BPDZCptBcDCA compte = repositoryBpdzCptbcdca.findById(compteId)
                .orElseThrow(() -> new IllegalArgumentException("Compte not found with ID: " + compteId));

       

        
        mandat.setBanque(banque);
        mandat.setCompte(compte);
      
       

        return repositoryBpdzMandat.save(mandat);
    }
	//-----------------------------------------
	
	public void addDir(BPDZDir dir) {
	    // Normalize input
	    String bic = dir.getBic().trim().toUpperCase();
	    String codeBanque = dir.getCodeBanque().trim().toUpperCase();
	    String nomBanque = dir.getNomBanque().trim();
	    String tel = dir.getTel().trim();
	    String mail = dir.getMail().trim();

	    // Set normalized values back
	   
        if(!checkFraudForAdd(dir)) {
        	 throw new IllegalArgumentException("Alert il existe fraude");
        }
	    // Uniqueness checks
	    if (repositoryBpdzDir.existsByBic(bic)) {
	        throw new IllegalArgumentException("Ce BIC existe déjà.");
	    }

	    if (repositoryBpdzDir.existsByCodeBanque(codeBanque)) {
	        throw new IllegalArgumentException("Ce code banque existe déjà.");
	    }

	    if (repositoryBpdzDir.existsByMail(mail)) {
	        throw new IllegalArgumentException("Cet e-mail est déjà utilisé.");
	    }

	    if (repositoryBpdzDir.existsByTel(tel)) {
	        throw new IllegalArgumentException("Ce numéro de téléphone est déjà utilisé.");
	    }

	    if (repositoryBpdzDir.existsByNomBanque(nomBanque)) {
	        throw new IllegalArgumentException("Ce nom de banque est déjà utilisé.");
	    }
	    dir.setBic(bic);
	    dir.setCodeBanque(codeBanque);
	    dir.setNomBanque(nomBanque);
	    dir.setTel(tel);
	    dir.setMail(mail);
	    

	    // Persist entity
	    repositoryBpdzDir.save(dir);
	}

	
	public void addBPDZHabi(BPDZHabi habi, String bic) {
	    // Trim and normalize fields
	    if (habi.getLogin() != null) habi.setLogin(habi.getLogin().trim());
	    habi.setMotDePasse(habi.getMotDePasse().trim());
	    habi.setAdresseIP(habi.getAdresseIP().trim());
	    habi.setNom(habi.getNom().trim());
	    habi.setPrenom(habi.getPrenom().trim());

	    // Check required fields
	    if (habi.getLogin() == null || habi.getLogin().isEmpty() ||
	        habi.getMotDePasse() == null || habi.getMotDePasse().isEmpty() ||
	        habi.getAdresseIP() == null || habi.getAdresseIP().isEmpty() ||
	        habi.getNom() == null || habi.getNom().isEmpty() ||
	        habi.getPrenom() == null || habi.getPrenom().isEmpty()) {
	        throw new IllegalArgumentException("Tous les champs obligatoires doivent être remplis.");
	    }

	    // Check login uniqueness
	    if (repositoryBpdzHbi.findByLogin(habi.getLogin()) != null) {
	        throw new IllegalArgumentException("Le login est déjà utilisé.");
	    }

	    // Check BIC exists
	    BPDZDir banque = repositoryBpdzDir.findByBic(bic);
	    if (banque == null) {
	        throw new IllegalArgumentException("Le BIC n'existe pas.");
	    }

	    // Validate ports
	    if (habi.getPortReception() < 1 || habi.getPortReception() > 65535) {
	        throw new IllegalArgumentException("Le port de réception est invalide.");
	    }
	    if (habi.getPortReceptionSecours() != null &&
	        (habi.getPortReceptionSecours() < 1 || habi.getPortReceptionSecours() > 65535)) {
	        throw new IllegalArgumentException("Le port de réception secours est invalide.");
	    }

	    // Validate IP
	   /* if (!habi.getAdresseIP().matches("\\b(?:\\d{1,3}\\.){3}\\d{1,3}\\b")) {
	        throw new IllegalArgumentException("Adresse IP invalide.");
	    }*/

	    // Set encoded password and associated bank
	    habi.setMotDePasse(passwordEncoder.encode(habi.getMotDePasse()));
	    habi.setPermissions(passwordEncoder.encode(habi.getPermissions()));
	    habi.setAdresseipsecours(habi.getAdresseipsecours());
	    
	    habi.setBanque(banque);

	    // Save
	    repositoryBpdzHbi.save(habi);
	}
	
	
	
	public void addBPDZCptBcDCA(BPDZCptBcDCA cpt, String bic) {
	    // Trim and basic checks
	    if (cpt.getNumeroCompte() == null || cpt.getNumeroCompte().trim().isEmpty()) {
	        throw new IllegalArgumentException("Le numéro de compte est obligatoire.");
	    }

	    cpt.setNumeroCompte(cpt.getNumeroCompte().trim());

	    if (repositoryBpdzCptbcdca.findByNumeroCompte(cpt.getNumeroCompte()) != null) {
	        throw new IllegalArgumentException("Le numéro de compte est déjà utilisé.");
	    }
	    BPDZDir banque = repositoryBpdzDir.findByBic(bic);
	    if (banque == null) {
	        throw new IllegalArgumentException("Banque avec le BIC donné n'existe pas.");
	    }
	    if (repositoryBpdzCptbcdca.existsByBanqueAndNatureCompte(banque, cpt.getNatureCompte())) {
	        throw new IllegalArgumentException("Un compte de type '" + cpt.getNatureCompte() + "' existe déjà pour cette banque.");
	    }

	    // Validate banque (bank must exist)

	    cpt.setBanque(banque);

	    // Set default values if not already set
	    if (cpt.getDevise() == null || cpt.getDevise().isBlank()) {
	        cpt.setDevise("DA");
	    }

	    if (cpt.getDateSolde() == null) {
	        cpt.setDateSolde(LocalDate.now());
	    }

	    if (cpt.getSolde() == null) {
	        cpt.setSolde(BigDecimal.ZERO);
	    }

	    // Let @PrePersist handle validation of natureCompte
	    repositoryBpdzCptbcdca.save(cpt);
	}
	
	
	@Transactional
    public void updateCompte(BPDZCptBcDCA compteForm, String bic) {
        Optional<BPDZCptBcDCA> opt = repositoryBpdzCptbcdca.findById(compteForm.getId());
        if (opt.isPresent()) {
            BPDZCptBcDCA compte = opt.get();
            BPDZDir banque = repositoryBpdzDir.findByBic(bic);
            compte.setBanque(banque);
            compte.setNatureCompte(compteForm.getNatureCompte());
            compte.setNumeroCompte(compteForm.getNumeroCompte());
            compte.setSolde(compteForm.getSolde());
            compte.setDateSolde(compteForm.getDateSolde());

            repositoryBpdzCptbcdca.save(compte);
        } else {
            throw new RuntimeException("Compte introuvable");
        }
    }
	 public void updatehabi(BPDZHabi habiForm, String bic) {
	        Optional<BPDZHabi> opt = repositoryBpdzHbi.findById(habiForm.getId());
	        BPDZDir banque=null;
	         banque = repositoryBpdzDir.findByBic(bic);
	        if (opt.isPresent() && banque!=null) {
	            BPDZHabi compte = opt.get();
	           
	            compte.setBanque(banque);
	            compte.setPermissions(passwordEncoder.encode(compte.getPermissions()));
	            compte.setLogin(habiForm.getLogin());
	            compte.setMotDePasse(passwordEncoder.encode(habiForm.getMotDePasse()));
	            compte.setNom(habiForm.getNom());
	            compte.setRole(habiForm.getRole());;
	            compte.setPrenom(habiForm.getPrenom());
	            compte.setAdresseIP(habiForm.getAdresseIP());
	            compte.setAdresseipsecours(habiForm.getAdresseipsecours());
	            compte.setPortReception(habiForm.getPortReception());
	            compte.setPortReceptionSecours(habiForm.getPortReceptionSecours());
	            
	            

	            repositoryBpdzHbi.save(compte);
	        } else {
	            throw new RuntimeException("Compte introuvable");
	        }
	    }
	 
	 public void updateSer(BPDZSer SerForm, String bic) {
	        Optional<BPDZSer> opt = repositoryBpdzSer.findById(SerForm.getId());
	        BPDZDir banque=null;
	         banque = repositoryBpdzDir.findByBic(bic);
	        if (opt.isPresent() && banque!=null) {
	            BPDZSer compte = opt.get();
	           
	            compte.setBanque(banque);
	            compte.setNomService(SerForm.getNomService());
	            compte.setTypeMessage(SerForm.getTypeMessage());
	            
	            

	            repositoryBpdzSer.save(compte);
	        } else {
	            throw new RuntimeException("Compte introuvable");
	        }
	    }

	 public void updateMov(BPDZmt Mov) {
	        Optional<BPDZmt> opt = repositoryBpdzmt.findById(Mov.getId());
	        
	        if (opt.isPresent() && Mov!=null) {
	            BPDZmt compte = opt.get();
	           
	            compte.setStatutMouvement(Mov.getStatutMouvement());
	            //compte.setTypeMessage(Mov.getTypeMessage());
	            

	            repositoryBpdzmt.save(compte);
	        } else {
	            throw new RuntimeException("Compte introuvable");
	        }
	    }
	 
	 public String fraudCheck(BPDZmt Mov) throws Exception {
		    String xml = Mov.getXmlFile(); // This is your stored PACS.008-like XML
            
		    String creditorName=null;
		    String debtorName=null;
		    String ibancriditor=null;
		    String ibandebtor = null;
		    String senderBic = null;
		    String receiverBic = null;
		    
		    String pacsType = pacsXmlParser.detectPacsType(xml);
	    	 BigDecimal amount;
	    	if ("pacs009".equals(pacsType)) {
	            Pacs009DTO dto = pacsXmlParser.parsePacs009Xml(xml);
	            creditorName=dto.getCreditorName();
	            debtorName=dto.getDebtorName();
	            receiverBic=dto.getReceiverBic();
	            senderBic=dto.getSenderBic();
	        } else if ("pacs008".equals(pacsType)) {
	            Pacs008DTO dto = pacsXmlParser.parsePacs008Xml(xml);
	            creditorName=dto.getCreditorName();
	            debtorName=dto.getDebtorName();
	            ibancriditor=dto.getCreditorIban();
	            ibandebtor=dto.getDebtorIban();
	            receiverBic=dto.getReceiverBic();
	            senderBic=dto.getSenderBic();
	        }else if ("pacs003".equals(pacsType)) {
	            Pacs003DTO dto = pacsXmlParser.parsePacs003Xml(xml);
	            creditorName=dto.getCreditorName();
	            debtorName=dto.getDebtorName();
	            ibancriditor=dto.getCreditorIban();
	            ibandebtor=dto.getDebtorIban();
	            receiverBic=dto.getInstructedAgentBic();
	            senderBic=dto.getInstructingAgentBic();
	            
	        } else if ("pacs004".equals(pacsType)) {
	            Pacs004DTO dto = pacsXmlParser.parsePacs004Xml(xml);
	            creditorName=dto.getCreditorName();
	            debtorName=dto.getDebtorName();
	            ibancriditor=dto.getCreditorIban();
	            ibandebtor=dto.getDebtorIban();
	            receiverBic=dto.getInstructedAgentBic();
	            senderBic=dto.getInstructingAgentBic();
	            
	        }else {
	        	 Pacs008DTO dto = pacsXmlParser.parsePacs008Xml(xml);
	            throw new IllegalArgumentException("Unknown PACS type in XML");            
	        }
	    	Optional<BPDZFraude> fraudeCreditorName=repositoryBpdzFraude.findByInformationInterdite(creditorName);
	        Optional<BPDZFraude> fraudeDebtorName=repositoryBpdzFraude.findByInformationInterdite(debtorName);
	        Optional<BPDZFraude> fraudeIbancriditor=repositoryBpdzFraude.findByInformationInterdite(ibancriditor);
	        Optional<BPDZFraude> fraudeIbandebtor=repositoryBpdzFraude.findByInformationInterdite(ibandebtor);
	        Optional<BPDZFraude> fraudeReceiverBic=repositoryBpdzFraude.findByInformationInterdite(receiverBic);
	        Optional<BPDZFraude> fraudeSenderBic=repositoryBpdzFraude.findByInformationInterdite(senderBic);
	        if(fraudeCreditorName.isPresent()||fraudeDebtorName.isPresent()||fraudeIbancriditor.isPresent()
	        		||fraudeIbandebtor.isPresent()||fraudeReceiverBic.isPresent()||fraudeSenderBic.isPresent()) {
	        	return "Alert";
	        }
		    return null;

		}
	 
	 public boolean checkFraudForAdd (BPDZDir dir) {
		
		    Optional<BPDZFraude> fraudeBic=repositoryBpdzFraude.findByInformationInterdite( dir.getBic());
	        Optional<BPDZFraude> fraudeNomBanque=repositoryBpdzFraude.findByInformationInterdite( dir.getNomBanque());
	        Optional<BPDZFraude> fraudeNomCorrespondant=repositoryBpdzFraude.findByInformationInterdite(dir.getNomCorrespondant());
	        Optional<BPDZFraude> fraudeTel=repositoryBpdzFraude.findByInformationInterdite( dir.getTel());
	        if(fraudeBic.isPresent()||fraudeNomBanque.isPresent()||fraudeNomCorrespondant.isPresent()
	        		||fraudeTel.isPresent()) {
	        	return false;
	        }
	        return true;
	 }
}
