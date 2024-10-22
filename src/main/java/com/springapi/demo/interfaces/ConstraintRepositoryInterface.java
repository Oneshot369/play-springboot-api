package com.springapi.demo.interfaces;

import com.springapi.demo.model.entity.ConstraintEntity;
import com.springapi.demo.model.entity.UserLocationEntities;
import com.springapi.demo.model.weatherResponse.WeatherTypes;

import jakarta.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface ConstraintRepositoryInterface extends JpaRepository<ConstraintEntity, Integer>{
    
    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = "INSERT INTO `personal`.`constraints` (`location_id`, `condition`, `val`, `above_below`, `name` ) VALUES (?1, ?2, ?3, ?4, ?5)")
    int saveConstraintToLocation(int locationId, String string, String val, boolean greaterOrLessThan, String name);

    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = "UPDATE `personal`.`constraints` set `name` = ?1, `condition` = ?2, `val` = ?3, `above_below` = ?4 WHERE constraint_id = ?5")
    int updateConstraintById(String string, String weatherTypes, String string2, boolean b, int i);
}
