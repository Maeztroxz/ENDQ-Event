package com.example.demo.api.services;

import com.example.demo.api.dtos.AttendanceResponse;
import com.example.demo.api.dtos.EventRegisterResponse;
import com.example.demo.api.dtos.EventRequest;
import com.example.demo.api.exceptions.BusinessException;
import com.example.demo.api.exceptions.ForbiddenException;
import com.example.demo.api.models.Event;
import com.example.demo.api.models.User;
import com.example.demo.api.repositories.EventRepository;

import com.example.demo.api.security.UserContext;
import com.example.demo.api.security.UserData;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class EventService  {
    @Autowired
    private EventRepository eventRepository ;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private UserContext userContext;

    @Autowired
    private ImageService imageService;

    public EventRegisterResponse addEvent(EventRequest eventComing){
        Event eventToSave = new Event();
        if(!eventComing.getTitle().isEmpty()
                && !eventComing.getDescription().isEmpty()
                && !eventComing.getLocation().isEmpty()
                && eventComing.getDate()!=null
                && !eventComing.getImage().isEmpty()) {

            eventToSave.setTitle(eventComing.getTitle().toLowerCase());
            eventToSave.setDescription(eventComing.getDescription());
            LocalDateTime dateNow = LocalDateTime.now();
            if(!eventComing.getDate().isAfter(dateNow)){
                System.out.println(dateNow.getDayOfMonth());
                throw new BusinessException("Date not valid");
            }
            eventToSave.setDate(eventComing.getDate());
            imageService.getFile(eventComing.getImage());
            eventToSave.setImage(eventComing.getImage());
            eventToSave.setAttendances(eventComing.getAttendances());
            eventToSave.setWillYouAttend(eventComing.getWillYouAttend());
            List location = eventComing.getLocation();

            eventToSave.setLocation(location.get(0).toString()+","+location.get(1).toString());
            eventRepository.save(eventToSave);

            EventRegisterResponse eventRegisterResponse = this.modelMapper.map(eventToSave,EventRegisterResponse.class);
                return eventRegisterResponse;}
        else
            throw new BusinessException("Some fields are empty.");

    }

    public Page<Event> getAllEvents(Pageable page, Double lat, Double ing, String title){
        Page<Event> events = null;
        String location = "";
            if(title == null && lat != null && ing != null)
            {

                location = lat+","+ing;
                events = eventRepository.findAllPagesByLatAndIng(location,page);}

            if(lat == null && ing == null && title != null )
                events = eventRepository.findAllPagesByTitle(title,page);

            if(lat == null && ing == null && title == null)
                events = eventRepository.findAllPagesNormal(page);

        return events;
    }
    public AttendanceResponse attendanceEvent(String id){
        UserData userData = userContext.getUser();
        User currentUser = this.modelMapper.map(userData,User.class);
        List<User> users= getAllUserAttendance(id);

        Optional<Event> eventFromDb = Optional.ofNullable(findOneEvent(id));
            if (!eventFromDb.isPresent()) {
                throw new BusinessException("Event not found");
            } else {
                Event eventAttendance = eventFromDb.get();

                    if(users.stream().filter( x -> x.getEmail().equals(currentUser.getEmail())).collect(Collectors.toList()).size()>=1){
                        throw new ForbiddenException("Your already register in this event");}

                eventAttendance.setWillYouAttend(true);
                eventAttendance.setAttendances(eventAttendance.getAttendances() + 1);
                users.add(currentUser);
                eventAttendance.setUsers(users);
                eventRepository.save(eventAttendance);
                AttendanceResponse attendanceResponse = this.modelMapper.map(eventAttendance, AttendanceResponse.class);
                return attendanceResponse;
            }

    }

    public AttendanceResponse deleteAttendanceEvent(String id){
        Optional<Event> eventFromDb = Optional.ofNullable(findOneEvent(id));
        UserData userData = userContext.getUser();
        User currentUser = this.modelMapper.map(userData,User.class);
        List<User> users = getAllUserAttendance(id);
            if (!eventFromDb.isPresent()) {
                throw new BusinessException("Event not found");
            } else {
                Event eventAttendance = eventFromDb.get();
                    if(users.stream().filter( x -> x.getEmail().equals(currentUser.getEmail())).collect(Collectors.toList()).size()<1){
                        throw new ForbiddenException("Your are not register in this event");}

                eventAttendance.setAttendances(eventAttendance.getAttendances() - 1);
                users.remove(currentUser);
                eventAttendance.setUsers(users);
                eventAttendance.setWillYouAttend(false);
                eventRepository.save(eventAttendance);
                AttendanceResponse attendanceResponse = this.modelMapper.map(eventAttendance, AttendanceResponse.class);
                return attendanceResponse;
            }

    }

    public Event findOneEvent(String id) {
        return eventRepository.findByIdEvent(id);
    }

    public List<User> getAllUserAttendance(String idEvent){
        Event event = findOneEvent(idEvent);
        List<User> usersList = event.getUsers();
        return usersList;
    }

}