package com.mall.service.impl;

import com.google.common.collect.Lists;
import com.mall.service.IFileService;
import com.mall.util.FtpUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service("iFileService")
public class IFileServiceImpl implements IFileService {

    private Logger logger = LoggerFactory.getLogger(IFileServiceImpl.class);

    @Override
    public String upload(String path, MultipartFile file) {
        String filename = file.getOriginalFilename();
        String fileExtensionName = filename.substring(filename.indexOf(".")+1);
        String newFileName = UUID.randomUUID()+"."+fileExtensionName;

        File fileDir = new File(path);
        if(!fileDir.exists()){
            fileDir.mkdirs();
        }

        File tagetFile = new File(path,newFileName);
        try {
            file.transferTo(tagetFile);
            FtpUtil ftpUtil = new FtpUtil();
            ftpUtil.updateFile(Lists.newArrayList(tagetFile));
            return tagetFile.getName();
        }catch (IOException e){
            logger.error("文件上传失败",e);
        }finally {
            tagetFile.delete();
        }
        return  null;
    }
}
