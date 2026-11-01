package com.Bank.BPDZ.Entity;

import jakarta.persistence.*;


import java.time.LocalDateTime;

@Entity
@Table(name = " bpdzpmta",
indexes = {
		@Index(name = "idx_bpdzpmta_emetteur", columnList = "id_banque_emetteur"),
        @Index(name = "idx_bpdzpmta_recepteur", columnList = "id_banque_recepteur"),
        @Index(name = "idx_bpdzpmta_typemessage", columnList = "typemessage"),
        @Index(name = "idx_bpdzpmta_statutmouvement", columnList = "statutmouvement")
    })

public class BPDZPmtA {

	 @Id
	 @GeneratedValue(strategy = GenerationType.IDENTITY)
	 private Long id;

	 @Column(name="statutmouvement",length = 100, nullable = false)
	 private String statutMouvement;

	 @Column(name="naturemouvement",length = 100, nullable = false)
	 private String natureMouvement;
	 
	 @Column(name="typemessage",length = 10, nullable = false)
	 private String typeMessage;

	 @Column(name="informationsmessage",columnDefinition = "TEXT")
	 private String informationsMessage;

	 @Column(name="modetransmission",length = 10)
	 private String modeTransmission;

	 @Column(name="xmlfile",columnDefinition = "TEXT")
	 private String xmlFile;
	 
	 @ManyToOne
	 @JoinColumn(name = "id_banque_emetteur", referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk_bpdzpmta_emetteur"))
	 private BPDZDir banqueEmetteur;

	 @ManyToOne
	 @JoinColumn(name = "id_banque_recepteur", referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk_bpdzpmta_recepteur"))
	 private BPDZDir banqueRecepteur;

	 @Column(name = "datecreation")
	 private LocalDateTime dateCreation;

	 @Column(name = "datereception")
	 private LocalDateTime dateReception;

	 @Column(name = "datetraitement")
	 private LocalDateTime dateTraitement;

	 @Column(name = "datearchived")
	 private LocalDateTime dateArchived;

	 // --- Getters and Setters ---



	 



	 @PrePersist
	 @PreUpdate
	 private void validateFields() {
	    
	     if (!typeMessage.matches("Pacs\\.008|Pacs\\.009|Pacs\\.003|Pacs\\.004|camt\\.053")) {
	         throw new IllegalArgumentException("TypeMessage must be one of: Pacs.008, Pacs.009, Pacs.003, Pacs.004, camt.053");
	     }
	     if (modeTransmission != null && !modeTransmission.matches("RTGS|CLRG|DNS")) {
	         throw new IllegalArgumentException("ModeTransmission must be 'RTGS', 'CLRG', or 'DNS'");
	     }
	 }
	 

		public BPDZPmtA() {
			super();
		}


		public BPDZPmtA(String statutMouvement, String natureMouvement,
				String typeMessage, String informationsMessage, String modeTransmission, String xmlFile
				) {
			super();
			
			this.statutMouvement = statutMouvement;
			this.natureMouvement = natureMouvement;
			
			this.typeMessage = typeMessage;
			this.informationsMessage = informationsMessage;
			this.modeTransmission = modeTransmission;
			this.xmlFile = xmlFile;
			
		}
		


		public BPDZPmtA( String statutMouvement, String natureMouvement, 
				String typeMessage, String informationsMessage, String modeTransmission, String xmlFile,
				BPDZDir banqueEmetteur, BPDZDir banqueRecepteur, LocalDateTime dateCreation, LocalDateTime dateReception,
				LocalDateTime dateTraitement, LocalDateTime dateArchived) {
			super();
			
			this.statutMouvement = statutMouvement;
			this.natureMouvement = natureMouvement;
		
			this.typeMessage = typeMessage;
			this.informationsMessage = informationsMessage;
			this.modeTransmission = modeTransmission;
			this.xmlFile = xmlFile;
			this.banqueEmetteur = banqueEmetteur;
			this.banqueRecepteur = banqueRecepteur;
			this.dateCreation = dateCreation;
			this.dateReception = dateReception;
			this.dateTraitement = dateTraitement;
			this.dateArchived = dateArchived;
		}
       
		

		public BPDZPmtA(Long id,  String statutMouvement, String natureMouvement,
				String typeMessage, String informationsMessage, String modeTransmission, String xmlFile,
				BPDZDir banqueEmetteur, BPDZDir banqueRecepteur, LocalDateTime dateCreation,
				LocalDateTime dateReception, LocalDateTime dateTraitement, LocalDateTime dateArchived) {
			super();
			this.id = id;
			
			this.statutMouvement = statutMouvement;
			this.natureMouvement = natureMouvement;
			this.typeMessage = typeMessage;
			this.informationsMessage = informationsMessage;
			this.modeTransmission = modeTransmission;
			this.xmlFile = xmlFile;
			this.banqueEmetteur = banqueEmetteur;
			this.banqueRecepteur = banqueRecepteur;
			this.dateCreation = dateCreation;
			this.dateReception = dateReception;
			this.dateTraitement = dateTraitement;
			this.dateArchived = dateArchived;
		}


		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		

		public String getStatutMouvement() {
			return statutMouvement;
		}

		public void setStatutMouvement(String statutMouvement) {
			this.statutMouvement = statutMouvement;
		}

		public String getNatureMouvement() {
			return natureMouvement;
		}

		public void setNatureMouvement(String natureMouvement) {
			this.natureMouvement = natureMouvement;
		}

		public String getTypeMessage() {
			return typeMessage;
		}

		public void setTypeMessage(String typeMessage) {
			this.typeMessage = typeMessage;
		}

		public String getInformationsMessage() {
			return informationsMessage;
		}

		public void setInformationsMessage(String informationsMessage) {
			this.informationsMessage = informationsMessage;
		}

		public String getModeTransmission() {
			return modeTransmission;
		}

		public void setModeTransmission(String modeTransmission) {
			this.modeTransmission = modeTransmission;
		}

		public String getXmlFile() {
			return xmlFile;
		}

		public void setXmlFile(String xmlFile) {
			this.xmlFile = xmlFile;
		}

		public BPDZDir getBanqueEmetteur() {
		     return banqueEmetteur;
		 }

		 public void setBanqueEmetteur(BPDZDir banqueEmetteur) {
		     this.banqueEmetteur = banqueEmetteur;
		 }

		 public BPDZDir getBanqueRecepteur() {
		     return banqueRecepteur;
		 }

		 public void setBanqueRecepteur(BPDZDir banqueRecepteur) {
		     this.banqueRecepteur = banqueRecepteur;
		 }

		 public LocalDateTime getDateCreation() {
		     return dateCreation;
		 }

		 public void setDateCreation(LocalDateTime dateCreation) {
		     this.dateCreation = dateCreation;
		 }

		 public LocalDateTime getDateReception() {
		     return dateReception;
		 }

		 public void setDateReception(LocalDateTime dateReception) {
		     this.dateReception = dateReception;
		 }

		 public LocalDateTime getDateTraitement() {
		     return dateTraitement;
		 }

		 public void setDateTraitement(LocalDateTime dateTraitement) {
		     this.dateTraitement = dateTraitement;
		 }

		 public LocalDateTime getDateArchived() {
		     return dateArchived;
		 }

		 public void setDateArchived(LocalDateTime dateArchived) {
		     this.dateArchived = dateArchived;
		 }
	 
	}

