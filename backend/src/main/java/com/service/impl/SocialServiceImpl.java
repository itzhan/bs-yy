 package com.service.impl;

  import org.springframework.stereotype.Service;
  import java.util.Map;
  import java.util.List;

  import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
  import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
  import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
  import com.utils.PageUtils;
  import com.utils.Query;

  import com.dao.SocialDao;
  import com.entity.SocialEntity;
  import com.service.SocialService;
  import com.entity.vo.SocialVO;
  import com.entity.view.SocialView;

  @Service("socialService")
  public class SocialServiceImpl extends ServiceImpl<SocialDao, SocialEntity> implements SocialService {

      @Override
      public PageUtils queryPage(Map<String, Object> params) {
          Page<SocialEntity> page = this.page(
                  new Query<SocialEntity>(params).getPage(),
                  new QueryWrapper<SocialEntity>()
          );
          return new PageUtils(page);
      }

      @Override
      public PageUtils queryPage(Map<String, Object> params, QueryWrapper<SocialEntity> wrapper) {
          Page<SocialView> page = new Query<SocialView>(params).getPage();
          page.setRecords(baseMapper.selectListView(page, wrapper));
          PageUtils pageUtil = new PageUtils(page);
          return pageUtil;
      }

      @Override
      public List<SocialVO> selectListVO(QueryWrapper<SocialEntity> wrapper) {
          return baseMapper.selectListVO(wrapper);
      }

      @Override
      public SocialVO selectVO(QueryWrapper<SocialEntity> wrapper) {
          return baseMapper.selectVO(wrapper);
      }

      @Override
      public List<SocialView> selectListView(QueryWrapper<SocialEntity> wrapper) {
          return baseMapper.selectListView(wrapper);
      }

      @Override
      public SocialView selectView(QueryWrapper<SocialEntity> wrapper) {
          return baseMapper.selectView(wrapper);
      }

  }