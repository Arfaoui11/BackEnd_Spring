package com.spring.pidev.service;


import com.spring.pidev.model.DatabaseFile;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;

public interface IFileService {
    public void addCVAndAssignToStudent(MultipartFile file,Long idStudent );
    void store(MultipartFile file, DatabaseFile f);
    Resource loadProductFiles(String filename, Integer  fileId);
    void init();
    public DatabaseFile storeFile(MultipartFile file,Long IdStudent);
    public DatabaseFile getFile(String fileId) throws FileNotFoundException;



}
