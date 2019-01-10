package com.webox.config;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Iterator;

import javax.annotation.PostConstruct;

import com.webox.common.process.ServiceManage;
import com.webox.common.utils.AppData;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class StartUpInit {

    @Autowired
    @Qualifier("generalServiceManage")
    private ServiceManage serviceManage;

    @PostConstruct
    public void init() {

         System.out.println("Start to initialize Data........");
        StringBuffer initialDataStr = assembleJsonObject("initialData.json");
        JSONObject jsonObject = new JSONObject(initialDataStr.toString());
        try {
            // System.out.println("loading cities code... ");
            JSONArray citiesArray = (JSONArray) jsonObject.get("cities");
            Iterator<Object> iteratorCities = citiesArray.iterator();
            while (iteratorCities.hasNext()) {
                JSONObject next = (JSONObject) iteratorCities.next();

                Iterator<?> keys = next.keys();
                String cityName = "";
                String cityCode = "";
                while (keys.hasNext()) {
                    String key = (String) keys.next();
                    if (("cityName").equalsIgnoreCase(key)) {
                        cityName = next.getString(key);
                    }
                    if (("cityId").equalsIgnoreCase(key)) {
                        cityCode = next.getString(key);
                    }
                }
                // System.out.println(next.toString());
                AppData.cityCode.put(cityCode, cityName);
            }
            // serviceTypes
            // System.out.println("loading serviceTypes ... ");
            JSONArray serviceTypes = (JSONArray) jsonObject.get("serviceTypes");
            Iterator<Object> iteratorServiceTypes = serviceTypes.iterator();
            while (iteratorServiceTypes.hasNext()) {
                JSONObject next = (JSONObject) iteratorServiceTypes.next();
                Iterator<?> keys = next.keys();
                String typeName = "";
                String typeId = "";
                while (keys.hasNext()) {
                    String key = (String) keys.next();
                    if (("typeName").equalsIgnoreCase(key)) {
                        typeName = next.getString(key);
                    }
                    if (("typeId").equalsIgnoreCase(key)) {
                        typeId = next.getString(key);
                    }
                }
                // System.out.println(next.toString());
                AppData.serviceType.put(typeId, typeName);
            }
            // System.out.println(jsonObject.get("serviceFunction").toString());
            AppData.serviceFunction = jsonObject.get("serviceFunction").toString();

        } catch (Exception e) {

        }

        // Loading searching matrix into memory
        // Loading services matrix
        serviceManage.fillupSearchingMatrix();
      //  System.out.println("service keywords collections" + AppData.searchingMatrix.get("services").toString());
        // end of Loading
    }

    private StringBuffer assembleJsonObject(String checkResource) {
        StringBuffer stringBuffer = new StringBuffer();
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(checkResource);
        BufferedReader r = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        String jsonString = null;
        try {
            while ((line = r.readLine()) != null) {
                stringBuilder.append(line);
            }
            jsonString = stringBuilder.toString();
        } catch (Exception e) {
            e.getStackTrace();
        }
        return stringBuffer.append(jsonString);
    }

}