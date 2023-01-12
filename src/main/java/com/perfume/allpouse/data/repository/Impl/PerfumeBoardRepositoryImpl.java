package com.perfume.allpouse.data.repository.Impl;

import com.perfume.allpouse.data.entity.PerfumeBoard;
import com.perfume.allpouse.data.entity.QPerfumeBoard;
import com.perfume.allpouse.data.entity.QPhoto;
import com.perfume.allpouse.data.repository.custom.PerfumeBoardRepositoryCustom;
import com.perfume.allpouse.model.dto.PerfumeResponseDto;
import com.perfume.allpouse.model.dto.QPerfumeResponseDto;
import com.perfume.allpouse.model.dto.QSearchPerfumeDto;
import com.perfume.allpouse.model.dto.SearchPerfumeDto;
import com.perfume.allpouse.utils.PageUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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


    // 전체 향수 중 조회수 순으로 향수 가져와서 페이지네이션(page, size 설정 가능)
    @Override
    public Page<PerfumeResponseDto> getPerfumeByHitCnt(Pageable pageable) {

        List<PerfumeResponseDto> perfumes = from(perfume)
                .leftJoin(photo)
                .on(perfume.id.eq(photo.boardId).and(photo.boardType.eq(PERFUME)))
                .select(new QPerfumeResponseDto(
                        perfume.id,
                        perfume.subject,
                        perfume.brand.id,
                        perfume.brand.name,
                        photo.path
                ))
                .orderBy(perfume.hitCnt.desc())
                .fetch();

        Page<PerfumeResponseDto> perfumePages = PageUtils.makePageList(perfumes, pageable);

        return perfumePages;
    }


    // 브랜드id로 해당 브랜드 향수 조회수 순으로 size개 가져옴
    @Override
    public List<PerfumeResponseDto> getPerfumeByBrandId(Long brandId, int size) {

        if (size == 0) {
            List<PerfumeResponseDto> perfumes = from(perfume)
                    .leftJoin(photo)
                    .on(perfume.id.eq(photo.boardId).and(photo.boardType.eq(PERFUME)))
                    .where(perfume.brand.id.eq(brandId))
                    .select(new QPerfumeResponseDto(
                            perfume.id,
                            perfume.subject,
                            perfume.brand.id,
                            perfume.brand.name,
                            photo.path
                    ))
                    .orderBy(perfume.hitCnt.desc())
                    .fetch();

            return perfumes;

        } else {
            List<PerfumeResponseDto> perfumes = from(perfume)
                    .leftJoin(photo)
                    .on(perfume.id.eq(photo.boardId).and(photo.boardType.eq(PERFUME)))
                    .where(perfume.brand.id.eq(brandId))
                    .select(new QPerfumeResponseDto(
                            perfume.id,
                            perfume.subject,
                            perfume.brand.id,
                            perfume.brand.name,
                            photo.path
                    ))
                    .orderBy(perfume.hitCnt.desc())
                    .limit(size)
                    .fetch();

            return perfumes;
        }
    }

}
