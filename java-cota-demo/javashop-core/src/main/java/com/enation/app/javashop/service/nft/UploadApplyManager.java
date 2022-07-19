package com.enation.app.javashop.service.nft;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.enation.app.javashop.framework.database.WebPage;
import com.enation.app.javashop.framework.exception.ServiceException;
import com.enation.app.javashop.framework.logs.Logger;
import com.enation.app.javashop.framework.logs.LoggerFactory;
import com.enation.app.javashop.framework.util.DateUtil;
import com.enation.app.javashop.mapper.nft.*;
import com.enation.app.javashop.model.errorcode.MemberErrorCode;
import com.enation.app.javashop.model.nft.dos.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UploadApplyManager {
    /**
     * 日志记录
     */
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private UploadApplyMapper uploadApplyMapper;
    @Autowired
    private NftCollectionManager nftCollectionManager;

    public IPage<Map>  list(Long pageNo, Long pageSize, String nickname, String mobile, Integer status, String createTime) {
        Page<Map> page = new Page<Map>();
        page.setCurrent(pageNo);
        page.setSize(pageSize);
        QueryWrapper wrapper = new QueryWrapper<>();
        wrapper.eq(nickname!= null ,"n.nickname", nickname);
        wrapper.eq(mobile!= null ,"m.mobile", mobile);
        wrapper.eq(status!= null ,"u.status", status);
        wrapper.between(createTime!=null ,"u.create_time",DateUtil.getDateline(createTime),DateUtil.getDateline(createTime)+3600*24);

        return  uploadApplyMapper.pageUploadApply(page, wrapper);
    }

    public void  audit(Long uploadApplyId) {
        UploadApply u = uploadApplyMapper.selectById(uploadApplyId);
        NftPlayBill playbill = new NftPlayBill(u.getCollectionId(),u.getHeadImage(),u.getDescriptImage(),u.getCertImage());
        nftCollectionManager.setPlayBill(playbill);
        nftCollectionManager.setPrice(u.getCollectionId(),u.getPrice());
        u.setStatus(UploadApply.STAUS_UPLOADED);
        uploadApplyMapper.updateById(u);
    }

    public void  cancel(Long uploadApplyId) {
        UploadApply u = uploadApplyMapper.selectById(uploadApplyId);
        if(u.getStatus().equals(UploadApply.STAUS_UPLOADED)) {
            throw new ServiceException(MemberErrorCode.E110.code(), "当前会员申请的藏品模版已上架,不能取消！");
        }
        u.setStatus(UploadApply.STAUS_CANCELLED);
        uploadApplyMapper.updateById(u);
    }

}
