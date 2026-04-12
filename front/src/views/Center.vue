<template>
  <div class="center-container p-4">
    <el-card class="center-card">
      <template #header>
        <div class="center-card-header">
          <span class="center-title">个人中心</span>
          <el-button class="logout-btn" v-if="token" type="primary" link @click="logout">退出登录</el-button>
        </div>
      </template>
      <div v-if="!token" class="login-tip">请先登录</div>
      <div v-else>
        <el-tabs v-model="activeTab" class="category-container">
          <el-tab-pane label="个人信息" name="profile" class="center-pane">
            <div class="profile-pane form-container">
              <div class="profile-toolbar form-main" v-if="hasBalance">
                <span class="balance-label"><span class="form-item-label">当前余额</span><strong>{{ balanceDisplay || '未配置' }}</strong></span>
                <el-button class="recharge-btn" size="small" type="primary" @click="openRecharge">余额充值</el-button>
              </div>
              <div v-if="profileLoading" class="profile-loading">
                <el-skeleton :rows="6" animated />
              </div>
              <el-result
                class="profile-result"
                v-else-if="profileError"
                icon="warning"
                :title="profileError"
              />
              <component
                v-else-if="profileComponent"
                :is="profileComponent"
                ref="addOrUpdateRef"
                @refreshDataList="handleProfileUpdated"
                @success="handleProfileUpdated"
              />
              <el-empty class="profile-empty" v-else description="正在准备个人资料表单" />
              <el-dialog
                v-model="rechargeDialogVisible"
                title="余额充值"
                width="520px"
                append-to-body
                @close="resetRecharge"
              >
                <div class="recharge-dialog">
                  <div class="recharge-amount">
                    <span class="label">充值金额：</span>
                    <el-input
                      v-model="rechargeForm.amount"
                      type="number"
                      placeholder="请输入充值金额"
                    />
                  </div>
                  <div class="recharge-balance" v-if="hasBalance">
                    当前余额：<span class="value">{{ balanceDisplay }}</span>
                  </div>
                  <el-radio-group v-model="rechargeForm.method" class="recharge-options">
                    <div
                      v-for="opt in rechargeOptions"
                      :key="opt.value"
                      class="recharge-option"
                    >
                      <el-radio :label="opt.value" class="recharge-radio">
                        <div class="option-body">
                          <img :src="opt.img" :alt="opt.label" />
                          <span>{{ opt.label }}</span>
                        </div>
                      </el-radio>
                    </div>
                  </el-radio-group>
                </div>
                <template #footer>
                  <el-button @click="resetRecharge">取 消</el-button>
                  <el-button type="primary" :loading="rechargeLoading" @click="submitRecharge">确认充值</el-button>
                </template>
              </el-dialog>
            </div>
          </el-tab-pane>
          <el-tab-pane label="修改密码" name="password" class="center-pane">
            <div class="form-container">
              <el-form class="form-main" ref="pwdFormRef" :model="pwdForm" :rules="pwdRules" label-width="100px">
                <el-form-item label="原密码" prop="oldPassword"><el-input v-model="pwdForm.oldPassword" type="password" show-password placeholder="请输入原密码" /></el-form-item>
                <el-form-item label="新密码" prop="newPassword"><el-input v-model="pwdForm.newPassword" type="password" show-password placeholder="请输入新密码" /></el-form-item>
                <el-form-item label="确认密码" prop="confirmPassword"><el-input v-model="pwdForm.confirmPassword" type="password" show-password placeholder="请再次输入新密码" /></el-form-item>
                <el-form-item  class="form-actions">
                  <el-button class="confirm-btn" type="primary" @click="handleUpdatePassword" :loading="pwdLoading">确认修改</el-button>
                  <el-button class="cancel-btn" @click="resetPwdForm">重置</el-button>
                </el-form-item>
              </el-form>
            </div>
          </el-tab-pane>
          <el-tab-pane label="聊天记录" name="chat" class="center-pane">
            <div class="chat-container">
              <div v-if="chatPeers.length===0" class="no-access empty">暂无聊天记录</div>
              <div v-else class="chat-list">
                <div v-for="p in chatPeers" :key="p.key" class="chat-item">
                  <div class="chat-item-content" @click="openChat(p)">
                    <img v-if="p.avatarUrl" :src="p.avatarUrl" class="avatar" />
                    <div class="info">
                      <div class="info-top">
                        <span class="name">{{ p.displayName }}</span>
                        <span class="time">{{ p.lastTimeText }}</span>
                      </div>
                      <div class="last">{{ p.lastMsgLabel }}</div>
                    </div>
                  </div>
                  <el-button class="del-btn" link type="danger" @click="removePeer(p)">删除</el-button>
                </div>
              </div>
            </div>
            <!-- 聊天弹窗（在个人中心内直接对话） -->
            <el-dialog
              v-model="chatVisible"
              :title="chatDialogTitle"
              width="600px"
              @close="handleChatClose"
              class="chat-dialog"
            >
              <div ref="chatContentRef" class="chat-content">
                <div class="chat-timeline">
                  <template v-for="item in chatList" :key="item.id">
                    <div v-if="item.showTime" class="chat-time">{{ item.showTime }}</div>
                    <div class="chat-item" :class="isMine(item) ? 'mine' : 'other'">
                      <div v-if="!isMine(item) && peerAvatarUrl" class="chat-avatar">
                        <img :src="peerAvatarUrl" alt="好友头像" />
                      </div>
                      <div class="chat-bubble" :class="{ image: item.format === 2 }">
                        <el-alert
                          v-if="item.format === 1"
                          class="text-content"
                          :title="item.content"
                          :closable="false"
                          :type="isMine(item) ? 'success' : 'primary'"
                          :effect="'dark'"
                        />
                        <el-image
                          v-else
                          class="chat-image"
                          fit="cover"
                          :src="resolveFileUrl(item.content)"
                          :preview-src-list="[resolveFileUrl(item.content)]"
                        />
                      </div>
                      <div v-if="isMine(item) && myAvatarUrl" class="chat-avatar">
                        <img :src="myAvatarUrl" alt="我的头像" />
                      </div>
                    </div>
                  </template>
                  <div v-if="chatList.length === 0" class="chat-empty">暂无聊天内容</div>
                </div>
              </div>
              <template #footer>
                <div class="chat-footer">
                  <el-input
                    v-model="chatForm.content"
                    placeholder="请输入内容"
                    @keydown.enter="addChat(null)"
                    class="chat-input"
                  />
                  <el-button :disabled="!chatForm.content" type="primary" @click="addChat(null)">发送</el-button>
                  <el-upload class="chat-upload" :action="uploadUrl" :on-success="uploadSuccess" :show-file-list="false">
                    <el-button type="success">上传图片</el-button>
                  </el-upload>
                </div>
              </template>
            </el-dialog>
          </el-tab-pane>
          <el-tab-pane
            v-for="tab in centerTabDefs"
            :key="tab.name"
            :label="tab.label"
            :name="tab.name"
            class="center-pane"
          >
            <div class="business-container">
              <div class="business-toolbar">
                <div class="business-note">{{ tab.label }}</div>
                <div class="business-actions">
                  <el-button
                    v-if="tab.table === 'address'"
                    type="primary"
                    link
                    @click="goAddAddress"
                  >新增地址</el-button>
                  <el-button type="primary" link @click="refreshCenterTab(tab.name)">刷新</el-button>
                </div>
              </div>
              <div v-if="centerTabState[tab.name]?.loading" class="business-loading">
                <el-skeleton :rows="5" animated />
              </div>
              <el-result
                v-else-if="centerTabState[tab.name]?.error"
                icon="warning"
                :title="centerTabState[tab.name].error"
                class="no-access"
              />
              <div
                v-else-if="!centerTabState[tab.name] || centerTabState[tab.name].list.length === 0"
                class="business-empty"
              >
                <el-empty :description="tab.emptyText" class="no-access">
                  <template #extra>
                    <el-button
                      v-if="tab.table === 'address'"
                      type="primary"
                      link
                      @click="goAddAddress"
                    >新增地址</el-button>
                    <el-button type="primary" link @click="goCenterModule(tab)">去查看</el-button>
                  </template>
                </el-empty>
              </div>
              <template v-else>
                <el-table
                  :data="centerTabState[tab.name].list"
                  border
                  stripe
                  class="business-table"
                >
                  <el-table-column type="index" width="60" label="#" align="center" />
                  <el-table-column
                    v-for="field in tab.displayFields"
                    :key="field.prop"
                    :label="field.label"
                    :prop="field.prop"
                    align="center"
                  >
                    <template #default="{ row }">
                      <el-image
                        v-if="field.formType === 'image'"
                        :src="imgSrc(row[field.prop])"
                        fit="cover"
                        class="business-thumb"
                        :preview-src-list="row[field.prop] ? [imgSrc(row[field.prop])] : []"
                      />
                      <div v-else-if="field.formType === 'file'" class="business-file-list">
                        <el-link
                          v-for="(fileItem, fileIndex) in String(row[field.prop] || '').split(',').filter(item => item)"
                          :key="fileIndex"
                          type="primary"
                          :underline="false"
                          @click.stop="openFileLink(fileItem)"
                        >
                          {{ formatFileName(fileItem, fileIndex) }}
                        </el-link>
                      </div>
                      <span v-else>{{ formatCenterValue(row, field) }}</span>
                    </template>
                  </el-table-column>
                  <el-table-column
                    label="操作"
                    :width="tab.table === 'address' ? 220 : 120"
                    align="center"
                  >
                    <template #default="{ row }">
                      <template v-if="tab.table === 'address'">
                        <el-button
                          v-if="String(row.isdefault || '').trim() !== '是'"
                          type="primary"
                          link
                          @click="setAddressDefault(tab, row)"
                        >设为默认</el-button>
                        <el-tag
                          v-else
                          type="success"
                          effect="plain"
                          size="small"
                          class="address-default-tag"
                        >默认地址</el-tag>
                        <el-button type="danger" link @click="removeAddress(tab, row)">删除</el-button>
                      </template>
                      <el-button type="primary" link @click="openCenterDetail(tab, row)">查看</el-button>
                    </template>
                  </el-table-column>
                </el-table>
                <div class="business-pagination" v-if="centerTabState[tab.name].total > centerTabState[tab.name].limit">
                  <el-pagination
                    background
                    layout="prev, pager, next"
                    :current-page="centerTabState[tab.name].page"
                    :page-size="centerTabState[tab.name].limit"
                    :total="centerTabState[tab.name].total"
                    @current-change="page => handleCenterTabPageChange(tab.name, page)"
                  />
                </div>
                <div
                  v-if="tab.table === 'coachmember' && centerDetailState[tab.name]?.visible"
                  :id="`center-detail-${tab.name}`"
                  class="center-detail-block"
                >
                  <el-card shadow="hover">
                    <template #header>
                      <div class="center-detail-header">
                        <span>绑定详情</span>
                        <el-button type="primary" link @click="closeCenterDetail(tab.name)">收起</el-button>
                      </div>
                    </template>
                    <el-skeleton v-if="centerDetailState[tab.name].loading" :rows="4" animated />
                    <el-descriptions v-else-if="centerDetailState[tab.name].detail" :column="2" border>
                      <el-descriptions-item label="教练姓名">{{ centerDetailState[tab.name].detail.coachname }}</el-descriptions-item>
                      <el-descriptions-item label="教练账号">{{ centerDetailState[tab.name].detail.coachaccount }}</el-descriptions-item>
                      <el-descriptions-item label="绑定状态">{{ centerDetailState[tab.name].detail.bindstatus }}</el-descriptions-item>
                      <el-descriptions-item label="绑定时间">{{ centerDetailState[tab.name].detail.addtime }}</el-descriptions-item>
                      <el-descriptions-item label="备注" :span="2">{{ centerDetailState[tab.name].detail.remark }}</el-descriptions-item>
                    </el-descriptions>
                    <el-empty v-else description="暂无数据" />
                  </el-card>
                </div>
              </template>
            </div>
          </el-tab-pane>
          <el-tab-pane :label="notifyTabLabel" name="notify" class="center-pane">
            <div class="notify-pane">
              <div class="notify-toolbar">
                <div class="notify-summary">
                  <span>共 {{ notifyTotal }} 条</span>
                  <span v-if="notifyList.length">，未读 {{ notifyList.filter(item => String(item.readstatus) !== '1').length }} 条</span>
                </div>
                <div class="notify-actions">
                  <el-button type="primary" link @click="loadNotify(true)">刷新</el-button>
                  <el-button type="primary" link @click="markAllNotifyRead">全部标记已读</el-button>
                </div>
              </div>
              <div v-if="notifyLoading" class="notify-loading">
                <el-skeleton :rows="5" animated />
              </div>
              <el-result
                v-else-if="notifyError"
                icon="warning"
                :title="notifyError"
              />
              <template v-else>
                <el-table v-if="notifyList.length" :data="notifyList" border stripe class="notify-table">
                  <el-table-column type="index" width="60" label="#" align="center" />
                  <el-table-column prop="title" label="标题" min-width="160">
                    <template #default="{ row }">
                      <span :class="['notify-title', String(row.readstatus) !== '1' ? 'unread' : '']">{{ row.title || '系统消息' }}</span>
                    </template>
                  </el-table-column>
                  <el-table-column prop="messagetype" label="类型" width="120" align="center">
                    <template #default="{ row }">
                      <el-tag type="info" v-if="row.messagetype">{{ row.messagetype }}</el-tag>
                      <span v-else>系统通知</span>
                    </template>
                  </el-table-column>
                  <el-table-column prop="readstatus" label="状态" width="100" align="center">
                    <template #default="{ row }">
                      <el-tag :type="String(row.readstatus) === '1' ? 'success' : 'warning'">
                        {{ String(row.readstatus) === '1' ? '已读' : '未读' }}
                      </el-tag>
                    </template>
                  </el-table-column>
                  <el-table-column prop="addtime" label="时间" width="180" align="center" />
                  <el-table-column label="操作" width="160" align="center">
                    <template #default="{ row }">
                      <el-button type="primary" link @click="openNotifyDetail(row)">查看</el-button>
                      <el-button
                        v-if="String(row.readstatus) !== '1'"
                        type="success"
                        link
                        :loading="row._loading === true"
                        @click="markNotifyRead(row)"
                      >标记已读</el-button>
                    </template>
                  </el-table-column>
                </el-table>
                <el-empty v-else description="暂无系统消息" />
              </template>
              <div class="notify-pagination" v-if="notifyTotal > notifyPageSize">
                <el-pagination
                  background
                  layout="prev, pager, next"
                  :current-page="notifyPage"
                  :page-size="notifyPageSize"
                  :total="notifyTotal"
                  @current-change="handleNotifyPageChange"
                />
              </div>
            </div>
          </el-tab-pane>
          <el-tab-pane label="我的收藏" name="fav" class="center-pane">
            <div class="no-access">
              <div v-if="favs.length===0" class="empty">暂无收藏</div>
              <div v-else class="list-container collect-container">
                <div v-for="s in favs" :key="s.id" class="list-item-column">
                  <div class="thumb-box" @click="openFav(s)">
                    <img v-if="s.picture" :src="imgSrc(s.picture.split(',')[0])" class="thumb" />
                    <div class="info-box">
                      <div class="title">{{ s.name || (s.tablename+'#'+s.refid) }}</div>
                      <div class="info-item">{{ s.tablename }}</div>
                    </div>
                  </div>
                  <el-button class="cancel-btn" link type="danger" @click="removeFav(s)">取消收藏</el-button>
                </div>
              </div>
            </div>
          </el-tab-pane>
        </el-tabs>
      </div>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, shallowRef, nextTick, onMounted, onUnmounted, computed, watch } from 'vue'
import { useWebsocket } from '@/mixins/WebsocketMixin'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox, type FormInstance, type FormRules } from 'element-plus'
import { http } from '@/utils/request'
import { menuConfig } from '@/config/menu'
import { isAuth, useAuthVersion, notifyAuthChanged } from '@/utils/auth'

const router = useRouter()
const token = ref(localStorage.getItem('frontToken')||'')
const username = ref(localStorage.getItem('username')||'')
const authVersion = useAuthVersion()

type CenterValueSource = 'userid' | 'username' | 'sessionField'

interface CenterTabField {
  prop: string
  label: string
  formType?: string
}

interface CenterTabDef {
  name: string
  label: string
  table: string
  userField: string
  valueSource: CenterValueSource
  sessionField?: string
  queryType: string
  pageSize: number
  extraQuery?: Record<string, any>
  displayFields: CenterTabField[]
  emptyText?: string
}

interface CenterTabState {
  list: any[]
  loading: boolean
  page: number
  limit: number
  total: number
  error: string
  initialized: boolean
}

interface CatePreferConfig {
  table: string
  tableLabel: string
  categoryField: string
  categoryFieldLabel: string
  categoryTable: string
  categoryTableLabel: string
  categoryColumn: string
  categoryImageField?: string | null
}

interface CatePreferOption {
  label: string
  value: string
  image?: string
}

interface CatePreferOptionState {
  loading: boolean
  options: CatePreferOption[]
  error: string
}

const catePreferEnabled = false
const catePreferConfigs: CatePreferConfig[] = catePreferEnabled ? [
] : []
const catePreferOptionState = reactive<Record<string, CatePreferOptionState>>({})
catePreferConfigs.forEach(cfg => {
  if (!catePreferOptionState[cfg.categoryTable]) {
    catePreferOptionState[cfg.categoryTable] = { loading: false, options: [], error: '' }
  }
})
const catePreferSelection = reactive<Record<string, string[]>>({})
catePreferConfigs.forEach(cfg => {
  catePreferSelection[cfg.table] = []
})
const catePreferStorageBase = 'gym_vclqwy4_catePrefer'





const centerTabDefs: CenterTabDef[] = [
  {
    name: 'center_courseenrollment',
    label: '我的课程报名记录',
    table: 'courseenrollment',
    userField: 'userid',
    valueSource: 'userid',
    sessionField: undefined,
    queryType: 'eq',
    pageSize: 10,
    extraQuery: undefined,
    displayFields: [
      { prop: 'coursename', label: '课程名称', formType: 'select' },
      { prop: 'classtime', label: '上课时间', formType: 'datetime' },
      { prop: 'coachname', label: '教练', formType: 'input' },
      { prop: 'orderstatus', label: '状态', formType: 'select' }
    ],
    emptyText: '暂无课程报名记录'
  },
  {
    name: 'center_productorder',
    label: '我的商品订单',
    table: 'productorder',
    userField: 'userid',
    valueSource: 'userid',
    sessionField: undefined,
    queryType: 'eq',
    pageSize: 10,
    extraQuery: undefined,
    displayFields: [
      { prop: 'productname', label: '商品名称', formType: 'select' },
      { prop: 'productprice', label: '商品单价', formType: 'number' },
      { prop: 'quantity', label: '购买数量', formType: 'number' },
      { prop: 'totalprice', label: '订单总价', formType: 'number' },
      { prop: 'orderstatus', label: '状态', formType: 'select' },
      { prop: 'addtime', label: '创建时间', formType: 'datetime' }
    ],
    emptyText: '暂无商品订单'
  },
  {
    name: 'center_cardapplication',
    label: '我的办卡记录',
    table: 'cardapplication',
    userField: 'userid',
    valueSource: 'userid',
    sessionField: undefined,
    queryType: 'eq',
    pageSize: 10,
    extraQuery: undefined,
    displayFields: [
      { prop: 'packagename', label: '套餐名称', formType: 'select' },
      { prop: 'packagetype', label: '套餐类型', formType: 'input' },
      { prop: 'packageprice', label: '价格', formType: 'number' },
      { prop: 'orderstatus', label: '状态', formType: 'select' },
      { prop: 'addtime', label: '创建时间', formType: 'datetime' }
    ],
    emptyText: '暂无办卡记录'
  },
  {
    name: 'center_cardrenewal',
    label: '我的续卡记录',
    table: 'cardrenewal',
    userField: 'userid',
    valueSource: 'userid',
    sessionField: undefined,
    queryType: 'eq',
    pageSize: 10,
    extraQuery: undefined,
    displayFields: [
      { prop: 'packagename', label: '套餐名称', formType: 'select' },
      { prop: 'packagetype', label: '套餐类型', formType: 'input' },
      { prop: 'packageprice', label: '价格', formType: 'number' },
      { prop: 'renewaldays', label: '续费时长', formType: 'number' },
      { prop: 'ispay', label: '是否支付', formType: 'select' },
      { prop: 'addtime', label: '创建时间', formType: 'datetime' }
    ],
    emptyText: '暂无续卡记录'
  },
  {
    name: 'center_feedback',
    label: '我的意见反馈',
    table: 'feedback',
    userField: 'userid',
    valueSource: 'userid',
    sessionField: undefined,
    queryType: 'eq',
    pageSize: 10,
    extraQuery: undefined,
    displayFields: [
      { prop: 'feedbacktitle', label: '标题', formType: 'input' },
      { prop: 'feedbacktype', label: '反馈类型', formType: 'select' },
      { prop: 'auditstatus', label: '审核状态', formType: 'select' },
      { prop: 'addtime', label: '创建时间', formType: 'datetime' }
    ],
    emptyText: '暂无意见反馈'
  }
  ,
  {
    name: 'center_coachmember',
    label: '我的教练',
    table: 'coachmember',
    userField: 'userid',
    valueSource: 'userid',
    sessionField: undefined,
    queryType: 'eq',
    pageSize: 10,
    extraQuery: undefined,
    displayFields: [
      { prop: 'coachname', label: '教练姓名', formType: 'input' },
      { prop: 'coachaccount', label: '教练账号', formType: 'input' },
      { prop: 'bindstatus', label: '绑定状态', formType: 'select' },
      { prop: 'addtime', label: '绑定时间', formType: 'datetime' }
    ],
    emptyText: '暂无绑定教练'
  }
]

const centerTabState = reactive<Record<string, CenterTabState>>({})
centerTabDefs.forEach(tab => {
  centerTabState[tab.name] = {
    list: [],
    loading: false,
    page: 1,
    limit: tab.pageSize,
    total: 0,
    error: '',
    initialized: false
  }
})
const centerDetailState = reactive<Record<string, { visible: boolean; loading: boolean; detail: any }>>({})
centerTabDefs.forEach(tab => {
  centerDetailState[tab.name] = {
    visible: false,
    loading: false,
    detail: null
  }
})
const notifyTabName = 'notify'
const notifyList = ref<any[]>([])
const notifyLoading = ref(false)
const notifyPage = ref(1)
const notifyPageSize = ref(10)
const notifyTotal = ref(0)
const notifyError = ref('')
const notifyTabLabel = computed(() => {
  const unread = notifyList.value.filter(item => String(item.readstatus) !== '1').length
  return unread > 0 ? `我的消息(${unread})` : '我的消息'
})
function resetNotifyState() {
  notifyList.value = []
  notifyTotal.value = 0
  notifyPage.value = 1
  notifyError.value = ''
  notifyLoading.value = false
}

async function loadNotify(reset = false) {
  if (!token.value) {
    resetNotifyState()
    notifyError.value = '请先登录'
    return
  }
  if (!userId.value) {
    await refreshSession(true)
  }
  if (!userId.value) {
    notifyError.value = '未获取到用户信息'
    notifyList.value = []
    notifyTotal.value = 0
    return
  }
  if (reset) notifyPage.value = 1
  notifyLoading.value = true
  notifyError.value = ''
  try {
    const params: Record<string, any> = { page: notifyPage.value, limit: notifyPageSize.value, userid: userId.value }
    const res: any = await http.get('notify/list', { params })
    if (res?.code === 0) {
      const list = Array.isArray(res.data?.list) ? res.data.list : []
      notifyList.value = list.map(item => ({ ...item, _loading: false }))
      const totalVal = res.data?.total
      notifyTotal.value = Number.isFinite(Number(totalVal)) ? Number(totalVal) : list.length
    } else {
      notifyList.value = []
      notifyTotal.value = 0
      notifyError.value = res?.msg || '加载失败'
    }
  } catch (error: any) {
    notifyList.value = []
    notifyTotal.value = 0
    notifyError.value = error?.message || '加载失败'
  } finally {
    notifyLoading.value = false
  }
}

async function markNotifyRead(row: any) {
  if (!row || String(row.readstatus) === '1' || row._loading) return
  if (!ensureLogin()) return
  row._loading = true
  try {
    const payload: Record<string, any> = { ...row, readstatus: '1' }
    const res: any = await http.post('notify/update', payload)
    if (res?.code === 0) {
      row.readstatus = '1'
    } else {
      ElMessage.error(res?.msg || '操作失败')
    }
  } catch (error: any) {
    ElMessage.error(error?.message || '操作失败')
  } finally {
    row._loading = false
  }
}

async function markAllNotifyRead() {
  if (!notifyList.value.length) return
  const unread = notifyList.value.filter(item => String(item.readstatus) !== '1')
  if (!unread.length) {
    ElMessage.success('暂无未读消息')
    return
  }
  if (!ensureLogin()) return
  try {
    notifyLoading.value = true
    const tasks = unread.map(item => http.post('notify/update', { ...item, readstatus: '1' }))
    await Promise.allSettled(tasks)
    await loadNotify()
    ElMessage.success('已全部标记为已读')
  } catch (error: any) {
    ElMessage.error(error?.message || '操作失败')
  } finally {
    notifyLoading.value = false
  }
}

function handleNotifyPageChange(page: number) {
  notifyPage.value = page
  void loadNotify()
}

function openNotifyDetail(row: any) {
  if (!ensureLogin()) return
  if (!row || !row.id) return
  router.push({ path: '/index/notifyDetail', query: { id: row.id } })
}
const roleName = (localStorage.getItem('frontSessionTable')||'').replace(/"/g, '')
const userId = ref<number>(Number(localStorage.getItem('frontUserid') || localStorage.getItem('userid') || '0'))
const catePreferKey = () => {
  const uid = userId.value
  const suffix = uid && uid > 0 ? `_${uid}` : ''
  return catePreferStorageBase + suffix
}
function readCatePreferRecord(): Record<string, string[]> {
  if (!catePreferEnabled) return {}
  try {
    const raw = localStorage.getItem(catePreferKey())
    if (!raw) return {}
    const parsed = JSON.parse(raw)
    return parsed && typeof parsed === 'object' ? parsed : {}
  } catch (error) {
    console.warn('读取分类偏好失败', error)
    return {}
  }
}
function writeCatePreferRecord(record: Record<string, string[]>) {
  if (!catePreferEnabled) return
  try {
    localStorage.setItem(catePreferKey(), JSON.stringify(record))
  } catch (error) {
    console.warn('保存分类偏好失败', error)
  }
}
function hydrateCatePreferSelection() {
  if (!catePreferEnabled) return
  const saved = readCatePreferRecord()
  catePreferConfigs.forEach((cfg) => {
    const values = saved?.[cfg.table]
    catePreferSelection[cfg.table] = Array.isArray(values) ? [...values] : []
  })
}
function saveCatePreferSelection(showToast = false) {
  if (!catePreferEnabled) return
  const payload: Record<string, string[]> = {}
  catePreferConfigs.forEach((cfg) => {
    const values = catePreferSelection[cfg.table] || []
    if (values.length) {
      payload[cfg.table] = [...values]
    }
  })
  writeCatePreferRecord(payload)
  if (showToast) {
    ElMessage.success('分类偏好已更新')
  }
}
function handleCatePreferChange(table: string) {
  if (!catePreferEnabled) return
  saveCatePreferSelection(true)
}
function clearCatePrefer(table: string) {
  if (!catePreferEnabled) return
  catePreferSelection[table] = []
  saveCatePreferSelection(true)
}
function clearAllCatePrefer() {
  if (!catePreferEnabled) return
  catePreferConfigs.forEach((cfg) => {
    catePreferSelection[cfg.table] = []
  })
  try {
    localStorage.removeItem(catePreferKey())
  } catch (error) {
    console.warn('清空分类偏好失败', error)
  }
  ElMessage.success('已清空全部分类偏好')
}
const hasAnyCatePrefer = computed(() => {
  if (!catePreferEnabled) return false
  return catePreferConfigs.some((cfg) => (catePreferSelection[cfg.table] || []).length > 0)
})
async function fetchCatePreferOptions(cfg: CatePreferConfig, force = false) {
  if (!catePreferEnabled) return
  const state = catePreferOptionState[cfg.categoryTable]
  if (!state) return
  if (!force && state.options.length) return
  state.loading = true
  state.error = ''
  try {
    const res: any = await http.get(`${cfg.categoryTable}/list`, { params: { page: 1, limit: 1000, sort: 'id', order: 'asc' } })
    const list = Array.isArray(res?.data?.list)
      ? res.data.list
      : Array.isArray(res?.data?.data?.list)
        ? res.data.data.list
        : Array.isArray(res?.list)
          ? res.list
          : []
    const map = new Map<string, CatePreferOption>()
    list.forEach((row: any) => {
      const value = row?.[cfg.categoryColumn]
      if (!value) return
      const option: CatePreferOption = { label: String(value), value: String(value) }
      if (cfg.categoryImageField && row?.[cfg.categoryImageField]) {
        option.image = row[cfg.categoryImageField]
      }
      map.set(option.value, option)
    })
    state.options = Array.from(map.values())
  } catch (error: any) {
    state.error = error?.message || '加载分类失败'
  } finally {
    state.loading = false
  }
}
async function loadCatePreferOptions(force = false) {
  if (!catePreferEnabled) return
  for (const cfg of catePreferConfigs) {
    await fetchCatePreferOptions(cfg, force)
  }
}
function refreshCatePreferOptions() {
  void loadCatePreferOptions(true)
}
if (catePreferEnabled) {
  hydrateCatePreferSelection()
  watch(userId, () => {
    hydrateCatePreferSelection()
  })
}
const myAvatar = ref('')
const activeTab = ref<string>('profile')

const sessionData = reactive<Record<string, any>>({})
try {
  const rawSession = localStorage.getItem('frontSession')
  if (rawSession) {
    const parsed = JSON.parse(rawSession)
    if (parsed && typeof parsed === 'object') {
      Object.assign(sessionData, parsed)
    }
  }
} catch (error) {
  /* ignore */
}

const balanceField = ref('money')

const hasBalance = computed(() => !!balanceField.value)
const balanceAmount = computed(() => {
  if (!balanceField.value) return 0
  const value = sessionData[balanceField.value]
  const num = Number(value)
  return Number.isFinite(num) ? Number(num) : 0
})
const balanceDisplay = computed(() => hasBalance.value ? `￥${balanceAmount.value.toFixed(2)}` : '')

const rechargeDialogVisible = ref(false)
const rechargeForm = reactive({ amount: '', method: 'weixin' })
const rechargeLoading = ref(false)
const rechargeOptions = [
  { value: 'weixin', label: '微信支付', img: 'https://workflowes.oss-cn-beijing.aliyuncs.com/weixin.png' },
  { value: 'zhifubao', label: '支付宝支付', img: 'https://workflowes.oss-cn-beijing.aliyuncs.com/zhifubao.png' },
  { value: 'jianshe', label: '建设银行', img: 'https://workflowes.oss-cn-beijing.aliyuncs.com/jianshe.png' },
  { value: 'jiaotong', label: '交通银行', img: 'https://workflowes.oss-cn-beijing.aliyuncs.com/jiaotong.png' },
  { value: 'nongye', label: '农业银行', img: 'https://workflowes.oss-cn-beijing.aliyuncs.com/nongye.png' },
  { value: 'zhongguo', label: '中国银行', img: 'https://workflowes.oss-cn-beijing.aliyuncs.com/zhongguo.png' }
]

// 我的优惠券
type CouponFilterValue = 'usable' | 'pending' | 'used' | 'expired' | 'all'
const couponList = ref<any[]>([])
const couponLoading = ref(false)
const couponError = ref('')
const couponStatusFilter = ref<CouponFilterValue>('usable')
const couponInitialized = ref(false)
const couponFilterOptions = [
  { value: 'usable' as CouponFilterValue, label: '可使用' },
  { value: 'pending' as CouponFilterValue, label: '未生效' },
  { value: 'used' as CouponFilterValue, label: '已使用' },
  { value: 'expired' as CouponFilterValue, label: '已过期' },
  { value: 'all' as CouponFilterValue, label: '全部' }
]
const couponStats = computed<Record<CouponFilterValue, number>>(() => {
  const stats: Record<CouponFilterValue, number> = {
    usable: 0,
    pending: 0,
    used: 0,
    expired: 0,
    all: couponList.value.length
  }
  couponList.value.forEach(item => {
    switch (item.statusText) {
      case '可使用':
        stats.usable += 1
        break
      case '未生效':
        stats.pending += 1
        break
      case '已使用':
        stats.used += 1
        break
      case '已过期':
        stats.expired += 1
        break
      default:
        break
    }
  })
  return stats
})
const filteredCoupons = computed(() => {
  switch (couponStatusFilter.value) {
    case 'usable':
      return couponList.value.filter(item => item.statusText === '可使用')
    case 'pending':
      return couponList.value.filter(item => item.statusText === '未生效')
    case 'used':
      return couponList.value.filter(item => item.statusText === '已使用')
    case 'expired':
      return couponList.value.filter(item => item.statusText === '已过期')
    case 'all':
    default:
      return couponList.value
  }
})

function numberOrZero(val: unknown): number {
  const num = Number(val)
  return Number.isFinite(num) ? num : 0
}

function normalizeDiscountRate(rate: unknown): number {
  const num = Number(rate)
  if (!Number.isFinite(num) || num <= 0) return 1
  if (num > 100) return Math.min(num / 100, 1)
  if (num > 10) return Math.min(num / 10, 1)
  if (num > 1) return Math.min(num / 10, 1)
  return Math.min(num, 1)
}

function formatDiscountText(rate: number): string {
  const value = Math.min(Math.max(rate, 0), 1)
  if (value <= 0) return '折扣券'
  if (value >= 1) return '无折扣'
  const raw = value * 10
  const display = Math.abs(raw - Math.round(raw)) < 0.01 ? Math.round(raw) : Number(raw.toFixed(1))
  return `${display}折`
}

function formatTimeRange(start: any, end: any): string {
  const normalize = (value: any) => {
    if (!value) return ''
    const text = String(value)
    if (text.length >= 10) return text.slice(0, 10)
    return text
  }
  const s = normalize(start)
  const e = normalize(end)
  if (s && e) return `${s} ~ ${e}`
  if (s) return `${s} 起`
  if (e) return `${e} 截止`
  return ''
}

function computeCouponStatus(start: any, end: any): '未生效' | '可使用' | '已过期' {
  const now = Date.now()
  const startTime = start ? new Date(start).getTime() : NaN
  const endTime = end ? new Date(end).getTime() : NaN
  if (Number.isFinite(endTime) && now > endTime) return '已过期'
  if (Number.isFinite(startTime) && now < startTime) return '未生效'
  return '可使用'
}

function normalizeCouponRecord(record: any) {
  const fullAmount = numberOrZero(record.couponfullamount)
  const deductAmount = numberOrZero(record.coupondeductamount)
  const rateRaw = record.coupondiscountrate
  const rateValue = normalizeDiscountRate(rateRaw)
  const start = record.couponstarttime ?? record.startime ?? record.start ?? null
  const end = record.couponendtime ?? record.endtime ?? record.end ?? null
  const statusCalc = computeCouponStatus(start, end)
  const type = record.coupontype || record.type || '满减券'
  const merchantAccount = normalizeText(record.merchantaccount ?? record.merchantAccount ?? record.merchant_account ?? '')
  const merchantName = record.merchantname || record.shopname || record.shangjianame || ''
  const displayValue = type === '折扣券' ? formatDiscountText(rateValue) : `￥${deductAmount.toFixed(2)}`
  const displayRule = type === '折扣券'
    ? (fullAmount > 0 ? `满${fullAmount.toFixed(2)}可享` : '无门槛折扣')
    : (fullAmount > 0 ? `满${fullAmount.toFixed(2)}减${deductAmount.toFixed(2)}` : `立减${deductAmount.toFixed(2)}`)
  const couponStatus = String(record.couponstatus || '').trim()
  const statusText = couponStatus === '已使用' ? '已使用' : statusCalc
  const disabled = statusText !== '可使用'
  const disableReason = disabled
    ? (statusText === '未生效'
        ? '尚未到使用时间'
        : statusText === '已过期'
          ? '优惠券已过期'
          : statusText === '已使用'
            ? '优惠券已使用'
            : '暂不可用')
    : ''
  return {
    ...record,
    recordId: record.id,
    couponfullamount: fullAmount,
    coupondeductamount: deductAmount,
    coupondiscountrate: record.coupondiscountrate ?? rateRaw,
    couponstarttime: start,
    couponendtime: end,
    coupontype: type,
    merchantaccount: merchantAccount,
    merchantname: merchantName,
    merchantid: normalizeText(record.merchantid ?? record.merchantId ?? record.merchant_id ?? record.shangjiaid ?? record.shopid ?? ''),
    statusText,
    displayValue,
    displayRule,
    timeRangeText: formatTimeRange(start, end),
    disabled,
    disableReason
  }
}

function normalizeText(val: any): string {
  if (val === undefined || val === null) return ''
  const text = String(val).trim()
  if (!text) return ''
  const lower = text.toLowerCase()
  if (lower === 'null' || lower === 'undefined') return ''
  return text
}

async function loadMyCoupons(force = false) {
  if (!token.value) {
    couponList.value = []
    couponInitialized.value = false
    return
  }
  if (couponLoading.value) return
  if (!force && couponInitialized.value) return
  if (!userId.value) {
    await refreshSession(true)
  }
  if (!userId.value) {
    couponError.value = '无法获取当前用户信息'
    return
  }
  couponLoading.value = true
  couponError.value = ''
  try {
    const res: any = await http.get('couponreceive/list', { params: { page: 1, limit: 200, userid: userId.value } })
    const list = Array.isArray(res?.data?.list) ? res.data.list : Array.isArray(res?.data?.records) ? res.data.records : []
    const normalized = list.map(normalizeCouponRecord)
    couponList.value = normalized
    couponInitialized.value = true
    if (!normalized.length) {
      couponError.value = ''
    }
  } catch (error: any) {
    couponError.value = error?.message || '加载优惠券失败'
  } finally {
    couponLoading.value = false
  }
}

function syncSession(data: Record<string, any>, emit = true) {
  try {
    localStorage.setItem('frontSession', JSON.stringify(data))
  } catch (error) {
    console.warn('更新本地会话失败', error)
  }
  if (emit) {
    notifyAuthChanged()
  }
}

async function refreshSession(force = false) {
  if (!token.value || !roleName) return
  if (!force && Object.keys(sessionData).length) return
  try {
    const res: any = await http.get(roleName + '/session')
    const data = res?.data || {}
    for (const key of Object.keys(sessionData)) {
      delete sessionData[key]
    }
    Object.assign(sessionData, data)
    if (balanceField.value && !(balanceField.value in sessionData)) {
      balanceField.value = ''
    }
    if (data && data.id) {
      const parsedId = Number(data.id)
      if (Number.isFinite(parsedId)) userId.value = parsedId
    }
    if (data) {
      const roleCfg = loginRoles.find(r => r.name === roleName)
      const unameField = roleCfg?.usernameField || 'username'
      if (typeof data[unameField] === 'string') {
        username.value = data[unameField]
      } else if (typeof data.username === 'string') {
        username.value = data.username
      }
    }
    syncSession(sessionData, false)
  } catch (error) {
    console.warn('获取会话信息失败', error)
  }
}

async function openRecharge() {
  if (!ensureLogin()) return
  if (!hasBalance.value) {
    ElMessage.warning('当前账号未配置余额字段')
    return
  }
  if (!roleName) {
    ElMessage.error('当前角色无法充值余额')
    return
  }
  await refreshSession(true)
  rechargeForm.amount = ''
  rechargeForm.method = 'weixin'
  rechargeDialogVisible.value = true
}

function resetRecharge() {
  rechargeForm.amount = ''
  rechargeForm.method = 'weixin'
  rechargeDialogVisible.value = false
}

async function submitRecharge() {
  if (rechargeLoading.value) return
  if (!ensureLogin()) return
  if (!hasBalance.value) {
    ElMessage.warning('当前账号未配置余额字段')
    return
  }
  if (!roleName) {
    ElMessage.error('当前角色无法充值余额')
    return
  }
  const amountNum = Number(rechargeForm.amount)
  if (!rechargeForm.amount || !Number.isFinite(amountNum)) {
    ElMessage.warning('请输入正确的充值金额')
    return
  }
  if (amountNum <= 0) {
    ElMessage.warning('充值金额需大于 0')
    return
  }
  if (!rechargeForm.method) {
    ElMessage.warning('请选择支付方式')
    return
  }
  const idVal = sessionData.id ?? sessionData.userid ?? sessionData.userId ?? userId.value
  const numericId = Number(idVal)
  if (!numericId || !Number.isFinite(numericId)) {
    ElMessage.error('未找到用户编号，无法充值')
    return
  }
  const current = balanceAmount.value
  const next = Number((current + amountNum).toFixed(2))
  const payload: Record<string, any> = { id: numericId }
  payload[balanceField.value] = next
  rechargeLoading.value = true
  try {
    const res: any = await http.post(roleName + '/update', payload)
    if (res?.code === 0) {
      sessionData[balanceField.value] = next
      syncSession(sessionData, false)
      ElMessage.success('充值成功')
      await refreshSession(true)
      resetRecharge()
    } else {
      ElMessage.error(res?.msg || '充值失败')
    }
  } catch (error: any) {
    ElMessage.error(error?.message || '充值失败')
  } finally {
    rechargeLoading.value = false
    loadProfileComponent(true)
  }
}

// 个人信息
const profileComponent = shallowRef<any>(null)
const addOrUpdateRef = ref<any>()
const profileLoading = ref(false)
const profileError = ref('')

async function loadProfileComponent(force = false) {
  if (!roleName || !token.value) return
  profileError.value = ''
  await refreshSession(force || !Object.keys(sessionData).length)
  if (!profileComponent.value || force) {
    profileLoading.value = true
    try {
      const module = await import(`@/views/modules/${roleName}/AddOrUpdate.vue`)
      profileComponent.value = module.default
    } catch (error) {
      console.error('加载个人资料组件失败', error)
      profileError.value = '未找到个人资料编辑组件'
    } finally {
      profileLoading.value = false
    }
  }
  await nextTick()
  if (addOrUpdateRef.value && typeof addOrUpdateRef.value.init === 'function') {
    try {
      await addOrUpdateRef.value.init(userId.value, 'embed')
    } catch (error) {
      console.warn('初始化个人资料表单失败', error)
    }
  }
}

async function handleProfileUpdated() {
  await refreshSession(true)
  await loadProfileComponent()
}

watch(activeTab, async (tab, prev) => {
  if (tab === 'coupon') {
    if (!token.value) {
      ElMessage.warning('请先登录')
      activeTab.value = 'profile'
      return
    }
    await loadMyCoupons()
    return
  }
  if (tab === 'cateprefer') {
    if (!catePreferEnabled) {
      return
    }
    if (!token.value) {
      ElMessage.warning('请先登录')
      activeTab.value = 'profile'
      return
    }
    await loadCatePreferOptions()
    return
  }
  if (tab === 'profile' && prev !== 'profile') {
    await loadProfileComponent()
  }
  if (isCenterTabName(tab)) {
    if (!token.value) {
      ElMessage.warning('请先登录')
      activeTab.value = 'profile'
      return
    }
    const state = centerTabState[tab]
    if (state && !state.initialized) {
      await loadCenterTab(tab, true)
    }
  }
  if (tab === notifyTabName && prev !== notifyTabName) {
    if (!token.value) {
      ElMessage.warning('请先登录')
      activeTab.value = 'profile'
      return
    }
    await loadNotify(true)
  }
})

async function handleAuthUpdated() {
  token.value = localStorage.getItem('frontToken') || ''
  username.value = localStorage.getItem('username') || ''
  userId.value = Number(localStorage.getItem('frontUserid') || localStorage.getItem('userid') || '0')
  if (catePreferEnabled) {
    hydrateCatePreferSelection()
  }
  await refreshSession(true)
  resetCenterTabStates()
  resetCouponState()
  resetNotifyState()
  if (rechargeDialogVisible.value) {
    resetRecharge()
  }
  if (!token.value) {
    activeTab.value = 'profile'
    return
  }
  if (activeTab.value === 'profile') {
    await loadProfileComponent()
  } else if (isCenterTabName(activeTab.value) && token.value) {
    await loadCenterTab(activeTab.value, true)
  } else if (activeTab.value === 'coupon') {
    await loadMyCoupons(true)
  } else if (activeTab.value === notifyTabName) {
    await loadNotify(true)
  }
}

watch(authVersion, async (version, prev) => {
  if (version === prev) return
  await handleAuthUpdated()
})

// 修改密码
const pwdFormRef = ref<FormInstance>()
const pwdLoading = ref(false)
const pwdForm = reactive({ oldPassword:'', newPassword:'', confirmPassword:'' })
const pwdRules: FormRules = {
  oldPassword: [ { required: true, message: '请输入原密码', trigger: 'blur' } ],
  newPassword: [ { required: true, message: '请输入新密码', trigger: 'blur' }, { min: 6, message: '不少于 6 位', trigger: 'blur' } ],
  confirmPassword: [ { required: true, message: '请再次输入新密码', trigger: 'blur' } ]
}
async function handleUpdatePassword(){
  if (!pwdFormRef.value) return
  await pwdFormRef.value.validate(async (valid) => {
    if (!valid) return
    if (pwdForm.newPassword !== pwdForm.confirmPassword) { ElMessage.error('两次密码不一致'); return }
    pwdLoading.value = true
    try {
      // 先验证原密码是否正确
      const loginRes: any = await http.get(roleName + '/login', { params: { username: username.value, password: pwdForm.oldPassword } })
      if (loginRes?.code !== 0) { ElMessage.error('原密码错误'); return }
      // 查找该角色在菜单配置中的密码字段名
      const roleCfg: any = (menuConfig as any[]).find((r:any) => r.tableName === roleName)
      const passwordField = roleCfg?.passwordField || 'password'
      // 更新为新密码
      const payload: any = { id: userId.value }
      payload[passwordField] = pwdForm.newPassword
      await http.post(roleName + '/update', payload)
      ElMessage.success('密码修改成功，请重新登录')
      logout()
    } catch(e) {
      ElMessage.error('修改失败，请重试')
    } finally { pwdLoading.value = false }
  })
}
function resetPwdForm(){ pwdFormRef.value?.resetFields() }

// 聊天记录
interface ChatPeer {
  key: string
  id: number
  name?: string
  alias?: string
  displayName: string
  avatar?: string
  avatarUrl?: string
  lastMsg?: string
  lastMsgLabel?: string
  format?: number
  lastTime?: string
  lastTimeText?: string
  friendId?: number
  tablename?: string
}

const chatPeers = ref<ChatPeer[]>([])
const baseUrl = import.meta.env.VITE_API_BASE_URL || ''
function imgSrc(raw?: string) {
  if (!raw) return ''
  const value = String(raw).split(',')[0].trim()
  if (!value) return ''
  if (/^https?:\/\//.test(value)) return value
  const normalizedBase = baseUrl ? (baseUrl.endsWith('/') ? baseUrl.slice(0, -1) : baseUrl) : ''
  const normalizedPath = value.startsWith('/') ? value : '/' + value
  if (normalizedBase) {
    return normalizedBase + normalizedPath
  }
  return value
}

// 登录角色配置（用于补充昵称与头像）
const loginRoles = [
    { name: 'user', usernameField: 'useraccount', avatarField: 'image' },
    { name: 'coach', usernameField: 'coachaccount', avatarField: 'coachimage' },
    { name: 'admin', usernameField: 'adminaccount', avatarField: 'image' },
]

const chatVisible = ref(false)
const chatList = ref<any[]>([])
const chatForm = reactive({ content: '' })
const chatContentRef = ref<HTMLElement | null>(null)
const fid = ref<number | null>(null)
const fname = ref('')
const fpic = ref('')
const chatDialogTitle = computed(() => fname.value ? '与 ' + fname.value + ' 私聊' : '聊天记录')
const myAvatarUrl = computed(() => imgSrc(myAvatar.value))
const peerAvatarUrl = computed(() => imgSrc(fpic.value))
const currentPeerTable = ref('')
const uploadUrl = (baseUrl && !baseUrl.endsWith('/') ? baseUrl + '/' : baseUrl) + 'file/upload'
const { initWebSocket: initChatSocket, websocketSend, closeWebSocket, clearReconnect } = useWebsocket({
  onMessage: () => getChatList(),
  shouldReconnect: () => chatVisible.value && !!fid.value,
})

async function initCurrentUser() {
  if (!token.value || !roleName) return
  try {
    const user = JSON.parse(localStorage.getItem("frontSession") || '{}')
    userId.value = user.id
    const currentRole = loginRoles.find(r => r.name === roleName)
    const unameField = currentRole?.usernameField || 'username'
    username.value = user[unameField] || ''
    const avatarField = currentRole?.avatarField
    myAvatar.value = avatarField ? (user[avatarField] || '') : ''
  } catch (e) {
    /* ignore */
  }
}

async function loadChatPeers() {
  if (!userId.value) {
    chatPeers.value = []
    return
  }
  const map = new Map<number, ChatPeer>()
  await collectFriendPeers(map)
  await collectMessagePeers(map)
  const peers = Array.from(map.values())
  for (const peer of peers) {
    await fillPeerProfile(peer)
    peer.displayName = resolveDisplayName(peer)
    peer.avatarUrl = imgSrc(peer.avatar)
    peer.lastMsgLabel = peer.format === 2 ? '[图片]' : (peer.lastMsg || '')
    if (peer.lastTime) peer.lastTimeText = formatDisplayTime(peer.lastTime)
  }
  peers.sort((a, b) => compareTime(b.lastTime) - compareTime(a.lastTime))
  chatPeers.value = peers
}

function ensureLogin() {
  if (!token.value) {
    window.dispatchEvent(new CustomEvent('front-auth-open'))
    return false
  }
  return true
}

async function openChat(p: ChatPeer) {
  if (!ensureLogin()) return
  fid.value = p.id
  fname.value = p.displayName || ('用户#' + p.id)
  fpic.value = p.avatar || ''
  currentPeerTable.value = p.tablename ? String(p.tablename) : ''
  initChatSocket(p.id)
  await getChatList()
  chatVisible.value = true
}

async function getChatList() {
  if (!fid.value || !userId.value) return
  try {
    const rs: any = await http.get('chatmessage/mlist', { params: { page: 1, limit: 1000, uid: userId.value, fid: fid.value } })
    const list = Array.isArray(rs?.data?.list) ? [...rs.data.list] : []
    list.sort((a: any, b: any) => compareTime(a.addtime) - compareTime(b.addtime))
    chatList.value = formatChatMessages(list)
    scrollToBottom()
  } catch (e) {
    chatList.value = []
  }
}

async function addChat(fileUrl?: string) {
  if (!fid.value || !ensureLogin()) return
  const content = fileUrl ? String(fileUrl) : chatForm.content.trim()
  if (!content) return
  const payload: any = { uid: userId.value, fid: fid.value, content, format: fileUrl ? 2 : 1 }
  if (currentPeerTable.value) {
    payload.tablename = currentPeerTable.value
  }
  try {
    const res: any = await http.post('chatmessage/add', payload)
    if (res?.code === 0) {
      chatForm.content = ''
      websocketSend(content)
      await getChatList()
      await loadChatPeers()
    }
  } catch (e) {
    /* ignore */
  }
}

function handleChatClose() {
  chatForm.content = ''
  chatVisible.value = false
  fid.value = null
  fname.value = ''
  fpic.value = ''
  currentPeerTable.value = ''
  clearReconnect()
  closeWebSocket()
}

function uploadSuccess(res: any) {
  const raw = res?.file || res?.url || res?.data?.file || res?.data?.url || res?.data
  if (!raw) return
  addChat(String(raw))
}

function isMine(m: any) {
  return String(m?.uid) === String(userId.value)
}

async function removePeer(p: ChatPeer) {
  if (p.friendId) {
    try { await http.post('friend/delete', [p.friendId]) } catch (e) { /* ignore */ }
  }
  chatPeers.value = chatPeers.value.filter(x => x.key !== p.key)
  if (fid.value === p.id) {
    chatVisible.value = false
    chatList.value = []
    clearReconnect()
    closeWebSocket()
  }
}

function peerKey(id: number) {
  return 'p_' + id
}

async function collectFriendPeers(map: Map<number, ChatPeer>) {
  if (!userId.value) return
  const queries = [
    { uid: userId.value, type: 2 },
    { fid: userId.value, type: 2 }
  ]
  for (const params of queries) {
    try {
      const rs: any = await http.get('friend/list', { params: { page: 1, limit: 1000, ...params } })
      const list: any[] = rs?.data?.list || []
      for (const item of list) {
        const typeVal = String(item.type ?? '')
        if (typeVal && typeVal !== '2' && typeVal !== '聊天记录') continue
        const peerId = params.uid ? Number(item.fid) : Number(item.uid)
        if (!peerId || peerId === userId.value) continue
        const existing = map.get(peerId) || { key: peerKey(peerId), id: peerId, displayName: '' }
        if (item.id && !existing.friendId) existing.friendId = item.id
        if (item.name && !existing.name) existing.name = item.name
        if (item.alias && !existing.alias) existing.alias = item.alias
        if (item.tablename && !existing.tablename) existing.tablename = item.tablename
        if (item.picture && !existing.avatar) {
          const pic = String(item.picture).split(',')[0]
          if (pic) existing.avatar = pic
        }
        map.set(peerId, existing)
      }
    } catch (e) { /* ignore */ }
  }
}

async function collectMessagePeers(map: Map<number, ChatPeer>) {
  if (!userId.value) return
  const queries = [
    { uid: userId.value },
    { fid: userId.value }
  ]
  const messages: any[] = []
  for (const params of queries) {
    try {
      const rs: any = await http.get('chatmessage/list', { params: { page: 1, limit: 1000, ...params } })
      messages.push(...(rs?.data?.list || []))
    } catch (e) { /* ignore */ }
  }
  messages.sort((a: any, b: any) => compareTime(b.addtime) - compareTime(a.addtime))
  for (const msg of messages) {
    const mine = String(msg.uid) === String(userId.value)
    const peerId = mine ? Number(msg.fid) : Number(msg.uid)
    if (!peerId || peerId === userId.value) continue
    let peer = map.get(peerId)
    if (!peer) {
      peer = { key: peerKey(peerId), id: peerId, displayName: '' }
      map.set(peerId, peer)
    }
    if (!peer.lastTime || compareTime(msg.addtime) > compareTime(peer.lastTime)) {
      peer.lastMsg = msg.content || ''
      peer.format = Number(msg.format || 1)
      peer.lastTime = msg.addtime
    }
    const tableName = String(msg?.tablename || '').trim()
    if (!peer.tablename && tableName) {
      peer.tablename = tableName
    }
  }
}

async function fillPeerProfile(peer: ChatPeer) {
  const defaultName = '用户#' + peer.id
  const hasName = peer.displayName && peer.displayName !== defaultName
  if (hasName && peer.avatar) return

  // Bug5: 教练ID偏移量处理
  const isCoachPeer = peer.id >= 100000
  const realId = isCoachPeer ? peer.id - 100000 : peer.id
  if (isCoachPeer && !peer.tablename) {
    peer.tablename = 'coach'
  }

  const tryFill = async (role: { name: string; usernameField?: string; avatarField?: string }) => {
    try {
      if (!role) return false
      const queryId = role.name === 'coach' ? realId : peer.id
      const detail: any = await http.get(role.name + '/detail/' + queryId)
      const data = detail?.data
      if (!data) return false
      if (!peer.displayName) {
        if (role.usernameField && data[role.usernameField]) {
          peer.displayName = data[role.usernameField]
        } else if (data.username) {
          peer.displayName = data.username
        }
      }
      if (!peer.avatar && role.avatarField && data[role.avatarField]) {
        const avatar = String(data[role.avatarField]).split(',')[0]
        if (avatar) peer.avatar = avatar
      }
      return peer.displayName || peer.avatar
    } catch (e) {
      return false
    }
  }

  const tableGuess = String(peer.tablename || '').trim()
  if (tableGuess) {
    const role = loginRoles.find(r => r.name === tableGuess)
    const filled = await tryFill(role || { name: tableGuess })
    if (filled && peer.displayName && peer.avatar) return
  }

  for (const role of loginRoles) {
    if (tableGuess && role.name === tableGuess) continue
    const filled = await tryFill(role)
    if (filled && peer.displayName && peer.avatar) break
  }
}

function resolveDisplayName(peer: ChatPeer) {
  return peer.displayName || peer.name || peer.alias || ('用户#' + peer.id)
}

function resolveFileUrl(path?: string) {
  return imgSrc(path)
}

function scrollToBottom() {
  nextTick(() => {
    if (chatContentRef.value) {
      chatContentRef.value.scrollTop = chatContentRef.value.scrollHeight
    }
  })
}

function parseDate(val?: string) {
  if (!val) return null
  const normalized = String(val).replace('T', ' ').replace(/-/g, '/').split('.')[0]
  const date = new Date(normalized)
  if (Number.isNaN(date.getTime())) return null
  return date
}

function compareTime(val?: string) {
  const date = parseDate(val)
  return date ? date.getTime() : 0
}

function pad(num: number) {
  return num < 10 ? '0' + num : String(num)
}

function formatClock(date: Date) {
  return pad(date.getHours()) + ':' + pad(date.getMinutes())
}

function formatDisplayTime(val?: string) {
  const date = parseDate(val)
  if (!date) return val || ''
  const now = new Date()
  const dayMs = 24 * 60 * 60 * 1000
  const diff = now.getTime() - date.getTime()
  if (now.toDateString() === date.toDateString()) {
    return formatClock(date)
  }
  if (diff < dayMs * 2 && diff >= dayMs) {
    return '昨天 ' + formatClock(date)
  }
  return (date.getMonth() + 1) + '月' + pad(date.getDate()) + '日 ' + formatClock(date)
}

function formatChatMessages(list: any[]) {
  const formatted: any[] = []
  let lastTs = 0
  for (const item of list) {
    const copy = { ...item }
    const ts = compareTime(item.addtime)
    if (!lastTs || Math.abs(ts - lastTs) > 3 * 60 * 1000) {
      copy.showTime = formatDisplayTime(item.addtime)
      lastTs = ts
    } else {
      copy.showTime = ''
    }
    formatted.push(copy)
  }
  return formatted
}

const centerTabNameSet = new Set(centerTabDefs.map(tab => tab.name))

function isCenterTabName(name?: string | null): boolean {
  if (!name) return false
  return centerTabNameSet.has(name)
}

function resolveCenterFilterValue(tab: CenterTabDef) {
  const source = tab.valueSource
  if (source === 'userid') {
    const candidates = [userId.value, sessionData.id, sessionData.userId, sessionData.userid]
    for (const val of candidates) {
      const num = Number(val)
      if (Number.isFinite(num) && num > 0) {
        return num
      }
    }
    return undefined
  }
  if (source === 'sessionField' && tab.sessionField) {
    let val = sessionData[tab.sessionField]
    if (val !== undefined && val !== null && String(val) !== '') {
      return val
    }
    return undefined
  }
  if (username.value) return username.value
  const fallback = sessionData[tab.userField] ?? sessionData.username
  if (fallback !== undefined && fallback !== null && String(fallback) !== '') {
    return fallback
  }
  return undefined
}

async function loadCenterTab(tabName: string, resetPage = false) {
  const tab = centerTabDefs.find(item => item.name === tabName)
  if (!tab) return
  const state = centerTabState[tab.name]
  if (!state) return
  if (!token.value) {
    state.error = '请先登录'
    state.list = []
    state.total = 0
    state.initialized = true
    return
  }
  if (resetPage) state.page = 1
  const filterValue = resolveCenterFilterValue(tab)
  if (filterValue === undefined || filterValue === null || filterValue === '') {
    state.error = '当前账号暂无关联数据'
    state.list = []
    state.total = 0
    state.initialized = true
    return
  }
  const params: Record<string, any> = { page: state.page, limit: state.limit,sort:"id",order:"desc" }
  params[tab.userField] = filterValue
  if (tab.queryType && tab.queryType.toLowerCase() === 'like') {
    params[tab.userField + 'type'] = 'like'
  }
  if (tab.extraQuery) {
    Object.assign(params, tab.extraQuery)
  }
  state.loading = true
  state.error = ''
  try {
    const res: any = await http.get(`${tab.table}/list`, { params })
    if (res?.code === 0) {
      const list = Array.isArray(res.data?.list) ? res.data.list : []
      state.list = list
      const totalVal = res.data?.total
      state.total = Number.isFinite(Number(totalVal)) ? Number(totalVal) : list.length
    } else {
      state.list = []
      state.total = 0
      state.error = res?.msg || '加载失败'
    }
  } catch (error: any) {
    state.list = []
    state.total = 0
    state.error = error?.message || '加载失败'
  } finally {
    state.loading = false
    state.initialized = true
  }
}

function refreshCenterTab(tabName: string) {
  void loadCenterTab(tabName, true)
}

function handleCenterTabPageChange(tabName: string, page: number) {
  const state = centerTabState[tabName]
  if (!state) return
  state.page = page
  void loadCenterTab(tabName)
}

function goCenterModule(tab: CenterTabDef) {
  if (!ensureLogin()) return
  router.push({ path: '/index/' + tab.table })
}

function openCenterDetail(tab: CenterTabDef, row: any) {
  if (!ensureLogin()) return
  if (tab.table === 'coachmember') {
    openCenterCoachmemberDetail(tab, row)
    return
  }
  const idVal = row?.id ?? row?.ID ?? row?.Id
  if (!idVal) {
    router.push({ path: '/index/' + tab.table, query: { [tab.userField]: resolveCenterFilterValue(tab) } })
    return
  }
  router.push({ path: '/index/' + tab.table + 'Detail', query: { id: idVal } })
}

async function openCenterCoachmemberDetail(tab: CenterTabDef, row: any) {
  const idVal = row?.id ?? row?.ID ?? row?.Id
  if (!idVal) {
    return
  }
  const state = centerDetailState[tab.name]
  if (!state) return
  state.visible = true
  state.loading = true
  state.detail = null
  try {
    const res: any = await http.get(`coachmember/detail/${idVal}`)
    state.detail = res?.data || null
  } catch {
    state.detail = null
  } finally {
    state.loading = false
    await nextTick()
    const el = document.getElementById(`center-detail-${tab.name}`)
    if (el && typeof el.scrollIntoView === 'function') {
      el.scrollIntoView({ behavior: 'smooth', block: 'start' })
    }
  }
}

function closeCenterDetail(tabName: string) {
  const state = centerDetailState[tabName]
  if (!state) return
  state.visible = false
  state.detail = null
}

function formatCenterValue(row: any, field: CenterTabField) {
  const value = row?.[field.prop]
  if (value === undefined || value === null || value === '') return ''
  const type = (field.formType || '').toLowerCase()
  if (type === 'date' || type === 'datetime') {
    return formatDisplayTime(String(value))
  }
  if (type === 'file') {
    const list = String(value).split(',').filter(item => item)
    if (!list.length) return ''
    return `共${list.length}个附件`
  }
  if (Array.isArray(value)) {
    return value.join(',')
  }
  return value
}

function formatFileName(path: string, index: number) {
  if (!path) return `附件1`
  const segments = String(path).split('/')
  const name = segments[segments.length - 1] || ''
  return name ? decodeURIComponent(name) : `附件1`
}

function openFileLink(path?: string) {
  if (!path) return
  const url = resolveFileUrl(path)
  if (!url) return
  const win = window.open(url, '_blank')
  win?.focus()
}

function resolveLoginUserId(): number {
  const candidates = [userId.value, sessionData.id, sessionData.userid, sessionData.userId]
  for (const val of candidates) {
    const num = Number(val)
    if (Number.isFinite(num) && num > 0) {
      return num
    }
  }
  return 0
}

async function setAddressDefault(tab: CenterTabDef, row: any) {
  if (!ensureLogin()) return
  const idVal = row?.id ?? row?.ID ?? row?.Id
  if (!idVal) {
    ElMessage.warning('未找到地址编号')
    return
  }
  let uid = resolveLoginUserId()
  if (!uid) {
    await refreshSession(true)
    uid = resolveLoginUserId()
  }
  const payload: Record<string, any> = { id: idVal, isdefault: '是' }
  if (uid) {
    payload.userid = uid
  }
  try {
    await http.post('address/update', payload)
    ElMessage.success('已设为默认地址')
    await loadCenterTab(tab.name)
  } catch (error: any) {
    ElMessage.error(error?.message || '设置失败')
  }
}

async function removeAddress(tab: CenterTabDef, row: any) {
  if (!ensureLogin()) return
  const idVal = row?.id ?? row?.ID ?? row?.Id
  if (!idVal) {
    ElMessage.warning('未找到地址编号')
    return
  }
  try {
    await ElMessageBox.confirm('确定删除该地址吗？', '提示', {
      type: 'warning',
      confirmButtonText: '确定',
      cancelButtonText: '取消'
    })
  } catch {
    return
  }
  try {
    await http.post('address/delete', [idVal])
    ElMessage.success('已删除地址')
    const state = centerTabState[tab.name]
    if (state && state.list.length <= 1 && state.page > 1) {
      state.page = state.page - 1
    }
    await loadCenterTab(tab.name)
  } catch (error: any) {
    ElMessage.error(error?.message || '删除失败')
  }
}

function goAddAddress() {
  if (!ensureLogin()) return
  router.push('/index/addressAdd')
}

function resetCenterTabStates() {
  centerTabDefs.forEach(tab => {
    const state = centerTabState[tab.name]
    if (!state) return
    state.list = []
    state.total = 0
    state.page = 1
    state.error = ''
    state.initialized = false
    state.loading = false
  })
}

function resetCouponState() {
  couponList.value = []
  couponInitialized.value = false
  couponError.value = ''
  couponStatusFilter.value = 'usable'
}

// 我的收藏
const favs = ref<any[]>([])
async function loadFavs(){
  try {
    const rs: any = await http.get('social/list', { params: { page:1, limit:100, type:'1' } })
    favs.value = rs?.data?.list || []
  } catch(e) { favs.value = [] }
}
function openFav(s:any){ router.push({ path: '/index/' + s.tablename + 'Detail', query: { id: s.refid } }) }
async function removeFav(s:any){ try { await http.post('social/delete', [s.id]); await loadFavs(); ElMessage.success('已取消收藏') } catch(e) { ElMessage.error('取消失败') } }

function logout() {
  localStorage.clear()
  if (catePreferEnabled) {
    hydrateCatePreferSelection()
  }
  resetCenterTabStates()
  resetCouponState()
  resetNotifyState()
  activeTab.value = 'profile'
  window.dispatchEvent(new CustomEvent('front-auth-updated'))
  notifyAuthChanged()
  window.location.href = '/index/home'
}

onMounted(async () => {
  if (!token.value) return
  await refreshSession(true)
  await loadProfileComponent()
  await initCurrentUser()
  await loadChatPeers()
  await loadFavs()
  await loadNotify(true)
  if (activeTab.value === 'coupon') {
    await loadMyCoupons(true)
  }
})

onUnmounted(() => {
  clearReconnect()
  closeWebSocket()
})
</script>

<style>
.address-default-tag {
  margin: 0 8px;
}
.coupon-tab {
  display: flex;
  flex-direction: column;
  gap: 16px;
}
.coupon-toolbar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  flex-wrap: wrap;
  gap: 12px;
}
.coupon-filters .count {
  margin-left: 4px;
  font-size: 12px;
  color: var(--el-color-info);
}
.coupon-loading {
  padding: 12px;
  background: #fafafa;
  border-radius: 8px;
}
.coupon-list .coupon-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(260px, 1fr));
  gap: 14px;
}
.coupon-card {
  display: flex;
  justify-content: space-between;
  gap: 12px;
  padding: 16px;
  border-radius: 12px;
  border: 1px dashed rgba(245, 158, 11, 0.4);
  background: linear-gradient(135deg, rgba(255, 247, 237, 0.95), rgba(255, 255, 255, 0.95));
}
.center-detail-block {
  margin-top: 16px;
}
.center-detail-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.coupon-card.disabled {
  opacity: 0.55;
}
.coupon-card-main {
  flex: 1;
  min-width: 0;
  display: flex;
  flex-direction: column;
  gap: 6px;
}
.coupon-card-title {
  display: flex;
  justify-content: space-between;
  align-items: center;
  gap: 8px;
  font-weight: 600;
  color: #92400e;
}
.coupon-card-title .status {
  font-size: 12px;
  padding: 2px 8px;
  border-radius: 999px;
}
.coupon-card-title .status-usable {
  background: rgba(34, 197, 94, 0.16);
  color: #15803d;
}
.coupon-card-title .status-pending {
  background: rgba(59, 130, 246, 0.16);
  color: #1d4ed8;
}
.coupon-card-title .status-expired {
  background: rgba(148, 163, 184, 0.2);
  color: #475569;
}
.coupon-card-title .status-used {
  background: rgba(249, 115, 22, 0.18);
  color: #c2410c;
}
.coupon-card-main .rule {
  color: #b45309;
}
.coupon-card-main .time,
.coupon-card-main .merchant,
.coupon-card-main .reason {
  font-size: 12px;
  color: var(--el-color-info);
}
.coupon-card-main .reason {
  color: #f97316;
}
</style>
