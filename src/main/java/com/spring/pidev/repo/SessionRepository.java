package com.spring.pidev.repo;

import com.spring.pidev.model.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SessionRepository extends JpaRepository<Session, Long>{
	Session findByid(Long id);
}
