package com.springapi.demo.interfaces;

import com.springapi.demo.model.dataObject.UserLocationModel;
import com.springapi.demo.model.entity.UserEntity;
import com.springapi.demo.model.entity.UserLocationEntities;
import com.springapi.demo.model.weatherResponse.WeatherTypes;

import jakarta.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface UserRepositoryInterface extends JpaRepository<UserEntity, Long>{
    
    UserEntity getById(Long id);

    List<UserEntity> findByUsername(String userName);


    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = "INSERT INTO `personal`.`locations` (`user_id`, `lat`, `lon`, `name`) VALUES (?4, ?1, ?2, ?3)")
    int saveLocationToUser(double lat, double lon, String name, int userId);

    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = "INSERT INTO `personal`.`constraints` (`location_id`, `condition`, `val`, `above_below`, `name` ) VALUES (?1, ?2, ?3, ?4, ?5)")
    int saveConstraintToLocation(int locationId, String string, String val, boolean greaterOrLessThan, String name);
}
