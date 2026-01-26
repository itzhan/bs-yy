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

import com.entity.RefundcardrenewalEntity;
import com.entity.view.RefundcardrenewalView;

import com.service.RefundcardrenewalService;
import com.utils.PageUtils;
import com.utils.R;
import com.utils.MPUtil;
import com.utils.MapUtils;
import java.io.IOException;
import com.log.OperationLogRecorder;

/**
* 续卡记录退款
* 后端接口
* @author ds
* @email 1312461553@qq.com
* @date 2025-11-13 21:24:29
*/
@RestController
@RequestMapping("/refundcardrenewal")
public class RefundcardrenewalController {
    @Resource
    private RefundcardrenewalService refundcardrenewalService;
    @Resource
    private OperationLogRecorder operationLogRecorder;

    /**
    * 后台列表
    */
    @RequestMapping("/page")
    public R page(@RequestParam Map<String, Object> params, RefundcardrenewalEntity refundcardrenewal,
            HttpServletRequest request){
        if(!request.getSession().getAttribute("role").toString().equals("管理员")) {
            refundcardrenewal.setUserid((Long)request.getSession().getAttribute("userId"));
        }
        String tableName = request.getSession().getAttribute("tableName").toString();
        String username = (String)request.getSession().getAttribute("username");
        QueryWrapper<RefundcardrenewalEntity> ew = new QueryWrapper<RefundcardrenewalEntity>();
        PageUtils page = refundcardrenewalService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, refundcardrenewal), params), params));
        Map<String, String> deSens = new HashMap<>();
        DeSensUtil.desensitize(page,deSens);
        return R.ok().put("data", page);
    }

    /**
    * 前台列表
    */
    @IgnoreAuth
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params, RefundcardrenewalEntity refundcardrenewal,
            HttpServletRequest request){
//        if(!request.getSession().getAttribute("role").toString().equals("管理员")) {
//            refundcardrenewal.setUserid((Long)request.getSession().getAttribute("userId"));
//        }
        QueryWrapper<RefundcardrenewalEntity> ew = new QueryWrapper<RefundcardrenewalEntity>();
        PageUtils page = refundcardrenewalService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, refundcardrenewal), params), params));
        Map<String, String> deSens = new HashMap<>();
        DeSensUtil.desensitize(page,deSens);
        return R.ok().put("data", page);
    }


    /**
    * 列表
    */
    @RequestMapping("/lists")
    public R list(RefundcardrenewalEntity refundcardrenewal){
        QueryWrapper<RefundcardrenewalEntity> ew = new QueryWrapper<RefundcardrenewalEntity>();
        ew.allEq(MPUtil.allEQMapPre(refundcardrenewal, "refundcardrenewal"));
        return R.ok().put("data", refundcardrenewalService.selectListView(ew));
    }


    /**
    * 查询
    */
    @RequestMapping("/query")
    public R query(RefundcardrenewalEntity refundcardrenewal){
        QueryWrapper<RefundcardrenewalEntity> ew = new QueryWrapper<RefundcardrenewalEntity>();
        ew.allEq(MPUtil.allEQMapPre(refundcardrenewal, "refundcardrenewal"));
        RefundcardrenewalView refundcardrenewalView = refundcardrenewalService.selectView(ew);
        return R.ok("查询续卡记录退款成功").put("data", refundcardrenewalView);
    }

    /**
    * 后台详情
    */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
        RefundcardrenewalEntity refundcardrenewal = refundcardrenewalService.getById(id);
        Map<String, String> deSens = new HashMap<>();
        DeSensUtil.desensitize(refundcardrenewal,deSens);
        return R.ok().put("data", refundcardrenewal);
    }

    /**
    * 前台详情
    */
    @IgnoreAuth
    @RequestMapping("/detail/{id}")
    public R detail(@PathVariable("id") Long id){
        RefundcardrenewalEntity refundcardrenewal = refundcardrenewalService.getById(id);
        Map<String, String> deSens = new HashMap<>();
        DeSensUtil.desensitize(refundcardrenewal,deSens);
        return R.ok().put("data", refundcardrenewal);
    }

    /**
    * 后台保存
    */
    @RequestMapping("/save")
    public R save(@RequestBody RefundcardrenewalEntity refundcardrenewal, HttpServletRequest request){
        //ValidatorUtils.validateEntity(refundcardrenewal);
        refundcardrenewal.setId(null);
        if (refundcardrenewal!= null && refundcardrenewal.getUserid() == null) {
            refundcardrenewal.setUserid((Long)request.getSession().getAttribute("userId"));
        }
        refundcardrenewalService.save(refundcardrenewal);
        operationLogRecorder.record("refundcardrenewal", "续卡记录退款", "新增", refundcardrenewal, request);
        return R.ok().put("data",refundcardrenewal.getId());
    }

    /**
    * 前台保存
    */
    @RequestMapping("/add")
    public R add(@RequestBody RefundcardrenewalEntity refundcardrenewal, HttpServletRequest request){
        //ValidatorUtils.validateEntity(refundcardrenewal);
        refundcardrenewal.setId(null);
        if (refundcardrenewal!= null && refundcardrenewal.getUserid() == null) {
            refundcardrenewal.setUserid((Long)request.getSession().getAttribute("userId"));
        }
        refundcardrenewalService.save(refundcardrenewal);
        operationLogRecorder.record("refundcardrenewal", "续卡记录退款", "新增", refundcardrenewal, request);
        return R.ok().put("data",refundcardrenewal.getId());
    }


    /**
    * 修改
    */
    @RequestMapping("/update")
    @Transactional
    public R update(@RequestBody RefundcardrenewalEntity refundcardrenewal, HttpServletRequest request){
        //ValidatorUtils.validateEntity(refundcardrenewal);
        //全部更新
        refundcardrenewalService.updateById(refundcardrenewal);
        operationLogRecorder.record("refundcardrenewal", "续卡记录退款", "修改", refundcardrenewal, request);
        return R.ok();
    }

    /**
    * 删除
    */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids, HttpServletRequest request){
        refundcardrenewalService.removeByIds(Arrays.asList(ids));
        operationLogRecorder.record("refundcardrenewal", "续卡记录退款", "删除", Arrays.asList(ids), request);
        return R.ok();
    }



   /**
    * 前台智能排序
    */
    @IgnoreAuth
    @RequestMapping("/autoSort")
    public R autoSort(@RequestParam Map<String, Object> params, RefundcardrenewalEntity refundcardrenewal, HttpServletRequest request, String pre){
        QueryWrapper<RefundcardrenewalEntity> ew = new QueryWrapper<RefundcardrenewalEntity>();
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
        PageUtils page = refundcardrenewalService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, refundcardrenewal), params), params));
        return R.ok().put("data", page);
    }





}
