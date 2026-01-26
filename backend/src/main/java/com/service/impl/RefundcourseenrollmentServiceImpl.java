 package com.service.impl;

  import org.springframework.stereotype.Service;
  import java.util.Map;
  import java.util.List;

  import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
  import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
  import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
  import com.utils.PageUtils;
  import com.utils.Query;

  import com.dao.RefundcourseenrollmentDao;
  import com.entity.RefundcourseenrollmentEntity;
  import com.service.RefundcourseenrollmentService;
  import com.entity.vo.RefundcourseenrollmentVO;
  import com.entity.view.RefundcourseenrollmentView;

  @Service("refundcourseenrollmentService")
  public class RefundcourseenrollmentServiceImpl extends ServiceImpl<RefundcourseenrollmentDao, RefundcourseenrollmentEntity> implements RefundcourseenrollmentService {

      @Override
      public PageUtils queryPage(Map<String, Object> params) {
          Page<RefundcourseenrollmentEntity> page = this.page(
                  new Query<RefundcourseenrollmentEntity>(params).getPage(),
                  new QueryWrapper<RefundcourseenrollmentEntity>()
          );
          return new PageUtils(page);
      }

      @Override
      public PageUtils queryPage(Map<String, Object> params, QueryWrapper<RefundcourseenrollmentEntity> wrapper) {
          Page<RefundcourseenrollmentView> page = new Query<RefundcourseenrollmentView>(params).getPage();
          page.setRecords(baseMapper.selectListView(page, wrapper));
          PageUtils pageUtil = new PageUtils(page);
          return pageUtil;
      }


      @Override
      public List<RefundcourseenrollmentVO> selectListVO(QueryWrapper<RefundcourseenrollmentEntity> wrapper) {
          return baseMapper.selectListVO(wrapper);
      }

      @Override
      public RefundcourseenrollmentVO selectVO(QueryWrapper<RefundcourseenrollmentEntity> wrapper) {
          return baseMapper.selectVO(wrapper);
      }

      @Override
      public List<RefundcourseenrollmentView> selectListView(QueryWrapper<RefundcourseenrollmentEntity> wrapper) {
          return baseMapper.selectListView(wrapper);
      }

      @Override
      public RefundcourseenrollmentView selectView(QueryWrapper<RefundcourseenrollmentEntity> wrapper) {
          return baseMapper.selectView(wrapper);
      }

  }
