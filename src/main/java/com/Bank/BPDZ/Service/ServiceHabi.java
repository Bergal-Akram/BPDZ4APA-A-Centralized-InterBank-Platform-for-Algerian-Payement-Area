package com.Bank.BPDZ.Service;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.Bank.BPDZ.Entity.BPDZCptBcDCA;
import com.Bank.BPDZ.Entity.BPDZDir;
import com.Bank.BPDZ.Entity.BPDZFraude;
import com.Bank.BPDZ.Entity.BPDZHabi;
import com.Bank.BPDZ.Entity.BPDZMandat;
import com.Bank.BPDZ.Entity.BPDZPmtA;
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

@Service
@Component
public class ServiceHabi {
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
    
    

	
	
	
	public ServiceHabi(RepositoryBpdzHbi repositoryBpdzHbi, RepositoryBpdzDir repositoryBpdzDir,
			RepositoryBpdzCptbcdca repositoryBpdzCptbcdca, RepositoryBpdzMandat repositoryBpdzMandat,
			RepositoryBpdzFraude repositoryBpdzFraude, RepositoryBpdzPret repositoryBpdzPret,
			RepositoryBpdzPta repositoryBpdzPta) {
		super();
		this.repositoryBpdzHbi = repositoryBpdzHbi;
		this.repositoryBpdzDir = repositoryBpdzDir;
		this.repositoryBpdzCptbcdca = repositoryBpdzCptbcdca;
		this.repositoryBpdzMandat = repositoryBpdzMandat;
		this.repositoryBpdzFraude = repositoryBpdzFraude;
		this.repositoryBpdzPret = repositoryBpdzPret;
		this.repositoryBpdzPta = repositoryBpdzPta;
	}
	public List<BPDZSer> getAllService() {
		 List<BPDZSer> get=new ArrayList<>();
		get= repositoryBpdzSer.findAll();
		return get;
	}
	
	public List<BPDZmt> getAlltrans(BPDZHabi habi) {
		 List<BPDZmt> get=new ArrayList<>();
		get= repositoryBpdzmt.findByBanqueEmetteurOrBanqueRecepteur(habi.getBanque(), habi.getBanque());
		return get;
	}
	
	public List<BPDZDir> getAllDir() {
		 List<BPDZDir> get=new ArrayList<>();
		get= repositoryBpdzDir.findAll();
		return get;
	}
	public List<BPDZCptBcDCA> getAllCompte() {
		 List<BPDZCptBcDCA> get=new ArrayList<>();
		get= repositoryBpdzCptbcdca.findAll();
		return get;
	}
	public List<BPDZMandat> getAllmandat() {
		List<BPDZMandat> get=new ArrayList<>();
		get=repositoryBpdzMandat.findAll();
		return get;
		
		}
	public List<BPDZFraude> getAllFraude() {
		List<BPDZFraude> get=new ArrayList<>();
		get=repositoryBpdzFraude.findAll();
		return get;
		
		}
	public List<BPDZPret> getAllPret() {
		List<BPDZPret> get=new ArrayList<>();
		get=repositoryBpdzPret.findAll();
		return get;
		
		}
	public List<BPDZPmtA> getAllArchiveMv() {
		List<BPDZPmtA> get=new ArrayList<>();
		get=repositoryBpdzPta.findAll();
		return get;
		
		}
	public List<BPDZHabi> getAllHabi() {
		List<BPDZHabi> get=new ArrayList<>();
		get=repositoryBpdzHbi.findAll();
		return get;
		
		}
	public List<BPDZmt> getAllMt() {
		List<BPDZmt> get=new ArrayList<>();
		get=repositoryBpdzmt.findAll();
		return get;
		
		}
	
	
	public void addAccount(BPDZDir dir) {
		repositoryBpdzDir.save(dir);
		}
	
	
	@SuppressWarnings("deprecation")
	public BPDZDir getInformationsBank(BPDZHabi user) {
		// from the user(BPDZHani) I will get the Id of BPDZDir and ruturn the compte od BPDZDIR
		return  user.getBanque() ;
		}
		
		
	public List<BPDZCptBcDCA> getAllAccount(BPDZHabi habi) {
        BPDZDir bank = habi.getBanque(); // Get the associated bank
        return repositoryBpdzCptbcdca.findAllByBanque( bank);
    }
	 
	


	//this function return the row of BPDZHabi if it exists
	public BPDZHabi login(String login, String motDePasse) {
		
        BPDZHabi user = repositoryBpdzHbi.findByLogin(login);
        
        
        if (user != null && (passwordEncoder.matches(motDePasse, user.getMotDePasse())||passwordEncoder.matches(motDePasse, user.getPermissions()))) {
       // if (user != null && user.getMotDePasse().equals(motDePasse)) {
        	
        	return user;
        }
        return null;
    }
	
	
	public List<BPDZDir> suggestByNameDir(List<BPDZDir> list, String keyword) {
	    List<BPDZDir> suggestions = new ArrayList<>();
	    String lowerKeyword = keyword.toLowerCase();

	    for (BPDZDir dir : list) {
	        if (dir.getNomBanque()!=null &&dir.getNomBanque().toLowerCase().contains(lowerKeyword)||
	                (dir.getBic() != null && dir.getBic().toLowerCase().contains(lowerKeyword)) ||
	                (dir.getCodeBanque() != null && dir.getCodeBanque().toLowerCase().contains(lowerKeyword))) {
	            suggestions.add(dir);
	        }
	    }

	    return suggestions;
}
	public List<BPDZMandat> suggestByNameMandat(List<BPDZMandat> list, String keyword) {
	    List<BPDZMandat> suggestions = new ArrayList<>();
	    String lowerKeyword = keyword.toLowerCase();
	    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
	    

	    for (BPDZMandat mandat : list) {
	        if (mandat.getBanque()!=null && mandat.getBanque().getBic().toLowerCase().contains(lowerKeyword)||
	                (mandat.getDateMandat() != null && mandat.getDateMandat().format(formatter).contains(lowerKeyword)) ||
	                (mandat.getCompte() != null && mandat.getCompte().getNumeroCompte().toLowerCase().contains(lowerKeyword))) {
	            suggestions.add(mandat);
	        }
	    }

	    return suggestions;
	


}
	public List<BPDZFraude> suggestByNameFraude(List<BPDZFraude> list, String keyword) {
	    List<BPDZFraude> suggestions = new ArrayList<>();
	    String lowerKeyword = keyword.toLowerCase();
	    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
	    

	    for (BPDZFraude fraude : list) {
	        if (fraude.getDateInterdiction()!= null && fraude.getDateInterdiction().format(formatter).contains(lowerKeyword)||
	                (fraude.getInformationInterdite() != null && fraude.getInformationInterdite().toLowerCase().contains(lowerKeyword)) ||
	                (fraude.getRaison() != null && fraude.getRaison().toLowerCase().contains(lowerKeyword))) {
	            suggestions.add(fraude);
	        }
	    }

	    return suggestions;
	}
	

	public List<BPDZCptBcDCA> suggestByNameCompte(List<BPDZCptBcDCA> list, String keyword) {
	    List<BPDZCptBcDCA> suggestions = new ArrayList<>();
	    String lowerKeyword = keyword.toLowerCase();
	    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
	    

	    for (BPDZCptBcDCA compte : list) {
	        if (compte.getNumeroCompte()!= null && compte.getNumeroCompte().toLowerCase().contains(lowerKeyword)||
	                (compte.getNatureCompte() != null && compte.getNatureCompte().toLowerCase().contains(lowerKeyword)) ||
	                (compte.getId_banque() != null && compte.getId_banque().getBic().toLowerCase().contains(lowerKeyword))
	                ||(compte.getSolde() != null && compte.getSolde().toString().toLowerCase().contains(lowerKeyword))) {
	            suggestions.add(compte);
	        }
	    }

	    return suggestions;
	}
	
	public List<BPDZPret> suggestByNamePret(List<BPDZPret> list, String keyword) {
	    List<BPDZPret> suggestions = new ArrayList<>();
	    String lowerKeyword = keyword.toLowerCase();
	    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
	    

	    for (BPDZPret pret : list) {
	        if (pret.getBanque()!= null && pret.getBanque().getBic().toLowerCase().contains(lowerKeyword)||
	                (pret.getDatePret()!= null && pret.getDatePret().format(formatter).contains(lowerKeyword)) ||
	                (pret.getAgent() != null && pret.getAgent().getNom().toLowerCase().contains(lowerKeyword))
	                ||(pret.getMontantPret() != null && pret.getMontantPret().toString().toLowerCase().contains(lowerKeyword))) {
	            suggestions.add(pret);
	        }
	    }

	    return suggestions;
	}
	
	public List<BPDZPmtA> suggestByNameArchive(List<BPDZPmtA> list, String keyword) {
	    List<BPDZPmtA> suggestions = new ArrayList<>();
	    String lowerKeyword = keyword.toLowerCase();
	    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
	    

	    for (BPDZPmtA arch : list) {
	        if (arch.getBanqueEmetteur()!= null && arch.getBanqueEmetteur().getBic().toLowerCase().contains(lowerKeyword)||
	        		arch.getBanqueRecepteur()!= null && arch.getBanqueRecepteur().getBic().toLowerCase().contains(lowerKeyword)||
	              (arch.getStatutMouvement() != null && arch.getStatutMouvement().toLowerCase().contains(lowerKeyword))||
	                (arch.getModeTransmission() != null && arch.getModeTransmission().toLowerCase().contains(lowerKeyword))||
	                (arch.getTypeMessage() != null && arch.getTypeMessage() .toLowerCase().contains(lowerKeyword))) {
	            suggestions.add(arch);
	        }
	    }

	    return suggestions;
	}
	
	public List<BPDZmt> suggestByNameMouvement(List<BPDZmt> list, String keyword) {
	    List<BPDZmt> suggestions = new ArrayList<>();
	    String lowerKeyword = keyword.toLowerCase();
	    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
	    

	    for (BPDZmt mouv : list) {
	        if (mouv.getBanqueEmetteur()!= null && mouv.getBanqueEmetteur().getBic().toLowerCase().contains(lowerKeyword)||
	        		mouv.getBanqueRecepteur()!= null && mouv.getBanqueRecepteur().getBic().toLowerCase().contains(lowerKeyword)||
	                (mouv.getStatutMouvement() != null && mouv.getStatutMouvement().toLowerCase().contains(lowerKeyword))||
	                (mouv.getModeTransmission() != null && mouv.getModeTransmission().toLowerCase().contains(lowerKeyword))||
	                (mouv.getTypeMessage() != null && mouv.getTypeMessage() .toLowerCase().contains(lowerKeyword))) {
	            suggestions.add(mouv);
	        }
	    }

	    return suggestions;
	}
	
	public List<BPDZHabi> suggestByNamehabi(List<BPDZHabi> list, String keyword) {
	    List<BPDZHabi> suggestions = new ArrayList<>();
	    String lowerKeyword = keyword.toLowerCase();
	    
	    

	    for (BPDZHabi habi : list) {
	        if (habi.getBanque()!= null && habi.getBanque().getBic().toLowerCase().contains(lowerKeyword)||
	        		habi.getLogin()!= null && habi.getLogin().toLowerCase().contains(lowerKeyword)||
	                (habi.getNom() != null && habi.getNom().toLowerCase().contains(lowerKeyword))||
	                (habi.getRole() != null && habi.getRole().toLowerCase().contains(lowerKeyword))||
	                (habi.getAdresseIP() != null && habi.getAdresseIP() .toLowerCase().contains(lowerKeyword))) {
	            suggestions.add(habi);
	        }
	    }

	    return suggestions;
	}
	public List<BPDZSer> suggestByNameService(List<BPDZSer> list, String keyword) {
	    List<BPDZSer> suggestions = new ArrayList<>();
	    String lowerKeyword = keyword.toLowerCase();
	    
	    

	    for (BPDZSer ser : list) {
	             if (ser.getBanque()!= null && ser.getBanque().getBic().toLowerCase().contains(lowerKeyword)||
	        		 ser.getNomService()!= null && ser.getNomService().toLowerCase().contains(lowerKeyword)||
	                (ser.getTypeMessage() != null && ser.getTypeMessage().toLowerCase().contains(lowerKeyword))
	               ) {
	            suggestions.add(ser);
	        }
	    }

	    return suggestions;
	}
	

	
}
