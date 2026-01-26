
export const menuConfig = [

{
    "roleName":"用户",
    "tableName":"user",
    "hasBackLogin":"否",
    "hasBackRegister":"否",
    "hasFrontLogin":"是",
    "hasFrontRegister":"是",
    "usernameField":"useraccount",
    "passwordField":"userpassword",
    "frontMenu":[
                
        {
            "menu": "健身课程",
            "child": [
{
    "menu": "健身课程",
    "tableName": "course",
    "hidden": false,
    "cateRefTable": "coursetype",
    "cateRefColumn": "coursetypename",
    "allButtons": [
        "查看","新增" ,"审核" ,"收藏" ,"评论" ,"私聊" ,"报名"    ],
    "buttons": [
        "查看" ,"新增" ,"收藏" ,"评论" ,"私聊" ,"报名"    ]
}
            ]
        }
                ,
        {
            "menu": "课程报名记录",
            "child": [
{
    "menu": "课程报名记录",
    "tableName": "courseenrollment",
    "hidden": true,
    "cateRefTable": "course",
    "cateRefColumn": "coursename",
    "allButtons": [
        "查看","新增" ,"私聊" ,"立即购买" ,"取消课程报名记录","退款","发货","确认收货"    ],
    "buttons": [
        "查看" ,"私聊" ,"立即购买" ,"取消课程报名记录" ,"退款" ,"确认收货"    ]
}
            ]
        }
                ,
        {
            "menu": "健身商品",
            "child": [
{
    "menu": "健身商品",
    "tableName": "product",
    "hidden": false,
    "cateRefTable": "producttype",
    "cateRefColumn": "producttypename",
    "allButtons": [
        "查看","新增" ,"审核" ,"收藏" ,"评论" ,"购买"    ],
    "buttons": [
        "查看" ,"新增" ,"收藏" ,"评论" ,"购买"    ]
}
            ]
        }
                ,
        {
            "menu": "商品订单",
            "child": [
{
    "menu": "商品订单",
    "tableName": "productorder",
    "hidden": true,
    "cateRefTable": "product",
    "cateRefColumn": "productname",
    "allButtons": [
        "查看","新增" ,"评论" ,"私聊" ,"立即购买" ,"取消商品订单","退款","发货","确认收货"    ],
    "buttons": [
        "查看" ,"评论" ,"私聊" ,"立即购买" ,"取消商品订单" ,"退款" ,"确认收货"    ]
}
            ]
        }
                ,
        {
            "menu": "会员卡套餐",
            "child": [
{
    "menu": "会员卡套餐",
    "tableName": "membershippackage",
    "hidden": false,
    "allButtons": [
        "查看","新增" ,"办卡","续卡"    ],
    "buttons": [
        "查看" ,"新增" ,"办卡" ,"续卡"    ]
}
            ]
        }
                ,
        {
            "menu": "办卡记录",
            "child": [
{
    "menu": "办卡记录",
    "tableName": "cardapplication",
    "hidden": true,
    "cateRefTable": "membershippackage",
    "cateRefColumn": "packagename",
    "allButtons": [
        "查看","新增" ,"私聊" ,"立即购买" ,"取消办卡记录","退款","发货","确认收货"    ],
    "buttons": [
        "查看" ,"私聊" ,"立即购买" ,"取消办卡记录" ,"退款" ,"确认收货"    ]
}
            ]
        }
                ,
        {
            "menu": "续卡记录",
            "child": [
{
    "menu": "续卡记录",
    "tableName": "cardrenewal",
    "hidden": true,
    "cateRefTable": "membershippackage",
    "cateRefColumn": "packagename",
    "allButtons": [
        "查看","新增" ,"私聊" ,"立即购买" ,"取消续卡记录","退款","发货","确认收货"    ],
    "buttons": [
        "查看" ,"私聊" ,"立即购买" ,"取消续卡记录" ,"退款" ,"确认收货"    ]
}
            ]
        }
                ,
        {
            "menu": "会员交流",
            "child": [
{
    "menu": "会员交流",
    "tableName": "share",
    "hidden": false,
    "cateRefTable": "sharetype",
    "cateRefColumn": "sharetypename",
    "allButtons": [
        "查看","新增" ,"审核" ,"收藏" ,"评论"    ],
    "buttons": [
        "查看" ,"新增" ,"收藏" ,"评论"    ]
}
            ]
        }
                ,
        {
            "menu": "意见反馈",
            "child": [
{
    "menu": "意见反馈",
    "tableName": "feedback",
    "hidden": true,
    "allButtons": [
        "查看","新增" ,"审核" ,"私聊"    ],
    "buttons": [
        "查看" ,"新增" ,"私聊"    ]
}
            ]
        }
                ,
        {
            "menu": "取消课程报名记录",
            "child": [
{
    "menu": "取消课程报名记录",
    "tableName": "cancelcourseenrollment",
    "hidden": true,
    "cateRefTable": "course",
    "cateRefColumn": "coursename",
    "allButtons": [
        "查看","新增" ,"私聊"    ],
    "buttons": [
        "查看" ,"新增" ,"私聊"    ]
}
            ]
        }
                ,
        {
            "menu": "课程报名记录退款",
            "child": [
{
    "menu": "课程报名记录退款",
    "tableName": "refundcourseenrollment",
    "hidden": true,
    "cateRefTable": "course",
    "cateRefColumn": "coursename",
    "allButtons": [
        "查看","新增" ,"私聊"    ],
    "buttons": [
        "查看" ,"新增" ,"私聊"    ]
}
            ]
        }
                ,
        {
            "menu": "取消办卡记录",
            "child": [
{
    "menu": "取消办卡记录",
    "tableName": "cancelcardapplication",
    "hidden": true,
    "cateRefTable": "membershippackage",
    "cateRefColumn": "packagename",
    "allButtons": [
        "查看","新增" ,"私聊"    ],
    "buttons": [
        "查看" ,"新增" ,"私聊"    ]
}
            ]
        }
                ,
        {
            "menu": "办卡记录退款",
            "child": [
{
    "menu": "办卡记录退款",
    "tableName": "refundcardapplication",
    "hidden": true,
    "cateRefTable": "membershippackage",
    "cateRefColumn": "packagename",
    "allButtons": [
        "查看","新增" ,"私聊"    ],
    "buttons": [
        "查看" ,"新增" ,"私聊"    ]
}
            ]
        }
                ,
        {
            "menu": "取消商品订单",
            "child": [
{
    "menu": "取消商品订单",
    "tableName": "cancelproductorder",
    "hidden": true,
    "cateRefTable": "product",
    "cateRefColumn": "productname",
    "allButtons": [
        "查看","新增" ,"私聊"    ],
    "buttons": [
        "查看" ,"新增" ,"私聊"    ]
}
            ]
        }
                ,
        {
            "menu": "商品订单退款",
            "child": [
{
    "menu": "商品订单退款",
    "tableName": "refundproductorder",
    "hidden": true,
    "cateRefTable": "product",
    "cateRefColumn": "productname",
    "allButtons": [
        "查看","新增" ,"私聊"    ],
    "buttons": [
        "查看" ,"新增" ,"私聊"    ]
}
            ]
        }
                ,
        {
            "menu": "取消续卡记录",
            "child": [
{
    "menu": "取消续卡记录",
    "tableName": "cancelcardrenewal",
    "hidden": true,
    "cateRefTable": "membershippackage",
    "cateRefColumn": "packagename",
    "allButtons": [
        "查看","新增" ,"私聊"    ],
    "buttons": [
        "查看" ,"新增" ,"私聊"    ]
}
            ]
        }
                ,
        {
            "menu": "续卡记录退款",
            "child": [
{
    "menu": "续卡记录退款",
    "tableName": "refundcardrenewal",
    "hidden": true,
    "cateRefTable": "membershippackage",
    "cateRefColumn": "packagename",
    "allButtons": [
        "查看","新增" ,"私聊"    ],
    "buttons": [
        "查看" ,"新增" ,"私聊"    ]
}
            ]
        }
        ,
        {
            "menu": "公告资讯",
            "child": [
                {
                    "menu": "公告资讯",
                    "tableName": "news",
                    "allButtons": [
                        "查看","新增","收藏","评论"
                    ],
                    "buttons": [
                        "查看","收藏","评论"
                    ]
                }
            ]
        }
    ]
}
,
{
    "roleName":"教练",
    "tableName":"coach",
    "hasBackLogin":"是",
    "hasBackRegister":"是",
    "hasFrontLogin":"否",
    "hasFrontRegister":"否",
    "usernameField":"coachaccount",
    "passwordField":"coachpassword",

    "backMenu": [
                
                {
                    "menu": "用户管理",
                    "child": [
{
    "menu": "用户管理",
    "tableName": "user",
    "allButtons": [
        "新增","查看","修改","删除","导出" ,"私聊"    ],
    "buttons": [
        "新增","查看","修改","删除" ,"私聊"    ]
}
                    ]
                }
                ,
                {
                    "menu": "教练管理",
                    "child": [
{
    "menu": "教练管理",
    "tableName": "coach",
    "allButtons": [
        "新增","查看","修改","删除","导出" ,"私聊"    ],
    "buttons": [
        "新增","查看","修改","删除" ,"私聊"    ]
}
                    ]
                }
                ,
                {
                    "menu": "课程分类管理",
                    "child": [
{
    "menu": "课程分类管理",
    "tableName": "coursetype",
    "allButtons": [
        "新增","查看","修改","删除","导出"    ],
    "buttons": [
        "新增","查看","修改","删除"    ]
}
                    ]
                }
                ,
                {
                    "menu": "健身课程管理",
                    "child": [
{
    "menu": "健身课程管理",
    "tableName": "course",
    "allButtons": [
        "新增","查看","修改","删除","导出" ,"查看评论" ,"审核" ,"报名"    ],
    "buttons": [
        "新增","查看","修改","删除" ,"查看评论" ,"审核" ,"报名"    ]
}
                    ]
                }
                ,
                {
                    "menu": "课程报名记录管理",
                    "child": [
{
    "menu": "课程报名记录",
    "tableName": "courseenrollment",
    "allButtons": [
        "新增","查看","修改","删除","导出" ,"支付" ,"私聊" ,"取消课程报名记录","退款","发货","确认收货"    ],
    "buttons": [
        "新增","查看","修改","删除" ,"私聊" ,"取消课程报名记录" ,"发货"    ]
}
                        ,{
    "menu": "取消课程报名记录",
    "tableName": "cancelcourseenrollment",
    "allButtons": [
        "新增","查看","修改","删除","导出" ,"私聊"    ],
    "buttons": [
        "新增","查看","修改","删除" ,"私聊"    ]
}

                        ,{
    "menu": "课程报名记录退款",
    "tableName": "refundcourseenrollment",
    "allButtons": [
        "新增","查看","修改","删除","导出" ,"私聊"    ],
    "buttons": [
        "新增","查看","修改","删除" ,"私聊"    ]
}

                    ]
                }
                ,
                {
                    "menu": "商品分类管理",
                    "child": [
{
    "menu": "商品分类管理",
    "tableName": "producttype",
    "allButtons": [
        "新增","查看","修改","删除","导出"    ],
    "buttons": [
        "新增","查看","修改","删除"    ]
}
                    ]
                }
                ,
                {
                    "menu": "健身商品管理",
                    "child": [
{
    "menu": "健身商品管理",
    "tableName": "product",
    "allButtons": [
        "新增","查看","修改","删除","导出" ,"查看评论" ,"审核" ,"购买"    ],
    "buttons": [
        "新增","查看","修改","删除" ,"查看评论" ,"审核" ,"购买"    ]
}
                    ]
                }
                ,
                {
                    "menu": "商品订单管理",
                    "child": [
{
    "menu": "商品订单",
    "tableName": "productorder",
    "allButtons": [
        "新增","查看","修改","删除","导出" ,"查看评论" ,"支付" ,"私聊" ,"取消商品订单","退款","发货","确认收货"    ],
    "buttons": [
        "新增","查看","修改","删除" ,"查看评论" ,"私聊" ,"取消商品订单" ,"发货"    ]
}
                        ,{
    "menu": "取消商品订单",
    "tableName": "cancelproductorder",
    "allButtons": [
        "新增","查看","修改","删除","导出" ,"私聊"    ],
    "buttons": [
        "新增","查看","修改","删除" ,"私聊"    ]
}

                        ,{
    "menu": "商品订单退款",
    "tableName": "refundproductorder",
    "allButtons": [
        "新增","查看","修改","删除","导出" ,"私聊"    ],
    "buttons": [
        "新增","查看","修改","删除" ,"私聊"    ]
}

                    ]
                }
                ,
                {
                    "menu": "会员卡套餐管理",
                    "child": [
{
    "menu": "会员卡套餐管理",
    "tableName": "membershippackage",
    "allButtons": [
        "新增","查看","修改","删除","导出" ,"办卡","续卡"    ],
    "buttons": [
        "新增","查看","修改","删除" ,"办卡" ,"续卡"    ]
}
                    ]
                }
                ,
                {
                    "menu": "办卡记录管理",
                    "child": [
{
    "menu": "办卡记录",
    "tableName": "cardapplication",
    "allButtons": [
        "新增","查看","修改","删除","导出" ,"支付" ,"私聊" ,"取消办卡记录","退款","发货","确认收货"    ],
    "buttons": [
        "新增","查看","修改","删除" ,"私聊" ,"取消办卡记录" ,"发货"    ]
}
                        ,{
    "menu": "取消办卡记录",
    "tableName": "cancelcardapplication",
    "allButtons": [
        "新增","查看","修改","删除","导出" ,"私聊"    ],
    "buttons": [
        "新增","查看","修改","删除" ,"私聊"    ]
}

                        ,{
    "menu": "办卡记录退款",
    "tableName": "refundcardapplication",
    "allButtons": [
        "新增","查看","修改","删除","导出" ,"私聊"    ],
    "buttons": [
        "新增","查看","修改","删除" ,"私聊"    ]
}

                    ]
                }
                ,
                {
                    "menu": "续卡记录管理",
                    "child": [
{
    "menu": "续卡记录",
    "tableName": "cardrenewal",
    "allButtons": [
        "新增","查看","修改","删除","导出" ,"支付" ,"私聊" ,"取消续卡记录","退款","发货","确认收货"    ],
    "buttons": [
        "新增","查看","修改","删除" ,"私聊" ,"取消续卡记录" ,"发货"    ]
}
                        ,{
    "menu": "取消续卡记录",
    "tableName": "cancelcardrenewal",
    "allButtons": [
        "新增","查看","修改","删除","导出" ,"私聊"    ],
    "buttons": [
        "新增","查看","修改","删除" ,"私聊"    ]
}

                        ,{
    "menu": "续卡记录退款",
    "tableName": "refundcardrenewal",
    "allButtons": [
        "新增","查看","修改","删除","导出" ,"私聊"    ],
    "buttons": [
        "新增","查看","修改","删除" ,"私聊"    ]
}

                    ]
                }
                ,
                {
                    "menu": "交流分类管理",
                    "child": [
{
    "menu": "交流分类管理",
    "tableName": "sharetype",
    "allButtons": [
        "新增","查看","修改","删除","导出"    ],
    "buttons": [
        "新增","查看","修改","删除"    ]
}
                    ]
                }
                ,
                {
                    "menu": "会员交流管理",
                    "child": [
{
    "menu": "会员交流管理",
    "tableName": "share",
    "allButtons": [
        "新增","查看","修改","删除","导出" ,"查看评论" ,"审核"    ],
    "buttons": [
        "新增","查看","修改","删除" ,"查看评论" ,"审核"    ]
}
                    ]
                }
                ,
                {
                    "menu": "意见反馈管理",
                    "child": [
{
    "menu": "意见反馈管理",
    "tableName": "feedback",
    "allButtons": [
        "新增","查看","修改","删除","导出" ,"审核" ,"私聊"    ],
    "buttons": [
        "新增","查看","修改","删除" ,"审核" ,"私聊"    ]
}
                    ]
                }
    ],
}
,
{
    "roleName":"管理员",
    "tableName":"admin",
    "hasBackLogin":"是",
    "hasBackRegister":"否",
    "hasFrontLogin":"否",
    "hasFrontRegister":"否",
    "usernameField":"adminaccount",
    "passwordField":"adminpassword",

    "backMenu": [
                
                {
                    "menu": "用户管理",
                    "child": [
{
    "menu": "用户管理",
    "tableName": "user",
    "allButtons": [
        "新增","查看","修改","删除","导出" ,"私聊"    ],
    "buttons": [
        "新增","查看","修改","删除" ,"私聊"    ]
}
                    ]
                }
                ,
                {
                    "menu": "教练管理",
                    "child": [
{
    "menu": "教练管理",
    "tableName": "coach",
    "allButtons": [
        "新增","查看","修改","删除","导出" ,"私聊"    ],
    "buttons": [
        "新增","查看","修改","删除" ,"私聊"    ]
}
                    ]
                }
                ,
                {
                    "menu": "课程分类管理",
                    "child": [
{
    "menu": "课程分类管理",
    "tableName": "coursetype",
    "allButtons": [
        "新增","查看","修改","删除","导出"    ],
    "buttons": [
        "新增","查看","修改","删除"    ]
}
                    ]
                }
                ,
                {
                    "menu": "健身课程管理",
                    "child": [
{
    "menu": "健身课程管理",
    "tableName": "course",
    "allButtons": [
        "新增","查看","修改","删除","导出" ,"查看评论" ,"审核" ,"报名"    ],
    "buttons": [
        "新增","查看","修改","删除" ,"查看评论" ,"审核" ,"报名"    ]
}
                    ]
                }
                ,
                {
                    "menu": "课程报名记录管理",
                    "child": [
{
    "menu": "课程报名记录",
    "tableName": "courseenrollment",
    "allButtons": [
        "新增","查看","修改","删除","导出" ,"支付" ,"私聊" ,"取消课程报名记录","退款","发货","确认收货"    ],
    "buttons": [
        "新增","查看","修改","删除" ,"私聊" ,"取消课程报名记录" ,"发货"    ]
}
                        ,{
    "menu": "取消课程报名记录",
    "tableName": "cancelcourseenrollment",
    "allButtons": [
        "新增","查看","修改","删除","导出" ,"私聊"    ],
    "buttons": [
        "新增","查看","修改","删除" ,"私聊"    ]
}

                        ,{
    "menu": "课程报名记录退款",
    "tableName": "refundcourseenrollment",
    "allButtons": [
        "新增","查看","修改","删除","导出" ,"私聊"    ],
    "buttons": [
        "新增","查看","修改","删除" ,"私聊"    ]
}

                    ]
                }
                ,
                {
                    "menu": "商品分类管理",
                    "child": [
{
    "menu": "商品分类管理",
    "tableName": "producttype",
    "allButtons": [
        "新增","查看","修改","删除","导出"    ],
    "buttons": [
        "新增","查看","修改","删除"    ]
}
                    ]
                }
                ,
                {
                    "menu": "健身商品管理",
                    "child": [
{
    "menu": "健身商品管理",
    "tableName": "product",
    "allButtons": [
        "新增","查看","修改","删除","导出" ,"查看评论" ,"审核" ,"购买"    ],
    "buttons": [
        "新增","查看","修改","删除" ,"查看评论" ,"审核" ,"购买"    ]
}
                    ]
                }
                ,
                {
                    "menu": "商品订单管理",
                    "child": [
{
    "menu": "商品订单",
    "tableName": "productorder",
    "allButtons": [
        "新增","查看","修改","删除","导出" ,"查看评论" ,"支付" ,"私聊" ,"取消商品订单","退款","发货","确认收货"    ],
    "buttons": [
        "新增","查看","修改","删除" ,"查看评论" ,"私聊" ,"取消商品订单" ,"发货"    ]
}
                        ,{
    "menu": "取消商品订单",
    "tableName": "cancelproductorder",
    "allButtons": [
        "新增","查看","修改","删除","导出" ,"私聊"    ],
    "buttons": [
        "新增","查看","修改","删除" ,"私聊"    ]
}

                        ,{
    "menu": "商品订单退款",
    "tableName": "refundproductorder",
    "allButtons": [
        "新增","查看","修改","删除","导出" ,"私聊"    ],
    "buttons": [
        "新增","查看","修改","删除" ,"私聊"    ]
}

                    ]
                }
                ,
                {
                    "menu": "会员卡套餐管理",
                    "child": [
{
    "menu": "会员卡套餐管理",
    "tableName": "membershippackage",
    "allButtons": [
        "新增","查看","修改","删除","导出" ,"办卡","续卡"    ],
    "buttons": [
        "新增","查看","修改","删除" ,"办卡" ,"续卡"    ]
}
                    ]
                }
                ,
                {
                    "menu": "办卡记录管理",
                    "child": [
{
    "menu": "办卡记录",
    "tableName": "cardapplication",
    "allButtons": [
        "新增","查看","修改","删除","导出" ,"支付" ,"私聊" ,"取消办卡记录","退款","发货","确认收货"    ],
    "buttons": [
        "新增","查看","修改","删除" ,"私聊" ,"取消办卡记录" ,"发货"    ]
}
                        ,{
    "menu": "取消办卡记录",
    "tableName": "cancelcardapplication",
    "allButtons": [
        "新增","查看","修改","删除","导出" ,"私聊"    ],
    "buttons": [
        "新增","查看","修改","删除" ,"私聊"    ]
}

                        ,{
    "menu": "办卡记录退款",
    "tableName": "refundcardapplication",
    "allButtons": [
        "新增","查看","修改","删除","导出" ,"私聊"    ],
    "buttons": [
        "新增","查看","修改","删除" ,"私聊"    ]
}

                    ]
                }
                ,
                {
                    "menu": "续卡记录管理",
                    "child": [
{
    "menu": "续卡记录",
    "tableName": "cardrenewal",
    "allButtons": [
        "新增","查看","修改","删除","导出" ,"支付" ,"私聊" ,"取消续卡记录","退款","发货","确认收货"    ],
    "buttons": [
        "新增","查看","修改","删除" ,"私聊" ,"取消续卡记录" ,"发货"    ]
}
                        ,{
    "menu": "取消续卡记录",
    "tableName": "cancelcardrenewal",
    "allButtons": [
        "新增","查看","修改","删除","导出" ,"私聊"    ],
    "buttons": [
        "新增","查看","修改","删除" ,"私聊"    ]
}

                        ,{
    "menu": "续卡记录退款",
    "tableName": "refundcardrenewal",
    "allButtons": [
        "新增","查看","修改","删除","导出" ,"私聊"    ],
    "buttons": [
        "新增","查看","修改","删除" ,"私聊"    ]
}

                    ]
                }
                ,
                {
                    "menu": "交流分类管理",
                    "child": [
{
    "menu": "交流分类管理",
    "tableName": "sharetype",
    "allButtons": [
        "新增","查看","修改","删除","导出"    ],
    "buttons": [
        "新增","查看","修改","删除"    ]
}
                    ]
                }
                ,
                {
                    "menu": "会员交流管理",
                    "child": [
{
    "menu": "会员交流管理",
    "tableName": "share",
    "allButtons": [
        "新增","查看","修改","删除","导出" ,"查看评论" ,"审核"    ],
    "buttons": [
        "新增","查看","修改","删除" ,"查看评论" ,"审核"    ]
}
                    ]
                }
                ,
                {
                    "menu": "意见反馈管理",
                    "child": [
{
    "menu": "意见反馈管理",
    "tableName": "feedback",
    "allButtons": [
        "新增","查看","修改","删除","导出" ,"审核" ,"私聊"    ],
    "buttons": [
        "新增","查看","修改","删除" ,"审核" ,"私聊"    ]
}
                    ]
                }
    ,
        {
            "menu": "系统管理",
            "child": [
                {
                    "menu": "客服工作台",
                    "tableName": "chatworkbench",
                    "allButtons": [
                        "查看"
                    ],
                    "buttons": [
                        "查看"
                    ]
                },
                {
                    "menu": "消息通知管理",
                    "tableName": "notify",
                    "allButtons": [
                        "新增","查看","修改","删除","导出"
                    ],
                    "buttons": [
                        "新增","查看","修改","删除"
                    ]
                },
                {
                    "menu": "操作日志管理",
                    "tableName": "log",
                    "allButtons": [
                        "新增","查看","修改","删除","导出"
                    ],
                    "buttons": [
                        "新增","查看","修改","删除"
                    ]
                },
                {
                    "menu": "公告管理",
                    "tableName": "news",
                    "allButtons": [
                        "新增","查看","修改","删除","导出" ,"查看评论" ,"审核"                    ],
                    "buttons": [
                        "新增","查看","修改","删除" ,"查看评论" ,"审核"                    ]
                },
                {
                    "menu": "公告分类管理",
                    "tableName": "newstype",
                    "allButtons": [
                        "新增","查看","修改","删除","导出"                    ],
                    "buttons": [
                        "新增","查看","修改","删除"                    ]
                },
                {
                    "menu": "轮播图管理",
                    "tableName": "config",
                    "allButtons": [
                        "新增","查看","修改","删除","导出"
                    ],
                    "buttons": [
                        "新增","查看","修改","删除"                    ]
                }
            ]
        }
    ],
}
]
export default {
    list() {
        return menuConfig
    }
}
