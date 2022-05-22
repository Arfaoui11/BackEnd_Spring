package com.spring.pidev.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@Entity
@Table(name = "options")
public class Option implements Serializable {
  
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long Id;
	

	private String optionText;
	
	@ManyToOne()
	@JoinColumn(name="question_id")
	private Question question;

	public Option() {

	}
	public Option(String optionText) {
		super();
		this.optionText = optionText;
	}


}
