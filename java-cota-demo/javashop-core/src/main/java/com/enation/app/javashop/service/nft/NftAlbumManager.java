package com.enation.app.javashop.service.nft;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.enation.app.javashop.framework.exception.ServiceException;
import com.enation.app.javashop.framework.logs.Logger;
import com.enation.app.javashop.framework.logs.LoggerFactory;
import com.enation.app.javashop.framework.util.DateUtil;
import com.enation.app.javashop.mapper.nft.NftAlbumMapper;
import com.enation.app.javashop.mapper.nft.NftMemberMapper;
import com.enation.app.javashop.model.errorcode.MemberErrorCode;
import com.enation.app.javashop.model.nft.dos.NftAlbum;
import com.enation.app.javashop.service.member.MemberManager;
import com.enation.app.javashop.service.nft.plugin.CotaNftPlugin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
public class NftAlbumManager {
    /**
     * 日志记录
     */
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private NftAlbumMapper nftAlbumMapper;


    public IPage<Map> albums(Long pageNo, Long pageSize) {
        Page page = new Page();
        page.setCurrent(pageNo);
        page.setSize(pageSize);
        QueryWrapper wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("a.album_id");
        return nftAlbumMapper.pageNftAlbum(page, wrapper);
    }

    public NftAlbum addAlbum(String name, String face, String description, String openTime,Integer tag) {
        NftAlbum album = new NftAlbum();
        if (name != null)
            album.setName(name);
        if (face != null)
            album.setFace(face);
        if (description != null)
            album.setDescription(description);
        if (openTime != null)
            album.setOpenTime(DateUtil.toDate(openTime, "yyyy-MM-dd hh:mm:ss").getTime() / 1000);
        if (tag != null)
            album.setTag(tag);
        album.setCreateTime(System.currentTimeMillis() / 1000);
        album.setIssuerId(2l);//未来
        nftAlbumMapper.insert(album);
        return album;
    }

    public NftAlbum modifyAlbum(String name, String face, String description, String openTime, Long albumId) {
        NftAlbum ret = nftAlbumMapper.selectById(albumId);
        if (name != null)
            ret.setName(name);
        if (face != null)
            ret.setFace(face);
        if (description != null)
            ret.setDescription(description);
        if (openTime != null)
            ret.setOpenTime(DateUtil.toDate(openTime, "yyyy-MM-dd hh:mm:ss").getTime() / 1000);

        nftAlbumMapper.updateById(ret);
        return ret;
    }

    public void deleteAlbums(Long[] ids) {
        //新建修改条件包装器
        QueryWrapper<NftAlbum> wrapper = new QueryWrapper<>();
        wrapper.in("album_id", Arrays.asList(ids))
                .gt("num", 0);

        List<NftAlbum> ms = nftAlbumMapper.selectList(wrapper);
        if (ms.size() > 0)
            throw new ServiceException(MemberErrorCode.E110.code(), "不能删除，已经有专辑定义了收藏品" + Arrays.asList(ids).toString());
        nftAlbumMapper.deleteBatchIds(Arrays.asList(ids));
    }

    public void addOrModify(NftAlbum album) {
        if (album.getAlbumId()==null) {
            nftAlbumMapper.insert(album);
        }else {
            nftAlbumMapper.updateById(album);
        }
    }
}
