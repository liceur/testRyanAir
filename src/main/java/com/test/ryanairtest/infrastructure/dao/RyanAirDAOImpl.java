package com.test.ryanairtest.infrastructure.dao;

import com.test.ryanairtest.domain.dao.RyanAirDAO;
import com.test.ryanairtest.domain.dto.Route;
import com.test.ryanairtest.domain.dto.Schedules;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;


@Repository
public class RyanAirDAOImpl implements RyanAirDAO {

    private static final Logger logger = LogManager
            .getLogger(RyanAirDAOImpl.class);

    @Value("${ryanair.api.routes}")
    private String apiRoutesPath;

    @Value("${ryanair.api.schedules}")
    private String apiSchedulesPath;

    private RestTemplate restTemplate = new RestTemplate();

    @Override
    public List<Route> getRoutes() {

        logger.debug("Load routes...");

        Route[] routesList = restTemplate.getForObject(apiRoutesPath, Route[].class);

        return Arrays.asList(routesList);
    }


    @Override
    public Schedules getSchedules(String departure, String arrival, String year, String month) {
     logger.debug("Load schedules from {} to {} in {} of {}...",
                departure, arrival, month, year);

        Schedules schedules = null;
        try {
            schedules = restTemplate.getForObject(
                    apiSchedulesPath
                    , Schedules.class
                    , departure
                    , arrival
                    , year
                    , month
            );
        }catch (Exception e){
            logger.error("Schedule not fount: " + departure + "-" + arrival);
        }
        return schedules;
    }
}
