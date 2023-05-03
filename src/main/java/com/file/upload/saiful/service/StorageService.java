package com.file.upload.saiful.service;

import com.file.upload.saiful.dto.FileInfo;
import com.file.upload.saiful.entity.FileData;
import com.file.upload.saiful.entity.ImageData;
import com.file.upload.saiful.respository.FileDataRepository;
import com.file.upload.saiful.respository.StorageRepository;
import com.file.upload.saiful.util.ImageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class StorageService {

    @Autowired
    private StorageRepository repository;

    @Autowired
    private FileDataRepository fileDataRepository;

    private final String FOLDER_PATH="H://img/";

    public String uploadImage(MultipartFile file) throws IOException {

        ImageData imageData = repository.save(ImageData.builder()
                .name(file.getOriginalFilename())
                .type(file.getContentType())
                .imageData(ImageUtils.compressImage(file.getBytes())).build());

        if (imageData != null) {
            return "file uploaded successfully : " + file.getOriginalFilename();
        }
        return null;
    }



    public byte[] downloadImage(String fileName) {
        Optional<ImageData> dbImageData = repository.findByName(fileName);
        byte[] images = ImageUtils.decompressImage(dbImageData.get().getImageData());
        return images;
    }


    public List<FileInfo> downloadImageList() throws IOException {
        List<FileInfo> fileInfos = new ArrayList<>();
        List<ImageData> fileDataList = repository.findAll();
        fileDataList.forEach(fileData -> {
            try {
                byte[] images = ImageUtils.decompressImage(fileData.getImageData());
                FileInfo fileInfo = new FileInfo(fileData.getName(), images);
                fileInfos.add(fileInfo);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        return fileInfos;
    }

    public String uploadImageToFileSystem(MultipartFile file) throws IOException {

        String filePath = FOLDER_PATH+file.getOriginalFilename();
        FileData fileData=fileDataRepository.save(
                FileData.builder()
                .name(file.getOriginalFilename())
                .type(file.getContentType())
                .filePath(filePath).build());


//        FileData fileData  = new FileData();
//        fileData.setName(file.getOriginalFilename());
//        fileData.setType(file.getContentType());
//        fileData.setFilePath(filePath);
//        fileDataRepository.save(fileData);


        file.transferTo(new File(filePath));

        if (fileData != null) {
            return "file uploaded successfully : " + filePath;
        }
        return null;
    }

    public byte[] downloadImageFromFileSystem(String fileName) throws IOException {
//        Optional<FileData> fileData = fileDataRepository.findByName(fileName);
        FileData fileData = fileDataRepository.findAllSortedByNameUsingNative(fileName);
        String filePath=fileData.getFilePath();
//        String filePath=fileData.get().getFilePath();
        byte[] images = Files.readAllBytes(new File(filePath).toPath());
        return images;
    }

    public List<FileInfo> getAllImage() throws IOException {
        List<FileInfo> fileInfos = new ArrayList<>();
        List<FileData> fileDataList = fileDataRepository.findAll();
        fileDataList.forEach(fileData -> {
            try {
                String filePath=fileData.getFilePath();
                byte[] images = Files.readAllBytes(new File(filePath).toPath());
                FileInfo fileInfo = new FileInfo(fileData.getName(), fileData.getFilePath(), images);
                fileInfos.add(fileInfo);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        return fileInfos;
    }



}
