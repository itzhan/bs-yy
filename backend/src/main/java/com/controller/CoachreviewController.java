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
import com.entity.CoachreviewEntity;
import com.entity.CoachEntity;
import com.entity.UserEntity;
import com.entity.view.CoachreviewView;
import com.service.CoachreviewService;
import com.service.CoachService;
import com.service.UserService;
import com.utils.DeSensUtil;
import com.utils.MPUtil;
import com.utils.PageUtils;
import com.utils.R;
import com.log.OperationLogRecorder;

/**
* 教练评价
* 后端接口
* @author ds
* @email 1312461553@qq.com
*/
@RestController
@RequestMapping("/coachreview")
public class CoachreviewController {
    @Resource
    private CoachreviewService coachreviewService;
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
    public R page(@RequestParam Map<String, Object> params, CoachreviewEntity coachreview,
            HttpServletRequest request){
        Object roleObj = request.getSession().getAttribute("role");
        Object tableObj = request.getSession().getAttribute("tableName");
        if(roleObj != null && !"管理员".equals(roleObj.toString())) {
            String tableName = tableObj == null ? "" : tableObj.toString();
            if("coach".equals(tableName)) {
                coachreview.setCoachid((Long)request.getSession().getAttribute("userId"));
            } else if("user".equals(tableName)) {
                coachreview.setUserid((Long)request.getSession().getAttribute("userId"));
            }
        }
        QueryWrapper<CoachreviewEntity> ew = new QueryWrapper<CoachreviewEntity>();
        PageUtils page = coachreviewService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, coachreview), params), params));
        Map<String, String> deSens = new HashMap<>();
        DeSensUtil.desensitize(page,deSens);
        return R.ok().put("data", page);
    }

    /**
    * 前台列表
    */
    @IgnoreAuth
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params, CoachreviewEntity coachreview,
            HttpServletRequest request){
        QueryWrapper<CoachreviewEntity> ew = new QueryWrapper<CoachreviewEntity>();
        PageUtils page = coachreviewService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, coachreview), params), params));
        Map<String, String> deSens = new HashMap<>();
        DeSensUtil.desensitize(page,deSens);
        return R.ok().put("data", page);
    }

    /**
    * 列表
    */
    @RequestMapping("/lists")
    public R list(CoachreviewEntity coachreview){
        QueryWrapper<CoachreviewEntity> ew = new QueryWrapper<CoachreviewEntity>();
        ew.allEq(MPUtil.allEQMapPre(coachreview, "coachreview"));
        return R.ok().put("data", coachreviewService.selectListView(ew));
    }

    /**
    * 查询
    */
    @RequestMapping("/query")
    public R query(CoachreviewEntity coachreview){
        QueryWrapper<CoachreviewEntity> ew = new QueryWrapper<CoachreviewEntity>();
        ew.allEq(MPUtil.allEQMapPre(coachreview, "coachreview"));
        CoachreviewView coachreviewView = coachreviewService.selectView(ew);
        return R.ok("查询教练评价成功").put("data", coachreviewView);
    }

    /**
    * 后台详情
    */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
        CoachreviewEntity coachreview = coachreviewService.getById(id);
        Map<String, String> deSens = new HashMap<>();
        DeSensUtil.desensitize(coachreview,deSens);
        return R.ok().put("data", coachreview);
    }

    /**
    * 前台详情
    */
    @IgnoreAuth
    @RequestMapping("/detail/{id}")
    public R detail(@PathVariable("id") Long id){
        CoachreviewEntity coachreview = coachreviewService.getById(id);
        Map<String, String> deSens = new HashMap<>();
        DeSensUtil.desensitize(coachreview,deSens);
        return R.ok().put("data", coachreview);
    }

    private void fillUserCoachInfo(CoachreviewEntity coachreview) {
        if (coachreview == null) {
            return;
        }
        if (coachreview.getCoachid() != null && (StrUtil.isBlank(coachreview.getCoachname()) || StrUtil.isBlank(coachreview.getCoachaccount()))) {
            CoachEntity coach = coachService.getById(coachreview.getCoachid());
            if (coach != null) {
                if (StrUtil.isBlank(coachreview.getCoachname())) {
                    coachreview.setCoachname(coach.getCoachname());
                }
                if (StrUtil.isBlank(coachreview.getCoachaccount())) {
                    coachreview.setCoachaccount(coach.getCoachaccount());
                }
            }
        }
        if (coachreview.getUserid() != null && (StrUtil.isBlank(coachreview.getUsername()) || StrUtil.isBlank(coachreview.getUseraccount()))) {
            UserEntity user = userService.getById(coachreview.getUserid());
            if (user != null) {
                if (StrUtil.isBlank(coachreview.getUsername())) {
                    coachreview.setUsername(user.getName());
                }
                if (StrUtil.isBlank(coachreview.getUseraccount())) {
                    coachreview.setUseraccount(user.getUseraccount());
                }
                if (StrUtil.isBlank(coachreview.getUserimage())) {
                    coachreview.setUserimage(user.getImage());
                }
            }
        }
    }

    /**
    * 后台保存
    */
    @RequestMapping("/save")
    public R save(@RequestBody CoachreviewEntity coachreview, HttpServletRequest request){
        coachreview.setId(null);
        Object tableObj = request.getSession().getAttribute("tableName");
        if (coachreview != null && tableObj != null) {
            String tableName = tableObj.toString();
            if ("user".equals(tableName) && coachreview.getUserid() == null) {
                coachreview.setUserid((Long)request.getSession().getAttribute("userId"));
            }
            if ("coach".equals(tableName) && coachreview.getCoachid() == null) {
                coachreview.setCoachid((Long)request.getSession().getAttribute("userId"));
            }
        }
        fillUserCoachInfo(coachreview);
        coachreviewService.save(coachreview);
        operationLogRecorder.record("coachreview", "教练评价", "新增", coachreview, request);
        return R.ok().put("data",coachreview.getId());
    }

    /**
    * 前台保存
    */
    @RequestMapping("/add")
    public R add(@RequestBody CoachreviewEntity coachreview, HttpServletRequest request){
        coachreview.setId(null);
        Object tableObj = request.getSession().getAttribute("tableName");
        if (coachreview != null && tableObj != null) {
            String tableName = tableObj.toString();
            if ("user".equals(tableName) && coachreview.getUserid() == null) {
                coachreview.setUserid((Long)request.getSession().getAttribute("userId"));
            }
            if ("coach".equals(tableName) && coachreview.getCoachid() == null) {
                coachreview.setCoachid((Long)request.getSession().getAttribute("userId"));
            }
        }
        fillUserCoachInfo(coachreview);
        coachreviewService.save(coachreview);
        operationLogRecorder.record("coachreview", "教练评价", "新增", coachreview, request);
        return R.ok().put("data",coachreview.getId());
    }

    /**
    * 修改
    */
    @RequestMapping("/update")
    public R update(@RequestBody CoachreviewEntity coachreview, HttpServletRequest request){
        coachreviewService.updateById(coachreview);
        operationLogRecorder.record("coachreview", "教练评价", "修改", coachreview, request);
        return R.ok();
    }

    /**
    * 删除
    */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids, HttpServletRequest request){
        coachreviewService.removeByIds(Arrays.asList(ids));
        operationLogRecorder.record("coachreview", "教练评价", "删除", Arrays.asList(ids), request);
        return R.ok();
    }
}
