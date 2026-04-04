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

import com.entity.RefundcardapplicationEntity;
import com.entity.view.RefundcardapplicationView;
import com.entity.CardapplicationEntity;

import com.service.RefundcardapplicationService;
import com.service.CardapplicationService;
import com.utils.PageUtils;
import com.utils.R;
import com.utils.MPUtil;
import com.utils.MapUtils;
import java.io.IOException;
import com.log.OperationLogRecorder;

/**
* 办卡记录退款
* 后端接口
* @author ds
* @email 1312461553@qq.com
* @date 2025-11-13 21:24:29
*/
@RestController
@RequestMapping("/refundcardapplication")
public class RefundcardapplicationController {
    @Resource
    private RefundcardapplicationService refundcardapplicationService;
    @Resource
    private CardapplicationService cardapplicationService;
    @Resource
    private OperationLogRecorder operationLogRecorder;

    /**
    * 后台列表
    */
    @RequestMapping("/page")
    public R page(@RequestParam Map<String, Object> params, RefundcardapplicationEntity refundcardapplication,
            HttpServletRequest request){
        if(!request.getSession().getAttribute("role").toString().equals("管理员")) {
            refundcardapplication.setUserid((Long)request.getSession().getAttribute("userId"));
        }
        String tableName = request.getSession().getAttribute("tableName").toString();
        String username = (String)request.getSession().getAttribute("username");
        QueryWrapper<RefundcardapplicationEntity> ew = new QueryWrapper<RefundcardapplicationEntity>();
        PageUtils page = refundcardapplicationService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, refundcardapplication), params), params));
        Map<String, String> deSens = new HashMap<>();
        DeSensUtil.desensitize(page,deSens);
        return R.ok().put("data", page);
    }

    /**
    * 前台列表
    */
    @IgnoreAuth
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params, RefundcardapplicationEntity refundcardapplication,
            HttpServletRequest request){
//        if(!request.getSession().getAttribute("role").toString().equals("管理员")) {
//            refundcardapplication.setUserid((Long)request.getSession().getAttribute("userId"));
//        }
        QueryWrapper<RefundcardapplicationEntity> ew = new QueryWrapper<RefundcardapplicationEntity>();
        PageUtils page = refundcardapplicationService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, refundcardapplication), params), params));
        Map<String, String> deSens = new HashMap<>();
        DeSensUtil.desensitize(page,deSens);
        return R.ok().put("data", page);
    }


    /**
    * 列表
    */
    @RequestMapping("/lists")
    public R list(RefundcardapplicationEntity refundcardapplication){
        QueryWrapper<RefundcardapplicationEntity> ew = new QueryWrapper<RefundcardapplicationEntity>();
        ew.allEq(MPUtil.allEQMapPre(refundcardapplication, "refundcardapplication"));
        return R.ok().put("data", refundcardapplicationService.selectListView(ew));
    }


    /**
    * 查询
    */
    @RequestMapping("/query")
    public R query(RefundcardapplicationEntity refundcardapplication){
        QueryWrapper<RefundcardapplicationEntity> ew = new QueryWrapper<RefundcardapplicationEntity>();
        ew.allEq(MPUtil.allEQMapPre(refundcardapplication, "refundcardapplication"));
        RefundcardapplicationView refundcardapplicationView = refundcardapplicationService.selectView(ew);
        return R.ok("查询办卡记录退款成功").put("data", refundcardapplicationView);
    }

    /**
    * 后台详情
    */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
        RefundcardapplicationEntity refundcardapplication = refundcardapplicationService.getById(id);
        Map<String, String> deSens = new HashMap<>();
        DeSensUtil.desensitize(refundcardapplication,deSens);
        return R.ok().put("data", refundcardapplication);
    }

    /**
    * 前台详情
    */
    @IgnoreAuth
    @RequestMapping("/detail/{id}")
    public R detail(@PathVariable("id") Long id){
        RefundcardapplicationEntity refundcardapplication = refundcardapplicationService.getById(id);
        Map<String, String> deSens = new HashMap<>();
        DeSensUtil.desensitize(refundcardapplication,deSens);
        return R.ok().put("data", refundcardapplication);
    }

    /**
    * 后台保存
    */
    @RequestMapping("/save")
    public R save(@RequestBody RefundcardapplicationEntity refundcardapplication, HttpServletRequest request){
        //ValidatorUtils.validateEntity(refundcardapplication);
        refundcardapplication.setId(null);
        refundcardapplication.setAuditstatus("待审核");
        if (refundcardapplication!= null && refundcardapplication.getUserid() == null) {
            refundcardapplication.setUserid((Long)request.getSession().getAttribute("userId"));
        }
        refundcardapplicationService.save(refundcardapplication);
        operationLogRecorder.record("refundcardapplication", "办卡记录退款", "新增", refundcardapplication, request);
        return R.ok().put("data",refundcardapplication.getId());
    }

    /**
    * 前台保存
    */
    @RequestMapping("/add")
    public R add(@RequestBody RefundcardapplicationEntity refundcardapplication, HttpServletRequest request){
        //ValidatorUtils.validateEntity(refundcardapplication);
        refundcardapplication.setId(null);
        refundcardapplication.setAuditstatus("待审核");
        if (refundcardapplication!= null && refundcardapplication.getUserid() == null) {
            refundcardapplication.setUserid((Long)request.getSession().getAttribute("userId"));
        }
        refundcardapplicationService.save(refundcardapplication);
        operationLogRecorder.record("refundcardapplication", "办卡记录退款", "新增", refundcardapplication, request);
        return R.ok().put("data",refundcardapplication.getId());
    }


    /**
    * 修改
    */
    @RequestMapping("/update")
    @Transactional
    public R update(@RequestBody RefundcardapplicationEntity refundcardapplication, HttpServletRequest request){
        //ValidatorUtils.validateEntity(refundcardapplication);
        //全部更新
        refundcardapplicationService.updateById(refundcardapplication);
        operationLogRecorder.record("refundcardapplication", "办卡记录退款", "修改", refundcardapplication, request);
        return R.ok();
    }

    /**
    * 退款审核
    */
    @RequestMapping("/audit")
    @Transactional
    public R audit(@RequestBody Map<String, Object> params, HttpServletRequest request){
        Long id = Long.parseLong(params.get("id").toString());
        String auditstatus = params.get("auditstatus").toString();
        RefundcardapplicationEntity refund = refundcardapplicationService.getById(id);
        if(refund == null) {
            return R.error("退款记录不存在");
        }
        refund.setAuditstatus(auditstatus);
        refundcardapplicationService.updateById(refund);
        if("已通过".equals(auditstatus) && refund.getCrossrefid() != null) {
            CardapplicationEntity order = cardapplicationService.getById(refund.getCrossrefid());
            if(order != null) {
                order.setOrderstatus("已退款");
                cardapplicationService.updateById(order);
            }
        }
        if("已拒绝".equals(auditstatus) && refund.getCrossrefid() != null) {
            CardapplicationEntity order = cardapplicationService.getById(refund.getCrossrefid());
            if(order != null) {
                order.setOrderstatus("已支付");
                cardapplicationService.updateById(order);
            }
        }
        operationLogRecorder.record("refundcardapplication", "办卡记录退款", "审核:" + auditstatus, refund, request);
        return R.ok();
    }

    /**
    * 删除
    */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids, HttpServletRequest request){
        refundcardapplicationService.removeByIds(Arrays.asList(ids));
        operationLogRecorder.record("refundcardapplication", "办卡记录退款", "删除", Arrays.asList(ids), request);
        return R.ok();
    }



   /**
    * 前台智能排序
    */
    @IgnoreAuth
    @RequestMapping("/autoSort")
    public R autoSort(@RequestParam Map<String, Object> params, RefundcardapplicationEntity refundcardapplication, HttpServletRequest request, String pre){
        QueryWrapper<RefundcardapplicationEntity> ew = new QueryWrapper<RefundcardapplicationEntity>();
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
        PageUtils page = refundcardapplicationService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, refundcardapplication), params), params));
        return R.ok().put("data", page);
    }





}
