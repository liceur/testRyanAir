package com.test.ryanairtest.client;



import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.test.ryanairtest.domain.dto.IATACode;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;


public class FlightResponsePayload {

    private String departureAirport;
    private String arrivalAirport;
    private LocalDateTime departureDateTime;
    private LocalDateTime arrivalDateTime;

    @JsonCreator
    public FlightResponsePayload(
            @JsonProperty("departureAirport") String departure,
            @JsonProperty("arrivalAirport") String arrival,
            @JsonProperty("departureDateTim") LocalDateTime departureTime,
            @JsonProperty("arrival DateTim") LocalDateTime arrivalTime) {
        this.departureAirport = departure;
        this.arrivalAirport = arrival;
        this.departureDateTime = departureTime;
        this.arrivalDateTime = arrivalTime;
    }

    public String getDepartureAirport() {
        return departureAirport;
    }

    public void setDepartureAirport(String departureAirport) {
        this.departureAirport = departureAirport;
    }

    public String getArrivalAirport() {
        return arrivalAirport;
    }

    public void setArrivalAirport(String arrivalAirport) {
        this.arrivalAirport = arrivalAirport;
    }

    public LocalDateTime getDepartureDateTime() {
        return departureDateTime.withSecond(0);
    }

    public void setDepartureDateTime(LocalDateTime departureDateTime) {
        this.departureDateTime = departureDateTime;
    }

    public LocalDateTime getArrivalDateTime() {
        return arrivalDateTime;
    }

    public void setArrivalDateTime(LocalDateTime arrivalDateTime) {
        this.arrivalDateTime = arrivalDateTime;
    }
}
