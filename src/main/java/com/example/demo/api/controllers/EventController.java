package com.example.demo.api.controllers;
import javax.validation.Valid;


import com.example.demo.api.dtos.EventRequest;
import com.example.demo.api.models.Event;
import com.example.demo.api.repositories.EventRepository;
import com.example.demo.api.services.EventService;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class EventController {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private EventService eventService;



    @RequestMapping(value = "/events", method = RequestMethod.POST)
    public ResponseEntity<?> addEvent(@Valid @RequestBody EventRequest eventRequest)
    {

        return ResponseEntity.ok(eventService.addEvent(eventRequest));
    }

    @RequestMapping(value = "/events", method = RequestMethod.GET)
    public Page<?> GetAllEvents(
            @Valid @RequestParam (value = "page", required = false) int page,
            @Valid @RequestParam (value = "limit", required = false) int limit,
            @Valid @RequestParam (value = "lat", required = false) Double lat,
            @Valid @RequestParam (value = "ing", required = false) Double ing,
            @Valid @RequestParam (value = "title", required = false) String title)
    {

        PageRequest pageable = PageRequest.of(page, limit, Sort.by("date").ascending());
        Page<Event> events = eventService.getAllEvents(pageable,lat,ing,title);


        return events;
    }

    @RequestMapping(value = "/events/attendance/{eventId}", method = RequestMethod.POST)
    public ResponseEntity<?> attendanceEvent(@Valid @PathVariable String eventId)
    {
        return ResponseEntity.ok(eventService.attendanceEvent(eventId));
    }

    @RequestMapping(value = "/events/attendance/{eventId}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteattendanceEvent(@Valid @PathVariable String eventId)
    {
        return ResponseEntity.ok(eventService.deleteAttendanceEvent(eventId));
    }


}
