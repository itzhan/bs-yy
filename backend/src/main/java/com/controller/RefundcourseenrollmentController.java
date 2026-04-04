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

import com.entity.RefundcourseenrollmentEntity;
import com.entity.view.RefundcourseenrollmentView;
import com.entity.CourseenrollmentEntity;
import com.entity.CourseEntity;

import com.service.RefundcourseenrollmentService;
import com.service.CourseenrollmentService;
import com.service.CourseService;
import com.utils.PageUtils;
import com.utils.R;
import com.utils.MPUtil;
import com.utils.MapUtils;
import java.io.IOException;
import com.log.OperationLogRecorder;

/**
* 课程报名记录退款
* 后端接口
* @author ds
* @email 1312461553@qq.com
* @date 2025-11-13 21:24:29
*/
@RestController
@RequestMapping("/refundcourseenrollment")
public class RefundcourseenrollmentController {
    @Resource
    private RefundcourseenrollmentService refundcourseenrollmentService;
    @Resource
    private CourseenrollmentService courseenrollmentService;
    @Resource
    private CourseService courseService;
    @Resource
    private OperationLogRecorder operationLogRecorder;

    /**
    * 后台列表
    */
    @RequestMapping("/page")
    public R page(@RequestParam Map<String, Object> params, RefundcourseenrollmentEntity refundcourseenrollment,
            HttpServletRequest request){
        if(!request.getSession().getAttribute("role").toString().equals("管理员")) {
            refundcourseenrollment.setUserid((Long)request.getSession().getAttribute("userId"));
        }
        String tableName = request.getSession().getAttribute("tableName").toString();
        String username = (String)request.getSession().getAttribute("username");
        QueryWrapper<RefundcourseenrollmentEntity> ew = new QueryWrapper<RefundcourseenrollmentEntity>();
        PageUtils page = refundcourseenrollmentService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, refundcourseenrollment), params), params));
        Map<String, String> deSens = new HashMap<>();
        DeSensUtil.desensitize(page,deSens);
        return R.ok().put("data", page);
    }

    /**
    * 前台列表
    */
    @IgnoreAuth
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params, RefundcourseenrollmentEntity refundcourseenrollment,
            HttpServletRequest request){
//        if(!request.getSession().getAttribute("role").toString().equals("管理员")) {
//            refundcourseenrollment.setUserid((Long)request.getSession().getAttribute("userId"));
//        }
        QueryWrapper<RefundcourseenrollmentEntity> ew = new QueryWrapper<RefundcourseenrollmentEntity>();
        PageUtils page = refundcourseenrollmentService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, refundcourseenrollment), params), params));
        Map<String, String> deSens = new HashMap<>();
        DeSensUtil.desensitize(page,deSens);
        return R.ok().put("data", page);
    }


    /**
    * 列表
    */
    @RequestMapping("/lists")
    public R list(RefundcourseenrollmentEntity refundcourseenrollment){
        QueryWrapper<RefundcourseenrollmentEntity> ew = new QueryWrapper<RefundcourseenrollmentEntity>();
        ew.allEq(MPUtil.allEQMapPre(refundcourseenrollment, "refundcourseenrollment"));
        return R.ok().put("data", refundcourseenrollmentService.selectListView(ew));
    }


    /**
    * 查询
    */
    @RequestMapping("/query")
    public R query(RefundcourseenrollmentEntity refundcourseenrollment){
        QueryWrapper<RefundcourseenrollmentEntity> ew = new QueryWrapper<RefundcourseenrollmentEntity>();
        ew.allEq(MPUtil.allEQMapPre(refundcourseenrollment, "refundcourseenrollment"));
        RefundcourseenrollmentView refundcourseenrollmentView = refundcourseenrollmentService.selectView(ew);
        return R.ok("查询课程报名记录退款成功").put("data", refundcourseenrollmentView);
    }

    /**
    * 后台详情
    */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
        RefundcourseenrollmentEntity refundcourseenrollment = refundcourseenrollmentService.getById(id);
        Map<String, String> deSens = new HashMap<>();
        DeSensUtil.desensitize(refundcourseenrollment,deSens);
        return R.ok().put("data", refundcourseenrollment);
    }

    /**
    * 前台详情
    */
    @IgnoreAuth
    @RequestMapping("/detail/{id}")
    public R detail(@PathVariable("id") Long id){
        RefundcourseenrollmentEntity refundcourseenrollment = refundcourseenrollmentService.getById(id);
        Map<String, String> deSens = new HashMap<>();
        DeSensUtil.desensitize(refundcourseenrollment,deSens);
        return R.ok().put("data", refundcourseenrollment);
    }

    /**
    * 后台保存
    */
    @RequestMapping("/save")
    public R save(@RequestBody RefundcourseenrollmentEntity refundcourseenrollment, HttpServletRequest request){
        //ValidatorUtils.validateEntity(refundcourseenrollment);
        refundcourseenrollment.setId(null);
        refundcourseenrollment.setAuditstatus("待审核");
        if (refundcourseenrollment!= null && refundcourseenrollment.getUserid() == null) {
            refundcourseenrollment.setUserid((Long)request.getSession().getAttribute("userId"));
        }
        refundcourseenrollmentService.save(refundcourseenrollment);
        operationLogRecorder.record("refundcourseenrollment", "课程报名记录退款", "新增", refundcourseenrollment, request);
        return R.ok().put("data",refundcourseenrollment.getId());
    }

    /**
    * 前台保存
    */
    @RequestMapping("/add")
    public R add(@RequestBody RefundcourseenrollmentEntity refundcourseenrollment, HttpServletRequest request){
        //ValidatorUtils.validateEntity(refundcourseenrollment);
        refundcourseenrollment.setId(null);
        refundcourseenrollment.setAuditstatus("待审核");
        if (refundcourseenrollment!= null && refundcourseenrollment.getUserid() == null) {
            refundcourseenrollment.setUserid((Long)request.getSession().getAttribute("userId"));
        }
        refundcourseenrollmentService.save(refundcourseenrollment);
        operationLogRecorder.record("refundcourseenrollment", "课程报名记录退款", "新增", refundcourseenrollment, request);
        return R.ok().put("data",refundcourseenrollment.getId());
    }


    /**
    * 修改
    */
    @RequestMapping("/update")
    @Transactional
    public R update(@RequestBody RefundcourseenrollmentEntity refundcourseenrollment, HttpServletRequest request){
        //ValidatorUtils.validateEntity(refundcourseenrollment);
        //全部更新
        refundcourseenrollmentService.updateById(refundcourseenrollment);
        operationLogRecorder.record("refundcourseenrollment", "课程报名记录退款", "修改", refundcourseenrollment, request);
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
        RefundcourseenrollmentEntity refund = refundcourseenrollmentService.getById(id);
        if(refund == null) {
            return R.error("退款记录不存在");
        }
        refund.setAuditstatus(auditstatus);
        refundcourseenrollmentService.updateById(refund);
        // 审核通过时，更新原订单状态为"已退款"并恢复课程名额
        if("已通过".equals(auditstatus) && refund.getCrossrefid() != null) {
            CourseenrollmentEntity enrollment = courseenrollmentService.getById(refund.getCrossrefid());
            if(enrollment != null) {
                enrollment.setOrderstatus("已退款");
                courseenrollmentService.updateById(enrollment);
                // 恢复课程名额
                if(enrollment.getCoursename() != null) {
                    QueryWrapper<CourseEntity> cw = new QueryWrapper<>();
                    cw.eq("coursename", enrollment.getCoursename());
                    CourseEntity course = courseService.getOne(cw);
                    if(course != null && course.getQuota() != null) {
                        course.setQuota(course.getQuota() + 1);
                        courseService.updateById(course);
                    }
                }
            }
        }
        // 审核拒绝时，恢复原订单状态为"已支付"
        if("已拒绝".equals(auditstatus) && refund.getCrossrefid() != null) {
            CourseenrollmentEntity enrollment = courseenrollmentService.getById(refund.getCrossrefid());
            if(enrollment != null) {
                enrollment.setOrderstatus("已支付");
                courseenrollmentService.updateById(enrollment);
            }
        }
        operationLogRecorder.record("refundcourseenrollment", "课程报名记录退款", "审核:" + auditstatus, refund, request);
        return R.ok();
    }

    /**
    * 删除
    */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids, HttpServletRequest request){
        refundcourseenrollmentService.removeByIds(Arrays.asList(ids));
        operationLogRecorder.record("refundcourseenrollment", "课程报名记录退款", "删除", Arrays.asList(ids), request);
        return R.ok();
    }



   /**
    * 前台智能排序
    */
    @IgnoreAuth
    @RequestMapping("/autoSort")
    public R autoSort(@RequestParam Map<String, Object> params, RefundcourseenrollmentEntity refundcourseenrollment, HttpServletRequest request, String pre){
        QueryWrapper<RefundcourseenrollmentEntity> ew = new QueryWrapper<RefundcourseenrollmentEntity>();
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
        PageUtils page = refundcourseenrollmentService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, refundcourseenrollment), params), params));
        return R.ok().put("data", page);
    }





}
