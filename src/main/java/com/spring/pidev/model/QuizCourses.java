package com.spring.pidev.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table( name = "QuizCourses")
public class QuizCourses  implements Serializable {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer idQuiz;

    private String title;

    private Integer score;

    @Temporal (TemporalType.DATE)
    private Date createAt;

    private String content ;

    @ManyToOne
    @JsonIgnore
    private Formation formation;

    @OneToMany(mappedBy = "quiz",fetch = FetchType.LAZY,cascade = {CascadeType.PERSIST,CascadeType.REMOVE})
    @JsonIgnore
    @ToString.Exclude
    private Set<QuestionCourses> question;

    @OneToMany(mappedBy = "quiz",fetch = FetchType.LAZY,cascade = {CascadeType.PERSIST,CascadeType.REMOVE})

    @ToString.Exclude
    private Set<Result> results;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        QuizCourses that = (QuizCourses) o;
        return idQuiz != null && Objects.equals(idQuiz, that.idQuiz);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
