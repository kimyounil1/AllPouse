package com.perfume.allpouse.service.impl;

import com.perfume.allpouse.model.reponse.CommonResponse;
import com.perfume.allpouse.model.reponse.ListResponse;
import com.perfume.allpouse.model.reponse.SingleResponse;
import com.perfume.allpouse.service.ResponseService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ResponseServiceImpl implements ResponseService {


    @Override
    public <T> SingleResponse<T> getSingleResponse(T data) {
        SingleResponse singleResponse = new SingleResponse();
        singleResponse.setData(data);
        setSuccessResponse(singleResponse);
        return singleResponse;
    }

    @Override
    public <T> ListResponse<T> getListResponse(List<T> dataList) {
        ListResponse listResponse = new ListResponse();
        listResponse.setDataList(dataList);
        listResponse.setCount(dataList.size());
        setSuccessResponse(listResponse);
        return listResponse;
    }

    @Override
    public void setSuccessResponse(CommonResponse response) {
        response.setCode(0);
        response.setSuccess(true);
        response.setMsg("SUCCESS");
    }

    @Override
    public void setFalseResponse(CommonResponse response) {
        response.setCode(-1);
        response.setSuccess(false);
        response.setMsg("FALSE");
    }

    @Override
    public CommonResponse getErrorResponse(int code, String msg) {
        CommonResponse response = new CommonResponse();
        response.setSuccess(false);
        response.setCode(code);
        response.setMsg(msg);
        return response;
    }
}
