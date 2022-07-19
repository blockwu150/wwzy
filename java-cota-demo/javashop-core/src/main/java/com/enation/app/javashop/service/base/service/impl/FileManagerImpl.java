package com.enation.app.javashop.service.base.service.impl;


import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enation.app.javashop.model.base.CachePrefix;
import com.enation.app.javashop.model.base.dto.FileDTO;
import com.enation.app.javashop.model.base.vo.ConfigItem;
import com.enation.app.javashop.model.base.vo.FileVO;
import com.enation.app.javashop.service.base.plugin.upload.Uploader;
import com.enation.app.javashop.service.base.service.FileManager;
import com.enation.app.javashop.service.system.factory.UploadFactory;
import com.enation.app.javashop.model.system.vo.UploaderVO;
import com.enation.app.javashop.framework.cache.Cache;
import com.enation.app.javashop.framework.util.StringUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import storage.nft.ApiClient;
import storage.nft.ApiException;
import storage.nft.Configuration;
import storage.nft.api.NftStorageApi;
import storage.nft.auth.HttpBearerAuth;
import storage.nft.model.DeleteResponse;

/**
 * 文件上传接口实现
 *
 * @author zh
 * @version v2.0
 * @since v7.0
 * 2018年3月19日 下午4:38:42
 */
@Service
public class FileManagerImpl implements FileManager {

    @Autowired
    private UploadFactory uploadFactory;
    @Autowired
    private Cache cache;

    @Override
    public FileVO upload(FileDTO input, String scene) {
        if (StringUtil.isEmpty(scene)) {
            scene = "normal";
        }
        Uploader uploader = uploadFactory.getUploader();
        return uploader.upload(input, scene, this.getconfig());
    }

    @Override
    public void deleteFile(String filePath) {
        Uploader uploader = uploadFactory.getUploader();
        uploader.deleteFile(filePath, this.getconfig());
    }

    /**
     * 获取存储方案配置
     *
     * @return
     */
    private Map getconfig() {
        UploaderVO upload = (UploaderVO) cache.get(CachePrefix.UPLOADER.getPrefix());
        if (StringUtil.isEmpty(upload.getConfig())) {
            return new HashMap<>(16);
        }
        Gson gson = new Gson();
        List<ConfigItem> list = gson.fromJson(upload.getConfig(), new TypeToken<List<ConfigItem>>() {
        }.getType());
        Map<String, String> result = new HashMap<>(16);
        if (list != null) {
            for (ConfigItem item : list) {
                result.put(item.getName(), StringUtil.toString(item.getValue()));
            }
        }
        return result;
    }


//    public void uploadNft(File file) {
//        ApiClient defaultClient = Configuration.getDefaultApiClient();
//        defaultClient.setBasePath("https://api.nft.storage");
//        // Configure HTTP bearer authorization: bearerAuth
//        HttpBearerAuth bearerAuth = (HttpBearerAuth) defaultClient.getAuthentication("bearerAuth");
//        bearerAuth.setBearerToken("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJkaWQ6ZXRocjoweDJhQjVGNUQ0MkM0MDU2MzFhMWVlZmRkNTQ2OTkxNzc0NGEwMTcwMTgiLCJpc3MiOiJuZnQtc3RvcmFnZSIsImlhdCI6MTY1ODIxMDIyMzI2NCwibmFtZSI6Imd1b2dhbmcgeXUifQ.8CXisQc3jgxqg6_bD8W6trqP4vvfJOrucYY5nSiF7UI");
//        NftStorageApi apiInstance = new NftStorageApi(defaultClient);
//        try {
////            apiInstance.store(file.);
//
////            System.out.println(result);
//        } catch (ApiException e) {
//            System.err.println("Exception when calling NftStorageApi#delete");
//            System.err.println("Status code: " + e.getCode());
//            System.err.println("Reason: " + e.getResponseBody());
//            System.err.println("Response headers: " + e.getResponseHeaders());
//            e.printStackTrace();
//        }
//    }
}
