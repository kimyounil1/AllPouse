package com.perfume.allpouse.repository;

import com.perfume.allpouse.data.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


/**
 * <extends JPARepository>
 *     - save() -> insert, update
 *     - findOne(id)
 *     - findAll()
 *     - count()
 *     - delete()
 *
 */
public interface UserRepository extends JpaRepository<User, Long> {
}
