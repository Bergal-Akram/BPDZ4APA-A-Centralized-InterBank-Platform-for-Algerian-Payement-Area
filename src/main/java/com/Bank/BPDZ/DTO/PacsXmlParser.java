package com.Bank.BPDZ.DTO;
import org.springframework.stereotype.Component;
import org.w3c.dom.*;
import javax.xml.parsers.*;
import org.xml.sax.InputSource;

import java.io.ByteArrayInputStream;
import java.io.StringReader;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

@Component
public class PacsXmlParser {

    public Pacs008DTO parsePacs008Xml(String xml) throws Exception {
        // Set up DOM parser
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(new InputSource(new StringReader(xml)));

        Pacs008DTO dto = new Pacs008DTO();

        // Populate fields
        dto.setInstructionId(getText(doc, "InstrId", 0));
        dto.setEndToEndId(getText(doc, "EndToEndId", 0));
        dto.setDebtorName(getText(doc, "Nm", 0));                // 1st <Nm>
        dto.setCreditorName(getText(doc, "Nm", 1));              // 2nd <Nm>
        dto.setDebtorIban(getText(doc, "IBAN", 0));              // 1st <IBAN>
        dto.setCreditorIban(getText(doc, "IBAN", 1));            // 2nd <IBAN>
        dto.setSenderBic(getText(doc, "BICFI", 0));              // 1st <BICFI>
        dto.setReceiverBic(getText(doc, "BICFI", 1));            // 2nd <BICFI>
        dto.setChargeBearer(getText(doc, "ChrgBr", 0));
        dto.setPaymentDetails(getText(doc, "PaymentDetails", 0)); // optional

        // Amount + Currency
        Node amtNode = doc.getElementsByTagName("InstdAmt").item(0);
        if (amtNode != null) {
            dto.setAmount(new BigDecimal(amtNode.getTextContent()));
            dto.setCurrency(amtNode.getAttributes().getNamedItem("Ccy").getTextContent());
        }

        // Transmission Mode
        dto.setTransmissionMode(getText(doc, "TransmissionMode", 0));

        return dto;
    }

    private String getText(Document doc, String tag, int index) {
        NodeList list = doc.getElementsByTagName(tag);
        return (list.getLength() > index) ? list.item(index).getTextContent() : null;
    }
    //-------------------------------------------------------------pars Pacs.009------------------------------------------------------------------------
    public Pacs009DTO parsePacs009Xml(String xml) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(new InputSource(new StringReader(xml)));
        doc.getDocumentElement().normalize();

        Pacs009DTO dto = new Pacs009DTO();

        // Group Header
        Element grpHdr = (Element) doc.getElementsByTagName("GrpHdr").item(0);
        dto.setMessageId(getTextContent(grpHdr, "MsgId"));

        // Transaction Info
        Element cdtTrfTxInf = (Element) doc.getElementsByTagName("CdtTrfTxInf").item(0);

        // Payment Identification
        Element pmtId = (Element) cdtTrfTxInf.getElementsByTagName("PmtId").item(0);
        dto.setInstructionId(getTextContent(pmtId, "InstrId"));
        dto.setEndToEndId(getTextContent(pmtId, "EndToEndId"));

        // Amount
        Element amt = (Element) cdtTrfTxInf.getElementsByTagName("Amt").item(0);
        Element instdAmt = (Element) amt.getElementsByTagName("InstdAmt").item(0);
        dto.setCurrency(instdAmt.getAttribute("Ccy"));
        dto.setAmount(new BigDecimal(instdAmt.getTextContent()));

        // Settlement Method (e.g. CLRG, INGA)
        dto.setSettlementMethod(getTextContent(cdtTrfTxInf, "SttlmMtd"));

        // Transmission Mode (custom, e.g. FILE, MQ)
        dto.setTransmissionMode(getTextContent(cdtTrfTxInf, "TxMode"));

        // Sender BIC (Debtor Agent)
        Element dbtrAgt = (Element) cdtTrfTxInf.getElementsByTagName("DbtrAgt").item(0);
        Element dbtrAgtFinInstnId = (Element) dbtrAgt.getElementsByTagName("FinInstnId").item(0);
        dto.setSenderBic(getTextContent(dbtrAgtFinInstnId, "BICFI"));

        // Receiver BIC (Creditor Agent)
        Element cdtrAgt = (Element) cdtTrfTxInf.getElementsByTagName("CdtrAgt").item(0);
        Element cdtrAgtFinInstnId = (Element) cdtrAgt.getElementsByTagName("FinInstnId").item(0);
        dto.setReceiverBic(getTextContent(cdtrAgtFinInstnId, "BICFI"));

        // Debtor Name
        Element dbtr = (Element) cdtTrfTxInf.getElementsByTagName("Dbtr").item(0);
        dto.setDebtorName(getTextContent(dbtr, "Nm"));

        // Creditor Name
        Element cdtr = (Element) cdtTrfTxInf.getElementsByTagName("Cdtr").item(0);
        dto.setCreditorName(getTextContent(cdtr, "Nm"));

        // Charge Bearer
        dto.setChargeBearer(getTextContent(cdtTrfTxInf, "ChrgBr"));

        return dto;
    }


    // Helper method
    private String getTextContent(Element parent, String tagName) {
        NodeList list = parent.getElementsByTagName(tagName);
        if (list.getLength() > 0) {
            Node node = list.item(0);
            return node.getTextContent();
        }
        return null;
    }
//---------------------------------------------------------------detectPacsType--------------------------------------------------------------------
    public String detectPacsType(String xmlContent) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(new InputSource(new StringReader(xmlContent)));

        // Get the root element and its namespace
        Element root = doc.getDocumentElement();
        String namespace = root.getAttribute("xmlns");

        if (namespace.contains("pacs.009")) {
            return "pacs009";
        }   else if (namespace.contains("pacs.008")) {
            return "pacs008";
        }   else if (namespace.contains("pacs.003")) {
                return "pacs003";
        }  else if (namespace.contains("pacs.004")) {
            return "pacs004";
        }  else {
            return "unknown";
        }
    }
    //--------------------------------------------------------parse Pacs.003-------------------------------------------------------------------------
    public Pacs003DTO parsePacs003Xml(String xml) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8)));

        Pacs003DTO dto = new Pacs003DTO();

        // Root: Document > FItoFICstmrDrctDbt > GrpHdr
        Element root = doc.getDocumentElement();
        Element fiToFi = (Element) root.getElementsByTagName("FItoFICstmrDrctDbt").item(0);
        Element grpHdr = (Element) fiToFi.getElementsByTagName("GrpHdr").item(0);

        dto.setMessageId(getTextContent(grpHdr, "MsgId"));
        dto.setCreationDateTime(LocalDateTime.parse(getTextContent(grpHdr, "CreDtTm")));
        dto.setNumberOfTransactions(Integer.parseInt(getTextContent(grpHdr, "NbOfTxs")));
        dto.setControlSum(new BigDecimal(getTextContent(grpHdr, "CtrlSum")));

        Element instgAgt = (Element) grpHdr.getElementsByTagName("InstgAgt").item(0);
        dto.setInstructingAgentBic(getTextContent(instgAgt, "BICFI"));

        Element instdAgt = (Element) grpHdr.getElementsByTagName("InstdAgt").item(0);
        dto.setInstructedAgentBic(getTextContent(instdAgt, "BICFI"));

        // Transaction Info: DrctDbtTxInf
        Element txInfo = (Element) fiToFi.getElementsByTagName("DrctDbtTxInf").item(0);

        Element pmtId = (Element) txInfo.getElementsByTagName("PmtId").item(0);
        dto.setEndToEndId(getTextContent(pmtId, "EndToEndId"));

        Element amt = (Element) txInfo.getElementsByTagName("Amt").item(0);
        Element instdAmt = (Element) amt.getElementsByTagName("InstdAmt").item(0);
        dto.setAmount(new BigDecimal(instdAmt.getTextContent()));
        dto.setCurrency(instdAmt.getAttribute("Ccy"));

        Element dbtr = (Element) txInfo.getElementsByTagName("Dbtr").item(0);
        dto.setDebtorName(getTextContent(dbtr, "Nm"));

        Element dbtrAcct = (Element) txInfo.getElementsByTagName("DbtrAcct").item(0);
        Element dbtrId = (Element) dbtrAcct.getElementsByTagName("Id").item(0);
        dto.setDebtorIban(getTextContent(dbtrId, "IBAN"));

        Element cdtr = (Element) txInfo.getElementsByTagName("Cdtr").item(0);
        dto.setCreditorName(getTextContent(cdtr, "Nm"));

        Element cdtrAcct = (Element) txInfo.getElementsByTagName("CdtrAcct").item(0);
        Element cdtrId = (Element) cdtrAcct.getElementsByTagName("Id").item(0);
        dto.setCreditorIban(getTextContent(cdtrId, "IBAN"));

        Element rmtInf = (Element) txInfo.getElementsByTagName("RmtInf").item(0);
        dto.setRemittanceInfo(getTextContent(rmtInf, "Ustrd"));

        return dto;
    }
//--------------------------------------------------------------------------------------------------------------------------------------------
    public Pacs004DTO parsePacs004Xml(String xml) throws Exception {
        Pacs004DTO dto = new Pacs004DTO();
        
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(new InputSource(new StringReader(xml)));

        Node root = doc.getDocumentElement(); // Document

        // GrpHdr
        Node grpHdr = getNode(root, "PmtRtr/GrpHdr");
        dto.setMessageId(getText(grpHdr, "MsgId"));
        // Note: CreDtTm is skipped because it's generated dynamically
        dto.setInstructingAgentBic(getText(grpHdr, "InstgAgt/FinInstnId/BICFI"));
        dto.setInstructedAgentBic(getText(grpHdr, "InstdAgt/FinInstnId/BICFI"));

        // OrgnlGrpInf
        Node orgnlGrpInf = getNode(root, "PmtRtr/OrgnlGrpInf");
        dto.setOriginalMessageId(getText(orgnlGrpInf, "OrgnlMsgId"));
        dto.setOriginalMessageName(getText(orgnlGrpInf, "OrgnlMsgNmId"));

        // TxInf
        Node txInf = getNode(root, "PmtRtr/TxInf");
        dto.setReturnId(getText(txInf, "RtrId"));
        dto.setOriginalInstructionId(getText(txInf, "OrgnlInstrId"));
        dto.setOriginalEndToEndId(getText(txInf, "OrgnlEndToEndId"));
        dto.setOriginalTransactionId(getText(txInf, "OrgnlTxId"));

        // Amount
        Element amtElem = (Element) getNode(txInf, "RtrdIntrBkSttlmAmt");
        if (amtElem != null) {
            dto.setCurrency(amtElem.getAttribute("Ccy"));
            dto.setReturnedAmount(new BigDecimal(amtElem.getTextContent()));
        }

        // Return reason
        Node rsn = getNode(txInf, "RtrRsnInf/Rsn");
        dto.setReturnReasonCode(getText(rsn, "Cd"));
        dto.setAdditionalInfo(getText(txInf, "RtrRsnInf/AddtlInf"));

        // Debtor
        dto.setDebtorName(getText(txInf, "Dbtr/Nm"));
        dto.setDebtorIban(getText(txInf, "DbtrAcct/Id/IBAN"));

        // Creditor
        dto.setCreditorName(getText(txInf, "Cdtr/Nm"));
        dto.setCreditorIban(getText(txInf, "CdtrAcct/Id/IBAN"));

        // Remittance info
        dto.setRemittanceInformation(getText(txInf, "RmtInf/Ustrd"));

        return dto;
    }
    private Node getNode(Node parent, String path) {
        String[] parts = path.split("/");
        Node current = parent;
        for (String part : parts) {
            NodeList children = current.getChildNodes();
            boolean found = false;
            for (int i = 0; i < children.getLength(); i++) {
                Node node = children.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE && node.getNodeName().equals(part)) {
                    current = node;
                    found = true;
                    break;
                }
            }
            if (!found) return null;
        }
        return current;
    }

    private String getText(Node parent, String path) {
        Node node = getNode(parent, path);
        return node != null ? node.getTextContent() : null;
    }
    //----------------------------------------------------------------parsePacs002Xml------------------------------------------------------------------------------
    public Pacs002DTO parsePacs002Xml(String xml) throws Exception {
        Pacs002DTO dto = new Pacs002DTO();

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8)));

        Node root = doc.getDocumentElement();

        // Navigate to FIToFIPmtStsRpt
        NodeList reports = doc.getElementsByTagName("FIToFIPmtStsRpt");
        if (reports.getLength() == 0) return dto;

        Element report = (Element) reports.item(0);

        // OrgnlGrpInfAndSts
        Element orgnlGrpInf = (Element) report.getElementsByTagName("OrgnlGrpInfAndSts").item(0);
        if (orgnlGrpInf != null) {
            dto.setOriginalMessageId(getText(orgnlGrpInf, "OrgnlMsgId"));
            dto.setStatus(getText(orgnlGrpInf, "GrpSts"));
        }

        // TxInfAndSts
        Element txInf = (Element) report.getElementsByTagName("TxInfAndSts").item(0);
        if (txInf != null) {
            dto.setOriginalEndToEndId(getText(txInf, "OrgnlEndToEndId"));
            String txSts = getText(txInf, "TxSts");
            if (txSts != null) {
                dto.setStatus(txSts);
            }

            // Status Reason Info (optional)
            Element stsRsnInf = (Element) txInf.getElementsByTagName("StsRsnInf").item(0);
            if (stsRsnInf != null) {
                Element rsn = (Element) stsRsnInf.getElementsByTagName("Rsn").item(0);
                if (rsn != null) {
                    dto.setReasonCode(getText(rsn, "Cd"));
                }
                dto.setReasonText(getText(stsRsnInf, "AddtlInf"));
            }

            // Additional Information
            dto.setAdditionalInformation(getText(txInf, "AddtlInf"));

            // Amount and Currency
            Element orgnlTxRef = (Element) txInf.getElementsByTagName("OrgnlTxRef").item(0);
            if (orgnlTxRef != null) {
                Element amt = (Element) orgnlTxRef.getElementsByTagName("IntrBkSttlmAmt").item(0);
                if (amt != null) {
                    String amountValue = amt.getTextContent();
                    String currency = amt.getAttribute("Ccy");

                    if (amountValue != null && !amountValue.isEmpty()) {
                        dto.setOriginalAmount(new BigDecimal(amountValue));
                    }
                    dto.setOriginalCurrency(currency);
                }
            }
        }

        return dto;
    }

    // Helper method to get tag value
    private String getText(Element parent, String tagName) {
        NodeList list = parent.getElementsByTagName(tagName);
        if (list.getLength() > 0) {
            Node node = list.item(0);
            return node.getTextContent();
        }
        return null;
    }


}
