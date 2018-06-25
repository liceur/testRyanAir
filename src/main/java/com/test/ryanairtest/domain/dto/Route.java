package com.test.ryanairtest.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Route {

    private String airportFrom;
    private String airportTo;
    private String connectingAirport;
    private Boolean newRoute;
    private Boolean seasonalRoute;
    private String group;


    public Route() {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public Route(String airportFrom, String airportTo, String connectingAirport, Boolean newRoute, Boolean seasonalRoute, String group) {
        this.airportFrom = airportFrom;
        this.airportTo = airportTo;
        this.connectingAirport = connectingAirport;
        this.newRoute = newRoute;
        this.seasonalRoute = seasonalRoute;
        this.group = group;
    }

    public String getAirportFrom() {
        return airportFrom;
    }

    public void setAirportFrom(String airportFrom) {
        this.airportFrom = airportFrom;
    }

    public String getAirportTo() {
        return airportTo;
    }

    public void setAirportTo(String airportTo) {
        this.airportTo = airportTo;
    }

    public String getConnectingAirport() {
        return connectingAirport;
    }

    public void setConnectingAirport(String connectingAirport) {
        this.connectingAirport = connectingAirport;
    }

    public Boolean getNewRoute() {
        return newRoute;
    }

    public void setNewRoute(Boolean newRoute) {
        this.newRoute = newRoute;
    }

    public Boolean getSeasonalRoute() {
        return seasonalRoute;
    }

    public void setSeasonalRoute(Boolean seasonalRoute) {
        this.seasonalRoute = seasonalRoute;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Route routes = (Route) o;

        return new EqualsBuilder()
                .append(airportFrom, routes.airportFrom)
                .append(airportTo, routes.airportTo)
                .append(connectingAirport, routes.connectingAirport)
                .append(newRoute, routes.newRoute)
                .append(seasonalRoute, routes.seasonalRoute)
                .append(group, routes.group)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(airportFrom)
                .append(airportTo)
                .append(connectingAirport)
                .append(newRoute)
                .append(seasonalRoute)
                .append(group)
                .toHashCode();
    }
}
