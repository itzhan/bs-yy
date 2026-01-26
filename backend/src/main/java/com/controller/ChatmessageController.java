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

import com.entity.ChatmessageEntity;
import com.entity.view.ChatmessageView;

import com.service.ChatmessageService;
import com.utils.PageUtils;
import com.utils.R;
import com.utils.MPUtil;
import com.utils.MapUtils;
import java.io.IOException;
import com.log.OperationLogRecorder;

/**
* 消息表
* 后端接口
* @author ds
* @email 1312461553@qq.com
* @date 2025-11-13 21:24:29
*/
@RestController
@RequestMapping("/chatmessage")
public class ChatmessageController {
    @Resource
    private ChatmessageService chatmessageService;
    @Resource
    private OperationLogRecorder operationLogRecorder;

    /**
    * 后台列表
    */
    @RequestMapping("/page")
    public R page(@RequestParam Map<String, Object> params, ChatmessageEntity chatmessage,
            HttpServletRequest request){
        QueryWrapper<ChatmessageEntity> ew = new QueryWrapper<ChatmessageEntity>();
        PageUtils page = chatmessageService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, chatmessage), params), params));
        Map<String, String> deSens = new HashMap<>();
        DeSensUtil.desensitize(page,deSens);
        return R.ok().put("data", page);
    }

    /**
    * 前台列表
    */
    @IgnoreAuth
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params, ChatmessageEntity chatmessage,
            HttpServletRequest request){
        QueryWrapper<ChatmessageEntity> ew = new QueryWrapper<ChatmessageEntity>();
        PageUtils page = chatmessageService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, chatmessage), params), params));
        Map<String, String> deSens = new HashMap<>();
        DeSensUtil.desensitize(page,deSens);
        return R.ok().put("data", page);
    }

    /**
     * 消息会话列表（按 uid/fid 双向）
     */
    @RequestMapping("/mlist")
    public R mlist(@RequestParam Map<String, Object> params, ChatmessageEntity chatmessage, HttpServletRequest request){
        QueryWrapper<ChatmessageEntity> ew = new QueryWrapper<ChatmessageEntity>();
        ew.and(w -> w.eq("uid", chatmessage.getUid()).eq("fid", chatmessage.getFid()))
          .or(w -> w.eq("fid", chatmessage.getUid()).eq("uid", chatmessage.getFid()));
        PageUtils page = chatmessageService.queryPage(params, ew);
        if (chatmessage.getFid() != null) {
            UpdateWrapper<ChatmessageEntity> uw = new UpdateWrapper<>();
            uw.eq("isread", 0).eq("fid", chatmessage.getUid()).eq("uid", chatmessage.getFid());
            chatmessageService.update(new ChatmessageEntity(), uw.set("isread", 1));
        }
        return R.ok().put("data", page);
    }

    /**
    * 列表
    */
    @RequestMapping("/lists")
    public R list(ChatmessageEntity chatmessage){
        QueryWrapper<ChatmessageEntity> ew = new QueryWrapper<ChatmessageEntity>();
        ew.allEq(MPUtil.allEQMapPre(chatmessage, "chatmessage"));
        return R.ok().put("data", chatmessageService.selectListView(ew));
    }


    /**
    * 查询
    */
    @RequestMapping("/query")
    public R query(ChatmessageEntity chatmessage){
        QueryWrapper<ChatmessageEntity> ew = new QueryWrapper<ChatmessageEntity>();
        ew.allEq(MPUtil.allEQMapPre(chatmessage, "chatmessage"));
        ChatmessageView chatmessageView = chatmessageService.selectView(ew);
        return R.ok("查询消息表成功").put("data", chatmessageView);
    }

    /**
    * 后台详情
    */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
        ChatmessageEntity chatmessage = chatmessageService.getById(id);
        Map<String, String> deSens = new HashMap<>();
        DeSensUtil.desensitize(chatmessage,deSens);
        return R.ok().put("data", chatmessage);
    }

    /**
    * 前台详情
    */
    @IgnoreAuth
    @RequestMapping("/detail/{id}")
    public R detail(@PathVariable("id") Long id){
        ChatmessageEntity chatmessage = chatmessageService.getById(id);
        Map<String, String> deSens = new HashMap<>();
        DeSensUtil.desensitize(chatmessage,deSens);
        return R.ok().put("data", chatmessage);
    }

    /**
    * 后台保存
    */
    @RequestMapping("/save")
    public R save(@RequestBody ChatmessageEntity chatmessage, HttpServletRequest request){
        //ValidatorUtils.validateEntity(chatmessage);
        chatmessage.setId(null);
        chatmessageService.save(chatmessage);
        operationLogRecorder.record("chatmessage", "消息表", "新增", chatmessage, request);
        return R.ok().put("data",chatmessage.getId());
    }

    /**
    * 前台保存
    */
    @RequestMapping("/add")
    public R add(@RequestBody ChatmessageEntity chatmessage, HttpServletRequest request){
        //ValidatorUtils.validateEntity(chatmessage);
        chatmessage.setId(null);
        chatmessageService.save(chatmessage);
        operationLogRecorder.record("chatmessage", "消息表", "新增", chatmessage, request);
        return R.ok().put("data",chatmessage.getId());
    }


    /**
    * 修改
    */
    @RequestMapping("/update")
    @Transactional
    @IgnoreAuth
    public R update(@RequestBody ChatmessageEntity chatmessage, HttpServletRequest request){
        //ValidatorUtils.validateEntity(chatmessage);
        //全部更新
        chatmessageService.updateById(chatmessage);
        operationLogRecorder.record("chatmessage", "消息表", "修改", chatmessage, request);
        return R.ok();
    }

    /**
    * 删除
    */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids, HttpServletRequest request){
        chatmessageService.removeByIds(Arrays.asList(ids));
        operationLogRecorder.record("chatmessage", "消息表", "删除", Arrays.asList(ids), request);
        return R.ok();
    }



   /**
    * 前台智能排序
    */
    @IgnoreAuth
    @RequestMapping("/autoSort")
    public R autoSort(@RequestParam Map<String, Object> params, ChatmessageEntity chatmessage, HttpServletRequest request, String pre){
        QueryWrapper<ChatmessageEntity> ew = new QueryWrapper<ChatmessageEntity>();
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
        PageUtils page = chatmessageService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, chatmessage), params), params));
        return R.ok().put("data", page);
    }





}
