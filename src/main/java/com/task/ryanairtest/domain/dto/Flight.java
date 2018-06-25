package com.task.ryanairtest.domain.dto;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.Date;

public class Flight {

    private String number;
    private String departure;
    private String arrival;
    private Date departureTime;
    private Date arrivalTime;


    public Flight() {
    }

    public Flight(String number, String departure, String arrival, Date departureTime, Date arrivalTime) {
        this.number = number;
        this.departure = departure;
        this.arrival = arrival;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
    }


    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getDeparture() {
        return departure;
    }

    public void setDeparture(String departure) {
        this.departure = departure;
    }

    public String getArrival() {
        return arrival;
    }

    public void setArrival(String arrival) {
        this.arrival = arrival;
    }

    public Date getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(Date departureTime) {
        this.departureTime = departureTime;
    }

    public Date getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(Date arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Flight flight = (Flight) o;

        return new EqualsBuilder()
                .append(number, flight.number)
                .append(departure, flight.departure)
                .append(arrival, flight.arrival)
                .append(departureTime, flight.departureTime)
                .append(arrivalTime, flight.arrivalTime)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(number)
                .append(departure)
                .append(arrival)
                .append(departureTime)
                .append(arrivalTime)
                .toHashCode();
    }
}
