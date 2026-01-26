 package com.service.impl;

  import org.springframework.stereotype.Service;
  import java.util.Map;
  import java.util.List;

  import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
  import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
  import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
  import com.utils.PageUtils;
  import com.utils.Query;

  import com.dao.LogDao;
  import com.entity.LogEntity;
  import com.service.LogService;
  import com.entity.vo.LogVO;
  import com.entity.view.LogView;

  @Service("logService")
  public class LogServiceImpl extends ServiceImpl<LogDao, LogEntity> implements LogService {

      @Override
      public PageUtils queryPage(Map<String, Object> params) {
          Page<LogEntity> page = this.page(
                  new Query<LogEntity>(params).getPage(),
                  new QueryWrapper<LogEntity>()
          );
          return new PageUtils(page);
      }

      @Override
      public PageUtils queryPage(Map<String, Object> params, QueryWrapper<LogEntity> wrapper) {
          Page<LogView> page = new Query<LogView>(params).getPage();
          page.setRecords(baseMapper.selectListView(page, wrapper));
          PageUtils pageUtil = new PageUtils(page);
          return pageUtil;
      }


      @Override
      public List<LogVO> selectListVO(QueryWrapper<LogEntity> wrapper) {
          return baseMapper.selectListVO(wrapper);
      }

      @Override
      public LogVO selectVO(QueryWrapper<LogEntity> wrapper) {
          return baseMapper.selectVO(wrapper);
      }

      @Override
      public List<LogView> selectListView(QueryWrapper<LogEntity> wrapper) {
          return baseMapper.selectListView(wrapper);
      }

      @Override
      public LogView selectView(QueryWrapper<LogEntity> wrapper) {
          return baseMapper.selectView(wrapper);
      }

  }
