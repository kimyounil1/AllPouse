package com.perfume.allpouse.service;

import com.perfume.allpouse.model.reponse.CommonResponse;
import com.perfume.allpouse.model.reponse.ListResponse;
import com.perfume.allpouse.model.reponse.SingleResponse;

import java.util.List;

public interface ResponseService {

    public<T> SingleResponse<T> getSingleResponse(T data);

    public<T> ListResponse<T> getListResponse(List<T> dataList);

    public void setSuccessResponse(CommonResponse response);

    public void setFalseResponse(CommonResponse response);

    public CommonResponse getErrorResponse(int code, String msg);

}
