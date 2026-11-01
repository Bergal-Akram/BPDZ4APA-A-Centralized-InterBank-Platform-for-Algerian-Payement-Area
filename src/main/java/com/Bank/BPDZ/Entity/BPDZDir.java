package com.Bank.BPDZ.Entity;

import jakarta.persistence.*;


@Entity
@Table(name = "bpdzdir", 
       indexes = {
           @Index(name = "idx_bpdzdir_codebanque", columnList = "codebanque"),
           @Index(name = "idx_bpdzdir_bic", columnList = "bic"),
           
       })

public class BPDZDir {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombanque", length = 50, nullable = false)
    private String nomBanque;

    @Column(name = "codebanque", length = 50, nullable = false, unique = true)
    private String codeBanque;

    @Column(name = "bic", length = 50, nullable = false, unique = true)
    private String bic;

   

    @Column(nullable = false)
    private Boolean participante = true;

    @Column(name = "iso20022integration", length = 1, nullable = false)
    private String iso20022Integration = "N";

    @Column(length = 2, nullable = false)
    private String abonnement;

	@Column(name = "pret", nullable = false)
    private Boolean pret = false;

    @Column(name = "nomcorrespondant", length = 50, nullable = false)
    private String nomCorrespondant;

    @Column(name = "mail", length = 50, nullable = false)
    private String mail;

    @Column(name = "tel", length = 15, nullable = false)
    private String tel;

    @PrePersist
    @PreUpdate
    private void validate() {
        if (!iso20022Integration.equals("O") && !iso20022Integration.equals("N")) {
            throw new IllegalArgumentException("ISO20022integration must be 'O' or 'N'");
        }
        if (!abonnement.matches("P[1-4]")) {
            throw new IllegalArgumentException("Abonnement must be one of: P1, P2, P3, P4");
        }
    }

	public BPDZDir() {
		super();
	}

	public BPDZDir(String nomBanque, String codeBanque, String bic, Boolean participante,
			String iso20022Integration, String abonnement) {
		super();
		this.nomBanque = nomBanque;
		this.codeBanque = codeBanque;
		this.bic = bic;
		
		this.participante = participante;
		this.iso20022Integration = iso20022Integration;
		this.abonnement = abonnement;
	}
	
	 public BPDZDir( String nomBanque, String codeBanque, String bic, String adresse, Boolean participante,
				String iso20022Integration, String abonnement, Boolean pret, String nomCorrespondant, String mail,
				String tel) {
			super();
			
			this.nomBanque = nomBanque;
			this.codeBanque = codeBanque;
			this.bic = bic;
			
			this.participante = participante;
			this.iso20022Integration = iso20022Integration;
			this.abonnement = abonnement;
			this.pret = pret;
			this.nomCorrespondant = nomCorrespondant;
			this.mail = mail;
			this.tel = tel;
		}
	 

	public BPDZDir(Long id, String nomBanque, String codeBanque, String bic, Boolean participante,
			String iso20022Integration, String abonnement, Boolean pret, String nomCorrespondant, String mail,
			String tel) {
		super();
		this.id = id;
		this.nomBanque = nomBanque;
		this.codeBanque = codeBanque;
		this.bic = bic;
		
		this.participante = participante;
		this.iso20022Integration = iso20022Integration;
		this.abonnement = abonnement;
		this.pret = pret;
		this.nomCorrespondant = nomCorrespondant;
		this.mail = mail;
		this.tel = tel;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNomBanque() {
		return nomBanque;
	}

	public void setNomBanque(String nomBanque) {
		this.nomBanque = nomBanque;
	}

	public String getCodeBanque() {
		return codeBanque;
	}

	public void setCodeBanque(String codeBanque) {
		this.codeBanque = codeBanque;
	}

	public String getBic() {
		return bic;
	}

	public void setBic(String bic) {
		this.bic = bic;
	}

	

	public Boolean getParticipante() {
		return participante;
	}

	public void setParticipante(Boolean participante) {
		this.participante = participante;
	}

	public String getIso20022Integration() {
		return iso20022Integration;
	}

	public void setIso20022Integration(String iso20022Integration) {
		this.iso20022Integration = iso20022Integration;
	}

	public String getAbonnement() {
		return abonnement;
	}

	public void setAbonnement(String abonnement) {
		this.abonnement = abonnement;
	}
	public Boolean getPret() {
		return pret;
	}

	public void setPret(Boolean pret) {
		this.pret = pret;
	}

	public String getNomCorrespondant() {
		return nomCorrespondant;
	}

	public void setNomCorrespondant(String nomCorrespondant) {
		this.nomCorrespondant = nomCorrespondant;
	}

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}
}
