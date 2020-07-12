package com.example.demo.api.repositories;

import com.example.demo.api.models.Event;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface EventRepository  extends JpaRepository<Event, String> {
     @Query("SELECT evt FROM Event evt WHERE evt.id = :id")
     Event findByIdEvent(String id);

     @Query("SELECT evt FROM Event evt")
     Page<Event> findAllPagesNormal(Pageable pageable);

     @Query("SELECT evt FROM Event evt WHERE LOWER(evt.title) LIKE LOWER(concat('%',:title,'%'))")
     Page<Event> findAllPagesByTitle(String title, Pageable pageable);

     @Query("SELECT evt FROM Event evt WHERE evt.location LIKE :location")
     Page<Event> findAllPagesByLatAndIng(String location, Pageable pageable);
}
