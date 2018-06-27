package com.task.ryanairtest.app;


import com.task.ryanairtest.client.InterconnectionPayload;
import com.task.ryanairtest.domain.dto.Flight;
import com.task.ryanairtest.domain.dto.Interconnection;
import com.task.ryanairtest.domain.services.RyanAirServices;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class RyanAirControllerTest {

    public static final String DEPARTURE = "MAD";
    public static final String ARRIVAL = "IBZ";
    public static final String DEPARTURE_TIME = "2018-06-29T12:00";
    public static final String ARRIVAL_TIME = "2018-06-29T23:00";

    private RyanAirServices ryanAirServices = mock(RyanAirServices.class);

    private RyanAirController ryanAirController = new RyanAirController(ryanAirServices);


    @Test
    public void givenADepartureAndArrivalAndDepartureTimAndArrivalTimeWhenInvokeControllerThenReturnFlightResponsePayload() throws Exception {

        // given

        // when
        when(ryanAirServices.getInterconnections(DEPARTURE,
                ARRIVAL,
                TimeUtils.fromISO8601UTC(DEPARTURE_TIME),
                TimeUtils.fromISO8601UTC(ARRIVAL_TIME))).thenReturn(getMockInterconnectionList());


        List<InterconnectionPayload> res = ryanAirController.getInterconnections(DEPARTURE, ARRIVAL, DEPARTURE_TIME, ARRIVAL_TIME);

        // then
        assertThat(res != null);
        assertThat(!res.isEmpty());
        assertThat(res.size() == 1);


    }


    private List<Interconnection> getMockInterconnectionList(){

        List<Flight> flights = new ArrayList<>();
        Flight flight = new Flight();
        flight.setDeparture(DEPARTURE);
        flight.setArrival(ARRIVAL);
        flight.setDepartureTime(TimeUtils.fromISO8601UTC(DEPARTURE_TIME));
        flight.setArrivalTime(TimeUtils.fromISO8601UTC(ARRIVAL_TIME));
        flights.add(flight);

        Interconnection interconnection = new Interconnection(1, flights);

        return Arrays.asList(interconnection);
    }

}
