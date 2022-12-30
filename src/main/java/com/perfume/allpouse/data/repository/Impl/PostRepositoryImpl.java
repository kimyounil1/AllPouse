package com.perfume.allpouse.data.repository.Impl;

import com.perfume.allpouse.data.entity.Post;
import com.perfume.allpouse.data.entity.QPhoto;
import com.perfume.allpouse.data.entity.QPost;
import com.perfume.allpouse.data.repository.custom.PostRepositoryCustom;
import com.perfume.allpouse.model.dto.QSearchPostDto;
import com.perfume.allpouse.model.dto.SearchPostDto;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;

import static com.perfume.allpouse.model.enums.BoardType.POST;

public class PostRepositoryImpl extends QuerydslRepositorySupport implements PostRepositoryCustom {

    public PostRepositoryImpl() {super(Post.class);}

    QPost post = QPost.post;
    QPhoto photo = QPhoto.photo;


    // 기본검색
    @Override
    public List<SearchPostDto> search(String keyword) {

        List<SearchPostDto> post = from(this.post)
                .leftJoin(photo)
                .on(this.post.id.eq(photo.boardId).and(photo.boardType.eq(POST)))
                .where(this.post.title.containsIgnoreCase(keyword).or(this.post.content.containsIgnoreCase(keyword)))
                .select(new QSearchPostDto(
                        this.post.id,
                        this.post.type.stringValue(),
                        this.post.title,
                        this.post.content,
                        this.post.hitCnt,
                        this.post.recommendCnt,
                        this.post.user.id,
                        this.post.user.userName))
                .orderBy(this.post.title.asc())
                .limit(10)
                .fetch();

        return post;
    }

}
