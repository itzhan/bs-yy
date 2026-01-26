 package com.service.impl;

  import org.springframework.stereotype.Service;
  import java.util.Map;
  import java.util.List;

  import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
  import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
  import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
  import com.utils.PageUtils;
  import com.utils.Query;

  import com.dao.ShareDao;
  import com.entity.ShareEntity;
  import com.service.ShareService;
  import com.entity.vo.ShareVO;
  import com.entity.view.ShareView;

  @Service("shareService")
  public class ShareServiceImpl extends ServiceImpl<ShareDao, ShareEntity> implements ShareService {

      @Override
      public PageUtils queryPage(Map<String, Object> params) {
          Page<ShareEntity> page = this.page(
                  new Query<ShareEntity>(params).getPage(),
                  new QueryWrapper<ShareEntity>()
          );
          return new PageUtils(page);
      }

      @Override
      public PageUtils queryPage(Map<String, Object> params, QueryWrapper<ShareEntity> wrapper) {
          Page<ShareView> page = new Query<ShareView>(params).getPage();
          page.setRecords(baseMapper.selectListView(page, wrapper));
          PageUtils pageUtil = new PageUtils(page);
          return pageUtil;
      }


      @Override
      public List<ShareVO> selectListVO(QueryWrapper<ShareEntity> wrapper) {
          return baseMapper.selectListVO(wrapper);
      }

      @Override
      public ShareVO selectVO(QueryWrapper<ShareEntity> wrapper) {
          return baseMapper.selectVO(wrapper);
      }

      @Override
      public List<ShareView> selectListView(QueryWrapper<ShareEntity> wrapper) {
          return baseMapper.selectListView(wrapper);
      }

      @Override
      public ShareView selectView(QueryWrapper<ShareEntity> wrapper) {
          return baseMapper.selectView(wrapper);
      }

  }
