package com.task.ryanairtest.infrastructure.dao;

import com.task.ryanairtest.domain.dao.RyanAirDAO;
import com.task.ryanairtest.domain.dto.Route;
import com.task.ryanairtest.domain.dto.Routes;
import com.task.ryanairtest.domain.dto.Schedules;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


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
    public Routes getRoutes() {

        logger.debug("Load routes...");

        Route[] routesList = restTemplate.getForObject(apiRoutesPath, Route[].class);

        // Filter avoid routes with connect
        return getFilterRoutes(routesList);
    }

    /**
     * Filter routes , we get only with connectingAirport=null
     * @param routesList
     * @return
     */
    private Routes getFilterRoutes(Route[] routesList) {
        Map<String, List<Route>> result = Arrays.asList(routesList)
                .stream()
                .filter(x -> x.getConnectingAirport() == null)
                .collect(Collectors.groupingBy(Route::getAirportFrom));

        Routes routes = new Routes(result);
        routes.setRoutesMap(result);
        return routes;
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
