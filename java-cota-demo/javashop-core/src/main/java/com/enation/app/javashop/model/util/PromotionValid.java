package com.enation.app.javashop.model.util;

import com.enation.app.javashop.model.promotion.tool.dto.PromotionGoodsDTO;
import com.enation.app.javashop.framework.exception.ServiceException;
import com.enation.app.javashop.framework.exception.SystemErrorCodeV1;
import com.enation.app.javashop.framework.util.DateUtil;

import java.util.List;

/**
 * 参数验证
 * @author Snow create in 2018/3/30
 * @version v2.0
 * @since v7.0.0
 */
public class PromotionValid {

    /**
     * 30天的秒数
     */
    private final static Long MAX_TIME = 60 * 60 * 24 * 30L;
    /**
     * 参数验证
     * 1、活动起始时间必须大于当前时间
     * 2、验证活动开始时间是否大于活动结束时间
     *
     * 无返回值，如有错误直接抛异常
     * @param startTime 活动开始时间
     * @param endTime   活动结束时间
     * @param rangeType 是否全部商品参与
     * @param goodsList 选择的商品
     *
     */
    public static void paramValid(Long startTime, Long endTime, int rangeType, List<PromotionGoodsDTO> goodsList){

        long nowTime  = DateUtil.getDateline();

        if((startTime - nowTime ) > MAX_TIME){
            throw new ServiceException(SystemErrorCodeV1.INVALID_REQUEST_PARAMETER,"参数异常：开始时间必须在30天内");
        }

        if((endTime - nowTime ) > MAX_TIME){
            throw new ServiceException(SystemErrorCodeV1.INVALID_REQUEST_PARAMETER,"参数异常：结束时间必须在30天内");
        }


        //如果活动起始时间小于现在时间
        if(startTime.longValue() < nowTime){
            throw new ServiceException(SystemErrorCodeV1.INVALID_REQUEST_PARAMETER,"活动起始时间必须大于当前时间");
        }

        // 开始时间不能大于结束时间
        if (startTime.longValue() > endTime.longValue() ) {
            throw new ServiceException(SystemErrorCodeV1.INVALID_REQUEST_PARAMETER,"活动起始时间不能大于活动结束时间");
        }

        //部分商品
        int part = 2;

        // 如果促销活动选择的是部分商品参加活动
        if (rangeType == part) {
            // 商品id组不能为空
            if (goodsList == null) {
                throw new ServiceException(SystemErrorCodeV1.INVALID_REQUEST_PARAMETER,"请选择要参与活动的商品");
            }
        }
    }


}
