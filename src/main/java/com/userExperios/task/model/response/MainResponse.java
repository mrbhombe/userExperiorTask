package com.userExperios.task.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MainResponse {

    List<ActivityForMonth> activityForMonthList;
    List<ActivityForWithCompare> activityStaticsYesterdayVsToday;
}
