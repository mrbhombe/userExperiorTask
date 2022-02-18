package com.userExperios.task.TaskServiceImpl;

import com.userExperios.task.dao.EventDataDao;
import com.userExperios.task.dao.EventMastDao;
import com.userExperios.task.model.EventData;
import com.userExperios.task.model.EventMast;
import com.userExperios.task.model.response.ActivityForMonth;
import com.userExperios.task.model.response.ActivityForWithCompare;
import com.userExperios.task.model.response.MainResponse;
import org.json.simple.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;


@Service("taskService")
public class TaskServiceImpl {

    @Autowired
    EventMastDao eventMastDao;

    @Autowired
    EventDataDao eventDataDao;

    public Boolean addFilesIntoDb() throws Exception {

        List<EventMast> eventMastListToSave = new ArrayList<>();
        List<EventMast> eventMastList = new ArrayList<>();
        File directory = new File("ActivitiesToProcess");
        if (!directory.exists())
            throw new Exception("Directory not found");


        if (directory.listFiles().length < 10 || directory.listFiles().length > 100000)
            throw new Exception("Directory file should be > 10 and < 100000");

        List<String> activityNames = new ArrayList<>(Arrays.asList(
                "doubleTap", "singleTap", "crash", "anr"
        ));
        List<Object> uniqueListIds = new ArrayList<>();

        File[] directoryListing = directory.listFiles();
        if (directoryListing != null) {

            //check all the validation
            //1.Unique id validation
            //2.File containe more than 10 and <100000
            //3.check the activityName
            for (File actualFile : directoryListing) {

                //System.out.println(actualFile.getAbsolutePath());
                JSONParser jsonParser = new JSONParser();
                FileReader reader = new FileReader(actualFile);
                Object jsonArrayObjectFromFile = jsonParser.parse(reader);
                JSONObject mappingJsonObject = (JSONObject) jsonArrayObjectFromFile;

                JSONArray jsonArray = (JSONArray) mappingJsonObject.get("activities");
                for (int i = 0; i < jsonArray.size(); i++) {
                    //System.out.println(jsonArray.size());
                    JSONObject activityJsonObject = (JSONObject) jsonArray.get(i);
                    if (!activityNames.contains((String) activityJsonObject.get("activityName"))) {
                        throw new Exception("Activity not found");
                    }
                }

                if (!uniqueListIds.contains(mappingJsonObject.get("uniqueId"))) {
                    //check in db record is exist
                    EventMast eventMastExistWithUniqueId = eventMastDao.getEventMastUniqueId((String) mappingJsonObject.get("uniqueId"));
                    if (eventMastExistWithUniqueId != null)
                        throw new Exception("record already exist");
                } else {
                    throw new Exception("duplicate unique id found");
                }
                uniqueListIds.add(mappingJsonObject.get("uniqueId"));
            }


            //store record in db
            for (File actualFile : directoryListing) {

                JSONParser jsonParser = new JSONParser();
                FileReader reader = new FileReader(actualFile);
                Object jsonArrayObjectFromFile = jsonParser.parse(reader);
                JSONObject mappingJsonObject = (JSONObject) jsonArrayObjectFromFile;

                List<EventData> eventDataList = new ArrayList<>();
                EventMast eventMast = new EventMast(mappingJsonObject.get("uniqueId"));
                JSONArray jsonArrayOfEvents = (JSONArray) mappingJsonObject.get("activities");
                jsonArrayOfEvents.forEach(e -> {
                    EventData eventData = null;
                    try {
                        eventData = new EventData((JSONObject) e);
                    } catch (ParseException ex) {
                        ex.printStackTrace();
                    }
                    eventDataList.add(eventData);
                });
                eventMast.setEventDataList(eventDataList);
                eventMastListToSave.add(eventMast);
                reader.close();
            }
        }

        eventMastDao.saveAll(eventMastListToSave);
        return true;
    }

    public MainResponse getDataForStatitics(String record) throws ParseException {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date to = simpleDateFormat.parse(record);
        calendar.setTime(to);
        to = calendar.getTime();

        calendar.add(Calendar.DATE, -30);

        Date from = calendar.getTime();

        MainResponse mainResponse = new MainResponse();
        List<ActivityForMonth> activityForMonthList = eventDataDao.getEventWithOccurrence(from, to);
        mainResponse.setActivityForMonthList(activityForMonthList);
        //from = to;
        calendar.setTime(to);
        calendar.add(Calendar.DATE, -1);
        from = calendar.getTime();
        List<ActivityForMonth> todayActivities = eventDataDao.getEventsStatiticsByDate(to);
        List<ActivityForMonth> yesterdayActivities = eventDataDao.getEventsStatiticsByDate(from);
        List<ActivityForWithCompare> activityForWithCompares = null;
        if (yesterdayActivities.size() > todayActivities.size()) {
            activityForWithCompares = compareWithYesterDay(yesterdayActivities, todayActivities);
        }
        else
        {
            activityForWithCompares = compareWithToday(yesterdayActivities, todayActivities);
        }
        mainResponse.setActivityStaticsYesterdayVsToday(activityForWithCompares);


        return mainResponse;
    }

    private List<ActivityForWithCompare> compareWithToday(List<ActivityForMonth> yesterdayActivities, List<ActivityForMonth> todayActivities) {
        List<ActivityForWithCompare> list = new ArrayList<>();
        todayActivities.forEach(e -> {
            Optional<ActivityForMonth> activityForMonth = yesterdayActivities.stream().filter(activity -> activity.getActivityName().equalsIgnoreCase(e.getActivityName())).findFirst();

            if (activityForMonth.isPresent()) {
                list.add(new ActivityForWithCompare(e.getActivityName(),activityForMonth.get().getOccurrence(),e.getOccurrence()));
            } else {
                list.add(new ActivityForWithCompare(e.getActivityName(), 0l, e.getOccurrence()));
            }

        });

        return list;
    }

    private List<ActivityForWithCompare> compareWithYesterDay(List<ActivityForMonth> yesterdayActivities, List<ActivityForMonth> todayActivities) {
        List<ActivityForWithCompare> list = new ArrayList<>();
        yesterdayActivities.forEach(e -> {
            Optional<ActivityForMonth> activityForMonth = todayActivities.stream().filter(activity -> activity.getActivityName().equalsIgnoreCase(e.getActivityName())).findFirst();

            if (activityForMonth.isPresent()) {
                list.add(new ActivityForWithCompare(e.getActivityName(), e.getOccurrence(), activityForMonth.get().getOccurrence()));
            } else {
                list.add(new ActivityForWithCompare(e.getActivityName(), e.getOccurrence(), 0l));
            }

        });

        return list;
    }

}
