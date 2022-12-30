package com.perfume.allpouse.data.repository.Impl;

import com.perfume.allpouse.data.entity.PerfumeBoard;
import com.perfume.allpouse.data.entity.QPerfumeBoard;
import com.perfume.allpouse.data.entity.QPhoto;
import com.perfume.allpouse.data.repository.custom.PerfumeBoardRepositoryCustom;
import com.perfume.allpouse.model.dto.QSearchPerfumeDto;
import com.perfume.allpouse.model.dto.SearchPerfumeDto;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;

import static com.perfume.allpouse.model.enums.BoardType.PERFUME;

public class PerfumeBoardRepositoryImpl extends QuerydslRepositorySupport implements PerfumeBoardRepositoryCustom {

    public PerfumeBoardRepositoryImpl() {
        super(PerfumeBoard.class);
    }

    QPerfumeBoard perfume = QPerfumeBoard.perfumeBoard;
    QPhoto photo = QPhoto.photo;



    // 기본검색
    // 조건 : 향수의 이름 혹은 향수가 속한 브랜드 이름에 키워드가 포함되는 경우
    @Override
    public List<SearchPerfumeDto> search(String keyword) {

        List<SearchPerfumeDto> perfumes = from(perfume)
                .leftJoin(photo)
                .on(perfume.id.eq(photo.boardId).and(photo.boardType.eq(PERFUME)))
                .where(perfume.subject.containsIgnoreCase(keyword).or(perfume.brand.name.containsIgnoreCase(keyword)))
                .select(new QSearchPerfumeDto(
                        perfume.id,
                        perfume.subject,
                        perfume.brand.id,
                        perfume.brand.name,
                        photo.path
                ))
                .orderBy(perfume.subject.asc())
                .fetch();

        return perfumes;
    }

}