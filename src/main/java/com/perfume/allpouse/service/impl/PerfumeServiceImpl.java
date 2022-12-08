package com.perfume.allpouse.service.impl;


import com.perfume.allpouse.data.entity.Brand;
import com.perfume.allpouse.data.entity.PerfumeBoard;
import com.perfume.allpouse.data.entity.QPerfumeBoard;
import com.perfume.allpouse.data.entity.QPhoto;
import com.perfume.allpouse.data.repository.BrandRepository;
import com.perfume.allpouse.data.repository.PerfumeBoardRepository;
import com.perfume.allpouse.exception.CustomException;
import com.perfume.allpouse.exception.ExceptionEnum;
import com.perfume.allpouse.model.dto.PerfumeInfoDto;
import com.perfume.allpouse.model.dto.QPerfumeInfoDto;
import com.perfume.allpouse.model.dto.SavePerfumeDto;
import com.perfume.allpouse.service.PerfumeService;
import com.perfume.allpouse.service.PhotoService;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static com.perfume.allpouse.exception.ExceptionEnum.*;
import static com.perfume.allpouse.model.enums.BoardType.PERFUME;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PerfumeServiceImpl implements PerfumeService {

    private final Logger LOGGER = LoggerFactory.getLogger(PerfumeServiceImpl.class);

    private final PerfumeBoardRepository perfumeRepository;

    private final BrandRepository brandRepository;

    private final PhotoService photoService;

    private final JPAQueryFactory queryFactory;

    QPerfumeBoard perfumeBoard = new QPerfumeBoard("perfumeBoard");

    QPhoto photo = new QPhoto("photo");


    // 향수 저장 - 사진 있는 경우
    @Override
    @Transactional
    public Long save(SavePerfumeDto savePerfumeDto, List<MultipartFile> photos) throws IOException {

        Long perfumeId = savePerfumeDto.getId();

        // 아직 저장된 적 없음 -> save
        if (perfumeId == null) {
            PerfumeBoard savedPerfume = perfumeRepository.save(toEntity(savePerfumeDto));
            Long savedId = savedPerfume.getId();
            photoService.save(photos, PERFUME, savedId);

            return savedId;
        } else {
            photoService.delete(PERFUME, perfumeId);
            photoService.save(photos, PERFUME, perfumeId);
            update(savePerfumeDto);

            return perfumeId;
        }
    }

    // 향수 저장 - 사진 없는 경우
    @Override
    @Transactional
    public Long save(SavePerfumeDto savePerfumeDto) {

        Long perfumeId = savePerfumeDto.getId();

        // 아직 저장된 적 없음 -> save
        if (perfumeId == null) {
            PerfumeBoard savedPerfume = perfumeRepository.save(toEntity(savePerfumeDto));
            return savedPerfume.getId();
        } else {
            photoService.delete(PERFUME, perfumeId);
            Long savedPerfumeId = update(savePerfumeDto);
            return savedPerfumeId;
        }
    }


    // 향수 수정
    @Override
    @Transactional
    public Long update(SavePerfumeDto savePerfumeDto) {

        Long perfumeId = savePerfumeDto.getId();

        Optional<PerfumeBoard> perfume = perfumeRepository.findById(perfumeId);

        if (perfume.isPresent()) {
            PerfumeBoard perfumeBoard = perfume.get();
            perfumeBoard.changePerfume(savePerfumeDto);

            return perfumeId;
        } else {throw new CustomException(INVALID_PARAMETER);}
    }


    // 향수 삭제
    @Override
    @Transactional
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


    // 향수 Id로 PerfumeInfoDto 받는 메소드
    @Override
    public PerfumeInfoDto getPerfumeInfo(Long id) {
        PerfumeInfoDto perfumeInfoDto = queryFactory
                .select(new QPerfumeInfoDto(perfumeBoard.id, perfumeBoard.subject, perfumeBoard.brand.name, perfumeBoard.price, perfumeBoard.content, perfumeBoard.hitCnt, photo.path))
                .from(perfumeBoard)
                .leftJoin(photo)
                .on(perfumeBoard.id.eq(photo.boardId).and(photo.boardType.eq(PERFUME)))
                .where(perfumeBoard.id.eq(id))
                .fetchOne();

        return perfumeInfoDto;
    }


    // 향수 단건 조회(with id)
    @Override
    public PerfumeBoard findById(Long id) {
        Optional<PerfumeBoard> findPerfume = perfumeRepository.findById(id);

        if (findPerfume.isPresent()) {
            return findPerfume.get();
        } else {
            throw new IllegalStateException("향수를 찾을 수 없습니다.");
        }    }


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


    // 조회수 추가
    @Override
    @Transactional
    public void addHitCnt(Long id) {

        Optional<PerfumeBoard> perfume = perfumeRepository.findById(id);

        if (perfume.isEmpty()) {
            throw new CustomException(INVALID_PARAMETER);
        } else {
            perfumeRepository.updateHitCnt(id);
        }
    }


    // Dto -> PerfumeBoard
    private PerfumeBoard toEntity(SavePerfumeDto perfumeDto) {

        PerfumeBoard perfume = PerfumeBoard.builder()
                .id(perfumeDto.getId())
                .subject(perfumeDto.getSubject())
                .content(perfumeDto.getContent())
                .price(perfumeDto.getPrice())
                .build();

        Brand brand = brandRepository.findById(perfumeDto.getBrandId()).get();
        perfume.addBrand(brand);

        return perfume;
    }
}
