package com.task.ryanairtest.domain.dto;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.io.Serializable;
import java.util.List;

public class Schedules implements Serializable {

    private String month;
    private List<Days> days;

    public Schedules() {
    }

    public Schedules(
            String month,
            List<Days> days) {
        this.month = month;
        this.days = days;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public List<Days> getDays() {
        return days;
    }

    public void setDays(List<Days> days) {
        this.days = days;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Schedules schedules = (Schedules) o;

        return new EqualsBuilder()
                .append(month, schedules.month)
                .append(days, schedules.days)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(month)
                .append(days)
                .toHashCode();
    }
}
