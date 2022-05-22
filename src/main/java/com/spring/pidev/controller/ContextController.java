package com.spring.pidev.controller;

import com.spring.pidev.model.Question;
import com.spring.pidev.model.Quiz;
import com.spring.pidev.model.Session;
import com.spring.pidev.model.User;

import java.util.List;


public class ContextController {

	//public static Learner Learner;
	//public static Admin admin;
	//public static Former former;
	public static User user;
	public static Session session;
	public static Quiz quiz;
	public static List<Question> questions;

	public static User getUser() {
		return user;
	}
	public static void setUser(User user) {
		ContextController.user = user;
	}

	public static Session getSession() {
		return session;
	}
	public static void setSession(Session session) {
		ContextController.session = session;
	}
	public static Quiz getQuiz() {
		return quiz;
	}
	public static void setQuiz(Quiz quiz) {
		ContextController.quiz = quiz;
	}


}
