package com.userExperios.task.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ActivityForWithCompare {


    String activityName;
    Long yesterdayOccurrence;
    Long todayOccurrence;
    String status;

    public ActivityForWithCompare(String activityName, Long yesterdayOccurrence, Long todayOccurrence, String status) {
        this.activityName = activityName;
        this.yesterdayOccurrence = yesterdayOccurrence;
        this.todayOccurrence = todayOccurrence;
        this.status = status;
    }


    public ActivityForWithCompare(String activityName, Long yesterdayOccurrence, Long todayOccurrence) {
        this.activityName = activityName;
        this.yesterdayOccurrence = yesterdayOccurrence;
        this.todayOccurrence = todayOccurrence;
        this.status = yesterdayOccurrence == todayOccurrence ? "unaltered" : todayOccurrence > yesterdayOccurrence ? "positive" : "negative";

    }
}
