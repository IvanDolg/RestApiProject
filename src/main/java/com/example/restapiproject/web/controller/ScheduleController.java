package com.example.restapiproject.web.controller;

import lombok.RequiredArgsConstructor;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/schedule")
@RequiredArgsConstructor
public class ScheduleController {

    @GetMapping("/{studentGroup}/{weekDay}")
    public ResponseEntity<String> getSchedule(@PathVariable("studentGroup") String studentGroup,
                                              @PathVariable("weekDay") String weekDay) {
        String currentWeekApiUrl = "https://iis.bsuir.by/api/v1/schedule/current-week";
        String apiUrl = "https://iis.bsuir.by/api/v1/schedule?studentGroup=" + studentGroup;

        try {
            HttpClient httpClient = HttpClientBuilder.create().build();

            HttpGet currentWeekHttpGet = new HttpGet(currentWeekApiUrl);
            HttpResponse currentWeekResponse = httpClient.execute(currentWeekHttpGet);
            String currentWeekResponseBody = EntityUtils.toString(currentWeekResponse.getEntity());
            int currentWeek = Integer.parseInt(currentWeekResponseBody);

            HttpGet scheduleHttpGet = new HttpGet(apiUrl);
            HttpResponse scheduleResponse = httpClient.execute(scheduleHttpGet);
            String responseBody = EntityUtils.toString(scheduleResponse.getEntity());

            JSONObject responseObj = new JSONObject(responseBody);
            JSONObject schedulesObj = responseObj.getJSONObject("schedules");

            JSONArray scheduleArray = schedulesObj.getJSONArray(weekDay);
            JSONArray filteredScheduleArray = new JSONArray();

            for (Object obj : scheduleArray) {
                if (obj instanceof JSONObject) {
                    JSONObject scheduleObj = (JSONObject) obj;
                    JSONArray weekNumberArray = scheduleObj.getJSONArray("weekNumber");

                    if (weekNumberArray.toList().contains(currentWeek)) {
                        filteredScheduleArray.put(scheduleObj);
                    }
                }
            }
            JSONArray subjectsArray = new JSONArray();
            for (Object obj : filteredScheduleArray) {
                if (obj instanceof JSONObject) {
                    JSONObject scheduleObj = (JSONObject) obj;
                    subjectsArray.put(scheduleObj.getString("subject"));
                }
            }
            return ResponseEntity.ok(subjectsArray.toString());
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
            HttpClient httpClient = HttpClients.createDefault();

            HttpGet scheduleHttpGet = new HttpGet(apiUrl);
            HttpResponse scheduleResponse = httpClient.execute(scheduleHttpGet);
            String responseBody = EntityUtils.toString(scheduleResponse.getEntity());

            HttpGet currentWeekHttpGet = new HttpGet(currentWeekApiUrl);
            HttpResponse currentWeekResponse = httpClient.execute(currentWeekHttpGet);
            String currentWeekResponseBody = EntityUtils.toString(currentWeekResponse.getEntity());
            int currentWeek = Integer.parseInt(currentWeekResponseBody);

            JSONObject responseObj = new JSONObject(responseBody);
            JSONObject schedulesObj = responseObj.getJSONObject("schedules");

            JSONObject scheduleByDayOfWeek = new JSONObject();

            for (String dayOfWeek : schedulesObj.keySet()) {
                JSONArray scheduleArray = schedulesObj.getJSONArray(dayOfWeek);

                JSONArray subjectsArray = new JSONArray();
                for (int i = 0; i < scheduleArray.length(); i++) {
                    JSONObject scheduleObj = scheduleArray.getJSONObject(i);
                    JSONArray weekNumberArray = scheduleObj.getJSONArray("weekNumber");

                    if (weekNumberArray.toList().contains(currentWeek)) {
                        subjectsArray.put(scheduleObj.getString("subject"));
                    }
                }
                scheduleByDayOfWeek.put(dayOfWeek, subjectsArray);
            }

            return ResponseEntity.ok(scheduleByDayOfWeek.toString());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Error occurred");
        }
    }
}