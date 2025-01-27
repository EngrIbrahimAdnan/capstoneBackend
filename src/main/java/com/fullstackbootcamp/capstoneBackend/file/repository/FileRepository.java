package com.fullstackbootcamp.capstoneBackend.file.repository;

import com.fullstackbootcamp.capstoneBackend.file.entity.FileEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<FileEntity, Long> {
}

