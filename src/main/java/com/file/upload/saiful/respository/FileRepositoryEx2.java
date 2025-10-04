package com.file.upload.saiful.respository;


import com.file.upload.saiful.entity.FileEntityEx2;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileRepositoryEx2 extends JpaRepository<FileEntityEx2, Long> {
}
