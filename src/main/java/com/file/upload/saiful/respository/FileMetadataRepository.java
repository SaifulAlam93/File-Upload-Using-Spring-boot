package com.file.upload.saiful.respository;


import com.file.upload.saiful.entity.FileMetadata;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FileMetadataRepository extends JpaRepository<FileMetadata, Long> {
    Optional<FileMetadata> findByUploadId(String uploadId);
    Optional<FileMetadata> findByFileName(String fileName);
}