package com.example.demo.api.models;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.lang.reflect.Array;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;


@Data
@Entity
@Table(name = "event")
public class Event {

    @Id
    @Column(name = "id", unique=true, nullable = false)
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    private String id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private LocalDateTime date;


    @Column(nullable = false)
    private String image;


    private long Attendances;


    private Boolean willYouAttend;

    @ManyToMany(fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    private List<User> users;

    @Column(nullable = false)
    private String location;
}
