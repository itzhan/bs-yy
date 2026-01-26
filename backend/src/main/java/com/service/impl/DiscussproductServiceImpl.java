 package com.service.impl;

  import org.springframework.stereotype.Service;
  import java.util.Map;
  import java.util.List;

  import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
  import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
  import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
  import com.utils.PageUtils;
  import com.utils.Query;

  import com.dao.DiscussproductDao;
  import com.entity.DiscussproductEntity;
  import com.service.DiscussproductService;
  import com.entity.vo.DiscussproductVO;
  import com.entity.view.DiscussproductView;

  @Service("discussproductService")
  public class DiscussproductServiceImpl extends ServiceImpl<DiscussproductDao, DiscussproductEntity> implements DiscussproductService {

      @Override
      public PageUtils queryPage(Map<String, Object> params) {
          Page<DiscussproductEntity> page = this.page(
                  new Query<DiscussproductEntity>(params).getPage(),
                  new QueryWrapper<DiscussproductEntity>()
          );
          return new PageUtils(page);
      }

      @Override
      public PageUtils queryPage(Map<String, Object> params, QueryWrapper<DiscussproductEntity> wrapper) {
          Page<DiscussproductView> page = new Query<DiscussproductView>(params).getPage();
          page.setRecords(baseMapper.selectListView(page, wrapper));
          PageUtils pageUtil = new PageUtils(page);
          return pageUtil;
      }


      @Override
      public List<DiscussproductVO> selectListVO(QueryWrapper<DiscussproductEntity> wrapper) {
          return baseMapper.selectListVO(wrapper);
      }

      @Override
      public DiscussproductVO selectVO(QueryWrapper<DiscussproductEntity> wrapper) {
          return baseMapper.selectVO(wrapper);
      }

      @Override
      public List<DiscussproductView> selectListView(QueryWrapper<DiscussproductEntity> wrapper) {
          return baseMapper.selectListView(wrapper);
      }

      @Override
      public DiscussproductView selectView(QueryWrapper<DiscussproductEntity> wrapper) {
          return baseMapper.selectView(wrapper);
      }

  }
