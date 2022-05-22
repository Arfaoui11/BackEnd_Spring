package com.spring.pidev.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.Date;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class DislikesC {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer nbrSubsDislikes;

    @Temporal(TemporalType.DATE)
    private Date createAt;



     @ManyToOne
     @JsonIgnore
     private Subscription subscss;
}
