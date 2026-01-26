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

import com.entity.NewsEntity;
import com.entity.view.NewsView;

import com.service.NewsService;
import com.utils.PageUtils;
import com.utils.R;
import com.utils.MPUtil;
import com.utils.MapUtils;
import java.io.IOException;
import com.log.OperationLogRecorder;

/**
* 公告资讯
* 后端接口
* @author ds
* @email 1312461553@qq.com
* @date 2025-11-13 21:24:29
*/
@RestController
@RequestMapping("/news")
public class NewsController {
    @Resource
    private NewsService newsService;
    @Resource
    private javax.sql.DataSource dataSource;
    @Resource
    private OperationLogRecorder operationLogRecorder;

    /**
     * 审核
     */
    @RequestMapping("/audit/batch")
    @Transactional
    public R auditBatch(@RequestBody Long[] ids, @RequestParam String auditstatus, @RequestParam String auditreply, HttpServletRequest request){
        List<NewsEntity> list = new ArrayList<NewsEntity>();
        for(Long id : ids) {
            NewsEntity news = newsService.getById(id);
            news.setAuditstatus(auditstatus);
            news.setAuditreply(auditreply);
            list.add(news);
        }
        newsService.updateBatchById(list);
        return R.ok();
    }
    /**
    * 后台列表
    */
    @RequestMapping("/page")
    public R page(@RequestParam Map<String, Object> params, NewsEntity news,
            HttpServletRequest request){
        QueryWrapper<NewsEntity> ew = new QueryWrapper<NewsEntity>();
        PageUtils page = newsService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, news), params), params));
        Map<String, String> deSens = new HashMap<>();
        DeSensUtil.desensitize(page,deSens);
        return R.ok().put("data", page);
    }

    /**
    * 前台列表
    */
    @IgnoreAuth
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params, NewsEntity news,
            HttpServletRequest request){
        QueryWrapper<NewsEntity> ew = new QueryWrapper<NewsEntity>();
        PageUtils page = newsService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, news), params), params));
        Map<String, String> deSens = new HashMap<>();
        DeSensUtil.desensitize(page,deSens);
        return R.ok().put("data", page);
    }


    /**
    * 列表
    */
    @RequestMapping("/lists")
    public R list(NewsEntity news){
        QueryWrapper<NewsEntity> ew = new QueryWrapper<NewsEntity>();
        ew.allEq(MPUtil.allEQMapPre(news, "news"));
        return R.ok().put("data", newsService.selectListView(ew));
    }


    /**
    * 查询
    */
    @RequestMapping("/query")
    public R query(NewsEntity news){
        QueryWrapper<NewsEntity> ew = new QueryWrapper<NewsEntity>();
        ew.allEq(MPUtil.allEQMapPre(news, "news"));
        NewsView newsView = newsService.selectView(ew);
        return R.ok("查询公告资讯成功").put("data", newsView);
    }

    /**
    * 后台详情
    */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
        NewsEntity news = newsService.getById(id);
        news.setClicknum(news.getClicknum()+1);
		news.setClicktime(new Date());
		newsService.updateById(news);
        news = newsService.selectView(new QueryWrapper<NewsEntity>().eq("id", id));
        Map<String, String> deSens = new HashMap<>();
        DeSensUtil.desensitize(news,deSens);
        return R.ok().put("data", news);
    }

    /**
    * 前台详情
    */
    @IgnoreAuth
    @RequestMapping("/detail/{id}")
    public R detail(@PathVariable("id") Long id){
        NewsEntity news = newsService.getById(id);
        news.setClicknum(news.getClicknum()+1);
		news.setClicktime(new Date());
		newsService.updateById(news);
        news = newsService.selectView(new QueryWrapper<NewsEntity>().eq("id", id));
        Map<String, String> deSens = new HashMap<>();
        DeSensUtil.desensitize(news,deSens);
        return R.ok().put("data", news);
    }

    /**
    * 后台保存
    */
    @RequestMapping("/save")
    public R save(@RequestBody NewsEntity news, HttpServletRequest request){
        //ValidatorUtils.validateEntity(news);
        news.setId(null);
        if ("管理员".equals(String.valueOf(request.getSession().getAttribute("role")))) {
            news.setAuditstatus("通过");
        }
        newsService.save(news);
        operationLogRecorder.record("news", "公告资讯", "新增", news, request);
        return R.ok().put("data",news.getId());
    }

    /**
    * 前台保存
    */
    @RequestMapping("/add")
    public R add(@RequestBody NewsEntity news, HttpServletRequest request){
        //ValidatorUtils.validateEntity(news);
        news.setId(null);
        newsService.save(news);
        operationLogRecorder.record("news", "公告资讯", "新增", news, request);
        return R.ok().put("data",news.getId());
    }


    /**
    * 修改
    */
    @RequestMapping("/update")
    @Transactional
    @IgnoreAuth
    public R update(@RequestBody NewsEntity news, HttpServletRequest request){
        //ValidatorUtils.validateEntity(news);
        //全部更新
        newsService.updateById(news);
        operationLogRecorder.record("news", "公告资讯", "修改", news, request);
        return R.ok();
    }

    /**
    * 删除
    */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids, HttpServletRequest request){
        newsService.removeByIds(Arrays.asList(ids));
        operationLogRecorder.record("news", "公告资讯", "删除", Arrays.asList(ids), request);
        return R.ok();
    }



    /**
    * 赞或踩
    */
    @RequestMapping("/like/{id}")
    public R like(@PathVariable("id") String id,String type){
        NewsEntity news = newsService.getById(id);
        if(type.equals("1")) {
            news.setLikenum(news.getLikenum()+1);
        } else {
            news.setStepnum(news.getStepnum()+1);
        }
        newsService.updateById(news);
        return R.ok("操作成功");
    }

   /**
    * 前台智能排序
    */
    @IgnoreAuth
    @RequestMapping("/autoSort")
    public R autoSort(@RequestParam Map<String, Object> params, NewsEntity news, HttpServletRequest request, String pre){
        QueryWrapper<NewsEntity> ew = new QueryWrapper<NewsEntity>();
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
        PageUtils page = newsService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, news), params), params));
        return R.ok().put("data", page);
    }

    /**
     * 协同推荐（基于用户收藏）
     * - 仅登录用户；若未登录或无收藏则回退为热门排序
     * - 返回数据结构与 /list 一致（data.list）以兼容前端
     */
    @IgnoreAuth
    @RequestMapping("/autoSort2")
    public R autoSort2(@RequestParam Map<String, Object> params, HttpServletRequest request) {
        Long uid = null;
        try { uid = (Long) request.getSession().getAttribute("userId"); } catch (Exception ignore) {}
        int limit = 8;
        try { if (params.get("limit") != null) limit = Integer.parseInt(String.valueOf(params.get("limit"))); } catch (Exception ignore) {}
        List<String> catePreferValues = Collections.emptyList();

        // 未登录则回退
        if (uid == null) {
            Map<String, Object> p = new HashMap<>();
            p.putAll(params);
            p.put("page", String.valueOf(1));
            p.put("limit", String.valueOf(limit));
            p.put("sort", "clicktime");
            p.put("order", "desc");
            QueryWrapper<NewsEntity> ew = new QueryWrapper<>();
            PageUtils page = newsService.queryPage(p, MPUtil.sort(ew, p));
            return R.ok().put("data", page);
        }

        List<Long> myFav = new ArrayList<>();
        List<Long> neighborUsers = new ArrayList<>();
        Map<Long, Integer> candidateScore = new HashMap<>();
        Set<Long> myFavSet = new java.util.HashSet<>();

        // 1) 查询当前用户在本表的收藏记录
        String q1 = "SELECT refid FROM social WHERE userid=? AND tablename=? AND type='1'";
        // 2) 找出也收藏了这些记录的其他用户
        // 3) 统计这些用户收藏的其它记录出现次数，作为推荐分数
        try (Connection conn = dataSource.getConnection()) {
            // step 1
            try (PreparedStatement ps = conn.prepareStatement(q1)) {
                ps.setLong(1, uid);
                ps.setString(2, "news");
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) { myFav.add(rs.getLong(1)); }
                }
            }
            myFavSet.addAll(myFav);

            if (!myFav.isEmpty()) {
                // step 2: 找相邻用户
                String inPlace = myFav.stream().map(x -> "?").collect(Collectors.joining(","));
                String q2 = "SELECT DISTINCT userid FROM social WHERE tablename=? AND type='1' AND userid<>? AND refid IN (" + inPlace + ")";
                try (PreparedStatement ps2 = conn.prepareStatement(q2)) {
                    int idx = 1;
                    ps2.setString(idx++, "news");
                    ps2.setLong(idx++, uid);
                    for (Long id : myFav) ps2.setLong(idx++, id);
                    try (ResultSet rs2 = ps2.executeQuery()) {
                        while (rs2.next()) neighborUsers.add(rs2.getLong(1));
                    }
                }

                if (!neighborUsers.isEmpty()) {
                    // step 3: 统计候选项
                    String inUsers = neighborUsers.stream().map(x -> "?").collect(Collectors.joining(","));
                    String q3 = "SELECT refid, COUNT(*) cnt FROM social WHERE tablename=? AND type='1' AND userid IN (" + inUsers + ") GROUP BY refid";
                    try (PreparedStatement ps3 = conn.prepareStatement(q3)) {
                        int idx3 = 1;
                        ps3.setString(idx3++, "news");
                        for (Long u : neighborUsers) ps3.setLong(idx3++, u);
                        try (ResultSet rs3 = ps3.executeQuery()) {
                            while (rs3.next()) {
                                long rid = rs3.getLong("refid");
                                int cnt = rs3.getInt("cnt");
                                if (!myFavSet.contains(rid)) {
                                    candidateScore.put(rid, candidateScore.getOrDefault(rid, 0) + cnt);
                                }
                            }
                        }
                    }
                }
            }
        } catch (SQLException e) {
            // 降级：任意异常回退热门
            Map<String, Object> p = new HashMap<>();
            p.putAll(params);
            p.put("page", String.valueOf(1));
            p.put("limit", String.valueOf(limit));
            p.put("sort", "clicktime");
            p.put("order", "desc");
            QueryWrapper<NewsEntity> ew = new QueryWrapper<>();
            PageUtils page = newsService.queryPage(p, MPUtil.sort(ew, p));
            return R.ok().put("data", page);
        }

        List<Map.Entry<Long,Integer>> sorted = new ArrayList<>(candidateScore.entrySet());
        sorted.sort((a,b) -> Integer.compare(b.getValue(), a.getValue()));
        List<NewsEntity> recList = new ArrayList<>();
        for (Map.Entry<Long,Integer> e : sorted) {
            NewsEntity one = newsService.getById(e.getKey());
            if (one != null) recList.add(one);
            if (recList.size() >= limit) break;
        }
        // 如果协同结果为空，回退热门
        if (recList.isEmpty()) {
            Map<String, Object> p = new HashMap<>();
            p.putAll(params);
            p.put("page", String.valueOf(1));
            p.put("limit", String.valueOf(limit));
            p.put("sort", "clicktime");
            p.put("order", "desc");
            QueryWrapper<NewsEntity> ew = new QueryWrapper<>();
            PageUtils page = newsService.queryPage(p, MPUtil.sort(ew, p));
            return R.ok().put("data", page);
        }

        Map<String, Object> data = new HashMap<>();
        data.put("list", recList);
        data.put("total", recList.size());
        return R.ok().put("data", data);
    }




}
