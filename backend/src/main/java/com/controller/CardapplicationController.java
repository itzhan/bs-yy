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

import com.entity.CardapplicationEntity;
import com.entity.view.CardapplicationView;

import com.service.CardapplicationService;
import com.utils.PageUtils;
import com.utils.R;
import com.utils.MPUtil;
import com.utils.MapUtils;
import java.io.IOException;
import com.service.NotifyService;
import com.entity.NotifyEntity;
import com.log.OperationLogRecorder;

/**
* 办卡记录
* 后端接口
* @author ds
* @email 1312461553@qq.com
* @date 2025-11-13 21:24:29
*/
@RestController
@RequestMapping("/cardapplication")
public class CardapplicationController {
    @Resource
    private CardapplicationService cardapplicationService;
    @Resource
    private NotifyService notifyService;
    @Resource
    private OperationLogRecorder operationLogRecorder;

    /**
    * 后台列表
    */
    @RequestMapping("/page")
    public R page(@RequestParam Map<String, Object> params, CardapplicationEntity cardapplication,
            HttpServletRequest request){
        if(!request.getSession().getAttribute("role").toString().equals("管理员")) {
            cardapplication.setUserid((Long)request.getSession().getAttribute("userId"));
        }
        QueryWrapper<CardapplicationEntity> ew = new QueryWrapper<CardapplicationEntity>();
        PageUtils page = cardapplicationService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, cardapplication), params), params));
        Map<String, String> deSens = new HashMap<>();
        DeSensUtil.desensitize(page,deSens);
        return R.ok().put("data", page);
    }

    /**
    * 前台列表
    */
    @IgnoreAuth
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params, CardapplicationEntity cardapplication,
            HttpServletRequest request){
//        if(!request.getSession().getAttribute("role").toString().equals("管理员")) {
//            cardapplication.setUserid((Long)request.getSession().getAttribute("userId"));
//        }
        QueryWrapper<CardapplicationEntity> ew = new QueryWrapper<CardapplicationEntity>();
        PageUtils page = cardapplicationService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, cardapplication), params), params));
        Map<String, String> deSens = new HashMap<>();
        DeSensUtil.desensitize(page,deSens);
        return R.ok().put("data", page);
    }


    /**
    * 列表
    */
    @RequestMapping("/lists")
    public R list(CardapplicationEntity cardapplication){
        QueryWrapper<CardapplicationEntity> ew = new QueryWrapper<CardapplicationEntity>();
        ew.allEq(MPUtil.allEQMapPre(cardapplication, "cardapplication"));
        return R.ok().put("data", cardapplicationService.selectListView(ew));
    }


    /**
    * 查询
    */
    @RequestMapping("/query")
    public R query(CardapplicationEntity cardapplication){
        QueryWrapper<CardapplicationEntity> ew = new QueryWrapper<CardapplicationEntity>();
        ew.allEq(MPUtil.allEQMapPre(cardapplication, "cardapplication"));
        CardapplicationView cardapplicationView = cardapplicationService.selectView(ew);
        return R.ok("查询办卡记录成功").put("data", cardapplicationView);
    }

    /**
    * 后台详情
    */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
        CardapplicationEntity cardapplication = cardapplicationService.getById(id);
        Map<String, String> deSens = new HashMap<>();
        DeSensUtil.desensitize(cardapplication,deSens);
        return R.ok().put("data", cardapplication);
    }

    /**
    * 前台详情
    */
    @IgnoreAuth
    @RequestMapping("/detail/{id}")
    public R detail(@PathVariable("id") Long id){
        CardapplicationEntity cardapplication = cardapplicationService.getById(id);
        Map<String, String> deSens = new HashMap<>();
        DeSensUtil.desensitize(cardapplication,deSens);
        return R.ok().put("data", cardapplication);
    }

    /**
    * 后台保存
    */
    @RequestMapping("/save")
    public R save(@RequestBody CardapplicationEntity cardapplication, HttpServletRequest request){
        //ValidatorUtils.validateEntity(cardapplication);
        cardapplication.setId(null);
        if (cardapplication!= null && cardapplication.getUserid() == null) {
            cardapplication.setUserid((Long)request.getSession().getAttribute("userId"));
        }
        cardapplicationService.save(cardapplication);
        {
            NotifyEntity nf_0 = new NotifyEntity();
            Long nf_0_userId = null;
            nf_0_userId = (Long)request.getSession().getAttribute("userId");
            if (nf_0_userId == null) {
                nf_0_userId = 1L;
            }
            nf_0.setUserid(nf_0_userId);
            nf_0.setTitle(String.format("办卡申请已提交", cardapplication.getPackagename()));
            nf_0.setContent(String.format("您的%s办卡申请已提交，请尽快完成支付", cardapplication.getPackagename()));
            nf_0.setMessagetype("系统通知");
            nf_0.setReadstatus("未读");
            nf_0.setSenduser("系统");
            notifyService.save(nf_0);
        }
        operationLogRecorder.record("cardapplication", "办卡记录", "新增", cardapplication, request);
        return R.ok().put("data",cardapplication.getId());
    }

    /**
    * 前台保存
    */
    @RequestMapping("/add")
    public R add(@RequestBody CardapplicationEntity cardapplication, HttpServletRequest request){
        //ValidatorUtils.validateEntity(cardapplication);
        cardapplication.setId(null);
        if (cardapplication!= null && cardapplication.getUserid() == null) {
            cardapplication.setUserid((Long)request.getSession().getAttribute("userId"));
        }
        cardapplicationService.save(cardapplication);
        {
            NotifyEntity nf_0 = new NotifyEntity();
            Long nf_0_userId = null;
            nf_0_userId = (Long)request.getSession().getAttribute("userId");
            if (nf_0_userId == null) {
                nf_0_userId = 1L;
            }
            nf_0.setUserid(nf_0_userId);
            nf_0.setTitle(String.format("办卡申请已提交", cardapplication.getPackagename()));
            nf_0.setContent(String.format("您的%s办卡申请已提交，请尽快完成支付", cardapplication.getPackagename()));
            nf_0.setMessagetype("系统通知");
            nf_0.setReadstatus("未读");
            nf_0.setSenduser("系统");
            notifyService.save(nf_0);
        }
        operationLogRecorder.record("cardapplication", "办卡记录", "新增", cardapplication, request);
        return R.ok().put("data",cardapplication.getId());
    }


    /**
    * 修改
    */
    @RequestMapping("/update")
    @Transactional
    @IgnoreAuth
    public R update(@RequestBody CardapplicationEntity cardapplication, HttpServletRequest request){
        //ValidatorUtils.validateEntity(cardapplication);
        //全部更新
        cardapplicationService.updateById(cardapplication);
        {
            boolean _send = true;
            _send = "已支付".equals(String.valueOf(cardapplication.getIspay()));
            if (_send) {
            NotifyEntity nf_1 = new NotifyEntity();
            Long nf_1_userId = null;
            nf_1_userId = (Long)request.getSession().getAttribute("userId");
            if (nf_1_userId == null) {
                nf_1_userId = 1L;
            }
            nf_1.setUserid(nf_1_userId);
            nf_1.setTitle(String.format("办卡支付成功", cardapplication.getPackagename()));
            nf_1.setContent(String.format("您的%s办卡已支付成功，会员权益已生效", cardapplication.getPackagename()));
            nf_1.setMessagetype("支付通知");
            nf_1.setReadstatus("未读");
            nf_1.setSenduser("系统");
            notifyService.save(nf_1);
            }
        }
        operationLogRecorder.record("cardapplication", "办卡记录", "修改", cardapplication, request);
        return R.ok();
    }

    /**
    * 删除
    */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids, HttpServletRequest request){
        cardapplicationService.removeByIds(Arrays.asList(ids));
        operationLogRecorder.record("cardapplication", "办卡记录", "删除", Arrays.asList(ids), request);
        return R.ok();
    }



   /**
    * 前台智能排序
    */
    @IgnoreAuth
    @RequestMapping("/autoSort")
    public R autoSort(@RequestParam Map<String, Object> params, CardapplicationEntity cardapplication, HttpServletRequest request, String pre){
        QueryWrapper<CardapplicationEntity> ew = new QueryWrapper<CardapplicationEntity>();
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
        PageUtils page = cardapplicationService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, cardapplication), params), params));
        return R.ok().put("data", page);
    }





}
