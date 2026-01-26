 package com.service.impl;

  import org.springframework.stereotype.Service;
  import java.util.Map;
  import java.util.List;

  import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
  import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
  import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
  import com.utils.PageUtils;
  import com.utils.Query;

  import com.dao.CoachDao;
  import com.entity.CoachEntity;
  import com.service.CoachService;
  import com.entity.vo.CoachVO;
  import com.entity.view.CoachView;

  @Service("coachService")
  public class CoachServiceImpl extends ServiceImpl<CoachDao, CoachEntity> implements CoachService {

      @Override
      public PageUtils queryPage(Map<String, Object> params) {
          Page<CoachEntity> page = this.page(
                  new Query<CoachEntity>(params).getPage(),
                  new QueryWrapper<CoachEntity>()
          );
          return new PageUtils(page);
      }

      @Override
      public PageUtils queryPage(Map<String, Object> params, QueryWrapper<CoachEntity> wrapper) {
          Page<CoachView> page = new Query<CoachView>(params).getPage();
          page.setRecords(baseMapper.selectListView(page, wrapper));
          PageUtils pageUtil = new PageUtils(page);
          return pageUtil;
      }


      @Override
      public List<CoachVO> selectListVO(QueryWrapper<CoachEntity> wrapper) {
          return baseMapper.selectListVO(wrapper);
      }

      @Override
      public CoachVO selectVO(QueryWrapper<CoachEntity> wrapper) {
          return baseMapper.selectVO(wrapper);
      }

      @Override
      public List<CoachView> selectListView(QueryWrapper<CoachEntity> wrapper) {
          return baseMapper.selectListView(wrapper);
      }

      @Override
      public CoachView selectView(QueryWrapper<CoachEntity> wrapper) {
          return baseMapper.selectView(wrapper);
      }

  }
