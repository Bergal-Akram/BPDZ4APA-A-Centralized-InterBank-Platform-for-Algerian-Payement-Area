package com.Bank.BPDZ.DTO;

import java.math.BigDecimal;
public class Pacs009DTO {
    private String messageId;
    private String instructionId;
    private String endToEndId;
    private BigDecimal amount;
    private String currency;
    private String settlementMethod; // CLRG, INGA, etc.
    private String transmissionMode; // FILE, MQ, etc.
    private String senderBic;
    private String receiverBic;
    private String debtorName;
    private String creditorName;
    private String chargeBearer; 
    
    
    
	public Pacs009DTO() {
		super();
	}
	public Pacs009DTO(String messageId, String instructionId, String endToEndId, BigDecimal amount, String currency,
			String settlementMethod, String transmissionMode, String senderBic, String receiverBic, String debtorName,
			String creditorName, String chargeBearer) {
		super();
		this.messageId = messageId;
		this.instructionId = instructionId;
		this.endToEndId = endToEndId;
		this.amount = amount;
		this.currency = currency;
		this.settlementMethod = settlementMethod;
		this.transmissionMode = transmissionMode;
		this.senderBic = senderBic;
		this.receiverBic = receiverBic;
		this.debtorName = debtorName;
		this.creditorName = creditorName;
		this.chargeBearer = chargeBearer;
	}
	public String getMessageId() {
		return messageId;
	}
	public void setMessageId(String messageId) {
		this.messageId = messageId;
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
	public String getSettlementMethod() {
		return settlementMethod;
	}
	public void setSettlementMethod(String settlementMethod) {
		this.settlementMethod = settlementMethod;
	}
	public String getTransmissionMode() {
		return transmissionMode;
	}
	public void setTransmissionMode(String transmissionMode) {
		this.transmissionMode = transmissionMode;
	}
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
	public String getChargeBearer() {
		return chargeBearer;
	}
	public void setChargeBearer(String chargeBearer) {
		this.chargeBearer = chargeBearer;
	}
    
    
}
