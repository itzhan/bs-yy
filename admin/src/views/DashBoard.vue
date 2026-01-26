<template>
    <div class="dashboard bg-gray-50 min-h-screen">
      <div class="container mx-auto px-4 py-6">
        <!-- 页面标题 -->
        <div class="mb-6">
          <h1 class="text-2xl font-bold text-gray-900">健身房运营管理系统数据面板</h1>
          <p class="text-gray-600 mt-1">实时数据监控与统计分析</p>
        </div>

        <!-- 统计卡片区域 -->
        <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-4 mb-6">
          <div class="bg-white rounded-lg shadow-sm border border-gray-200 p-4 hover:shadow-md transition-shadow">
            <div class="flex items-center justify-between">
              <div class="flex items-center">
                <div class="p-2 rounded-lg bg-blue-100 w-10 h-10 flex items-center justify-center">
                  <el-icon class="text-blue-600" :size="20">
                    <User />
                  </el-icon>
                </div>
                <div class="ml-3">
                  <h3 class="text-sm font-medium text-gray-500">会员总数</h3>
                  <p class="text-lg font-semibold text-gray-900">
                    
                    {{ statsData[0] !== null ? formatNumber(statsData[0]) : '-' }}
                    
                  </p>
                </div>
              </div>
              <div v-if="statsLoading[0]" class="animate-spin">
                <el-icon><Loading /></el-icon>
              </div>
            </div>
          </div>
          <div class="bg-white rounded-lg shadow-sm border border-gray-200 p-4 hover:shadow-md transition-shadow">
            <div class="flex items-center justify-between">
              <div class="flex items-center">
                <div class="p-2 rounded-lg bg-purple-100 w-10 h-10 flex items-center justify-center">
                  <el-icon class="text-purple-600" :size="20">
                    <UserFilled />
                  </el-icon>
                </div>
                <div class="ml-3">
                  <h3 class="text-sm font-medium text-gray-500">教练总数</h3>
                  <p class="text-lg font-semibold text-gray-900">
                    
                    {{ statsData[1] !== null ? formatNumber(statsData[1]) : '-' }}
                    
                  </p>
                </div>
              </div>
              <div v-if="statsLoading[1]" class="animate-spin">
                <el-icon><Loading /></el-icon>
              </div>
            </div>
          </div>
          <div class="bg-white rounded-lg shadow-sm border border-gray-200 p-4 hover:shadow-md transition-shadow">
            <div class="flex items-center justify-between">
              <div class="flex items-center">
                <div class="p-2 rounded-lg bg-green-100 w-10 h-10 flex items-center justify-center">
                  <el-icon class="text-green-600" :size="20">
                    <Money />
                  </el-icon>
                </div>
                <div class="ml-3">
                  <h3 class="text-sm font-medium text-gray-500">商品销售总额</h3>
                  <p class="text-lg font-semibold text-gray-900">
                    ¥
                    {{ statsData[2] !== null ? formatNumber(statsData[2]) : '-' }}
                    
                  </p>
                </div>
              </div>
              <div v-if="statsLoading[2]" class="animate-spin">
                <el-icon><Loading /></el-icon>
              </div>
            </div>
          </div>
          <div class="bg-white rounded-lg shadow-sm border border-gray-200 p-4 hover:shadow-md transition-shadow">
            <div class="flex items-center justify-between">
              <div class="flex items-center">
                <div class="p-2 rounded-lg bg-orange-100 w-10 h-10 flex items-center justify-center">
                  <el-icon class="text-orange-600" :size="20">
                    <Document />
                  </el-icon>
                </div>
                <div class="ml-3">
                  <h3 class="text-sm font-medium text-gray-500">今日课程报名</h3>
                  <p class="text-lg font-semibold text-gray-900">
                    
                    {{ statsData[3] !== null ? formatNumber(statsData[3]) : '-' }}
                    
                  </p>
                </div>
              </div>
              <div v-if="statsLoading[3]" class="animate-spin">
                <el-icon><Loading /></el-icon>
              </div>
            </div>
          </div>
        </div>

        <!-- 快速统计区域 -->
        <div class="grid grid-cols-1 md:grid-cols-2 gap-4 mb-6">
          <div class="bg-white rounded-lg shadow-sm border border-gray-200 p-4">
            <h4 class="text-sm font-medium text-gray-900 mb-3">课程分类统计</h4>
            <div v-loading="quickStatsLoading[0]">
              <div class="space-y-2">
                <div
                  v-for="item in quickStatsData[0]"
                  :key="item.name || item.key"
                  class="flex justify-between items-center py-1"
                >
                  <span class="text-sm text-gray-600">{{ item.name || item.key }}</span>
                  <span class="text-sm font-medium text-gray-900">{{ item.value || item.count }}</span>
                </div>
              </div>
            </div>
          </div>
          <div class="bg-white rounded-lg shadow-sm border border-gray-200 p-4">
            <h4 class="text-sm font-medium text-gray-900 mb-3">商品分类统计</h4>
            <div v-loading="quickStatsLoading[1]">
              <div class="space-y-2">
                <div
                  v-for="item in quickStatsData[1]"
                  :key="item.name || item.key"
                  class="flex justify-between items-center py-1"
                >
                  <span class="text-sm text-gray-600">{{ item.name || item.key }}</span>
                  <span class="text-sm font-medium text-gray-900">{{ item.value || item.count }}</span>
                </div>
              </div>
            </div>
          </div>
        </div>

        <!-- 图表区域 -->
        <div class="grid grid-cols-12 gap-4">
          <div class="col-span-12 lg:col-span-6 bg-white rounded-lg shadow-sm border border-gray-200 p-4">
            <h4 class="text-lg font-medium text-gray-900 mb-4">课程分类报名统计</h4>
            <div
              :id="'chart-0'"
              v-loading="chartsLoading[0]"
              style="height: 300px;"
            ></div>
          </div>
          <div class="col-span-12 lg:col-span-6 bg-white rounded-lg shadow-sm border border-gray-200 p-4">
            <h4 class="text-lg font-medium text-gray-900 mb-4">商品销售趋势</h4>
            <div
              :id="'chart-1'"
              v-loading="chartsLoading[1]"
              style="height: 300px;"
            ></div>
          </div>
          <div class="col-span-12 lg:col-span-6 bg-white rounded-lg shadow-sm border border-gray-200 p-4">
            <h4 class="text-lg font-medium text-gray-900 mb-4">会员卡套餐办卡统计</h4>
            <div
              :id="'chart-2'"
              v-loading="chartsLoading[2]"
              style="height: 300px;"
            ></div>
          </div>
          <div class="col-span-12 lg:col-span-6 bg-white rounded-lg shadow-sm border border-gray-200 p-4">
            <h4 class="text-lg font-medium text-gray-900 mb-4">商品分类销售占比</h4>
            <div
              :id="'chart-3'"
              v-loading="chartsLoading[3]"
              style="height: 300px;"
            ></div>
          </div>
        </div>
      </div>
    </div>
  </template>

  <script setup lang="ts">
  import { ref, onMounted, onUnmounted, nextTick } from 'vue'
  import * as echarts from 'echarts'
  import { http } from '@/utils/request'

  // 响应式数据
  const statsData = ref<(number | null)[]>(new Array(4).fill(null))
  const statsLoading = ref<boolean[]>(new Array(4).fill(false))

  const quickStatsData = ref<any[][]>(new Array(2).fill([]))
  const quickStatsLoading = ref<boolean[]>(new Array(2).fill(false))

  const chartsLoading = ref<boolean[]>(new Array(4).fill(false))
  const charts = ref<echarts.ECharts[]>([])
  let refreshTimer: number | null = null

  // 配置数据（使用 enricher 预处理后的视图模型，避免模板内复杂判断与 URL 拼接）
  const dashboardConfig = {
    statsCards: [
      {
        index: 0,
        title: '会员总数',
        url: '/count/user/id'
        
        
      },
      {
        index: 1,
        title: '教练总数',
        url: '/count/coach/id'
        
        
      },
      {
        index: 2,
        title: '商品销售总额',
        url: '/cal/productorder/totalprice'
        , prefix: '¥'
        
      },
      {
        index: 3,
        title: '今日课程报名',
        url: '/count/courseenrollment/id?conditionColumn=addtime&conditionValue=today'
        
        
      }
    ],
    charts: [
      {
        index: 0,
        title: '课程分类报名统计',
        type: 'pie',
        span: 6,
        url: '/group/courseenrollment/coursetype',
        keyField: 'coursetype'
      },
      {
        index: 1,
        title: '商品销售趋势',
        type: 'line',
        span: 6,
        url: '/value/productorder/addtime/totalprice/day',
        keyField: 'addtime'
      },
      {
        index: 2,
        title: '会员卡套餐办卡统计',
        type: 'bar',
        span: 6,
        url: '/group/cardapplication/packagetype',
        keyField: 'packagetype'
      },
      {
        index: 3,
        title: '商品分类销售占比',
        type: 'pie',
        span: 6,
        url: '/group/productorder/producttype',
        keyField: 'producttype'
      }
    ],
    quickStats: [
      {
        index: 0,
        title: '课程分类统计',
        displayType: 'list',
        url: '/group/course/coursetype',
        keyField: 'coursetype'
        , limit: 10
      },
      {
        index: 1,
        title: '商品分类统计',
        displayType: 'list',
        url: '/group/product/producttype',
        keyField: 'producttype'
        , limit: 10
      }
    ]
  }

  // 数据转换函数
  const transformGroupData = (data: any[], keyField: string): any[] => {
    if (!Array.isArray(data)) return []
    
    return data.map(item => ({
      name: item[keyField]?.toString() || '未知',
      value: item.total || item.count || 0,
      key: item[keyField]?.toString() || '未知',
      count: item.total || item.count || 0
    }))
  }

  // 工具函数
  const formatNumber = (num: number | string): string => {
    if (typeof num === 'string') {
      const parsed = parseFloat(num)
      return isNaN(parsed) ? num : parsed.toLocaleString()
    }
    return num.toLocaleString()
  }

  // API调用函数
  const fetchData = async <T = any>(url: string): Promise<T | null> => {
    try {
      console.log('调用接口:', url)
       const response = await http.get(url)
      console.log('接口返回:', response)
      
      if (response.code === 0) {
        return response.data !== undefined ? response.data : (response.count !== undefined ? response.count : response)
      }
      return null
    } catch (error) {
      console.error('API调用失败:', error)
      return null
    }
  }

  // 加载统计卡片数据
  const loadStatsCards = async () => {
    const promises = dashboardConfig.statsCards.map(async (card, index) => {
      if (statsLoading.value[index]) return
      
      statsLoading.value[index] = true
      
      const url = card.url
      
      const data = await fetchData<any>(url)
      
      let displayValue: number | null = null
      if (data !== null) {
        if (typeof data === 'number') {
          displayValue = data
        } else if (typeof data === 'string') {
          displayValue = parseFloat(data)
        } else if (typeof data === 'object') {
          if (data.sum !== undefined) {
            displayValue = typeof data.sum === 'string' ? parseFloat(data.sum) : data.sum
          } else if (data.count !== undefined) {
            displayValue = data.count
          } else if (data.data !== undefined) {
            displayValue = typeof data.data === 'string' ? parseFloat(data.data) : data.data
          }
        }
      }
      
      statsData.value[index] = displayValue
      statsLoading.value[index] = false
    })
    
    await Promise.allSettled(promises)
  }

  // 加载快速统计数据
  const loadQuickStats = async () => {
    const promises = dashboardConfig.quickStats.map(async (quickStat, index) => {
      if (quickStatsLoading.value[index]) return
      
      quickStatsLoading.value[index] = true
      
      const url = quickStat.url
      const result = await fetchData<any>(url)
      
      if (result && Array.isArray(result)) {
        const transformedData = transformGroupData(result, quickStat.keyField)
        quickStatsData.value[index] = quickStat.limit ? transformedData.slice(0, quickStat.limit) : transformedData
      } else {
        quickStatsData.value[index] = []
      }
      
      quickStatsLoading.value[index] = false
    })
    
    await Promise.allSettled(promises)
  }

  // 初始化图表
  const initCharts = async () => {
    await nextTick()
    
    const promises = dashboardConfig.charts.map(async (chartConfig, index) => {
      if (chartsLoading.value[index]) return
      
      chartsLoading.value[index] = true
      
      const url = chartConfig.url
      
      const result = await fetchData<any>(url)
      
      let data: any[] = []
      if (result && Array.isArray(result)) {
        data = transformGroupData(result, chartConfig.keyField)
      }
      
      const chartDom = document.getElementById(`chart-${index}`)
      if (!chartDom) {
        chartsLoading.value[index] = false
        return
      }
      
      const chart = echarts.init(chartDom)
      charts.value[index] = chart
      
      let option: echarts.EChartsOption = {}
      
      if (chartConfig.type === 'pie') {
        option = {
          tooltip: { 
            trigger: 'item',
            formatter: '{b}: {c} ({d}%)'
          },
          legend: {
            orient: 'vertical',
            left: 'left'
          },
          series: [{
            type: 'pie',
            radius: ['40%', '70%'],
            center: ['60%', '50%'],
            data: data.map(item => ({
              name: item.name,
              value: item.value
            }))
          }]
        }
      } else if (chartConfig.type === 'line') {
        option = {
          tooltip: { trigger: 'axis' },
          grid: {
            left: '3%',
            right: '4%',
            bottom: '3%',
            containLabel: true
          },
          xAxis: {
            type: 'category',
            boundaryGap: false,
            data: data.map(item => item.name)
          },
          yAxis: { type: 'value' },
          series: [{
            type: 'line',
            smooth: true,
            areaStyle: {
              opacity: 0.3
            },
            itemStyle: {
              color: '#409eff'
            },
            data: data.map(item => item.value)
          }]
        }
      } else if (chartConfig.type === 'bar') {
        option = {
          tooltip: { trigger: 'axis' },
          grid: {
            left: '3%',
            right: '4%',
            bottom: '3%',
            containLabel: true
          },
          xAxis: {
            type: 'category',
            data: data.map(item => item.name)
          },
          yAxis: { type: 'value' },
          series: [{
            type: 'bar',
            itemStyle: {
              color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
                { offset: 0, color: '#83bff6' },
                { offset: 0.5, color: '#188df0' },
                { offset: 1, color: '#188df0' }
              ])
            },
            data: data.map(item => item.value)
          }]
        }
      }
      
      chart.setOption(option)
      chartsLoading.value[index] = false
    })
    
    await Promise.allSettled(promises)
  }

  // 响应式处理
  const handleResize = () => {
    charts.value.forEach(chart => {
      if (chart && !chart.isDisposed()) {
        chart.resize()
      }
    })
  }

  // 刷新所有数据
  const refreshAllData = async () => {
    await Promise.all([
      loadStatsCards(),
      loadQuickStats()
    ])
  }

  // 生命周期
  onMounted(async () => {
    // 初始加载
    await Promise.all([
      loadStatsCards(),
      loadQuickStats(),
      initCharts()
    ])
    
    // 设置定时刷新
    refreshTimer = window.setInterval(refreshAllData, 5 * 60 * 1000) // 5分钟
    
    // 监听窗口大小变化
    window.addEventListener('resize', handleResize)
  })

  onUnmounted(() => {
    // 清理定时器
    if (refreshTimer) {
      clearInterval(refreshTimer)
    }
    
    // 销毁图表
    charts.value.forEach(chart => {
      if (chart && !chart.isDisposed()) {
        chart.dispose()
      }
    })
    
    // 移除事件监听
    window.removeEventListener('resize', handleResize)
  })
  </script>

  <style scoped>
  .dashboard {
    /* TailwindCSS 提供样式 */
  }

  /* 自定义加载动画 */
  .animate-spin {
    animation: spin 1s linear infinite;
  }

  @keyframes spin {
    from {
      transform: rotate(0deg);
    }
    to {
      transform: rotate(360deg);
    }
  }
  </style>
