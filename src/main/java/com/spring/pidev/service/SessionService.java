package com.spring.pidev.service;

import com.spring.pidev.model.Session;

import java.util.List;

public interface SessionService{
	List<Session> getAllSessions();
	Session saveSession(Session session);
	Session findSessionById(Long id);

	
}
