package net.engineeringdigest.journalApp.service;

import net.engineeringdigest.journalApp.ApiRespsonse.WeatherResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class WeatherServices {
   @Value("${weather.api.key}")
    private String apiKey;

    @Autowired
    RestTemplate restTemplate=new RestTemplate();

    public double getWeather(String city){
        String uri="http://api.openweathermap.org/data/2.5/weather?" +
                "q={city}&appid={apiKey}&units=metric";
        String finalUrl = uri
                .replace("{apiKey}", apiKey)
                .replace("{city}", city);

        ResponseEntity<WeatherResponse> response = restTemplate.exchange(finalUrl, HttpMethod.GET, null, WeatherResponse.class);

        assert response.getBody() != null;
        return response.getBody().getMain().getFeelsLike();
    }



}
