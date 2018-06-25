package com.task.ryanairtest.domain.dto;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.io.Serializable;
import java.util.List;

public class Days implements Serializable{

    private Integer day;
    private List<FlightSchedules> flights;

    public Days() {
    }

    public Days(
            Integer day,
            List<FlightSchedules> flights) {
        this.day = day;
        this.flights = flights;
    }

    public Integer getDay() {
        return day;
    }

    public void setDay(Integer day) {
        this.day = day;
    }

    public List<FlightSchedules> getFlights() {
        return flights;
    }

    public void setFlights(List<FlightSchedules> flights) {
        this.flights = flights;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Days that = (Days) o;

        return new EqualsBuilder()
                .append(day, that.day)
                .append(flights, that.flights)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(day)
                .append(flights)
                .toHashCode();
    }
}
