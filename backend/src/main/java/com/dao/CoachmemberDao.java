package com.dao;

import com.entity.CoachmemberEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import java.util.List;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import com.entity.vo.CoachmemberVO;
import com.entity.view.CoachmemberView;

/**
 * 教练-会员绑定
 * 
 * @author ds
 * @email 1312461553@qq.com
 */
public interface CoachmemberDao extends BaseMapper<CoachmemberEntity> {

    List<CoachmemberVO> selectListVO(@Param("ew") QueryWrapper<CoachmemberEntity> wrapper);

    CoachmemberVO selectVO(@Param("ew") QueryWrapper<CoachmemberEntity> wrapper);

    List<CoachmemberView> selectListView(@Param("ew") QueryWrapper<CoachmemberEntity> wrapper);

    List<CoachmemberView> selectListView(Page page, @Param("ew") QueryWrapper<CoachmemberEntity> wrapper);

    CoachmemberView selectView(@Param("ew") QueryWrapper<CoachmemberEntity> wrapper);
}
