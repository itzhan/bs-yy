 package com.service.impl;

  import org.springframework.stereotype.Service;
  import java.util.Map;
  import java.util.List;

  import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
  import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
  import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
  import com.utils.PageUtils;
  import com.utils.Query;

  import com.dao.DiscussnewsDao;
  import com.entity.DiscussnewsEntity;
  import com.service.DiscussnewsService;
  import com.entity.vo.DiscussnewsVO;
  import com.entity.view.DiscussnewsView;

  @Service("discussnewsService")
  public class DiscussnewsServiceImpl extends ServiceImpl<DiscussnewsDao, DiscussnewsEntity> implements DiscussnewsService {

      @Override
      public PageUtils queryPage(Map<String, Object> params) {
          Page<DiscussnewsEntity> page = this.page(
                  new Query<DiscussnewsEntity>(params).getPage(),
                  new QueryWrapper<DiscussnewsEntity>()
          );
          return new PageUtils(page);
      }

      @Override
      public PageUtils queryPage(Map<String, Object> params, QueryWrapper<DiscussnewsEntity> wrapper) {
          Page<DiscussnewsView> page = new Query<DiscussnewsView>(params).getPage();
          page.setRecords(baseMapper.selectListView(page, wrapper));
          PageUtils pageUtil = new PageUtils(page);
          return pageUtil;
      }


      @Override
      public List<DiscussnewsVO> selectListVO(QueryWrapper<DiscussnewsEntity> wrapper) {
          return baseMapper.selectListVO(wrapper);
      }

      @Override
      public DiscussnewsVO selectVO(QueryWrapper<DiscussnewsEntity> wrapper) {
          return baseMapper.selectVO(wrapper);
      }

      @Override
      public List<DiscussnewsView> selectListView(QueryWrapper<DiscussnewsEntity> wrapper) {
          return baseMapper.selectListView(wrapper);
      }

      @Override
      public DiscussnewsView selectView(QueryWrapper<DiscussnewsEntity> wrapper) {
          return baseMapper.selectView(wrapper);
      }

  }
