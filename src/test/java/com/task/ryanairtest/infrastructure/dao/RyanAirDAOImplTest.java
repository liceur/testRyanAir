package com.task.ryanairtest.infrastructure.dao;

import com.task.ryanairtest.domain.dao.RyanAirDAO;
import com.task.ryanairtest.domain.dto.Routes;
import com.task.ryanairtest.domain.dto.Schedules;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class RyanAirDAOImplTest {

    private static final String DEPARTURE = "MAD";
    private static final String ARRIVAL = "BCN";
    private static final String YEAR = "2018";
    private static final String MONTH = "7";

    @Autowired
    private RyanAirDAO ryanAirDAO;


    @Test
    public void whenInvokeRouteTheCallRoutesDao(){

        // when
        Routes res = ryanAirDAO.getRoutes();

        // then
        assertThat(res != null);

    }

    @Test
    public void givenDepartureAndArrivanAndYearAndMonthWhenInvokeSchedulesThenReturnSchedules(){

        // Given
        String departure = DEPARTURE;
        String arrival = ARRIVAL;
        String year = YEAR;
        String month = MONTH;

        // When
        Schedules res = ryanAirDAO.getSchedules(departure, arrival, year, month);

        // Then
        assertThat(res != null);

    }
}
