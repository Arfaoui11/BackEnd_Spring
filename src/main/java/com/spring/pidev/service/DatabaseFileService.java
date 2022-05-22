package com.spring.pidev.service;



import com.spring.pidev.fileUpload.FileStorageException;
import com.spring.pidev.model.DatabaseFile;
import com.spring.pidev.model.Formation;
import com.spring.pidev.repo.DatabaseFileRepository;
import com.spring.pidev.repo.IFormationRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;

@Service
public class DatabaseFileService {

    @Autowired
    private DatabaseFileRepository dbFileRepository;
    @Autowired
    private IFormationRepo iFormationRepo;

    public DatabaseFile storeFile(MultipartFile file, Integer idFormation ) {
        // Normalize file name
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        Formation formation = iFormationRepo.findById(idFormation).orElse(null);

        try {
            // Check if the file's name contains invalid characters
            if(fileName.contains("..")) {
                throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
            }

            DatabaseFile dbFile = new DatabaseFile(fileName, file.getContentType(), file.getBytes());
            dbFile.setUser(formation.getFormateur());
            dbFile.setFormation(formation);
            return dbFileRepository.save(dbFile);
        } catch (IOException ex) {
            throw new FileStorageException("Could not store file " + fileName + ". Please try again!", ex);
        }
    }

    public DatabaseFile getFile(String fileId) throws FileNotFoundException {
        return dbFileRepository.findById(fileId)
                .orElseThrow(() -> new FileNotFoundException("File not found with id " + fileId));
    }
}
