package com.enation.app.javashop.client.system;

import com.enation.app.javashop.model.member.vo.RegionVO;
import com.enation.app.javashop.model.system.dos.Regions;

import java.util.List;

/**
 * @version v7.0
 * @Description: 地区Client
 * @Author: zjp
 * @Date: 2018/7/27 11:14
 */
public interface RegionsClient {
    /**
     * 根据地区id获取其子地区
     *
     * @param regionId 地区id
     * @return 地区集合
     */
    List<Regions> getRegionsChildren(Long regionId);

    /**
     * 获取地区
     *
     * @param id 地区主键
     * @return Regions  地区
     */
    Regions getModel(Long id);

    /**
     * 根据深度获取组织地区数据结构的数据
     *
     * @param depth 地区深度
     * @return 地区集合
     */
    List<RegionVO> getRegionByDepth(Integer depth);
}
