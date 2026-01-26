 package com.service.impl;

  import org.springframework.stereotype.Service;
  import java.util.Map;
  import java.util.List;

  import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
  import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
  import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
  import com.utils.PageUtils;
  import com.utils.Query;

  import com.dao.CardrenewalDao;
  import com.entity.CardrenewalEntity;
  import com.service.CardrenewalService;
  import com.entity.vo.CardrenewalVO;
  import com.entity.view.CardrenewalView;

  @Service("cardrenewalService")
  public class CardrenewalServiceImpl extends ServiceImpl<CardrenewalDao, CardrenewalEntity> implements CardrenewalService {

      @Override
      public PageUtils queryPage(Map<String, Object> params) {
          Page<CardrenewalEntity> page = this.page(
                  new Query<CardrenewalEntity>(params).getPage(),
                  new QueryWrapper<CardrenewalEntity>()
          );
          return new PageUtils(page);
      }

      @Override
      public PageUtils queryPage(Map<String, Object> params, QueryWrapper<CardrenewalEntity> wrapper) {
          Page<CardrenewalView> page = new Query<CardrenewalView>(params).getPage();
          page.setRecords(baseMapper.selectListView(page, wrapper));
          PageUtils pageUtil = new PageUtils(page);
          return pageUtil;
      }


      @Override
      public List<CardrenewalVO> selectListVO(QueryWrapper<CardrenewalEntity> wrapper) {
          return baseMapper.selectListVO(wrapper);
      }

      @Override
      public CardrenewalVO selectVO(QueryWrapper<CardrenewalEntity> wrapper) {
          return baseMapper.selectVO(wrapper);
      }

      @Override
      public List<CardrenewalView> selectListView(QueryWrapper<CardrenewalEntity> wrapper) {
          return baseMapper.selectListView(wrapper);
      }

      @Override
      public CardrenewalView selectView(QueryWrapper<CardrenewalEntity> wrapper) {
          return baseMapper.selectView(wrapper);
      }

  }
