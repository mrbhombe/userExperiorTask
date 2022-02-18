package com.userExperios.task.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.json.simple.JSONObject;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Data
public class EventData {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;
    Long controlId;
    String activityName;
    Date timeDate;
    Long duration;

    public EventData(JSONObject e) throws ParseException {
        this.activityName = e.get("activityName").toString();
        this.timeDate = getTimeInDate((String)e.get("time"));
        this.duration = (Long) e.get("duration");
    }

    private Date getTimeInDate(String time) throws ParseException {

        if(time==null)
            return null;

        Date date = null;
        Calendar c = Calendar.getInstance();
        //System.out.println(1);
        SimpleDateFormat datetimeFormatter1 = new SimpleDateFormat("yyyy-MM-dd");
        date = datetimeFormatter1.parse(time);
        c.setTime(date);
        return c.getTime();
    }
}
