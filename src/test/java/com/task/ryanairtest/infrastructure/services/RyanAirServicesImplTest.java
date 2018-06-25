package com.task.ryanairtest.infrastructure.services;

import com.task.ryanairtest.domain.dao.RyanAirDAO;
import com.task.ryanairtest.domain.dto.*;
import com.task.ryanairtest.domain.services.RyanAirServices;
import org.junit.Before;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

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


    private List<Route> getMockRoutes() {
        List<Route> routes = new ArrayList();

        Route route1 = new Route();
        route1.setAirportFrom("MAD");
        route1.setAirportTo("BCN");

        Route route2 = new Route();
        route2.setAirportFrom("BCN");
        route2.setAirportTo("DUB");

        routes.add(route1);
        routes.add(route2);

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

}
