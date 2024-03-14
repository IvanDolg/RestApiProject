package com.example.restapiproject.web.controller;

import lombok.RequiredArgsConstructor;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/schedule")
@RequiredArgsConstructor
public class Schedule {

    @GetMapping("/{studentGroup}/{weekDay}")
    public ResponseEntity<String> getSchedule(@PathVariable("studentGroup") String studentGroup,
                                              @PathVariable("weekDay") String weekDay) {

        String currentWeekApiUrl = "https://iis.bsuir.by/api/v1/schedule/current-week";
        String apiUrl = "https://iis.bsuir.by/api/v1/schedule?studentGroup=" + studentGroup;

        try {
            HttpClient httpClient = HttpClientBuilder.create().build();

            HttpGet currentWeekHttpGet = new HttpGet(currentWeekApiUrl);
            HttpResponse currentWeekResponse = httpClient.execute(currentWeekHttpGet);
            HttpEntity currentWeekEntity = currentWeekResponse.getEntity();
            String currentWeekResponseBody = EntityUtils.toString(currentWeekEntity);
            int currentWeek = Integer.parseInt(currentWeekResponseBody);

            HttpGet scheduleHttpGet = new HttpGet(apiUrl);
            HttpResponse scheduleResponse = httpClient.execute(scheduleHttpGet);
            HttpEntity scheduleEntity = scheduleResponse.getEntity();
            String responseBody = EntityUtils.toString(scheduleEntity);

            JSONObject responseObj = new JSONObject(responseBody);
            JSONObject schedulesObj = responseObj.getJSONObject("schedules");


            JSONArray scheduleArray = schedulesObj.getJSONArray(weekDay);
            JSONArray filteredScheduleArray = new JSONArray();
            for (int i = 0; i < scheduleArray.length(); i++) {
                JSONObject scheduleObj = scheduleArray.getJSONObject(i);
                JSONArray weekNumberArray = scheduleObj.getJSONArray("weekNumber");
                boolean containsCurrentWeek = false;
                for (int j = 0; j < weekNumberArray.length(); j++) {
                    int weekNumberValue = weekNumberArray.getInt(j);
                    if (weekNumberValue == currentWeek) {
                        containsCurrentWeek = true;
                        break;
                    }
                }
                if (containsCurrentWeek) {
                    filteredScheduleArray.put(scheduleObj);
                }
            }

            List<String> subjects = new ArrayList<>();
            for (int i = 0; i < filteredScheduleArray.length(); i++) {
                JSONObject scheduleObj = filteredScheduleArray.getJSONObject(i);
                String subject = scheduleObj.getString("subject");
                subjects.add(subject);
            }

            JSONArray subjectsArray = new JSONArray(subjects);
            String subjectsJson = subjectsArray.toString();

            return ResponseEntity.ok(subjectsJson);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Error occurred");
        }
    }
    @GetMapping("/current-week")
    public ResponseEntity<Integer> getCurrentWeek() {
        String apiUrl = "https://iis.bsuir.by/api/v1/schedule/current-week";

        try {
            HttpClient httpClient = HttpClientBuilder.create().build();
            HttpGet httpGet = new HttpGet(apiUrl);
            HttpResponse response = httpClient.execute(httpGet);
            HttpEntity entity = response.getEntity();
            String responseBody = EntityUtils.toString(entity);

            int currentWeek = Integer.parseInt(responseBody);
            return ResponseEntity.ok(currentWeek);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(-1);
        }
    }


    @GetMapping("/week-schedule/{studentGroup}")
    public ResponseEntity<String> getWeekSchedule(@PathVariable("studentGroup") String studentGroup) {

        String currentWeekApiUrl = "https://iis.bsuir.by/api/v1/schedule/current-week";
        String apiUrl = "https://iis.bsuir.by/api/v1/schedule?studentGroup=" + studentGroup;

        try {
            HttpClient httpClient = HttpClientBuilder.create().build();

            HttpGet scheduleHttpGet = new HttpGet(apiUrl);
            HttpResponse scheduleResponse = httpClient.execute(scheduleHttpGet);
            HttpEntity scheduleEntity = scheduleResponse.getEntity();
            String responseBody = EntityUtils.toString(scheduleEntity);

            HttpGet currentWeekHttpGet = new HttpGet(currentWeekApiUrl);
            HttpResponse currentWeekResponse = httpClient.execute(currentWeekHttpGet);
            HttpEntity currentWeekEntity = currentWeekResponse.getEntity();
            String currentWeekResponseBody = EntityUtils.toString(currentWeekEntity);
            int currentWeek = Integer.parseInt(currentWeekResponseBody);

            JSONObject responseObj = new JSONObject(responseBody);
            JSONObject schedulesObj = responseObj.getJSONObject("schedules");

            JSONArray filteredScheduleArray = new JSONArray();

            for (String key : schedulesObj.keySet()) {
                JSONArray scheduleArray = schedulesObj.getJSONArray(key);
                for (int i = 0; i < scheduleArray.length(); i++) {
                    JSONObject scheduleObj = scheduleArray.getJSONObject(i);
                    JSONArray weekNumberArray = scheduleObj.getJSONArray("weekNumber");

                    // Проверка, содержит ли weekNumberArray текущую неделю
                    boolean containsCurrentWeek = false;
                    for (int j = 0; j < weekNumberArray.length(); j++) {
                        int weekNumberValue = weekNumberArray.getInt(j);
                        if (weekNumberValue == currentWeek) {
                            containsCurrentWeek = true;
                            break;
                        }
                    }

                    if (containsCurrentWeek) {
                        filteredScheduleArray.put(scheduleObj);
                    }
                }
            }

            List<String> subjects = new ArrayList<>();
            for (int i = 0; i < filteredScheduleArray.length(); i++) {
                JSONObject scheduleObj = filteredScheduleArray.getJSONObject(i);
                String subject = scheduleObj.getString("subject");
                subjects.add(subject);
            }

            JSONArray subjectsArray = new JSONArray(subjects);
            String subjectsJson = subjectsArray.toString();

            return ResponseEntity.ok(subjectsJson);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Error occurred");
        }
    }
}