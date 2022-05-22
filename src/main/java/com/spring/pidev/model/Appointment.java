package com.spring.pidev.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Appointment implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long idApp;
    @Temporal(TemporalType.DATE)
    private Date dateApp;
   // @NotBlank
   // @Size(max = 20)
    private String remark;
   @ManyToOne
   @JsonIgnore
    private User users;
    @ManyToOne
    @JsonIgnore
    private User doctor;


}
