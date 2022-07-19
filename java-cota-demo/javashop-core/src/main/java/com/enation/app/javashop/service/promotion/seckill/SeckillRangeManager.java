package com.enation.app.javashop.service.promotion.seckill;

import com.enation.app.javashop.framework.database.WebPage;
import com.enation.app.javashop.model.promotion.seckill.dos.SeckillRangeDO;
import com.enation.app.javashop.model.promotion.seckill.vo.TimeLineVO;

import java.util.List;

/**
 * 限时抢购时刻业务层
 * @author Snow
 * @version v2.0.0
 * @since v7.0.0
 * 2018-04-02 18:24:47
 */
public interface SeckillRangeManager	{

	/**
	 * 查询限时抢购时刻分页列表数据
	 * @param page 页码
	 * @param pageSize 每页数量
	 * @return WebPage
	 */
	WebPage list(long page, long pageSize);

	/**
	* 修改限时抢购时刻
	* @param seckillRange 限时抢购时刻
	* @param id 限时抢购时刻主键
	* @return SeckillRange 限时抢购时刻
	*/
	SeckillRangeDO edit(SeckillRangeDO seckillRange, Long id);

	/**
	 * 删除限时抢购时刻
	 * @param id 限时抢购时刻主键
	 */
	void delete(Long id);

	/**
	 * 获取限时抢购时刻
	 * @param id 限时抢购时刻主键
	 * @return SeckillRange  限时抢购时刻
	 */
	SeckillRangeDO getModel(Long id);

	/**
	 * 根据时刻的集合，入库
	 * @param list 限时抢购活动时刻集合信息
	 * @param seckillId 限时抢购活动ID
	 */
	void addList(List<Integer> list, Long seckillId);

	/**
	 * 根据限时抢购活动ID，读取此时刻集合
	 * @param seckillId 限时抢购活动ID
	 * @return
	 */
	List<SeckillRangeDO> getList(Long seckillId);

	/**
	 * 读取当期限时抢购时刻信息集合
	 * @return
	 */
	List<TimeLineVO> readTimeList();

}
