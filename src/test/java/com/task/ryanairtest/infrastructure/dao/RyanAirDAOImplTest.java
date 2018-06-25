package com.task.ryanairtest.infrastructure.dao;

import com.task.ryanairtest.domain.dao.RyanAirDAO;
import com.task.ryanairtest.domain.dto.Route;
import com.task.ryanairtest.domain.dto.Routes;
import com.task.ryanairtest.domain.dto.Schedules;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.properties")
public class RyanAirDAOImplTest {

    private static final String DEPARTURE = "MAD";
    private static final String ARRIVAL = "BCN";
    private static final String YEAR = "2018";
    private static final String MONTH = "1";
    private RyanAirDAO ryanAirDAO;

    @Before
    public void init(){
        ryanAirDAO = new RyanAirDAOImpl();
    }

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
