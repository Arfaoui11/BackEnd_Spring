package com.spring.pidev.repo;

import com.spring.pidev.model.Publicity;
import org.springframework.data.repository.CrudRepository;


public interface IPublicityRepo extends CrudRepository<Publicity, Integer> {
}
