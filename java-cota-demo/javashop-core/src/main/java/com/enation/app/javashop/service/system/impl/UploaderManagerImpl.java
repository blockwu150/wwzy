package com.enation.app.javashop.service.system.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.enation.app.javashop.framework.database.WebPage;
import com.enation.app.javashop.mapper.system.UploaderMapper;
import com.enation.app.javashop.model.base.CachePrefix;
import com.enation.app.javashop.service.base.plugin.upload.Uploader;
import com.enation.app.javashop.model.errorcode.SystemErrorCode;
import com.enation.app.javashop.model.system.dos.UploaderDO;
import com.enation.app.javashop.model.system.vo.UploaderVO;
import com.enation.app.javashop.service.system.UploaderManager;
import com.enation.app.javashop.framework.cache.Cache;
import com.enation.app.javashop.framework.exception.ResourceNotFoundException;
import com.enation.app.javashop.framework.exception.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 存储方案业务类
 *
 * @author zh
 * @version v7.0.0
 * @since v7.0.0
 * 2018-03-22 09:31:56
 */
@Service
public class UploaderManagerImpl implements UploaderManager {

    @Autowired
    private List<Uploader> uploaders;
    @Autowired
    private Cache cache;
    @Autowired
    private UploaderMapper uploaderMapper;


    /**
     * 查询存储方案列表
     *
     * @param page     页码
     * @param pageSize 每页数量
     * @return WebPage
     */
    @Override
    public WebPage list(long page, long pageSize) {
        List<UploaderVO> resultList = this.getUploads();
        for (UploaderVO vo : resultList) {
            this.add(vo);
        }
        return new WebPage(page, Integer.valueOf(resultList.size()).longValue(), pageSize, resultList);
    }

    /**
     * 添加存储方案
     *
     * @param uploader 存储方案
     * @return Uploader 存储方案
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public UploaderDO add(UploaderVO uploader) {
        UploaderDO upload = new UploaderDO(uploader);
        Long id = upload.getId();
        if (upload.getId() == null || id == 0) {
            UploaderDO up = this.getUploader(uploader.getBean());
            if (up != null) {
                throw new ServiceException(SystemErrorCode.E900.code(), "该存储方案已经存在");
            }
            uploaderMapper.insert(upload);
        }
        // 更新缓存
        cache.remove(CachePrefix.UPLOADER.getPrefix());
        return upload;

    }

    /**
     * 获取存储方案
     *
     * @param id 存储方案主键
     * @return Uploader  存储方案
     */
    @Override
    public UploaderDO getUploader(Long id) {
        return uploaderMapper.selectById(id);
    }

    /**
     * 获取存储方案
     *
     * @param bean 存储方案beanid
     * @return Uploader  存储方案
     */
    @Override
    public UploaderDO getUploader(String bean) {
        QueryWrapper<UploaderDO> wrapper = new QueryWrapper<>();
        wrapper.eq("bean", bean);
        return uploaderMapper.selectOne(wrapper);
    }
    /**
     * 开启某个存储方案
     *
     * @param bean 存储方案bean
     * @return Uploader  存储方案
     */
    @Override
    public void openUploader(String bean) {
        List<UploaderVO> vos = this.getUploads();
        for (UploaderVO vo : vos) {
            this.add(vo);
        }
        UploaderDO upload = this.getUploader(bean);
        if (upload == null) {
            throw new ResourceNotFoundException("该存储方案不存在");
        }

        UploaderDO uploadDo = new UploaderDO();
        UpdateWrapper<UploaderDO> wrapper = new UpdateWrapper<>();
        uploadDo.setOpen(0);
        uploaderMapper.update(uploadDo,wrapper);
        wrapper.eq("bean",bean);
        uploadDo.setOpen(1);
        uploaderMapper.update(uploadDo,wrapper);

        // 更新缓存
        cache.remove(CachePrefix.UPLOADER.getPrefix());
    }


    /**
     * 获取所有的存储方案
     *
     * @return 所有的存储方案
     */
    private List<UploaderVO> getUploads() {
        List<UploaderVO> resultList = new ArrayList<>();

        QueryWrapper<UploaderDO> wrapper = new QueryWrapper<>();
        List<UploaderDO> list = uploaderMapper.selectList(wrapper);

        Map<String, UploaderDO> map = new HashMap<>(16);

        for (UploaderDO upload : list) {
            map.put(upload.getBean(), upload);
        }

        for (Uploader plugin : uploaders) {
            UploaderDO upload = map.get(plugin.getPluginId());
            UploaderVO result = null;

            if (upload != null) {
                result = new UploaderVO(upload);
            } else {
                result = UploaderVoConverter.toValidatorPlatformVO(plugin);
            }

            resultList.add(result);
        }
        return resultList;
    }

    /**
     * 根据存储方案的beanid 获取存储方案的配置项
     *
     * @param bean 存储方案beanid
     * @return 存储方案
     */
    @Override
    public UploaderVO getUploadConfig(String bean) {
        List<UploaderVO> vos = this.getUploads();
        for (UploaderVO vo : vos) {
            this.add(vo);
        }
        UploaderDO upload = this.getUploader(bean);
        if (upload == null) {
            throw new ResourceNotFoundException("该存储方案不存在");
        }
        return new UploaderVO(upload);
    }


    /**
     * 修改存储方案
     *
     * @param uploader 存储方案
     * @return Uploader 存储方案
     */
    @Override
    public UploaderVO edit(UploaderVO uploader) {
        List<UploaderVO> vos = this.getUploads();
        for (UploaderVO vo : vos) {
            this.add(vo);
        }
        UploaderDO up = this.getUploader(uploader.getBean());
        if (up == null) {
            throw new ResourceNotFoundException("该存储方案不存在");
        }
        uploader.setId(up.getId());
        uploader.setOpen(up.getOpen());
        uploaderMapper.updateById(new UploaderDO(uploader));
        return uploader;
    }
}
