package com.perfume.allpouse.service.impl;

import com.perfume.allpouse.service.PhotoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class PhotoServiceImpl implements PhotoService {

    private final S3ServiceImpl s3ServiceImpl;

    public PhotoServiceImpl(S3ServiceImpl s3ServiceImpl) {
        this.s3ServiceImpl = s3ServiceImpl;
    }

    @Override
    public List<String> save(List<MultipartFile> multipartFileList) throws IOException {
        
        List<String> result = s3ServiceImpl.upload(multipartFileList);
        for ( String path : result) {

        }
        
        return result;
    }


}
