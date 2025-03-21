package com.springapi.demo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.springapi.demo.model.weatherResponse.CurrentWeatherModel;
import com.springapi.demo.model.weatherResponse.ForecastModel;
import com.springapi.demo.model.weatherResponse.LocationModel;
import com.springapi.demo.services.WeatherService;
import com.springapi.demo.util.ResponseObject;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class WeatherServiceTests {

	@InjectMocks
	private WeatherService weatherService;

	@Mock
	private RestTemplate restTemplate;

	private String key = "mockApiKey";

	private String host = "https://mockweatherapi.com/";

	@BeforeEach
	void setUp() {
		ReflectionTestUtils.setField(weatherService, "key", key);
		ReflectionTestUtils.setField(weatherService, "host", host);
	}

	@Test
	void testGetWeatherFromLatAndLon_Success() {
		double lat = 40.7128;
		double lon = -74.0060;

		String uri = host
				+ String.format("data/2.5/weather?lat=%f&lon=%f&appid=%s&units=%s", lat, lon, key, "imperial");
		CurrentWeatherModel mockResponse = new CurrentWeatherModel();
		mockResponse.setCod(70);
		mockResponse.setName("Clear");

		when(restTemplate.getForObject(uri, CurrentWeatherModel.class)).thenReturn(mockResponse);

		ResponseEntity<ResponseObject> response = weatherService.getWeatherFromLatAndLon(lat, lon);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(mockResponse, response.getBody().getData());
		verify(restTemplate, times(1)).getForObject(uri, CurrentWeatherModel.class);
	}

	@Test
	void testGetWeatherFromLatAndLon_Failure() {
		double lat = 40.7128;
		double lon = -74.0060;

		String uri = host
				+ String.format("data/2.5/weather?lat=%f&lon=%f&appid=%s&units=%s", lat, lon, key, "imperial");

		when(restTemplate.getForObject(uri, CurrentWeatherModel.class))
				.thenThrow(new HttpClientErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "Error fetching weather"));

		ResponseEntity<ResponseObject> response = weatherService.getWeatherFromLatAndLon(lat, lon);

		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
		assertEquals(response.getBody().getData(), ("500 Error fetching weather"));
		verify(restTemplate, times(1)).getForObject(uri, CurrentWeatherModel.class);
	}

	@Test
	void testGetForecast_Success() {
		double lat = 40.7128;
		double lon = -74.0060;

		String uri = host + String.format("data/2.5/forecast?lat=%f&lon=%f&appid=%s&units=%s&cnt=%d", lat, lon, key,
				"imperial", 10);
		ForecastModel mockResponse = new ForecastModel();
		mockResponse.setMessage(1);
		mockResponse.setCod("test");

		when(restTemplate.getForObject(uri, ForecastModel.class)).thenReturn(mockResponse);

		ResponseEntity<ResponseObject> response = weatherService.getForecast(lat, lon);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertNotNull(response.getBody());
		assertEquals(mockResponse, response.getBody().getData());
		verify(restTemplate, times(1)).getForObject(uri, ForecastModel.class);
	}

	@Test
	void testGetForecast_Failure() {
		double lat = 40.7128;
		double lon = -74.0060;

		String uri = host + String.format("data/2.5/forecast?lat=%f&lon=%f&appid=%s&units=%s&cnt=%d", lat, lon, key,
				"imperial", 10);

		when(restTemplate.getForObject(uri, ForecastModel.class))
				.thenThrow(new HttpClientErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "Error fetching forecast"));

		ResponseEntity<ResponseObject> response = weatherService.getForecast(lat, lon);

		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
		assertEquals(response.getBody().getData(), ("500 Error fetching forecast"));
		verify(restTemplate, times(1)).getForObject(uri, ForecastModel.class);
	}

	@Test
	void testGetWeatherFromLatAndLonForEmail_success() {
		Double lat = 37.7749;
		Double lon = -122.4194;
		CurrentWeatherModel mockResponse = new CurrentWeatherModel();

		String expectedUri = host + String.format(
				"data/2.5/weather?lat=%f&lon=%f&appid=%s&units=%s", lat, lon, key, "imperial");

		when(restTemplate.getForObject(expectedUri, CurrentWeatherModel.class)).thenReturn(mockResponse);

		CurrentWeatherModel result = weatherService.getWeatherFromLatAndLonForEmail(lat, lon);

		assertNotNull(result);
		assertEquals(mockResponse, result);
	}
	@Test
void testGetWeatherFromLatAndLonForEmail_exceptionReturnsNull() {
    Double lat = 37.7749;
    Double lon = -122.4194;

    String expectedUri = host + String.format(
            "data/2.5/weather?lat=%f&lon=%f&appid=%s&units=%s", lat, lon, key, "imperial");

    when(restTemplate.getForObject(expectedUri, CurrentWeatherModel.class))
        .thenThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST));

    CurrentWeatherModel result = weatherService.getWeatherFromLatAndLonForEmail(lat, lon);

    assertNull(result);
}
@Test
void testGetWeatherFromName_success() {
    String cityName = "New York";
    LocationModel[] mockArray = { new LocationModel(), new LocationModel() };

    String expectedUri = host + String.format("geo/1.0/direct?q=%s&limit=10&appid=%s", cityName, key, "imperial");

    when(restTemplate.getForObject(expectedUri, LocationModel[].class)).thenReturn(mockArray);

    ResponseEntity<ResponseObject> response = weatherService.getWeatherFromName(cityName);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertTrue(response.getBody().getData() instanceof List);
    assertEquals(2, ((List<?>) response.getBody().getData()).size());
}
@Test
void testGetWeatherFromName_exceptionReturns500() {
    String cityName = "UnknownCity";

    String expectedUri = host + String.format("geo/1.0/direct?q=%s&limit=10&appid=%s", cityName, key, "imperial");

    when(restTemplate.getForObject(expectedUri, LocationModel[].class))
        .thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND, "City not found"));

    ResponseEntity<ResponseObject> response = weatherService.getWeatherFromName(cityName);

    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    assertTrue(response.getBody().toString().contains("City not found"));
}


}
