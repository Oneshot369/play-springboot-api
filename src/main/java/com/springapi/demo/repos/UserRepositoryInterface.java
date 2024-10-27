package com.springapi.demo.repos;

import com.springapi.demo.model.entity.UserEntity;

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
    @Query(nativeQuery = true, value = "UPDATE `personal`.`users` set `last_login` = ?1 WHERE ID = ?2")
    void updateLastLoginTime(String time, Long id);
}
