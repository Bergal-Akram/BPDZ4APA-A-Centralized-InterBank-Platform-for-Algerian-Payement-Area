package com.Bank.BPDZ.Controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import com.Bank.BPDZ.DTO.Pacs002DTO;
import com.Bank.BPDZ.DTO.Pacs003DTO;
import com.Bank.BPDZ.DTO.Pacs004DTO;
import com.Bank.BPDZ.DTO.Pacs008DTO;
import com.Bank.BPDZ.DTO.Pacs009DTO;
import com.Bank.BPDZ.Entity.BPDZCptBcDCA;
import com.Bank.BPDZ.Entity.BPDZDir;
import com.Bank.BPDZ.Entity.BPDZFraude;
import com.Bank.BPDZ.Entity.BPDZHabi;
import com.Bank.BPDZ.Entity.BPDZMandat;
import com.Bank.BPDZ.Entity.BPDZPmtA;
import com.Bank.BPDZ.Entity.BPDZPret;
import com.Bank.BPDZ.Entity.BPDZSer;
import com.Bank.BPDZ.Entity.BPDZmt;
import com.Bank.BPDZ.Service.ServiceHabi;
import com.Bank.BPDZ.Service.ServiceMADU;

import jakarta.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;



@Controller 
public class ControllerHabi {
   @Autowired
	private ServiceHabi seviceHabi;
   @Autowired
    private ServiceMADU serviceMADU;

	public ControllerHabi(ServiceHabi seviceHabi) {
		super();
		this.seviceHabi = seviceHabi;
	}
	// for page of login without permission
	@GetMapping("/sigin")
	public String sigin(HttpSession session) {
		session.invalidate();
		return "login";
	}
	
    @GetMapping("/login")
	public String getLogin(HttpSession session) {
		BPDZHabi user = (BPDZHabi) session.getAttribute("loggedInUser");
		if (user != null) {
	        // ✅ Session still valid, redirect based on role
	        switch (user.getRole()) {
	            case "Admin":
	                return "/admin/home/home_admin";
	            case "Modifier":
	                return "/auditor/home/home_auditor";
	            case "Viewer":
	                return "veiw/home/home_view";
	            case "User":
	                return "bank_page";
	            default:
	                session.invalidate(); // Unknown role = force logout
	                return "redirect:/sigin";
	        }
	    }

	    // ❌ Not logged in, show login form
	    return "redirect:/sigin";
	
	}
	//-----------------------------------------------------------------------------------------------------------------
	@PostMapping("/login")
	//RequestParam (it get the content of login and motDePass from the login.html  )
	public String postLogin(@RequestParam String login,
            @RequestParam String motDePasse,
            Model model,
            HttpSession session) {

         BPDZHabi user = seviceHabi.login(login, motDePasse);

         if (user != null) {
	        
	        //1If the user doesn't make any requests in that time (1 minute), the session will expire and they will be logged out automatically.
	        session.setMaxInactiveInterval(1000);
	        //session.setAttribute("loggedInUser", user);
            // Redirect based on role
         switch (user.getRole()) {
           case "Admin":
        	   session.setAttribute("loggedInUser", user);
             return "/admin/home/home_admin";
             
           case "Modifier":
        	   session.setAttribute("loggedInUser", user);
             return "/auditor/home/home_auditor";
             
           case "Viewer":
        	   session.setAttribute("loggedInUser", user);
             return "veiw/home/home_view";
             
           case "User":
        	   session.setAttribute("loggedInUser", user);
             return "bank_page";
           default:
             model.addAttribute("error", "Role inconnu");
             return "redirect:/sigin";
           }

           } else {
	       // in this part ( the model will hold the "Login ou mot de passe invalide" in "error" and pass it to login.html)
           model.addAttribute("error", "Login ou mot de passe invalide");
           return "redirect:/sigin";
           }
    }
	
	
	
	@GetMapping("/form")
	public String formPage(HttpSession session ,Model model) {
        BPDZHabi user = (BPDZHabi) session.getAttribute("loggedInUser");
        model.addAttribute("pacs009DTO", new Pacs009DTO ());
        model.addAttribute("pacs003DTO", new Pacs003DTO ());
        model.addAttribute("pacs002DTO", new Pacs002DTO ());
        model.addAttribute("pacs004DTO", new Pacs004DTO ());
        if (user == null) {return "redirect:/sigin";}
        switch (user.getRole()) {
       
        case "User":   
        	model.addAttribute("pacs008DTO", new Pacs008DTO ());
          return "form";
        default:
          
          return "redirect:/login";
        }

		
	}
	
	
	//--------------------------------------------------------------------------------------------------

	

	    @GetMapping("/profile")
	    public String showBankInfo(Model model, HttpSession session) {
	        BPDZHabi user = (BPDZHabi) session.getAttribute("loggedInUser");

	        if (user == null) {
	           
	            return "redirect:/sigin";
	        }
	        
	        
	        switch (user.getRole()) {
	        case "Admin":
	        	 model.addAttribute("compte", seviceHabi.getAllAccount(user));
	             model.addAttribute("banks",seviceHabi.getInformationsBank(user));
	 	       
	          return "/admin/profile/prf_adm";
	          
	        case "Modifier":
	        	model.addAttribute("compte", seviceHabi.getAllAccount(user));
	             model.addAttribute("banks",seviceHabi.getInformationsBank(user));
	          return "/auditor/profile/prf_auditor";
	          
	        case "Viewer": 
	        	model.addAttribute("compte", seviceHabi.getAllAccount(user));
	             model.addAttribute("banks",seviceHabi.getInformationsBank(user));
	          return "/veiw/profile/prf_view";
	          
	        case "User": 
	        	model.addAttribute("compte", seviceHabi.getAllAccount(user));
	             model.addAttribute("banks",seviceHabi.getInformationsBank(user));
	          return "prf_bnk";
	        default:
	          
	          return "redirect:/login";
	        }

	        
	    }
	

        @GetMapping("/dir")
        public String showTableDir(Model model, HttpSession session) {
        	String search=null;
        	model.addAttribute("search", search);
        	model.addAttribute("banks", seviceHabi.getAllDir());
        	BPDZHabi user = (BPDZHabi) session.getAttribute("loggedInUser");
            if (user == null) {return "redirect:/sigin";}
            switch (user.getRole()) {
            case "Admin":
            	model.addAttribute("search", search);
            	model.addAttribute("banks", seviceHabi.getAllDir());
            	model.addAttribute("dirToEdit", new BPDZDir());
              return "/admin/table/dir";
              
            case "Modifier":
            	model.addAttribute("search", search);
            	model.addAttribute("banks", seviceHabi.getAllDir());
            	model.addAttribute("dirToEdit", new BPDZDir());
              return "/auditor/table/dir";
              
            case "Viewer":
            	model.addAttribute("search", search);
            	model.addAttribute("banks", seviceHabi.getAllDir());
              return "/veiw/table/dir";
              
            default:
              return "redirect:/login";
        	
            }
        }
        @PostMapping("/search")
        public String postSearchDir(@ModelAttribute("search") String search ,Model model , HttpSession session) {
            //TODO: process POST request
        	List<BPDZDir> allBanks = seviceHabi.getAllDir();
            List<BPDZDir> filtered = seviceHabi.suggestByNameDir(allBanks, search);
            
            BPDZHabi user = (BPDZHabi) session.getAttribute("loggedInUser");
            if (user == null) {return "redirect:/sigin";}
            switch (user.getRole()) {
            case "Admin":
            	model.addAttribute("banks", filtered);
                model.addAttribute("search", search);
                model.addAttribute("dirToEdit", new BPDZDir());
            	 filtered = seviceHabi.suggestByNameDir(allBanks, search);
              return "/admin/table/dir";
              
            case "Modifier":
            	model.addAttribute("banks", filtered);
                model.addAttribute("search", search);
                model.addAttribute("dirToEdit", new BPDZDir());
            	 filtered = seviceHabi.suggestByNameDir(allBanks, search);
              return "/auditor/table/dir";
              
            case "Viewer":
            	model.addAttribute("banks", filtered);
                model.addAttribute("search", search);
            	 filtered = seviceHabi.suggestByNameDir(allBanks, search);
              return "/veiw/table/dir";
              
            default:
              return "redirect:/login";
        	
            }
            
            
        }
        
        @GetMapping("/mondat")
        public String showTableMandat(Model model, HttpSession session) {
        	String search=null;
        	model.addAttribute("search", search);
        	model.addAttribute("mandat", seviceHabi.getAllmandat());
        	BPDZHabi user = (BPDZHabi) session.getAttribute("loggedInUser");
            if (user == null) {return "redirect:/sigin";}
            switch (user.getRole()) {
            case "Admin":
            	model.addAttribute("search", search);
            	model.addAttribute("mandat", seviceHabi.getAllmandat());
              return "/admin/table/mondat";
              
            case "Modifier":
            	model.addAttribute("search", search);
            	model.addAttribute("mandat", seviceHabi.getAllmandat());
              return "/auditor/table/mondat";
              
            case "Viewer":
            	model.addAttribute("search", search);
            	model.addAttribute("mandat", seviceHabi.getAllmandat());
              return "/veiw/table/mondat";
              
            default:
              return "redirect:/login";
        	
            }
        }
        @PostMapping("/searchmandat")
        public String postSearchMandat(@ModelAttribute("search") String search ,Model model , HttpSession session) {
            //TODO: process POST request
        	List<BPDZMandat> allBanks = seviceHabi.getAllmandat();
            List<BPDZMandat> filtered = seviceHabi.suggestByNameMandat(allBanks, search);
           
       // model.addAttribute("banks",seviceHabi.suggestByName(banks, search) );
            
            BPDZHabi user = (BPDZHabi) session.getAttribute("loggedInUser");
            if (user == null) {return "redirect:/sigin";}
            switch (user.getRole()) {
            case "Admin":
            	filtered = seviceHabi.suggestByNameMandat(allBanks, search);
            	 model.addAttribute("mandat", filtered);
                 model.addAttribute("search", search);
              return "/admin/table/mondat";
              
            case "Modifier":
            	filtered = seviceHabi.suggestByNameMandat(allBanks, search);
            	 model.addAttribute("mandat", filtered);
                 model.addAttribute("search", search);
              return "/auditor/table/mondat";
              
            case "Viewer": 
            	filtered = seviceHabi.suggestByNameMandat(allBanks, search);
            	 model.addAttribute("mandat", filtered);
                 model.addAttribute("search", search);
              return "/veiw/table/mondat";
              
            default:
              return "redirect:/login";
        	
            }
        }
	
        
        @GetMapping("/fraud")
        public String showTableFraude(Model model, HttpSession session) {
        	String search=null;
        	model.addAttribute("search", search);
        	model.addAttribute("fraude", seviceHabi.getAllFraude());
        	BPDZHabi user = (BPDZHabi) session.getAttribute("loggedInUser");
            if (user == null) {return "redirect:/sigin";}
            switch (user.getRole()) {
            case "Admin":
            	model.addAttribute("search", search);
            	model.addAttribute("fraude", seviceHabi.getAllFraude());
            	model.addAttribute("fraudToEdit",new BPDZFraude() );
              return "/admin/table/fraud";
              
            case "Modifier":
            	model.addAttribute("search", search);
            	model.addAttribute("fraude", seviceHabi.getAllFraude());
            	model.addAttribute("fraudToEdit",new BPDZFraude() );
              return "/auditor/table/fraud";
              
            case "Viewer":
            	model.addAttribute("search", search);
            	model.addAttribute("fraude", seviceHabi.getAllFraude());
              return "/veiw/table/fraud";
              
            default:
              return "redirect:/login";
        	
            }
        }
        @PostMapping("/searchfraude")
        public String postSearchFraude(@ModelAttribute("search") String search ,Model model , HttpSession session) {
            //TODO: process POST request 
        	List<BPDZFraude> allFraude = seviceHabi.getAllFraude();
            List<BPDZFraude> filtered = seviceHabi.suggestByNameFraude(allFraude, search);
            
       // model.addAttribute("banks",seviceHabi.suggestByName(banks, search) );
            BPDZHabi user = (BPDZHabi) session.getAttribute("loggedInUser");
            if (user == null) {return "redirect:/sigin";}
            switch (user.getRole()) {
            case "Admin":
            	model.addAttribute("fraude", filtered);
                model.addAttribute("search", search);
                model.addAttribute("fraudToEdit",new BPDZFraude() );
              return "/admin/table/fraud";
              
            case "Modifier":
            	model.addAttribute("fraude", filtered);
                model.addAttribute("search", search);
                model.addAttribute("fraudToEdit",new BPDZFraude() );
              return "/auditor/table/fraud";
              
            case "Viewer":
            	model.addAttribute("fraude", filtered);
                model.addAttribute("search", search);
              return "/veiw/table/fraud";
              
            default:
              return "redirect:/login";
        	
            }
        }
        
        
        @GetMapping("/compte")
        public String showTableCompte(Model model, HttpSession session) {
        	String search=null;
        	
        	BPDZHabi user = (BPDZHabi) session.getAttribute("loggedInUser");
            if (user == null) {return "redirect:/sigin";}
            switch (user.getRole()) {
            case "Admin":
            	model.addAttribute("search", search);
            	model.addAttribute("compte", seviceHabi.getAllCompte());
            	model.addAttribute("compteToEdit", new BPDZCptBcDCA());
              return "/admin/table/compte";
              
            case "Modifier":
            	model.addAttribute("search", search);
            	model.addAttribute("compte", seviceHabi.getAllCompte());
            	model.addAttribute("compteToEdit", new BPDZCptBcDCA());
              return "/auditor/table/compte";
              
            case "Viewer":
            	model.addAttribute("search", search);
            	model.addAttribute("compte", seviceHabi.getAllCompte());
              return "/veiw/table/compte";
            case "User":
            return "redirect:/login";
            default:
              return "redirect:/login";
        	
            }
        }
        @PostMapping("/searchcompte")
        public String postSearchCompte(@ModelAttribute("search") String search ,Model model , HttpSession session) {
            //TODO: process POST request 
        	List<BPDZCptBcDCA> allCompte = seviceHabi.getAllCompte();
            List<BPDZCptBcDCA> filtered = seviceHabi.suggestByNameCompte(allCompte, search);
           
       // model.addAttribute("banks",seviceHabi.suggestByName(banks, search) );
            BPDZHabi user = (BPDZHabi) session.getAttribute("loggedInUser");
            if (user == null) {return "redirect:/sigin";}
            switch (user.getRole()) {
            case "Admin":
            	 model.addAttribute("compte", filtered);
                 model.addAttribute("search", search);
                 model.addAttribute("compteToEdit", new BPDZCptBcDCA());
              return "/admin/table/compte";
              
            case "Modifier":
            	 model.addAttribute("compte", filtered);
                 model.addAttribute("search", search);
                 model.addAttribute("compteToEdit", new BPDZCptBcDCA());
              return "/auditor/table/compte";
              
            case "Viewer":  
            	 model.addAttribute("compte", filtered);
                 model.addAttribute("search", search);
              return "/veiw/table/compte";
            case "User":
                return "redirect:/login"; 
            default:
              return "redirect:/login";
        	
            }
        }
        
        @GetMapping("/pret")
        public String showTablePret(Model model, HttpSession session) {
        	String search=null;
        	model.addAttribute("search", search);
        	model.addAttribute("pret", seviceHabi.getAllPret());
        	BPDZHabi user = (BPDZHabi) session.getAttribute("loggedInUser");
            if (user == null) {return "redirect:/sigin";}
            switch (user.getRole()) {
            case "Admin":
            	model.addAttribute("search", search);
            	model.addAttribute("pret", seviceHabi.getAllPret());
              return "/admin/table/pret";
              
            case "Modifier":
            	model.addAttribute("search", search);
            	model.addAttribute("pret", seviceHabi.getAllPret());
              return "/auditor/table/pret";
              
            case "Viewer":
            	model.addAttribute("search", search);
            	model.addAttribute("pret", seviceHabi.getAllPret());
              return "/veiw/table/pret";
            case "User":
                return "redirect:/login"; 
            default:
              return "redirect:/login";
        	
            }
        }
        @PostMapping("/searchpret")
        public String postSearchPret(@ModelAttribute("search") String search ,Model model , HttpSession session) {
            //TODO: process POST request 
        	List<BPDZPret> allPret = seviceHabi.getAllPret();
            List<BPDZPret> filtered = seviceHabi.suggestByNamePret(allPret, search);
            
       // model.addAttribute("banks",seviceHabi.suggestByName(banks, search) );
            BPDZHabi user = (BPDZHabi) session.getAttribute("loggedInUser");
            if (user == null) {return "redirect:/sigin";}
            switch (user.getRole()) {
            case "Admin":
            	model.addAttribute("pret", filtered);
                model.addAttribute("search", search);
              return "/admin/table/pret";
              
            case "Modifier":
            	model.addAttribute("pret", filtered);
                model.addAttribute("search", search);
              return "/auditor/table/pret";
              
            case "Viewer":
            	model.addAttribute("pret", filtered);
                model.addAttribute("search", search);
              return "/veiw/table/pret";
            case "User":
                return "redirect:/login";   
            default:
              return "redirect:/login";
        	
            }
        }
        
        @GetMapping("/archive")
        public String showTableArchive(Model model, HttpSession session) {
        	String search=null;
        	model.addAttribute("search", search);
        	model.addAttribute("archive", seviceHabi.getAllArchiveMv());
        	BPDZHabi user = (BPDZHabi) session.getAttribute("loggedInUser");
            if (user == null) {return "redirect:/sigin";}
            switch (user.getRole()) {
            case "Admin":
            	model.addAttribute("search", search);
            	model.addAttribute("archive", seviceHabi.getAllArchiveMv());
              return "/admin/table/archive";
              
            case "Modifier":
            	model.addAttribute("search", search);
            	model.addAttribute("archive", seviceHabi.getAllArchiveMv());
              return "/auditor/table/archive";
              
            case "Viewer":
            	model.addAttribute("search", search);
            	model.addAttribute("archive", seviceHabi.getAllArchiveMv());
              return "/veiw/table/archive";
              
            default:
              return "redirect:/login";
        	
            }
        }
        @PostMapping("/searcharchive")
        public String postSearchArchive(@ModelAttribute("search") String search ,Model model , HttpSession session) {
            //TODO: process POST request 
        	List<BPDZPmtA> allArchive = seviceHabi.getAllArchiveMv();
            List<BPDZPmtA> filtered = seviceHabi.suggestByNameArchive(allArchive, search);
           
       // model.addAttribute("banks",seviceHabi.suggestByName(banks, search) );
            BPDZHabi user = (BPDZHabi) session.getAttribute("loggedInUser");
            if (user == null) {return "redirect:/sigin";}
            switch (user.getRole()) {
            case "Admin":
            	 model.addAttribute("archive", filtered);
                 model.addAttribute("search", search);
              return "/admin/table/archive";
              
            case "Modifier":
            	 model.addAttribute("archive", filtered);
                 model.addAttribute("search", search);
              return "/auditor/table/archive";
              
            case "Viewer":
            	 model.addAttribute("archive", filtered);
                 model.addAttribute("search", search);
              return "/veiw/table/archive";
            case "User":
                return "redirect:/login";  
            default:
              return "redirect:/login";
        	
            }
        }
        
        @GetMapping("/movment")
        public String showTableMovment(Model model, HttpSession session) {
        	String search=null;
        	model.addAttribute("search", search);
        	model.addAttribute("movment", seviceHabi.getAllMt());
        	BPDZHabi user = (BPDZHabi) session.getAttribute("loggedInUser");
            if (user == null) {return "redirect:/sigin";}
            switch (user.getRole()) {
            case "Admin":
            	
            	model.addAttribute("MovToEdit",new BPDZmt());
              return "/admin/table/movment";
              
            case "Modifier":
            	
            	model.addAttribute("MovToEdit",new BPDZmt());
              return "/auditor/table/movment";
              
            case "Viewer":
            	
              return "/veiw/table/movment";
            case "User":
            	model.addAttribute("trans", seviceHabi.getAlltrans(user));
                return "/movment";  
            default:
              return "redirect:/login";
        	
            }
        }
        @PostMapping("/searchmovment")
        public String postSearchMovmen(@ModelAttribute("search") String search ,Model model, HttpSession session ) {
            //TODO: process POST request 
        	List<BPDZmt> allMovment = seviceHabi.getAllMt();       	
            List<BPDZmt> filtered = seviceHabi.suggestByNameMouvement(allMovment, search);
            List<BPDZmt> filteredd =null;
       // model.addAttribute("banks",seviceHabi.suggestByName(banks, search) );
            BPDZHabi user = (BPDZHabi) session.getAttribute("loggedInUser");
            if (user == null) {return "redirect:/sigin";}
            switch (user.getRole()) {
            case "Admin":
            	 model.addAttribute("movment", filtered);
                 model.addAttribute("search", search);
                 model.addAttribute("MovToEdit",new BPDZmt());
              return "/admin/table/movment";
              
            case "Modifier":
            	 model.addAttribute("movment", filtered);
                 model.addAttribute("search", search);
                 model.addAttribute("MovToEdit",new BPDZmt());
              return "/auditor/table/movment";
              
            case "Viewer":
            	 model.addAttribute("movment", filtered);
                 model.addAttribute("search", search);
              return "/veiw/table/movment";
            case "User":
            	 List<BPDZmt> allTrans = seviceHabi.getAlltrans(user);
            	  filteredd = seviceHabi.suggestByNameMouvement(allTrans, search); 
            	 model.addAttribute("trans", filteredd);
                 model.addAttribute("search", search);
            	 return "/movment";    
            default:
              return "redirect:/login";
        	
            }
        }
        
        @GetMapping("/habi")
        public String showTableHabi(Model model, HttpSession session) {
        	String search=null;
        	model.addAttribute("search", search);
        	model.addAttribute("habi", seviceHabi.getAllHabi());
        	BPDZHabi user = (BPDZHabi) session.getAttribute("loggedInUser");
            if (user == null) {return "redirect:/sigin";}
            switch (user.getRole()) {
            case "Admin":
            	model.addAttribute("habi", seviceHabi.getAllHabi());
            	model.addAttribute("habiToEdit",new BPDZHabi());
              return "/admin/table/habi";
              
            case "Modifier":
            	model.addAttribute("habi", seviceHabi.getAllHabi());
            	model.addAttribute("habiToEdit",new BPDZHabi());
              return "/auditor/table/habi";
              
            case "Viewer":
            	model.addAttribute("habi", seviceHabi.getAllHabi());
              return "/veiw/table/habi";
            case "User":
                return "redirect:/login";  
            default:
              return "redirect:/login";
        	
            }
        }
        
        @PostMapping("/searchhabi")
        public String postSearchhabi(@ModelAttribute("search") String search ,Model model, HttpSession session ) {
            //TODO: process POST request 
        	List<BPDZHabi> allhabi = seviceHabi.getAllHabi();
            List<BPDZHabi> filtered = seviceHabi.suggestByNamehabi(allhabi, search);
           
       // model.addAttribute("banks",seviceHabi.suggestByName(banks, search) );
            BPDZHabi user = (BPDZHabi) session.getAttribute("loggedInUser");
            if (user == null) {return "redirect:/sigin";}
            switch (user.getRole()) {
            case "Admin":
            	 model.addAttribute("habi", filtered);
                 model.addAttribute("search", search);
                 model.addAttribute("habiToEdit",new BPDZHabi());
              return "/admin/table/habi";
              
            case "Modifier":
            	 model.addAttribute("habi", filtered);
                 model.addAttribute("search", search);
                 model.addAttribute("habiToEdit",new BPDZHabi());
              return "/auditor/table/habi";
              
            case "Viewer":
            	 model.addAttribute("habi", filtered);
                 model.addAttribute("search", search);
              return "/veiw/table/habi";
            case "User":
                return "redirect:/login";  
            default:
              return "redirect:/login";
        	
            }
        }
        
        @GetMapping("/service")
        public String showTableService(Model model, HttpSession session) {
        	String search=null;
        	model.addAttribute("search", search);
        	model.addAttribute("service", seviceHabi.getAllService());
        	BPDZHabi user = (BPDZHabi) session.getAttribute("loggedInUser");
            if (user == null) {return "redirect:/sigin";}
            switch (user.getRole()) {
            case "Admin":
            	
            	model.addAttribute("serviceToEdit",new BPDZSer());
              return "/admin/table/service";
              
            case "Modifier":
            	
            	model.addAttribute("serviceToEdit",new BPDZSer());
              return "/auditor/table/service";
              
            case "Viewer":
            
              return "/veiw/table/service";
            case "User":
                return "redirect:/login";  
            default:
              return "redirect:/login";
        	
            }
        }
        
        @PostMapping("/searchService")
        public String postSearchService(@ModelAttribute("search") String search ,Model model, HttpSession session ) {
            //TODO: process POST request 
        	List<BPDZSer> allSer = seviceHabi.getAllService();
            List<BPDZSer> filtered = seviceHabi.suggestByNameService(allSer, search);
            model.addAttribute("service", filtered);
            model.addAttribute("search", search);
       // model.addAttribute("banks",seviceHabi.suggestByName(banks, search) );
            BPDZHabi user = (BPDZHabi) session.getAttribute("loggedInUser");
            if (user == null) {return "redirect:/sigin";}
            switch (user.getRole()) {
            case "Admin":         	 
            	model.addAttribute("serviceToEdit",new BPDZSer());
              return "/admin/table/service";
              
            case "Modifier":          	
            	model.addAttribute("serviceToEdit",new BPDZSer());
              return "/auditor/table/service";
              
            case "Viewer":
            	 
              return "/veiw/table/service";
            case "User":
                return "redirect:/login";  
            default:
              return "redirect:/login";
        	
            }
        }
        //--------------------------------------------------------------------------------------------------
        
        
        @GetMapping("/admin/add/add_compte/add_prof")
        public String getadd_prof( HttpSession session,Model model) {
        	 BPDZHabi user = (BPDZHabi) session.getAttribute("loggedInUser");
             if (user == null) {return "redirect:/sigin";}
             model.addAttribute("dir", new BPDZDir());
             model.addAttribute("habi", new BPDZHabi());
             model.addAttribute("compte", new BPDZCptBcDCA());
             
             switch (user.getRole()) {
             case "Admin":
            	 return "/admin/add/add_compte/add_prof";
             default:
                 return "redirect:/login";
           	
               }
            
        }
        @GetMapping("/admin/add/add_auditor/add_audi")
        public String getAdmindAdd( HttpSession session,Model model) {
        	 BPDZHabi user = (BPDZHabi) session.getAttribute("loggedInUser");
        	 model.addAttribute("pret", new BPDZPret());
        	 model.addAttribute("fraud", new BPDZFraude());
        	 model.addAttribute("mandat", new BPDZMandat());
             if (user == null) {return "redirect:/sigin";}
             switch (user.getRole()) {
             case "Admin":
            return "/admin/add/add_auditor/add_audi";
             case "User":
             case "Viewer":
             case "Modifier":
                 return "redirect:/login";
               
             default:
                 return "redirect:/login";
           	
               }
        }
        @GetMapping("/auditor/add_auditor/add_audi")
        public String getAuditorAdd( HttpSession session,Model model ) {
        	 BPDZHabi user = (BPDZHabi) session.getAttribute("loggedInUser");
             if (user == null) {return "redirect:/sigin";}
             model.addAttribute("pret", new BPDZPret());
             model.addAttribute("fraud", new BPDZFraude());
             model.addAttribute("mandat", new BPDZMandat());
             switch (user.getRole()) {
             case "Modifier":
                 return "/auditor/add_auditor/add_audi";
             default:
                 return "redirect:/login";
           	
               }
      
        }
       

	

}