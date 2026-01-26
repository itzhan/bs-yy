 package com.service.impl;

  import org.springframework.stereotype.Service;
  import java.util.Map;
  import java.util.List;

  import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
  import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
  import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
  import com.utils.PageUtils;
  import com.utils.Query;

  import com.dao.NotifyDao;
  import com.entity.NotifyEntity;
  import com.service.NotifyService;
  import com.entity.vo.NotifyVO;
  import com.entity.view.NotifyView;

  @Service("notifyService")
  public class NotifyServiceImpl extends ServiceImpl<NotifyDao, NotifyEntity> implements NotifyService {

      @Override
      public PageUtils queryPage(Map<String, Object> params) {
          Page<NotifyEntity> page = this.page(
                  new Query<NotifyEntity>(params).getPage(),
                  new QueryWrapper<NotifyEntity>()
          );
          return new PageUtils(page);
      }

      @Override
      public PageUtils queryPage(Map<String, Object> params, QueryWrapper<NotifyEntity> wrapper) {
          Page<NotifyView> page = new Query<NotifyView>(params).getPage();
          page.setRecords(baseMapper.selectListView(page, wrapper));
          PageUtils pageUtil = new PageUtils(page);
          return pageUtil;
      }


      @Override
      public List<NotifyVO> selectListVO(QueryWrapper<NotifyEntity> wrapper) {
          return baseMapper.selectListVO(wrapper);
      }

      @Override
      public NotifyVO selectVO(QueryWrapper<NotifyEntity> wrapper) {
          return baseMapper.selectVO(wrapper);
      }

      @Override
      public List<NotifyView> selectListView(QueryWrapper<NotifyEntity> wrapper) {
          return baseMapper.selectListView(wrapper);
      }

      @Override
      public NotifyView selectView(QueryWrapper<NotifyEntity> wrapper) {
          return baseMapper.selectView(wrapper);
      }

  }
