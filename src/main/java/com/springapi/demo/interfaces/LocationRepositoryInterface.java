package com.springapi.demo.interfaces;

import com.springapi.demo.model.entity.UserLocationEntities;
import jakarta.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface LocationRepositoryInterface extends JpaRepository<UserLocationEntities, Integer>{
    
    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = "INSERT INTO `personal`.`locations` (`user_id`, `lat`, `lon`, `name`) VALUES (?4, ?1, ?2, ?3)")
    int saveLocationToUser(double lat, double lon, String name, int userId);
    
    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = "UPDATE `personal`.`locations` set `name` = ?1, `lat` = ?2, `lon` = ?3 WHERE id = ?4")
    int updateLocationById(String string, double d, double e, int i);

    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = "DELETE FROM `personal`.`locations` where id = ?1 ")
    void deleteLocationById(int locationId);


}
