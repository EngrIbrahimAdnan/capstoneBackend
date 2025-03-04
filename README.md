# Shloanik - Backend Repository


<img src="https://github.com/user-attachments/assets/4440b202-c0dd-4aff-8dfa-21cc926b798d" alt="drawing"/>

## Overview
Shloanik is a centralized platform that allows business owners to apply for loans and receive competitive offers from banks. The system enables banks to compete and counter-offer loan applications, ensuring businesses get the best possible deals. 

The platform incorporates AI-powered insights by analyzing uploaded financial statements and business licenses, leveraging OCR technology to extract and visualize key financial data such as profits, assets, liabilities, and equity. 

## Features
- **Business Loan Application:**
  - Business owners can submit loan requests specifying the purpose, loan amount, term, and repayment plan.
  - Uploaded financial statements and business licenses are analyzed to extract AI-powered insights.
- **Banker Offers & Negotiations:**
  - Bankers can accept, negotiate, or reject loan requests.
  - Business owners and bankers can communicate via a chat system.
- **AI-Powered Insights & OCR:**
  - Automated data extraction from financial statements using OCR.
  - AI-driven financial analysis and data visualizations.
- **Electronic Signatures:**
  - Loan offers must be signed electronically before acceptance.
- **Notifications & Tracking:**
  - Businesses are notified of banker responses.
  - Status updates for loan applications.

<img src="https://github.com/user-attachments/assets/2f19c079-97f2-4ff0-932e-ab72a1640914" alt="drawing"/>

## Tech Stack
- **Backend:** Spring Boot (Java) 
- **Database:** PostgreSQL 
- **Caching:** Redis
- **Containerization:** Docker & Docker Compose
- **AI & Image Processing:** Stable Diffusion (for profile & business logos, using open-source models from CivitAI)
- **File Processing:** Apache PDFBox (for working with PDFs)
- **Authentication:** Refresh Token-based Biometric Authentication
- **Email Services:** Spring Boot Mail
- **Messaging & Animations:**
  - React Native Animated & LottieView (for vector and JSON animations)
  - React Native Paper (for mobile UI)
- **Mapping & Location:** React Native Maps

## Entity Relationship Diagram (ERD)
The system consists of several key entities:

- **User:** Represents all users, including Business Owners, Bankers, and Admins. Each user has a role and may belong to a specific bank.
- **Business:** Links a business owner to their business details, including uploaded financial statements and business licenses.
- **LoanRequest:** Captures loan application details such as loan amount, purpose, term, repayment plan, and the banks involved.
- **LoanResponse:** Stores bankers' responses to loan requests, including acceptance, negotiation, or rejection details.
- **FinancialStatement:** Contains extracted financial data such as revenue, profit, liabilities, and equity.
- **BusinessLicense:** Stores business license details like registration number, issue/expiry dates, and legal entity information.
- **ChatEntity & MessageEntity:** Handles communication between business owners and bankers.
- **FileEntity:** Manages uploaded files such as financial statements and business licenses.
- **Notifications:** Keeps track of system notifications related to loan requests and responses.

Refer to `capstoneERD.drawio.pdf` for a detailed database structure.

## Installation & Setup
### Prerequisites
- Java 17+
- PostgreSQL
- Redis
- Docker & Docker Compose
- Node.js & Expo (for mobile development)

### Backend Setup
1. Clone the repository:
   ```sh
   git clone https://github.com/EngrIbrahimAdnan/capstoneBackend.git
   cd capstoneBackend
   ```
2. Configure environment variables (e.g., database credentials, AI model paths, email configurations).
3. Run the backend service:
   ```sh
   ./mvnw spring-boot:run
   ```
   Or, using Docker:
   ```sh
   docker-compose up --build
   ```

## API Endpoints
Refer to `api-docs.md` (or use Swagger UI if enabled) for a complete list of API routes.

## Contributing
1. Fork the repository.
2. Create a new branch (`feature-branch-name`).
3. Commit your changes.
4. Push to your fork.
5. Create a pull request.

## License
This project is licensed under the MIT License.
