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

import com.entity.FeedbackEntity;
import com.entity.view.FeedbackView;

import com.service.FeedbackService;
import com.utils.PageUtils;
import com.utils.R;
import com.utils.MPUtil;
import com.utils.MapUtils;
import java.io.IOException;
import com.service.NotifyService;
import com.entity.NotifyEntity;
import com.log.OperationLogRecorder;

/**
* 意见反馈
* 后端接口
* @author ds
* @email 1312461553@qq.com
* @date 2025-11-13 21:24:29
*/
@RestController
@RequestMapping("/feedback")
public class FeedbackController {
    @Resource
    private FeedbackService feedbackService;
    @Resource
    private NotifyService notifyService;
    @Resource
    private OperationLogRecorder operationLogRecorder;

    /**
     * 审核
     */
    @RequestMapping("/audit/batch")
    @Transactional
    public R auditBatch(@RequestBody Long[] ids, @RequestParam String auditstatus, @RequestParam String auditreply, HttpServletRequest request){
        List<FeedbackEntity> list = new ArrayList<FeedbackEntity>();
        for(Long id : ids) {
            FeedbackEntity feedback = feedbackService.getById(id);
            feedback.setAuditstatus(auditstatus);
            feedback.setAuditreply(auditreply);
            list.add(feedback);
        }
        feedbackService.updateBatchById(list);
        for (FeedbackEntity item : list) {
            if ("通过".equals(auditstatus)) {
            NotifyEntity nf_0 = new NotifyEntity();
            Long nf_0_userId = null;
            nf_0_userId = (Long)request.getSession().getAttribute("userId");
            if (nf_0_userId == null) {
                nf_0_userId = 1L;
            }
            nf_0.setUserid(nf_0_userId);
            nf_0.setTitle(String.format("您的反馈《%s》已处理", item.getFeedbacktitle()));
            nf_0.setContent(String.format("您提交的反馈《%s》已被管理员处理，感谢您的宝贵意见", item.getFeedbacktitle()));
            nf_0.setMessagetype("反馈通知");
            nf_0.setReadstatus("未读");
            nf_0.setSenduser("系统");
            notifyService.save(nf_0);
            }
            if ("未通过".equals(auditstatus)) {
            NotifyEntity nf_1 = new NotifyEntity();
            Long nf_1_userId = null;
            nf_1_userId = (Long)request.getSession().getAttribute("userId");
            if (nf_1_userId == null) {
                nf_1_userId = 1L;
            }
            nf_1.setUserid(nf_1_userId);
            nf_1.setTitle(String.format("您的反馈《%s》处理结果", item.getFeedbacktitle(), item.getAuditreply()));
            nf_1.setContent(String.format("您提交的反馈《%s》处理结果：%s", item.getFeedbacktitle(), item.getAuditreply()));
            nf_1.setMessagetype("反馈通知");
            nf_1.setReadstatus("未读");
            nf_1.setSenduser("系统");
            notifyService.save(nf_1);
            }
        }
        return R.ok();
    }
    /**
    * 后台列表
    */
    @RequestMapping("/page")
    public R page(@RequestParam Map<String, Object> params, FeedbackEntity feedback,
            HttpServletRequest request){
        if(!request.getSession().getAttribute("role").toString().equals("管理员")) {
            feedback.setUserid((Long)request.getSession().getAttribute("userId"));
        }
        QueryWrapper<FeedbackEntity> ew = new QueryWrapper<FeedbackEntity>();
        PageUtils page = feedbackService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, feedback), params), params));
        Map<String, String> deSens = new HashMap<>();
        DeSensUtil.desensitize(page,deSens);
        return R.ok().put("data", page);
    }

    /**
    * 前台列表
    */
    @IgnoreAuth
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params, FeedbackEntity feedback,
            HttpServletRequest request){
//        if(!request.getSession().getAttribute("role").toString().equals("管理员")) {
//            feedback.setUserid((Long)request.getSession().getAttribute("userId"));
//        }
        QueryWrapper<FeedbackEntity> ew = new QueryWrapper<FeedbackEntity>();
        PageUtils page = feedbackService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, feedback), params), params));
        Map<String, String> deSens = new HashMap<>();
        DeSensUtil.desensitize(page,deSens);
        return R.ok().put("data", page);
    }


    /**
    * 列表
    */
    @RequestMapping("/lists")
    public R list(FeedbackEntity feedback){
        QueryWrapper<FeedbackEntity> ew = new QueryWrapper<FeedbackEntity>();
        ew.allEq(MPUtil.allEQMapPre(feedback, "feedback"));
        return R.ok().put("data", feedbackService.selectListView(ew));
    }


    /**
    * 查询
    */
    @RequestMapping("/query")
    public R query(FeedbackEntity feedback){
        QueryWrapper<FeedbackEntity> ew = new QueryWrapper<FeedbackEntity>();
        ew.allEq(MPUtil.allEQMapPre(feedback, "feedback"));
        FeedbackView feedbackView = feedbackService.selectView(ew);
        return R.ok("查询意见反馈成功").put("data", feedbackView);
    }

    /**
    * 后台详情
    */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
        FeedbackEntity feedback = feedbackService.getById(id);
        Map<String, String> deSens = new HashMap<>();
        DeSensUtil.desensitize(feedback,deSens);
        return R.ok().put("data", feedback);
    }

    /**
    * 前台详情
    */
    @IgnoreAuth
    @RequestMapping("/detail/{id}")
    public R detail(@PathVariable("id") Long id){
        FeedbackEntity feedback = feedbackService.getById(id);
        Map<String, String> deSens = new HashMap<>();
        DeSensUtil.desensitize(feedback,deSens);
        return R.ok().put("data", feedback);
    }

    /**
    * 后台保存
    */
    @RequestMapping("/save")
    public R save(@RequestBody FeedbackEntity feedback, HttpServletRequest request){
        //ValidatorUtils.validateEntity(feedback);
        feedback.setId(null);
        if (feedback!= null && feedback.getUserid() == null) {
            feedback.setUserid((Long)request.getSession().getAttribute("userId"));
        }
        if ("管理员".equals(String.valueOf(request.getSession().getAttribute("role")))) {
            feedback.setAuditstatus("通过");
        }
        feedbackService.save(feedback);
        operationLogRecorder.record("feedback", "意见反馈", "新增", feedback, request);
        return R.ok().put("data",feedback.getId());
    }

    /**
    * 前台保存
    */
    @RequestMapping("/add")
    public R add(@RequestBody FeedbackEntity feedback, HttpServletRequest request){
        //ValidatorUtils.validateEntity(feedback);
        feedback.setId(null);
        if (feedback!= null && feedback.getUserid() == null) {
            feedback.setUserid((Long)request.getSession().getAttribute("userId"));
        }
        feedbackService.save(feedback);
        operationLogRecorder.record("feedback", "意见反馈", "新增", feedback, request);
        return R.ok().put("data",feedback.getId());
    }


    /**
    * 修改
    */
    @RequestMapping("/update")
    @Transactional
    @IgnoreAuth
    public R update(@RequestBody FeedbackEntity feedback, HttpServletRequest request){
        //ValidatorUtils.validateEntity(feedback);
        //全部更新
        feedbackService.updateById(feedback);
        operationLogRecorder.record("feedback", "意见反馈", "修改", feedback, request);
        return R.ok();
    }

    /**
    * 删除
    */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids, HttpServletRequest request){
        feedbackService.removeByIds(Arrays.asList(ids));
        operationLogRecorder.record("feedback", "意见反馈", "删除", Arrays.asList(ids), request);
        return R.ok();
    }



   /**
    * 前台智能排序
    */
    @IgnoreAuth
    @RequestMapping("/autoSort")
    public R autoSort(@RequestParam Map<String, Object> params, FeedbackEntity feedback, HttpServletRequest request, String pre){
        QueryWrapper<FeedbackEntity> ew = new QueryWrapper<FeedbackEntity>();
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
        PageUtils page = feedbackService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, feedback), params), params));
        return R.ok().put("data", page);
    }





}
