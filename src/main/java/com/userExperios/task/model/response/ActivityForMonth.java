package com.userExperios.task.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ActivityForMonth {

    String activityName;
    Long occurrence;

    public ActivityForMonth(String activityName, Long occurrence) {
        this.activityName = activityName;
        this.occurrence = occurrence;
    }
}
