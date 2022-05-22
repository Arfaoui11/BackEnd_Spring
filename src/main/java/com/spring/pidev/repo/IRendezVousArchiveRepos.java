package com.spring.pidev.repo;

import com.spring.pidev.model.ArchiveAppointment;
import org.springframework.data.repository.CrudRepository;

public interface IRendezVousArchiveRepos  extends CrudRepository<ArchiveAppointment,Long> {
}
