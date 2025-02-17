package com.fullstackbootcamp.capstoneBackend.file.service;

import com.fullstackbootcamp.capstoneBackend.file.entity.FileEntity;
import com.fullstackbootcamp.capstoneBackend.file.repository.FileRepository;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Optional;

@Service
public class FileServiceImpl implements FileService {

    private final FileRepository fileRepository;

    public FileServiceImpl(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    // save file in a repository
    public FileEntity saveFile(MultipartFile file) throws Exception {

        // TODO: further error handling to ensure the file is indeed image/jpeg and not text file
        FileEntity fileEntity = new FileEntity();

        // get file name
        fileEntity.setName(file.getOriginalFilename());

        // Set content type (i.e., "image/jpeg")
        fileEntity.setType(file.getContentType());

        // Set file size (currently set to 5MB max, see application.properties)
        fileEntity.setData(file.getBytes());

        return fileRepository.save(fileEntity);
    }

    @Override
    public FileEntity convertAndSaveAsPDF(MultipartFile imageFile) throws Exception {
        if (!imageFile.getContentType().startsWith("image/")) {
            throw new IllegalArgumentException("Invalid file type. Only images can be converted to PDF.");
        }

        byte[] pdfData = convertImageToPDF(imageFile);

        FileEntity fileEntity = new FileEntity();
        fileEntity.setName(imageFile.getOriginalFilename().replaceAll("\\.[^.]+$", "") + ".pdf");
        fileEntity.setType("application/pdf");
        fileEntity.setData(pdfData);

        return fileRepository.save(fileEntity);
    }

    private byte[] convertImageToPDF(MultipartFile imageFile) throws IOException {
        PDDocument document = new PDDocument();
        PDPage page = new PDPage(PDRectangle.A4);
        document.addPage(page);

        // Convert MultipartFile to byte array
        byte[] imageBytes = imageFile.getBytes();

        PDImageXObject image = PDImageXObject.createFromByteArray(document, imageBytes, imageFile.getOriginalFilename());

        // Adjust image scaling to fit the page
        float pageWidth = page.getMediaBox().getWidth();
        float pageHeight = page.getMediaBox().getHeight();
        float imageWidth = image.getWidth();
        float imageHeight = image.getHeight();

        float scaleFactor = Math.min(pageWidth / imageWidth, pageHeight / imageHeight);

        float scaledWidth = imageWidth * scaleFactor;
        float scaledHeight = imageHeight * scaleFactor;
        float xOffset = (pageWidth - scaledWidth) / 2;
        float yOffset = (pageHeight - scaledHeight) / 2;

        PDPageContentStream contentStream = new PDPageContentStream(document, page);
        contentStream.drawImage(image, xOffset, yOffset, scaledWidth, scaledHeight);
        contentStream.close();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        document.save(outputStream);
        document.close();

        return outputStream.toByteArray();
    }

    public Optional<FileEntity> getFile(Long id) {
        return fileRepository.findById(id);
    }
}
