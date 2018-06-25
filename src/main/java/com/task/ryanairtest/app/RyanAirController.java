package com.task.ryanairtest.app;

import com.task.ryanairtest.client.FlightResponsePayload;
import com.task.ryanairtest.client.InterconnectionPayload;
import com.task.ryanairtest.domain.dto.Interconnection;
import com.task.ryanairtest.domain.services.RyanAirServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping({"/ryanairtest"})
public class RyanAirController {

    @Autowired
    private RyanAirServices ryanAirServices;

    @RequestMapping
    public String status() {
        return "OK";
    }


    @GetMapping("/interconnections")
    public List<InterconnectionPayload> getInterconnections(
            @RequestParam("departure") String departure,
            @RequestParam("arrival") String arrival,
            @RequestParam("departureDateTime") String departureTime,
            @RequestParam("arrivalDateTime") String arrivalTime) throws Exception{

        // Check departure and arrival
        Date departureDateTime = TimeUtils.fromISO8601UTC(departureTime);
        Date arrivalDateTime = TimeUtils.fromISO8601UTC(arrivalTime);

        // Call Service
        List<Interconnection> interconnections = ryanAirServices.getInterconnections(
                departure,
                arrival,
                departureDateTime,
                arrivalDateTime);

        // Map List<Interconnection> to List<InterconnectionsPayload>
        List<InterconnectionPayload> result = interconnections.stream().map(temp -> {

            InterconnectionPayload obj = new InterconnectionPayload(
                    temp.getStops(),
                    temp.getLegs().stream().map(x -> {
                        FlightResponsePayload flightResponsePayload = new FlightResponsePayload(
                                x.getDeparture(),
                                x.getArrival(),
                                TimeUtils.convertDateToLocalDateTime(x.getDepartureTime()),
                                TimeUtils.convertDateToLocalDateTime(x.getArrivalTime()));
                        return flightResponsePayload;
                    }).collect(Collectors.toList())
            );

            return obj;
        }).collect(Collectors.toList());

        return result;
    }



}
