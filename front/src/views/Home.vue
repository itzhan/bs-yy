<template>
  <div class="home-wrap">
    <div class="home-container">
      <section v-for="(sec,idx) in sections" :key="idx" class="area-container">
        <!-- 公告 -->
        <template v-if="sec.type === 'news' && isAuth('news', '查看')">
          <div class="">
            <div class="area-title-box">
              <span class="area-title">{{ sec.title || '公告资讯' }}</span>
              <span class="area-subhead">NEWS</span>
            </div>
            <div v-if="news.list.length" class="area-content">
              <div class="area-content-unique">
                <div v-if="news.list[0]" class="area-unique-left" @click="toNewsDetail(news.list[0])">
                  <div class="img"><img :src="resolveImage(news.list[0].image)" alt="news-0" /></div>
                  <div class="title">{{ news.list[0].title }}</div>
                </div>
                <div class="area-unique-right">
                  <div v-for="n in news.list.slice(1,3)" :key="n.id" class="area-unique-item" @click="toNewsDetail(n)">
                    <div class="img"><img :src="resolveImage(n.image)" alt="news" /></div>
                    <div class="info">
                      <div class="title">{{ n.title }}</div>
                      <div class="desc">{{ n.introduction }}</div>
                      <span class="time">{{ (n.addtime || '').split(' ')[0] }}</span>
                    </div>
                  </div>
                </div>
              </div>
              <div class="area-content-split">
                <div v-for="(n, index) in news.list.slice(3)" :key="n.id" class="area-split-item" @click="toNewsDetail(n)">
                  <div class="title">{{ n.title }}</div>
                  <span class="time">{{ (n.addtime || '').split(' ')[0] }}</span>
                </div>
              </div>
            </div>
            <div class="more-btn-box" @click="router.push('/index/news')">
              <span class="more-btn">查看更多</span>
              <i class="icon iconfont icon-gengduo1"></i>
            </div>
          </div>
        </template>

        <!-- 推荐 -->
        <template v-else-if="sec.type === 'uniform' && isAuth(sec.table, '查看')">
          <div class="area-title-box">
            <h2 class="area-title">{{ sec.title || labelOf(sec.table) }}</h2>
            <div class="area-subhead">{{ (sec.table||'').toUpperCase() }} RECOMMEND</div>
          </div>
          <div :class="['area-content', sec.layout==='grid' ? 'grid' : 'media']">
            <div class="area-content-item" v-for="it in uniformData[idx] || []" :key="it.id" @click="toUniformDetail(sec.table, it)">
              <div class="thumb-box">
                <img v-if="coverOf(sec.table, it)" :src="coverOf(sec.table, it)" alt="cover" />
                <div v-else class="no-cover">无图</div>
              </div>
              <div class="info-box">
                <div class="info-items">
                  <div class="info-item" v-for="f in fieldsToShow(sec.table)" :key="f.name">
                    <span class="label">{{ f.label }}：</span>
                    <template v-if="f.formType === 'file'">
                      <span class="text file-links" v-if="it[f.name]">
                        <el-link
                          v-for="(fileItem, fileIndex) in String(it[f.name] || '').split(',').filter(Boolean)"
                          :key="fileIndex"
                          type="primary"
                          :underline="false"
                          @click.stop="openFileLink(fileItem)"
                        >
                          {{ formatFileName(fileItem, fileIndex) }}
                        </el-link>
                      </span>
                      <span class="text" v-else>暂无文件</span>
                    </template>
                    <span
                      v-else-if="f.formType === 'editor'"
                      class="text rich-text"
                      v-html="it[f.name] || ''"
                    ></span>
                    <span v-else class="text">{{ it[f.name] ?? '—' }}</span>
                  </div>
                </div>
                <div class="info-time">
                  <span class="icon">🕒</span>
                  <span class="label">发布时间：</span>
                  <span class="text">{{ (it.addtime || '').toString().split(' ')[0] }}</span>
                </div>
                <div class="info-stats">
                  <span v-if="hasStat(sec.table,'like')" class="stat"><span class="icon">👍</span><span class="num">{{ statValue(sec.table,'like', it) }}</span></span>
                  <span v-if="hasStat(sec.table,'favorite')" class="stat"><span class="icon">⭐</span><span class="num">{{ statValue(sec.table,'favorite', it) }}</span></span>
                  <span v-if="hasStat(sec.table,'click')" class="stat"><span class="icon">👁</span><span class="num">{{ statValue(sec.table,'click', it) }}</span></span>
                  <span v-if="hasStat(sec.table,'discuss')" class="stat"><span class="icon">💬</span><span class="num">{{ statValue(sec.table,'discuss', it) }}</span></span>
                </div>
              </div>
            </div>
          </div>
          <div class="more-btn-box"><el-button class="more-btn" size="small" @click="router.push('/index/' + sec.table)">查看更多</el-button></div>
        </template>

      </section>

      <!-- 空态兜底：当所有模块均无数据时显示 -->
      <section v-if="isAllEmpty" class="area-container">
        <el-card shadow="always">
          <div class="empty-title">欢迎使用 健身房运营管理系统 用户端</div>
          <div class="empty-sub">请选择上方导航进入模块</div>
        </el-card>
      </section>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted, computed, watch } from 'vue'
import { useRouter } from 'vue-router'
import { http } from '@/utils/request'
import { menuConfig } from '@/config/menu'
import { isAuth, useAuthVersion } from '@/utils/auth'

const router = useRouter()
const apiBase = import.meta.env.VITE_API_BASE_URL || ''
const token = ref(localStorage.getItem('frontToken') || '')
const authVersion = useAuthVersion()
const catePreferEnabled = false
const catePreferStorageBase = 'gym_vclqwy4_catePrefer'
const buildCatePreferKey = () => {
  const uid = localStorage.getItem('frontUserid') || localStorage.getItem('userid') || ''
  return uid ? `${catePreferStorageBase}_${uid}` : catePreferStorageBase
}
function resolveCatePreferParam(table?: string) {
  if (!catePreferEnabled || !table) return ''
  try {
    const raw = localStorage.getItem(buildCatePreferKey())
    if (!raw) return ''
    const parsed = JSON.parse(raw)
    const values = parsed?.[table]
    if (Array.isArray(values) && values.length) {
      return values.join(',')
    }
  } catch (error) {
    console.warn('读取分类偏好失败', error)
  }
  return ''
}

// 统一展示元数据由 FieldHelperEnricher 预先计算
const uniformMeta = {
  'user': {
    label: '用户',
    imageField: 'image',
    titleField: 'name',
    summaryFields: [
          { name: 'useraccount', label: '账号', formType: 'input' }
          ,{ name: 'sex', label: '性别', formType: 'radio' }
          ,{ name: 'age', label: '年龄', formType: 'number' }
          ,{ name: 'phone', label: '手机号码', formType: 'input' }
          ,{ name: 'money', label: '余额', formType: 'number' }
          ,{ name: 'addtime', label: '创建时间', formType: 'datetime' }
    ],
    likeField: null,
    favoriteField: null,
    clickField: null,
    discussField: null
  },
  'coach': {
    label: '教练',
    imageField: 'coachimage',
    titleField: 'coachaccount',
    summaryFields: [
          { name: 'coachname', label: '教练姓名', formType: 'input' }
          ,{ name: 'sex', label: '性别', formType: 'radio' }
          ,{ name: 'phone', label: '手机号码', formType: 'input' }
          ,{ name: 'jobno', label: '工号', formType: 'input' }
          ,{ name: 'specialty', label: '擅长领域', formType: 'input' }
          ,{ name: 'coachlevel', label: '教练等级', formType: 'select' }
    ],
    likeField: null,
    favoriteField: null,
    clickField: null,
    discussField: null
  },
  'coursetype': {
    label: '课程分类',
    imageField: null,
    titleField: 'coursetypename',
    summaryFields: [
          { name: 'addtime', label: '创建时间', formType: 'datetime' }
    ],
    likeField: null,
    favoriteField: null,
    clickField: null,
    discussField: null
  },
  'course': {
    label: '健身课程',
    imageField: 'courseimage',
    titleField: 'coursename',
    summaryFields: [
          { name: 'coursetype', label: '课程分类', formType: 'select' }
          ,{ name: 'classtime', label: '上课时间', formType: 'datetime' }
          ,{ name: 'duration', label: '课程时长', formType: 'number' }
          ,{ name: 'coachname', label: '教练', formType: 'select' }
          ,{ name: 'quota', label: '剩余名额', formType: 'number' }
          ,{ name: 'addtime', label: '创建时间', formType: 'datetime' }
    ],
    likeField: 'likenum',
    favoriteField: 'favoritenum',
    clickField: 'clicknum',
    discussField: 'discussnum'
  },
  'courseenrollment': {
    label: '课程报名记录',
    imageField: 'courseimage',
    titleField: 'coursename',
    summaryFields: [
          { name: 'coursetype', label: '课程分类', formType: 'select' }
          ,{ name: 'classtime', label: '上课时间', formType: 'datetime' }
          ,{ name: 'coachname', label: '教练', formType: 'input' }
          ,{ name: 'courseprice', label: '课程单价', formType: 'number' }
          ,{ name: 'totalprice', label: '总价', formType: 'number' }
          ,{ name: 'addtime', label: '创建时间', formType: 'datetime' }
    ],
    likeField: null,
    favoriteField: null,
    clickField: null,
    discussField: null
  },
  'producttype': {
    label: '商品分类',
    imageField: null,
    titleField: 'producttypename',
    summaryFields: [
          { name: 'addtime', label: '创建时间', formType: 'datetime' }
    ],
    likeField: null,
    favoriteField: null,
    clickField: null,
    discussField: null
  },
  'product': {
    label: '健身商品',
    imageField: 'productimage',
    titleField: 'productname',
    summaryFields: [
          { name: 'producttype', label: '商品分类', formType: 'select' }
          ,{ name: 'productprice', label: '价格', formType: 'number' }
          ,{ name: 'stock', label: '库存', formType: 'number' }
          ,{ name: 'addtime', label: '创建时间', formType: 'datetime' }
          ,{ name: 'auditstatus', label: '审核状态', formType: 'select' }
    ],
    likeField: 'likenum',
    favoriteField: 'favoritenum',
    clickField: 'clicknum',
    discussField: 'discussnum'
  },
  'productorder': {
    label: '商品订单',
    imageField: 'productimage',
    titleField: 'productname',
    summaryFields: [
          { name: 'producttype', label: '商品分类', formType: 'select' }
          ,{ name: 'productprice', label: '商品单价', formType: 'number' }
          ,{ name: 'quantity', label: '购买数量', formType: 'number' }
          ,{ name: 'totalprice', label: '订单总价', formType: 'number' }
          ,{ name: 'addtime', label: '创建时间', formType: 'datetime' }
          ,{ name: 'ispay', label: '是否支付', formType: 'select' }
    ],
    likeField: null,
    favoriteField: null,
    clickField: null,
    discussField: 'discussnum'
  },
  'membershippackage': {
    label: '会员卡套餐',
    imageField: 'packageimage',
    titleField: 'packagename',
    summaryFields: [
          { name: 'packagetype', label: '套餐类型', formType: 'select' }
          ,{ name: 'packageprice', label: '价格', formType: 'number' }
          ,{ name: 'includedcourses', label: '包含课时', formType: 'number' }
          ,{ name: 'addtime', label: '创建时间', formType: 'datetime' }
    ],
    likeField: null,
    favoriteField: null,
    clickField: null,
    discussField: null
  },
  'cardapplication': {
    label: '办卡记录',
    imageField: 'packageimage',
    titleField: 'packagename',
    summaryFields: [
          { name: 'packagetype', label: '套餐类型', formType: 'input' }
          ,{ name: 'packageprice', label: '价格', formType: 'number' }
          ,{ name: 'includedcourses', label: '包含课时', formType: 'number' }
          ,{ name: 'addtime', label: '创建时间', formType: 'datetime' }
          ,{ name: 'ispay', label: '是否支付', formType: 'select' }
          ,{ name: 'orderstatus', label: '状态', formType: 'select' }
    ],
    likeField: null,
    favoriteField: null,
    clickField: null,
    discussField: null
  },
  'cardrenewal': {
    label: '续卡记录',
    imageField: null,
    titleField: 'packagename',
    summaryFields: [
          { name: 'packagetype', label: '套餐类型', formType: 'input' }
          ,{ name: 'packageprice', label: '价格', formType: 'number' }
          ,{ name: 'renewaldays', label: '续费时长', formType: 'number' }
          ,{ name: 'addtime', label: '创建时间', formType: 'datetime' }
          ,{ name: 'ispay', label: '是否支付', formType: 'select' }
          ,{ name: 'orderstatus', label: '状态', formType: 'select' }
    ],
    likeField: null,
    favoriteField: null,
    clickField: null,
    discussField: null
  },
  'sharetype': {
    label: '交流分类',
    imageField: null,
    titleField: 'sharetypename',
    summaryFields: [
          { name: 'addtime', label: '创建时间', formType: 'datetime' }
    ],
    likeField: null,
    favoriteField: null,
    clickField: null,
    discussField: null
  },
  'share': {
    label: '会员交流',
    imageField: 'shareimage',
    titleField: 'sharetitle',
    summaryFields: [
          { name: 'sharetype', label: '分类', formType: 'select' }
          ,{ name: 'addtime', label: '创建时间', formType: 'datetime' }
          ,{ name: 'clicknum', label: '点击数', formType: 'number' }
          ,{ name: 'auditstatus', label: '审核状态', formType: 'select' }
    ],
    likeField: 'likenum',
    favoriteField: 'favoritenum',
    clickField: 'clicknum',
    discussField: 'discussnum'
  },
  'feedback': {
    label: '意见反馈',
    imageField: null,
    titleField: 'feedbacktitle',
    summaryFields: [
          { name: 'feedbacktype', label: '反馈类型', formType: 'select' }
          ,{ name: 'addtime', label: '创建时间', formType: 'datetime' }
          ,{ name: 'auditstatus', label: '审核状态', formType: 'select' }
          ,{ name: 'auditreply', label: '审核回复', formType: 'textarea' }
    ],
    likeField: null,
    favoriteField: null,
    clickField: null,
    discussField: null
  },
  'discusscourse': {
    label: '健身课程评论表',
    imageField: 'nickname',
    titleField: 'nickname',
    summaryFields: [
          { name: 'avatarurl', label: '头像', formType: '' }
          ,{ name: 'content', label: '评论内容', formType: '' }
          ,{ name: 'reply', label: '回复内容', formType: '' }
          ,{ name: 'addtime', label: '创建时间', formType: '' }
    ],
    likeField: null,
    favoriteField: null,
    clickField: null,
    discussField: null
  },
  'cancelcourseenrollment': {
    label: '取消课程报名记录',
    imageField: 'courseimage',
    titleField: 'coursename',
    summaryFields: [
          { name: 'coursetype', label: '课程分类', formType: 'select' }
          ,{ name: 'classtime', label: '上课时间', formType: 'datetime' }
          ,{ name: 'coachname', label: '教练', formType: 'input' }
          ,{ name: 'cancelreason', label: '取消原因', formType: 'textarea' }
          ,{ name: 'addtime', label: '创建时间', formType: 'datetime' }
    ],
    likeField: null,
    favoriteField: null,
    clickField: null,
    discussField: null
  },
  'refundcourseenrollment': {
    label: '课程报名记录退款',
    imageField: 'courseimage',
    titleField: 'coursename',
    summaryFields: [
          { name: 'coursetype', label: '课程分类', formType: 'select' }
          ,{ name: 'classtime', label: '上课时间', formType: 'datetime' }
          ,{ name: 'coachname', label: '教练', formType: 'input' }
          ,{ name: 'refundreason', label: '退款原因', formType: 'textarea' }
          ,{ name: 'addtime', label: '创建时间', formType: 'datetime' }
    ],
    likeField: null,
    favoriteField: null,
    clickField: null,
    discussField: null
  },
  'discussproduct': {
    label: '健身商品评论表',
    imageField: 'nickname',
    titleField: 'nickname',
    summaryFields: [
          { name: 'avatarurl', label: '头像', formType: '' }
          ,{ name: 'content', label: '评论内容', formType: '' }
          ,{ name: 'reply', label: '回复内容', formType: '' }
          ,{ name: 'addtime', label: '创建时间', formType: '' }
    ],
    likeField: null,
    favoriteField: null,
    clickField: null,
    discussField: null
  },
  'cancelcardapplication': {
    label: '取消办卡记录',
    imageField: 'packageimage',
    titleField: 'packagename',
    summaryFields: [
          { name: 'packagetype', label: '套餐类型', formType: 'input' }
          ,{ name: 'packageprice', label: '价格', formType: 'number' }
          ,{ name: 'includedcourses', label: '包含课时', formType: 'number' }
          ,{ name: 'cancelreason', label: '取消原因', formType: 'textarea' }
          ,{ name: 'addtime', label: '创建时间', formType: 'datetime' }
    ],
    likeField: null,
    favoriteField: null,
    clickField: null,
    discussField: null
  },
  'refundcardapplication': {
    label: '办卡记录退款',
    imageField: 'packageimage',
    titleField: 'packagename',
    summaryFields: [
          { name: 'packagetype', label: '套餐类型', formType: 'input' }
          ,{ name: 'packageprice', label: '价格', formType: 'number' }
          ,{ name: 'includedcourses', label: '包含课时', formType: 'number' }
          ,{ name: 'refundreason', label: '退款原因', formType: 'textarea' }
          ,{ name: 'addtime', label: '创建时间', formType: 'datetime' }
    ],
    likeField: null,
    favoriteField: null,
    clickField: null,
    discussField: null
  },
  'cancelproductorder': {
    label: '取消商品订单',
    imageField: 'productimage',
    titleField: 'productname',
    summaryFields: [
          { name: 'producttype', label: '商品分类', formType: 'select' }
          ,{ name: 'productprice', label: '商品单价', formType: 'number' }
          ,{ name: 'quantity', label: '购买数量', formType: 'number' }
          ,{ name: 'totalprice', label: '订单总价', formType: 'number' }
          ,{ name: 'cancelreason', label: '取消原因', formType: 'textarea' }
          ,{ name: 'addtime', label: '创建时间', formType: 'datetime' }
    ],
    likeField: null,
    favoriteField: null,
    clickField: null,
    discussField: null
  },
  'refundproductorder': {
    label: '商品订单退款',
    imageField: 'productimage',
    titleField: 'productname',
    summaryFields: [
          { name: 'producttype', label: '商品分类', formType: 'select' }
          ,{ name: 'productprice', label: '商品单价', formType: 'number' }
          ,{ name: 'quantity', label: '购买数量', formType: 'number' }
          ,{ name: 'totalprice', label: '订单总价', formType: 'number' }
          ,{ name: 'refundreason', label: '退款原因', formType: 'textarea' }
          ,{ name: 'addtime', label: '创建时间', formType: 'datetime' }
    ],
    likeField: null,
    favoriteField: null,
    clickField: null,
    discussField: null
  },
  'discussproductorder': {
    label: '商品订单评论表',
    imageField: 'nickname',
    titleField: 'nickname',
    summaryFields: [
          { name: 'avatarurl', label: '头像', formType: '' }
          ,{ name: 'content', label: '评论内容', formType: '' }
          ,{ name: 'reply', label: '回复内容', formType: '' }
          ,{ name: 'addtime', label: '创建时间', formType: '' }
    ],
    likeField: null,
    favoriteField: null,
    clickField: null,
    discussField: null
  },
  'cancelcardrenewal': {
    label: '取消续卡记录',
    imageField: null,
    titleField: 'packagename',
    summaryFields: [
          { name: 'packagetype', label: '套餐类型', formType: 'input' }
          ,{ name: 'packageprice', label: '价格', formType: 'number' }
          ,{ name: 'renewaldays', label: '续费时长', formType: 'number' }
          ,{ name: 'cancelreason', label: '取消原因', formType: 'textarea' }
          ,{ name: 'addtime', label: '创建时间', formType: 'datetime' }
    ],
    likeField: null,
    favoriteField: null,
    clickField: null,
    discussField: null
  },
  'refundcardrenewal': {
    label: '续卡记录退款',
    imageField: null,
    titleField: 'packagename',
    summaryFields: [
          { name: 'packagetype', label: '套餐类型', formType: 'input' }
          ,{ name: 'packageprice', label: '价格', formType: 'number' }
          ,{ name: 'renewaldays', label: '续费时长', formType: 'number' }
          ,{ name: 'refundreason', label: '退款原因', formType: 'textarea' }
          ,{ name: 'addtime', label: '创建时间', formType: 'datetime' }
    ],
    likeField: null,
    favoriteField: null,
    clickField: null,
    discussField: null
  },
  'discussshare': {
    label: '会员交流评论表',
    imageField: 'nickname',
    titleField: 'nickname',
    summaryFields: [
          { name: 'avatarurl', label: '头像', formType: '' }
          ,{ name: 'content', label: '评论内容', formType: '' }
          ,{ name: 'reply', label: '回复内容', formType: '' }
          ,{ name: 'addtime', label: '创建时间', formType: '' }
    ],
    likeField: null,
    favoriteField: null,
    clickField: null,
    discussField: null
  },
  'discussnews': {
    label: '公告资讯评论表',
    imageField: 'nickname',
    titleField: 'nickname',
    summaryFields: [
          { name: 'avatarurl', label: '头像', formType: '' }
          ,{ name: 'content', label: '评论内容', formType: '' }
          ,{ name: 'reply', label: '回复内容', formType: '' }
          ,{ name: 'addtime', label: '创建时间', formType: '' }
    ],
    likeField: null,
    favoriteField: null,
    clickField: null,
    discussField: null
  },
  'admin': {
    label: '管理员',
    imageField: 'image',
    titleField: 'adminaccount',
    summaryFields: [
          { name: 'role', label: '角色', formType: 'input' }
          ,{ name: 'addtime', label: '新增时间', formType: '' }
    ],
    likeField: null,
    favoriteField: null,
    clickField: null,
    discussField: null
  },
  'newstype': {
    label: '公告分类',
    imageField: null,
    titleField: 'typename',
    summaryFields: [
          { name: 'addtime', label: '创建时间', formType: '' }
    ],
    likeField: null,
    favoriteField: null,
    clickField: null,
    discussField: null
  },
  'news': {
    label: '公告资讯',
    imageField: 'image',
    titleField: 'title',
    summaryFields: [
          { name: 'introduction', label: '简介', formType: 'textarea' }
          ,{ name: 'typename', label: '分类名称', formType: 'select' }
          ,{ name: 'name', label: '发布人', formType: 'input' }
          ,{ name: 'content', label: '内容', formType: 'editor' }
          ,{ name: 'addtime', label: '创建时间', formType: '' }
          ,{ name: 'clicknum', label: '点击数', formType: '' }
    ],
    likeField: 'likenum',
    favoriteField: 'favoritenum',
    clickField: 'clicknum',
    discussField: 'discussnum'
  },
  'config': {
    label: '轮播图',
    imageField: 'url',
    titleField: 'name',
    summaryFields: [
          { name: 'value', label: '配置参数值', formType: '' }
    ],
    likeField: null,
    favoriteField: null,
    clickField: null,
    discussField: null
  },
  'log': {
    label: '操作日志',
    imageField: null,
    titleField: 'tablename',
    summaryFields: [
          { name: 'module', label: '模块名称', formType: 'input' }
          ,{ name: 'operatetype', label: '操作类型', formType: 'input' }
          ,{ name: 'operatorname', label: '操作人名称', formType: 'input' }
          ,{ name: 'requesturl', label: '请求路径', formType: 'input' }
          ,{ name: 'requestmethod', label: '请求方法', formType: 'input' }
          ,{ name: 'requestip', label: '请求IP', formType: 'input' }
    ],
    likeField: null,
    favoriteField: null,
    clickField: null,
    discussField: null
  },
  'notify': {
    label: '消息通知',
    imageField: null,
    titleField: 'title',
    summaryFields: [
          { name: 'messagetype', label: '消息类型', formType: 'select' }
          ,{ name: 'readstatus', label: '阅读状态', formType: 'select' }
          ,{ name: 'senduser', label: '发送人', formType: 'input' }
          ,{ name: 'addtime', label: '记录时间', formType: 'datetime' }
    ],
    likeField: null,
    favoriteField: null,
    clickField: null,
    discussField: null
  },
  'chat': {
    label: '在线客服',
    imageField: 'uimage',
    titleField: 'uname',
    summaryFields: [
          { name: 'isreply', label: '是否回复(0/1)', formType: '' }
          ,{ name: 'isread', label: '是否已读(0/1)', formType: '' }
          ,{ name: 'addtime', label: '创建时间', formType: '' }
    ],
    likeField: null,
    favoriteField: null,
    clickField: null,
    discussField: null
  },
  'chatmessage': {
    label: '消息表',
    imageField: null,
    titleField: 'tablename',
    summaryFields: [
          { name: 'format', label: '格式(1:文字，2:图片)', formType: '' }
          ,{ name: 'isread', label: '消息已读(0:未读，1:已读)', formType: '' }
          ,{ name: 'addtime', label: '创建时间', formType: '' }
    ],
    likeField: null,
    favoriteField: null,
    clickField: null,
    discussField: null
  },
  'friend': {
    label: '好友表',
    imageField: 'picture',
    titleField: 'name',
    summaryFields: [
          { name: 'role', label: '角色标识', formType: 'input' }
          ,{ name: 'tablename', label: '表名', formType: 'input' }
          ,{ name: 'alias', label: '好友备注', formType: 'input' }
          ,{ name: 'type', label: '关系类型', formType: 'number' }
          ,{ name: 'addtime', label: '新增时间', formType: '' }
    ],
    likeField: null,
    favoriteField: null,
    clickField: null,
    discussField: null
  }
}

type HomeSection = {
  type: 'news'|'uniform'
  title?: string
  limit?: number
  table?: string
  layout?: 'mediaList'|'grid'|'media'
  useAutoSort?: boolean
  sortField?: string
  sortOrder?: 'asc'|'desc'
}


const sections = ref<HomeSection[]>([
  { type: 'uniform', table: 'course', title: '健身课程推荐', layout: 'media', limit: 8, useAutoSort: true },
  { type: 'news', title: '公告资讯', limit: 7 },
])

function resolveImage(val?: string) {
  if (!val) return ''
  return /^https?:\/\//.test(val) ? val : apiBase + val
}

// 公告数据
const news = reactive({ list: [] as any[] })
async function fetchNews(limit = 7) {
  try {
    const res: any = await http.get('news/list', { params: { page: 1, limit, sort: 'addtime', order: 'desc' } })
    if (res?.code === 0) news.list = res.data?.list || []
  } catch {}
}
function toNewsDetail(n:any){ router.push({ path: '/index/newsDetail', query: { id: n.id } }) }

// 统一模块数据：按 sections 索引存放
const uniformData = reactive<Record<number, any[]>>({})
async function fetchUniform(idx:number, sec: HomeSection){
  if (!sec.table) return
  if (!canViewSection(sec)) {
    delete uniformData[idx]
    return
  }
  const limit = sec.limit || 8
  const useAuto = sec.useAutoSort !== false // 默认启用
  const url = useAuto ? `${sec.table}/${token.value?"autoSort2":"autoSort"}` : `${sec.table}/list`
  const params: any = { page: 1, limit }
  if (token.value) {
    const preferParam = resolveCatePreferParam(sec.table)
    if (preferParam) {
      params.catePrefer = preferParam
    }
  }
  if (!useAuto) {
    params.sort = sec.sortField || 'id'; params.order = sec.sortOrder || 'desc'
  }
  try {
    const res: any = await http.get(url, { params })
    if (res?.code === 0) uniformData[idx] = res.data?.list || []
  } catch { uniformData[idx] = [] }
}

// 生成期注入：每个表的统一展示元数据（图片字段、前6个isList字段、是否有赞/收藏/浏览）
function getTableMeta(table?: string) {
  if (!table) return undefined
  return uniformMeta[table]
}

function fieldsToShow(table?: string) {
  const meta = getTableMeta(table)
  return meta?.summaryFields || []
}

function labelOf(table?: string){
  if (!table) return '内容'
  const meta = getTableMeta(table)
  if (meta?.label) return meta.label
  const cfg: any[] = (menuConfig as any[])
  for (const role of cfg){
    const groups = role?.frontMenu || []
    for (const g of groups){
      const child = g?.child || []
      for (const c of child){ if (c?.tableName === table) return c?.menu || table }
    }
  }
  return table
}

function toUniformDetail(table: string | undefined, row: any){
  if (!table || !row?.id) return
  router.push({ path: `/index/${table}Detail`, query: { id: row.id } })
}

function coverOf(table: string | undefined, row:any){
  if (!row || !table) return ''
  const meta = getTableMeta(table)
  if (!meta?.imageField) return ''
  const raw = row[meta.imageField]
  if (!raw) return ''
  const first = String(raw).split(',')[0]
  return /^https?:\/\//.test(first)
    ? first
    : apiBase + (first.startsWith('/') ? first : '/' + first)
}

function resolveFileUrl(path?: string) {
  if (!path) return ''
  if (/^https?:\/\//.test(path)) return path
  const base = apiBase ? (apiBase.endsWith('/') ? apiBase.slice(0, -1) : apiBase) : ''
  const relative = path.startsWith('/') ? path : '/' + path
  return base ? base + relative : relative
}

function formatFileName(path: string, index: number) {
  if (!path) return '附件' + (index + 1)
  const segments = String(path).split('/')
  const raw = decodeURIComponent(segments[segments.length - 1] || '')
  if (!raw) return '附件' + (index + 1)
  const dot = raw.lastIndexOf('.')
  const extension = dot > 0 ? raw.slice(dot) : ''
  const basename = dot > 0 ? raw.slice(0, dot) : raw
  const limit = 12
  const truncated = basename.length > limit ? basename.slice(0, limit) + '…' : basename
  return truncated + extension
}

function openFileLink(path?: string) {
  if (!path) return
  const url = resolveFileUrl(path)
  if (!url) return
  const win = window.open(url, '_blank')
  win?.focus()
}

function hasStat(table: string | undefined, key: 'like' | 'favorite' | 'click' | 'discuss') {
  const meta = getTableMeta(table)
  if (!meta) return false
  if (key === 'like') return !!meta.likeField
  if (key === 'favorite') return !!meta.favoriteField
  if (key === 'click') return !!meta.clickField
  if (key === 'discuss') return !!meta.discussField
  return false
}

function statValue(table: string | undefined, key: 'like' | 'favorite' | 'click' | 'discuss', row: any) {
  const meta = getTableMeta(table)
  if (!meta || !row) return 0
  let field = ''
  if (key === 'like') field = meta.likeField || ''
  if (key === 'favorite') field = meta.favoriteField || ''
  if (key === 'click') field = meta.clickField || ''
  if (key === 'discuss') field = meta.discussField || ''
  return field ? (row[field] ?? 0) : 0
}

const isAllEmpty = computed(() => {
  authVersion.value
  const newsVisible = sections.value.some(sec => sec.type === 'news' && canViewSection(sec))
  const uniformVisible = sections.value.some((sec, idx) => sec.type === 'uniform' && canViewSection(sec) && (uniformData[idx] ?? []).length > 0)
  const hasNews = newsVisible && news.list.length > 0
  const hasUniform = uniformVisible
  return !(hasNews || hasUniform)
})

function canViewSection(sec: HomeSection): boolean {
  authVersion.value
  if (sec.type === 'uniform' && sec.table) {
    return isAuth(sec.table, '查看')
  }
  if (sec.type === 'news') {
    return isAuth('news', '查看')
  }
  return true
}

async function refreshSectionsData() {
  const tasks: Promise<any>[] = []
  sections.value.forEach((sec, idx) => {
    if (!canViewSection(sec)) {
      if (sec.type === 'uniform') delete uniformData[idx]
      if (sec.type === 'news') news.list = []
      return
    }
    if (sec.type === 'news') tasks.push(fetchNews(sec.limit || 7))
    if (sec.type === 'uniform') tasks.push(fetchUniform(idx, sec))
  })
  if (tasks.length) await Promise.allSettled(tasks)
}

onMounted(async () => {
  await refreshSectionsData()
})

watch(authVersion, () => {
  token.value = localStorage.getItem('frontToken') || ''
  refreshSectionsData()
})
</script>

<style>

</style>
