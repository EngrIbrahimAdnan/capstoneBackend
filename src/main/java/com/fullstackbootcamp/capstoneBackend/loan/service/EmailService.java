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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


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
                        "        body {\n" +
                        "            margin: 0;\n" +
                        "            padding: 30px;\n" +
                        "            background: linear-gradient(135deg, #1a1a1a 0%, #0a0a0a 100%);\n" +
                        "            font-family: 'Segoe UI', Arial, sans-serif;\n" +
                        "            color: #ffffff;\n" +
                        "            line-height: 1.6;\n" +
                        "        }\n" +
                        "\n" +
                        "        .container {\n" +
                        "            max-width: 800px;\n" +
                        "            margin: 0 auto;\n" +
                        "            background: linear-gradient(145deg, rgba(35, 35, 35, 0.9), rgba(25, 25, 25, 0.9));\n" +
                        "            border: 1px solid rgba(255, 215, 0, 0.1);\n" +
                        "            border-radius: 20px;\n" +
                        "            padding: 40px;\n" +
                        "            box-shadow: 0 20px 40px rgba(0, 0, 0, 0.3);\n" +
                        "        }\n" +
                        "\n" +
                        "        .header {\n" +
                        "            text-align: center;\n" +
                        "            margin-bottom: 40px;\n" +
                        "            padding-bottom: 30px;\n" +
                        "            border-bottom: 1px solid rgba(255, 215, 0, 0.1);\n" +
                        "        }\n" +
                        "\n" +
                        "        .header h1 {\n" +
                        "            color: #999;\n" +
                        "            font-size: 28px;\n" +
                        "            -webkit-background-clip: text;\n" +
                        "            -webkit-text-fill-color: transparent;\n" +
                        "            margin-bottom: 15px;\n" +
                        "        }\n" +
                        "\n" +
                        "        .header p {\n" +
                        "            color: #999;\n" +
                        "            margin: 0;\n" +
                        "            font-size: 18px;\n" +

                        "        }\n" +
                        "\n" +
                        "        .card {\n" +
                        "            background: rgba(255, 255, 255, 0.03);\n" +
                        "            border: 1px solid rgba(255, 255, 255, 0.05);\n" +
                        "            border-radius: 15px;\n" +
                        "            padding: 25px;\n" +
                        "            margin-bottom: 30px;\n" +
                        "            transition: transform 0.3s ease;\n" +
                        "        }\n" +
                        "\n" +
                        "        .card:hover {\n" +
                        "            transform: translateY(-5px);\n" +
                        "        }\n" +
                        "\n" +
                        "        .card-title {\n" +
                        "            font-size: 20px;\n" +
                        "            font-weight: 600;\n" +
                        "            margin-bottom: 20px;\n" +
                        "            padding-bottom: 15px;\n" +
                        "            border-bottom: 1px solid rgba(255, 215, 0, 0.1);\n" +
                        "            color: #FFD700;\n" +
                        "            display: flex;\n" +
                        "            align-items: center;\n" +
                        "            gap: 10px;\n" +
                        "        }\n" +
                        "\n" +
                        "        .data-grid {\n" +
                        "            display: grid;\n" +
                        "            grid-template-columns: repeat(2, 1fr);\n" +
                        "            gap: 20px;\n" +
                        "        }\n" +
                        "\n" +
                        "        .data-item {\n" +
                        "            background: rgba(255, 255, 255, 0.02);\n" +
                        "            padding: 15px;\n" +
                        "            border-radius: 10px;\n" +
                        "        }\n" +
                        "\n" +
                        "        .data-label {\n" +
                        "            color: #999;\n" +
                        "            font-size: 14px;\n" +
                        "            margin-bottom: 5px;\n" +
                        "        }\n" +
                        "\n" +
                        "        .data-value {\n" +
                        "            color: #fff;\n" +
                        "            font-size: 16px;\n" +
                        "            font-weight: 500;\n" +
                        "        }\n" +
                        "\n" +
                        "        .profile-section {\n" +
                        "            display: flex;\n" +
                        "            align-items: center;\n" +
                        "            gap: 20px;\n" +
                        "            padding: 20px;\n" +
                        "            background: rgba(255, 255, 255, 0.02);\n" +
                        "            border-radius: 15px;\n" +
                        "            margin-bottom: 20px;\n" +
                        "        }\n" +
                        "\n" +
                        "        .avatar {\n" +
                        "            width: 80px;\n" +
                        "            height: 80px;\n" +
                        "            border-radius: 40px;\n" +
                        "            border: 3px solid #FFD700;\n" +
                        "            box-shadow: 0 0 20px rgba(255, 215, 0, 0.2);\n" +
                        "            margin-bottom: 20;\n" +

                        "        }\n" +
                        "\n" +
                        "        .profile-info h3 {\n" +
                        "            margin: 0;\n" +
                        "            color: #fff;\n" +
                        "            font-size: 18px;\n" +
                        "        }\n" +
                        "\n" +
                        "        .profile-info p {\n" +
                        "            margin: 5px 0 0;\n" +
                        "            color: #999;\n" +
                        "        }\n" +
                        "\n" +
                        "        .notice {\n" +
                        "            background: linear-gradient(45deg, #FFD700, #FFA500);\n" +
                        "            color: #000;\n" +
                        "            padding: 20px;\n" +
                        "            border-radius: 15px;\n" +
                        "            margin: 30px 0;\n" +
                        "            position: relative;\n" +
                        "            overflow: hidden;\n" +
                        "        }\n" +
                        "\n" +
                        "        .notice::before {\n" +
                        "            content: '!';\n" +
                        "            position: absolute;\n" +
                        "            right: -10px;\n" +
                        "            top: -20px;\n" +
                        "            font-size: 100px;\n" +
                        "            opacity: 0.1;\n" +
                        "            font-weight: bold;\n" +
                        "        }\n" +
                        "\n" +
                        "        .button {\n" +
                        "            display: inline-block;\n" +
                        "            padding: 15px 30px;\n" +
                        "            background: linear-gradient(45deg, #FFD700, #FFA500);\n" +
                        "            color: #000;\n" +
                        "            text-decoration: none;\n" +
                        "            border-radius: 25px;\n" +
                        "            font-weight: 600;\n" +
                        "            text-align: center;\n" +
                        "            transition: all 0.3s ease;\n" +
                        "            border: none;\n" +
                        "            box-shadow: 0 5px 15px rgba(255, 215, 0, 0.2);\n" +
                        "        }\n" +
                        "\n" +
                        "        .button:hover {\n" +
                        "            transform: translateY(-2px);\n" +
                        "            box-shadow: 0 8px 20px rgba(255, 215, 0, 0.3);\n" +
                        "        }\n" +
                        "\n" +
                        "        .metrics {\n" +
                        "            display: grid;\n" +
                        "            grid-template-columns: repeat(2, 1fr);\n" +
                        "            gap: 20px;\n" +
                        "            margin-bottom: 20px;\n" +
                        "        }\n" +
                        "\n" +
                        "        .metric-card {\n" +
                        "            background: rgba(255, 255, 255, 0.02);\n" +
                        "            padding: 20px;\n" +
                        "            border-radius: 15px;\n" +
                        "            text-align: center;\n" +
                        "        }\n" +
                        "\n" +
                        "        .metric-value {\n" +
                        "            font-size: 24px;\n" +
                        "            font-weight: 600;\n" +
                        "            color: #FFD700;\n" +
                        "            margin-bottom: 5px;\n" +
                        "        }\n" +
                        "\n" +
                        "        .metric-label {\n" +
                        "            color: #999;\n" +
                        "            font-size: 14px;\n" +
                        "            text-transform: uppercase;\n" +
                        "            letter-spacing: 0.5px;\n" +
                        "        }\n" 
                        "\n" +
                        "        .logo {\n" +
                        "            width: 300px; /* Adjust size as needed */\n" +
                        "            height: auto;\n" +
                        "            margin-bottom: 10px;\n" +
                        "        }\n" +

                        "        .footer {\n" +
                        "            text-align: center;\n" +
                        "            margin-top: 40px;\n" +
                        "            padding-top: 30px;\n" +
                        "            font-size: 16px;\n" +

                        "            border-top: 1px solid rgba(255, 255, 255, 0.1);\n" +
                        "            color: #666;\n" +
                        "        }\n" +
                        "\n" +
                        "        @media screen and (max-width: 768px) {\n" +
                        "            .data-grid {\n" +
                        "                grid-template-columns: 1fr;\n" +
                        "            }\n" +
                        "            \n" +
                        "            .metrics {\n" +
                        "                grid-template-columns: 1fr;\n" +
                        "            }\n" +
                        "        }\n" +
                        "    </style>\n" +
                        "</head>\n" +
                        "<body>\n" +
                        "    <div class=\"container\">\n" +
                        "        <div class=\"header\">\n" +
                        "            <img class=\"logo\" src=\"cid:shloanikAvatar\" alt=\"Shloanik Logo\" />\n" +
                        "            <h1>New Loan Request</h1>\n" +
                        "            <p>We have received a new loan request for Boubyan Bank. Please review the details below:</p>\n" +
                        "        </div>\n" +
                        "\n" +
                        "        <div class=\"card\">\n" +
                        "            <div class=\"card-title\"> Loan Request Details</div>\n" +
                        "            <div class=\"data-grid\">\n" +
                        "                <div class=\"data-item\">\n" +
                        "                    <div class=\"data-label\">Loan Title</div>\n" +
                        "                    <div class=\"data-value\">" + loanRequest.getLoanTitle() + "</div>\n" +
                        "                </div>\n" +
                        "                <div class=\"data-item\">\n" +
                        "                    <div class=\"data-label\">Loan Purpose</div>\n" +
                        "                    <div class=\"data-value\">" + loanRequest.getLoanPurpose() + "</div>\n" +
                        "                </div>\n" +
                        "                <div class=\"data-item\">\n" +
                        "                    <div class=\"data-label\">Loan Amount</div>\n" +
                        "                    <div class=\"data-value\">" + loanRequest.getAmount() + "</div>\n" +
                        "                </div>\n" +
                        "                <div class=\"data-item\">\n" +
                        "                    <div class=\"data-label\">Loan Term</div>\n" +
                        "                    <div class=\"data-value\">" + formatString(loanRequest.getLoanTerm().toString()) + "</div>\n" +
                        "                </div>\n" +
                        "                <div class=\"data-item\">\n" +
                        "                    <div class=\"data-label\">Repayment Plan</div>\n" +
                        "                    <div class=\"data-value\">" + formatString(loanRequest.getRepaymentPlan().toString()) + "</div>\n" +
                        "                </div>\n" +
                        "                <div class=\"data-item\">\n" +
                        "                    <div class=\"data-label\">Date Submitted</div>\n" +
                        "                    <div class=\"data-value\">" + DateTimeFormatter(loanRequest.getStatusDate()) + "</div>\n" +
                        "                </div>\n" +
                        "            </div>\n" +
                        "        </div>\n" +
                        "\n" +
                        "        <div class=\"card\">\n" +
                        "            <div class=\"card-title\"> Business and Owner Details</div>\n" +
                        "            <div class=\"profile-section\">\n" +
                        "                <img class=\"avatar\" src=\"cid:ownerAvatar\" alt=\"Owner Avatar\" />\n" +
                        "                <div class=\"profile-info\">\n" +
                        "                    <h3>" + formatString(loanRequest.getBusiness().getBusinessOwnerUser().getFirstName() + "_" + loanRequest.getBusiness().getBusinessOwnerUser().getLastName()) + "</h3>\n" +
                        "                    <p>Business Owner</p>\n" +
                        "                </div>\n" +
                        "            </div>\n" +

                        "            <div class=\"profile-section\">\n" +
                        "                <img class=\"avatar\" src=\"cid:businessAvatar\" alt=\"Business Avatar\" />\n" +
                        "                <div class=\"profile-info\">\n" +
                        "                    <h3>" + loanRequest.getBusiness().getBusinessNickname() + "</h3>\n" +
                        "                    <p>Business License ID: #29398492049</p>\n" +
                        "                </div>\n" +
                        "            </div>\n" +
                        "\n" +
                        "            <div class=\"metrics\">\n" +
                        "                <div class=\"metric-card\">\n" +
                        "                    <div class=\"metric-value\">" + loanRequest.getBusiness().getFinancialScore() + "</div>\n" +
                        "                    <div class=\"metric-label\">Financial Score</div>\n" +
                        "                </div>\n" +
                        "                <div class=\"metric-card\">\n" +
                        "                    <div class=\"metric-value\">" + formatString(loanRequest.getLoanTerm().toString()) + "</div>\n" +
                        "                    <div class=\"metric-label\">Term Length</div>\n" +
                        "                </div>\n" +
                        "            </div>\n" +
                        "\n" +
                        "            <div class=\"data-grid\">\n" +
                        "                <div class=\"data-item\">\n" +
                        "                    <div class=\"data-label\">Civil ID</div>\n" +
                        "                    <div class=\"data-value\">" + loanRequest.getBusiness().getBusinessOwnerUser().getCivilId() + "</div>\n" +
                        "                </div>\n" +
                        "                <div class=\"data-item\">\n" +
                        "                    <div class=\"data-label\">Mobile Number</div>\n" +
                        "                    <div class=\"data-value\">" + loanRequest.getBusiness().getBusinessOwnerUser().getMobileNumber() + "</div>\n" +
                        "                </div>\n" +
                        "                <div class=\"data-item\">\n" +
                        "                    <div class=\"data-label\">Business Name</div>\n" +
                        "                    <div class=\"data-value\">" + formatString(loanRequest.getBusiness().getBusinessOwnerUser().getFirstName() + "_" + loanRequest.getBusiness().getBusinessOwnerUser().getLastName()) + "</div>\n" +
                        "                </div>\n" +
                        "            </div>\n" +
                        "        </div>\n" +
                        "\n" +
                        "        <div class=\"card\">\n" +
                        "            <div class=\"card-title\"> AI Analysis</div>\n" +
                        "            <p style=\"color: #fff; font-size: 15px; line-height: 1.6; margin: 0;\">\n" +
                        "                " + loanRequest.getBusiness().getFinancialAnalysis() + "\n" +
                        "            </p>\n" +
                        "        </div>\n" +
                        "\n" +
                        "        <div class=\"notice\">\n" +
                        "            <strong>Important Notice</strong>\n" +
                        "            <p style=\"margin: 10px 0 0 0;\">This loan request has been submitted to other banks. Please act swiftly to review and approve the request.</p>\n" +
                        "        </div>\n" +
                        "\n" +
                        "        <div style=\"text-align: center;\">\n" +
                        "            <a href=\"" + ButtonLink + "\" class=\"button\">View More Information</a>\n" +
                        "        </div>\n" +
                        "\n" +
                        "        <div class=\"footer\">\n" +
                        "            <p>Best regards,<br><strong style=\"color: #FFD700;\">Shloanik Team</strong></p>\n" +

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
                FileSystemResource shloanikAvatar = new FileSystemResource(new File("src/main/resources/static/logo.png"));
                helper.addInline("shloanikAvatar", shloanikAvatar);  // This will match the 'src' in the <img> tag


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

    public static String DateTimeFormatter(LocalDateTime input) {
        // Define the desired format
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        // Format the LocalDateTime
        String formattedDate = input.format(formatter);

        // Print the result
        return formattedDate;
    }


    public static String formatString(String input) {
        String[] words = input.toLowerCase().split("_");
        StringBuilder result = new StringBuilder();

        for (String word : words) {
            if (!word.isEmpty()) {
                result.append(Character.toUpperCase(word.charAt(0)))
                        .append(word.substring(1))
                        .append(" ");
            }
        }

        return result.toString().trim();
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