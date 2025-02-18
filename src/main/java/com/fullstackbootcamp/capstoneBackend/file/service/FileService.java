package com.fullstackbootcamp.capstoneBackend.file.service;

import com.fullstackbootcamp.capstoneBackend.file.entity.FileEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

public interface FileService {
    public FileEntity convertAndSaveAsPDF(MultipartFile imageFile) throws Exception;
    FileEntity saveFile(MultipartFile file) throws Exception;
    Optional<FileEntity> getFile(Long id);
}
