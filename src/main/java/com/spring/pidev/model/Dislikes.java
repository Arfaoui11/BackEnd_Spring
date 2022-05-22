package com.spring.pidev.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Dislikes implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer nbrDislikes;

    @Temporal(TemporalType.DATE)
    private Date createAt;

    @ManyToOne
    @JsonIgnore
    private PostComments postComments;

    @ManyToOne
    private User userD;


}
