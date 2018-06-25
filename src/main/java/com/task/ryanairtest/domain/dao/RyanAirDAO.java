package com.task.ryanairtest.domain.dao;


import com.task.ryanairtest.domain.dto.Route;
import com.task.ryanairtest.domain.dto.Schedules;

import java.util.List;

public interface RyanAirDAO {

    List<Route> getRoutes();

    Schedules getSchedules(String departure, String arrival, String year, String month);

}