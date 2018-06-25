package com.test.ryanairtest.domain.services;

import com.test.ryanairtest.domain.dto.Interconnection;
import com.test.ryanairtest.domain.dto.Route;

import java.util.Date;
import java.util.List;

public interface RyanAirServices {

    /**
     * Get flights from departure to arrival, direct or with one stop between dates.
     * @param departure a departure airport IATA code
     * @param arrival an arrival airport IATA code
     * @param departureDateTime a departure datetime in the departure airport timezone in ISO format
     * @param arrivalDateTime an arrival datetime in the arrival airport timezone in ISO format
     * @return List
     */
    List<Interconnection> getInterconnections(String departure, String arrival, Date departureDateTime, Date arrivalDateTime);


}
