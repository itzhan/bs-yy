 package com.controller;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Map;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Date;
import java.util.List;
import java.util.Collections;
import java.util.Set;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.IdUtil;

import java.util.stream.Collectors;
import jakarta.servlet.http.HttpServletRequest;
import com.utils.ValidatorUtils;
import com.utils.DeSensUtil;
import org.springframework.format.annotation.DateTimeFormat;
import jakarta.annotation.Resource;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.annotation.IgnoreAuth;

import com.entity.CancelproductorderEntity;
import com.entity.view.CancelproductorderView;

import com.service.CancelproductorderService;
import com.utils.PageUtils;
import com.utils.R;
import com.utils.MPUtil;
import com.utils.MapUtils;
import java.io.IOException;
import com.log.OperationLogRecorder;

/**
* 取消商品订单
* 后端接口
* @author ds
* @email 1312461553@qq.com
* @date 2025-11-13 21:24:29
*/
@RestController
@RequestMapping("/cancelproductorder")
public class CancelproductorderController {
    @Resource
    private CancelproductorderService cancelproductorderService;
    @Resource
    private OperationLogRecorder operationLogRecorder;

    /**
    * 后台列表
    */
    @RequestMapping("/page")
    public R page(@RequestParam Map<String, Object> params, CancelproductorderEntity cancelproductorder,
            HttpServletRequest request){
        if(!request.getSession().getAttribute("role").toString().equals("管理员")) {
            cancelproductorder.setUserid((Long)request.getSession().getAttribute("userId"));
        }
        String tableName = request.getSession().getAttribute("tableName").toString();
        String username = (String)request.getSession().getAttribute("username");
        QueryWrapper<CancelproductorderEntity> ew = new QueryWrapper<CancelproductorderEntity>();
        PageUtils page = cancelproductorderService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, cancelproductorder), params), params));
        Map<String, String> deSens = new HashMap<>();
        DeSensUtil.desensitize(page,deSens);
        return R.ok().put("data", page);
    }

    /**
    * 前台列表
    */
    @IgnoreAuth
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params, CancelproductorderEntity cancelproductorder,
            HttpServletRequest request){
//        if(!request.getSession().getAttribute("role").toString().equals("管理员")) {
//            cancelproductorder.setUserid((Long)request.getSession().getAttribute("userId"));
//        }
        QueryWrapper<CancelproductorderEntity> ew = new QueryWrapper<CancelproductorderEntity>();
        PageUtils page = cancelproductorderService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, cancelproductorder), params), params));
        Map<String, String> deSens = new HashMap<>();
        DeSensUtil.desensitize(page,deSens);
        return R.ok().put("data", page);
    }


    /**
    * 列表
    */
    @RequestMapping("/lists")
    public R list(CancelproductorderEntity cancelproductorder){
        QueryWrapper<CancelproductorderEntity> ew = new QueryWrapper<CancelproductorderEntity>();
        ew.allEq(MPUtil.allEQMapPre(cancelproductorder, "cancelproductorder"));
        return R.ok().put("data", cancelproductorderService.selectListView(ew));
    }


    /**
    * 查询
    */
    @RequestMapping("/query")
    public R query(CancelproductorderEntity cancelproductorder){
        QueryWrapper<CancelproductorderEntity> ew = new QueryWrapper<CancelproductorderEntity>();
        ew.allEq(MPUtil.allEQMapPre(cancelproductorder, "cancelproductorder"));
        CancelproductorderView cancelproductorderView = cancelproductorderService.selectView(ew);
        return R.ok("查询取消商品订单成功").put("data", cancelproductorderView);
    }

    /**
    * 后台详情
    */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
        CancelproductorderEntity cancelproductorder = cancelproductorderService.getById(id);
        Map<String, String> deSens = new HashMap<>();
        DeSensUtil.desensitize(cancelproductorder,deSens);
        return R.ok().put("data", cancelproductorder);
    }

    /**
    * 前台详情
    */
    @IgnoreAuth
    @RequestMapping("/detail/{id}")
    public R detail(@PathVariable("id") Long id){
        CancelproductorderEntity cancelproductorder = cancelproductorderService.getById(id);
        Map<String, String> deSens = new HashMap<>();
        DeSensUtil.desensitize(cancelproductorder,deSens);
        return R.ok().put("data", cancelproductorder);
    }

    /**
    * 后台保存
    */
    @RequestMapping("/save")
    public R save(@RequestBody CancelproductorderEntity cancelproductorder, HttpServletRequest request){
        //ValidatorUtils.validateEntity(cancelproductorder);
        cancelproductorder.setId(null);
        if (cancelproductorder!= null && cancelproductorder.getUserid() == null) {
            cancelproductorder.setUserid((Long)request.getSession().getAttribute("userId"));
        }
        cancelproductorderService.save(cancelproductorder);
        operationLogRecorder.record("cancelproductorder", "取消商品订单", "新增", cancelproductorder, request);
        return R.ok().put("data",cancelproductorder.getId());
    }

    /**
    * 前台保存
    */
    @RequestMapping("/add")
    public R add(@RequestBody CancelproductorderEntity cancelproductorder, HttpServletRequest request){
        //ValidatorUtils.validateEntity(cancelproductorder);
        cancelproductorder.setId(null);
        if (cancelproductorder!= null && cancelproductorder.getUserid() == null) {
            cancelproductorder.setUserid((Long)request.getSession().getAttribute("userId"));
        }
        cancelproductorderService.save(cancelproductorder);
        operationLogRecorder.record("cancelproductorder", "取消商品订单", "新增", cancelproductorder, request);
        return R.ok().put("data",cancelproductorder.getId());
    }


    /**
    * 修改
    */
    @RequestMapping("/update")
    @Transactional
    public R update(@RequestBody CancelproductorderEntity cancelproductorder, HttpServletRequest request){
        //ValidatorUtils.validateEntity(cancelproductorder);
        //全部更新
        cancelproductorderService.updateById(cancelproductorder);
        operationLogRecorder.record("cancelproductorder", "取消商品订单", "修改", cancelproductorder, request);
        return R.ok();
    }

    /**
    * 删除
    */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids, HttpServletRequest request){
        cancelproductorderService.removeByIds(Arrays.asList(ids));
        operationLogRecorder.record("cancelproductorder", "取消商品订单", "删除", Arrays.asList(ids), request);
        return R.ok();
    }



   /**
    * 前台智能排序
    */
    @IgnoreAuth
    @RequestMapping("/autoSort")
    public R autoSort(@RequestParam Map<String, Object> params, CancelproductorderEntity cancelproductorder, HttpServletRequest request, String pre){
        QueryWrapper<CancelproductorderEntity> ew = new QueryWrapper<CancelproductorderEntity>();
        Map<String, Object> newMap = new HashMap<String, Object>();
        Map<String, Object> param = new HashMap<String, Object>();
        Iterator<Map.Entry<String, Object>> it = param.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, Object> entry = it.next();
            String key = entry.getKey();
            String newKey = entry.getKey();
            if (pre.endsWith(".")) {
                newMap.put(pre + newKey, entry.getValue());
            } else if (StrUtil.isEmpty(pre)) {
                newMap.put(newKey, entry.getValue());
            } else {
                newMap.put(pre + "." + newKey, entry.getValue());
            }
        }
        params.put("sort", "clicktime");
        params.put("order", "desc");
        PageUtils page = cancelproductorderService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, cancelproductorder), params), params));
        return R.ok().put("data", page);
    }





}
