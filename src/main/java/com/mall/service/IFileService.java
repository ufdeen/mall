package com.mall.service;

import com.mall.common.ServerResponse;
import org.springframework.web.multipart.MultipartFile;


public interface  IFileService {

    String upload(String path,  MultipartFile file);
}
