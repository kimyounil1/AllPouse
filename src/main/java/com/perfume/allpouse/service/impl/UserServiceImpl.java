package com.perfume.allpouse.service.impl;

import com.perfume.allpouse.data.entity.User;
import com.perfume.allpouse.data.repository.UserRepository;
import com.perfume.allpouse.exception.CustomException;
import com.perfume.allpouse.model.dto.ReviewResponseDto;
import com.perfume.allpouse.model.dto.UserInfoDto;
import com.perfume.allpouse.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

import static com.perfume.allpouse.exception.ExceptionEnum.INVALID_PARAMETER;


@Transactional
@Service
public class UserServiceImpl implements UserService {

    private final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserRepository userRepository;

    private final EntityManager em;

    public UserServiceImpl(UserRepository userRepository, EntityManager em) {
        this.userRepository = userRepository;
        this.em = em;
    }



    public User loadUserById(long id) {
        LOGGER.info("[loadUserById] 함수 실행. id : {}",id);
        return userRepository.getReferenceById(id);
    }

    @Override
    public UserInfoDto getUserInfoDtoById(Long id) {

        try{
            UserInfoDto dtoList = em.createQuery(
                            "select new com.perfume.allpouse.model.dto.UserInfoDto(r.id, r.userName, r.age, r.gender, p.path )"
                                    + " from User r"
                                    + " left join Photo p"
                                    + " on r.id = p.boardId"
                                    + " and p.boardType = 'USER'"
                                    + " where r.id = :id ", UserInfoDto.class)
                    .setParameter("id", id).getSingleResult();

            return dtoList;

        } catch (Exception e) {throw new CustomException(INVALID_PARAMETER);}
    }

    // id로 단건 조회
    @Override
    public User findOne(Long id) {
        Optional<User> user = userRepository.findById(id);

        if (user.isPresent()) {
            return user.get();
        } else {
            throw new CustomException(INVALID_PARAMETER);
        }
    }

}
