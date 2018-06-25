package com.test.ryanairtest.domain.dao;


import com.test.ryanairtest.domain.dto.IATACode;
import com.test.ryanairtest.domain.dto.Route;
import com.test.ryanairtest.domain.dto.Schedules;

import java.util.List;

public interface RyanAirDAO {

    List<Route> getRoutes();

    Schedules getSchedules(String departure, String arrival, String year, String month);

}