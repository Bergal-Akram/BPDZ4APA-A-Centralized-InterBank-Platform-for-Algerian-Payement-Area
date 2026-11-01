package com.Bank.BPDZ.DTO;

import java.math.BigDecimal;

public class Pacs002DTO {
    private String originalMessageId;
    private String originalEndToEndId;
    private String status; // ACCP, RJCT, PDNG, PART, TECH, CANC
    private String reasonCode; // AM04, BE05, etc.
    private String reasonText;
    private String additionalInformation;
    private BigDecimal originalAmount;
    private String originalCurrency;
    
    
	public Pacs002DTO() {
		super();
	}
	public Pacs002DTO(String originalMessageId, String originalEndToEndId, String status, String reasonCode,
			String reasonText, String additionalInformation, BigDecimal originalAmount, String originalCurrency) {
		super();
		this.originalMessageId = originalMessageId;
		this.originalEndToEndId = originalEndToEndId;
		this.status = status;
		this.reasonCode = reasonCode;
		this.reasonText = reasonText;
		this.additionalInformation = additionalInformation;
		this.originalAmount = originalAmount;
		this.originalCurrency = originalCurrency;
	}
	public String getOriginalMessageId() {
		return originalMessageId;
	}
	public void setOriginalMessageId(String originalMessageId) {
		this.originalMessageId = originalMessageId;
	}
	public String getOriginalEndToEndId() {
		return originalEndToEndId;
	}
	public void setOriginalEndToEndId(String originalEndToEndId) {
		this.originalEndToEndId = originalEndToEndId;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getReasonCode() {
		return reasonCode;
	}
	public void setReasonCode(String reasonCode) {
		this.reasonCode = reasonCode;
	}
	public String getReasonText() {
		return reasonText;
	}
	public void setReasonText(String reasonText) {
		this.reasonText = reasonText;
	}
	public String getAdditionalInformation() {
		return additionalInformation;
	}
	public void setAdditionalInformation(String additionalInformation) {
		this.additionalInformation = additionalInformation;
	}
	public BigDecimal getOriginalAmount() {
		return originalAmount;
	}
	public void setOriginalAmount(BigDecimal originalAmount) {
		this.originalAmount = originalAmount;
	}
	public String getOriginalCurrency() {
		return originalCurrency;
	}
	public void setOriginalCurrency(String originalCurrency) {
		this.originalCurrency = originalCurrency;
	}
    
    
}
