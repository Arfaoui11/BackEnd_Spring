package com.javachinna.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table( name = "Offres")
public class Offres {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idOffer")
    private Integer idOffer;
    private Integer priceOffer;
    private String ProfessionTitle;
    @Temporal(TemporalType.DATE)
    private Date dateEndOffer;
    @Temporal(TemporalType.DATE)
    private Date dateBeginOffer;
    @Temporal(TemporalType.DATE)
    private Date dateInterview;
    @Enumerated(EnumType.STRING)
    private Profession profession;


    @ManyToOne
    @JsonIgnore
    private User users;


    @OneToMany(mappedBy ="offers",fetch = FetchType.LAZY,
            cascade = {
                    CascadeType.PERSIST,
                    CascadeType.MERGE,
                    CascadeType.REMOVE
            })
    @JsonIgnore
    private Set<Candidacy> candidacy;





}
