package com.perfume.allpouse.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.User;

public interface UserRepository extends JpaRepository<User, Long> {

    com.perfume.allpouse.data.entity.User getByUserName(String userName);

}
