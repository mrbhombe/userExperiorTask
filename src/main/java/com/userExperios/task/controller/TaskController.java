package com.userExperios.task.controller;

import com.userExperios.task.TaskServiceImpl.TaskServiceImpl;
import com.userExperios.task.model.GeneralResponse;
import com.userExperios.task.model.response.MainResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class TaskController {


    @Autowired
    TaskServiceImpl taskService;


    @GetMapping("/add")
    public GeneralResponse<Boolean> addEventFromFolder()
    {
        GeneralResponse<Boolean> result = null;
        try{

            Boolean flag = taskService.addFilesIntoDb();
            if(flag == true)
            {
                result = new GeneralResponse<>(flag,true,"Data added",System.currentTimeMillis(), HttpStatus.OK);
            }
            else
            {
                result = new GeneralResponse<>(flag,true,"Data not found",System.currentTimeMillis(), HttpStatus.OK);
            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
            result = new GeneralResponse<>(null,false,e.getMessage(),System.currentTimeMillis(), HttpStatus.BAD_REQUEST);

        }
        return result;
    }


    @GetMapping("/getData")
    public GeneralResponse<MainResponse> getAllResponse(@RequestParam("date") String date)
    {
        GeneralResponse<MainResponse> result = null;
        try{

            if(date == null || date == "")
                throw new Exception("NULL RECORD PASSED");

            MainResponse mainResponse= taskService.getDataForStatitics(date);
            if(mainResponse == null)
            {
                result = new GeneralResponse<>(mainResponse,true,"Data found",System.currentTimeMillis(), HttpStatus.OK);
            }
            else
            {
                result = new GeneralResponse<>(mainResponse,true,"Data not found",System.currentTimeMillis(), HttpStatus.OK);
            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
            result = new GeneralResponse<>(null,false,e.getMessage(),System.currentTimeMillis(), HttpStatus.BAD_REQUEST);

        }
        return result;
    }

}
