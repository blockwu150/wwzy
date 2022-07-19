package com.enation.app.javashop.service.system.impl;

import com.enation.app.javashop.framework.context.instance.AppInstance;
import com.enation.app.javashop.framework.context.instance.InstanceContext;
import com.enation.app.javashop.framework.database.WebPage;
import com.enation.app.javashop.framework.elasticsearch.EsSettings;
import com.enation.app.javashop.framework.logs.Logger;
import com.enation.app.javashop.framework.logs.LoggerFactory;
import com.enation.app.javashop.framework.logs.appender.EsLog;
import com.enation.app.javashop.framework.util.CurrencyUtil;
import com.enation.app.javashop.framework.util.DateUtil;
import com.enation.app.javashop.service.system.LogManager;
import org.elasticsearch.action.admin.indices.settings.put.UpdateSettingsResponse;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @Description : 日志业务层实现类
 * @Author snow
 * @Date: 2020-02-03 17:22
 * @Version v1.0
 */
@Service
public class LogManagerImpl implements LogManager {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    @Autowired
    InstanceContext instanceContext;

    /**

     * ES限制
     * from * size must be less than or equal to: [10000]
     */
    private static final Integer MAX_RESULT_WINDOW = 10000;


    /**
     * 读取服务名列表
     * @return
     */

    @Override
    public List<Map> appNameList() {
        Set<String> apps = instanceContext.getApps();
        List<Map> list = new ArrayList();
        for (String appName : apps) {
            Map map = new HashMap(16);
            map.put("name", appName);
            list.add(map);
        }
        return list;
    }


    /**
     * 根据服务名查询实例列表
     * @param appName
     * @return
     */
    @Override
    public List<Map> instancesList(String appName) {

        List<AppInstance> instances = instanceContext.getInstances();

        List list = new ArrayList();
        for (AppInstance instance : instances) {
            if(appName.equals(instance.getAppName())){
                Map map = new HashMap(16);
                map.put("uuid", instance.getUuid());
                list.add(map);
            }


        }

        return list;
    }


    /**
     * 读取日志
     * @param appName   服务名
     * @param instances 实例UUID
     * @param date      日期，格式 yyyy-MM-dd
     * @param pageNo    页码
     * @param pageSize  每页记录数
     * @return
     */
    @Override
    public WebPage<String> getLogs(String appName, String instances,
                                   String date, int pageNo, int pageSize) throws RuntimeException {

        StringBuffer indexName = new StringBuffer(EsSettings.LOG_INDEX_NAME);

        if (date == null) {
            indexName.append(DateUtil.toString(new Date(), "yyyy-MM-dd"));
        } else {
            indexName.append(date);
        }

        //根据参数查询
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.must(QueryBuilders.matchPhraseQuery("appName", appName));
        boolQueryBuilder.must(QueryBuilders.matchPhraseQuery("instance", instances));

        //分页参数
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);

        //排序
        SortBuilder sortBuilder = SortBuilders.fieldSort("logTime");
        sortBuilder.order(SortOrder.ASC);

        NativeSearchQueryBuilder searchQuery = new NativeSearchQueryBuilder();
        //修改ES最大MAX_RESULT_WINDOW限制
        updateIndex(indexName.toString(),pageable.getPageNumber(),pageable.getPageSize());

        searchQuery.withIndices(indexName.toString())
                .withTypes(EsSettings.LOG_TYPE_NAME)
                .withPageable(pageable)
                .withQuery(boolQueryBuilder)
                .withSort(sortBuilder);

        Page<EsLog> page = null;
        try {
            page = elasticsearchTemplate.queryForPage(searchQuery.build(), EsLog.class);
        } catch (Exception e) {
            logger.error("读取ES日志异常:", e);
            throw e;
        }
        List<String> logList = new ArrayList<>();
        for (EsLog esLog : page.getContent()) {
            StringBuffer logStr = new StringBuffer();
            logStr.append(DateUtil.toString(esLog.getLogTime(), "yyyy-MM-dd HH:mm:ss,SSS") + " ");
            logStr.append(esLog.getLevel() + " ");
            logStr.append("[" + esLog.getThreadName() + "] ");
            logStr.append(esLog.getLoggerName() + " : ");
            logStr.append(esLog.getMessage());
            logList.add(logStr.toString());
        }

        WebPage<String> esLogPage = new WebPage<>();
        esLogPage.setPageNo(Integer.valueOf(pageNo).longValue());
        esLogPage.setPageSize(Integer.valueOf(pageSize).longValue());
        esLogPage.setDataTotal(page.getTotalElements());

        System.out.println(page.getSize());
        esLogPage.setData(logList);

        return esLogPage;
    }

    /**
     * 更新索引
     * @param indices  索引名称
     * @param from     页码
     * @param size      页面显示条数
     * @return  修改结果  true false
     */
    private void updateIndex(String indices, int from, int size) {
        int records = from * size + size;
        if (records <= MAX_RESULT_WINDOW) return;
        //重试三次
        for (int i = 0; i < 4; i++) {
            try {
                UpdateSettingsResponse indexResponse = elasticsearchTemplate.getClient().admin().indices()
                        .prepareUpdateSettings(indices)
                        .setSettings(Settings.builder()
                                .put("index.max_result_window", records)
                                .build()
                        ).get();
                logger.debug("更新index.max_result_window结果:" + indexResponse.isAcknowledged());
                return;
            } catch (Exception e) {
                try {
                    Thread.sleep(1000L);
                } catch (InterruptedException ei) {

                }
            }
        }
        logger.debug("更新index.max_result_window结果: 失败");

    }

}
