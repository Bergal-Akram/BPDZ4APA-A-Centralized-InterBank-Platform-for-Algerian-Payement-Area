package com.Bank.BPDZ.Entity;

import jakarta.persistence.*;


@Entity
@Table(name = "bpdzhabi",
       indexes = {
           @Index(name = "idx_bpdzhabi_id_banque", columnList = "id_banque"),
           @Index(name = "idx_bpdzhabi_login", columnList = "login")
       })

public class BPDZHabi {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    //Every record must be linked to a Bank
    /*This annotation defines a "many-to-one" relationship, meaning that many instances of the current entity  "Habilitation"
     *  can be associated with one instance of the BPDZDir 
     */
    @ManyToOne(optional = false)
    @JoinColumn(name = "id_banque", referencedColumnName = "id", nullable = false,// ON DELETE CASCADE
    // I named this foreign key for debugging because the name is generated automatically when you don't name it
                foreignKey = @ForeignKey(name = "fk_banque_habi", value = ConstraintMode.CONSTRAINT))
    private BPDZDir banque;

    @Column(length = 80, nullable = false, unique = true)
    private String login;

    @Column(name="motdepasse",length = 80, nullable = false)
    private String motDePasse;

    @Column(name="adresseip",length = 50, nullable = false)
    private String adresseIP;

    @Column(name="portreception",nullable = false)
    private Integer portReception;

    @Column(length = 50)
    private String adresseipsecours;
    
    @Column(name="portreceptionsecours")
    private Integer portReceptionSecours;

    @Column(length = 10, nullable = false)
    private String role;

    @Column(columnDefinition = "TEXT")
    private String permissions;
    
    @Column(name = "nom", length = 15, nullable = false)
    private String nom;

    @Column(name = "prenom", length = 15, nullable = false)
    private String prenom;

  

	public BPDZHabi(BPDZDir banque, String login, String motDePasse, String adresseIP, Integer portReception,
			String adresseIPSecours, Integer portReceptionSecours, String role, String permissions, String nom,
			String prenom) {
		super();
		this.banque = banque;
		this.login = login;
		this.motDePasse = motDePasse;
		this.adresseIP = adresseIP;
		this.portReception = portReception;
		this.adresseipsecours = adresseIPSecours;
		this.portReceptionSecours = portReceptionSecours;
		this.role = role;
		this.permissions = permissions;
		this.nom = nom;
		this.prenom = prenom;
	}


	@PrePersist
    @PreUpdate
    private void validateRole() {
        if (!role.matches("Viewer|Modifier|Admin|User")) {
            throw new IllegalArgumentException("Role must be one of: Viewer, Modifier, Admin, User");
        }
    }
    

	public BPDZHabi() {
		super();
	}


	public BPDZHabi(BPDZDir banque, String login, String motDePasse, String adresseIP, Integer portReception,
			String adresseIPSecours, Integer portReceptionSecours, String role, String permissions) {
		super();
		this.banque = banque;
		this.login = login;
		this.motDePasse = motDePasse;
		this.adresseIP = adresseIP;
		this.portReception = portReception;
		this.adresseipsecours = adresseIPSecours;
		this.portReceptionSecours = portReceptionSecours;
		this.role = role;
		this.permissions = permissions;
	}
    
	

	public BPDZHabi(Long id, BPDZDir banque, String login, String motDePasse, String adresseIP, Integer portReception,
			String adresseipsecours, Integer portReceptionSecours, String role, String permissions, String nom,
			String prenom) {
		super();
		this.id = id;
		this.banque = banque;
		this.login = login;
		this.motDePasse = motDePasse;
		this.adresseIP = adresseIP;
		this.portReception = portReception;
		this.adresseipsecours = adresseipsecours;
		this.portReceptionSecours = portReceptionSecours;
		this.role = role;
		this.permissions = permissions;
		this.nom = nom;
		this.prenom = prenom;
	}


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public BPDZDir getBanque() {
		return banque;
	}

	public void setBanque(BPDZDir banque) {
		this.banque = banque;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getMotDePasse() {
		return motDePasse;
	}

	public void setMotDePasse(String motDePasse) {
		this.motDePasse = motDePasse;
	}

	public String getAdresseIP() {
		return adresseIP;
	}

	public void setAdresseIP(String adresseIP) {
		this.adresseIP = adresseIP;
	}

	public Integer getPortReception() {
		return portReception;
	}

	public void setPortReception(Integer portReception) {
		this.portReception = portReception;
	}

	public String getAdresseIPSecours() {
		return adresseipsecours;
	}

	public void setAdresseIPSecours(String adresseIPSecours) {
		this.adresseipsecours = adresseIPSecours;
	}

	public Integer getPortReceptionSecours() {
		return portReceptionSecours;
	}

	public void setPortReceptionSecours(Integer portReceptionSecours) {
		this.portReceptionSecours = portReceptionSecours;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getPermissions() {
		return permissions;
	}

	public void setPermissions(String permissions) {
		this.permissions = permissions;
	}


	public String getAdresseipsecours() {
		return adresseipsecours;
	}


	public void setAdresseipsecours(String adresseipsecours) {
		this.adresseipsecours = adresseipsecours;
	}
	  public String getNom() {
			return nom;
		}


		public void setNom(String nom) {
			this.nom = nom;
		}


		public String getPrenom() {
			return prenom;
		}


		public void setPrenom(String prenom) {
			this.prenom = prenom;
		}

    
}
