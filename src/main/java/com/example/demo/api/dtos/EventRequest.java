package com.example.demo.api.dtos;

import lombok.Data;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.lang.reflect.Array;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Data
public class EventRequest {

    private String id;


    private String title;

    private String description;


    private LocalDateTime date;


    private String image;


    private long Attendances;


    private Boolean willYouAttend;

    private List location;
}
