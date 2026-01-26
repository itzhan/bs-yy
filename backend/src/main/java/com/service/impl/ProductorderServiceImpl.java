 package com.service.impl;

  import org.springframework.stereotype.Service;
  import java.util.Map;
  import java.util.List;

  import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
  import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
  import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
  import com.utils.PageUtils;
  import com.utils.Query;

  import com.dao.ProductorderDao;
  import com.entity.ProductorderEntity;
  import com.service.ProductorderService;
  import com.entity.vo.ProductorderVO;
  import com.entity.view.ProductorderView;

  @Service("productorderService")
  public class ProductorderServiceImpl extends ServiceImpl<ProductorderDao, ProductorderEntity> implements ProductorderService {

      @Override
      public PageUtils queryPage(Map<String, Object> params) {
          Page<ProductorderEntity> page = this.page(
                  new Query<ProductorderEntity>(params).getPage(),
                  new QueryWrapper<ProductorderEntity>()
          );
          return new PageUtils(page);
      }

      @Override
      public PageUtils queryPage(Map<String, Object> params, QueryWrapper<ProductorderEntity> wrapper) {
          Page<ProductorderView> page = new Query<ProductorderView>(params).getPage();
          page.setRecords(baseMapper.selectListView(page, wrapper));
          PageUtils pageUtil = new PageUtils(page);
          return pageUtil;
      }


      @Override
      public List<ProductorderVO> selectListVO(QueryWrapper<ProductorderEntity> wrapper) {
          return baseMapper.selectListVO(wrapper);
      }

      @Override
      public ProductorderVO selectVO(QueryWrapper<ProductorderEntity> wrapper) {
          return baseMapper.selectVO(wrapper);
      }

      @Override
      public List<ProductorderView> selectListView(QueryWrapper<ProductorderEntity> wrapper) {
          return baseMapper.selectListView(wrapper);
      }

      @Override
      public ProductorderView selectView(QueryWrapper<ProductorderEntity> wrapper) {
          return baseMapper.selectView(wrapper);
      }

  }
