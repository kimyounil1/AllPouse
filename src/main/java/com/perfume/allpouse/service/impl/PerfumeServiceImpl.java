package com.perfume.allpouse.service.impl;


import com.perfume.allpouse.controller.PerfumeController;
import com.perfume.allpouse.data.entity.Brand;
import com.perfume.allpouse.data.entity.PerfumeBoard;
import com.perfume.allpouse.data.entity.QPerfumeBoard;
import com.perfume.allpouse.data.entity.QPhoto;
import com.perfume.allpouse.data.repository.BrandRepository;
import com.perfume.allpouse.data.repository.PerfumeBoardRepository;
import com.perfume.allpouse.model.dto.PerfumeInfoDto;
import com.perfume.allpouse.model.dto.QPerfumeInfoDto;
import com.perfume.allpouse.model.enums.BoardType;
import com.perfume.allpouse.service.PerfumeService;
import com.perfume.allpouse.model.dto.SavePerfumeDto;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PerfumeServiceImpl implements PerfumeService {

    private final Logger LOGGER = LoggerFactory.getLogger(PerfumeServiceImpl.class);

    private final PerfumeBoardRepository perfumeRepository;

    private final BrandRepository brandRepository;

    private final JPAQueryFactory queryFactory;

    QPerfumeBoard perfumeBoard = new QPerfumeBoard("perfumeBoard");

    QPhoto photo = new QPhoto("photo");



    // 향수 저장
    @Transactional
    @Override
    public Long save(SavePerfumeDto savePerfumeDto) {

        PerfumeBoard perfumeBoard = toEntity(savePerfumeDto);
        PerfumeBoard savedPerfume = perfumeRepository.save(perfumeBoard);

        return savedPerfume.getId();
    }


    // 향수 수정
    @Transactional
    @Override
    public Long update(SavePerfumeDto savePerfumeDto) {

        PerfumeBoard perfume = perfumeRepository.findById(savePerfumeDto.getId()).get();
        perfume.changePerfume(savePerfumeDto);

        return perfume.getId();
    }


    // 향수 삭제
    @Transactional
    @Override
    public void delete(Long id) {

        Optional<PerfumeBoard> perfume = perfumeRepository.findById(id);

        if (perfume.isPresent()) {
            perfumeRepository.deleteById(id);
        } else {
            throw new IllegalStateException("삭제할 향수가 없습니다.");
        }
    }


    // 전체 향수 조회
    @Override
    public List<PerfumeBoard> findAll() {return perfumeRepository.findAll();}


    // 향수 Id로 PerfumeDto 받는 메소드
    @Override
    public PerfumeInfoDto getPerfumeInfo(Long id) {
        PerfumeInfoDto perfumeInfoDto = queryFactory
                .select(new QPerfumeInfoDto(perfumeBoard.id, perfumeBoard.subject, perfumeBoard.brand.name, perfumeBoard.price, perfumeBoard.content, perfumeBoard.hitCnt, photo.path))
                .from(perfumeBoard)
                .leftJoin(photo)
                .on(perfumeBoard.id.eq(photo.boardId).and(photo.boardType.eq(BoardType.PERFUME)))
                .where(perfumeBoard.id.eq(id))
                .fetchOne();

        return perfumeInfoDto;
    }


    // 향수 단건 조회(with id)
    @Override
    public PerfumeBoard findOne(Long id) {
        Optional<PerfumeBoard> findPerfume = perfumeRepository.findById(id);

        if (findPerfume.isPresent()) {
            return findPerfume.get();
        } else {
            throw new IllegalStateException("향수를 찾을 수 없습니다.");
        }
    }


    // 향수 조회(파라미터 Brand_id로 조회)
    // 기본정렬 : 이름(subject) 오름차순
    @Override
    public List<PerfumeBoard> findByBrandId(Long id) {

        List<PerfumeBoard> perfumes = perfumeRepository.findByBrandId(id);

        if (perfumes.isEmpty()) {
            throw new IllegalStateException("검색된 향수가 없습니다.");
        } else {
            return perfumes;
        }
    }


    // Dto -> PerfumeBoard
    private PerfumeBoard toEntity(SavePerfumeDto perfumeDto) {

        Brand brand = brandRepository.findById(perfumeDto.getBrandId()).get();

        PerfumeBoard perfume = PerfumeBoard.builder()
                .id(perfumeDto.getId())
                .subject(perfumeDto.getSubject())
                .content(perfumeDto.getContent())
                .price(perfumeDto.getPrice())
                .build();

        perfume.addBrand(brand);

        return perfume;
    }
}
