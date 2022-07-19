package com.enation.app.javashop.client.system;

import com.enation.app.javashop.model.base.SettingGroup;
import com.enation.app.javashop.model.pagedata.HotKeyword;

import java.util.List;

/**
 * @author zs
 * @version v1.0
 * @Description: 热点关键字client
 * @date 2020-11-17
 * @since v7.2.2
 */
public interface HotkeywordClient {

    /**
     * 查询热门关键字
     * @param num
     * @return
     */
    List<HotKeyword> listByNum(Integer num);


}
