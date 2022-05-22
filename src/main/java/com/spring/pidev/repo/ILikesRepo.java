package com.spring.pidev.repo;

import com.spring.pidev.model.Likes;
import org.springframework.data.repository.CrudRepository;

public interface ILikesRepo extends CrudRepository<Likes,Integer> {
}
