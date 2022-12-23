package com.perfume.allpouse.service.impl;

import com.perfume.allpouse.data.entity.BoardLog;
import com.perfume.allpouse.data.entity.PerfumeBoard;
import com.perfume.allpouse.data.entity.ReviewBoard;
import com.perfume.allpouse.data.entity.User;
import com.perfume.allpouse.data.repository.BoradLogRepository;
import com.perfume.allpouse.data.repository.UserRepository;
import com.perfume.allpouse.model.dto.SaveReviewDto;
import com.perfume.allpouse.service.BoardLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
@Service
@Transactional
public class BoardLogServiceImpl implements BoardLogService {

    private final BoradLogRepository boradLogRepository;

    private final UserRepository userRepository;

    public BoardLogServiceImpl(BoradLogRepository boradLogRepository, UserRepository userRepository) {
        this.boradLogRepository = boradLogRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void save(BoardLog boardLog) {
        boradLogRepository.save(boardLog);
    }

    @Override
    public BoardLog setSuccessLog(String type, String action, String detail, Long boardId, Long userId) {
        Optional<User> user = userRepository.findById(userId);
        BoardLog boardLog = BoardLog.builder()
                .status(true)
                .type(type)
                .action(action)
                .boardId(boardId)
                .user(user.get())
                .detailLog(detail).build();
        return boardLog;
    }
}
