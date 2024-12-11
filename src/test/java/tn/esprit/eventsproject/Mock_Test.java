package tn.esprit.eventsproject;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tn.esprit.eventsproject.entities.Event;
import tn.esprit.eventsproject.entities.Logistics;
import tn.esprit.eventsproject.entities.Participant;
import tn.esprit.eventsproject.entities.Tache;
import tn.esprit.eventsproject.repositories.EventRepository;
import tn.esprit.eventsproject.repositories.LogisticsRepository;
import tn.esprit.eventsproject.repositories.ParticipantRepository;
import tn.esprit.eventsproject.services.EventServicesImpl;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class Mock_Test {

    @Mock
    private EventRepository eventRepository;

    @Mock
    private ParticipantRepository participantRepository;

    @Mock
    private LogisticsRepository logisticsRepository;

    @InjectMocks
    private EventServicesImpl eventServices;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void addParticipant_ShouldSaveParticipant() {
        Participant participant = new Participant();
        when(participantRepository.save(participant)).thenReturn(participant);

        Participant savedParticipant = eventServices.addParticipant(participant);

        assertNotNull(savedParticipant);
        verify(participantRepository, times(1)).save(participant);
    }

    @Test
    void addAffectEvenParticipant_ShouldAddEventToParticipant() {
        Participant participant = new Participant();
        participant.setIdPart(1);
        Event event = new Event();
        when(participantRepository.findById(1)).thenReturn(Optional.of(participant));
        when(eventRepository.save(event)).thenReturn(event);

        Event savedEvent = eventServices.addAffectEvenParticipant(event, 1);

        assertNotNull(savedEvent);
        verify(participantRepository, times(1)).findById(1);
        verify(eventRepository, times(1)).save(event);
    }

    @Test
    void addAffectLog_ShouldAddLogisticsToEvent() {
        Event event = new Event();
        Logistics logistics = new Logistics();
        when(eventRepository.findByDescription("testEvent")).thenReturn(event);
        when(logisticsRepository.save(logistics)).thenReturn(logistics);

        Logistics savedLogistics = eventServices.addAffectLog(logistics, "testEvent");

        assertNotNull(savedLogistics);
        verify(eventRepository, times(1)).findByDescription("testEvent");
        verify(logisticsRepository, times(1)).save(logistics);
    }

    @Test
    void getLogisticsDates_ShouldReturnLogisticsWithinDateRange() {
        Event event = new Event();
        Logistics logistics = new Logistics();
        logistics.setReserve(true);
        event.setLogistics(new HashSet<>(Collections.singletonList(logistics)));

        when(eventRepository.findByDateDebutBetween(any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(Collections.singletonList(event));

        List<Logistics> logisticsList = eventServices.getLogisticsDates(LocalDate.now(), LocalDate.now().plusDays(1));

        assertNotNull(logisticsList);
        assertEquals(1, logisticsList.size());
        verify(eventRepository, times(1)).findByDateDebutBetween(any(LocalDate.class), any(LocalDate.class));
    }

    @Test
    void calculCout_ShouldCalculateAndLogEventCost() {
        Event event = new Event();
        event.setDescription("Test Event");
        Logistics logistics = new Logistics();
        logistics.setReserve(true);
        logistics.setPrixUnit(10.0f);
        logistics.setQuantite(2);
        event.setLogistics(new HashSet<>(Collections.singletonList(logistics)));

        when(eventRepository.findByParticipants_NomAndParticipants_PrenomAndParticipants_Tache(
                eq("Tounsi"), eq("Ahmed"), eq(Tache.ORGANISATEUR)))
                .thenReturn(Collections.singletonList(event));

        eventServices.calculCout();

        verify(eventRepository, times(1))
                .findByParticipants_NomAndParticipants_PrenomAndParticipants_Tache("Tounsi", "Ahmed", Tache.ORGANISATEUR);
        verify(eventRepository, times(1)).save(event);
    }
}