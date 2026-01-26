/**
 * 验证工具类
 */

/**
 * 邮箱验证
 */
export function isEmail(email: string): boolean {
  return /^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+((.[a-zA-Z0-9_-]{2,3}){1,2})$/.test(email)
}

/**
 * 手机号码验证
 */
export function isMobile(mobile: string): boolean {
  return /^1[3456789]\d{9}$/.test(mobile)
}

/**
 * 电话号码验证
 */
export function isPhone(phone: string): boolean {
  return /^([0-9]{3,4}-)?[0-9]{7,8}$/.test(phone)
}

/**
 * URL地址验证
 */
export function isURL(url: string): boolean {
  return /^http[s]?:\/\/.*/.test(url)
}

/**
 * 数字验证（可以是小数，不可以是负数，可以为空）
 */
export function isNumber(value: string): boolean {
  return /(^-?[+-]?([0-9]*\.?[0-9]+|[0-9]+\.?[0-9]*)([eE][+-]?[0-9]+)?$)|(^$)/.test(value)
}

/**
 * 整数验证（可以为空）
 */
export function isIntNumber(value: string): boolean {
  return /(^-?\d+$)|(^$)/.test(value)
}

/**
 * 身份证验证
 */
export function checkIdCard(idcard: string): boolean {
  const regIdCard = /(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/
  return regIdCard.test(idcard)
}

/**
 * 密码强度验证
 * @param password 密码
 * @returns 0-弱，1-中，2-强
 */
export function checkPasswordStrength(password: string): number {
  let level = 0
  if (password.length >= 6) level++
  if (/[a-z]/.test(password) && /[A-Z]/.test(password)) level++
  if (/\d/.test(password)) level++
  if (/[^\w\s]/.test(password)) level++
  return Math.min(level, 2)
}

/**
 * 非空验证
 */
export function isNotEmpty(value: any): boolean {
  return value !== null && value !== undefined && value !== ''
}

/**
 * Element Plus 数字验证器
 */
export function validateNumber(_rule: any, value: string, callback: Function) {
  if (!value) {
    callback()
  } else if (!/^\d+(\.\d+)?$/.test(value)) {
    callback(new Error('请输入数字'))
  } else {
    callback()
  }
}

/**
 * Element Plus 手机号验证器
 */
export function validateMobile(_rule: any, value: string, callback: Function) {
  if (!value) {
    callback()
  } else if (!/^1[3-9]\d{9}$/.test(value)) {
    callback(new Error('请输入正确的手机号码'))
  } else {
    callback()
  }
}

/**
 * Element Plus 邮箱验证器
 */
export function validateEmail(_rule: any, value: string, callback: Function) {
  if (!value) {
    callback()
  } else if (!isEmail(value)) {
    callback(new Error('请输入正确的邮箱地址'))
  } else {
    callback()
  }
}

/**
 * Element Plus 身份证验证器
 */
export function validateIdCard(_rule: any, value: string, callback: Function) {
  if (!value) {
    callback()
  } else if (!checkIdCard(value)) {
    callback(new Error('请输入正确的身份证号码'))
  } else {
    callback()
  }
}

/**
 * Element Plus 整数验证器
 */
export function validateIntNumber(_rule: any, value: string, callback: Function) {
  if (!value) {
    callback()
  } else if (!isIntNumber(value)) {
    callback(new Error('请输入整数'))
  } else {
    callback()
  }
}