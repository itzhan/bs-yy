 package com.service.impl;

  import org.springframework.stereotype.Service;
  import java.util.Map;
  import java.util.List;

  import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
  import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
  import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
  import com.utils.PageUtils;
  import com.utils.Query;

  import com.dao.DiscussshareDao;
  import com.entity.DiscussshareEntity;
  import com.service.DiscussshareService;
  import com.entity.vo.DiscussshareVO;
  import com.entity.view.DiscussshareView;

  @Service("discussshareService")
  public class DiscussshareServiceImpl extends ServiceImpl<DiscussshareDao, DiscussshareEntity> implements DiscussshareService {

      @Override
      public PageUtils queryPage(Map<String, Object> params) {
          Page<DiscussshareEntity> page = this.page(
                  new Query<DiscussshareEntity>(params).getPage(),
                  new QueryWrapper<DiscussshareEntity>()
          );
          return new PageUtils(page);
      }

      @Override
      public PageUtils queryPage(Map<String, Object> params, QueryWrapper<DiscussshareEntity> wrapper) {
          Page<DiscussshareView> page = new Query<DiscussshareView>(params).getPage();
          page.setRecords(baseMapper.selectListView(page, wrapper));
          PageUtils pageUtil = new PageUtils(page);
          return pageUtil;
      }


      @Override
      public List<DiscussshareVO> selectListVO(QueryWrapper<DiscussshareEntity> wrapper) {
          return baseMapper.selectListVO(wrapper);
      }

      @Override
      public DiscussshareVO selectVO(QueryWrapper<DiscussshareEntity> wrapper) {
          return baseMapper.selectVO(wrapper);
      }

      @Override
      public List<DiscussshareView> selectListView(QueryWrapper<DiscussshareEntity> wrapper) {
          return baseMapper.selectListView(wrapper);
      }

      @Override
      public DiscussshareView selectView(QueryWrapper<DiscussshareEntity> wrapper) {
          return baseMapper.selectView(wrapper);
      }

  }
