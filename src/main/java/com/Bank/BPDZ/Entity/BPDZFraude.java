package com.Bank.BPDZ.Entity;

import jakarta.persistence.*;


import java.time.LocalDate;

@Entity
@Table(name = "bpdzfraude",
       indexes = {
           @Index(name = "idx_bpdzfraude_information_interdite", columnList = "informationinterdite")
       })

public class BPDZFraude {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

   /* @ManyToOne(optional = false)
    @JoinColumn(name = "id_mouvement", referencedColumnName = "id", nullable = false,
                foreignKey = @ForeignKey(name = "fk_fraude_mouvement", value = ConstraintMode.CONSTRAINT))
    private BPDZmt mouvement;*/

    @Column(name="informationinterdite",length = 50, unique = true, nullable = false)
    private String informationInterdite; // Bank Name, BIC, or Country

    @Column(length = 500)
    private String raison;

    @Column(name="dateinterdiction",nullable = false, columnDefinition = "DATE DEFAULT CURRENT_DATE")
    private LocalDate dateInterdiction;
    
    @Column(name = "typeinformation", nullable = false, length = 20)
    private String typeInformation;
    
    @PrePersist
    @PreUpdate
    private void validateDateInterdiction() {
        if (dateInterdiction == null) {
            dateInterdiction = LocalDate.now();  // Ensures dateInterdiction is set if not already provided
        }
    }
    

	public BPDZFraude(String informationInterdite, String raison, LocalDate dateInterdiction) {
		super();
		this.informationInterdite = informationInterdite;
		this.raison = raison;
		this.dateInterdiction = dateInterdiction;
	}
	


	public BPDZFraude(Long id, String informationInterdite, String raison, LocalDate dateInterdiction) {
		super();
		this.id = id;
		this.informationInterdite = informationInterdite;
		this.raison = raison;
		this.dateInterdiction = dateInterdiction;
	}
	


	public BPDZFraude() {
		super();
	}


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getInformationInterdite() {
		return informationInterdite;
	}

	public void setInformationInterdite(String informationInterdite) {
		this.informationInterdite = informationInterdite;
	}

	public String getRaison() {
		return raison;
	}

	public void setRaison(String raison) {
		this.raison = raison;
	}

	public LocalDate getDateInterdiction() {
		return dateInterdiction;
	}

	public void setDateInterdiction(LocalDate dateInterdiction) {
		this.dateInterdiction = dateInterdiction;
	}


	public String getTypeInformation() {
		return typeInformation;
	}


	public void setTypeInformation(String typeInformation) {
		this.typeInformation = typeInformation;
	}
    
}
