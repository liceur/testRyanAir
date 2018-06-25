package com.task.ryanairtest.infrastructure.services;

import com.task.ryanairtest.domain.dao.RyanAirDAO;
import com.task.ryanairtest.domain.dto.*;
import com.task.ryanairtest.domain.services.RyanAirServices;
import org.junit.Before;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class RyanAirServicesImplTest {

    private static final String DEPARTURE = "MAD";
    private static final String ARRIVAL = "BCN";

    private RyanAirServices ryanAirServices;

    private RyanAirDAO ryanAirDAO = mock(RyanAirDAO.class);

    @Before
    public void init() {

        ryanAirServices = new RyanAirServicesImpl(ryanAirDAO);
    }

    @Test
    public void givenDepartureAndArrivalAirportWithTimesWhenInvokeServiceThenReturnInterconnectionsFlights() {

        // given
        Date departureTime = new Date();
        Date arrivalTime = new Date();

        // When
        when(ryanAirDAO.getRoutes()).thenReturn(getMockRoutes());
        when(ryanAirDAO.getSchedules(any(), any(), any(), any())).thenReturn(getMockSchedules());

        List<Interconnection> res = ryanAirServices.getInterconnections(DEPARTURE, ARRIVAL, departureTime, arrivalTime);

        // Then
        assertThat( res != null );
    }


    private Routes getMockRoutes() {
        List<Route> routeListBCN = new ArrayList();
        Route route1 = createRoute("BCN", "MAD");
        routeListBCN.add(route1);

        List<Route> routeListMAD = new ArrayList();
        Route route2 = createRoute("MAD", "IBZ");
        routeListMAD.add(route2);

        Map<String, List<Route>> routeMap = new HashMap<String, List<Route>>();
        routeMap.put("BCN", routeListBCN);
        routeMap.put("MAD", routeListMAD);

        Routes routes = new Routes();
        routes.setRoutesMap(routeMap);

        return routes;
    }

    private Schedules getMockSchedules(){
        Schedules schedules = new Schedules();

        schedules.setMonth("1");

        Days days = new Days();
        days.setDay(1);

        FlightSchedules flightSchedules = new FlightSchedules();
        flightSchedules.setArrivalTime(createStringDate());
        flightSchedules.setDepartureTime(createStringDate());
        flightSchedules.setNumber("150");

        List<FlightSchedules> flightSchedulesList = Arrays.asList(flightSchedules);
        days.setFlights(flightSchedulesList);

        schedules.setDays(Arrays.asList(days));

        return schedules;
    }


    private String createStringDate(){
        SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm");
        return df.format(new Date());
    }


    private Route createRoute (String departure, String arrival){
        Route route = new Route();
        route.setAirportFrom(departure);
        route.setAirportTo(arrival);
        return route;
    }
}
