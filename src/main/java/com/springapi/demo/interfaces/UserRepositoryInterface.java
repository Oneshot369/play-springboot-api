package com.springapi.demo.interfaces;

import com.springapi.demo.model.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface UserRepositoryInterface extends JpaRepository<UserEntity, Long>{
    
    UserEntity getById(Long id);

    List<UserEntity> findByUsername(String userName);
}
