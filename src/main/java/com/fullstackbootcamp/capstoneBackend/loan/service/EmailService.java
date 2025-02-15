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
                String emailContent = "<html>"
                        + "<head>"
                        + "<style>"
                        + "body { font-family: Arial, sans-serif; color: #333; padding: 20px; }"
                        + "h1 { color: #0056b3; }"
                        + ".content { border: 1px solid #ddd; padding: 20px; background-color: #f9f9f9; border-radius: 8px; }"
                        + ".section-title { font-size: 18px; font-weight: bold; margin-bottom: 10px; }"
                        + ".section-content { margin-bottom: 15px; }"
                        + ".button { display: inline-block; padding: 10px 20px; background-color: #0056b3; color: white; text-decoration: none; border-radius: 5px; }"
                        + ".avatar { width: 80px; height: 80px; border-radius: 50%; margin-right: 10px; }"
                        + ".image-container { margin-top: 20px; }"
                        + "</style>"
                        + "</head>"
                        + "<body>"
                        + "<h1>Dear " + recieverName + ",</h1>"
                        + "<p>We have received a new loan request at Boubyan Bank. Please review the details below:</p>"

                        + "<div class='content'>"
                        + "<div class='section-title'>Loan Request Details</div>"
                        + "<div class='section-content'><strong>Loan Title:</strong> " + loanRequest.getLoanTitle() + "</div>"
                        + "<div class='section-content'><strong>Loan Purpose:</strong> " + loanRequest.getLoanPurpose() + " </div>"
                        + "<div class='section-content'><strong>Loan Amount:</strong> " + loanRequest.getAmount() + " </div>"
                        + "<div class='section-content'><strong>Loan Term:</strong> " + loanRequest.getLoanTerm() + " </div>"
                        + "<div class='section-content'><strong>Repayment Plan:</strong> " + loanRequest.getRepaymentPlan() + " </div>"
                        + "<div class='section-content'><strong>Date Submitted:</strong> " + loanRequest.getStatusDate() + " </div>"
                        + "</div>"

                        + "<div class='content'>"
                        + "<div class='section-title'>Business and Owner Details</div>"
                        + "<div class='section-content'><strong>Business Owner:</strong> <img class='avatar' src='cid:ownerAvatar' alt='Owner Avatar' /> " + loanRequest.getBusiness().getBusinessOwnerUser().getLastName() + "</div>"
                        + "<div class='section-content'><strong>Civil ID:</strong> " + loanRequest.getBusiness().getBusinessOwnerUser().getCivilId() + " </div>"
                        + "<div class='section-content'><strong>Mobile Number:</strong> " + loanRequest.getBusiness().getBusinessOwnerUser().getMobileNumber() + " </div>"

                        + "<div class='section-content'><strong>Business Owner:</strong> <img class='avatar' src='cid:businessAvatar' alt='Owner Avatar' /> " + loanRequest.getBusiness().getBusinessOwnerUser().getLastName() + " </div>"
                        + "<div class='section-content'><strong>Business Name:</strong> " + loanRequest.getBusiness().getBusinessOwnerUser().getFirstName() + " " + loanRequest.getBusiness().getBusinessOwnerUser().getLastName() + " </div>"
                        + "<div class='section-content'><strong>AI Analysis:</strong> " + loanRequest.getBusiness().getFinancialAnalysis() + " </div>"
                        + "<div class='section-content'><strong>Financial Score:</strong> " + loanRequest.getBusiness().getFinancialScore() + " </div>"
                        + "</div>"

                        + "<div class='content' style='background-color: #ffeb3b;'>"
                        + "<div class='section-title'>Important Notice</div>"
                        + "<div class='section-content'>This loan request has been submitted to other banks. Please act swiftly to review and approve the request.</div>"
                        + "</div>"

                        + "<div class='image-container'>\n" +
                        "    <p>\n" +
                        "        <a href='" + ButtonLink + "' class='button' style='\n" +
                        "            background-color: #FF5733; /* Red/Orange background */\n" +
                        "            color: white; /* White text */\n" +
                        "            padding: 12px 24px; /* Padding for a better size */\n" +
                        "            text-align: center; /* Center text */\n" +
                        "            text-decoration: none; /* Remove underline */\n" +
                        "            font-size: 16px; /* Adjust font size */\n" +
                        "            border-radius: 8px; /* Rounded corners */\n" +
                        "            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1); /* Subtle shadow */\n" +
                        "            transition: background-color 0.3s ease, transform 0.3s ease; /* Smooth transitions */\n" +
                        "        '\n" +
                        "        onmouseover='this.style.backgroundColor=\"#FF9500\"; this.style.transform=\"scale(1.05)\";' /* Hover effect */\n" +
                        "        onmouseout='this.style.backgroundColor=\"#FF5733\"; this.style.transform=\"scale(1)\";' /* Hover out effect */\n" +
                        "        >\n" +
                        "            Click here to view more information\n" +
                        "        </a>\n" +
                        "    </p>\n" +
                        "</div>\n"

                        + "<p>Best regards,</p>"
                        + "<p>Shloanik Team</p>"
                        + "</body>"
                        + "</html>";

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