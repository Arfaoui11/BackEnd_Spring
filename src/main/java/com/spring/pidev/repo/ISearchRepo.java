package com.spring.pidev.repo;

import com.spring.pidev.model.Search;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ISearchRepo extends CrudRepository<Search,Integer> {

    @Query(value = "select f.title from Formation f where concat(f.title,f.level,f.domain,f.frais,f.nbrHeures,f.nbrMaxParticipant) like %?1% order by f.title")
    List<String> keyWord(String keyword);

}
