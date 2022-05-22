package com.spring.pidev.repo;

import com.spring.pidev.model.Dislikes;
import org.springframework.data.repository.CrudRepository;

public interface IDislikesRepo extends CrudRepository<Dislikes,Integer> {
}
