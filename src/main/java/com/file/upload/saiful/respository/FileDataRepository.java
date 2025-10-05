package com.file.upload.saiful.respository;

import com.file.upload.saiful.entity.FileData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface FileDataRepository extends JpaRepository<FileData,Long> {
   

    @Query(value = "SELECT * FROM FILE_DATA where name = :name limit 1", nativeQuery = true)
    public FileData findAllSortedByNameUsingNative(@Param(value = "name") String name );

    Optional<FileData> findByName(String name);
}
