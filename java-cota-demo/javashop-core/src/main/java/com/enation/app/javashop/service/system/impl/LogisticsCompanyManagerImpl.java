package com.enation.app.javashop.service.system.impl;

import javax.validation.Valid;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.enation.app.javashop.client.member.ShopLogisticsCompanyClient;
import com.enation.app.javashop.framework.database.WebPage;
import com.enation.app.javashop.framework.util.PageConvert;
import com.enation.app.javashop.mapper.system.LogisticsCompanyMapper;
import com.enation.app.javashop.model.system.enums.DeleteStatusEnum;
import com.enation.app.javashop.model.system.enums.LogiCompanyStatusEnum;
import com.enation.app.javashop.service.system.LogisticsCompanyManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.enation.app.javashop.framework.exception.ServiceException;
import com.enation.app.javashop.framework.util.StringUtil;
import com.enation.app.javashop.model.errorcode.ShopErrorCode;
import com.enation.app.javashop.model.system.dos.LogisticsCompanyDO;

import java.util.List;

/**
 * 物流公司业务类
 * @author zjp
 * @version v7.0.0
 * @since v7.0.0
 * 2018-03-29 15:10:38
 */
@Service
public class LogisticsCompanyManagerImpl implements LogisticsCompanyManager {

	@Autowired
	private ShopLogisticsCompanyClient shopLogisticsCompanyClient;
	@Autowired
	private LogisticsCompanyMapper logisticsCompanyMapper;

	/**
	 * 查询物流公司列表
	 * @param page 页码
	 * @param pageSize 每页数量
	 * @param name 物流公司名称
	 * @return WebPage
	 */
	@Override
	public WebPage list(long page, long pageSize, String name){

		QueryWrapper<LogisticsCompanyDO> wrapper = new QueryWrapper<>();
		wrapper.eq("delete_status",DeleteStatusEnum.NORMAL.value());
		wrapper.like(StringUtil.notEmpty(name),"name",name);
		wrapper.orderByDesc("id");
		IPage<LogisticsCompanyDO> iPage = logisticsCompanyMapper.selectPage(new Page<>(page,pageSize), wrapper);
		return PageConvert.convert(iPage);

	}

	/**
	 * 添加物流公司
	 * @param logi 物流公司
	 * @return Logi 物流公司
	 */
	@Override
	@Transactional(value = "systemTransactionManager",propagation = Propagation.REQUIRED,rollbackFor=Exception.class)
	public LogisticsCompanyDO add(LogisticsCompanyDO logi)	{
		if(this.checkRepeat(logi.getName(), null, null, null)){
			throw new ServiceException(ShopErrorCode.E211.name(), "物流公司名称重复");
		}
		if(this.checkRepeat(null, logi.getCode(), null, null)){
			throw new ServiceException(ShopErrorCode.E213.name(), "物流公司代码重复");
		}
		if (StringUtil.notEmpty(logi.getKdcode())) {
			if(this.checkRepeat(null, null, logi.getKdcode(), null)){
				throw new ServiceException(ShopErrorCode.E212.name(), "快递鸟物流代码重复");
			}
		}

		logi.setDeleteStatus(DeleteStatusEnum.NORMAL.value());
		logi.setDisabled(LogiCompanyStatusEnum.OPEN.value());

		logisticsCompanyMapper.insert(logi);

		return logi;
	}

	/**
	 * 根据不同信息判断物流公司是否重复
	 * @param name 物流公司名称
	 * @param code 物流公司代码
	 * @param kdCode 快递鸟物流代码
	 * @param id 物流公司ID
	 * @return
	 */
	private boolean checkRepeat(String name, String code, String kdCode, Long id) {

		QueryWrapper<LogisticsCompanyDO> wrapper = new QueryWrapper<>();
		wrapper.eq("delete_status", DeleteStatusEnum.NORMAL.value());
        wrapper.eq(StringUtil.notEmpty(name),"name", name);
		wrapper.eq(StringUtil.notEmpty(code),"code", code);
		wrapper.eq(StringUtil.notEmpty(kdCode),"kdcode",kdCode);
		wrapper.ne(id != null,"id", id);
		int count = logisticsCompanyMapper.selectCount(wrapper);

		return count != 0 ? true : false;
	}

	/**
	 * 修改物流公司
	 * @param logi 物流公司
	 * @param id 物流公司主键
	 * @return Logi 物流公司
	 */
	@Override
	@Transactional(value = "systemTransactionManager",propagation = Propagation.REQUIRED,rollbackFor=Exception.class)
	public LogisticsCompanyDO edit(@Valid LogisticsCompanyDO logi, Long id){
		LogisticsCompanyDO model = this.getModel(id);
		if(model==null) {
			throw new ServiceException(ShopErrorCode.E214.name(), "物流公司不存在");
		}
		//当支持电子面单时，需要填写快递鸟物流公司code
		if(logi.getIsWaybill() == 1 && StringUtil.isEmpty(logi.getKdcode())){
			throw new ServiceException(ShopErrorCode.E212.name(), "快递鸟公司代码必填");
		}

		if(this.checkRepeat(logi.getName(), null, null, id)){
			throw new ServiceException(ShopErrorCode.E211.name(), "物流公司名称重复");
		}
		if(this.checkRepeat(null, logi.getCode(), null, id)){
			throw new ServiceException(ShopErrorCode.E213.name(), "物流公司代码重复");
		}
		if (StringUtil.notEmpty(logi.getKdcode())) {
			if(this.checkRepeat(null, null, logi.getKdcode(), id)){
				throw new ServiceException(ShopErrorCode.E212.name(), "快递鸟物流代码重复");
			}
		}
		logi.setDisabled(model.getDisabled());
		logisticsCompanyMapper.updateById(logi);
		return logi;
	}

	/**
	 * 删除物流公司
	 * @param id 物流公司主键
	 */
	@Override
	@Transactional(value = "systemTransactionManager",propagation = Propagation.REQUIRED,rollbackFor=Exception.class)
	public	void delete(Long logiId)	{
		LogisticsCompanyDO model = this.getModel(logiId);
		if (model == null ) {
			throw new ServiceException(ShopErrorCode.E214.name(), "物流公司不存在");
		}

		List list = shopLogisticsCompanyClient.queryListByLogisticsId(logiId);
		if (StringUtil.isNotEmpty(list)) {
			throw new ServiceException(ShopErrorCode.E214.name(), "当前物流公司已经被商家使用，不能删除");
		}

		logisticsCompanyMapper.deleteById(logiId);
	}

	/**
	 * 获取物流公司
	 * @param id 物流公司主键
	 * @return Logi  物流公司
	 */
	@Override
	public LogisticsCompanyDO getModel(Long id)	{
		return logisticsCompanyMapper.selectById(id);
	}

	/**
	 * 通过code获取物流公司
	 * @param code 物流公司code
	 * @return 物流公司
	 */
	@Override
	public LogisticsCompanyDO getLogiByCode(String code) {

		QueryWrapper<LogisticsCompanyDO> wrapper = new QueryWrapper<>();
		wrapper.eq("code", code);
		LogisticsCompanyDO logiCompany =  logisticsCompanyMapper.selectOne(wrapper);

		return logiCompany;
	}

	/**
	 * 通过快递鸟物流code获取物流公司
	 * @param kdcode 快递鸟公司code
	 * @return 物流公司
	 */
	@Override
	public LogisticsCompanyDO getLogiBykdCode(String kdcode) {

		QueryWrapper<LogisticsCompanyDO> wrapper = new QueryWrapper<>();
		wrapper.eq("kdcode", kdcode);
		LogisticsCompanyDO logiCompany =  logisticsCompanyMapper.selectOne(wrapper);

		return logiCompany;
	}

	/**
	 * 根据物流名称查询物流信息
	 * @param name 物流名称
	 * @return 物流公司
	 */
	@Override
	public LogisticsCompanyDO getLogiByName(String name) {

		QueryWrapper<LogisticsCompanyDO> wrapper = new QueryWrapper<>();
		wrapper.eq("name", name);
		LogisticsCompanyDO logiCompany =  logisticsCompanyMapper.selectOne(wrapper);

		return logiCompany;
	}

	/**
	 * 查询物流公司列表(不分页)
	 * @return WebPage
	 */
	@Override
	public List<LogisticsCompanyDO> list() {

		QueryWrapper<LogisticsCompanyDO> wrapper = new QueryWrapper<>();
		wrapper.orderByDesc("id");
		return logisticsCompanyMapper.selectList(wrapper);

	}

	/**
	 * 开启或禁用物流公司
	 * @param id 物流公司主键ID
	 * @param disabled 状态 OPEN：开启，CLOSE：禁用
	 */
	@Override
	@Transactional(value = "systemTransactionManager",propagation = Propagation.REQUIRED,rollbackFor=Exception.class)
	public void openCloseLogi(Long id, String disabled) {
		if (StringUtil.isEmpty(disabled) || (!LogiCompanyStatusEnum.OPEN.name().equals(disabled) && !LogiCompanyStatusEnum.CLOSE.name().equals(disabled))) {
			throw new ServiceException(ShopErrorCode.E227.name(), "参数传递不正确");
		}

		LogisticsCompanyDO model = this.getModel(id);
		if (model == null ) {
			throw new ServiceException(ShopErrorCode.E214.name(), "物流公司不存在");
		}

		UpdateWrapper wrapper = new UpdateWrapper();
		wrapper.eq("id",id);
		model.setDisabled(disabled);
		logisticsCompanyMapper.updateById(model);

		//如果是禁用操作，需要将商家关联的物流公司信息删除掉
		if (disabled.equals(LogiCompanyStatusEnum.CLOSE.name())) {
			shopLogisticsCompanyClient.deleteByLogisticsId(id);
		}
	}

	/**
	 * 查询平台添加的全部物流公司（正常使用未删除的）
	 * @return
	 */
	@Override
	public List<LogisticsCompanyDO> listAllNormal() {

		QueryWrapper<LogisticsCompanyDO> wrapper = new QueryWrapper<>();
		wrapper.eq("delete_status", DeleteStatusEnum.NORMAL.value()).eq("disabled", LogiCompanyStatusEnum.OPEN.value());
		wrapper.orderByDesc("id");
		return logisticsCompanyMapper.selectList(wrapper);
	}
}
