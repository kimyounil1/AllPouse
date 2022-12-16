package com.perfume.allpouse.service;

import com.perfume.allpouse.data.entity.BoardLog;

public interface BoardLogService {

    void save(BoardLog boardLog);

    BoardLog setSuccessLog(String type, String action, String detail, Long boardId, Long userId);
}
