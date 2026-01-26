 package com.service.impl;

  import org.springframework.stereotype.Service;
  import java.util.Map;
  import java.util.List;

  import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
  import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
  import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
  import com.utils.PageUtils;
  import com.utils.Query;

  import com.dao.CoursetypeDao;
  import com.entity.CoursetypeEntity;
  import com.service.CoursetypeService;
  import com.entity.vo.CoursetypeVO;
  import com.entity.view.CoursetypeView;

  @Service("coursetypeService")
  public class CoursetypeServiceImpl extends ServiceImpl<CoursetypeDao, CoursetypeEntity> implements CoursetypeService {

      @Override
      public PageUtils queryPage(Map<String, Object> params) {
          Page<CoursetypeEntity> page = this.page(
                  new Query<CoursetypeEntity>(params).getPage(),
                  new QueryWrapper<CoursetypeEntity>()
          );
          return new PageUtils(page);
      }

      @Override
      public PageUtils queryPage(Map<String, Object> params, QueryWrapper<CoursetypeEntity> wrapper) {
          Page<CoursetypeView> page = new Query<CoursetypeView>(params).getPage();
          page.setRecords(baseMapper.selectListView(page, wrapper));
          PageUtils pageUtil = new PageUtils(page);
          return pageUtil;
      }


      @Override
      public List<CoursetypeVO> selectListVO(QueryWrapper<CoursetypeEntity> wrapper) {
          return baseMapper.selectListVO(wrapper);
      }

      @Override
      public CoursetypeVO selectVO(QueryWrapper<CoursetypeEntity> wrapper) {
          return baseMapper.selectVO(wrapper);
      }

      @Override
      public List<CoursetypeView> selectListView(QueryWrapper<CoursetypeEntity> wrapper) {
          return baseMapper.selectListView(wrapper);
      }

      @Override
      public CoursetypeView selectView(QueryWrapper<CoursetypeEntity> wrapper) {
          return baseMapper.selectView(wrapper);
      }

  }
