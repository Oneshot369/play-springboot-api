package com.springapi.demo;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.HttpRequestWithBody;
import com.springapi.demo.model.dataObject.ConstraintModel;
import com.springapi.demo.model.dataObject.UserLocationModel;
import com.springapi.demo.model.dataObject.UserModel;
import com.springapi.demo.model.weatherResponse.CurrentWeatherModel;
import com.springapi.demo.model.weatherResponse.WeatherTypes;
import com.springapi.demo.model.weatherResponse.submodels.MainWeatherModel;
import com.springapi.demo.services.EmailService;
import com.springapi.demo.services.UserService;
import com.springapi.demo.services.WeatherService;
import com.springapi.demo.util.JsonFormatter;
import com.springapi.demo.util.ResponseObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.ArgumentMatchers.eq;
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
    public void testSendEmailsToUsers_triggersEmailForTempConstraint() {
        // Create mocks for User, Location, Constraint and CurrentWeather.
        UserModel mockUser = mock(UserModel.class);
        UserLocationModel mockLocation = mock(UserLocationModel.class);
        ConstraintModel mockConstraint = mock(ConstraintModel.class);
        CurrentWeatherModel mockWeather = mock(CurrentWeatherModel.class);

        when(mockWeather.getMain()).thenReturn(new MainWeatherModel());

        // Set up the user, location and constraint
        when(mockUser.getLocations()).thenReturn(Arrays.asList(mockLocation));
        when(mockUser.getUsername()).thenReturn("test@example.com");
        when(mockUser.getId()).thenReturn(1L);

        when(mockLocation.getConstraints()).thenReturn(Arrays.asList(mockConstraint));
        when(mockLocation.getName()).thenReturn("Test Location");
        when(mockLocation.getLat()).thenReturn(10.0);
        when(mockLocation.getLon()).thenReturn(20.0);
        when(mockLocation.getId()).thenReturn(100);

        // Assume ConstraintModel has an enum Condition with value TEMP.
        when(mockConstraint.getCondition()).thenReturn(WeatherTypes.TEMP);
        when(mockConstraint.isGreaterThan()).thenReturn(true);
        when(mockConstraint.getVal()).thenReturn("30");
        when(mockConstraint.getName()).thenReturn("Temp Alert");
        when(mockConstraint.getId()).thenReturn(1000);

        // Stub weatherService to return our dummy weather.
        when(weatherService.getWeatherFromLatAndLonForEmail(10.0, 20.0)).thenReturn(mockWeather);
        // Stub userService to return a list with our user.
        when(userService.getAllUsersForEmail()).thenReturn(Arrays.asList(mockUser));

        // Spy on the EmailService to intercept calls to sendEmail.
        EmailService emailServiceSpy = spy(emailService);
        doReturn(ResponseEntity.ok(new ResponseObject(org.springframework.http.HttpStatus.OK, "sent email")))
                .when(emailServiceSpy).sendEmail(anyString(), anyString(), anyString());

        // Execute the method under test.
        emailServiceSpy.sendEmailsToUsers();

        // Verify that sendEmail was called for the TEMP constraint.
        verify(emailServiceSpy, times(1))
                .sendEmail(ArgumentMatchers.argThat(msg -> msg.contains("The temperature in Test Location is currently")),
                           eq("test@example.com"),
                           ArgumentMatchers.argThat(sub -> sub.contains("Temp Alert")));
    }
}
