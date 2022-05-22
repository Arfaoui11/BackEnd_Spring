package com.spring.pidev.repo;

import com.spring.pidev.model.Certificat;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ICertificatRepo extends CrudRepository<Certificat,Long> {
}
