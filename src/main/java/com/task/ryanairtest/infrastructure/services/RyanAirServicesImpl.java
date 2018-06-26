package com.task.ryanairtest.infrastructure.services;


import com.task.ryanairtest.app.TimeUtils;
import com.task.ryanairtest.domain.dao.RyanAirDAO;
import com.task.ryanairtest.domain.dto.*;
import com.task.ryanairtest.domain.services.RyanAirServices;
import com.task.ryanairtest.infrastructure.dao.RyanAirDAOImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RyanAirServicesImpl implements RyanAirServices {

    private static final Logger logger = LogManager.getLogger(RyanAirServicesImpl.class);

    @Autowired
    private RyanAirDAO ryanAirDAO;


    public RyanAirServicesImpl() {
    }

    public RyanAirServicesImpl(RyanAirDAO ryanAirDAO) {
        this.ryanAirDAO = ryanAirDAO;
    }

    @Override
    public List<Interconnection> getInterconnections(String departure, String arrival, Date departureDateTime, Date arrivalDateTime) {

        // First call
        Routes res = ryanAirDAO.getRoutes();
        List<Interconnection> interconnectionList = getDirectAndOneStopFlightRoutes(res, departure, arrival);

        // Join the results
        List<Interconnection> completeInterconnections = completeConnectionsWithSchedules(
                departure, arrival, departureDateTime, arrivalDateTime, interconnectionList);

        return completeInterconnections;
    }

    /**
     * Get possible routes.
     * @param routes All routes
     * @param departure Departe airport
     * @param arrival Arrival airport
     * @return List of routes
     */
    private List<Interconnection> getDirectAndOneStopFlightRoutes(Routes routes, String departure, String arrival) {
        List<Interconnection> directAndOneStopRoutes = new ArrayList<>();
        // Same FromAirport routes
        List<Route> sameFromAirportRoutes = routes.getRoutesMap().get(departure);

        for (Route route : sameFromAirportRoutes) {
            if (route.getAirportTo().equals(arrival)) {
                // Direct routes
                Flight flight = createAirportsFlight(departure, arrival);
                Interconnection direct = createInterconnection(0, Arrays.asList(flight));
                directAndOneStopRoutes.add(direct);
                logger.debug("Direct route added " + departure + "-" + arrival);
            } else {
                // One stop routes
                Interconnection withOneStop = createOneStopFlight(routes, route, departure, arrival);
                if (withOneStop != null) {
                    directAndOneStopRoutes.add(withOneStop);
                    logger.debug("One stop route added " + withOneStop.getLegs());
                }
            }
        }
        return directAndOneStopRoutes;
    }


    /**
     * Create Interconnection with one stop.
     * @param routes List of routes
     * @param route Current Route
     * @param departure Departure airport
     * @param arrival Arrival airport
     * @return Interconnection with two flights or null (not found)
     */
    private Interconnection createOneStopFlight(Routes routes, Route route, String departure, String arrival) {
        Interconnection withOneStop = null;
        List<Route> oneStopFlight = routes.getRoutesMap().get(route.getAirportTo())
                .stream()
                .filter(x -> x.getAirportTo().equals(arrival))
                .collect(Collectors.toList());
        if (!oneStopFlight.isEmpty()) {
            String connection = oneStopFlight.get(0).getAirportFrom();
            Flight initialFlight = createAirportsFlight(departure, connection);
            Flight connectionFlight = createAirportsFlight(connection, arrival);
            withOneStop =
                    createInterconnection(1, Arrays.asList(initialFlight, connectionFlight));
        }
        return withOneStop;
    }


    /**
     * Complete the routes with its schedules.
     *
     * @param departure     Departure airport
     * @param arrival       Arrival airport
     * @param departureTime Departure time
     * @param arrivalTime   Arrival time
     * @param routes        Array of Interconnections
     * @return Complete list with routes and dateTime
     */
    private List<Interconnection> completeConnectionsWithSchedules(String departure, String arrival,
                                                                   Date departureTime, Date arrivalTime, List<Interconnection> routes) {
        List<Interconnection> resInterconnection = new ArrayList<>();

        for (Interconnection interconnection : routes) {
            List<Flight> legs = new ArrayList<>();
            for (Flight flight : interconnection.getLegs()) {
                // Direct Routes
                final Integer year = TimeUtils.getYearOfDate(departureTime);
                final Integer month = TimeUtils.getMonthOfDate(departureTime);
                final Integer day = TimeUtils.getDayOfDate(departureTime);
                // get schedules of flights
                Schedules schedules = ryanAirDAO.getSchedules(flight.getDeparture(), flight.getArrival(), String.valueOf(year), String.valueOf(month));
                if (schedules == null) {
                    break;
                }
                // TODO Joint two stream, filter day and hours in the same stream
                // get schedules for a day in correct time
                List<Days> res = schedules.getDays().stream().filter( x -> x.getDay() == day).collect(Collectors.toList());

                // get info for flights in the day.
                List<Flight> resFlight = null;
                if (!res.isEmpty()) {
                    resFlight = res.get(0).getFlights().stream()
                            .map(
                                    x -> {
                                        return createFlight(flight, year, month, day, x);
                                    }
                            ).collect(Collectors.toList());
                    legs.addAll(resFlight);
                }
            }
            // Join interconnections
            resInterconnection.addAll(matchInterconnections(interconnection, legs, departure, arrival, departureTime, arrivalTime));

        }// end for interconnection
        return resInterconnection;
    }

    /**
     * Create a flight
     * @param flight Incomplete flight
     * @param year Year
     * @param month Month
     * @param day Day
     * @param flightSchedules Data of flightSchedules
     * @return Flight
     */
    private Flight createFlight(Flight flight, Integer year, Integer month, Integer day, FlightSchedules flightSchedules) {
        Flight newFlight = new Flight();
        newFlight.setNumber(flightSchedules.getNumber());
        newFlight.setDeparture(flight.getDeparture());
        newFlight.setArrival(flight.getArrival());

        try {
            newFlight.setDepartureTime(TimeUtils.getDateFromScheduleString(year, month, day, flightSchedules.getDepartureTime()));
            newFlight.setArrivalTime(TimeUtils.getDateFromScheduleString(year, month, day, flightSchedules.getArrivalTime()));
        } catch (ParseException e) {
            logger.error("Error to parse time in Flight " + flight.getDeparture() + "-" + flight.getArrival());
            newFlight.setDepartureTime(null);
            newFlight.setArrivalTime(null);
        }
        return newFlight;
    }


    /**
     * Match all results and create connections
     * @param currentInterconnection Initial interconnection
     * @param legs Legs of route
     * @param departure Departure airport
     * @param arrival Arrival airport
     * @param departureTime Departure time
     * @param arrivalTime Arrival time
     * @return List of all possible connections
     */
    private List<Interconnection> matchInterconnections(Interconnection currentInterconnection, List<Flight> legs,
                                                        String departure, String arrival, Date departureTime, Date arrivalTime) {
        List<Interconnection> resInterconnections = new ArrayList<>();

        // Join interconnections
        if ( currentInterconnection.getStops() == 0){
            // Direct found flights
            for (Flight flight: legs){
                if ( checkInterconnectionTime(departureTime, arrivalTime, Arrays.asList(flight) )){
                    resInterconnections.add(createInterconnection(0, Arrays.asList(flight)));
                }
            }
        }else{
            legs.stream().map(
                    flight -> {
                        // Search candidates flights
                        List<Flight> candidates = legs.stream()
                                .filter(other -> conditionsFlightsConnector(departure, arrival, flight, other))
                                .collect(Collectors.toList());
                        // Create interconnection with each pair
                        for (Flight connectFlight: candidates){
                            if ( checkInterconnectionTime(departureTime, arrivalTime, Arrays.asList(flight, connectFlight)) ){
                                resInterconnections.add(createInterconnection(1, Arrays.asList(flight, connectFlight)));
                            }
                        }
                        return flight;
                    }
            ).collect(Collectors.toList());
        }
        return resInterconnections;
    }

    /**
     * Check if a flight or interconnection time is valid.
     * @param departureTime departure Time
     * @param arrivalTime airport Time
     * @param flights Flight or list or flight
     * @return True is a flight/interconnection is correct
     */
    private boolean checkInterconnectionTime(Date departureTime, Date arrivalTime, List<Flight> flights) {
        if ( flights.size() ==  1) {
            return !flights.get(0).getDepartureTime().before(departureTime) &&
                    !flights.get(0).getArrivalTime().after(arrivalTime);
        }else{
            return !flights.get(0).getDepartureTime().before(departureTime) &&
                    !flights.get(1).getArrivalTime().after(arrivalTime);
        }
    }

    /**
     * Condition to connect two flights
     * @param flight Flight
     * @param other Other Flight
     * @return true if they can connect
     */
    private boolean conditionsFlightsConnector(String departure, String arrival, Flight flight, Flight other) {
        return flight.getDeparture().equals(departure) &&
                other.getArrival().equals(arrival)&&
                TimeUtils.addHours(flight.getArrivalTime(),2).before(other.getDepartureTime());
    }

    /**
     * Create a flight
     *
     * @param departure Departure airport
     * @param arrival Arrival Airport
     * @return Flight
     */
    private Flight createAirportsFlight(String departure, String arrival) {
        Flight flight = new Flight();
        flight.setDeparture(departure);
        flight.setArrival(arrival);
        return flight;
    }

    /**
     * Create a Interconnection flight
     *
     * @param stops Number of stop of interconnection
     * @param flightList List of flight
     * @return Interconnection
     */
    private Interconnection createInterconnection(Integer stops, List<Flight> flightList) {
        return new Interconnection(stops, flightList);
    }

}
