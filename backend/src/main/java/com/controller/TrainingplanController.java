 package com.controller;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import cn.hutool.core.util.StrUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.annotation.IgnoreAuth;
import com.entity.TrainingplanEntity;
import com.entity.CoachEntity;
import com.entity.UserEntity;
import com.entity.CoachmemberEntity;
import com.entity.NotifyEntity;
import com.entity.view.TrainingplanView;
import com.service.TrainingplanService;
import com.service.CoachService;
import com.service.UserService;
import com.service.CoachmemberService;
import com.service.NotifyService;
import com.utils.DeSensUtil;
import com.utils.MPUtil;
import com.utils.PageUtils;
import com.utils.R;
import com.log.OperationLogRecorder;

/**
* 训练计划
* 后端接口
* @author ds
* @email 1312461553@qq.com
*/
@RestController
@RequestMapping("/trainingplan")
public class TrainingplanController {
    @Resource
    private TrainingplanService trainingplanService;
    @Resource
    private CoachService coachService;
    @Resource
    private UserService userService;
    @Resource
    private CoachmemberService coachmemberService;
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
        for(Long id : ids) {
            TrainingplanEntity plan = trainingplanService.getById(id);
            if (plan == null) {
                continue;
            }
            plan.setAuditstatus(auditstatus);
            plan.setAuditreply(auditreply);
            if ("通过".equals(auditstatus)) {
                plan.setPlanstatus("进行中");
            } else if ("不通过".equals(auditstatus) || "未通过".equals(auditstatus)) {
                plan.setPlanstatus("已驳回");
            }
            trainingplanService.updateById(plan);

            Long targetUserId = plan.getUserid();
            if (targetUserId == null) {
                Object sessionUser = request.getSession().getAttribute("userId");
                if (sessionUser instanceof Long) {
                    targetUserId = (Long) sessionUser;
                }
            }
            if (targetUserId != null) {
                NotifyEntity notify = new NotifyEntity();
                notify.setUserid(targetUserId);
                notify.setTitle(String.format("训练计划《%s》审核结果", plan.getPlanname()));
                notify.setContent(String.format("训练计划《%s》审核结果：%s", plan.getPlanname(), auditstatus));
                notify.setMessagetype("计划审核");
                notify.setReadstatus("未读");
                notify.setSenduser("系统");
                notifyService.save(notify);
            }
        }
        return R.ok();
    }

    /**
    * 后台列表
    */
    @RequestMapping("/page")
    public R page(@RequestParam Map<String, Object> params, TrainingplanEntity trainingplan,
            HttpServletRequest request){
        Object roleObj = request.getSession().getAttribute("role");
        Object tableObj = request.getSession().getAttribute("tableName");
        if(roleObj != null && !"管理员".equals(roleObj.toString())) {
            String tableName = tableObj == null ? "" : tableObj.toString();
            if("coach".equals(tableName)) {
                trainingplan.setCoachid((Long)request.getSession().getAttribute("userId"));
            } else if("user".equals(tableName)) {
                trainingplan.setUserid((Long)request.getSession().getAttribute("userId"));
            }
        }
        QueryWrapper<TrainingplanEntity> ew = new QueryWrapper<TrainingplanEntity>();
        PageUtils page = trainingplanService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, trainingplan), params), params));
        Map<String, String> deSens = new HashMap<>();
        DeSensUtil.desensitize(page,deSens);
        return R.ok().put("data", page);
    }

    /**
    * 前台列表
    */
    @IgnoreAuth
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params, TrainingplanEntity trainingplan,
            HttpServletRequest request){
        Object roleObj = request.getSession().getAttribute("role");
        Object tableObj = request.getSession().getAttribute("tableName");
        if(roleObj != null && !"管理员".equals(roleObj.toString())) {
            String tableName = tableObj == null ? "" : tableObj.toString();
            if("coach".equals(tableName)) {
                trainingplan.setCoachid((Long)request.getSession().getAttribute("userId"));
            } else if("user".equals(tableName)) {
                trainingplan.setUserid((Long)request.getSession().getAttribute("userId"));
            }
        }
        QueryWrapper<TrainingplanEntity> ew = new QueryWrapper<TrainingplanEntity>();
        PageUtils page = trainingplanService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, trainingplan), params), params));
        Map<String, String> deSens = new HashMap<>();
        DeSensUtil.desensitize(page,deSens);
        return R.ok().put("data", page);
    }

    /**
    * 列表
    */
    @RequestMapping("/lists")
    public R list(TrainingplanEntity trainingplan){
        QueryWrapper<TrainingplanEntity> ew = new QueryWrapper<TrainingplanEntity>();
        ew.allEq(MPUtil.allEQMapPre(trainingplan, "trainingplan"));
        return R.ok().put("data", trainingplanService.selectListView(ew));
    }

    /**
    * 查询
    */
    @RequestMapping("/query")
    public R query(TrainingplanEntity trainingplan){
        QueryWrapper<TrainingplanEntity> ew = new QueryWrapper<TrainingplanEntity>();
        ew.allEq(MPUtil.allEQMapPre(trainingplan, "trainingplan"));
        TrainingplanView trainingplanView = trainingplanService.selectView(ew);
        return R.ok("查询训练计划成功").put("data", trainingplanView);
    }

    /**
    * 后台详情
    */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
        TrainingplanEntity trainingplan = trainingplanService.getById(id);
        Map<String, String> deSens = new HashMap<>();
        DeSensUtil.desensitize(trainingplan,deSens);
        return R.ok().put("data", trainingplan);
    }

    /**
    * 前台详情
    */
    @IgnoreAuth
    @RequestMapping("/detail/{id}")
    public R detail(@PathVariable("id") Long id){
        TrainingplanEntity trainingplan = trainingplanService.getById(id);
        Map<String, String> deSens = new HashMap<>();
        DeSensUtil.desensitize(trainingplan,deSens);
        return R.ok().put("data", trainingplan);
    }

    private void fillUserCoachInfo(TrainingplanEntity trainingplan) {
        if (trainingplan == null) {
            return;
        }
        if (trainingplan.getCoachid() != null && (StrUtil.isBlank(trainingplan.getCoachname()) || StrUtil.isBlank(trainingplan.getCoachaccount()))) {
            CoachEntity coach = coachService.getById(trainingplan.getCoachid());
            if (coach != null) {
                if (StrUtil.isBlank(trainingplan.getCoachname())) {
                    trainingplan.setCoachname(coach.getCoachname());
                }
                if (StrUtil.isBlank(trainingplan.getCoachaccount())) {
                    trainingplan.setCoachaccount(coach.getCoachaccount());
                }
            }
        }
        if (trainingplan.getUserid() != null && (StrUtil.isBlank(trainingplan.getUsername()) || StrUtil.isBlank(trainingplan.getUseraccount()))) {
            UserEntity user = userService.getById(trainingplan.getUserid());
            if (user != null) {
                if (StrUtil.isBlank(trainingplan.getUsername())) {
                    trainingplan.setUsername(user.getName());
                }
                if (StrUtil.isBlank(trainingplan.getUseraccount())) {
                    trainingplan.setUseraccount(user.getUseraccount());
                }
            }
        }
    }

    private void applyDefaultStatus(TrainingplanEntity trainingplan, String tableName) {
        if (trainingplan == null) {
            return;
        }
        if ("coach".equals(tableName) || "admin".equals(tableName)) {
            if (StrUtil.isBlank(trainingplan.getAuditstatus())) {
                trainingplan.setAuditstatus("通过");
            }
            if (StrUtil.isBlank(trainingplan.getPlanstatus())) {
                trainingplan.setPlanstatus("进行中");
            }
        } else {
            if (StrUtil.isBlank(trainingplan.getAuditstatus())) {
                trainingplan.setAuditstatus("待审核");
            }
            if (StrUtil.isBlank(trainingplan.getPlanstatus())) {
                trainingplan.setPlanstatus("待审核");
            }
        }
    }

    private String validateCoachMemberBinding(String tableName, TrainingplanEntity trainingplan, HttpServletRequest request) {
        if (!"coach".equals(tableName)) {
            return null;
        }
        Long coachId = (Long) request.getSession().getAttribute("userId");
        if (coachId == null) {
            return "未获取到教练信息";
        }
        if (trainingplan.getUserid() == null) {
            return "请选择已绑定的会员";
        }
        CoachmemberEntity relation = coachmemberService.getOne(new QueryWrapper<CoachmemberEntity>()
                .eq("coachid", coachId)
                .eq("userid", trainingplan.getUserid()));
        if (relation == null) {
            return "当前会员未与您绑定，无法创建训练计划";
        }
        return null;
    }

    /**
    * 后台保存
    */
    @RequestMapping("/save")
    public R save(@RequestBody TrainingplanEntity trainingplan, HttpServletRequest request){
        trainingplan.setId(null);
        Object tableObj = request.getSession().getAttribute("tableName");
        String tableName = tableObj == null ? "" : tableObj.toString();
        if ("user".equals(tableName)) {
            return R.error("训练计划由教练制定，用户不可提交");
        }
        if ("coach".equals(tableName)) {
            trainingplan.setCoachid((Long)request.getSession().getAttribute("userId"));
            String error = validateCoachMemberBinding(tableName, trainingplan, request);
            if (error != null) {
                return R.error(error);
            }
        }
        fillUserCoachInfo(trainingplan);
        applyDefaultStatus(trainingplan, tableName);
        trainingplanService.save(trainingplan);
        operationLogRecorder.record("trainingplan", "训练计划", "新增", trainingplan, request);
        return R.ok().put("data",trainingplan.getId());
    }

    /**
    * 前台保存
    */
    @RequestMapping("/add")
    public R add(@RequestBody TrainingplanEntity trainingplan, HttpServletRequest request){
        trainingplan.setId(null);
        Object tableObj = request.getSession().getAttribute("tableName");
        String tableName = tableObj == null ? "" : tableObj.toString();
        if ("user".equals(tableName)) {
            return R.error("训练计划由教练制定，用户不可提交");
        }
        if ("coach".equals(tableName)) {
            trainingplan.setCoachid((Long)request.getSession().getAttribute("userId"));
            String error = validateCoachMemberBinding(tableName, trainingplan, request);
            if (error != null) {
                return R.error(error);
            }
        }
        fillUserCoachInfo(trainingplan);
        applyDefaultStatus(trainingplan, tableName);
        trainingplanService.save(trainingplan);
        operationLogRecorder.record("trainingplan", "训练计划", "新增", trainingplan, request);
        return R.ok().put("data",trainingplan.getId());
    }

    /**
    * 修改
    */
    @RequestMapping("/update")
    public R update(@RequestBody TrainingplanEntity trainingplan, HttpServletRequest request){
        Object tableObj = request.getSession().getAttribute("tableName");
        String tableName = tableObj == null ? "" : tableObj.toString();
        if ("user".equals(tableName)) {
            return R.error("训练计划由教练维护，用户不可修改");
        }
        if ("coach".equals(tableName)) {
            Long coachId = (Long)request.getSession().getAttribute("userId");
            TrainingplanEntity origin = trainingplan.getId() == null ? null : trainingplanService.getById(trainingplan.getId());
            if (origin != null && coachId != null && origin.getCoachid() != null && !origin.getCoachid().equals(coachId)) {
                return R.error("无权限修改其他教练的训练计划");
            }
            trainingplan.setCoachid(coachId);
            String error = validateCoachMemberBinding(tableName, trainingplan, request);
            if (error != null) {
                return R.error(error);
            }
        }
        trainingplanService.updateById(trainingplan);
        operationLogRecorder.record("trainingplan", "训练计划", "修改", trainingplan, request);
        return R.ok();
    }

    /**
    * 删除
    */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids, HttpServletRequest request){
        Object tableObj = request.getSession().getAttribute("tableName");
        String tableName = tableObj == null ? "" : tableObj.toString();
        if ("user".equals(tableName)) {
            return R.error("训练计划由教练维护，用户不可删除");
        }
        trainingplanService.removeByIds(Arrays.asList(ids));
        operationLogRecorder.record("trainingplan", "训练计划", "删除", Arrays.asList(ids), request);
        return R.ok();
    }
}
