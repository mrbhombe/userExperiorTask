package com.userExperios.task.dao;

import com.userExperios.task.model.EventData;
import com.userExperios.task.model.response.ActivityForMonth;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

public interface EventDataDao extends JpaRepository<EventData,Long> {


    @Query("select new com.userExperios.task.model.response.ActivityForMonth(ed.activityName,count(ed.id)) from EventData ed where DATE(ed.timeDate) >= DATE(:from) AND DATE(ed.timeDate)<=DATE(:to) GROUP BY ed.activityName")
    List<ActivityForMonth> getEventWithOccurrence(Date from, Date to);

    @Query("select new com.userExperios.task.model.response.ActivityForMonth(ed.activityName,count(ed.id)) from EventData ed where DATE(ed.timeDate) = DATE(:to) GROUP BY ed.activityName")
    List<ActivityForMonth> getEventsStatiticsByDate(Date to);
}
