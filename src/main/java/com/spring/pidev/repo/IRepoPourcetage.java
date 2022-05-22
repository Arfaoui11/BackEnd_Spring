package com.spring.pidev.repo;

import com.spring.pidev.model.Pourcentages;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface IRepoPourcetage extends CrudRepository<Pourcentages,Integer> {



    @Query(value= "select f.name , f.value from Pourcentages f")
    List<Object[]> getPourcentage();

}
