package com.spring.pidev.repo;

import com.spring.pidev.model.LikesC;
import org.springframework.data.repository.CrudRepository;

public interface ILRepo extends CrudRepository<LikesC,Integer> {
}
