package com.fullstackbootcamp.capstoneBackend.file.service;

import com.fullstackbootcamp.capstoneBackend.file.entity.FileEntity;
import com.fullstackbootcamp.capstoneBackend.file.repository.FileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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

    public Optional<FileEntity> getFile(Long id) {
        return fileRepository.findById(id);
    }
}
