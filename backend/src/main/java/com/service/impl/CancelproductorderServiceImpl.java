 package com.service.impl;

  import org.springframework.stereotype.Service;
  import java.util.Map;
  import java.util.List;

  import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
  import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
  import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
  import com.utils.PageUtils;
  import com.utils.Query;

  import com.dao.CancelproductorderDao;
  import com.entity.CancelproductorderEntity;
  import com.service.CancelproductorderService;
  import com.entity.vo.CancelproductorderVO;
  import com.entity.view.CancelproductorderView;

  @Service("cancelproductorderService")
  public class CancelproductorderServiceImpl extends ServiceImpl<CancelproductorderDao, CancelproductorderEntity> implements CancelproductorderService {

      @Override
      public PageUtils queryPage(Map<String, Object> params) {
          Page<CancelproductorderEntity> page = this.page(
                  new Query<CancelproductorderEntity>(params).getPage(),
                  new QueryWrapper<CancelproductorderEntity>()
          );
          return new PageUtils(page);
      }

      @Override
      public PageUtils queryPage(Map<String, Object> params, QueryWrapper<CancelproductorderEntity> wrapper) {
          Page<CancelproductorderView> page = new Query<CancelproductorderView>(params).getPage();
          page.setRecords(baseMapper.selectListView(page, wrapper));
          PageUtils pageUtil = new PageUtils(page);
          return pageUtil;
      }


      @Override
      public List<CancelproductorderVO> selectListVO(QueryWrapper<CancelproductorderEntity> wrapper) {
          return baseMapper.selectListVO(wrapper);
      }

      @Override
      public CancelproductorderVO selectVO(QueryWrapper<CancelproductorderEntity> wrapper) {
          return baseMapper.selectVO(wrapper);
      }

      @Override
      public List<CancelproductorderView> selectListView(QueryWrapper<CancelproductorderEntity> wrapper) {
          return baseMapper.selectListView(wrapper);
      }

      @Override
      public CancelproductorderView selectView(QueryWrapper<CancelproductorderEntity> wrapper) {
          return baseMapper.selectView(wrapper);
      }

  }
