package com.task.ryanairtest.infrastructure.services;


import com.task.ryanairtest.app.TimeUtils;
import com.task.ryanairtest.domain.dao.RyanAirDAO;
import com.task.ryanairtest.domain.dto.*;
import com.task.ryanairtest.domain.services.RyanAirServices;
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
        List<Route> res = ryanAirDAO.getRoutes();
        List<Interconnection> interconnectionList = getDirectAndOneStopFlightRoutes(res, departure, arrival);

        // Join the results
        List<Interconnection> completeInterconnections = completeConnectionsWithSchedules(
                departure, arrival, departureDateTime, arrivalDateTime, interconnectionList);

        return completeInterconnections;
    }


    private List<Interconnection> getDirectAndOneStopFlightRoutes(List<Route> routes, String departure, String arrival) {
        List<Interconnection> directAndOneStopRoutes = new ArrayList<>();

        // Same FromAirport routes
        List<Route> sameFromAirportRoutes = routes.stream().filter(x ->
                x.getAirportFrom().equals(departure) &&
                x.getConnectingAirport() == null
        ).collect(Collectors.toList());


        for (Route route : sameFromAirportRoutes) {

            if (route.getAirportTo().equals(arrival)) {
                // Direct routes
                Flight flight = createFlight(departure, arrival);
                Interconnection direct = createInterconnection(0, Arrays.asList(flight));
                directAndOneStopRoutes.add(direct);
            } else {
                // One stop routes
                List<Route> oneStopFlight = routes.stream().filter(
                        x ->
                                x.getAirportFrom().equals(route.getAirportTo()) &&
                                        x.getAirportTo().equals(arrival)
                ).collect(Collectors.toList());
                if (!oneStopFlight.isEmpty()) {
                    String connection = oneStopFlight.get(0).getAirportFrom();
                    Flight initialFlight = createFlight(departure, connection);
                    Flight connectionFlight = createFlight(connection, arrival);
                    Interconnection withOneStop =
                            createInterconnection(1, Arrays.asList(initialFlight, connectionFlight));
                    directAndOneStopRoutes.add(withOneStop);
                }
            }
        }
        return directAndOneStopRoutes;
    }

    /**
     * Complete the routes with its schedules.
     * @param departure Departure airport
     * @param arrival Arrival airport
     * @param departureTime Departure time
     * @param arrivalTime Arrival time
     * @param routes Array of Interconnections
     * @return Complete list with routes and dateTime
     */
    private List<Interconnection> completeConnectionsWithSchedules(String departure, String arrival,
                                                                   Date departureTime, Date arrivalTime, List<Interconnection> routes) {
        List<Interconnection> resInterconnection = new ArrayList<>();


        for (Interconnection interconnection: routes){
            List<Flight> legs = new ArrayList<>();
            for (Flight flight: interconnection.getLegs() ){
                // Direct Routes
                final Integer year = TimeUtils.getYearOfDate(departureTime);
                final Integer month = TimeUtils.getMonthOfDate(departureTime);
                final Integer day = TimeUtils.getDayOfDate(departureTime);

                // get schedules of flights
                Schedules schedules = ryanAirDAO.getSchedules(flight.getDeparture(), flight.getArrival(), String.valueOf(year), String.valueOf(month));

                if ( schedules == null){
                   break;
                }

                // get schedules for a day
                List<Days> res = schedules.getDays().stream().filter(x -> x.getDay() == day).collect(Collectors.toList());

                // get info for a flight
                List<Flight> resFlight = null;
                if ( !res.isEmpty()) {
                    resFlight = res.get(0).getFlights().stream().map(
                            x -> {
                                Flight newFlight = new Flight();
                                newFlight.setNumber(x.getNumber());
                                newFlight.setDeparture(flight.getDeparture());
                                newFlight.setArrival(flight.getArrival());

                                try {
                                    newFlight.setDepartureTime(TimeUtils.getDateFromScheduleString(year, month, day, x.getDepartureTime()));
                                    newFlight.setArrivalTime(TimeUtils.getDateFromScheduleString(year, month, day, x.getArrivalTime()));
                                } catch (ParseException e) {
                                    newFlight.setDepartureTime(null);
                                    newFlight.setArrivalTime(null);
                                }
                                return newFlight;
                            }
                    ).collect(Collectors.toList());
                    legs.add(resFlight.get(0));
                }
            }
            // Check one stop connections are completed
            if ( checkCompleteLegs(interconnection, legs)  &&
                    checkTime(legs,departureTime, arrivalTime)){
                interconnection.setLegs(legs);
                resInterconnection.add(interconnection);
            }

        }
        return resInterconnection;

    }

    /**
     * Check if the flight connections are completed.
     * @param interconnection Interconnection
     * @param legs Array of flights
     * @return True if its are completes
     */
    private Boolean checkCompleteLegs(Interconnection interconnection,
                                   List<Flight> legs) {
        if ( (interconnection.getStops() == 0 && legs.size() == 1) ||
                (interconnection.getStops() > 0 && legs.size() > 1) ){
           return true;
        }
        return false;
    }

    /** Check if the departure time,stop time between flights and arrival time is correct.
     * @param legs Array of flights
     * @param departureTime departure time
     * @param arrivalTime arrival time
     * @return
     */
    private Boolean checkTime(List<Flight> legs, Date departureTime, Date arrivalTime){
        // Direct flights
        if ( legs.size() == 1){
            return true;
        }else{

            Flight flight0 =  legs.get(0);
            Flight flight1 = legs.get(1);

            if ( flight0.getDepartureTime().after(departureTime) &&
                 TimeUtils.addHours(flight0.getArrivalTime(),2).before(flight1.getDepartureTime()) &&
                 flight1.getArrivalTime().before(arrivalTime)  ){
                return true;
            }
        }
        return false;

    }


    /**
     * Create a flight
     * @param departure
     * @param arrival
     * @return
     */
    private Flight createFlight(String departure, String arrival){
        Flight flight = new Flight();
        flight.setDeparture(departure);
        flight.setArrival(arrival);
        return flight;
    }


    /**
     * Create a Interconnection flight
     * @param stops
     * @param flightList
     * @return
     */
    private Interconnection createInterconnection(Integer stops, List<Flight> flightList){
        return new Interconnection(stops, flightList);
    }

}
