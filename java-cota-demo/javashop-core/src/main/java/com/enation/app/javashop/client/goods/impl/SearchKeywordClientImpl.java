package com.enation.app.javashop.client.goods.impl;

import com.enation.app.javashop.client.goods.SearchKeywordClient;
import com.enation.app.javashop.service.goodssearch.SearchKeywordManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

/**
* @author liuyulei
 * @version 1.0
 * @Description:  关键词所搜历史对外接口实现
 * @date 2019/5/28 11:09
 * @since v7.0
 */

@Service
@ConditionalOnProperty(value="javashop.product", havingValue="stand")
public class SearchKeywordClientImpl implements SearchKeywordClient {

    @Autowired
    private SearchKeywordManager searchKeywordManager;

    /**
     * 添加搜索关键字
     * @param keyword
     */
    @Override
    public void add(String keyword) {
        this.searchKeywordManager.add(keyword);
    }

    /**
     * 更新关键字数据
     * @param keyword
     */
    @Override
    public void update(String keyword) {
        this.searchKeywordManager.update(keyword);
    }

    /**
     * 判断关键字是否存在
     * @param keyword
     * @return
     */
    @Override
    public boolean isExist(String keyword) {
        return this.searchKeywordManager.isExist(keyword);
    }
}
