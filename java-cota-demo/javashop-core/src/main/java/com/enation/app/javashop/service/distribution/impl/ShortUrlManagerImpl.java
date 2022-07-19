package com.enation.app.javashop.service.distribution.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.enation.app.javashop.mapper.distribution.ShortUrlMapper;
import com.enation.app.javashop.model.distribution.dos.ShortUrlDO;
import com.enation.app.javashop.service.distribution.ShortUrlManager;
import com.enation.app.javashop.util.ShortUrlGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 短链接实现类
 * @author Chopper
 * @version v1.0
 * @since v7.0
 * 2018/6/14 上午7:13
 * @Description:
 *
 */

@Component
public class ShortUrlManagerImpl implements ShortUrlManager {

    @Autowired
    private ShortUrlMapper shortUrlMapper;

    /**
     * 生成一个短链接
     * @param memberId 会员id
     * @param goodsId 商品id
     * @return 短链接
     */
    @Override
    public ShortUrlDO createShortUrl(Long memberId, Long goodsId) {
        ShortUrlDO shortUrlDO = null;

        if (goodsId == null) {
            goodsId = 0L;
        }

        //设置跳转地址
        String url;
        if (goodsId == 0) {
            url = "/index.html?member_id=" + memberId;
        } else {
            url = "/goods/" + goodsId + "?member_id=" + memberId;
        }

        //根据跳转地址查询短链接
        QueryWrapper<ShortUrlDO> wrapper = new QueryWrapper<>();
        wrapper.eq("url", url);
        ShortUrlDO result = shortUrlMapper.selectOne(wrapper);

        if (result != null) {
            return result;
        }

        //生成短链接
        String[] shortUrls = ShortUrlGenerator.getShortUrl(url);

        //检测是否存在
        for (String tempUrl : shortUrls) {

            QueryWrapper<ShortUrlDO> wrapperr = new QueryWrapper<>();
            wrapperr.eq("su",tempUrl);
            int num = shortUrlMapper.selectCount(wrapperr);

            //如果不存在，则新插入一个短链接
            if (num == 0) {
                shortUrlDO = new ShortUrlDO();
                shortUrlDO.setSu(tempUrl);
                shortUrlDO.setUrl(url);

                shortUrlMapper.insert(shortUrlDO);
                break;
            }
        }

        return shortUrlDO;
    }

    /**
     * 根据短链接获得长链接
     * @param shortUrl 短链接 （可带前缀 即：http:xxx/）
     * @return 所对应的长链接
     */
    @Override
    public ShortUrlDO getLongUrl(String shortUrl) {
        QueryWrapper<ShortUrlDO> wrapperr = new QueryWrapper<>();
        wrapperr.eq("su",shortUrl);
        return shortUrlMapper.selectOne(wrapperr);
    }

}
