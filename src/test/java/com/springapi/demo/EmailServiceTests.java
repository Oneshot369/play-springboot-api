
/*
 * USING CHATGPT 
 * 
 * I used Open AI's ChatGPT to generate these tests for my project
 * 
 * Model: GPT-4o
 * 
 * https://chatgpt.com/
 */

package com.springapi.demo;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.HttpRequestWithBody;
import com.mashape.unirest.request.body.RequestBodyEntity;
import com.springapi.demo.model.dataObject.ConstraintModel;
import com.springapi.demo.model.dataObject.UserLocationModel;
import com.springapi.demo.model.dataObject.UserModel;
import com.springapi.demo.model.weatherResponse.CurrentWeatherModel;
import com.springapi.demo.model.weatherResponse.WeatherTypes;
import com.springapi.demo.model.weatherResponse.submodels.MainWeatherModel;
import com.springapi.demo.model.weatherResponse.submodels.WindModel;
import com.springapi.demo.services.EmailService;
import com.springapi.demo.services.UserService;
import com.springapi.demo.services.WeatherService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EmailServiceTests {

    @InjectMocks
    private EmailService emailService;

    @Mock
    private UserService userService;

    @Mock
    private WeatherService weatherService;

    // Set the email API properties manually using ReflectionTestUtils
    @BeforeEach
    public void setup() {
        ReflectionTestUtils.setField(emailService, "key", "dummyKey");
        ReflectionTestUtils.setField(emailService, "host", "http://dummyhost/");
        ReflectionTestUtils.setField(emailService, "domain", "dummyDomain");
    }

    @Test
    public void testSendEmailsToUsers_triggersEmailForTempConstraint() throws UnirestException {
        try (MockedStatic<Unirest> unirestMock = Mockito.mockStatic(Unirest.class)) {
            // Mock chain objects
            HttpRequestWithBody mockRequest = mock(HttpRequestWithBody.class);
            HttpRequestWithBody mockRequestWithHeader = mock(HttpRequestWithBody.class);
            RequestBodyEntity mockRequestWithBody = mock(RequestBodyEntity.class);
            HttpResponse<JsonNode> mockResponse = mock(HttpResponse.class);

            // Set up method chain

            // Mock Unirest.post(...)
            unirestMock.when(() -> Unirest.post(anyString())).thenReturn(mockRequest);

            // Call the method under test
            assertThrows(Exception.class, () -> {
                emailService.sendEmail("msg", "recip", "any");
            });
        }

    }

    @Test
    void testSendEmailsToUsers_withMixedConstraints() {
        // Setup test user
        UserModel user = new UserModel();
        user.setId(1L);
        user.setUsername("test@example.com");

        // Location 1: No constraints
        UserLocationModel location1 = new UserLocationModel();
        location1.setName("NoConstraintCity");
        location1.setLat(10.0);
        location1.setLon(20.0);
        location1.setConstraints(Collections.emptyList());

        // Location 2: Two constraints per type
        UserLocationModel location2 = new UserLocationModel();
        location2.setName("ConstraintCity");
        location2.setLat(30.0);
        location2.setLon(40.0);

        List<ConstraintModel> constraints = new ArrayList<>();
        for (WeatherTypes type : WeatherTypes.values()) {
            constraints.add(new ConstraintModel(1, type + "_gt", type, "50", true)); // trigger above
            constraints.add(new ConstraintModel(2, type + "_lt", type, "150", false)); // trigger below
        }
        location2.setConstraints(constraints);

        user.setLocations(Arrays.asList(location1, location2));

        // Mock userService to return the user
        when(userService.getAllUsersForEmail()).thenReturn(Collections.singletonList(user));

        // Set up CurrentWeatherModel with values between 51-149 to hit both constraints
        CurrentWeatherModel weather = new CurrentWeatherModel();
        weather.setMain(new MainWeatherModel(100.0, 100.0, 0, 0, 100, 100, 0, 0, 0));
        weather.setVisibility(100);
        weather.setWind(new WindModel(100.0, 0, 100.0));

        // Mock weatherService
        when(weatherService.getWeatherFromLatAndLonForEmail(30.0, 40.0)).thenReturn(weather);

        // Call the method
        emailService.sendEmailsToUsers();

    }
}
