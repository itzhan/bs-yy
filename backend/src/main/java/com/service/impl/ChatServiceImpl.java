 package com.service.impl;

  import org.springframework.stereotype.Service;
  import java.util.Map;
  import java.util.List;

  import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
  import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
  import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
  import com.utils.PageUtils;
  import com.utils.Query;

  import com.dao.ChatDao;
  import com.entity.ChatEntity;
  import com.service.ChatService;
  import com.entity.vo.ChatVO;
  import com.entity.view.ChatView;

  @Service("chatService")
  public class ChatServiceImpl extends ServiceImpl<ChatDao, ChatEntity> implements ChatService {

      @Override
      public PageUtils queryPage(Map<String, Object> params) {
          Page<ChatEntity> page = this.page(
                  new Query<ChatEntity>(params).getPage(),
                  new QueryWrapper<ChatEntity>()
          );
          return new PageUtils(page);
      }

      @Override
      public PageUtils queryPage(Map<String, Object> params, QueryWrapper<ChatEntity> wrapper) {
          Page<ChatView> page = new Query<ChatView>(params).getPage();
          page.setRecords(baseMapper.selectListView(page, wrapper));
          PageUtils pageUtil = new PageUtils(page);
          return pageUtil;
      }


      @Override
      public List<ChatVO> selectListVO(QueryWrapper<ChatEntity> wrapper) {
          return baseMapper.selectListVO(wrapper);
      }

      @Override
      public ChatVO selectVO(QueryWrapper<ChatEntity> wrapper) {
          return baseMapper.selectVO(wrapper);
      }

      @Override
      public List<ChatView> selectListView(QueryWrapper<ChatEntity> wrapper) {
          return baseMapper.selectListView(wrapper);
      }

      @Override
      public ChatView selectView(QueryWrapper<ChatEntity> wrapper) {
          return baseMapper.selectView(wrapper);
      }

  }
