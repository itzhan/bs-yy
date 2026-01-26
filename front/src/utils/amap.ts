declare global {
  interface Window {
    AMap?: any
    _AMapSecurityConfig?: {
      securityJsCode?: string
    }
  }
}

const AMAP_SCRIPT_ID = 'amap-sdk'
const DEFAULT_CENTER: [number, number] = [105.602725, 37.076636]
let loadPromise: Promise<any> | null = null

export const loadAmap = async (): Promise<any> => {
  if (typeof window === 'undefined') {
    throw new Error('当前环境不支持地图组件')
  }
  if (window.AMap) {
    return window.AMap
  }
  if (!loadPromise) {
    loadPromise = new Promise((resolve, reject) => {
      const key = (import.meta.env.VITE_AMAP_KEY || '').trim()
      if (!key) {
        console.warn('[AMap] 未配置 VITE_AMAP_KEY，地图可能无法正常加载')
      }
      const jsCode = (import.meta.env.VITE_AMAP_SECURITY_CODE || '').trim()
      if (jsCode) {
        window._AMapSecurityConfig = { securityJsCode: jsCode }
      }
      const existed = document.getElementById(AMAP_SCRIPT_ID) as HTMLScriptElement | null
      if (existed) {
        existed.addEventListener('load', () => resolve(window.AMap))
        existed.addEventListener('error', () => reject(new Error('加载高德地图失败')))
        return
      }
      const script = document.createElement('script')
      script.id = AMAP_SCRIPT_ID
      const queryKey = key ? key : 'undefined'
      script.src = `https://webapi.amap.com/maps?v=2.0&key=${queryKey}`
      script.async = true
      script.onload = () => resolve(window.AMap)
      script.onerror = () => reject(new Error('加载高德地图失败'))
      document.head.appendChild(script)
    })
  }
  return loadPromise
}

export const parseCoordinate = (value: unknown): number | null => {
  if (value === null || value === undefined) {
    return null
  }
  if (typeof value === 'number' && Number.isFinite(value)) {
    return value
  }
  const num = Number.parseFloat(String(value))
  if (Number.isFinite(num)) {
    return Number(num.toFixed(6))
  }
  return null
}

export const hasValidCoordinate = (lng: unknown, lat: unknown): boolean => {
  return parseCoordinate(lng) !== null && parseCoordinate(lat) !== null
}

export const getDefaultCenter = (): [number, number] => DEFAULT_CENTER
