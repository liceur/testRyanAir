package com.task.ryanairtest.client;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class InterconnectionPayload {


        private Integer stops;
        private List<FlightResponsePayload> legs;

        @JsonCreator
        public InterconnectionPayload(
                @JsonProperty("stops") Integer stops,
                List<FlightResponsePayload> legs) {
                this.stops = stops;
                this.legs = legs;
        }

        public Integer getStops() {
                return stops;
        }

        public void setStops(Integer stops) {
                this.stops = stops;
        }

        public List<FlightResponsePayload> getLegs() {
                return legs;
        }

        public void setLegs(List<FlightResponsePayload> legs) {
                this.legs = legs;
        }
}
