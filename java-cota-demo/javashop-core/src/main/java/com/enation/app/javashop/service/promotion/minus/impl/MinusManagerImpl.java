package com.enation.app.javashop.service.promotion.minus.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.enation.app.javashop.framework.util.PageConvert;
import com.enation.app.javashop.mapper.promotion.minus.MinusMapper;
import com.enation.app.javashop.model.base.message.PromotionScriptMsg;
import com.enation.app.javashop.model.base.rabbitmq.TimeExecute;
import com.enation.app.javashop.client.goods.GoodsClient;
import com.enation.app.javashop.model.goods.vo.GoodsSelectLine;
import com.enation.app.javashop.model.errorcode.PromotionErrorCode;
import com.enation.app.javashop.model.promotion.minus.dos.MinusDO;
import com.enation.app.javashop.model.promotion.minus.vo.MinusVO;
import com.enation.app.javashop.service.promotion.minus.MinusManager;
import com.enation.app.javashop.model.promotion.tool.dos.PromotionGoodsDO;
import com.enation.app.javashop.model.promotion.tool.dto.PromotionDetailDTO;
import com.enation.app.javashop.model.promotion.tool.enums.PromotionStatusEnum;
import com.enation.app.javashop.model.promotion.tool.enums.PromotionTypeEnum;
import com.enation.app.javashop.model.promotion.tool.dto.PromotionGoodsDTO;
import com.enation.app.javashop.model.promotion.tool.enums.ScriptOperationTypeEnum;
import com.enation.app.javashop.service.promotion.tool.PromotionGoodsManager;
import com.enation.app.javashop.service.promotion.tool.impl.AbstractPromotionRuleManagerImpl;
import com.enation.app.javashop.service.promotion.tool.support.PromotionCacheKeys;
import com.enation.app.javashop.model.util.PromotionValid;
import com.enation.app.javashop.framework.cache.Cache;
import com.enation.app.javashop.framework.context.user.UserContext;
import com.enation.app.javashop.framework.database.WebPage;
import com.enation.app.javashop.framework.exception.NoPermissionException;
import com.enation.app.javashop.framework.exception.ServiceException;
import com.enation.app.javashop.framework.security.model.Seller;
import com.enation.app.javashop.framework.trigger.Interface.TimeTrigger;
import com.enation.app.javashop.framework.util.BeanUtil;
import com.enation.app.javashop.framework.util.DateUtil;
import com.enation.app.javashop.framework.util.StringUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;


/**
 * 单品立减实现类
 * @author Snow create in 2018/3/22
 * @version v2.0
 * @since v7.0.0
 */
@SuppressWarnings("Duplicates")
@Service
public class MinusManagerImpl extends AbstractPromotionRuleManagerImpl implements MinusManager {

    @Autowired
    private MinusMapper minusMapper;

    @Autowired
    private PromotionGoodsManager promotionGoodsManager;

    @Autowired
    private GoodsClient goodsClient;

    @Autowired
    private Cache cache;

    @Autowired
    private TimeTrigger timeTrigger;

    /**
     * 查询单品立减促销活动分页数据列表
     * @param page 页码
     * @param pageSize 每页数量
     * @param keywords 查询关键字
     * @return WebPage
     */
    @Override
    public WebPage list(long page, long pageSize, String keywords){

        // 获取当前登录的店铺ID
        Long sellerId = UserContext.getSeller().getSellerId();

        QueryWrapper<MinusDO> queryWrapper = new QueryWrapper<MinusDO>()
                .eq("seller_id", sellerId)
                .like(StringUtil.notEmpty(keywords), "title", keywords)
                .orderByDesc("minus_id");

        IPage<MinusVO> iPage = minusMapper.selectMinusVoPage(new Page(page, pageSize), queryWrapper);

        List<MinusVO> minusVOList = iPage.getRecords();

        for (MinusVO minusVO :minusVOList){
            long nowTime = DateUtil.getDateline();
            //当前时间小于活动的开始时间 则为活动未开始
            if(nowTime < minusVO.getStartTime().longValue() ){
                minusVO.setStatusText("活动未开始");
                minusVO.setStatus(PromotionStatusEnum.WAIT.toString());
                //大于活动的开始时间，小于活动的结束时间
            }else if(minusVO.getStartTime().longValue() < nowTime && nowTime < minusVO.getEndTime() ){
                minusVO.setStatusText("正在进行中");
                minusVO.setStatus(PromotionStatusEnum.UNDERWAY.toString());

            }else{
                minusVO.setStatusText("活动已失效");
                minusVO.setStatus(PromotionStatusEnum.END.toString());
            }
        }

        return PageConvert.convert(iPage);
    }

    /**
     * 添加单品立减促销活动信息
     * @param minusVO 单品立减促销活动信息
     * @return minusVO 单品立减促销活动信息
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = {RuntimeException.class,Exception.class, ServiceException.class})
    public MinusVO add(MinusVO minusVO)	{
        //检测开始时间和结束时间
        PromotionValid.paramValid(minusVO.getStartTime(),minusVO.getEndTime(),1,null);
        this.verifyTime(minusVO.getStartTime(),minusVO.getEndTime(),PromotionTypeEnum.MINUS,null);

        //初步形成商品的DTO列表
        List<PromotionGoodsDTO> goodsDTOList = new ArrayList<>();
        //是否是全部商品参与
        if(minusVO.getRangeType() == 1){
            PromotionGoodsDTO goodsDTO = new PromotionGoodsDTO();
            goodsDTO.setGoodsId(-1L);
            goodsDTO.setSkuId(-1L);
            goodsDTO.setGoodsName("全部商品");
            goodsDTO.setThumbnail("path");
            goodsDTOList.add(goodsDTO);
            minusVO.setGoodsList(goodsDTOList);
        }
        //检测活动规则
        this.verifyRule(minusVO.getGoodsList());

        MinusDO minusDO = new MinusDO();
        BeanUtils.copyProperties(minusVO,minusDO);
        minusMapper.insert(minusDO);

        // 获取活动Id
        Long minusId = minusDO.getMinusId();
        minusDO.setMinusId(minusId);
        minusVO.setMinusId(minusId);

        PromotionDetailDTO detailDTO = new PromotionDetailDTO();
        detailDTO.setStartTime(minusVO.getStartTime());
        detailDTO.setEndTime(minusVO.getEndTime());
        detailDTO.setActivityId(minusVO.getMinusId());
        detailDTO.setPromotionType(PromotionTypeEnum.MINUS.name());
        detailDTO.setTitle(minusVO.getTitle());

        //将活动商品入库
        this.promotionGoodsManager.add(minusVO.getGoodsList(),detailDTO);

        String minusKey = PromotionCacheKeys.getMinusKey(minusId);
        cache.put(minusKey, minusDO);

        //启用延时任务创建促销活动脚本信息
        PromotionScriptMsg promotionScriptMsg = new PromotionScriptMsg();
        promotionScriptMsg.setPromotionId(minusId);
        promotionScriptMsg.setPromotionName(minusDO.getTitle());
        promotionScriptMsg.setPromotionType(PromotionTypeEnum.MINUS);
        promotionScriptMsg.setOperationType(ScriptOperationTypeEnum.CREATE);
        promotionScriptMsg.setEndTime(minusDO.getEndTime());
        String uniqueKey = "{TIME_TRIGGER_" + PromotionTypeEnum.MINUS.name() + "}_" + minusId;
        timeTrigger.add(TimeExecute.SELLER_PROMOTION_SCRIPT_EXECUTER, promotionScriptMsg, minusDO.getStartTime(), uniqueKey);

        return minusVO;
    }

    /**
     * 修改单品立减促销活动信息
     * @param minusVO 单品立减促销活动信息
     * @param id 单品立减促销活动主键ID
     * @return minusVO 单品立减促销活动信息
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = {RuntimeException.class,Exception.class,ServiceException.class,NoPermissionException.class})
    public MinusVO edit(MinusVO minusVO,Long id){

        //检查此活动是否可操作
        this.verifyStatus(id);
        //检测开始时间和结束时间
        PromotionValid.paramValid(minusVO.getStartTime(),minusVO.getEndTime(),1,null);
        this.verifyTime(minusVO.getStartTime(),minusVO.getEndTime(),PromotionTypeEnum.MINUS,id);

        //获取修改操作之前的单品立减促销活动信息
        MinusDO oldMinus = this.getFromDB(id);

        //初步形成商品的DTO列表
        List<PromotionGoodsDTO> goodsDTOList = new ArrayList<>();
        //是否是全部商品参与
        if(minusVO.getRangeType() == 1){
            PromotionGoodsDTO goodsDTO = new PromotionGoodsDTO();
            goodsDTO.setGoodsId(-1L);
            goodsDTO.setSkuId(-1L);
            goodsDTO.setGoodsName("全部商品");
            goodsDTO.setThumbnail("");
            goodsDTOList.add(goodsDTO);
            minusVO.setGoodsList(goodsDTOList);
        }
        //检测活动规则
        this.verifyRule(minusVO.getGoodsList());

        // 获取当前登录的店铺ID
        Seller seller = UserContext.getSeller();
        Long sellerId = seller.getSellerId();
        minusVO.setSellerId(sellerId);

        MinusDO minusDO = new MinusDO();
        BeanUtils.copyProperties(minusVO,minusDO);

        minusDO.setMinusId(id);
        minusMapper.updateById(minusDO);

        //删除之前的活动与商品的对照关系
        PromotionDetailDTO detailDTO = new PromotionDetailDTO();
        detailDTO.setStartTime(minusVO.getStartTime());
        detailDTO.setEndTime(minusVO.getEndTime());
        detailDTO.setActivityId(minusVO.getMinusId());
        detailDTO.setPromotionType(PromotionTypeEnum.MINUS.name());
        detailDTO.setTitle(minusVO.getTitle());

        //将活动商品入库
        this.promotionGoodsManager.edit(minusVO.getGoodsList(),detailDTO);

        String minusKey = PromotionCacheKeys.getMinusKey(id);
        cache.put(minusKey, minusDO);

        //启用延时任务创建促销活动脚本信息
        PromotionScriptMsg promotionScriptMsg = new PromotionScriptMsg();
        promotionScriptMsg.setPromotionId(id);
        promotionScriptMsg.setPromotionName(minusDO.getTitle());
        promotionScriptMsg.setPromotionType(PromotionTypeEnum.MINUS);
        promotionScriptMsg.setOperationType(ScriptOperationTypeEnum.CREATE);
        promotionScriptMsg.setEndTime(minusDO.getEndTime());
        String uniqueKey = "{TIME_TRIGGER_" + PromotionTypeEnum.MINUS.name() + "}_" + id;
        timeTrigger.edit(TimeExecute.SELLER_PROMOTION_SCRIPT_EXECUTER, promotionScriptMsg, oldMinus.getStartTime(), minusDO.getStartTime(), uniqueKey);

        return minusVO;
    }

    /**
     * 根据主键ID删除单品立减促销活动信息
     * 1.根据活动id删除esMinus中的数据
     * 2.调用promotionGoodsManager中的删除方法，删除esPromotionGoods表中的数据
     * 3.删除Redis中的活动实例对象
     * @param id 单品立减活动主键ID
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = {RuntimeException.class,Exception.class,ServiceException.class,NoPermissionException.class})
    public void delete(Long id)	{
        //验证此活动是否可进行编辑删除操作
        this.verifyStatus(id);
        //获取单品立减促销活动信息
        MinusDO minusDO = this.getFromDB(id);
        //删除操作
        minusMapper.deleteById(id);
        //删除单品立减商品活动对照表
        this.promotionGoodsManager.delete(id,PromotionTypeEnum.MINUS.name());
        this.cache.remove(PromotionCacheKeys.getMinusKey(id));

        //删除缓存中的延时任务执行器
        String uniqueKey = "{TIME_TRIGGER_" + PromotionTypeEnum.MINUS.name() + "}_" + id;
        timeTrigger.delete(TimeExecute.SELLER_PROMOTION_SCRIPT_EXECUTER, minusDO.getStartTime(), uniqueKey);
        if (minusDO.getEndTime().longValue() < DateUtil.getDateline()) {
            timeTrigger.delete(TimeExecute.SELLER_PROMOTION_SCRIPT_EXECUTER, minusDO.getEndTime(), uniqueKey);
        }
    }

    /**
     * 根据主键ID获取单品立减促销活动信息
     * @param minusId 单品立减活动主键ID
     * @return minusVO 单品立减促销活动信息
     */
    @Override
    public MinusVO getFromDB(Long minusId) {
        //从缓存中获取单品立减促销活动信息
        MinusDO minusDO = (MinusDO) this.cache.get(PromotionCacheKeys.getMinusKey(minusId));
        //缓存中如果活动信息为空，则去数据库中查询数据
        if(minusDO == null ){
            minusDO = minusMapper.selectById(minusId);
            //如果数据库中的活动信息也为空则直接抛出异常；否则将活动信息重新放入缓存中
            if (minusDO == null) {
                throw new ServiceException(PromotionErrorCode.E400.code(),"单品立减促销活动信息不存在");
            } else {
                this.cache.put(PromotionCacheKeys.getMinusKey(minusId), minusDO);
            }
        }

        //创建单品立减促销活动信息VO对象
        MinusVO minusVO = new MinusVO();
        //复制minusDO信息到minusVO中
        BeanUtils.copyProperties(minusDO,minusVO);

        //根据活动ID和活动类型获取参与单品立减活动的商品信息集合
        List<PromotionGoodsDO> goodsDOList = this.promotionGoodsManager.getPromotionGoods(minusId,PromotionTypeEnum.MINUS.name());
        //循环获取参与活动的商品sku信息集合
        Long[] skuIds = new Long[goodsDOList.size()];
        for(int i =0;i<goodsDOList.size(); i++){
            skuIds[i] = goodsDOList.get(i).getSkuId();
        }

        //根据参与活动的商品SKU信息获取相关商品信息
        List<GoodsSelectLine> goodsSelectLineList = this.goodsClient.querySkus(skuIds);

        //循环将商品信息放入促销商品信息集合中
        List<PromotionGoodsDTO> goodsList = new ArrayList<>();
        for(GoodsSelectLine goodsSelectLine:goodsSelectLineList){
            PromotionGoodsDTO goodsDTO = new PromotionGoodsDTO();
            BeanUtil.copyProperties(goodsSelectLine,goodsDTO);
            goodsList.add(goodsDTO);
        }

        minusVO.setGoodsList(goodsList);
        return minusVO;
    }

    /**
     * 验证操作权限<br/>
     * 如有问题直接抛出权限异常
     * @param minusId 单品立减活动主键ID
     */
    @Override
    public void verifyAuth(Long minusId) {
        //获取当前登录的商家信息
        Seller seller = UserContext.getSeller();
        //通过ID获取单品立减促销活动信息
        MinusDO minusDO = this.getFromDB(minusId);
        //验证越权操作（判断单品立减促销活动是否属于当前登录的商家）
        if (minusDO == null || seller.getSellerId().intValue() != minusDO.getSellerId().intValue() ){
            throw new NoPermissionException("无权操作");
        }
    }

    /**
     * 验证此活动是否可进行编辑删除操作<br/>
     * 如有问题则抛出异常
     * @param minusId   活动id
     */
    private void verifyStatus(Long minusId) {
        //通过ID获取单品立减促销活动信息
        MinusVO minusVO = this.getFromDB(minusId);
        //获取当前时间戳
        long nowTime = DateUtil.getDateline();

        //如果活动起始时间小于现在时间，活动已经开始了。
        if(minusVO.getStartTime().longValue() < nowTime && minusVO.getEndTime().longValue() > nowTime){
            throw new ServiceException(PromotionErrorCode.E400.code(),"活动已经开始，不能进行编辑删除操作");
        }
    }
}
