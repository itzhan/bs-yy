 package com.service.impl;

  import org.springframework.stereotype.Service;
  import java.util.Map;
  import java.util.List;

  import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
  import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
  import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
  import com.utils.PageUtils;
  import com.utils.Query;

  import com.dao.ProducttypeDao;
  import com.entity.ProducttypeEntity;
  import com.service.ProducttypeService;
  import com.entity.vo.ProducttypeVO;
  import com.entity.view.ProducttypeView;

  @Service("producttypeService")
  public class ProducttypeServiceImpl extends ServiceImpl<ProducttypeDao, ProducttypeEntity> implements ProducttypeService {

      @Override
      public PageUtils queryPage(Map<String, Object> params) {
          Page<ProducttypeEntity> page = this.page(
                  new Query<ProducttypeEntity>(params).getPage(),
                  new QueryWrapper<ProducttypeEntity>()
          );
          return new PageUtils(page);
      }

      @Override
      public PageUtils queryPage(Map<String, Object> params, QueryWrapper<ProducttypeEntity> wrapper) {
          Page<ProducttypeView> page = new Query<ProducttypeView>(params).getPage();
          page.setRecords(baseMapper.selectListView(page, wrapper));
          PageUtils pageUtil = new PageUtils(page);
          return pageUtil;
      }


      @Override
      public List<ProducttypeVO> selectListVO(QueryWrapper<ProducttypeEntity> wrapper) {
          return baseMapper.selectListVO(wrapper);
      }

      @Override
      public ProducttypeVO selectVO(QueryWrapper<ProducttypeEntity> wrapper) {
          return baseMapper.selectVO(wrapper);
      }

      @Override
      public List<ProducttypeView> selectListView(QueryWrapper<ProducttypeEntity> wrapper) {
          return baseMapper.selectListView(wrapper);
      }

      @Override
      public ProducttypeView selectView(QueryWrapper<ProducttypeEntity> wrapper) {
          return baseMapper.selectView(wrapper);
      }

  }
