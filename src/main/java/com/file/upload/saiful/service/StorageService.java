

package com.file.upload.saiful.service;

import com.file.upload.saiful.dto.FileInfo;
import com.file.upload.saiful.entity.FileData;
import com.file.upload.saiful.entity.ImageData;
import com.file.upload.saiful.respository.FileDataRepository;
import com.file.upload.saiful.respository.StorageRepository;
import com.file.upload.saiful.util.ImageUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class StorageService {

    private final StorageRepository repository;
    private final FileDataRepository fileDataRepository;

    @Value("${file.storage.path:H:/img/}")
    private String folderPath;

    public StorageService(StorageRepository repository,
                          FileDataRepository fileDataRepository
    ) {
        this.repository = repository;
        this.fileDataRepository = fileDataRepository;
    }

    // ----------------------
    // DATABASE (BLOB STORAGE)
    // ----------------------
    public String uploadImage(MultipartFile file) throws IOException {
        ImageData imageData = ImageData.builder()
                .name(file.getOriginalFilename())
                .type(file.getContentType())
                .imageData(ImageUtils.compressImage(file.getBytes()))
                .build();

        ImageData saved = repository.save(imageData);

        return "File uploaded successfully: " + saved.getName();
    }

    public byte[] downloadImage(String fileName) {
        return repository.findByName(fileName)
                .map(img -> ImageUtils.decompressImage(img.getImageData()))
                .orElseThrow(() -> new RuntimeException("File not found: " + fileName));
    }

    public List<FileInfo> downloadImageList() {
        List<FileInfo> fileInfos = new ArrayList<>();
        List<ImageData> fileDataList = repository.findAll();

        fileDataList.forEach(fileData -> {
            byte[] image = ImageUtils.decompressImage(fileData.getImageData());
            fileInfos.add(new FileInfo(fileData.getName(), image));
        });

        return fileInfos;
    }

    // ----------------------
    // FILE SYSTEM STORAGE
    // ----------------------
    public String uploadImageToFileSystem(MultipartFile file) throws IOException {

        String originalFilename = file.getOriginalFilename();
        String extension = "";

        if (originalFilename != null && originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }

        String uniqueFileName = UUID.randomUUID().toString() + extension;
        String filePath = folderPath + uniqueFileName;

//        String filePath = folderPath + file.getOriginalFilename();

        FileData fileData = FileData.builder()
                .name(uniqueFileName)
                .type(file.getContentType())
                .filePath(filePath)
                .build();

        FileData saved = fileDataRepository.save(fileData);

        // Save the actual file in the folder
        file.transferTo(new File(filePath));

        return "File uploaded successfully: " + saved.getFilePath();
    }

    public byte[] downloadImageFromFileSystem(String fileName) throws IOException {
        FileData fileData = fileDataRepository.findAllSortedByNameUsingNative(fileName);

        if (fileData == null) {
            throw new RuntimeException("File not found: " + fileName);
        }

        return Files.readAllBytes(new File(fileData.getFilePath()).toPath());
    }

    public List<FileInfo> getAllImage() {
        List<FileInfo> fileInfos = new ArrayList<>();
        List<FileData> fileDataList = fileDataRepository.findAll();

        fileDataList.forEach(fileData -> {
            try {
                byte[] image = Files.readAllBytes(new File(fileData.getFilePath()).toPath());
                FileInfo fn = new FileInfo(fileData.getName(), fileData.getFilePath(), image);
                fileInfos.add(fn);
            } catch (IOException e) {
//                throw new RuntimeException("Error reading file: " + fileData.getName(), e);
                System.out.println("Error reading file: " + fileData.getName()+"    "+ e);
            }
        });

        return fileInfos;
    }


    public void delete(Long id) {
        FileData fileData = fileDataRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("File not found with id: " + id));

        File file = new File(fileData.getFilePath());
        if (file.exists()) {
            if (!file.delete()) {
                throw new RuntimeException("Failed to delete file from disk: " + fileData.getFilePath());
            }
        }

        fileDataRepository.deleteById(id);
    }

    public void deleteByName(String name) {

        FileData fileData = fileDataRepository.findByName(name)
                .orElseThrow(() -> new RuntimeException("File not found: " + name));

        File file = new File(fileData.getFilePath());
        if (file.exists() && !file.delete()) {
            throw new RuntimeException("Failed to delete file from disk: " + fileData.getFilePath());
        }

        fileDataRepository.delete(fileData);
    }

}


//package com.file.upload.saiful.service;
//
//import com.file.upload.saiful.dto.FileInfo;
//import com.file.upload.saiful.entity.FileData;
//import com.file.upload.saiful.entity.ImageData;
//import com.file.upload.saiful.respository.FileDataRepository;
//import com.file.upload.saiful.respository.StorageRepository;
//import com.file.upload.saiful.util.ImageUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.File;
//import java.io.IOException;
//import java.nio.file.Files;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Optional;
//
//@Service
//public class StorageService {
//
//    @Autowired
//    private StorageRepository repository;
//
//    @Autowired
//    private FileDataRepository fileDataRepository;
//
//    private final String FOLDER_PATH="H://img/";
//
//    public String uploadImage(MultipartFile file) throws IOException {
//
//        ImageData imageData = repository.save(ImageData.builder()
//                .name(file.getOriginalFilename())
//                .type(file.getContentType())
//                .imageData(ImageUtils.compressImage(file.getBytes())).build());
//
//        if (imageData != null) {
//            return "file uploaded successfully : " + file.getOriginalFilename();
//        }
//        return null;
//    }
//    public byte[] downloadImage(String fileName) {
//        Optional<ImageData> dbImageData = repository.findByName(fileName);
//        byte[] images = ImageUtils.decompressImage(dbImageData.get().getImageData());
//        return images;
//    }
//    public List<FileInfo> downloadImageList() throws IOException {
//        List<FileInfo> fileInfos = new ArrayList<>();
//        List<ImageData> fileDataList = repository.findAll();
//        fileDataList.forEach(fileData -> {
//            try {
//                byte[] images = ImageUtils.decompressImage(fileData.getImageData());
//                FileInfo fileInfo = new FileInfo(fileData.getName(), images);
//                fileInfos.add(fileInfo);
//            } catch (Exception e) {
//                throw new RuntimeException(e);
//            }
//        });
//        return fileInfos;
//    }
//
//    public String uploadImageToFileSystem(MultipartFile file) throws IOException {
//
//        String filePath = FOLDER_PATH+file.getOriginalFilename();
//        FileData fileData=fileDataRepository.save(
//                FileData.builder()
//                .name(file.getOriginalFilename())
//                .type(file.getContentType())
//                .filePath(filePath).build());
//
//
/// /        FileData fileData  = new FileData();
/// /        fileData.setName(file.getOriginalFilename());
/// /        fileData.setType(file.getContentType());
/// /        fileData.setFilePath(filePath);
/// /        fileDataRepository.save(fileData);
//
//
//        file.transferTo(new File(filePath));
//
//        if (fileData != null) {
//            return "file uploaded successfully : " + filePath;
//        }
//        return null;
//    }
//
//    public byte[] downloadImageFromFileSystem(String fileName) throws IOException {
////        Optional<FileData> fileData = fileDataRepository.findByName(fileName);
//        FileData fileData = fileDataRepository.findAllSortedByNameUsingNative(fileName);
//        String filePath=fileData.getFilePath();
////        String filePath=fileData.get().getFilePath();
//        byte[] images = Files.readAllBytes(new File(filePath).toPath());
//        return images;
//    }
//
//    public List<FileInfo> getAllImage() throws IOException {
//        List<FileInfo> fileInfos = new ArrayList<>();
//        List<FileData> fileDataList = fileDataRepository.findAll();
//        fileDataList.forEach(fileData -> {
//            try {
//                String filePath=fileData.getFilePath();
//                byte[] images = Files.readAllBytes(new File(filePath).toPath());
//                FileInfo fileInfo = new FileInfo(fileData.getName(), fileData.getFilePath(), images);
//                fileInfos.add(fileInfo);
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//        });
//        return fileInfos;
//    }
//}
