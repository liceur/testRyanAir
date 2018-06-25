package com.test.ryanairtest.domain.dto;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.List;

public class Interconnection {

    private Integer stops;
    private List<Flight> legs;


    public Interconnection( Integer stops, List<Flight> legs) {
        this.stops = stops;
        this.legs = legs;
    }

    public Integer getStops() {
        return stops;
    }

    public void setStops(Integer stops) {
        this.stops = stops;
    }

    public List<Flight> getLegs() {
        return legs;
    }

    public void setLegs(List<Flight> legs) {
        this.legs = legs;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Interconnection that = (Interconnection) o;

        return new EqualsBuilder()
                .append(stops, that.stops)
                .append(legs, that.legs)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(stops)
                .append(legs)
                .toHashCode();
    }
}
