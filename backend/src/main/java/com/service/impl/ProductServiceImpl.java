 package com.service.impl;

  import org.springframework.stereotype.Service;
  import java.util.Map;
  import java.util.List;

  import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
  import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
  import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
  import com.utils.PageUtils;
  import com.utils.Query;

  import com.dao.ProductDao;
  import com.entity.ProductEntity;
  import com.service.ProductService;
  import com.entity.vo.ProductVO;
  import com.entity.view.ProductView;

  @Service("productService")
  public class ProductServiceImpl extends ServiceImpl<ProductDao, ProductEntity> implements ProductService {

      @Override
      public PageUtils queryPage(Map<String, Object> params) {
          Page<ProductEntity> page = this.page(
                  new Query<ProductEntity>(params).getPage(),
                  new QueryWrapper<ProductEntity>()
          );
          return new PageUtils(page);
      }

      @Override
      public PageUtils queryPage(Map<String, Object> params, QueryWrapper<ProductEntity> wrapper) {
          Page<ProductView> page = new Query<ProductView>(params).getPage();
          page.setRecords(baseMapper.selectListView(page, wrapper));
          PageUtils pageUtil = new PageUtils(page);
          return pageUtil;
      }


      @Override
      public List<ProductVO> selectListVO(QueryWrapper<ProductEntity> wrapper) {
          return baseMapper.selectListVO(wrapper);
      }

      @Override
      public ProductVO selectVO(QueryWrapper<ProductEntity> wrapper) {
          return baseMapper.selectVO(wrapper);
      }

      @Override
      public List<ProductView> selectListView(QueryWrapper<ProductEntity> wrapper) {
          return baseMapper.selectListView(wrapper);
      }

      @Override
      public ProductView selectView(QueryWrapper<ProductEntity> wrapper) {
          return baseMapper.selectView(wrapper);
      }

  }
