import { createRouter, createWebHistory, type RouteRecordRaw } from 'vue-router'
import { useAuthStore } from '@/stores/auth'

const routes: RouteRecordRaw[] = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/Login.vue'),
    meta: { requiresAuth: false }
  },
  {
    path: '/register',
    name: 'Register',
    component: () => import('@/views/Register.vue'),
    meta: { requiresAuth: false }
  },
  {
    path: '/',
    redirect: '/dashboard'
  },
  {
    path: '/dashboard',
    component: () => import('@/layout/Layout.vue'),
    children: [
      {
        path: '',
        name: 'Dashboard',
        component: () => import('@/views/DashBoard.vue'),
        meta: { title: '首页' }
      },
      // 模块路由
      {
        path: 'modules/user',
        name: 'User',
        component: () => import('@/views/modules/user/List.vue'),
        meta: { title: '用户管理' }
      },
      {
        path: 'modules/coach',
        name: 'Coach',
        component: () => import('@/views/modules/coach/List.vue'),
        meta: { title: '教练管理' }
      },
      {
        path: 'modules/coachmember',
        name: 'Coachmember',
        component: () => import('@/views/modules/coachmember/List.vue'),
        meta: { title: '教练-会员绑定管理' }
      },
      {
        path: 'modules/trainingplan',
        name: 'Trainingplan',
        component: () => import('@/views/modules/trainingplan/List.vue'),
        meta: { title: '训练计划管理' }
      },
      {
        path: 'modules/trainingplanitem',
        name: 'Trainingplanitem',
        component: () => import('@/views/modules/trainingplanitem/List.vue'),
        meta: { title: '训练计划明细管理' }
      },
      {
        path: 'modules/trainingrecord',
        name: 'Trainingrecord',
        component: () => import('@/views/modules/trainingrecord/List.vue'),
        meta: { title: '训练记录管理' }
      },
      {
        path: 'modules/coachreview',
        name: 'Coachreview',
        component: () => import('@/views/modules/coachreview/List.vue'),
        meta: { title: '教练评价管理' }
      },
      {
        path: 'modules/coursetype',
        name: 'Coursetype',
        component: () => import('@/views/modules/coursetype/List.vue'),
        meta: { title: '课程分类管理' }
      },
      {
        path: 'modules/course',
        name: 'Course',
        component: () => import('@/views/modules/course/List.vue'),
        meta: { title: '健身课程管理' }
      },
      {
        path: 'modules/discusscourse',
        name: 'CourseDiscuss',
        component: () => import('@/views/modules/discusscourse/List.vue'),
        meta: { title: '健身课程评论管理' }
      },
      {
        path: 'modules/courseenrollment',
        name: 'Courseenrollment',
        component: () => import('@/views/modules/courseenrollment/List.vue'),
        meta: { title: '课程报名记录管理' }
      },
      {
        path: 'modules/producttype',
        name: 'Producttype',
        component: () => import('@/views/modules/producttype/List.vue'),
        meta: { title: '商品分类管理' }
      },
      {
        path: 'modules/product',
        name: 'Product',
        component: () => import('@/views/modules/product/List.vue'),
        meta: { title: '健身商品管理' }
      },
      {
        path: 'modules/discussproduct',
        name: 'ProductDiscuss',
        component: () => import('@/views/modules/discussproduct/List.vue'),
        meta: { title: '健身商品评论管理' }
      },
      {
        path: 'modules/productorder',
        name: 'Productorder',
        component: () => import('@/views/modules/productorder/List.vue'),
        meta: { title: '商品订单管理' }
      },
      {
        path: 'modules/discussproductorder',
        name: 'ProductorderDiscuss',
        component: () => import('@/views/modules/discussproductorder/List.vue'),
        meta: { title: '商品订单评论管理' }
      },
      {
        path: 'modules/membershippackage',
        name: 'Membershippackage',
        component: () => import('@/views/modules/membershippackage/List.vue'),
        meta: { title: '会员卡套餐管理' }
      },
      {
        path: 'modules/cardapplication',
        name: 'Cardapplication',
        component: () => import('@/views/modules/cardapplication/List.vue'),
        meta: { title: '办卡记录管理' }
      },
      {
        path: 'modules/cardrenewal',
        name: 'Cardrenewal',
        component: () => import('@/views/modules/cardrenewal/List.vue'),
        meta: { title: '续卡记录管理' }
      },
      {
        path: 'modules/sharetype',
        name: 'Sharetype',
        component: () => import('@/views/modules/sharetype/List.vue'),
        meta: { title: '交流分类管理' }
      },
      {
        path: 'modules/share',
        name: 'Share',
        component: () => import('@/views/modules/share/List.vue'),
        meta: { title: '会员交流管理' }
      },
      {
        path: 'modules/discussshare',
        name: 'ShareDiscuss',
        component: () => import('@/views/modules/discussshare/List.vue'),
        meta: { title: '会员交流评论管理' }
      },
      {
        path: 'modules/feedback',
        name: 'Feedback',
        component: () => import('@/views/modules/feedback/List.vue'),
        meta: { title: '意见反馈管理' }
      },
      {
        path: 'modules/discusscourse',
        name: 'Discusscourse',
        component: () => import('@/views/modules/discusscourse/List.vue'),
        meta: { title: '健身课程评论表管理' }
      },
      {
        path: 'modules/cancelcourseenrollment',
        name: 'Cancelcourseenrollment',
        component: () => import('@/views/modules/cancelcourseenrollment/List.vue'),
        meta: { title: '取消课程报名记录管理' }
      },
      {
        path: 'modules/refundcourseenrollment',
        name: 'Refundcourseenrollment',
        component: () => import('@/views/modules/refundcourseenrollment/List.vue'),
        meta: { title: '课程报名记录退款管理' }
      },
      {
        path: 'modules/discussproduct',
        name: 'Discussproduct',
        component: () => import('@/views/modules/discussproduct/List.vue'),
        meta: { title: '健身商品评论表管理' }
      },
      {
        path: 'modules/cancelcardapplication',
        name: 'Cancelcardapplication',
        component: () => import('@/views/modules/cancelcardapplication/List.vue'),
        meta: { title: '取消办卡记录管理' }
      },
      {
        path: 'modules/refundcardapplication',
        name: 'Refundcardapplication',
        component: () => import('@/views/modules/refundcardapplication/List.vue'),
        meta: { title: '办卡记录退款管理' }
      },
      {
        path: 'modules/cancelproductorder',
        name: 'Cancelproductorder',
        component: () => import('@/views/modules/cancelproductorder/List.vue'),
        meta: { title: '取消商品订单管理' }
      },
      {
        path: 'modules/refundproductorder',
        name: 'Refundproductorder',
        component: () => import('@/views/modules/refundproductorder/List.vue'),
        meta: { title: '商品订单退款管理' }
      },
      {
        path: 'modules/discussproductorder',
        name: 'Discussproductorder',
        component: () => import('@/views/modules/discussproductorder/List.vue'),
        meta: { title: '商品订单评论表管理' }
      },
      {
        path: 'modules/cancelcardrenewal',
        name: 'Cancelcardrenewal',
        component: () => import('@/views/modules/cancelcardrenewal/List.vue'),
        meta: { title: '取消续卡记录管理' }
      },
      {
        path: 'modules/refundcardrenewal',
        name: 'Refundcardrenewal',
        component: () => import('@/views/modules/refundcardrenewal/List.vue'),
        meta: { title: '续卡记录退款管理' }
      },
      {
        path: 'modules/discussshare',
        name: 'Discussshare',
        component: () => import('@/views/modules/discussshare/List.vue'),
        meta: { title: '会员交流评论表管理' }
      },
      {
        path: 'modules/discussnews',
        name: 'Discussnews',
        component: () => import('@/views/modules/discussnews/List.vue'),
        meta: { title: '公告资讯评论表管理' }
      },
      {
        path: 'modules/newstype',
        name: 'Newstype',
        component: () => import('@/views/modules/newstype/List.vue'),
        meta: { title: '公告分类管理' }
      },
      {
        path: 'modules/news',
        name: 'News',
        component: () => import('@/views/modules/news/List.vue'),
        meta: { title: '公告资讯管理' }
      },
      {
        path: 'modules/discussnews',
        name: 'NewsDiscuss',
        component: () => import('@/views/modules/discussnews/List.vue'),
        meta: { title: '公告资讯评论管理' }
      },
      {
        path: 'modules/config',
        name: 'Config',
        component: () => import('@/views/modules/config/List.vue'),
        meta: { title: '轮播图管理' }
      },
      {
        path: 'modules/log',
        name: 'Log',
        component: () => import('@/views/modules/log/List.vue'),
        meta: { title: '操作日志管理' }
      },
      {
        path: 'modules/notify',
        name: 'Notify',
        component: () => import('@/views/modules/notify/List.vue'),
        meta: { title: '消息通知管理' }
      },
      {
        path: 'modules/chat',
        name: 'Chat',
        component: () => import('@/views/modules/chat/List.vue'),
        meta: { title: '在线客服管理' }
      },
      {
        path: 'modules/chatmessage',
        name: 'Chatmessage',
        component: () => import('@/views/modules/chatmessage/List.vue'),
        meta: { title: '消息表管理' }
      },
      {
        path: 'modules/friend',
        name: 'Friend',
        component: () => import('@/views/modules/friend/List.vue'),
        meta: { title: '好友表管理' }
      },
      {
        path: 'modules/chatworkbench',
        name: 'ChatWorkbench',
        component: () => import('@/views/modules/chat/Workbench.vue'),
        meta: { title: '客服工作台' }
      },
      {
        path: 'modules/notify',
        name: 'Notify',
        component: () => import('@/views/modules/notify/List.vue'),
        meta: { title: '消息通知管理' }
      },
      // 支付页面（固定）
      {
        path: 'pay',
        name: 'Pay',
        component: () => import('@/views/Pay.vue'),
        meta: { title: '支付' }
      },
      // 个人信息
      {
        path: 'profile',
        name: 'Profile',
        component: () => import('@/views/Profile.vue'),
        meta: { title: '个人信息' }
      },
      // 修改密码
      {
        path: 'update-password',
        name: 'UpdatePassword',
        component: () => import('@/views/UpdatePassword.vue'),
        meta: { title: '修改密码' }
      },
      // 404页面处理
      {
        path: '/:pathMatch(.*)*',
        name: '404',
        component: () => import('@/views/404.vue'),
        meta: { requiresAuth: false }
      }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

// 路由守卫
router.beforeEach((to, _from, next) => {
  const authStore = useAuthStore()
  
  if (to.meta.requiresAuth !== false && !authStore.token) {
    next('/login')
  } else {
    next()
  }
})

export default router
