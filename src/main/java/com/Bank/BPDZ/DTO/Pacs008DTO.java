package com.Bank.BPDZ.DTO;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Data;





public class Pacs008DTO {
    @NotBlank @Size(min = 8, max = 11)
    private String senderBic;

    @NotBlank @Size(min = 8, max = 11)
    private String receiverBic;

    @NotBlank
    private String instructionId;

    @NotBlank
    private String endToEndId;

    @NotNull @Positive
    private BigDecimal amount;

    @NotBlank @Size(min = 2, max = 3)
    private String currency;

    @NotBlank
    private String debtorName;

    @NotBlank
    private String creditorName;

    @NotBlank
    private String paymentDetails;

    @NotBlank
    private String chargeBearer;

    @NotBlank
    private String debtorIban;

    @NotBlank
    private String creditorIban;

    @NotBlank
    @Pattern(regexp = "RTGS|CLRG|DNS")
    private String transmissionMode;

    
    
public Pacs008DTO() {
		super();
	}

public Pacs008DTO(@NotBlank @Size(min = 8, max = 11) String senderBic,
			@NotBlank @Size(min = 8, max = 11) String receiverBic, @NotBlank String instructionId,
			@NotBlank String endToEndId, @NotNull @Positive BigDecimal amount,
			@NotBlank @Size(min = 2, max = 3) String currency, @NotBlank String debtorName,
			@NotBlank String creditorName, @NotBlank String paymentDetails, @NotBlank String chargeBearer,
			@NotBlank String debtorIban, @NotBlank String creditorIban,
			@NotBlank @Pattern(regexp = "RTGS|CLRG|DNS") String transmissionMode) {
		super();
		this.senderBic = senderBic;
		this.receiverBic = receiverBic;
		this.instructionId = instructionId;
		this.endToEndId = endToEndId;
		this.amount = amount;
		this.currency = currency;
		this.debtorName = debtorName;
		this.creditorName = creditorName;
		this.paymentDetails = paymentDetails;
		this.chargeBearer = chargeBearer;
		this.debtorIban = debtorIban;
		this.creditorIban = creditorIban;
		this.transmissionMode = transmissionMode;
	}

//----------------------------------------------------------------------------------------------------------------------------------
/*@XmlRootElement(name = "Pacs008")
@XmlAccessorType(XmlAccessType.FIELD)
public class Pacs008DTO {

    @XmlElement
    private String senderBic;
    
    @XmlElement
    private String receiverBic;

    @XmlElement
    private String instructionId;

    @XmlElement
    private String endToEndId;

    @XmlElement
    private BigDecimal amount;

    @XmlElement
    private String currency;

    @XmlElement
    private String debtorName;

    @XmlElement
    private String creditorName;

    @XmlElement
    private String paymentDetails;

    @XmlElement
    private String chargeBearer;

    @XmlElement
    private String debtorIban;

    @XmlElement
    private String creditorIban;

    @XmlElement
    private String transmissionMode;*/

    // Getters and setters for each field

    public String getSenderBic() {
        return senderBic;
    }

    public void setSenderBic(String senderBic) {
        this.senderBic = senderBic;
    }

    public String getReceiverBic() {
        return receiverBic;
    }

    public void setReceiverBic(String receiverBic) {
        this.receiverBic = receiverBic;
    }

    public String getInstructionId() {
        return instructionId;
    }

    public void setInstructionId(String instructionId) {
        this.instructionId = instructionId;
    }

    public String getEndToEndId() {
        return endToEndId;
    }

    public void setEndToEndId(String endToEndId) {
        this.endToEndId = endToEndId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getDebtorName() {
        return debtorName;
    }

    public void setDebtorName(String debtorName) {
        this.debtorName = debtorName;
    }

    public String getCreditorName() {
        return creditorName;
    }

    public void setCreditorName(String creditorName) {
        this.creditorName = creditorName;
    }

    public String getPaymentDetails() {
        return paymentDetails;
    }

    public void setPaymentDetails(String paymentDetails) {
        this.paymentDetails = paymentDetails;
    }

    public String getChargeBearer() {
        return chargeBearer;
    }

    public void setChargeBearer(String chargeBearer) {
        this.chargeBearer = chargeBearer;
    }

    public String getDebtorIban() {
        return debtorIban;
    }

    public void setDebtorIban(String debtorIban) {
        this.debtorIban = debtorIban;
    }

    public String getCreditorIban() {
        return creditorIban;
    }

    public void setCreditorIban(String creditorIban) {
        this.creditorIban = creditorIban;
    }

    public String getTransmissionMode() {
        return transmissionMode;
    }

    public void setTransmissionMode(String transmissionMode) {
        this.transmissionMode = transmissionMode;
    }
}
