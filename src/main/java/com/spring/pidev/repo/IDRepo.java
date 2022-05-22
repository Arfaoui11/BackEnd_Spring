package com.spring.pidev.repo;

import com.spring.pidev.model.DislikesC;
import org.springframework.data.repository.CrudRepository;

public interface IDRepo extends CrudRepository<DislikesC,Integer> {
}
