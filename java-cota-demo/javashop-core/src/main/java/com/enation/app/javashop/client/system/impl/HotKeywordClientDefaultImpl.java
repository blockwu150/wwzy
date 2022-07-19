package com.enation.app.javashop.client.system.impl;

import com.enation.app.javashop.client.system.HotkeywordClient;
import com.enation.app.javashop.client.system.SettingClient;
import com.enation.app.javashop.model.base.SettingGroup;
import com.enation.app.javashop.model.pagedata.HotKeyword;
import com.enation.app.javashop.service.base.service.SettingManager;
import com.enation.app.javashop.service.pagedata.HotKeywordManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author zs
 * @version v1.0
 * @Description: 热点关键字
 * @date 2020-11-17
 * @since v7.2.2
 */
@Service
@ConditionalOnProperty(value="javashop.product", havingValue="stand")
public class HotKeywordClientDefaultImpl implements HotkeywordClient {

    @Autowired
    private HotKeywordManager hotKeywordManager;

    @Override
    public List<HotKeyword> listByNum(Integer num) {
        return hotKeywordManager.listByNum(num);
    }
}
