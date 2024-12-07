package com.springapi.demo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.springapi.demo.config.JwtTokenProvider;
import com.springapi.demo.model.dataObject.ConstraintModel;
import com.springapi.demo.model.dataObject.LoginAttemptModel;
import com.springapi.demo.model.dataObject.UserLocationModel;
import com.springapi.demo.model.dataObject.UserModel;
import com.springapi.demo.model.entity.UserEntity;
import com.springapi.demo.model.weatherResponse.WeatherTypes;
import com.springapi.demo.repos.ConstraintRepositoryInterface;
import com.springapi.demo.repos.LocationRepositoryInterface;
import com.springapi.demo.repos.UserRepositoryInterface;
import com.springapi.demo.services.UserService;
import com.springapi.demo.util.ResponseObject;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class UserServiceTests {

	@InjectMocks
	private UserService userService;

	@Mock
	private UserRepositoryInterface userRepo;

	@Mock
	private LocationRepositoryInterface locationRepo;

	@Mock
	private ConstraintRepositoryInterface constraintRepo;

	@Mock
	private AuthenticationManager authenticationManager;

	@Mock
	private JwtTokenProvider jwtTokenProvider;

	@Mock
	private PasswordEncoder passwordEncoder;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	// Helper mock user
	private User mockAdminUser() {
		User adminUser = new User("admin", "pw", List.of(new SimpleGrantedAuthority("ADMIN")));
		return adminUser;
	}

	@Test
	void testGetUserById_ValidId() {
		User mockAdmin = mockAdminUser();
		UserEntity mockUserEntity = new UserEntity();
		mockUserEntity.setId(1L);
		mockUserEntity.setUsername("testUser");

		when(userRepo.getById(1L)).thenReturn(mockUserEntity);

		ResponseEntity<ResponseObject> response = userService.getUserById(1L, mockAdmin);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals("testUser", ((UserModel) response.getBody().getData()).getUsername());
	}

	@Test
	void testGetUserById_NoAuthority() {
		User nonAdmin = new User("user", "pw", List.of());

		ResponseEntity<ResponseObject> response = userService.getUserById(1L, nonAdmin);

		assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
		assertEquals("This is a admin feature", response.getBody().getData().toString());
	}

	@Test
	void testGetUserByName_ValidUsername() {
		User mockAdmin = mockAdminUser();
		UserEntity mockUserEntity = new UserEntity();
		mockUserEntity.setUsername("testUser");

		when(userRepo.findByUsername("testUser")).thenReturn(List.of(mockUserEntity));

		ResponseEntity<ResponseObject> response = userService.getUserByName("testUser", mockAdmin);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals("testUser", ((UserModel) response.getBody().getData()).getUsername());
	}

	@Test
	void testGetUserByName_UserNotFound() {
		User mockAdmin = mockAdminUser();
		when(userRepo.findByUsername("testUser")).thenReturn(Collections.emptyList());

		ResponseEntity<ResponseObject> response = userService.getUserByName("testUser", mockAdmin);

		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
		assertEquals("User not found by: testUser", response.getBody().getData());
	}

	@Test
	void testGetUserByName_NoAuthority() {
		User nonAdmin = new User("user", "pw", List.of());
		;

		ResponseEntity<ResponseObject> response = userService.getUserByName("testUser", nonAdmin);

		assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
		assertEquals("This is a admin feature", response.getBody().getData());
	}

	@Test
	void testGetAllUsers_Valid() {
		User mockAdmin = mockAdminUser();
		UserEntity mockUserEntity = new UserEntity();
		mockUserEntity.setUsername("user1");

		when(userRepo.findAll()).thenReturn(List.of(mockUserEntity));

		ResponseEntity<ResponseObject> response = userService.getAllUsers(mockAdmin);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertFalse(((List<UserModel>) response.getBody().getData()).isEmpty());
	}

	@Test
	void testGetAllUsers_EmptyList() {
		User mockAdmin = mockAdminUser();
		when(userRepo.findAll()).thenReturn(Collections.emptyList());

		ResponseEntity<ResponseObject> response = userService.getAllUsers(mockAdmin);

		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
		assertEquals("No users found", response.getBody().getData());
	}

	@Test
	void testGetAllUsers_NoAuthority() {
		User nonAdmin = new User("user", "pw", List.of());

		ResponseEntity<ResponseObject> response = userService.getAllUsers(nonAdmin);

		assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
		assertEquals("This is a admin feature", response.getBody().getData());
	}

	@Test
void testSaveUser_ValidUserModel() {
    UserModel mockModel = new UserModel();
    mockModel.setUsername("testUser");
    mockModel.setPassword("password");
    
    UserEntity savedEntity = new UserEntity();
    savedEntity.setId(1L);

    when(passwordEncoder.encode(mockModel.getPassword())).thenReturn("encodedPassword");
    when(userRepo.save(any(UserEntity.class))).thenReturn(savedEntity);

    ResponseEntity<ResponseObject> response = userService.saveUser(mockModel);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(response.getBody().getData(), ("User saved with Id: 1"));
    verify(passwordEncoder, times(1)).encode("password");
    verify(userRepo, times(1)).save(any(UserEntity.class));
}

@Test
void testSaveLocationToUser_ValidLocation() {
    UserLocationModel mockLocation = new UserLocationModel();
    mockLocation.setName("Home");
    mockLocation.setLat(40.7128);
    mockLocation.setLon(-74.0060);

    //doNothing().when(locationRepo).saveLocationToUser(mockLocation.getLat(), mockLocation.getLon(), mockLocation.getName(), 1);

    ResponseEntity<ResponseObject> response = userService.saveLocationToUser(mockLocation, 1);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(response.getBody().getData(), ("Location was saved for user: 1"));
    verify(locationRepo, times(1)).saveLocationToUser(mockLocation.getLat(), mockLocation.getLon(), mockLocation.getName(), 1);
}

@Test
void testUpdateLocation_ValidLocation() {
    UserLocationModel mockLocation = new UserLocationModel();
    mockLocation.setId(1);
    mockLocation.setName("Work");
    mockLocation.setLat(37.7749);
    mockLocation.setLon(-122.4194);

    //doNothing().when(locationRepo).updateLocationById(mockLocation.getName(), mockLocation.getLat(), mockLocation.getLon(), mockLocation.getId());

    ResponseEntity<ResponseObject> response = userService.updateLocation(mockLocation);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(response.getBody().getData(), ("Location was Updated."));
    verify(locationRepo, times(1)).updateLocationById(mockLocation.getName(), mockLocation.getLat(), mockLocation.getLon(), mockLocation.getId());
}
@Test
void testDeleteLocation_ValidId() {
    int locationId = 1;

    doNothing().when(locationRepo).deleteLocationById(locationId);

    ResponseEntity<ResponseObject> response = userService.deleteLocation(locationId);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(response.getBody().getData(), ("Location was deleted."));
    verify(locationRepo, times(1)).deleteLocationById(locationId);
}
@Test
void testSaveConstraintToUser_ValidConstraint() {
    ConstraintModel mockConstraint = new ConstraintModel();
    mockConstraint.setName("Temperature");
    mockConstraint.setCondition(WeatherTypes.TEMP);
    mockConstraint.setVal("25.0");
    mockConstraint.setGreaterThan(false);

    //doNothing().when(constraintRepo).saveConstraintToLocation(1, "LESS_THAN", "25.0", false, "Temperature");

    ResponseEntity<ResponseObject> response = userService.saveConstraintToUser(mockConstraint, 1);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(response.getBody().getData(), ("Constraint was saved for location: 1"));
}

@Test
void testUpdateConstraint_ValidConstraint() {
    ConstraintModel mockConstraint = new ConstraintModel();
    mockConstraint.setId(1);
    mockConstraint.setName("Temperature");
    mockConstraint.setCondition(WeatherTypes.FEELS_LIKE);
    mockConstraint.setVal("30.0");
    mockConstraint.setGreaterThan(true);

    //doNothing().when(constraintRepo).updateConstraintById("Temperature", WeatherTypes.HUMIDITY.toString(), "30.0", true, 1);

    ResponseEntity<ResponseObject> response = userService.updateConstraint(mockConstraint);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(response.getBody().getData(), "Constraint was updated.");
}

@Test
void testDeleteConstraint_ValidId() {
    int constraintId = 1;

    doNothing().when(constraintRepo).deleteById(constraintId);

    ResponseEntity<ResponseObject> response = userService.deleteConstraint(constraintId);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(response.getBody().getData(),"Constraint was deleted.");
    verify(constraintRepo, times(1)).deleteById(constraintId);
}

@Test
void testAttemptLogin_Successful() {
    LoginAttemptModel mockLogin = new LoginAttemptModel();
    mockLogin.setUsername("testUser");
    mockLogin.setPassword("password");

    Authentication mockAuth = mock(Authentication.class);

    when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(mockAuth);
    when(jwtTokenProvider.generateToken(mockAuth)).thenReturn("mockJwtToken");

    ResponseEntity<ResponseObject> response = userService.attemptLogin(mockLogin);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals("mockJwtToken", response.getBody().getData());
    verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
    verify(jwtTokenProvider, times(1)).generateToken(mockAuth);
}

@Test
void testAttemptLogin_Failure() {
    LoginAttemptModel mockLogin = new LoginAttemptModel();
    mockLogin.setUsername("testUser");
    mockLogin.setPassword("wrongPassword");

    when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
        .thenThrow(new BadCredentialsException("Invalid credentials"));

    ResponseEntity<ResponseObject> response = userService.attemptLogin(mockLogin);

    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertEquals(response.getBody().getData(),"Invalid credentials");
    verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
}



}
