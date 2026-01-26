// 通用类型定义
export interface PageParams {
  page: number
  limit: number
  sort?: string
  order?: 'asc' | 'desc'
}
// 分页结果
export interface PageResult<T> {
  code: number
  msg: string
  data: {
    list: T[]
    total: number
    totalPage: number
    currPage: number
  }
}
// API响应
export interface ApiResponse<T = any> {
  code: number
  msg: string
  data?: T
}
// 角色登录字段映射
export const userInfoMapping = {
    user : {
        usernameField: 'useraccount',
        passwordField: 'userpassword'
    },
    coach : {
        usernameField: 'coachaccount',
        passwordField: 'coachpassword'
    },
    admin : {
        usernameField: 'adminaccount',
        passwordField: 'adminpassword'
    },
}
// 用户
export interface UserItem {
    id?: number
    useraccount?: string
    userpassword?: string
    name?: string
    sex?: string
    age?: number
    phone?: string
    image?: string
    money?: number
    addtime?: string
    cardno?: string
    memberlevel?: string
    expiretime?: string
    remainingcourses?: number
}
// 教练
export interface CoachItem {
    id?: number
    coachaccount?: string
    coachpassword?: string
    coachname?: string
    coachimage?: string
    sex?: string
    phone?: string
    jobno?: string
    specialty?: string
    coachlevel?: string
    workyears?: number
    addtime?: string
}
// 课程分类
export interface CoursetypeItem {
    id?: number
    coursetypename?: string
    addtime?: string
}
// 健身课程
export interface CourseItem {
    id?: number
    coursename?: string
    courseimage?: string
    coursetype?: string
    classtime?: string
    duration?: number
    coachname?: string
    coachaccount?: string
    quota?: number
    coursedesc?: string
    addtime?: string
    favoritenum?: number
    likenum?: number
    stepnum?: number
    clicknum?: number
    clicktime?: string
    auditstatus?: string
    auditreply?: string
    discussnum?: number
}
// 课程报名记录
export interface CourseenrollmentItem {
    id?: number
    coursename?: string
    courseimage?: string
    coursetype?: string
    classtime?: string
    duration?: number
    coachname?: string
    userid?: number
    useraccount?: string
    coachaccount?: string
    courseprice?: number
    quantity?: number
    totalprice?: number
    addtime?: string
    ispay?: string
    orderstatus?: string
    logistics?: string
    crossuserid?: number
    crossrefid?: number
}
// 商品分类
export interface ProducttypeItem {
    id?: number
    producttypename?: string
    addtime?: string
}
// 健身商品
export interface ProductItem {
    id?: number
    productname?: string
    productimage?: string
    producttype?: string
    productprice?: number
    stock?: number
    productdesc?: string
    addtime?: string
    favoritenum?: number
    likenum?: number
    stepnum?: number
    clicknum?: number
    clicktime?: string
    auditstatus?: string
    auditreply?: string
    discussnum?: number
}
// 商品订单
export interface ProductorderItem {
    id?: number
    productname?: string
    productimage?: string
    producttype?: string
    productprice?: number
    quantity?: number
    totalprice?: number
    addtime?: string
    ispay?: string
    orderstatus?: string
    logistics?: string
    discussnum?: number
    userid?: number
    crossuserid?: number
    crossrefid?: number
}
// 会员卡套餐
export interface MembershippackageItem {
    id?: number
    packagename?: string
    packageimage?: string
    packagetype?: string
    packageprice?: number
    validdays?: number
    includedcourses?: number
    packagedesc?: string
    addtime?: string
}
// 办卡记录
export interface CardapplicationItem {
    id?: number
    packagename?: string
    packageimage?: string
    packagetype?: string
    packageprice?: number
    validdays?: number
    includedcourses?: number
    quantity?: number
    userid?: number
    addtime?: string
    ispay?: string
    orderstatus?: string
    logistics?: string
    crossuserid?: number
    crossrefid?: number
}
// 续卡记录
export interface CardrenewalItem {
    id?: number
    packagename?: string
    packagetype?: string
    packageprice?: number
    renewaldays?: number
    addtime?: string
    ispay?: string
    orderstatus?: string
    logistics?: string
    userid?: number
    crossuserid?: number
    crossrefid?: number
}
// 交流分类
export interface SharetypeItem {
    id?: number
    sharetypename?: string
    addtime?: string
}
// 会员交流
export interface ShareItem {
    id?: number
    sharetitle?: string
    sharecontent?: string
    shareimage?: string
    sharetype?: string
    addtime?: string
    favoritenum?: number
    likenum?: number
    stepnum?: number
    clicknum?: number
    clicktime?: string
    auditstatus?: string
    auditreply?: string
    discussnum?: number
}
// 意见反馈
export interface FeedbackItem {
    id?: number
    feedbacktitle?: string
    feedbackcontent?: string
    feedbacktype?: string
    userid?: number
    addtime?: string
    auditstatus?: string
    auditreply?: string
}
// 健身课程评论表
export interface DiscusscourseItem {
    id?: number
    refid?: number
    userid?: number
    nickname?: string
    avatarurl?: string
    content?: string
    reply?: string
    likenum?: number
    stepnum?: number
    addtime?: string
}
// 取消课程报名记录
export interface CancelcourseenrollmentItem {
    id?: number
    coursename?: string
    courseimage?: string
    coursetype?: string
    classtime?: string
    duration?: number
    coachname?: string
    userid?: number
    cancelreason?: string
    addtime?: string
    crossuserid?: number
    crossrefid?: number
}
// 课程报名记录退款
export interface RefundcourseenrollmentItem {
    id?: number
    coursename?: string
    courseimage?: string
    coursetype?: string
    classtime?: string
    duration?: number
    coachname?: string
    userid?: number
    refundreason?: string
    addtime?: string
    crossuserid?: number
    crossrefid?: number
}
// 健身商品评论表
export interface DiscussproductItem {
    id?: number
    refid?: number
    userid?: number
    nickname?: string
    avatarurl?: string
    content?: string
    reply?: string
    likenum?: number
    stepnum?: number
    addtime?: string
}
// 取消办卡记录
export interface CancelcardapplicationItem {
    id?: number
    packagename?: string
    packageimage?: string
    packagetype?: string
    packageprice?: number
    validdays?: number
    includedcourses?: number
    userid?: number
    cancelreason?: string
    addtime?: string
    crossuserid?: number
    crossrefid?: number
}
// 办卡记录退款
export interface RefundcardapplicationItem {
    id?: number
    packagename?: string
    packageimage?: string
    packagetype?: string
    packageprice?: number
    validdays?: number
    includedcourses?: number
    userid?: number
    refundreason?: string
    addtime?: string
    crossuserid?: number
    crossrefid?: number
}
// 取消商品订单
export interface CancelproductorderItem {
    id?: number
    productname?: string
    productimage?: string
    producttype?: string
    productprice?: number
    quantity?: number
    totalprice?: number
    userid?: number
    cancelreason?: string
    addtime?: string
    crossuserid?: number
    crossrefid?: number
}
// 商品订单退款
export interface RefundproductorderItem {
    id?: number
    productname?: string
    productimage?: string
    producttype?: string
    productprice?: number
    quantity?: number
    totalprice?: number
    userid?: number
    refundreason?: string
    addtime?: string
    crossuserid?: number
    crossrefid?: number
}
// 商品订单评论表
export interface DiscussproductorderItem {
    id?: number
    refid?: number
    userid?: number
    nickname?: string
    avatarurl?: string
    content?: string
    reply?: string
    likenum?: number
    stepnum?: number
    addtime?: string
}
// 取消续卡记录
export interface CancelcardrenewalItem {
    id?: number
    packagename?: string
    packagetype?: string
    packageprice?: number
    renewaldays?: number
    userid?: number
    cancelreason?: string
    addtime?: string
    crossuserid?: number
    crossrefid?: number
}
// 续卡记录退款
export interface RefundcardrenewalItem {
    id?: number
    packagename?: string
    packagetype?: string
    packageprice?: number
    renewaldays?: number
    userid?: number
    refundreason?: string
    addtime?: string
    crossuserid?: number
    crossrefid?: number
}
// 会员交流评论表
export interface DiscussshareItem {
    id?: number
    refid?: number
    userid?: number
    nickname?: string
    avatarurl?: string
    content?: string
    reply?: string
    likenum?: number
    stepnum?: number
    addtime?: string
}
// 公告资讯评论表
export interface DiscussnewsItem {
    id?: number
    refid?: number
    userid?: number
    nickname?: string
    avatarurl?: string
    content?: string
    reply?: string
    likenum?: number
    stepnum?: number
    addtime?: string
}
// 管理员
export interface AdminItem {
    id?: number
    adminaccount?: string
    adminpassword?: string
    image?: string
    role?: string
    addtime?: string
}
// 公告分类
export interface NewstypeItem {
    id?: number
    typename?: string
    addtime?: string
}
// 公告资讯
export interface NewsItem {
    id?: number
    title?: string
    introduction?: string
    typename?: string
    name?: string
    avatar?: string
    image?: string
    content?: string
    addtime?: string
    favoritenum?: number
    likenum?: number
    stepnum?: number
    clicknum?: number
    clicktime?: string
    auditstatus?: string
    auditreply?: string
    discussnum?: number
}
// 轮播图
export interface ConfigItem {
    id?: number
    name?: string
    value?: string
    url?: string
}
// 操作日志
export interface LogItem {
    id?: number
    tablename?: string
    module?: string
    operatetype?: string
    businessid?: number
    operatorid?: number
    operatorname?: string
    requesturl?: string
    requestmethod?: string
    requestip?: string
    detail?: string
    addtime?: string
    userid?: number
}
// 消息通知
export interface NotifyItem {
    id?: number
    userid?: number
    title?: string
    content?: string
    messagetype?: string
    readstatus?: string
    senduser?: string
    addtime?: string
}
// 在线客服
export interface ChatItem {
    id?: number
    userid?: number
    adminid?: number
    ask?: string
    reply?: string
    asktype?: number
    replytype?: number
    isreply?: number
    isread?: number
    uname?: string
    uimage?: string
    addtime?: string
}
// 消息表
export interface ChatmessageItem {
    id?: number
    uid?: number
    fid?: number
    tablename?: string
    content?: string
    format?: number
    isread?: number
    addtime?: string
}
// 好友表
export interface FriendItem {
    id?: number
    uid?: number
    fid?: number
    name?: string
    picture?: string
    role?: string
    tablename?: string
    alias?: string
    type?: number
    addtime?: string
    userid?: number
}
