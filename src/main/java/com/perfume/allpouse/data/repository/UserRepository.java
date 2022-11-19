package com.perfume.allpouse.data.repository;

import com.perfume.allpouse.data.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    User getByUserName(String userName);

    User findBySocialId(String socialId);

}
