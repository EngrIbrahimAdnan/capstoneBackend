package com.fullstackbootcamp.capstoneBackend.loan.service;

import com.fullstackbootcamp.capstoneBackend.file.entity.FileEntity;
import com.fullstackbootcamp.capstoneBackend.loan.entity.LoanRequestEntity;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileOutputStream;

public class PdfGeneratorService {

    public static File generateLoanRequestPdf(LoanRequestEntity loanRequest, String businessOwnerAvatarPath, FileEntity businessAvatar) throws Exception {
        // Create a document object and set the page size
        Document document = new Document(PageSize.A4);
        File tempFile = File.createTempFile("LoanRequest_", ".pdf");
        FileOutputStream outputStream = new FileOutputStream(tempFile);

        // Initialize PdfWriter
        PdfWriter.getInstance(document, outputStream);
        document.open();

        // Add a title
        Font titleFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
        Paragraph title = new Paragraph("Loan Request Details", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);

        // Add a space
        document.add(Chunk.NEWLINE);

        // Add greeting message
        Font greetingFont = new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL);
        Paragraph greeting = new Paragraph("Dear Banker,", greetingFont);
        document.add(greeting);

        // Add new loan request info
        Paragraph loanInfo = new Paragraph("A new loan request has been submitted to Boubyan Bank" , greetingFont);
        document.add(loanInfo);

        // Add loan request details
        Font labelFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
        Font valueFont = new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL);

        document.add(new Paragraph("Loan Details:", labelFont));
        document.add(new Paragraph("Loan Title: " + loanRequest.getLoanTitle(), valueFont));
        document.add(new Paragraph("Loan Purpose: " + loanRequest.getLoanPurpose(), valueFont));
        document.add(new Paragraph("Loan Amount: " + loanRequest.getAmount(), valueFont));
        document.add(new Paragraph("Loan Term: " + loanRequest.getLoanTerm(), valueFont));
        document.add(new Paragraph("Repayment Plan: " + loanRequest.getRepaymentPlan(), valueFont));
        document.add(new Paragraph("Date: " + loanRequest.getStatusDate(), valueFont));

        // Add business and owner details
        document.add(new Paragraph("Business and Owner Details:", labelFont));
        document.add(new Paragraph("Business Owner: " + loanRequest.getBusiness().getBusinessOwnerUser().getFirstName()+" "+loanRequest.getBusiness().getBusinessOwnerUser().getFirstName(), valueFont));
        document.add(new Paragraph("Civil ID: " + loanRequest.getBusiness().getBusinessOwnerUser().getCivilId(), valueFont));
        document.add(new Paragraph("Business Owner: " + loanRequest.getBusiness().getBusinessNickname(), valueFont));
        document.add(new Paragraph("Business Financial Score: " + loanRequest.getBusiness().getFinancialScore(), valueFont));
        document.add(new Paragraph("Business analysis: " + loanRequest.getBusiness().getFinancialAnalysis(), valueFont));

        // Adding avatars to PDF
        try {
            Image businessOwnerAvatar = Image.getInstance(businessOwnerAvatarPath);
            businessOwnerAvatar.scaleToFit(50, 50); // Scale the avatar size
            document.add(businessOwnerAvatar);
            document.add(new Paragraph("Business Owner Avatar"));

        } catch (Exception e) {
            e.printStackTrace(); // Handle error if avatars can't be loaded
        }

        // Add the business avatar (from byte[])
        try {
            if (businessAvatar != null && businessAvatar.getData() != null) {
                Image avatarImage = Image.getInstance(businessAvatar.getData());
                avatarImage.scaleToFit(50, 50); // Adjust size as necessary
                document.add(avatarImage);
                document.add(new Paragraph("Business Avatar"));
            } else {
                document.add(new Paragraph("No Business Avatar Available"));
            }
        } catch (Exception e) {
            e.printStackTrace(); // Handle error if avatar can't be loaded
        }


        // Add footer
        Paragraph footer = new Paragraph("It should be noted that this request has also been submitted to other banks.");
        document.add(footer);

        // Closing the document
        document.close();
        return tempFile;
    }
}
