package com.task.ryanairtest.infrastructure.services;

import com.task.ryanairtest.domain.dao.RyanAirDAO;
import com.task.ryanairtest.domain.dto.*;
import com.task.ryanairtest.domain.services.RyanAirServices;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class RyanAirServicesImplTest {

    private static final String DEPARTURE = "BCN";
    private static final String ARRIVAL = "IBZ";
    private static final String MONTH = "7";
    private static final String YEAR = "2018";
    private static final Integer DAY = 10;
    private static final String NUMBE_OF_FLIGHT = "1001";

    private static final String PATTERN_DATE = "yyyy-MM-dd'T'HH:mm";
    private static final String DATE_DEPARTURE = "2018-06-27T10:00";
    private static final String DATE_ARRIVAL = "2018-06-27T20:00";
    private static final String DATE_HOUR_MINUTE = "08:00";


    @Autowired
    private RyanAirServices ryanAirServices;

    private RyanAirDAO ryanAirDAO = mock(RyanAirDAO.class);

    @Before
    public void init() {

    }

    @Test
    public void givenDepartureAndArrivalAirportWithTimesWhenInvokeServiceThenReturnInterconnectionsFlights() {

        // given
        Date departureTime = createDateFromString(DATE_DEPARTURE);
        Date arrivalTime = createDateFromString(DATE_ARRIVAL);

        // When
        when(ryanAirDAO.getRoutes()).thenReturn(getMockRoutes());
        when(ryanAirDAO.getSchedules(DEPARTURE, ARRIVAL, YEAR, MONTH)).thenReturn(getMockSchedules());

        List<Interconnection> res = ryanAirServices.getInterconnections(DEPARTURE, ARRIVAL, departureTime, arrivalTime);

        // Then
        assertThat( res != null );
        assertThat( res.size() == 2);
    }


    private Routes getMockRoutes() {
        List<Route> routeListBCN = new ArrayList();
        Route route1 = createRoute("BCN", "MAD");
        Route route3 = createRoute("BCN", "IBZ");
        routeListBCN.add(route1);
        routeListBCN.add(route3);

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

        schedules.setMonth(MONTH);

        Days days = new Days();
        days.setDay(DAY);

        FlightSchedules flightSchedules = new FlightSchedules();
        flightSchedules.setArrivalTime(DATE_DEPARTURE);
        flightSchedules.setDepartureTime(DATE_ARRIVAL);
        flightSchedules.setNumber(NUMBE_OF_FLIGHT);


        List<FlightSchedules> flightSchedulesList = Arrays.asList(flightSchedules);
        days.setFlights(flightSchedulesList);

        schedules.setDays(Arrays.asList(days));

        return schedules;
    }


    private Date createDateFromString(String  dateStr){
        DateFormat df = new SimpleDateFormat(PATTERN_DATE);
        try {
            return df.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }


    private Route createRoute (String departure, String arrival){
        Route route = new Route();
        route.setAirportFrom(departure);
        route.setAirportTo(arrival);
        return route;
    }
}
