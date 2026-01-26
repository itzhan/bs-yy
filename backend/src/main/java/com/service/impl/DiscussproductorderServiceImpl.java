 package com.service.impl;

  import org.springframework.stereotype.Service;
  import java.util.Map;
  import java.util.List;

  import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
  import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
  import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
  import com.utils.PageUtils;
  import com.utils.Query;

  import com.dao.DiscussproductorderDao;
  import com.entity.DiscussproductorderEntity;
  import com.service.DiscussproductorderService;
  import com.entity.vo.DiscussproductorderVO;
  import com.entity.view.DiscussproductorderView;

  @Service("discussproductorderService")
  public class DiscussproductorderServiceImpl extends ServiceImpl<DiscussproductorderDao, DiscussproductorderEntity> implements DiscussproductorderService {

      @Override
      public PageUtils queryPage(Map<String, Object> params) {
          Page<DiscussproductorderEntity> page = this.page(
                  new Query<DiscussproductorderEntity>(params).getPage(),
                  new QueryWrapper<DiscussproductorderEntity>()
          );
          return new PageUtils(page);
      }

      @Override
      public PageUtils queryPage(Map<String, Object> params, QueryWrapper<DiscussproductorderEntity> wrapper) {
          Page<DiscussproductorderView> page = new Query<DiscussproductorderView>(params).getPage();
          page.setRecords(baseMapper.selectListView(page, wrapper));
          PageUtils pageUtil = new PageUtils(page);
          return pageUtil;
      }


      @Override
      public List<DiscussproductorderVO> selectListVO(QueryWrapper<DiscussproductorderEntity> wrapper) {
          return baseMapper.selectListVO(wrapper);
      }

      @Override
      public DiscussproductorderVO selectVO(QueryWrapper<DiscussproductorderEntity> wrapper) {
          return baseMapper.selectVO(wrapper);
      }

      @Override
      public List<DiscussproductorderView> selectListView(QueryWrapper<DiscussproductorderEntity> wrapper) {
          return baseMapper.selectListView(wrapper);
      }

      @Override
      public DiscussproductorderView selectView(QueryWrapper<DiscussproductorderEntity> wrapper) {
          return baseMapper.selectView(wrapper);
      }

  }
