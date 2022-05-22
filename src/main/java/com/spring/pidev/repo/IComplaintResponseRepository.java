package com.spring.pidev.repo;

import com.spring.pidev.model.ComplaintResponse;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface IComplaintResponseRepository extends CrudRepository<ComplaintResponse, Long> {
}
