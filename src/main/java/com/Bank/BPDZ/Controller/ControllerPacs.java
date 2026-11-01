package com.Bank.BPDZ.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.Bank.BPDZ.DTO.Pacs002DTO;
import com.Bank.BPDZ.DTO.Pacs003DTO;
import com.Bank.BPDZ.DTO.Pacs004DTO;
import com.Bank.BPDZ.DTO.Pacs008DTO;
import com.Bank.BPDZ.DTO.Pacs009DTO;
import com.Bank.BPDZ.DTO.PacsXmlGenerator;
import com.Bank.BPDZ.DTO.PacsXmlParser;
import com.Bank.BPDZ.Entity.BPDZHabi;
import com.Bank.BPDZ.Entity.BPDZmt;
import com.Bank.BPDZ.Service.PacsService;
import com.Bank.BPDZ.Service.SavePacsService;

import jakarta.servlet.http.HttpSession;

import org.springframework.core.io.Resource;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.core.io.ByteArrayResource;


import java.io.ByteArrayOutputStream;



@Controller

@RequestMapping("/Pacs")
public class ControllerPacs {
	@Autowired
	private PacsService pacsService;
	@Autowired
	private SavePacsService SavePacsService;
	@Autowired
	private PacsXmlGenerator pacsXmlGenerator;
	@Autowired
	private PacsXmlParser pacsXmlParser;
	private final String FILE_DIRECTORY = "C:/myfiles";
	
	 @PostMapping("/generatePacs008")
	    public String generateAndSavePacs008( @ModelAttribute ("pacs008DTO") Pacs008DTO dto,
	    		RedirectAttributes redirectAttributes, HttpSession session) throws Exception   {
	        BPDZHabi user = (BPDZHabi) session.getAttribute("loggedInUser");
	        if (user == null) {
	            return "redirect:/sigin";
	        }
	        try {
	           BPDZmt x= SavePacsService.savePacs008(dto);
	           pacsService.processPayment(x, user);
	          
	            redirectAttributes.addFlashAttribute("successMessage", "Données modifiées avec succès.");
	        } catch (Exception e) {
	        	
	            redirectAttributes.addFlashAttribute("errorMessage", "Une erreur s'est produite lors de la mise à jour.");
	           
	        }
	
	       
	        return "redirect:/form"; // Changez selon votre besoin
	    }
	 @PostMapping("/generatePacs009")
	    public String generateAndSavePacs009( @ModelAttribute ("pacs009DTO") Pacs009DTO dto,
	    		RedirectAttributes redirectAttributes, HttpSession session) throws Exception   {
	        BPDZHabi user = (BPDZHabi) session.getAttribute("loggedInUser");
	        if (user == null) {
	            return "redirect:/sigin";
	        }
	        try {
	           BPDZmt x= SavePacsService.savePacs009(dto);
	           pacsService.processPayment(x, user);
	          
	            redirectAttributes.addFlashAttribute("successMessage", "Données modifiées avec succès.");
	        } catch (Exception e) {
	        	
	            redirectAttributes.addFlashAttribute("errorMessage", "Une erreur s'est produite lors de la mise à jour.");
	           
	        }
	           BPDZmt x= SavePacsService.savePacs009(dto);
	           pacsService.processPayment(x, user);
	        
	        
	       // pacsService.jeutContablePacs008(dto,user);
	        return "redirect:/form"; // Changez selon votre besoin
	    }
	 @PostMapping("/generatePacs003")
	    public String generateAndSavePacs003( @ModelAttribute ("pacs003DTO") Pacs003DTO dto,
	    		RedirectAttributes redirectAttributes, HttpSession session) throws Exception   {
	        BPDZHabi user = (BPDZHabi) session.getAttribute("loggedInUser");
	        if (user == null) {
	            return "redirect:/sigin";
	        }
	        try {
	           BPDZmt x= SavePacsService.savePacs003(dto);
	           pacsService.processPayment(x, user);
	          
	            redirectAttributes.addFlashAttribute("successMessage", "Données modifiées avec succès.");
	        } catch (Exception e) {
	        	
	            redirectAttributes.addFlashAttribute("errorMessage", "Une erreur s'est produite lors de la mise à jour.");
	           
	        }
	        
	      
	       // pacsService.jeutContablePacs008(dto,user);
	        return "redirect:/form"; // Changez selon votre besoin
	    }
	 @PostMapping("/generatePacs004")
	    public String generateAndSavePacs004( @ModelAttribute ("pacs004DTO") Pacs004DTO dto,
	    		RedirectAttributes redirectAttributes, HttpSession session) throws Exception   {
	        BPDZHabi user = (BPDZHabi) session.getAttribute("loggedInUser");
	        if (user == null) {
	            return "redirect:/sigin";
	        }
	        try {
	           BPDZmt x= SavePacsService.savePacs004(dto);
	           pacsService.processPayment(x, user);
	          
	            redirectAttributes.addFlashAttribute("successMessage", "Données modifiées avec succès.");
	        } catch (Exception e) {
	        	
	            redirectAttributes.addFlashAttribute("errorMessage", "Une erreur s'est produite lors de la mise à jour.");
	           
	        }
	        
	      
	        
	       // pacsService.jeutContablePacs008(dto,user);
	        return "redirect:/form"; // Changez selon votre besoin
	    }
	 
	 @PostMapping("/generatePacs002")
	    public String generateAndSavePacs002( @ModelAttribute ("pacs002DTO") Pacs002DTO dto,
	    		RedirectAttributes redirectAttributes, HttpSession session) throws Exception   {
	        BPDZHabi user = (BPDZHabi) session.getAttribute("loggedInUser");
	        if (user == null) {
	            return "redirect:/sigin";
	        }
	        try {
	            BPDZmt x= SavePacsService.savePacs002(dto);	          
	            redirectAttributes.addFlashAttribute("successMessage", "Données modifiées avec succès.");
	        } catch (Exception e) {        	
	            redirectAttributes.addFlashAttribute("errorMessage", "Une erreur s'est produite lors de la mise à jour.");	           
	        }	        
	                          
	       // pacsService.jeutContablePacs008(dto,user);
	        return "redirect:/form"; // Changez selon votre besoin
	    }
	 //----------------------------------------------------------------uplaod--------------------------------------------------------------------------------
	 @PostMapping("/uploadXml")
	 public String handleXmlUpload(@RequestParam("file") MultipartFile file,
	                               RedirectAttributes redirectAttributes,
	                               HttpSession session) throws Exception {
		 BPDZHabi user = (BPDZHabi) session.getAttribute("loggedInUser");
	        if (user == null) {
	            return "redirect:/sigin";
	        }

	     if (file.isEmpty()) {
	         redirectAttributes.addFlashAttribute("errorMessage", "No file selected.");
	         return "redirect:/form";
	     }

	     // Validate file extension
	     String filename = file.getOriginalFilename();
	     if (filename == null || !filename.toLowerCase().endsWith(".xml")) {
	         redirectAttributes.addFlashAttribute("errorMessage", "Only XML files are allowed.");
	         return "redirect:/form";
	     }

	     try {
	         // Optional: Save or parse the file
	    	 String xmlContent = new String(file.getBytes(), StandardCharsets.UTF_8);
	    	 String pacsType = pacsXmlParser.detectPacsType(xmlContent);
	         // TODO: parse or process XML content
	    	 if ("pacs008".equals(pacsType)) {
	             // PACS 008
	             Pacs008DTO dto = pacsXmlParser.parsePacs008Xml(xmlContent);
	             BPDZmt x= SavePacsService.savePacs008(dto);
		           pacsService.processPayment(x, user);
	             return "redirect:/form";
	         } else if ("pacs002".equals(pacsType)) {
	             // PACS 002
	             Pacs002DTO dto = pacsXmlParser.parsePacs002Xml(xmlContent);
	             BPDZmt x= SavePacsService.savePacs002(dto);	
	            
	             return "redirect:/form";
	         } else if ("pacs004".equals(pacsType)) {
	             // PACS 004
	             Pacs004DTO dto = pacsXmlParser.parsePacs004Xml(xmlContent);
	             BPDZmt x= SavePacsService.savePacs004(dto);
		           pacsService.processPayment(x, user);
	            
	             return "redirect:/form";
	         } else if ("pacs009".equals(pacsType)) {
	             // PACS 009
	             Pacs009DTO dto = pacsXmlParser.parsePacs009Xml(xmlContent);
	             BPDZmt x= SavePacsService.savePacs009(dto);
		           pacsService.processPayment(x, user);
		          
	             return "redirect:/generatePacs009";
	         } else if ("pacs003".equals(pacsType)) {
	             // PACS 003
	             Pacs003DTO dto = pacsXmlParser.parsePacs003Xml(xmlContent);
	             BPDZmt x= SavePacsService.savePacs003(dto);
		           pacsService.processPayment(x, user);
		          
	             return "redirect:/form";
	         } else {
	        	 redirectAttributes.addFlashAttribute("errorMessage", "Unknown PACS type.");
	        	 
	             return "redirect:/form";
	         }
	     } catch (Exception e) {
	         redirectAttributes.addFlashAttribute("errorMessage", "File processing failed.");
	     }

	     return "redirect:/form";
	 }

	 
	 //-------------------------------------------------------------download---------------------------------------------------------------------------
	 @GetMapping("/download")
	 public ResponseEntity<Resource> downloadXmlFile(
	         @RequestParam("xml") String xml,
	         @RequestParam("pacs") String filename,
	         HttpSession session) throws Exception {

	     BPDZHabi user = (BPDZHabi) session.getAttribute("loggedInUser");
	     if (user == null) {
	         // Instead of redirect, send 401 Unauthorized or a message
	         return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
	     }

	     return pacsService.generateXmlDownload(xml, filename);
	 }

}
