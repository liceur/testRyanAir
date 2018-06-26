package com.task.ryanairtest.domain.dto;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.List;
import java.util.Map;

public class Routes {

    private Map<String, List<Route>> routesMap;


    public Routes() {
    }

    public Routes(Map<String, List<Route>> routesMap) {
        this.routesMap = routesMap;
    }

    public Map<String, List<Route>> getRoutesMap() {
        return routesMap;
    }

    public void setRoutesMap(Map<String, List<Route>> routesMap) {
        this.routesMap = routesMap;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Routes routes = (Routes) o;

        return new EqualsBuilder()
                .append(routesMap, routes.routesMap)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(routesMap)
                .toHashCode();
    }
}
