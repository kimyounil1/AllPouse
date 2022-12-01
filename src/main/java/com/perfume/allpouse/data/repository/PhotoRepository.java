package com.perfume.allpouse.data.repository;

import com.perfume.allpouse.data.entity.Photo;
import com.perfume.allpouse.model.enums.BoardType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PhotoRepository extends JpaRepository<Photo, Long> {

    @Query("select p from Photo p " +
            "where p.boardType = :type " +
            "and p.boardId = :boardId")
    Photo findPhotoByBoardTypeAndBoardId(@Param("type") BoardType type, @Param("boardId") Long boardId);

    @Query("SELECT COUNT(p.id) > 0 " +
            "FROM Photo p " +
            "WHERE p.boardType =:type " +
            "AND p.boardId = :boardId")
    boolean exists(@Param("type") BoardType type, @Param("boardId")Long boardId);
}
