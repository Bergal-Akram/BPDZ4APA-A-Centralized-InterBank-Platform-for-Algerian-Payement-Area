package com.Bank.BPDZ.DTO;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Pacs003DTO {
    // Header information
    private String messageId;            // GrpHdr.MsgId
    private LocalDateTime creationDateTime; // GrpHdr.CreDtTm
    private Integer numberOfTransactions; // GrpHdr.NbOfTxs
    private BigDecimal controlSum;      // GrpHdr.CtrlSum
    private String instructingAgentBic; // GrpHdr.InstgAgt.FinInstnId.BICFI
    private String instructedAgentBic;  // GrpHdr.InstdAgt.FinInstnId.BICFI
    
    // Transaction information
    private String endToEndId;          // CdtTrfTxInf.PmtId.EndToEndId
    private BigDecimal amount;          // CdtTrfTxInf.Amt.InstdAmt
    private String currency;            // CdtTrfTxInf.Amt.InstdAmt.@Ccy
    
    // Parties information
    private String debtorName;          // CdtTrfTxInf.Dbtr.Nm
    private String debtorIban;          // CdtTrfTxInf.DbtrAcct.Id.IBAN
    private String creditorName;        // CdtTrfTxInf.Cdtr.Nm
    private String creditorIban;        // CdtTrfTxInf.CdtrAcct.Id.IBAN
    
    // Additional information
    private String remittanceInfo;      // CdtTrfTxInf.RmtInf.Ustrd
    private String transmissionMode;  
    // Transmission method
	public String getMessageId() {
		return messageId;
	}
	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}
	public LocalDateTime getCreationDateTime() {
		return creationDateTime;
	}
	public void setCreationDateTime(LocalDateTime creationDateTime) {
		this.creationDateTime = creationDateTime;
	}
	public Integer getNumberOfTransactions() {
		return numberOfTransactions;
	}
	public void setNumberOfTransactions(Integer numberOfTransactions) {
		this.numberOfTransactions = numberOfTransactions;
	}
	public BigDecimal getControlSum() {
		return controlSum;
	}
	public void setControlSum(BigDecimal controlSum) {
		this.controlSum = controlSum;
	}
	public String getInstructingAgentBic() {
		return instructingAgentBic;
	}
	public void setInstructingAgentBic(String instructingAgentBic) {
		this.instructingAgentBic = instructingAgentBic;
	}
	public String getInstructedAgentBic() {
		return instructedAgentBic;
	}
	public void setInstructedAgentBic(String instructedAgentBic) {
		this.instructedAgentBic = instructedAgentBic;
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
	public String getDebtorIban() {
		return debtorIban;
	}
	public void setDebtorIban(String debtorIban) {
		this.debtorIban = debtorIban;
	}
	public String getCreditorName() {
		return creditorName;
	}
	public void setCreditorName(String creditorName) {
		this.creditorName = creditorName;
	}
	public String getCreditorIban() {
		return creditorIban;
	}
	public void setCreditorIban(String creditorIban) {
		this.creditorIban = creditorIban;
	}
	public String getRemittanceInfo() {
		return remittanceInfo;
	}
	public void setRemittanceInfo(String remittanceInfo) {
		this.remittanceInfo = remittanceInfo;
	}
	public String getTransmissionMode() {
		return transmissionMode;
	}
	public void setTransmissionMode(String transmissionMode) {
		this.transmissionMode = transmissionMode;
	}
    
    
}