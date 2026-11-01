package com.Bank.BPDZ.DTO;

import java.math.BigDecimal;

public class Pacs004DTO {
    // Header information
    private String messageId;
    private String instructingAgentBic;  // GrpHdr.InstgAgt.FinInstnId.BICFI
    private String instructedAgentBic;   // GrpHdr.InstdAgt.FinInstnId.BICFI
    
    // Original transaction reference
    private String originalMessageId;    // OrgnlGrpInf.OrgnlMsgId
    private String originalMessageName; // OrgnlGrpInf.OrgnlMsgNmId
    private String originalInstructionId; // TxInf.OrgnlInstrId
    private String originalEndToEndId;  // TxInf.OrgnlEndToEndId
    private String originalTransactionId; // TxInf.OrgnlTxId
    
    // Return information
    private String returnId;            // TxInf.RtrId
    private BigDecimal returnedAmount;  // TxInf.RtrdIntrBkSttlmAmt
    private BigDecimal returnedInstructedAmount; // TxInf.RtrdInstdAmt
    private String currency;            // TxInf.RtrdIntrBkSttlmAmt.@Ccy
    private String returnReasonCode;    // TxInf.RtrRsnInf.Rsn.Cd
    private String additionalInfo;      // TxInf.RtrRsnInf.AddtlInf
    
    // Parties information
    private String debtorName;         // TxInf.Dbtr.Nm
    private String debtorIban;         // TxInf.DbtrAcct.Id.IBAN
    private String creditorName;       // TxInf.Cdtr.Nm
    private String creditorIban;       // TxInf.CdtrAcct.Id.IBAN
    
    // Additional information
    private String remittanceInformation; // TxInf.RmtInf.Ustrd
    private String transmissionMode;    // Transmission method
	public String getMessageId() {
		return messageId;
	}
	public void setMessageId(String messageId) {
		this.messageId = messageId;
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
	public String getOriginalMessageId() {
		return originalMessageId;
	}
	public void setOriginalMessageId(String originalMessageId) {
		this.originalMessageId = originalMessageId;
	}
	public String getOriginalMessageName() {
		return originalMessageName;
	}
	public void setOriginalMessageName(String originalMessageName) {
		this.originalMessageName = originalMessageName;
	}
	public String getOriginalInstructionId() {
		return originalInstructionId;
	}
	public void setOriginalInstructionId(String originalInstructionId) {
		this.originalInstructionId = originalInstructionId;
	}
	public String getOriginalEndToEndId() {
		return originalEndToEndId;
	}
	public void setOriginalEndToEndId(String originalEndToEndId) {
		this.originalEndToEndId = originalEndToEndId;
	}
	public String getOriginalTransactionId() {
		return originalTransactionId;
	}
	public void setOriginalTransactionId(String originalTransactionId) {
		this.originalTransactionId = originalTransactionId;
	}
	public String getReturnId() {
		return returnId;
	}
	public void setReturnId(String returnId) {
		this.returnId = returnId;
	}
	public BigDecimal getReturnedAmount() {
		return returnedAmount;
	}
	public void setReturnedAmount(BigDecimal returnedAmount) {
		this.returnedAmount = returnedAmount;
	}
	public BigDecimal getReturnedInstructedAmount() {
		return returnedInstructedAmount;
	}
	public void setReturnedInstructedAmount(BigDecimal returnedInstructedAmount) {
		this.returnedInstructedAmount = returnedInstructedAmount;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public String getReturnReasonCode() {
		return returnReasonCode;
	}
	public void setReturnReasonCode(String returnReasonCode) {
		this.returnReasonCode = returnReasonCode;
	}
	public String getAdditionalInfo() {
		return additionalInfo;
	}
	public void setAdditionalInfo(String additionalInfo) {
		this.additionalInfo = additionalInfo;
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
	public String getRemittanceInformation() {
		return remittanceInformation;
	}
	public void setRemittanceInformation(String remittanceInformation) {
		this.remittanceInformation = remittanceInformation;
	}
	public String getTransmissionMode() {
		return transmissionMode;
	}
	public void setTransmissionMode(String transmissionMode) {
		this.transmissionMode = transmissionMode;
	}
    
}