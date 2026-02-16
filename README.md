# BPDZ4APA Centralized Interbank Payment Platform

## Project Overview
BPDZ4APA is a national interbank payment platform designed to modernize the country’s financial infrastructure. Inspired by **SEPA** and **TARGET2**, it enables real-time, secure, and standardized transactions between banks using the **ISO 20022** standard. It represents a major step toward a unified digital payment ecosystem supporting financial interoperability and transparency.

## Objectives
- Modernize the interbank payment ecosystem.
- Reduce cash dependency and enhance financial transparency.
- Ensure compliance and interoperability with ISO 20022.
- Simulate a **Real-Time Gross Settlement (RTGS)** system.
- Prepare for multi-currency and cross-border payment expansion.

## Technical Implementation

### Backend
- **Framework:** Spring Boot (Java 17)  
- **Architecture:** MVC, RESTful APIs  
- **Database:** PostgreSQL 17  
- **Messaging Standards:** ISO 20022 (pacs.008, pacs.009, camt.053)  
- **Testing Tools:** Postman, Webhook  
- **Security:** Authentication, encryption, fraud detection  

### Frontend
- **Technologies:** HTML, CSS, JavaScript  
- **Features:** Dashboard, PACS message zone, real-time tracking  
- **Design:** Responsive web interfaces for banks and auditors  

### Database Model
Centralized schema reflecting real interbank operations with unified structures for:
- Banking directories  
- Users  
- Transactions  
- Fraud monitoring  

### Security and Control
- Multi-level authentication and access roles  
- Fraud detection based on stored transaction patterns  
- Encryption and integrity verification of messages  
- Full system logging for audit and compliance  

### System Modeling
- Class diagrams for key entities (banks, accounts, users)  
- Sequence diagrams for ISO 20022 message flows  
- RTGS simulation ensuring instant interbank settlement  

### Testing and Results
- Simulated message exchanges between multiple banks  
- Validation of XML ISO 20022 messages  
- Real-time settlement successfully demonstrated  

## Future Perspectives
- Expansion to multi-currency operations  
- Smart contract integration for automated lending  
- Blockchain-based traceability  

## Keywords
ISO20022 | RTGS | Spring Boot | PostgreSQL | FinTech | Security
