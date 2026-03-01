 package com.controller;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import cn.hutool.core.util.StrUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.annotation.IgnoreAuth;
import com.entity.CoachmemberEntity;
import com.entity.CoachEntity;
import com.entity.UserEntity;
import com.entity.view.CoachmemberView;
import com.service.CoachmemberService;
import com.service.CoachService;
import com.service.UserService;
import com.utils.DeSensUtil;
import com.utils.MPUtil;
import com.utils.PageUtils;
import com.utils.R;
import com.log.OperationLogRecorder;

/**
* 教练-会员绑定
* 后端接口
* @author ds
* @email 1312461553@qq.com
*/
@RestController
@RequestMapping("/coachmember")
public class CoachmemberController {
    @Resource
    private CoachmemberService coachmemberService;
    @Resource
    private CoachService coachService;
    @Resource
    private UserService userService;
    @Resource
    private OperationLogRecorder operationLogRecorder;

    /**
    * 后台列表
    */
    @RequestMapping("/page")
    public R page(@RequestParam Map<String, Object> params, CoachmemberEntity coachmember,
            HttpServletRequest request){
        Object roleObj = request.getSession().getAttribute("role");
        Object tableObj = request.getSession().getAttribute("tableName");
        if(roleObj != null && !"管理员".equals(roleObj.toString())) {
            String tableName = tableObj == null ? "" : tableObj.toString();
            if("coach".equals(tableName)) {
                coachmember.setCoachid((Long)request.getSession().getAttribute("userId"));
            } else if("user".equals(tableName)) {
                coachmember.setUserid((Long)request.getSession().getAttribute("userId"));
            }
        }
        QueryWrapper<CoachmemberEntity> ew = new QueryWrapper<CoachmemberEntity>();
        PageUtils page = coachmemberService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, coachmember), params), params));
        Map<String, String> deSens = new HashMap<>();
        DeSensUtil.desensitize(page,deSens);
        return R.ok().put("data", page);
    }

    /**
    * 前台列表
    */
    @IgnoreAuth
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params, CoachmemberEntity coachmember,
            HttpServletRequest request){
        QueryWrapper<CoachmemberEntity> ew = new QueryWrapper<CoachmemberEntity>();
        PageUtils page = coachmemberService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, coachmember), params), params));
        Map<String, String> deSens = new HashMap<>();
        DeSensUtil.desensitize(page,deSens);
        return R.ok().put("data", page);
    }

    /**
    * 列表
    */
    @RequestMapping("/lists")
    public R list(CoachmemberEntity coachmember){
        QueryWrapper<CoachmemberEntity> ew = new QueryWrapper<CoachmemberEntity>();
        ew.allEq(MPUtil.allEQMapPre(coachmember, "coachmember"));
        return R.ok().put("data", coachmemberService.selectListView(ew));
    }

    /**
    * 查询
    */
    @RequestMapping("/query")
    public R query(CoachmemberEntity coachmember){
        QueryWrapper<CoachmemberEntity> ew = new QueryWrapper<CoachmemberEntity>();
        ew.allEq(MPUtil.allEQMapPre(coachmember, "coachmember"));
        CoachmemberView coachmemberView = coachmemberService.selectView(ew);
        return R.ok("查询绑定记录成功").put("data", coachmemberView);
    }

    /**
    * 后台详情
    */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
        CoachmemberEntity coachmember = coachmemberService.getById(id);
        Map<String, String> deSens = new HashMap<>();
        DeSensUtil.desensitize(coachmember,deSens);
        return R.ok().put("data", coachmember);
    }

    /**
    * 前台详情
    */
    @IgnoreAuth
    @RequestMapping("/detail/{id}")
    public R detail(@PathVariable("id") Long id){
        CoachmemberEntity coachmember = coachmemberService.getById(id);
        Map<String, String> deSens = new HashMap<>();
        DeSensUtil.desensitize(coachmember,deSens);
        return R.ok().put("data", coachmember);
    }

    private void fillUserCoachInfo(CoachmemberEntity coachmember) {
        if (coachmember == null) {
            return;
        }
        if (coachmember.getCoachid() != null && (StrUtil.isBlank(coachmember.getCoachname()) || StrUtil.isBlank(coachmember.getCoachaccount()))) {
            CoachEntity coach = coachService.getById(coachmember.getCoachid());
            if (coach != null) {
                if (StrUtil.isBlank(coachmember.getCoachname())) {
                    coachmember.setCoachname(coach.getCoachname());
                }
                if (StrUtil.isBlank(coachmember.getCoachaccount())) {
                    coachmember.setCoachaccount(coach.getCoachaccount());
                }
            }
        }
        if (coachmember.getUserid() != null && (StrUtil.isBlank(coachmember.getUsername()) || StrUtil.isBlank(coachmember.getUseraccount()))) {
            UserEntity user = userService.getById(coachmember.getUserid());
            if (user != null) {
                if (StrUtil.isBlank(coachmember.getUsername())) {
                    coachmember.setUsername(user.getName());
                }
                if (StrUtil.isBlank(coachmember.getUseraccount())) {
                    coachmember.setUseraccount(user.getUseraccount());
                }
                if (StrUtil.isBlank(coachmember.getPhone())) {
                    coachmember.setPhone(user.getPhone());
                }
                if (StrUtil.isBlank(coachmember.getUserimage())) {
                    coachmember.setUserimage(user.getImage());
                }
            }
        }
        if (StrUtil.isBlank(coachmember.getBindstatus())) {
            coachmember.setBindstatus("已绑定");
        }
    }

    /**
    * 后台保存
    */
    @RequestMapping("/save")
    public R save(@RequestBody CoachmemberEntity coachmember, HttpServletRequest request){
        Object roleObj = request.getSession().getAttribute("role");
        if (roleObj == null || !"管理员".equals(String.valueOf(roleObj))) {
            return R.error("绑定关系由课程报名自动生成，仅管理员可维护");
        }
        coachmember.setId(null);
        Object tableObj = request.getSession().getAttribute("tableName");
        if (coachmember != null && tableObj != null) {
            String tableName = tableObj.toString();
            if ("user".equals(tableName) && coachmember.getUserid() == null) {
                coachmember.setUserid((Long)request.getSession().getAttribute("userId"));
            }
            if ("coach".equals(tableName) && coachmember.getCoachid() == null) {
                coachmember.setCoachid((Long)request.getSession().getAttribute("userId"));
            }
        }
        if (coachmember.getCoachid() != null && coachmember.getUserid() != null) {
            CoachmemberEntity exists = coachmemberService.getOne(new QueryWrapper<CoachmemberEntity>()
                    .eq("coachid", coachmember.getCoachid())
                    .eq("userid", coachmember.getUserid()));
            if (exists != null) {
                return R.error("该会员已绑定该教练");
            }
        }
        fillUserCoachInfo(coachmember);
        coachmemberService.save(coachmember);
        operationLogRecorder.record("coachmember", "教练-会员绑定", "新增", coachmember, request);
        return R.ok().put("data",coachmember.getId());
    }

    /**
    * 前台保存
    */
    @RequestMapping("/add")
    public R add(@RequestBody CoachmemberEntity coachmember, HttpServletRequest request){
        Object roleObj = request.getSession().getAttribute("role");
        if (roleObj == null || !"管理员".equals(String.valueOf(roleObj))) {
            return R.error("绑定关系由课程报名自动生成，仅管理员可维护");
        }
        coachmember.setId(null);
        Object tableObj = request.getSession().getAttribute("tableName");
        if (coachmember != null && tableObj != null) {
            String tableName = tableObj.toString();
            if ("user".equals(tableName) && coachmember.getUserid() == null) {
                coachmember.setUserid((Long)request.getSession().getAttribute("userId"));
            }
            if ("coach".equals(tableName) && coachmember.getCoachid() == null) {
                coachmember.setCoachid((Long)request.getSession().getAttribute("userId"));
            }
        }
        if (coachmember.getCoachid() != null && coachmember.getUserid() != null) {
            CoachmemberEntity exists = coachmemberService.getOne(new QueryWrapper<CoachmemberEntity>()
                    .eq("coachid", coachmember.getCoachid())
                    .eq("userid", coachmember.getUserid()));
            if (exists != null) {
                return R.error("该会员已绑定该教练");
            }
        }
        fillUserCoachInfo(coachmember);
        coachmemberService.save(coachmember);
        operationLogRecorder.record("coachmember", "教练-会员绑定", "新增", coachmember, request);
        return R.ok().put("data",coachmember.getId());
    }

    /**
    * 修改
    */
    @RequestMapping("/update")
    public R update(@RequestBody CoachmemberEntity coachmember, HttpServletRequest request){
        Object roleObj = request.getSession().getAttribute("role");
        if (roleObj == null || !"管理员".equals(String.valueOf(roleObj))) {
            return R.error("绑定关系由课程报名自动生成，仅管理员可维护");
        }
        coachmemberService.updateById(coachmember);
        operationLogRecorder.record("coachmember", "教练-会员绑定", "修改", coachmember, request);
        return R.ok();
    }

    /**
    * 删除
    */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids, HttpServletRequest request){
        Object roleObj = request.getSession().getAttribute("role");
        if (roleObj == null || !"管理员".equals(String.valueOf(roleObj))) {
            return R.error("绑定关系由课程报名自动生成，仅管理员可维护");
        }
        coachmemberService.removeByIds(Arrays.asList(ids));
        operationLogRecorder.record("coachmember", "教练-会员绑定", "删除", Arrays.asList(ids), request);
        return R.ok();
    }
}
