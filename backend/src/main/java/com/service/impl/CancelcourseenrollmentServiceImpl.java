 package com.service.impl;

  import org.springframework.stereotype.Service;
  import java.util.Map;
  import java.util.List;

  import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
  import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
  import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
  import com.utils.PageUtils;
  import com.utils.Query;

  import com.dao.CancelcourseenrollmentDao;
  import com.entity.CancelcourseenrollmentEntity;
  import com.service.CancelcourseenrollmentService;
  import com.entity.vo.CancelcourseenrollmentVO;
  import com.entity.view.CancelcourseenrollmentView;

  @Service("cancelcourseenrollmentService")
  public class CancelcourseenrollmentServiceImpl extends ServiceImpl<CancelcourseenrollmentDao, CancelcourseenrollmentEntity> implements CancelcourseenrollmentService {

      @Override
      public PageUtils queryPage(Map<String, Object> params) {
          Page<CancelcourseenrollmentEntity> page = this.page(
                  new Query<CancelcourseenrollmentEntity>(params).getPage(),
                  new QueryWrapper<CancelcourseenrollmentEntity>()
          );
          return new PageUtils(page);
      }

      @Override
      public PageUtils queryPage(Map<String, Object> params, QueryWrapper<CancelcourseenrollmentEntity> wrapper) {
          Page<CancelcourseenrollmentView> page = new Query<CancelcourseenrollmentView>(params).getPage();
          page.setRecords(baseMapper.selectListView(page, wrapper));
          PageUtils pageUtil = new PageUtils(page);
          return pageUtil;
      }


      @Override
      public List<CancelcourseenrollmentVO> selectListVO(QueryWrapper<CancelcourseenrollmentEntity> wrapper) {
          return baseMapper.selectListVO(wrapper);
      }

      @Override
      public CancelcourseenrollmentVO selectVO(QueryWrapper<CancelcourseenrollmentEntity> wrapper) {
          return baseMapper.selectVO(wrapper);
      }

      @Override
      public List<CancelcourseenrollmentView> selectListView(QueryWrapper<CancelcourseenrollmentEntity> wrapper) {
          return baseMapper.selectListView(wrapper);
      }

      @Override
      public CancelcourseenrollmentView selectView(QueryWrapper<CancelcourseenrollmentEntity> wrapper) {
          return baseMapper.selectView(wrapper);
      }

  }
