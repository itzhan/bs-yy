export interface SummaryFieldMeta {
  name: string
  label: string
}

export interface TextQueryMeta {
  field: string
  label: string
  fuzzy: boolean
}

export interface SelectFilterMeta {
  field: string
  label: string
  options: string[]
}

export interface RangeFilterMeta {
  field: string
  label: string
  type: 'number' | 'date'
}

export interface CategoryMeta {
  field: string
  label: string
  column: string
  table?: string
  options?: string[]
  imageField?: string
}

export interface TableFrontMeta {
  label: string
  imageField?: string
  titleField?: string
  summaryFields: SummaryFieldMeta[]
  textQueries: TextQueryMeta[]
  selectFilters: SelectFilterMeta[]
  rangeFilters: RangeFilterMeta[]
  category?: CategoryMeta | null
  stats: {
    like: boolean
    favorite: boolean
    click: boolean
  }
  hasDiscuss: boolean
  hasPay: boolean
}

export const frontMeta: Record<string, TableFrontMeta> = {
  'user': {
    label: '用户',
    imageField: 'image',
    titleField: 'name',
    summaryFields: [
          { name: 'useraccount', label: '账号' }
          ,{ name: 'sex', label: '性别' }
          ,{ name: 'age', label: '年龄' }
          ,{ name: 'phone', label: '手机号码' }
          ,{ name: 'money', label: '余额' }
          ,{ name: 'cardno', label: '会员卡号' }
    ],
    textQueries: [
          { field: 'useraccount', label: '账号', fuzzy: true }
          ,{ field: 'name', label: '姓名', fuzzy: true }
    ],
    selectFilters: [
          { field: 'sex', label: '性别', options: ['男', '女'] }
          ,{ field: 'memberlevel', label: '会员等级', options: ['普通会员', '银卡会员', '金卡会员', '钻石会员'] }
    ],
    rangeFilters: [
          { field: 'age', label: '年龄', type: 'number' }
    ],
    category: null,
    stats: {
      like: false,
      likeField: null,
      favorite: false,
      favoriteField: null,
      click: false,
      clickField: null
    },
    hasDiscuss: false,
    hasPay: false
  },
  'coach': {
    label: '教练',
    imageField: 'coachimage',
    titleField: 'coachname',
    summaryFields: [
          { name: 'coachaccount', label: '账号' }
          ,{ name: 'sex', label: '性别' }
          ,{ name: 'phone', label: '手机号码' }
          ,{ name: 'jobno', label: '工号' }
          ,{ name: 'specialty', label: '擅长领域' }
          ,{ name: 'coachlevel', label: '教练等级' }
    ],
    textQueries: [
          { field: 'coachaccount', label: '账号', fuzzy: true }
          ,{ field: 'coachname', label: '教练姓名', fuzzy: true }
          ,{ field: 'jobno', label: '工号', fuzzy: true }
    ],
    selectFilters: [
          { field: 'sex', label: '性别', options: ['男', '女'] }
          ,{ field: 'coachlevel', label: '教练等级', options: ['初级', '中级', '高级', '资深'] }
    ],
    rangeFilters: [
    ],
    category: null,
    stats: {
      like: false,
      likeField: null,
      favorite: false,
      favoriteField: null,
      click: false,
      clickField: null
    },
    hasDiscuss: false,
    hasPay: false
  },
  'coursetype': {
    label: '课程分类',
    titleField: 'coursetypename',
    summaryFields: [
    ],
    textQueries: [
          { field: 'coursetypename', label: '分类名称', fuzzy: true }
    ],
    selectFilters: [
    ],
    rangeFilters: [
    ],
    category: null,
    stats: {
      like: false,
      likeField: null,
      favorite: false,
      favoriteField: null,
      click: false,
      clickField: null
    },
    hasDiscuss: false,
    hasPay: false
  },
  'course': {
    label: '健身课程',
    imageField: 'courseimage',
    titleField: 'coursename',
    summaryFields: [
          { name: 'coursetype', label: '课程分类' }
          ,{ name: 'classtime', label: '上课时间' }
          ,{ name: 'duration', label: '课程时长' }
          ,{ name: 'coachname', label: '教练' }
          ,{ name: 'quota', label: '剩余名额' }
          ,{ name: 'auditstatus', label: '审核状态' }
    ],
    textQueries: [
          { field: 'coursename', label: '课程名称', fuzzy: true }
    ],
    selectFilters: [
          { field: 'auditstatus', label: '审核状态', options: ['待审核', '通过', '不通过'] }
    ],
    rangeFilters: [
          { field: 'classtime', label: '上课时间', type: 'date' }
    ],
    category: {
      field: 'coursetype',
      label: '课程分类',
      column: 'coursetypename',
      table: 'coursetype'
    },
    stats: {
      like: true,
      likeField: 'likenum',
      favorite: true,
      favoriteField: 'favoritenum',
      click: true,
      clickField: 'clicknum'
    },
    hasDiscuss: true,
    hasPay: false
  },
  'courseenrollment': {
    label: '课程报名记录',
    imageField: 'courseimage',
    titleField: 'coursename',
    summaryFields: [
          { name: 'coursetype', label: '课程分类' }
          ,{ name: 'classtime', label: '上课时间' }
          ,{ name: 'coachname', label: '教练' }
          ,{ name: 'courseprice', label: '课程单价' }
          ,{ name: 'totalprice', label: '总价' }
          ,{ name: 'ispay', label: '是否支付' }
    ],
    textQueries: [
    ],
    selectFilters: [
          { field: 'ispay', label: '是否支付', options: ['未支付', '已支付'] }
          ,{ field: 'orderstatus', label: '状态', options: ['未支付', '已支付', '已取消', '已退款', '已发货', '已完成'] }
    ],
    rangeFilters: [
    ],
    category: {
      field: 'coursename',
      label: '课程名称',
      column: 'coursename',
      table: 'course',
      imageField: 'courseimage'
    },
    stats: {
      like: false,
      likeField: null,
      favorite: false,
      favoriteField: null,
      click: false,
      clickField: null
    },
    hasDiscuss: false,
    hasPay: true
  },
  'producttype': {
    label: '商品分类',
    titleField: 'producttypename',
    summaryFields: [
    ],
    textQueries: [
          { field: 'producttypename', label: '分类名称', fuzzy: true }
    ],
    selectFilters: [
    ],
    rangeFilters: [
    ],
    category: null,
    stats: {
      like: false,
      likeField: null,
      favorite: false,
      favoriteField: null,
      click: false,
      clickField: null
    },
    hasDiscuss: false,
    hasPay: false
  },
  'product': {
    label: '健身商品',
    imageField: 'productimage',
    titleField: 'productname',
    summaryFields: [
          { name: 'producttype', label: '商品分类' }
          ,{ name: 'productprice', label: '价格' }
          ,{ name: 'stock', label: '库存' }
          ,{ name: 'auditstatus', label: '审核状态' }
    ],
    textQueries: [
          { field: 'productname', label: '商品名称', fuzzy: true }
    ],
    selectFilters: [
          { field: 'auditstatus', label: '审核状态', options: ['待审核', '通过', '未通过'] }
    ],
    rangeFilters: [
          { field: 'productprice', label: '价格', type: 'number' }
    ],
    category: {
      field: 'producttype',
      label: '商品分类',
      column: 'producttypename',
      table: 'producttype'
    },
    stats: {
      like: true,
      likeField: 'likenum',
      favorite: true,
      favoriteField: 'favoritenum',
      click: true,
      clickField: 'clicknum'
    },
    hasDiscuss: true,
    hasPay: false
  },
  'productorder': {
    label: '商品订单',
    imageField: 'productimage',
    titleField: 'productname',
    summaryFields: [
          { name: 'producttype', label: '商品分类' }
          ,{ name: 'productprice', label: '商品单价' }
          ,{ name: 'quantity', label: '购买数量' }
          ,{ name: 'totalprice', label: '订单总价' }
          ,{ name: 'ispay', label: '是否支付' }
          ,{ name: 'orderstatus', label: '状态' }
    ],
    textQueries: [
    ],
    selectFilters: [
          { field: 'ispay', label: '是否支付', options: ['未支付', '已支付'] }
          ,{ field: 'orderstatus', label: '状态', options: ['未支付', '已支付', '已取消', '已退款', '已发货', '已完成'] }
    ],
    rangeFilters: [
    ],
    category: {
      field: 'productname',
      label: '商品名称',
      column: 'productname',
      table: 'product',
      imageField: 'productimage'
    },
    stats: {
      like: false,
      likeField: null,
      favorite: false,
      favoriteField: null,
      click: false,
      clickField: null
    },
    hasDiscuss: true,
    hasPay: true
  },
  'membershippackage': {
    label: '会员卡套餐',
    imageField: 'packageimage',
    titleField: 'packagename',
    summaryFields: [
          { name: 'packagetype', label: '套餐类型' }
          ,{ name: 'packageprice', label: '价格' }
          ,{ name: 'validdays', label: '有效期' }
          ,{ name: 'includedcourses', label: '包含课时' }
    ],
    textQueries: [
          { field: 'packagename', label: '套餐名称', fuzzy: true }
    ],
    selectFilters: [
          { field: 'packagetype', label: '套餐类型', options: ['月卡', '季卡', '年卡'] }
    ],
    rangeFilters: [
          { field: 'packageprice', label: '价格', type: 'number' }
    ],
    category: null,
    stats: {
      like: false,
      likeField: null,
      favorite: false,
      favoriteField: null,
      click: false,
      clickField: null
    },
    hasDiscuss: false,
    hasPay: false
  },
  'cardapplication': {
    label: '办卡记录',
    imageField: 'packageimage',
    titleField: 'packagename',
    summaryFields: [
          { name: 'packagetype', label: '套餐类型' }
          ,{ name: 'packageprice', label: '价格' }
          ,{ name: 'validdays', label: '有效期' }
          ,{ name: 'includedcourses', label: '包含课时' }
          ,{ name: 'ispay', label: '是否支付' }
          ,{ name: 'orderstatus', label: '状态' }
    ],
    textQueries: [
          { field: 'packagetype', label: '套餐类型', fuzzy: true }
    ],
    selectFilters: [
          { field: 'ispay', label: '是否支付', options: ['未支付', '已支付'] }
          ,{ field: 'orderstatus', label: '状态', options: ['未支付', '已支付', '已取消', '已退款', '已发货', '已完成'] }
    ],
    rangeFilters: [
    ],
    category: {
      field: 'packagename',
      label: '套餐名称',
      column: 'packagename',
      table: 'membershippackage',
      imageField: 'packageimage'
    },
    stats: {
      like: false,
      likeField: null,
      favorite: false,
      favoriteField: null,
      click: false,
      clickField: null
    },
    hasDiscuss: false,
    hasPay: true
  },
  'cardrenewal': {
    label: '续卡记录',
    titleField: 'packagename',
    summaryFields: [
          { name: 'packagetype', label: '套餐类型' }
          ,{ name: 'packageprice', label: '价格' }
          ,{ name: 'renewaldays', label: '续费时长' }
          ,{ name: 'ispay', label: '是否支付' }
          ,{ name: 'orderstatus', label: '状态' }
          ,{ name: 'crossuserid', label: '跨表用户id' }
    ],
    textQueries: [
    ],
    selectFilters: [
          { field: 'ispay', label: '是否支付', options: ['未支付', '已支付'] }
          ,{ field: 'orderstatus', label: '状态', options: ['未支付', '已支付', '已取消', '已退款', '已发货', '已完成'] }
    ],
    rangeFilters: [
    ],
    category: {
      field: 'packagename',
      label: '套餐名称',
      column: 'packagename',
      table: 'membershippackage',
      imageField: 'packageimage'
    },
    stats: {
      like: false,
      likeField: null,
      favorite: false,
      favoriteField: null,
      click: false,
      clickField: null
    },
    hasDiscuss: false,
    hasPay: true
  },
  'sharetype': {
    label: '交流分类',
    titleField: 'sharetypename',
    summaryFields: [
    ],
    textQueries: [
          { field: 'sharetypename', label: '分类名称', fuzzy: true }
    ],
    selectFilters: [
    ],
    rangeFilters: [
    ],
    category: null,
    stats: {
      like: false,
      likeField: null,
      favorite: false,
      favoriteField: null,
      click: false,
      clickField: null
    },
    hasDiscuss: false,
    hasPay: false
  },
  'share': {
    label: '会员交流',
    imageField: 'shareimage',
    titleField: 'sharetitle',
    summaryFields: [
          { name: 'sharetype', label: '分类' }
          ,{ name: 'auditstatus', label: '审核状态' }
          ,{ name: 'discussnum', label: '评论数' }
    ],
    textQueries: [
          { field: 'sharetitle', label: '标题', fuzzy: true }
    ],
    selectFilters: [
          { field: 'auditstatus', label: '审核状态', options: ['待审核', '通过', '未通过'] }
    ],
    rangeFilters: [
    ],
    category: {
      field: 'sharetype',
      label: '分类',
      column: 'sharetypename',
      table: 'sharetype'
    },
    stats: {
      like: true,
      likeField: 'likenum',
      favorite: true,
      favoriteField: 'favoritenum',
      click: true,
      clickField: 'clicknum'
    },
    hasDiscuss: true,
    hasPay: false
  },
  'feedback': {
    label: '意见反馈',
    titleField: 'feedbacktitle',
    summaryFields: [
          { name: 'feedbacktype', label: '反馈类型' }
          ,{ name: 'auditstatus', label: '审核状态' }
          ,{ name: 'auditreply', label: '审核回复' }
    ],
    textQueries: [
          { field: 'feedbacktitle', label: '标题', fuzzy: true }
    ],
    selectFilters: [
          { field: 'feedbacktype', label: '反馈类型', options: ['功能建议', '问题反馈', '投诉'] }
          ,{ field: 'auditstatus', label: '审核状态', options: ['待审核', '通过', '未通过'] }
    ],
    rangeFilters: [
    ],
    category: null,
    stats: {
      like: false,
      likeField: null,
      favorite: false,
      favoriteField: null,
      click: false,
      clickField: null
    },
    hasDiscuss: false,
    hasPay: false
  },
  'discusscourse': {
    label: '健身课程评论表',
    imageField: 'nickname',
    titleField: 'nickname',
    summaryFields: [
          { name: 'id', label: '主键' }
          ,{ name: 'refid', label: '关联表id' }
          ,{ name: 'userid', label: '用户id' }
          ,{ name: 'avatarurl', label: '头像' }
          ,{ name: 'content', label: '评论内容' }
          ,{ name: 'reply', label: '回复内容' }
    ],
    textQueries: [
    ],
    selectFilters: [
    ],
    rangeFilters: [
    ],
    category: null,
    stats: {
      like: false,
      likeField: 'likenum',
      favorite: false,
      favoriteField: null,
      click: false,
      clickField: null
    },
    hasDiscuss: false,
    hasPay: false
  },
  'cancelcourseenrollment': {
    label: '取消课程报名记录',
    imageField: 'courseimage',
    titleField: 'coursename',
    summaryFields: [
          { name: 'coursetype', label: '课程分类' }
          ,{ name: 'classtime', label: '上课时间' }
          ,{ name: 'coachname', label: '教练' }
          ,{ name: 'cancelreason', label: '取消原因' }
          ,{ name: 'crossuserid', label: '跨表用户id' }
          ,{ name: 'crossrefid', label: '跨表来源id' }
    ],
    textQueries: [
    ],
    selectFilters: [
    ],
    rangeFilters: [
    ],
    category: {
      field: 'coursename',
      label: '课程名称',
      column: 'coursename',
      table: 'course',
      imageField: 'courseimage'
    },
    stats: {
      like: false,
      likeField: null,
      favorite: false,
      favoriteField: null,
      click: false,
      clickField: null
    },
    hasDiscuss: false,
    hasPay: false
  },
  'refundcourseenrollment': {
    label: '课程报名记录退款',
    imageField: 'courseimage',
    titleField: 'coursename',
    summaryFields: [
          { name: 'coursetype', label: '课程分类' }
          ,{ name: 'classtime', label: '上课时间' }
          ,{ name: 'coachname', label: '教练' }
          ,{ name: 'refundreason', label: '退款原因' }
          ,{ name: 'crossuserid', label: '跨表用户id' }
          ,{ name: 'crossrefid', label: '跨表来源id' }
    ],
    textQueries: [
    ],
    selectFilters: [
    ],
    rangeFilters: [
    ],
    category: {
      field: 'coursename',
      label: '课程名称',
      column: 'coursename',
      table: 'course',
      imageField: 'courseimage'
    },
    stats: {
      like: false,
      likeField: null,
      favorite: false,
      favoriteField: null,
      click: false,
      clickField: null
    },
    hasDiscuss: false,
    hasPay: false
  },
  'discussproduct': {
    label: '健身商品评论表',
    imageField: 'nickname',
    titleField: 'nickname',
    summaryFields: [
          { name: 'id', label: '主键' }
          ,{ name: 'refid', label: '关联表id' }
          ,{ name: 'userid', label: '用户id' }
          ,{ name: 'avatarurl', label: '头像' }
          ,{ name: 'content', label: '评论内容' }
          ,{ name: 'reply', label: '回复内容' }
    ],
    textQueries: [
    ],
    selectFilters: [
    ],
    rangeFilters: [
    ],
    category: null,
    stats: {
      like: false,
      likeField: 'likenum',
      favorite: false,
      favoriteField: null,
      click: false,
      clickField: null
    },
    hasDiscuss: false,
    hasPay: false
  },
  'cancelcardapplication': {
    label: '取消办卡记录',
    imageField: 'packageimage',
    titleField: 'packagename',
    summaryFields: [
          { name: 'packagetype', label: '套餐类型' }
          ,{ name: 'packageprice', label: '价格' }
          ,{ name: 'validdays', label: '有效期' }
          ,{ name: 'includedcourses', label: '包含课时' }
          ,{ name: 'cancelreason', label: '取消原因' }
          ,{ name: 'crossuserid', label: '跨表用户id' }
    ],
    textQueries: [
          { field: 'packagetype', label: '套餐类型', fuzzy: true }
    ],
    selectFilters: [
    ],
    rangeFilters: [
    ],
    category: {
      field: 'packagename',
      label: '套餐名称',
      column: 'packagename',
      table: 'membershippackage',
      imageField: 'packageimage'
    },
    stats: {
      like: false,
      likeField: null,
      favorite: false,
      favoriteField: null,
      click: false,
      clickField: null
    },
    hasDiscuss: false,
    hasPay: false
  },
  'refundcardapplication': {
    label: '办卡记录退款',
    imageField: 'packageimage',
    titleField: 'packagename',
    summaryFields: [
          { name: 'packagetype', label: '套餐类型' }
          ,{ name: 'packageprice', label: '价格' }
          ,{ name: 'validdays', label: '有效期' }
          ,{ name: 'includedcourses', label: '包含课时' }
          ,{ name: 'refundreason', label: '退款原因' }
          ,{ name: 'crossuserid', label: '跨表用户id' }
    ],
    textQueries: [
          { field: 'packagetype', label: '套餐类型', fuzzy: true }
    ],
    selectFilters: [
    ],
    rangeFilters: [
    ],
    category: {
      field: 'packagename',
      label: '套餐名称',
      column: 'packagename',
      table: 'membershippackage',
      imageField: 'packageimage'
    },
    stats: {
      like: false,
      likeField: null,
      favorite: false,
      favoriteField: null,
      click: false,
      clickField: null
    },
    hasDiscuss: false,
    hasPay: false
  },
  'cancelproductorder': {
    label: '取消商品订单',
    imageField: 'productimage',
    titleField: 'productname',
    summaryFields: [
          { name: 'producttype', label: '商品分类' }
          ,{ name: 'productprice', label: '商品单价' }
          ,{ name: 'quantity', label: '购买数量' }
          ,{ name: 'totalprice', label: '订单总价' }
          ,{ name: 'cancelreason', label: '取消原因' }
          ,{ name: 'crossuserid', label: '跨表用户id' }
    ],
    textQueries: [
    ],
    selectFilters: [
    ],
    rangeFilters: [
    ],
    category: {
      field: 'productname',
      label: '商品名称',
      column: 'productname',
      table: 'product',
      imageField: 'productimage'
    },
    stats: {
      like: false,
      likeField: null,
      favorite: false,
      favoriteField: null,
      click: false,
      clickField: null
    },
    hasDiscuss: false,
    hasPay: false
  },
  'refundproductorder': {
    label: '商品订单退款',
    imageField: 'productimage',
    titleField: 'productname',
    summaryFields: [
          { name: 'producttype', label: '商品分类' }
          ,{ name: 'productprice', label: '商品单价' }
          ,{ name: 'quantity', label: '购买数量' }
          ,{ name: 'totalprice', label: '订单总价' }
          ,{ name: 'refundreason', label: '退款原因' }
          ,{ name: 'crossuserid', label: '跨表用户id' }
    ],
    textQueries: [
    ],
    selectFilters: [
    ],
    rangeFilters: [
    ],
    category: {
      field: 'productname',
      label: '商品名称',
      column: 'productname',
      table: 'product',
      imageField: 'productimage'
    },
    stats: {
      like: false,
      likeField: null,
      favorite: false,
      favoriteField: null,
      click: false,
      clickField: null
    },
    hasDiscuss: false,
    hasPay: false
  },
  'discussproductorder': {
    label: '商品订单评论表',
    imageField: 'nickname',
    titleField: 'nickname',
    summaryFields: [
          { name: 'id', label: '主键' }
          ,{ name: 'refid', label: '关联表id' }
          ,{ name: 'userid', label: '用户id' }
          ,{ name: 'avatarurl', label: '头像' }
          ,{ name: 'content', label: '评论内容' }
          ,{ name: 'reply', label: '回复内容' }
    ],
    textQueries: [
    ],
    selectFilters: [
    ],
    rangeFilters: [
    ],
    category: null,
    stats: {
      like: false,
      likeField: 'likenum',
      favorite: false,
      favoriteField: null,
      click: false,
      clickField: null
    },
    hasDiscuss: false,
    hasPay: false
  },
  'cancelcardrenewal': {
    label: '取消续卡记录',
    titleField: 'packagename',
    summaryFields: [
          { name: 'packagetype', label: '套餐类型' }
          ,{ name: 'packageprice', label: '价格' }
          ,{ name: 'renewaldays', label: '续费时长' }
          ,{ name: 'cancelreason', label: '取消原因' }
          ,{ name: 'crossuserid', label: '跨表用户id' }
          ,{ name: 'crossrefid', label: '跨表来源id' }
    ],
    textQueries: [
    ],
    selectFilters: [
    ],
    rangeFilters: [
    ],
    category: {
      field: 'packagename',
      label: '套餐名称',
      column: 'packagename',
      table: 'membershippackage',
      imageField: 'packageimage'
    },
    stats: {
      like: false,
      likeField: null,
      favorite: false,
      favoriteField: null,
      click: false,
      clickField: null
    },
    hasDiscuss: false,
    hasPay: false
  },
  'refundcardrenewal': {
    label: '续卡记录退款',
    titleField: 'packagename',
    summaryFields: [
          { name: 'packagetype', label: '套餐类型' }
          ,{ name: 'packageprice', label: '价格' }
          ,{ name: 'renewaldays', label: '续费时长' }
          ,{ name: 'refundreason', label: '退款原因' }
          ,{ name: 'crossuserid', label: '跨表用户id' }
          ,{ name: 'crossrefid', label: '跨表来源id' }
    ],
    textQueries: [
    ],
    selectFilters: [
    ],
    rangeFilters: [
    ],
    category: {
      field: 'packagename',
      label: '套餐名称',
      column: 'packagename',
      table: 'membershippackage',
      imageField: 'packageimage'
    },
    stats: {
      like: false,
      likeField: null,
      favorite: false,
      favoriteField: null,
      click: false,
      clickField: null
    },
    hasDiscuss: false,
    hasPay: false
  },
  'discussshare': {
    label: '会员交流评论表',
    imageField: 'nickname',
    titleField: 'nickname',
    summaryFields: [
          { name: 'id', label: '主键' }
          ,{ name: 'refid', label: '关联表id' }
          ,{ name: 'userid', label: '用户id' }
          ,{ name: 'avatarurl', label: '头像' }
          ,{ name: 'content', label: '评论内容' }
          ,{ name: 'reply', label: '回复内容' }
    ],
    textQueries: [
    ],
    selectFilters: [
    ],
    rangeFilters: [
    ],
    category: null,
    stats: {
      like: false,
      likeField: 'likenum',
      favorite: false,
      favoriteField: null,
      click: false,
      clickField: null
    },
    hasDiscuss: false,
    hasPay: false
  },
  'discussnews': {
    label: '公告资讯评论表',
    imageField: 'nickname',
    titleField: 'nickname',
    summaryFields: [
          { name: 'id', label: '主键' }
          ,{ name: 'refid', label: '关联表id' }
          ,{ name: 'userid', label: '用户id' }
          ,{ name: 'avatarurl', label: '头像' }
          ,{ name: 'content', label: '评论内容' }
          ,{ name: 'reply', label: '回复内容' }
    ],
    textQueries: [
    ],
    selectFilters: [
    ],
    rangeFilters: [
    ],
    category: null,
    stats: {
      like: false,
      likeField: 'likenum',
      favorite: false,
      favoriteField: null,
      click: false,
      clickField: null
    },
    hasDiscuss: false,
    hasPay: false
  },
  'admin': {
    label: '管理员',
    imageField: 'image',
    titleField: 'adminaccount',
    summaryFields: [
          { name: 'role', label: '角色' }
    ],
    textQueries: [
          { field: 'adminaccount', label: '账号', fuzzy: true }
          ,{ field: 'role', label: '角色', fuzzy: true }
    ],
    selectFilters: [
    ],
    rangeFilters: [
    ],
    category: null,
    stats: {
      like: false,
      likeField: null,
      favorite: false,
      favoriteField: null,
      click: false,
      clickField: null
    },
    hasDiscuss: false,
    hasPay: false
  },
  'newstype': {
    label: '公告分类',
    titleField: 'typename',
    summaryFields: [
    ],
    textQueries: [
          { field: 'typename', label: '分类名称', fuzzy: true }
    ],
    selectFilters: [
    ],
    rangeFilters: [
    ],
    category: null,
    stats: {
      like: false,
      likeField: null,
      favorite: false,
      favoriteField: null,
      click: false,
      clickField: null
    },
    hasDiscuss: false,
    hasPay: false
  },
  'news': {
    label: '公告资讯',
    imageField: 'avatar',
    titleField: 'title',
    summaryFields: [
          { name: 'id', label: '主键' }
          ,{ name: 'introduction', label: '简介' }
          ,{ name: 'typename', label: '分类名称' }
          ,{ name: 'name', label: '发布人' }
          ,{ name: 'content', label: '内容' }
          ,{ name: 'auditstatus', label: '审核状态' }
    ],
    textQueries: [
          { field: 'title', label: '标题', fuzzy: true }
    ],
    selectFilters: [
          { field: 'auditstatus', label: '审核状态', options: ['待审核', '通过', '未通过'] }
    ],
    rangeFilters: [
    ],
    category: {
      field: 'typename',
      label: '分类名称',
      column: 'typename',
      table: 'newstype'
    },
    stats: {
      like: true,
      likeField: 'likenum',
      favorite: true,
      favoriteField: 'favoritenum',
      click: true,
      clickField: 'clicknum'
    },
    hasDiscuss: true,
    hasPay: false
  },
  'config': {
    label: '轮播图',
    imageField: 'url',
    titleField: 'name',
    summaryFields: [
          { name: 'value', label: '配置参数值' }
    ],
    textQueries: [
          { field: 'name', label: '配置参数名称', fuzzy: true }
    ],
    selectFilters: [
    ],
    rangeFilters: [
    ],
    category: null,
    stats: {
      like: false,
      likeField: null,
      favorite: false,
      favoriteField: null,
      click: false,
      clickField: null
    },
    hasDiscuss: false,
    hasPay: false
  },
  'log': {
    label: '操作日志',
    titleField: 'tablename',
    summaryFields: [
          { name: 'module', label: '模块名称' }
          ,{ name: 'operatetype', label: '操作类型' }
          ,{ name: 'businessid', label: '业务ID' }
          ,{ name: 'operatorname', label: '操作人名称' }
          ,{ name: 'requesturl', label: '请求路径' }
          ,{ name: 'requestmethod', label: '请求方法' }
    ],
    textQueries: [
          { field: 'module', label: '模块名称', fuzzy: true }
          ,{ field: 'operatetype', label: '操作类型', fuzzy: true }
          ,{ field: 'operatorname', label: '操作人名称', fuzzy: true }
          ,{ field: 'requesturl', label: '请求路径', fuzzy: true }
          ,{ field: 'requestmethod', label: '请求方法', fuzzy: true }
    ],
    selectFilters: [
    ],
    rangeFilters: [
    ],
    category: null,
    stats: {
      like: false,
      likeField: null,
      favorite: false,
      favoriteField: null,
      click: false,
      clickField: null
    },
    hasDiscuss: false,
    hasPay: false
  },
  'notify': {
    label: '消息通知',
    titleField: 'title',
    summaryFields: [
          { name: 'userid', label: '接收用户ID' }
          ,{ name: 'messagetype', label: '消息类型' }
          ,{ name: 'readstatus', label: '阅读状态' }
          ,{ name: 'senduser', label: '发送人' }
    ],
    textQueries: [
          { field: 'title', label: '消息标题', fuzzy: true }
    ],
    selectFilters: [
          { field: 'readstatus', label: '阅读状态', options: ['未读', '已读'] }
    ],
    rangeFilters: [
    ],
    category: {
      field: 'messagetype',
      label: '消息类型',
      column: 'messagetype',
      table: 'notify'
    },
    stats: {
      like: false,
      likeField: null,
      favorite: false,
      favoriteField: null,
      click: false,
      clickField: null
    },
    hasDiscuss: false,
    hasPay: false
  },
  'chat': {
    label: '在线客服',
    imageField: 'uimage',
    titleField: 'uname',
    summaryFields: [
          { name: 'userid', label: '用户ID' }
          ,{ name: 'adminid', label: '管理员ID' }
          ,{ name: 'isreply', label: '是否回复(0/1)' }
          ,{ name: 'isread', label: '是否已读(0/1)' }
    ],
    textQueries: [
          { field: 'userid', label: '用户ID', fuzzy: false }
          ,{ field: 'adminid', label: '管理员ID', fuzzy: false }
          ,{ field: 'isreply', label: '是否回复(0/1)', fuzzy: false }
          ,{ field: 'isread', label: '是否已读(0/1)', fuzzy: false }
          ,{ field: 'uname', label: '用户名', fuzzy: true }
    ],
    selectFilters: [
    ],
    rangeFilters: [
    ],
    category: null,
    stats: {
      like: false,
      likeField: null,
      favorite: false,
      favoriteField: null,
      click: false,
      clickField: null
    },
    hasDiscuss: false,
    hasPay: false
  },
  'chatmessage': {
    label: '消息表',
    titleField: 'tablename',
    summaryFields: [
          { name: 'uid', label: '用户ID' }
          ,{ name: 'fid', label: '好友用户ID' }
          ,{ name: 'format', label: '格式(1:文字，2:图片)' }
          ,{ name: 'isread', label: '消息已读(0:未读，1:已读)' }
    ],
    textQueries: [
          { field: 'uid', label: '用户ID', fuzzy: false }
          ,{ field: 'fid', label: '好友用户ID', fuzzy: false }
          ,{ field: 'tablename', label: '关联表名', fuzzy: false }
    ],
    selectFilters: [
    ],
    rangeFilters: [
    ],
    category: null,
    stats: {
      like: false,
      likeField: null,
      favorite: false,
      favoriteField: null,
      click: false,
      clickField: null
    },
    hasDiscuss: false,
    hasPay: false
  },
  'friend': {
    label: '好友表',
    imageField: 'picture',
    titleField: 'name',
    summaryFields: [
          { name: 'uid', label: '用户ID' }
          ,{ name: 'fid', label: '好友用户ID' }
          ,{ name: 'role', label: '角色标识' }
          ,{ name: 'tablename', label: '表名' }
          ,{ name: 'alias', label: '好友备注' }
          ,{ name: 'type', label: '关系类型' }
    ],
    textQueries: [
          { field: 'name', label: '好友昵称', fuzzy: true }
          ,{ field: 'alias', label: '好友备注', fuzzy: true }
    ],
    selectFilters: [
    ],
    rangeFilters: [
    ],
    category: null,
    stats: {
      like: false,
      likeField: null,
      favorite: false,
      favoriteField: null,
      click: false,
      clickField: null
    },
    hasDiscuss: false,
    hasPay: false
  }
}

export function useTableMeta(tableName: string): TableFrontMeta | undefined {
  return frontMeta[tableName]
}

export function resolveCoverUrl(item: Record<string, any>, meta: TableFrontMeta | undefined, baseUrl: string): string {
  if (!meta?.imageField) return ''
  const raw = item[meta.imageField]
  if (!raw) return ''
  const first = String(raw).split(',')[0]
  return /^https?:\/\//.test(first) ? first : baseUrl + first
}
