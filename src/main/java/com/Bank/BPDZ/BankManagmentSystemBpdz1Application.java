 package com.Bank.BPDZ;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.Bank.BPDZ.Entity.BPDZCptBcDCA;
import com.Bank.BPDZ.Entity.BPDZDir;
import com.Bank.BPDZ.Entity.BPDZFraude;
import com.Bank.BPDZ.Entity.BPDZHabi;
import com.Bank.BPDZ.Entity.BPDZSer;
import com.Bank.BPDZ.Repository.RepositoryBpdzDir;
import com.Bank.BPDZ.Repository.RepositoryBpdzHbi;
import com.Bank.BPDZ.Repository.RepositoryBpdzSer;
import com.Bank.BPDZ.Service.PacsService;
import com.Bank.BPDZ.Service.ServiceHabi;

@SpringBootApplication
@EnableScheduling
public class BankManagmentSystemBpdz1Application implements CommandLineRunner{
	

	public static void main(String[] args) {
		SpringApplication.run(BankManagmentSystemBpdz1Application.class, args);
	}
	/*public List<BPDZDir> suggestByName(List<BPDZDir> list, String keyword) {
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
	}*/
	@Autowired
	private PacsService pacsService;
    @Autowired
	private RepositoryBpdzDir savecompte;
    @Autowired
   	private RepositoryBpdzHbi takecompt;
    @Autowired
   	private RepositoryBpdzSer repositoryBpdzSer;
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    ServiceHabi servicehabi;
    //@Autowired
    
    //serviceFraude
	@Override
	public void run(String... args) throws Exception {
		// TODO Auto-generated method stub
		/*BPDZDir x=new BPDZDir("BNA","123456","BNALDZAL627","BNA A.P 627 N° 81 Rue Ali REMLI Bouzareah –Alger",true,"O","P1");
		 BPDZDir h=new BPDZDir(" BNP Paribas EL Djazair","1234568","BDLODZALXXX"," 8 Quartier d'Affaires d'Alger Lot 1 N°03.I – Bab Ezzouar – 16024 Alger",true,"O","P1");
		 BPDZDir v=new BPDZDir("BDL","1234567","BNPADZALXXX El Djazair"," 05, Rue GACI Amar, Staouéli, Alger ",true,"O","P1");
		 BPDZDir c=new BPDZDir("BPDZ","0000000","BPDZALALXXX","home",true,"O","P4");
		savecompte.save(h);
		savecompte.save(v);
		savecompte.save(c);*/
		
		
		/*BPDZFraude fraude = new BPDZFraude();
		
	    boolean s ;//serviceFraude.ghdjdjd("kjk",)
		
		 BPDZHabi compte=new BPDZHabi();
		  compte=takecompt.findByid(1L);
		  System.out.println("");
		  
		  
		  
		 String X=compte.getRole();
		 BPDZDir dir=new BPDZDir();
		 BPDZCptBcDCA cp=new BPDZCptBcDCA();
		 dir= servicehabi.getInformationsBank(compte);
		 cp=servicehabi.getOneAccount(compte);
		 System.out.println("the role is :"+cp.getNatureCompte());
		 //System.out.println("this is :"+dir.getBic());
		 System.out.println("this is :"+cp.getNatureCompte());
		 //BPDZDir [] s=savecompte.findAll(null);
		 List<BPDZDir> get=new ArrayList<>();
		 get=servicehabi.getAllDir();
		 for(int i=0;i<get.size();i++) {
			 BPDZDir dirr =get.get(i);
			 System.out.println("The [i] is :"+dirr.getBic());
		 }
		/*Scanner scanner = new Scanner(System.in);
		System.out.print("Enter name to search: ");
		String input = scanner.nextLine();

		List<BPDZDir> dirs = servicehabi.getAllDir();
		List<BPDZDir> suggestions = suggestByName(dirs, input);

		if (!suggestions.isEmpty()) {
		    System.out.println("Suggestions:");
		    for (int i = 0; i < suggestions.size(); i++) {
		        System.out.println((i + 1) + ". " + suggestions.get(i).getNomBanque());
		    }*/
		 
		//s=null;
		//s=servicehabi.getInformationsBank(compte);
		
		 //BPDZHabi compte=new BPDZHabi();
		 //compte=takecompt.findByid(1L);
		 //boolean s =passwordEncoder.matches("12345678", compte.getMotDePasse());
		 //System.out.println("this :"+s);
		 //System.out.println("");
		  // compte .setMotDePasse(passwordEncoder.encode("MotDePasse2"));
		   //System.out.println("this :"+compte.getMotDePasse());
		 //compte.setRole("User");
		 //System.out.println("this :"+compte.getRole());
		//takecompt.save(compte);
		BigDecimal big = BigDecimal.valueOf(50);	
		System.out.println("la valeur est :"+pacsService.calculateTax(big));
		System.out.println("la valeur estxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx :"); 
		
		/*BPDZSer service = new BPDZSer();
		service.setNomService("P1");
		service.setTypeMessage("Pacs.008"); 	
		service.setBanque(savecompte.findByBic("TBAADZALXXX"));
		repositoryBpdzSer.save(service);
		
		BPDZSer service1 = new BPDZSer();
		service1.setNomService("P1");
		service1.setTypeMessage("Pacs.009"); 
		service1.setBanque(savecompte.findByBic("TBAADZALXXX"));
		repositoryBpdzSer.save(service1);
		
		BPDZSer service2 = new BPDZSer();
		service2.setNomService("P1");
		service2.setTypeMessage("Pacs.003"); 
		service2.setBanque(savecompte.findByBic("TBAADZALXXX"));
		repositoryBpdzSer.save(service2);
		
		BPDZSer service3 = new BPDZSer();
		service3.setNomService("P1");
		service3.setTypeMessage("Pacs.004"); 
		service3.setBanque(savecompte.findByBic("TBAADZALXXX"));
		repositoryBpdzSer.save(service3);
		
		BPDZSer service4 = new BPDZSer();
		service4.setNomService("P1");
		service4.setTypeMessage("camt.053"); 
		service4.setBanque(savecompte.findByBic("TBAADZALXXX"));
		repositoryBpdzSer.save(service4);*/
		
		
		
		

			}
	
}
