package com.enation.app.javashop.service.system.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.enation.app.javashop.framework.database.WebPage;
import com.enation.app.javashop.framework.util.PageConvert;
import com.enation.app.javashop.mapper.system.MessageTemplateMapper;
import com.enation.app.javashop.model.system.enums.MessageCodeEnum;
import com.enation.app.javashop.model.system.dos.MessageTemplateDO;
import com.enation.app.javashop.model.system.dto.MessageTemplateDTO;
import com.enation.app.javashop.service.system.MessageTemplateManager;
import org.apache.commons.lang.text.StrSubstitutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

/**
 * 消息模版业务类
 * @author zjp
 * @version v7.0.0
 * @since v7.0.0
 * 2018-07-05 16:38:43
 */
@Service
public class MessageTemplateManagerImpl implements MessageTemplateManager {

	@Autowired
	private MessageTemplateMapper messageTemplateMapper;

	/**
	 * 查询消息模版列表
	 * @param page 页码
	 * @param pageSize 每页数量
	 * @param type 模版类型
	 * @return WebPage
	 */
	@Override
	public WebPage list(long page, long pageSize, String type){

		QueryWrapper<MessageTemplateDO> wrapper = new QueryWrapper<>();
		wrapper.eq("type",type);
		IPage<MessageTemplateDO> iPage = messageTemplateMapper.selectPage(new Page<>(page,pageSize), wrapper);
		return PageConvert.convert(iPage);

	}

	/**
	 * 修改消息模版
	 * @param messageTemplate 消息模版
	 * @param id 消息模版主键
	 * @return MessageTemplateDO 消息模版
	 */
	@Override
	@Transactional(value = "",propagation = Propagation.REQUIRED,rollbackFor=Exception.class)
	public	MessageTemplateDO  edit(MessageTemplateDTO messageTemplate, Long id){
		MessageTemplateDO messageTemplateDO = new MessageTemplateDO();
		UpdateWrapper<MessageTemplateDO> wrapper = new UpdateWrapper<>();
		wrapper.eq("id",id);
		messageTemplateDO.setTplName(messageTemplate.getTplName());
		messageTemplateDO.setEmailTitle(messageTemplate.getEmailTitle());
		messageTemplateDO.setSmsState(messageTemplate.getSmsState());
		messageTemplateDO.setNoticeState(messageTemplate.getNoticeState());
		messageTemplateDO.setEmailState(messageTemplate.getEmailState());
		messageTemplateDO.setContent(messageTemplate.getContent());
		messageTemplateDO.setSmsContent(messageTemplate.getSmsContent());
		messageTemplateDO.setEmailContent(messageTemplate.getEmailContent());
		messageTemplateMapper.update(messageTemplateDO,wrapper);
		return this.getModel(id);
	}


	/**
	 * 获取消息模版
	 * @param messageCodeEnum 消息模版编码
	 * @return MessageTemplateDO  消息模版
	 */
	@Override
	public	MessageTemplateDO getModel(MessageCodeEnum messageCodeEnum )	{
		QueryWrapper<MessageTemplateDO> wrapper = new QueryWrapper<>();
		wrapper.eq("tpl_code", messageCodeEnum.value());
		return messageTemplateMapper.selectOne(wrapper);
	}

	/**
	 * 替换文本内容
	 * @param content 文本
	 * @param valuesMap 要替换的文字
	 * @return
	 */
	@Override
	public String replaceContent(String content, Map<String, Object> valuesMap) {
		StrSubstitutor strSubstitutor = new StrSubstitutor(valuesMap);
		return strSubstitutor.replace(content);
	}

	/**
	 * 通过id获取模版
	 * @param id
	 * @return
	 */
	@Override
	public MessageTemplateDO getModel(Long id) {
		return messageTemplateMapper.selectById(id);
	}
}
