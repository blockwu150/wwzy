package com.enation.app.javashop.service.system;

import com.enation.app.javashop.model.system.dos.SmtpDO;
import com.enation.app.javashop.framework.database.WebPage;

/**
 * 邮件业务层
 *
 * @author zh
 * @version v7.0.0
 * @since v7.0.0
 * 2018-03-25 16:16:53
 */
public interface SmtpManager {

    /**
     * 查询邮件列表
     *
     * @param page     页码
     * @param pageSize 每页数量
     * @return WebPage
     */
    WebPage list(long page, long pageSize);

    /**
     * 修改邮件
     *
     * @param smtp 邮件
     * @param id   邮件主键
     * @return Smtp 邮件
     */
    SmtpDO edit(SmtpDO smtp, Long id);

    /**
     * 获取邮件
     *
     * @param id 邮件主键
     * @return Smtp  邮件
     */
    SmtpDO getModel(Long id);

    /**
     * 添加邮件
     *
     * @param smtp 邮件
     * @return Smtp 邮件
     */
    SmtpDO add(SmtpDO smtp);

    /**
     * 删除邮件
     *
     * @param id 邮件主键
     */
    void delete(Long id);

    /**
     * 发送测试邮件
     *
     * @param send 发送邮件地址
     * @param smtp smtp设置
     */
    void send(String send, SmtpDO smtp);

    /**
     * 获取当前使用的smtp方案
     *
     * @return smtp
     */
    SmtpDO getCurrentSmtp();
}
