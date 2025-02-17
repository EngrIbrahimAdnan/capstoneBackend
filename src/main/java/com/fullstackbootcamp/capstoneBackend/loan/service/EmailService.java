package com.fullstackbootcamp.capstoneBackend.loan.service;


import com.fullstackbootcamp.capstoneBackend.file.entity.FileEntity;
import com.fullstackbootcamp.capstoneBackend.loan.entity.LoanRequestEntity;
import jakarta.mail.internet.MimeMessage;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.messaging.MessagingException;
import org.springframework.stereotype.Service;

import java.io.File;
import java.net.InetAddress;


@Service
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;

    }

    public void sendVerificationEmail(String toEmail, String recieverName, LoanRequestEntity loanRequest, FileEntity businessAvatar) throws MessagingException {

        String ButtonLink = "http://localhost:3000/";

        // Ensures error is handled if mailSender faces an issue sending an email
        try {
            if (isValidEmail(toEmail)) {
                String subject = "New Loan Request Submitted to Boubyan Bank";
                String emailContent = "<!DOCTYPE html>\n" +
                        "<html>\n" +
                        "<head>\n" +
                        "    <meta charset=\"utf-8\">\n" +
                        "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                        "    <title>Loan Request Notification</title>\n" +
                        "    <style>\n" +
                        "        /* Reset styles for email clients */\n" +
                        "        body, table, td, div, p, a {\n" +
                        "            font-family: Arial, Helvetica, sans-serif;\n" +
                        "            -webkit-text-size-adjust: 100%;\n" +
                        "            -ms-text-size-adjust: 100%;\n" +
                        "        }\n" +
                        "        \n" +
                        "        body {\n" +
                        "            margin: 0;\n" +
                        "            padding: 0;\n" +
                        "            background-color: #1a1a1a;\n" +
                        "        }\n" +
                        "\n" +
                        "        /* Container styles */\n" +
                        "        .container {\n" +
                        "            max-width: 600px;\n" +
                        "            margin: 0 auto;\n" +
                        "            background-color: #242424;\n" +
                        "            padding: 20px;\n" +
                        "        }\n" +
                        "\n" +
                        "        /* Header styles */\n" +
                        "        .header {\n" +
                        "            padding: 20px;\n" +
                        "            text-align: left;\n" +
                        "            background-color: #2a2a2a;\n" +
                        "            border-radius: 10px;\n" +
                        "            margin-bottom: 20px;\n" +
                        "        }\n" +
                        "\n" +
                        "        .header h1 {\n" +
                        "            color: #ffffff;\n" +
                        "            margin: 0;\n" +
                        "            font-size: 24px;\n" +
                        "        }\n" +
                        "\n" +
                        "        /* Card styles */\n" +
                        "        .card {\n" +
                        "            background-color: #2a2a2a;\n" +
                        "            border-radius: 10px;\n" +
                        "            padding: 20px;\n" +
                        "            margin-bottom: 20px;\n" +
                        "            border: 1px solid #333333;\n" +
                        "        }\n" +
                        "\n" +
                        "        .card-title {\n" +
                        "            color: #FFD700;\n" +
                        "            font-size: 18px;\n" +
                        "            font-weight: bold;\n" +
                        "            margin-bottom: 15px;\n" +
                        "            border-bottom: 1px solid #333333;\n" +
                        "            padding-bottom: 10px;\n" +
                        "        }\n" +
                        "\n" +
                        "        .card-content {\n" +
                        "            color: #ffffff;\n" +
                        "        }\n" +
                        "\n" +
                        "        /* Data row styles */\n" +
                        "        .data-row {\n" +
                        "            margin-bottom: 10px;\n" +
                        "        }\n" +
                        "\n" +
                        "        .data-label {\n" +
                        "            color: #999999;\n" +
                        "            font-weight: bold;\n" +
                        "            display: inline-block;\n" +
                        "            width: 140px;\n" +
                        "        }\n" +
                        "\n" +
                        "        .data-value {\n" +
                        "            color: #ffffff;\n" +
                        "        }\n" +
                        "\n" +
                        "        /* Avatar styles */\n" +
                        "        .avatar-container {\n" +
                        "            display: inline-block;\n" +
                        "            vertical-align: middle;\n" +
                        "            margin-right: 10px;\n" +
                        "        }\n" +
                        "\n" +
                        "        .avatar {\n" +
                        "            width: 50px;\n" +
                        "            height: 50px;\n" +
                        "            border-radius: 25px;\n" +
                        "            border: 2px solid #FFD700;\n" +
                        "        }\n" +
                        "\n" +
                        "        /* Notice card styles */\n" +
                        "        .notice-card {\n" +
                        "            background-color: #FFD700;\n" +
                        "            color: #000000;\n" +
                        "            border-radius: 10px;\n" +
                        "            padding: 20px;\n" +
                        "            margin-bottom: 20px;\n" +
                        "        }\n" +
                        "\n" +
                        "        /* Button styles */\n" +
                        "        .button-container {\n" +
                        "            text-align: center;\n" +
                        "            margin: 30px 0;\n" +
                        "        }\n" +
                        "\n" +
                        "        .button {\n" +
                        "            background-color: #FFD700;\n" +
                        "            color: #000000;\n" +
                        "            padding: 15px 30px;\n" +
                        "            text-decoration: none;\n" +
                        "            border-radius: 25px;\n" +
                        "            font-weight: bold;\n" +
                        "            display: inline-block;\n" +
                        "        }\n" +
                        "\n" +
                        "        /* Footer styles */\n" +
                        "        .footer {\n" +
                        "            color: #666666;\n" +
                        "            text-align: center;\n" +
                        "            padding-top: 20px;\n" +
                        "            border-top: 1px solid #333333;\n" +
                        "        }\n" +
                        "\n" +
                        "        /* Responsive styles */\n" +
                        "        @media screen and (max-width: 600px) {\n" +
                        "            .container {\n" +
                        "                width: 100% !important;\n" +
                        "                padding: 10px !important;\n" +
                        "            }\n" +
                        "            \n" +
                        "            .data-label {\n" +
                        "                display: block;\n" +
                        "                width: 100%;\n" +
                        "                margin-bottom: 5px;\n" +
                        "            }\n" +
                        "        }\n" +
                        "    </style>\n" +
                        "</head>\n" +
                        "<body>\n" +
                        "    <div class=\"container\">\n" +
                        "        <div class=\"header\">\n" +
                        "            <h1>Dear ${recieverName},</h1>\n" +
                        "        </div>\n" +
                        "\n" +
                        "        <p style=\"color: #ffffff; margin-bottom: 20px;\">We have received a new loan request at Boubyan Bank. Please review the details below:</p>\n" +
                        "\n" +
                        "        <div class=\"card\">\n" +
                        "            <div class=\"card-title\">Loan Request Details</div>\n" +
                        "            <div class=\"card-content\">\n" +
                        "                <div class=\"data-row\">\n" +
                        "                    <span class=\"data-label\">Loan Title:</span>\n" +
                        "                    <span class=\"data-value\">${loanRequest.getLoanTitle()}</span>\n" +
                        "                </div>\n" +
                        "                <div class=\"data-row\">\n" +
                        "                    <span class=\"data-label\">Loan Purpose:</span>\n" +
                        "                    <span class=\"data-value\">${loanRequest.getLoanPurpose()}</span>\n" +
                        "                </div>\n" +
                        "                <div class=\"data-row\">\n" +
                        "                    <span class=\"data-label\">Loan Amount:</span>\n" +
                        "                    <span class=\"data-value\">${loanRequest.getAmount()}</span>\n" +
                        "                </div>\n" +
                        "                <div class=\"data-row\">\n" +
                        "                    <span class=\"data-label\">Loan Term:</span>\n" +
                        "                    <span class=\"data-value\">${loanRequest.getLoanTerm()}</span>\n" +
                        "                </div>\n" +
                        "                <div class=\"data-row\">\n" +
                        "                    <span class=\"data-label\">Repayment Plan:</span>\n" +
                        "                    <span class=\"data-value\">${loanRequest.getRepaymentPlan()}</span>\n" +
                        "                </div>\n" +
                        "                <div class=\"data-row\">\n" +
                        "                    <span class=\"data-label\">Date Submitted:</span>\n" +
                        "                    <span class=\"data-value\">${loanRequest.getStatusDate()}</span>\n" +
                        "                </div>\n" +
                        "            </div>\n" +
                        "        </div>\n" +
                        "\n" +
                        "        <div class=\"card\">\n" +
                        "            <div class=\"card-title\">Business and Owner Details</div>\n" +
                        "            <div class=\"card-content\">\n" +
                        "                <div class=\"data-row\">\n" +
                        "                    <span class=\"data-label\">Business Owner:</span>\n" +
                        "                    <div class=\"avatar-container\">\n" +
                        "                        <img class=\"avatar\" src=\"cid:ownerAvatar\" alt=\"Owner Avatar\" />\n" +
                        "                    </div>\n" +
                        "                    <span class=\"data-value\">${loanRequest.getBusiness().getBusinessOwnerUser().getLastName()}</span>\n" +
                        "                </div>\n" +
                        "                <div class=\"data-row\">\n" +
                        "                    <span class=\"data-label\">Civil ID:</span>\n" +
                        "                    <span class=\"data-value\">${loanRequest.getBusiness().getBusinessOwnerUser().getCivilId()}</span>\n" +
                        "                </div>\n" +
                        "                <div class=\"data-row\">\n" +
                        "                    <span class=\"data-label\">Mobile Number:</span>\n" +
                        "                    <span class=\"data-value\">${loanRequest.getBusiness().getBusinessOwnerUser().getMobileNumber()}</span>\n" +
                        "                </div>\n" +
                        "                <div class=\"data-row\">\n" +
                        "                    <span class=\"data-label\">Business Name:</span>\n" +
                        "                    <span class=\"data-value\">${loanRequest.getBusiness().getBusinessOwnerUser().getFirstName()} ${loanRequest.getBusiness().getBusinessOwnerUser().getLastName()}</span>\n" +
                        "                </div>\n" +
                        "                <div class=\"data-row\">\n" +
                        "                    <span class=\"data-label\">Financial Score:</span>\n" +
                        "                    <span class=\"data-value\">${loanRequest.getBusiness().getFinancialScore()}</span>\n" +
                        "                </div>\n" +
                        "            </div>\n" +
                        "        </div>\n" +
                        "\n" +
                        "        <div class=\"card\">\n" +
                        "            <div class=\"card-title\">AI Analysis</div>\n" +
                        "            <div class=\"card-content\">\n" +
                        "                ${loanRequest.getBusiness().getFinancialAnalysis()}\n" +
                        "            </div>\n" +
                        "        </div>\n" +
                        "\n" +
                        "        <div class=\"notice-card\">\n" +
                        "            <strong>Important Notice</strong>\n" +
                        "            <p style=\"margin: 10px 0 0 0;\">This loan request has been submitted to other banks. Please act swiftly to review and approve the request.</p>\n" +
                        "        </div>\n" +
                        "\n" +
                        "        <div class=\"button-container\">\n" +
                        "            <a href=\"${ButtonLink}\" class=\"button\">View More Information</a>\n" +
                        "        </div>\n" +
                        "\n" +
                        "        <div class=\"footer\">\n" +
                        "            <p>Best regards,<br>Shloanik Team</p>\n" +
                        "        </div>\n" +
                        "    </div>\n" +
                        "</body>\n" +
                        "</html>";

                // Inside the email-sending method
                File pdfFile = PdfGeneratorService.generateLoanRequestPdf(loanRequest, "src/main/resources/static/ibrahim.png", businessAvatar);


                MimeMessage message = mailSender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(message, true);

                helper.setTo(toEmail);
                helper.setSubject(subject);
                helper.setText(emailContent, true); // `true` indicates HTML content

                FileSystemResource pdfAttachment = new FileSystemResource(pdfFile);
                helper.addAttachment("LoanRequestDetails.pdf", pdfAttachment);

                // Adding the business owner avatar image
                FileSystemResource ownerAvatar = new FileSystemResource(new File("src/main/resources/static/ibrahim.png"));
                helper.addInline("ownerAvatar", ownerAvatar);  // This will match the 'src' in the <img> tag

                // Add inline image for the business avatar (if desired)
                if (businessAvatar != null && businessAvatar.getData() != null) {
                    ByteArrayResource avatarResource = new ByteArrayResource(businessAvatar.getData());
                    helper.addInline("businessAvatar", avatarResource, "image/png"); // or the appropriate type
                }
                mailSender.send(message);
            } else {
                // if email address doesn't conform to an email address structure
                throw new MessagingException("Invalid email address provided.");
            }
        } catch (Exception e) {
            throw new MessagingException("Error sending email.");
        }
    }

    /**
     * Validate an email address.
     *
     * @param email The email address to validate.
     * @return True if the email is valid; false otherwise.
     */
    public boolean isValidEmail(String email) {
        return isValidSyntax(email) && isDomainReachable(email);
    }

    /**
     * Check if the email syntax is valid using regex.
     *
     * @param email The email address to check.
     * @return True if the syntax is valid; false otherwise.
     */
    private boolean isValidSyntax(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        return email != null && email.matches(emailRegex);
    }

    /**
     * Check if the domain of the email is reachable.
     *
     * @param email The email address to check.
     * @return True if the domain is reachable; false otherwise.
     */
    private boolean isDomainReachable(String email) {
        try {
            String domain = email.substring(email.indexOf("@") + 1);
            InetAddress.getByName(domain);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}