 package com.controller;

import java.math.BigDecimal;
import java.math.RoundingMode;
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

import com.entity.CourseenrollmentEntity;
import com.entity.CourseEntity;
import com.entity.view.CourseenrollmentView;

import com.service.CourseenrollmentService;
import com.utils.PageUtils;
import com.utils.R;
import com.utils.MPUtil;
import com.utils.MapUtils;
import java.io.IOException;
import com.service.NotifyService;
import com.entity.NotifyEntity;
import com.log.OperationLogRecorder;
import com.service.CoachmemberService;
import com.entity.CoachmemberEntity;
import com.service.CoachService;
import com.service.CourseService;
import com.service.UserService;
import com.entity.CoachEntity;
import com.entity.UserEntity;

/**
* 课程报名记录
* 后端接口
* @author ds
* @email 1312461553@qq.com
* @date 2025-11-13 21:24:29
*/
@RestController
@RequestMapping("/courseenrollment")
public class CourseenrollmentController {
    @Resource
    private CourseenrollmentService courseenrollmentService;
    @Resource
    private NotifyService notifyService;
    @Resource
    private CoachmemberService coachmemberService;
    @Resource
    private CoachService coachService;
    @Resource
    private UserService userService;
    @Resource
    private CourseService courseService;
    @Resource
    private OperationLogRecorder operationLogRecorder;

    private void ensureCoachMemberBinding(CourseenrollmentEntity courseenrollment, HttpServletRequest request) {
        if (courseenrollment == null) {
            return;
        }
        Long userId = courseenrollment.getUserid();
        if (userId == null) {
            Object sessionUserId = request.getSession().getAttribute("userId");
            if (sessionUserId instanceof Long) {
                userId = (Long) sessionUserId;
            }
        }
        if (userId == null) {
            return;
        }
        String coachAccount = courseenrollment.getCoachaccount();
        String coachName = courseenrollment.getCoachname();
        CoachEntity coach = null;
        if (!StrUtil.isBlank(coachAccount)) {
            coach = coachService.getOne(new QueryWrapper<CoachEntity>().eq("coachaccount", coachAccount));
        }
        if (coach == null && !StrUtil.isBlank(coachName)) {
            coach = coachService.getOne(new QueryWrapper<CoachEntity>().eq("coachname", coachName));
        }
        if (coach == null || coach.getId() == null) {
            return;
        }
        CoachmemberEntity exists = coachmemberService.getOne(new QueryWrapper<CoachmemberEntity>()
                .eq("coachid", coach.getId())
                .eq("userid", userId));
        if (exists != null) {
            return;
        }
        UserEntity user = userService.getById(userId);
        CoachmemberEntity coachmember = new CoachmemberEntity();
        coachmember.setCoachid(coach.getId());
        coachmember.setCoachaccount(coach.getCoachaccount());
        coachmember.setCoachname(coach.getCoachname());
        coachmember.setUserid(userId);
        if (user != null) {
            coachmember.setUseraccount(user.getUseraccount());
            coachmember.setUsername(user.getName());
            coachmember.setPhone(user.getPhone());
            coachmember.setUserimage(user.getImage());
        }
        coachmember.setBindstatus("已绑定");
        coachmemberService.save(coachmember);
    }

    private void fillEnrollmentLockedFields(CourseenrollmentEntity courseenrollment, HttpServletRequest request) {
        if (courseenrollment == null) {
            return;
        }
        Long userId = courseenrollment.getUserid();
        if (userId == null) {
            Object sessionUserId = request.getSession().getAttribute("userId");
            if (sessionUserId instanceof Long) {
                userId = (Long) sessionUserId;
            }
        }
        if (userId != null) {
            courseenrollment.setUserid(userId);
            if (StrUtil.isBlank(courseenrollment.getUseraccount())) {
                UserEntity user = userService.getById(userId);
                if (user != null && !StrUtil.isBlank(user.getUseraccount())) {
                    courseenrollment.setUseraccount(user.getUseraccount());
                }
            }
        }

        CourseEntity course = null;
        Long refId = courseenrollment.getCrossrefid();
        if (refId != null) {
            course = courseService.getById(refId);
        }
        if (course == null && !StrUtil.isBlank(courseenrollment.getCoursename())) {
            course = courseService.getOne(new QueryWrapper<CourseEntity>().eq("coursename", courseenrollment.getCoursename()));
        }
        if (course != null) {
            courseenrollment.setCoursename(course.getCoursename());
            courseenrollment.setCourseimage(course.getCourseimage());
            courseenrollment.setCoursetype(course.getCoursetype());
            courseenrollment.setClasstime(course.getClasstime());
            courseenrollment.setDuration(course.getDuration());
            courseenrollment.setCoachname(course.getCoachname());
            courseenrollment.setCoachaccount(course.getCoachaccount());
            courseenrollment.setCourseprice(course.getCourseprice());
        }

        Integer quantity = courseenrollment.getQuantity();
        if (quantity == null || quantity <= 0) {
            quantity = 1;
            courseenrollment.setQuantity(quantity);
        }
        if (courseenrollment.getCourseprice() != null) {
            BigDecimal total = BigDecimal.valueOf(courseenrollment.getCourseprice())
                    .multiply(BigDecimal.valueOf(quantity))
                    .setScale(2, RoundingMode.HALF_UP);
            courseenrollment.setTotalprice(total.doubleValue());
        }
        if (StrUtil.isBlank(courseenrollment.getIspay())) {
            courseenrollment.setIspay("未支付");
        }
        if (StrUtil.isBlank(courseenrollment.getOrderstatus())) {
            courseenrollment.setOrderstatus("未支付");
        }
    }

    /**
    * 后台列表
    */
    @RequestMapping("/page")
    public R page(@RequestParam Map<String, Object> params, CourseenrollmentEntity courseenrollment,
            HttpServletRequest request){
        if(!request.getSession().getAttribute("role").toString().equals("管理员")) {
            courseenrollment.setUserid((Long)request.getSession().getAttribute("userId"));
        }
        String tableName = request.getSession().getAttribute("tableName").toString();
        String username = (String)request.getSession().getAttribute("username");
		if(tableName.equals("coach")) {
			courseenrollment.setCoachaccount(username);
            if(courseenrollment.getUserid()!=null) {
                courseenrollment.setUserid(null);
            }
		}
        QueryWrapper<CourseenrollmentEntity> ew = new QueryWrapper<CourseenrollmentEntity>();
        PageUtils page = courseenrollmentService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, courseenrollment), params), params));
        Map<String, String> deSens = new HashMap<>();
        DeSensUtil.desensitize(page,deSens);
        return R.ok().put("data", page);
    }

    /**
    * 前台列表
    */
    @IgnoreAuth
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params, CourseenrollmentEntity courseenrollment,
            HttpServletRequest request){
//        if(!request.getSession().getAttribute("role").toString().equals("管理员")) {
//            courseenrollment.setUserid((Long)request.getSession().getAttribute("userId"));
//        }
        QueryWrapper<CourseenrollmentEntity> ew = new QueryWrapper<CourseenrollmentEntity>();
        PageUtils page = courseenrollmentService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, courseenrollment), params), params));
        Map<String, String> deSens = new HashMap<>();
        DeSensUtil.desensitize(page,deSens);
        return R.ok().put("data", page);
    }


    /**
    * 列表
    */
    @RequestMapping("/lists")
    public R list(CourseenrollmentEntity courseenrollment){
        QueryWrapper<CourseenrollmentEntity> ew = new QueryWrapper<CourseenrollmentEntity>();
        ew.allEq(MPUtil.allEQMapPre(courseenrollment, "courseenrollment"));
        return R.ok().put("data", courseenrollmentService.selectListView(ew));
    }


    /**
    * 查询
    */
    @RequestMapping("/query")
    public R query(CourseenrollmentEntity courseenrollment){
        QueryWrapper<CourseenrollmentEntity> ew = new QueryWrapper<CourseenrollmentEntity>();
        ew.allEq(MPUtil.allEQMapPre(courseenrollment, "courseenrollment"));
        CourseenrollmentView courseenrollmentView = courseenrollmentService.selectView(ew);
        return R.ok("查询课程报名记录成功").put("data", courseenrollmentView);
    }

    /**
    * 后台详情
    */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
        CourseenrollmentEntity courseenrollment = courseenrollmentService.getById(id);
        Map<String, String> deSens = new HashMap<>();
        DeSensUtil.desensitize(courseenrollment,deSens);
        return R.ok().put("data", courseenrollment);
    }

    /**
    * 前台详情
    */
    @IgnoreAuth
    @RequestMapping("/detail/{id}")
    public R detail(@PathVariable("id") Long id){
        CourseenrollmentEntity courseenrollment = courseenrollmentService.getById(id);
        Map<String, String> deSens = new HashMap<>();
        DeSensUtil.desensitize(courseenrollment,deSens);
        return R.ok().put("data", courseenrollment);
    }

    /**
    * 后台保存
    */
    @RequestMapping("/save")
    public R save(@RequestBody CourseenrollmentEntity courseenrollment, HttpServletRequest request){
        //ValidatorUtils.validateEntity(courseenrollment);
        courseenrollment.setId(null);
        courseenrollment.setAuditstatus("待审核");
        if (courseenrollment!= null && courseenrollment.getUserid() == null) {
            courseenrollment.setUserid((Long)request.getSession().getAttribute("userId"));
        }
        fillEnrollmentLockedFields(courseenrollment, request);
        courseenrollmentService.save(courseenrollment);
        {
            NotifyEntity nf_0 = new NotifyEntity();
            Long nf_0_userId = null;
            nf_0_userId = (Long)request.getSession().getAttribute("userId");
            if (nf_0_userId == null) {
                nf_0_userId = 1L;
            }
            nf_0.setUserid(nf_0_userId);
            nf_0.setTitle("课程报名已提交");
            nf_0.setContent(String.format("您已提交报名课程《%s》，请等待教练审核，上课时间：%s", courseenrollment.getCoursename(), courseenrollment.getClasstime()));
            nf_0.setMessagetype("报名通知");
            nf_0.setReadstatus("未读");
            nf_0.setSenduser("系统");
            notifyService.save(nf_0);
        }
        operationLogRecorder.record("courseenrollment", "课程报名记录", "新增", courseenrollment, request);
        return R.ok().put("data",courseenrollment.getId());
    }

    /**
    * 前台保存
    */
    @RequestMapping("/add")
    public R add(@RequestBody CourseenrollmentEntity courseenrollment, HttpServletRequest request){
        //ValidatorUtils.validateEntity(courseenrollment);
        courseenrollment.setId(null);
        courseenrollment.setAuditstatus("待审核");
        if (courseenrollment!= null && courseenrollment.getUserid() == null) {
            courseenrollment.setUserid((Long)request.getSession().getAttribute("userId"));
        }
        fillEnrollmentLockedFields(courseenrollment, request);
        courseenrollmentService.save(courseenrollment);
        {
            NotifyEntity nf_0 = new NotifyEntity();
            Long nf_0_userId = null;
            nf_0_userId = (Long)request.getSession().getAttribute("userId");
            if (nf_0_userId == null) {
                nf_0_userId = 1L;
            }
            nf_0.setUserid(nf_0_userId);
            nf_0.setTitle("课程报名已提交");
            nf_0.setContent(String.format("您已提交报名课程《%s》，请等待教练审核，上课时间：%s", courseenrollment.getCoursename(), courseenrollment.getClasstime()));
            nf_0.setMessagetype("报名通知");
            nf_0.setReadstatus("未读");
            nf_0.setSenduser("系统");
            notifyService.save(nf_0);
        }
        operationLogRecorder.record("courseenrollment", "课程报名记录", "新增", courseenrollment, request);
        return R.ok().put("data",courseenrollment.getId());
    }


    /**
    * 修改
    */
    @RequestMapping("/update")
    @Transactional
    public R update(@RequestBody CourseenrollmentEntity courseenrollment, HttpServletRequest request){
        //ValidatorUtils.validateEntity(courseenrollment);
        //全部更新
        courseenrollmentService.updateById(courseenrollment);
        {
            boolean _send = true;
            _send = "已支付".equals(String.valueOf(courseenrollment.getIspay()));
            if (_send) {
            NotifyEntity nf_1 = new NotifyEntity();
            Long nf_1_userId = null;
            nf_1_userId = (Long)request.getSession().getAttribute("userId");
            if (nf_1_userId == null) {
                nf_1_userId = 1L;
            }
            nf_1.setUserid(nf_1_userId);
            nf_1.setTitle(String.format("课程支付成功", courseenrollment.getCoursename(), courseenrollment.getClasstime()));
            nf_1.setContent(String.format("您的课程《%s》已支付成功，上课时间：%s", courseenrollment.getCoursename(), courseenrollment.getClasstime()));
            nf_1.setMessagetype("支付通知");
            nf_1.setReadstatus("未读");
            nf_1.setSenduser("系统");
            notifyService.save(nf_1);
            }
        }
        if ("已支付".equals(String.valueOf(courseenrollment.getIspay()))) {
            ensureCoachMemberBinding(courseenrollment, request);
        }
        {
            boolean _send = true;
            _send = "已取消".equals(String.valueOf(courseenrollment.getOrderstatus()));
            if (_send) {
            NotifyEntity nf_2 = new NotifyEntity();
            Long nf_2_userId = null;
            nf_2_userId = (Long)request.getSession().getAttribute("userId");
            if (nf_2_userId == null) {
                nf_2_userId = 1L;
            }
            nf_2.setUserid(nf_2_userId);
            nf_2.setTitle(String.format("课程报名已取消", courseenrollment.getCoursename()));
            nf_2.setContent(String.format("您的课程《%s》报名已取消", courseenrollment.getCoursename()));
            nf_2.setMessagetype("取消通知");
            nf_2.setReadstatus("未读");
            nf_2.setSenduser("系统");
            notifyService.save(nf_2);
            }
        }
        operationLogRecorder.record("courseenrollment", "课程报名记录", "修改", courseenrollment, request);
        return R.ok();
    }

    /**
    * 报名审核（教练/管理员）
    */
    @RequestMapping("/audit")
    @Transactional
    public R audit(@RequestBody Map<String, Object> params, HttpServletRequest request){
        Long id = Long.parseLong(params.get("id").toString());
        String auditstatus = params.get("auditstatus").toString();
        CourseenrollmentEntity enrollment = courseenrollmentService.getById(id);
        if(enrollment == null) {
            return R.error("报名记录不存在");
        }
        enrollment.setAuditstatus(auditstatus);
        courseenrollmentService.updateById(enrollment);
        // 审核通过：创建教练-学员绑定，发送通知
        if("已通过".equals(auditstatus)) {
            ensureCoachMemberBinding(enrollment, request);
            NotifyEntity nf = new NotifyEntity();
            nf.setUserid(enrollment.getUserid());
            nf.setTitle("报名审核通过");
            nf.setContent(String.format("您报名的课程《%s》已审核通过，请及时完成支付", enrollment.getCoursename()));
            nf.setMessagetype("审核通知");
            nf.setReadstatus("未读");
            nf.setSenduser("系统");
            notifyService.save(nf);
        }
        // 审核驳回：恢复课程名额，发送通知
        if("已驳回".equals(auditstatus)) {
            if(enrollment.getCoursename() != null) {
                QueryWrapper<CourseEntity> cw = new QueryWrapper<>();
                cw.eq("coursename", enrollment.getCoursename());
                CourseEntity course = courseService.getOne(cw);
                if(course != null && course.getQuota() != null) {
                    course.setQuota(course.getQuota() + 1);
                    courseService.updateById(course);
                }
            }
            NotifyEntity nf = new NotifyEntity();
            nf.setUserid(enrollment.getUserid());
            nf.setTitle("报名审核未通过");
            nf.setContent(String.format("您报名的课程《%s》审核未通过，课程名额已恢复", enrollment.getCoursename()));
            nf.setMessagetype("审核通知");
            nf.setReadstatus("未读");
            nf.setSenduser("系统");
            notifyService.save(nf);
        }
        operationLogRecorder.record("courseenrollment", "课程报名记录", "审核:" + auditstatus, enrollment, request);
        return R.ok();
    }

    /**
    * 删除
    */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids, HttpServletRequest request){
        courseenrollmentService.removeByIds(Arrays.asList(ids));
        operationLogRecorder.record("courseenrollment", "课程报名记录", "删除", Arrays.asList(ids), request);
        return R.ok();
    }



   /**
    * 前台智能排序
    */
    @IgnoreAuth
    @RequestMapping("/autoSort")
    public R autoSort(@RequestParam Map<String, Object> params, CourseenrollmentEntity courseenrollment, HttpServletRequest request, String pre){
        QueryWrapper<CourseenrollmentEntity> ew = new QueryWrapper<CourseenrollmentEntity>();
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
        PageUtils page = courseenrollmentService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, courseenrollment), params), params));
        return R.ok().put("data", page);
    }





}
