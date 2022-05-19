package com.example.springweatherbot;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Map;

public class Weather {
	//токен подключения к сервису погоды
	private static final String API_KEY = "19f88a2991b65b36f4b237bd357f3764\r\n";
	//шаблон запроса
	private static final String REQUEST_URL = "http://api.openweathermap.org/data/2.5/weather?" +
            "lang=ru&" +
            "units=metric&" +
            "q=%s&" +
            "appid=%s";
	
	public static String getWeather(String city) throws IOException {
		//формирования строки запроса(подставляб город и токен)
		String requesUrl=String.format(REQUEST_URL,city, API_KEY);
		//извлекаю данные из ответа
		URL url = new URL(requesUrl);
		//выполняю запрос и получаю ответ
		URLConnection conn = url.openConnection();
		
		InputStream is = conn.getInputStream();
		InputStreamReader isr = new InputStreamReader(is);
		BufferedReader reader = new BufferedReader(isr);
		
		final StringBuffer buffer = new StringBuffer();
		//строки ответа в текстовом буфере
        reader.lines().forEach(line->buffer.append(line));       
        System.out.println(buffer);
		
		System.out.println("Connect done!");
		String result = buffer.toString();
		//Десериализация из JSON -строки в обьект
		JSONObject json = new JSONObject(result);
		
		
		Map<String, Object> jsonMap = json.toMap();
		Map<String, Object> mainMap = (Map<String, Object>) jsonMap.get("main");
		ArrayList<Object> weather2 = (ArrayList<Object>) jsonMap.get("weather");
		Map<String, Object> weather3 = (Map<String, Object>) weather2.get(0);
		Map<String, Double> wind = (Map<String, Double>) jsonMap.get("wind");
		String speed = String.valueOf(wind.get("speed"));
		double temp;
		if (mainMap.get("temp") instanceof BigDecimal) {
			temp = ((BigDecimal) mainMap.get("temp")).doubleValue();
		} else {
//			temp = (double) mainMap.get("temp");
			temp = (double) mainMap.get("temp");
		}
		String plusOrMunis = temp >= 1 ? "+" : "";
		
		double description1 ;
		if (mainMap.get("feels_like") instanceof BigDecimal) {
			description1 = ((BigDecimal) mainMap.get("feels_like")).doubleValue();
		} else {
			description1 = (double) mainMap.get("feels_like");
		}
		String plusOrMunis2 = description1 >= 1 ? "+" : "";
		String description2;
		if (weather3.get("description") instanceof Object) {
			description2 =  (String) (weather3.get("description"));
		} else {
			description2 = (String) mainMap.get("feels_like");
		}

		city = (String) jsonMap.get("name");
		int temp2 = (int) temp;
		result = String.format("Текущая температура в городе %s: "+"%s" + " %d ," +
				" \nЧувствуеться как: " + "%s "+ "%s " + "и " + "%s  ", city, plusOrMunis, (int) temp, plusOrMunis2,description1,description2 +
				" \nСкорость ветра: " +  speed + " метра в секунду."
		);
		return result;
		
	}
	
}
