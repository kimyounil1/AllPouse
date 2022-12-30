package com.perfume.allpouse.data.repository.Impl;

import com.perfume.allpouse.data.entity.QPhoto;
import com.perfume.allpouse.data.entity.QReviewBoard;
import com.perfume.allpouse.data.entity.ReviewBoard;
import com.perfume.allpouse.data.repository.custom.ReviewBoardRepositoryCustom;
import com.perfume.allpouse.model.dto.QSearchReviewDto;
import com.perfume.allpouse.model.dto.SearchReviewDto;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;

import static com.perfume.allpouse.model.enums.BoardType.REVIEW;

public class ReviewBoardRepositoryImpl extends QuerydslRepositorySupport implements ReviewBoardRepositoryCustom {

    public ReviewBoardRepositoryImpl() {
        super(ReviewBoard.class);
    }

    QReviewBoard review = QReviewBoard.reviewBoard;
    QPhoto photo = QPhoto.photo;


    // 기본검색
    @Override
    public List<SearchReviewDto> search(String keyword) {
        List<SearchReviewDto> reviews = from(review)
                .leftJoin(photo)
                .on(review.id.eq(photo.boardId).and(photo.boardType.eq(REVIEW)))
                .where(review.subject.containsIgnoreCase(keyword).or(review.content.containsIgnoreCase(keyword)))
                .select(new QSearchReviewDto(
                        review.id,
                        review.subject,
                        review.content,
                        review.user.id,
                        review.user.userName,
                        review.recommendCnt,
                        review.comments.size(),
                        photo.path))
                .orderBy(review.subject.asc())
                .limit(10)
                .fetch();

        return reviews;
    }
}