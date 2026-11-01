package com.Bank.BPDZ.Controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
import com.Bank.BPDZ.Service.ServiceHabi;
import com.Bank.BPDZ.Service.ServiceMADU;

import jakarta.servlet.http.HttpSession;

@Controller

@RequestMapping("/operation")
public class ControllerOperation {
	@Autowired
	private ServiceHabi seviceHabi;
   @Autowired
    private ServiceMADU serviceMADU;
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
      private RepositoryBpdzSer repositoryBpdzSer;
      @Autowired
      private PasswordEncoder passwordEncoder;
      @Autowired
      private RepositoryBpdzmt repositoryBpdzmt;
public ControllerOperation(ServiceHabi seviceHabi, ServiceMADU serviceMADU) {
	super();
	this.seviceHabi = seviceHabi;
	this.serviceMADU = serviceMADU;
}
   //---------------------------------------------------------dialoge confirm delete------------------------------------------------------------------------------------
@GetMapping("/confirmdelete")
public String dialoge(@RequestParam String type, @RequestParam Long id, Model model,HttpSession session) {
	 BPDZHabi user = (BPDZHabi) session.getAttribute("loggedInUser");
     if (user == null) {return "redirect:/sigin";}
	
	
    model.addAttribute("type", type);

    switch (type.toLowerCase()) {
        case "compte":
            repositoryBpdzCptbcdca.findById(id).ifPresent(obj -> model.addAttribute("object", obj));
            break;
        case "pret":
            repositoryBpdzPret.findById(id).ifPresent(obj -> model.addAttribute("object", obj));
            break;
        case "mouvment":
            repositoryBpdzmt.findById(id).ifPresent(obj -> model.addAttribute("object", obj));
            break;
        case "mondat":
            repositoryBpdzMandat.findById(id).ifPresent(obj -> model.addAttribute("object", obj));
            break;
        case "fraud":
            repositoryBpdzFraude.findById(id).ifPresent(obj -> model.addAttribute("object", obj));
            break;
        case "dir":
            repositoryBpdzDir.findById(id).ifPresent(obj -> model.addAttribute("object", obj));
            break;
        case "habi":
            repositoryBpdzHbi.findById(id).ifPresent(obj -> model.addAttribute("object", obj));
            break;
        case "service":
            repositoryBpdzSer.findById(id).ifPresent(obj -> model.addAttribute("object", obj));
            break;
        default:
            model.addAttribute("errorMessage", "Type inconnu.");
            return "redirect:/login";
    }

    return "confirm_delete";
}
//--------------------------------------------------------------delete Habi-------------------------------------------------------------------------------------
@PostMapping("/delete")
public String deleteHabi(@RequestParam Long id, @RequestParam String type, RedirectAttributes redirectAttributes,HttpSession session) {
	 BPDZHabi user = (BPDZHabi) session.getAttribute("loggedInUser");
     if (user == null) {return "redirect:/sigin";}
    boolean deleted = false;
    String redirectUrl = "redirect:/login";

    switch (type.toLowerCase()) {
        case "compte":
            deleted = serviceMADU.deleteCompte(id);
            redirectUrl = "redirect:/compte";
            break;
        case "pret":
            deleted = serviceMADU.deletePret(id);
            redirectUrl = "redirect:/pret";
            break;
        case "mouvment":
            deleted = serviceMADU.deletemovment(id);
            redirectUrl = "redirect:/movment";
            break;
        case "mondat":
            deleted = serviceMADU.deleteMondat(id);
            redirectUrl = "redirect:/mondat";
            break;
        case "fraud":
            deleted = serviceMADU.deleteFraud(id);
            redirectUrl = "redirect:/fraud";
            break;
        case "dir":
            deleted = serviceMADU.deletedir(id);
            redirectUrl = "redirect:/dir";
            break;
        case "habi":
            deleted = serviceMADU.deleteHabi(id);
            redirectUrl = "redirect:/habi";
            break;
        case "service":
            deleted = serviceMADU.deleteService(id);
            redirectUrl = "redirect:/service";
            break;
        default:
            redirectAttributes.addFlashAttribute("errorMessage", "Type de suppression inconnu.");
            return "redirect:/login";
    }

    if (deleted) {
        redirectAttributes.addFlashAttribute("message", "Suppression réussie.");
    } else {
        redirectAttributes.addFlashAttribute("errorMessage", "Suppression échouée.");
    }

    return redirectUrl;
}
//----------------------------------------------------------------add pret-----------------------------------------------------------------------------------

@PostMapping("/add")
public String addPret(@ModelAttribute BPDZPret pret,
                      @RequestParam Long agentId,
                      @RequestParam String bic,
                      RedirectAttributes redirectAttributes ,HttpSession session) {
	 BPDZHabi user = (BPDZHabi) session.getAttribute("loggedInUser");
     if (user == null) {return "redirect:/sigin";}
	
    try {
    	serviceMADU.addPret(pret, agentId, bic);
        redirectAttributes.addFlashAttribute("successMessage", "Prêt ajouté avec succès.");
    } catch (Exception e) {
        redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
    }

    return "redirect:/pret"; // or wherever you want to go after
}

//--------------------------------------------------------------add fraud---------------------------------------------------------------------------------
@PostMapping("/fraud/add")
public String addFraud(@ModelAttribute BPDZFraude fraud,HttpSession session , RedirectAttributes redirectAttributes) {
	
	 BPDZHabi user = (BPDZHabi) session.getAttribute("loggedInUser");
     if (user == null) {return "redirect:/sigin";}
     try {
         serviceMADU.addFraud(fraud);
         redirectAttributes.addFlashAttribute("successMessage", "Utilisateur ajouté avec succès.");
     } catch (IllegalArgumentException e) {
         redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
     } catch (Exception e) {
         redirectAttributes.addFlashAttribute("errorMessage", "Une erreur inattendue s'est produite.");
     }
     return "redirect:/fraud";
}

//----------------------------------------------------------------add dir-------------------------------------------------------------------------------

@PostMapping("/dir/add")
public String addDir(@ModelAttribute BPDZDir dir,HttpSession session , RedirectAttributes redirectAttributes) {
	
	 BPDZHabi user = (BPDZHabi) session.getAttribute("loggedInUser");
     if (user == null) {return "redirect:/sigin";}
     try {
         serviceMADU.addDir(dir);
         redirectAttributes.addFlashAttribute("successMessage", "Utilisateur ajouté avec succès.");
     } catch (IllegalArgumentException e) {
         redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
     } catch (Exception e) {
         redirectAttributes.addFlashAttribute("errorMessage", "Une erreur inattendue s'est produite.");
     }
   
     return "redirect:/dir";
}

//----------------------------------------------------------add habi-------------------------------------------------------------------------------------
@PostMapping("/addhabi")
public String addHabi(@ModelAttribute BPDZHabi habi,@RequestParam String bic,HttpSession session , RedirectAttributes redirectAttributes) {
	
	 BPDZHabi user = (BPDZHabi) session.getAttribute("loggedInUser");
     if (user == null) {return "redirect:/sigin";}
     try {
         serviceMADU.addBPDZHabi(habi, bic);
         redirectAttributes.addFlashAttribute("successMessage", "Utilisateur ajouté avec succès.");
     } catch (IllegalArgumentException e) {
         redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
     } catch (Exception e) {
         redirectAttributes.addFlashAttribute("errorMessage", "Une erreur inattendue s'est produite.");
     }
    // serviceMADU.addBPDZHabi(habi, bic);
     
     return "redirect:/habi";
}

//--------------------------------------------------------------------add compte---------------------------------------------------------------------------
@PostMapping("/comptes/add")
public String addCompte(@ModelAttribute BPDZCptBcDCA compte,@RequestParam String bic,HttpSession session , RedirectAttributes redirectAttributes) {
	
	 BPDZHabi user = (BPDZHabi) session.getAttribute("loggedInUser");
     if (user == null) {return "redirect:/sigin";}
     try {
         serviceMADU.addBPDZCptBcDCA(compte, bic);
         redirectAttributes.addFlashAttribute("successMessage", "Utilisateur ajouté avec succès.");
     } catch (IllegalArgumentException e) {
         redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
     } catch (Exception e) {
         redirectAttributes.addFlashAttribute("errorMessage", "Une erreur inattendue s'est produite.");
     }
    // serviceMADU.addBPDZHabi(habi, bic);
     
     return "redirect:/compte";}

//------------------------------------------------------------------------add mandat-----------------------------------------------------------------------
@PostMapping("/mandat/add")
public String addMandat(@ModelAttribute ("mandat")BPDZMandat mondat,  
                      @RequestParam ("bic") String bic,
                      @RequestParam ("idCompte")Long compteId,
                      RedirectAttributes redirectAttributes ,HttpSession session) {
	 BPDZHabi user = (BPDZHabi) session.getAttribute("loggedInUser");
     if (user == null) {return "redirect:/sigin";}
	
    try {
    	serviceMADU.addMandat(mondat, bic, compteId);
        redirectAttributes.addFlashAttribute("successMessage", "Prêt ajouté avec succès.");
    } catch (Exception e) {
        redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
    }

    return "redirect:/mondat"; // or wherever you want to go after
}


//---------------------------------------------------------this part for changes in the tables---------------------------------------------------------------

//------------------------------------------------------------------editCompte-----------------------------------------------------------------------------
@GetMapping("/editcompte")
public String editCompte(@RequestParam("id") Long id, Model model,HttpSession session, RedirectAttributes redirectAttributes) {
	 BPDZHabi user = (BPDZHabi) session.getAttribute("loggedInUser");
     if (user == null) {return "redirect:/sigin";}
     try {
         Optional<BPDZCptBcDCA> compteOpt = repositoryBpdzCptbcdca.findById(id);
         if (compteOpt.isPresent()) {
             BPDZCptBcDCA compte = compteOpt.get();
             model.addAttribute("compteToEdit", compte);
             model.addAttribute("compte", seviceHabi.getAllCompte());}
         
         
      }catch (IllegalArgumentException e) {
         redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
     } catch (Exception e) {
         redirectAttributes.addFlashAttribute("errorMessage", "Une erreur inattendue s'est produite.");
     }
    
    

    return "/admin/table/compte";
}

@PostMapping("/updatecompte")
public String updateCompte(@ModelAttribute("compteToEdit") BPDZCptBcDCA compteForm,@RequestParam ("banqueBic") String bic, HttpSession session, RedirectAttributes redirectAttributes) {
    BPDZHabi user = (BPDZHabi) session.getAttribute("loggedInUser");
    if (user == null) return "redirect:/sigin";

    try {
    	serviceMADU.updateCompte(compteForm, bic);    
        
    } catch (Exception e) {
        redirectAttributes.addFlashAttribute("errorMessage", "Erreur inattendue: " + e.getMessage());
    }
   // serviceMADU.updateCompte(compteForm, bic);
    return "redirect:/compte";
}


//-------------------------------------------------------------edit dir----------------------------------------------------------------------------------
@GetMapping("/editdir")
public String editDir(@RequestParam("id") Long id, Model model,HttpSession session, RedirectAttributes redirectAttributes) {
	 BPDZHabi user = (BPDZHabi) session.getAttribute("loggedInUser");
     if (user == null) {return "redirect:/sigin";}
     try {
         Optional<BPDZDir> compteOpt = repositoryBpdzDir.findById(id);
         if (compteOpt.isPresent()) {
             BPDZDir compte = compteOpt.get();
             model.addAttribute("dirToEdit", compte);
             model.addAttribute("banks", seviceHabi.getAllDir());}
         
      }catch (IllegalArgumentException e) {
         redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
     } catch (Exception e) {
         redirectAttributes.addFlashAttribute("errorMessage", "Une erreur inattendue s'est produite.");
     }
    
    return "/admin/table/dir";
}

@PostMapping("/updateDir")
public String updateDir(@ModelAttribute("dirToEdit") BPDZDir dir, RedirectAttributes redirectAttributes, HttpSession session) {
    BPDZHabi user = (BPDZHabi) session.getAttribute("loggedInUser");
    if (user == null) {
        return "redirect:/sigin";
    }

    try {
        repositoryBpdzDir.save(dir);
        redirectAttributes.addFlashAttribute("successMessage", "Données modifiées avec succès.");
    } catch (Exception e) {
        redirectAttributes.addFlashAttribute("errorMessage", "Une erreur s'est produite lors de la mise à jour.");
    }

    return "redirect:/dir"; // Changez selon votre besoin
}
//------------------------------
@GetMapping("/editFraud")
public String editfraud(@RequestParam("id") Long id, Model model,HttpSession session, RedirectAttributes redirectAttributes) {
	 BPDZHabi user = (BPDZHabi) session.getAttribute("loggedInUser");
     if (user == null) {return "redirect:/sigin";}
     try {
         Optional<BPDZFraude> compteOpt = repositoryBpdzFraude.findById(id);
         if (compteOpt.isPresent()) {
             BPDZFraude fraud = compteOpt.get();
             model.addAttribute("fraudToEdit", fraud);
             model.addAttribute("fraude", seviceHabi.getAllFraude());}
         
      }catch (IllegalArgumentException e) {
         redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
     } catch (Exception e) {
         redirectAttributes.addFlashAttribute("errorMessage", "Une erreur inattendue s'est produite.");
     }
    
    return "/admin/table/fraud";
}

@PostMapping("/updatFraud")
public String updateFraud(@ModelAttribute("fraudToEdit") BPDZFraude fraud, RedirectAttributes redirectAttributes, HttpSession session) {
    BPDZHabi user = (BPDZHabi) session.getAttribute("loggedInUser");
    if (user == null) {
        return "redirect:/sigin";
    }

    try {
    	repositoryBpdzFraude.save(fraud);
        redirectAttributes.addFlashAttribute("successMessage", "Données modifiées avec succès.");
    } catch (Exception e) {
        redirectAttributes.addFlashAttribute("errorMessage", "Une erreur s'est produite lors de la mise à jour.");
    }
   
    return "redirect:/fraud"; // Changez selon votre besoin
}

//--------------------------------------------------------------edit Mov---------------------------------------------------------------------------------
@GetMapping("/editMov")
public String editMouv(@RequestParam("id") Long id, Model model,HttpSession session, RedirectAttributes redirectAttributes) {
	 BPDZHabi user = (BPDZHabi) session.getAttribute("loggedInUser");
     if (user == null) {return "redirect:/sigin";}
     try {
         Optional<BPDZmt> compteOpt = repositoryBpdzmt.findById(id);
         if (compteOpt.isPresent()) {
             BPDZmt Mov = compteOpt.get();
             model.addAttribute("MovToEdit", Mov);
             model.addAttribute("movment", seviceHabi.getAllMt());}
       
      }catch (IllegalArgumentException e) {
         redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
     } catch (Exception e) {
         redirectAttributes.addFlashAttribute("errorMessage", "Une erreur inattendue s'est produite.");
     }    
    return "/admin/table/movment";
}
@PostMapping("/updateMov")
public String updateMov(@ModelAttribute("MovToEdit") BPDZmt mov, RedirectAttributes redirectAttributes, HttpSession session) {
    BPDZHabi user = (BPDZHabi) session.getAttribute("loggedInUser");
    if (user == null) {
        return "redirect:/sigin";
    }
    try {
    	serviceMADU.updateMov(mov);
        redirectAttributes.addFlashAttribute("successMessage", "Données modifiées avec succès.");
    } catch (Exception e) {
        redirectAttributes.addFlashAttribute("errorMessage", "Une erreur s'est produite lors de la mise à jour.");
    }  
    return "redirect:/movment"; // Changez selon votre besoin
}


//------------------------------------------------------------------edit habi-----------------------------------------------------------------------------
@GetMapping("/edithabi")
public String editHabi(@RequestParam("id") Long id, Model model,HttpSession session, RedirectAttributes redirectAttributes) {
	 BPDZHabi user = (BPDZHabi) session.getAttribute("loggedInUser");
     if (user == null) {return "redirect:/sigin";}
  
     try {
         Optional<BPDZHabi> compteOpt = repositoryBpdzHbi.findById(id);
         if (compteOpt.isPresent()) {
             BPDZHabi habi = compteOpt.get();
             model.addAttribute("habiToEdit", habi);
             model.addAttribute("habi", seviceHabi.getAllHabi());}
         
      }catch (IllegalArgumentException e) {
         redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
     } catch (Exception e) {
         redirectAttributes.addFlashAttribute("errorMessage", "Une erreur inattendue s'est produite.");
     }
     Optional<BPDZHabi> compteOpt = repositoryBpdzHbi.findById(id);
     
     BPDZHabi habi = compteOpt.get();
     model.addAttribute("habiToEdit", habi);
     model.addAttribute("habi", seviceHabi.getAllHabi());
    
    return "/admin/table/habi";
}

@PostMapping("/updateHabi")
public String updateHabi(@ModelAttribute("habiToEdit") BPDZHabi habi,@RequestParam ("banqueBic") String bic,
		RedirectAttributes redirectAttributes, HttpSession session) {
    BPDZHabi user = (BPDZHabi) session.getAttribute("loggedInUser");
    if (user == null) {
        return "redirect:/sigin";
    }
    try {
    	serviceMADU.updatehabi(habi, bic);
        redirectAttributes.addFlashAttribute("successMessage", "Données modifiées avec succès.");
    } catch (Exception e) {
        redirectAttributes.addFlashAttribute("errorMessage", "Une erreur s'est produite lors de la mise à jour.");
    }
    
    return "redirect:/habi"; // Changez selon votre besoin
}

//---------------------------------------------------------------edit service-----------------------------------------------------------------------------------
@GetMapping("/editService")
public String editService(@RequestParam("id") Long id, Model model,HttpSession session, RedirectAttributes redirectAttributes) {
	 BPDZHabi user = (BPDZHabi) session.getAttribute("loggedInUser");
     if (user == null) {return "redirect:/sigin";}
  
     try {
         Optional<BPDZSer> compteOpt = repositoryBpdzSer.findById(id);
         if (compteOpt.isPresent()) {
             BPDZSer service = compteOpt.get();
             model.addAttribute("serviceToEdit", service);
             model.addAttribute("service", seviceHabi.getAllService());}
         
      }catch (IllegalArgumentException e) {
         redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
     } catch (Exception e) {
         redirectAttributes.addFlashAttribute("errorMessage", "Une erreur inattendue s'est produite.");
     }
    
    
    return "/admin/table/service";
} 

@PostMapping("/updateService")
public String updateService(@ModelAttribute("serviceToEdit") BPDZSer ser,@RequestParam ("banqueBic") String bic,
		RedirectAttributes redirectAttributes, HttpSession session) {
    BPDZHabi user = (BPDZHabi) session.getAttribute("loggedInUser");
    if (user == null) {
        return "redirect:/sigin";
    }
    try {
    	serviceMADU.updateSer(ser, bic);
        redirectAttributes.addFlashAttribute("successMessage", "Données modifiées avec succès.");
    } catch (Exception e) {
        redirectAttributes.addFlashAttribute("errorMessage", "Une erreur s'est produite lors de la mise à jour.");
    }
    
    return "redirect:/service"; // Changez selon votre besoin
}
}

