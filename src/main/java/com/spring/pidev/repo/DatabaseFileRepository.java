package com.spring.pidev.repo;

import com.spring.pidev.model.DatabaseFile;
import org.springframework.data.repository.CrudRepository;

public interface DatabaseFileRepository extends CrudRepository<DatabaseFile, String> {

}
