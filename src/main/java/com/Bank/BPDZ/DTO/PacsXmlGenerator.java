package com.Bank.BPDZ.DTO;

import java.io.StringWriter;
import java.math.BigDecimal;
import java.io.StringReader;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;


import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.xml.sax.InputSource;

import com.Bank.BPDZ.Entity.BPDZDir;
import com.Bank.BPDZ.Entity.BPDZPmtA;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;


@Component
public class PacsXmlGenerator {
	@Autowired
	private PacsXmlParser pacsXmlParser;

    public String generatePacs008Xml(Pacs008DTO dto) throws Exception {
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
        Document doc = docBuilder.newDocument();

        // Root element with namespace
        Element rootElement = doc.createElement("Document");
        rootElement.setAttribute("xmlns", "urn:iso:std:iso:20022:tech:xsd:pacs.008.001.09");
        doc.appendChild(rootElement);

        // FIToFICustomerCreditTransfer element
        Element fiToFi = doc.createElement("FIToFICstmrCdtTrf");
        rootElement.appendChild(fiToFi);

        // Group Header
        Element grpHdr = doc.createElement("GrpHdr");
        fiToFi.appendChild(grpHdr);

        addElement(doc, grpHdr, "MsgId", dto.getInstructionId());
        addElement(doc, grpHdr, "CreDtTm", LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));
        addElement(doc, grpHdr, "NbOfTxs", "1");
        
        // Payment Information
        Element cdtTrfTxInf = doc.createElement("CdtTrfTxInf");
        fiToFi.appendChild(cdtTrfTxInf);

        // Payment Identification
        Element pmtId = doc.createElement("PmtId");
        cdtTrfTxInf.appendChild(pmtId);
        addElement(doc, pmtId, "InstrId", dto.getInstructionId());
        addElement(doc, pmtId, "EndToEndId", dto.getEndToEndId());

        // Amount
        Element amt = doc.createElement("Amt");
        cdtTrfTxInf.appendChild(amt);
        Element instdAmt = doc.createElement("InstdAmt");
        instdAmt.setAttribute("Ccy", dto.getCurrency());
        instdAmt.setTextContent(dto.getAmount().toString());
        amt.appendChild(instdAmt);

        // Charge Bearer
        addElement(doc, cdtTrfTxInf, "ChrgBr", dto.getChargeBearer());

        // Debtor
        Element dbtr = doc.createElement("Dbtr");
        cdtTrfTxInf.appendChild(dbtr);
        addElement(doc, dbtr, "Nm", dto.getDebtorName());

        // Debtor Account
        Element dbtrAcct = doc.createElement("DbtrAcct");
        cdtTrfTxInf.appendChild(dbtrAcct);
        Element id = doc.createElement("Id");
        dbtrAcct.appendChild(id);
        addElement(doc, id, "IBAN", dto.getDebtorIban());

        // Debtor Agent (Bank)
        Element dbtrAgt = doc.createElement("DbtrAgt");
        cdtTrfTxInf.appendChild(dbtrAgt);
        Element finInstnId = doc.createElement("FinInstnId");
        dbtrAgt.appendChild(finInstnId);
        addElement(doc, finInstnId, "BICFI", dto.getSenderBic());

        // Creditor
        Element cdtr = doc.createElement("Cdtr");
        cdtTrfTxInf.appendChild(cdtr);
        addElement(doc, cdtr, "Nm", dto.getCreditorName());

        // Creditor Account
        Element cdtrAcct = doc.createElement("CdtrAcct");
        cdtTrfTxInf.appendChild(cdtrAcct);
        Element cdtrId = doc.createElement("Id");
        cdtrAcct.appendChild(cdtrId);
        addElement(doc, cdtrId, "IBAN", dto.getCreditorIban());

        // Creditor Agent (Bank)
        Element cdtrAgt = doc.createElement("CdtrAgt");
        cdtTrfTxInf.appendChild(cdtrAgt);
        Element cdtrFinInstnId = doc.createElement("FinInstnId");
        cdtrAgt.appendChild(cdtrFinInstnId);
        addElement(doc, cdtrFinInstnId, "BICFI", dto.getReceiverBic());

        // Convert to XML string
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        StringWriter writer = new StringWriter();
        transformer.transform(new DOMSource(doc), new StreamResult(writer));
        
        return writer.toString();
    }

    private void addElement(Document doc, Element parent, String tagName, String textContent) {
        Element element = doc.createElement(tagName);
        element.setTextContent(textContent);
        parent.appendChild(element);
       
    }
    
 
    
    //------------------------------------------------------------------ generate Pacs 002---------------------------------------------------------------------------
    public String generatePacs002(Pacs002DTO dto) throws Exception {
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
        Document doc = docBuilder.newDocument();

        // Root element
        Element rootElement = doc.createElement("Document");
        rootElement.setAttribute("xmlns", "urn:iso:std:iso:20022:tech:xsd:pacs.002.001.10");
        doc.appendChild(rootElement);

        // Status Report
        Element statusReport = doc.createElement("FIToFIPmtStsRpt");
        rootElement.appendChild(statusReport);

        // Group Header
        Element grpHdr = doc.createElement("GrpHdr");
        statusReport.appendChild(grpHdr);
        addElement(doc, grpHdr, "MsgId", "STATUS-" + UUID.randomUUID().toString());
        addElement(doc, grpHdr, "CreDtTm", LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));

        // Original Group Information
        Element orgnlGrpInf = doc.createElement("OrgnlGrpInfAndSts");
        statusReport.appendChild(orgnlGrpInf);
        addElement(doc, orgnlGrpInf, "OrgnlMsgId", dto.getOriginalMessageId());
        addElement(doc, orgnlGrpInf, "OrgnlMsgNmId", "pacs.008.001.08");
        addElement(doc, orgnlGrpInf, "GrpSts", dto.getStatus());

        // Transaction Information (for detailed status)
        Element txInf = doc.createElement("TxInfAndSts");
        statusReport.appendChild(txInf);
        addElement(doc, txInf, "OrgnlEndToEndId", dto.getOriginalEndToEndId());
        addElement(doc, txInf, "TxSts", dto.getStatus());

        // Status Reason Information (for rejections)
        if ("RJCT".equals(dto.getStatus())) {
            Element stsRsnInf = doc.createElement("StsRsnInf");
            txInf.appendChild(stsRsnInf);
            
            Element rsn = doc.createElement("Rsn");
            stsRsnInf.appendChild(rsn);
            addElement(doc, rsn, "Cd", dto.getReasonCode());
            
            if (dto.getReasonText() != null) {
                addElement(doc, stsRsnInf, "AddtlInf", dto.getReasonText());
            }
        }

        // Original Transaction Reference
        Element orgnlTxRef = doc.createElement("OrgnlTxRef");
        txInf.appendChild(orgnlTxRef);
        Element amount = addElementt(doc, orgnlTxRef, "IntrBkSttlmAmt", dto.getOriginalAmount().toString());
        amount.setAttribute("Ccy", dto.getOriginalCurrency());

        // Additional Information
        if (dto.getAdditionalInformation() != null) {
            addElement(doc, txInf, "AddtlInf", dto.getAdditionalInformation());
        }

        // Convert to XML
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        StringWriter writer = new StringWriter();
        transformer.transform(new DOMSource(doc), new StreamResult(writer));
        
        return writer.toString();
    }

    private Element addElementt(Document doc, Element parent, String tagName, String textContent) {
        Element element = doc.createElement(tagName);
        if (textContent != null) {
            element.setTextContent(textContent);
        }
        parent.appendChild(element);
        return element;
    }
    //-----------------------------------------------------------generate Pacs.008-----------------------------------------------------------------------
    public String generatePacs009Xml(Pacs009DTO dto) throws Exception {
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
        Document doc = docBuilder.newDocument();

        // Root element with namespace
        Element rootElement = doc.createElement("Document");
        rootElement.setAttribute("xmlns", "urn:iso:std:iso:20022:tech:xsd:pacs.009.001.10");
        doc.appendChild(rootElement);

        // FICdtTrf element
        Element fiCdtTrf = doc.createElement("FICdtTrf");
        rootElement.appendChild(fiCdtTrf);

        // Group Header
        Element grpHdr = doc.createElement("GrpHdr");
        fiCdtTrf.appendChild(grpHdr);
        addElement(doc, grpHdr, "MsgId", dto.getMessageId());
        addElement(doc, grpHdr, "CreDtTm", java.time.LocalDateTime.now().toString());
        addElement(doc, grpHdr, "NbOfTxs", "1");

        // Transaction Information
        Element cdtTrfTxInf = doc.createElement("CdtTrfTxInf");
        fiCdtTrf.appendChild(cdtTrfTxInf);

        // Payment Identification
        Element pmtId = doc.createElement("PmtId");
        cdtTrfTxInf.appendChild(pmtId);
        addElement(doc, pmtId, "InstrId", dto.getInstructionId());
        addElement(doc, pmtId, "EndToEndId", dto.getEndToEndId());

        // Amount
        Element amt = doc.createElement("Amt");
        cdtTrfTxInf.appendChild(amt);
        Element instdAmt = doc.createElement("InstdAmt");
        instdAmt.setAttribute("Ccy", dto.getCurrency());
        instdAmt.setTextContent(dto.getAmount().toString());
        amt.appendChild(instdAmt);

        // Settlement Method
        addElement(doc, cdtTrfTxInf, "SttlmMtd", dto.getSettlementMethod());

        // Transmission Mode (custom field, assume your own extension)
        addElement(doc, cdtTrfTxInf, "TxMode", dto.getTransmissionMode());

        // Debtor
        Element dbtr = doc.createElement("Dbtr");
        cdtTrfTxInf.appendChild(dbtr);
        addElement(doc, dbtr, "Nm", dto.getDebtorName());

        // Creditor
        Element cdtr = doc.createElement("Cdtr");
        cdtTrfTxInf.appendChild(cdtr);
        addElement(doc, cdtr, "Nm", dto.getCreditorName());

        // Debtor Agent (Sender BIC)
        Element dbtrAgt = doc.createElement("DbtrAgt");
        cdtTrfTxInf.appendChild(dbtrAgt);
        Element dbtrAgtFinInstnId = doc.createElement("FinInstnId");
        dbtrAgt.appendChild(dbtrAgtFinInstnId);
        addElement(doc, dbtrAgtFinInstnId, "BICFI", dto.getSenderBic());

        // Creditor Agent (Receiver BIC)
        Element cdtrAgt = doc.createElement("CdtrAgt");
        cdtTrfTxInf.appendChild(cdtrAgt);
        Element cdtrAgtFinInstnId = doc.createElement("FinInstnId");
        cdtrAgt.appendChild(cdtrAgtFinInstnId);
        addElement(doc, cdtrAgtFinInstnId, "BICFI", dto.getReceiverBic());

        // Charge Bearer
        addElement(doc, cdtTrfTxInf, "ChrgBr", dto.getChargeBearer());

        // Convert to XML string
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        StringWriter writer = new StringWriter();
        transformer.transform(new DOMSource(doc), new StreamResult(writer));

        return writer.toString();
    }

    // Helper method
   
    //---------------------------------------------------------Pacs.003 generate------------------------------------------------------------------------
    public String generatePacs003Xml(Pacs003DTO dto) throws Exception {
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
        Document doc = docBuilder.newDocument();

        // Root element with namespace
        Element rootElement = doc.createElement("Document");
        rootElement.setAttribute("xmlns", "urn:iso:std:iso:20022:tech:xsd:pacs.003.001.10");
        doc.appendChild(rootElement);

        // FItoFICstmrDrctDbt element
        Element fiToFiDrctDbt = doc.createElement("FItoFICstmrDrctDbt");
        rootElement.appendChild(fiToFiDrctDbt);

        // Group Header
        Element grpHdr = doc.createElement("GrpHdr");
        fiToFiDrctDbt.appendChild(grpHdr);
        addElement(doc, grpHdr, "MsgId", dto.getMessageId());
        addElement(doc, grpHdr, "CreDtTm", dto.getCreationDateTime().toString());
        addElement(doc, grpHdr, "NbOfTxs", dto.getNumberOfTransactions().toString());
        addElement(doc, grpHdr, "CtrlSum", dto.getControlSum().toString());
        
        // Instructing Agent
        Element instgAgt = doc.createElement("InstgAgt");
        grpHdr.appendChild(instgAgt);
        Element instgFinInstnId = doc.createElement("FinInstnId");
        instgAgt.appendChild(instgFinInstnId);
        addElement(doc, instgFinInstnId, "BICFI", dto.getInstructingAgentBic());
        
        // Instructed Agent
        Element instdAgt = doc.createElement("InstdAgt");
        grpHdr.appendChild(instdAgt);
        Element instdFinInstnId = doc.createElement("FinInstnId");
        instdAgt.appendChild(instdFinInstnId);
        addElement(doc, instdFinInstnId, "BICFI", dto.getInstructedAgentBic());

        // Transaction Information
        Element drctDbtTxInf = doc.createElement("DrctDbtTxInf");
        fiToFiDrctDbt.appendChild(drctDbtTxInf);

        // Payment Identification
        Element pmtId = doc.createElement("PmtId");
        drctDbtTxInf.appendChild(pmtId);
        addElement(doc, pmtId, "EndToEndId", dto.getEndToEndId());

        // Amount
        Element amt = doc.createElement("Amt");
        drctDbtTxInf.appendChild(amt);
        Element instdAmt = doc.createElement("InstdAmt");
        instdAmt.setAttribute("Ccy", dto.getCurrency());
        instdAmt.setTextContent(dto.getAmount().toString());
        amt.appendChild(instdAmt);

        // Debtor Information
        Element dbtr = doc.createElement("Dbtr");
        drctDbtTxInf.appendChild(dbtr);
        addElement(doc, dbtr, "Nm", dto.getDebtorName());
        
        // Debtor Account
        Element dbtrAcct = doc.createElement("DbtrAcct");
        drctDbtTxInf.appendChild(dbtrAcct);
        Element dbtrAcctId = doc.createElement("Id");
        dbtrAcct.appendChild(dbtrAcctId);
        addElement(doc, dbtrAcctId, "IBAN", dto.getDebtorIban());

        // Creditor Information
        Element cdtr = doc.createElement("Cdtr");
        drctDbtTxInf.appendChild(cdtr);
        addElement(doc, cdtr, "Nm", dto.getCreditorName());
        
        // Creditor Account
        Element cdtrAcct = doc.createElement("CdtrAcct");
        drctDbtTxInf.appendChild(cdtrAcct);
        Element cdtrAcctId = doc.createElement("Id");
        cdtrAcct.appendChild(cdtrAcctId);
        addElement(doc, cdtrAcctId, "IBAN", dto.getCreditorIban());

        // Remittance Information
        Element rmtInf = doc.createElement("RmtInf");
        drctDbtTxInf.appendChild(rmtInf);
        addElement(doc, rmtInf, "Ustrd", dto.getRemittanceInfo());

        // Convert to XML string
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        StringWriter writer = new StringWriter();
        transformer.transform(new DOMSource(doc), new StreamResult(writer));
        
        return writer.toString();
    }
   //---------------------------------------------------------------generate Pacs.004------------------------------------------------------------------------------ 
    public String generatePacs004Xml(Pacs004DTO dto) throws Exception {
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
        Document doc = docBuilder.newDocument();

        // Root element with namespace
        Element rootElement = doc.createElement("Document");
        rootElement.setAttribute("xmlns", "urn:iso:std:iso:20022:tech:xsd:pacs.004.001.10");
        doc.appendChild(rootElement);

        // PmtRtr element
        Element pmtRtr = doc.createElement("PmtRtr");
        rootElement.appendChild(pmtRtr);

        // Group Header
        Element grpHdr = doc.createElement("GrpHdr");
        pmtRtr.appendChild(grpHdr);
        addElement(doc, grpHdr, "MsgId", dto.getMessageId());
        addElement(doc, grpHdr, "CreDtTm", java.time.LocalDateTime.now().toString());
        
        // Instructing Agent
        Element instgAgt = doc.createElement("InstgAgt");
        grpHdr.appendChild(instgAgt);
        Element instgFinInstnId = doc.createElement("FinInstnId");
        instgAgt.appendChild(instgFinInstnId);
        addElement(doc, instgFinInstnId, "BICFI", dto.getInstructingAgentBic());
        
        // Instructed Agent
        Element instdAgt = doc.createElement("InstdAgt");
        grpHdr.appendChild(instdAgt);
        Element instdFinInstnId = doc.createElement("FinInstnId");
        instdAgt.appendChild(instdFinInstnId);
        addElement(doc, instdFinInstnId, "BICFI", dto.getInstructedAgentBic());

        // Original Group Information
        Element orgnlGrpInf = doc.createElement("OrgnlGrpInf");
        pmtRtr.appendChild(orgnlGrpInf);
        addElement(doc, orgnlGrpInf, "OrgnlMsgId", dto.getOriginalMessageId());
        addElement(doc, orgnlGrpInf, "OrgnlMsgNmId", dto.getOriginalMessageName());

        // Transaction Information
        Element txInf = doc.createElement("TxInf");
        pmtRtr.appendChild(txInf);
        addElement(doc, txInf, "RtrId", dto.getReturnId());
        addElement(doc, txInf, "OrgnlInstrId", dto.getOriginalInstructionId());
        addElement(doc, txInf, "OrgnlEndToEndId", dto.getOriginalEndToEndId());
        addElement(doc, txInf, "OrgnlTxId", dto.getOriginalTransactionId());

        // Returned Amount
        Element rtrdIntrBkSttlmAmt = doc.createElement("RtrdIntrBkSttlmAmt");
        rtrdIntrBkSttlmAmt.setAttribute("Ccy", dto.getCurrency());
        rtrdIntrBkSttlmAmt.setTextContent(dto.getReturnedAmount().toString());
        txInf.appendChild(rtrdIntrBkSttlmAmt);

        // Return Reason
        Element rtrRsnInf = doc.createElement("RtrRsnInf");
        txInf.appendChild(rtrRsnInf);
        Element rsn = doc.createElement("Rsn");
        rtrRsnInf.appendChild(rsn);
        addElement(doc, rsn, "Cd", dto.getReturnReasonCode());
        addElement(doc, rtrRsnInf, "AddtlInf", dto.getAdditionalInfo());

        // Debtor Information
        Element dbtr = doc.createElement("Dbtr");
        txInf.appendChild(dbtr);
        addElement(doc, dbtr, "Nm", dto.getDebtorName());
        
        // Debtor Account
        Element dbtrAcct = doc.createElement("DbtrAcct");
        txInf.appendChild(dbtrAcct);
        Element dbtrAcctId = doc.createElement("Id");
        dbtrAcct.appendChild(dbtrAcctId);
        addElement(doc, dbtrAcctId, "IBAN", dto.getDebtorIban());

        // Creditor Information
        Element cdtr = doc.createElement("Cdtr");
        txInf.appendChild(cdtr);
        addElement(doc, cdtr, "Nm", dto.getCreditorName());
        
        // Creditor Account
        Element cdtrAcct = doc.createElement("CdtrAcct");
        txInf.appendChild(cdtrAcct);
        Element cdtrAcctId = doc.createElement("Id");
        cdtrAcct.appendChild(cdtrAcctId);
        addElement(doc, cdtrAcctId, "IBAN", dto.getCreditorIban());

        // Remittance Information
        Element rmtInf = doc.createElement("RmtInf");
        txInf.appendChild(rmtInf);
        addElement(doc, rmtInf, "Ustrd", dto.getRemittanceInformation());

        // Convert to XML string
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        StringWriter writer = new StringWriter();
        transformer.transform(new DOMSource(doc), new StreamResult(writer));
        
        return writer.toString();
    }
//-----------------------------------------------------------cmat.053---------------------------------------------------------------------------------
    public String generateStatement(BPDZDir bank, 
            BigDecimal openingBalance,
            BigDecimal closingBalance,
            List<BPDZPmtA> transactions,
            LocalDate statementDate) throws Exception {
         DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
         DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
         Document doc = docBuilder.newDocument();
         
         // Root element with namespace
         Element rootElement = doc.createElement("Document");
         rootElement.setAttribute("xmlns", "urn:iso:std:iso:20022:tech:xsd:camt.053.001.08");
         doc.appendChild(rootElement);
         
         // BankToCustomerStatement
         Element statement = doc.createElement("BankToCustomerStmt");
         rootElement.appendChild(statement);
         
         // Group Header
         Element grpHdr = doc.createElement("GrpHdr");
         statement.appendChild(grpHdr);
         addElement(doc, grpHdr, "MsgId", "STMT" + bank.getBic() + statementDate.format(DateTimeFormatter.BASIC_ISO_DATE));
         addElement(doc, grpHdr, "CreDtTm", java.time.LocalDateTime.now().toString());
         addElement(doc, grpHdr, "MsgRcpt", bank.getBic());
         
         // Statement
         Element stmt = doc.createElement("Stmt");
         statement.appendChild(stmt);
         
         // Statement Identification
         addElement(doc, stmt, "Id", bank.getBic() + "-" + statementDate.getMonthValue() + "-" + statementDate.getYear());
         addElement(doc, stmt, "CreDtTm", java.time.LocalDateTime.now().toString());
         
         // Account
         Element acct = doc.createElement("Acct");
         stmt.appendChild(acct);
         addElement(doc, acct, "Id", bank.getCodeBanque());
         addElement(doc, acct, "Tp", "CACC"); // Current Account
         
         // Balance
         Element bal = doc.createElement("Bal");
         stmt.appendChild(bal);
         addElement(doc, bal, "Tp", "PRCD"); // Previous Closing Balance
         addElement(doc, bal, "Amt", openingBalance.toString());
         addElement(doc, bal, "CdtDbtInd", openingBalance.compareTo(BigDecimal.ZERO) >= 0 ? "CRDT" : "DBIT");
         addElement(doc, bal, "Dt", statementDate.withDayOfMonth(1).toString());
         
         // Transaction entries
         for (BPDZPmtA tx : transactions) {
        	 String xml=tx.getXmlFile();
        	String pacsType = pacsXmlParser.detectPacsType(xml);
        	BigDecimal amount=null ;
        	String endToEndId=null;
        	String transactionId=null;
        	if ("pacs009".equals(pacsType)) {
                Pacs009DTO dto = pacsXmlParser.parsePacs009Xml(xml);
                amount = dto.getAmount();  // ✅ assign amount
                endToEndId=dto.getEndToEndId();
                transactionId=dto.getInstructionId();
            } else if ("pacs008".equals(pacsType)) {
                Pacs008DTO dto = pacsXmlParser.parsePacs008Xml(xml);
                amount = dto.getAmount();  // ✅ assign amount
                endToEndId=dto.getEndToEndId();
                transactionId=dto.getInstructionId();
            }
           
        	 
        	
         Element entry = doc.createElement("Ntry");
         stmt.appendChild(entry);
         
         addElement(doc, entry, "Amt", amount.toString());
         addElement(doc, entry, "CdtDbtInd", 
         tx.getTypeMessage().startsWith("Pacs.008") || tx.getTypeMessage().startsWith("Pacs.009") ? "CRDT" : "DBIT");
         addElement(doc, entry, "BookgDt", tx.getDateArchived().toLocalDate().toString());
         addElement(doc, entry, "ValDt", tx.getDateArchived().toLocalDate().toString());
         
         // Add transaction details
         Element txDtls = doc.createElement("NtryDtls");
         entry.appendChild(txDtls);
         
         Element refs = doc.createElement("Refs");
         txDtls.appendChild(refs);
         addElement(doc, refs, "EndToEndId", endToEndId);
         addElement(doc, refs, "TxId", transactionId);
         
         // Add parties information
         addPartyDetails(doc, txDtls, tx);
         }
         
         // Closing Balance
         Element closingBal = doc.createElement("Bal");
         stmt.appendChild(closingBal);
         addElement(doc, closingBal, "Tp", "CLBD"); // Closing Booked Balance
         addElement(doc, closingBal, "Amt", closingBalance.toString());
         addElement(doc, closingBal, "CdtDbtInd", closingBalance.compareTo(BigDecimal.ZERO) >= 0 ? "CRDT" : "DBIT");
         addElement(doc, closingBal, "Dt", statementDate.withDayOfMonth(statementDate.lengthOfMonth()).toString());
         
         // Convert to XML string
         TransformerFactory transformerFactory = TransformerFactory.newInstance();
         Transformer transformer = transformerFactory.newTransformer();
         StringWriter writer = new StringWriter();
         transformer.transform(new DOMSource(doc), new StreamResult(writer));
         
         return writer.toString();
         }
         
         private void addPartyDetails(Document doc, Element parent, BPDZPmtA tx) {
         Element rltdPties = doc.createElement("RltdPties");
         parent.appendChild(rltdPties);
         
         if (tx.getBanqueEmetteur() != null) {
         Element dbtr = doc.createElement("Dbtr");
         rltdPties.appendChild(dbtr);
         addElement(doc, dbtr, "Nm", tx.getBanqueEmetteur().getNomBanque());
         addElement(doc, dbtr, "Acct", tx.getBanqueEmetteur().getCodeBanque());
         }
         
if (tx.getBanqueRecepteur() != null) {
Element cdtr = doc.createElement("Cdtr");
rltdPties.appendChild(cdtr);
addElement(doc, cdtr, "Nm", tx.getBanqueRecepteur().getNomBanque());
addElement(doc, cdtr, "Acct", tx.getBanqueRecepteur().getCodeBanque());
}
}


}
