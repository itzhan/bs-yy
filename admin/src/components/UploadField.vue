<template>
  <div class="upload-field" :class="{ 'upload-field--image': isImage }">
    <div v-if="readonly" class="upload-readonly">
      <template v-if="fileValues.length">
        <div v-if="isImage" class="upload-readonly-images">
          <el-image
            v-for="(item, index) in fileValues"
            :key="`${item}-${index}`"
            :src="resolveUrl(item)"
            :preview-src-list="previewList"
            fit="cover"
            class="upload-readonly-image"
          />
        </div>
        <div v-else class="upload-readonly-files">
          <div
            v-for="(item, index) in fileValues"
            :key="`${item}-${index}`"
            class="upload-readonly-item"
          >
            <el-link
              :underline="false"
              type="primary"
              :href="resolveUrl(item)"
              target="_blank"
            >
              {{ displayName(item, index) }}
            </el-link>
          </div>
        </div>
      </template>
      <span v-else class="upload-empty">暂无文件</span>
    </div>
    <el-upload
      v-else
      :action="action"
      :headers="headers"
      :file-list="uploadFiles"
      :multiple="multiple"
      :limit="limitProp || undefined"
      :list-type="listType"
      :accept="acceptComputed || undefined"
      :disabled="props.disabled || props.readonly"
      :before-upload="handleBeforeUpload"
      :on-success="handleSuccess"
      :on-remove="handleRemove"
      :on-preview="handlePreview"
      name="file"
    >
      <template #trigger>
        <div v-if="listType === 'picture-card'" class="upload-picture-trigger" :class="{ 'is-disabled': uploadDisabled }">
          <el-icon>
            <Plus />
          </el-icon>
          <span>上传</span>
        </div>
        <el-button v-else type="primary" :disabled="uploadDisabled">选择文件</el-button>
      </template>
      <template v-if="tip" #tip>
        <div class="el-upload__tip">{{ tip }}</div>
      </template>
    </el-upload>
  </div>
</template>

<script setup lang="ts">
import { computed, ref, watch } from 'vue'
import { ElMessage, type UploadProps, type UploadFile, type UploadUserFile } from 'element-plus'
import { Plus } from '@element-plus/icons-vue'
import { useAuthStore } from '@/stores/auth'

type UploadMode = 'image' | 'file'

interface Props {
  modelValue?: string
  readonly?: boolean
  disabled?: boolean
  uploadType?: UploadMode
  limit?: number
  maxSize?: number
  tip?: string
  accept?: string
  separator?: string
}

const props = withDefaults(defineProps<Props>(), {
  modelValue: '',
  readonly: false,
  disabled: false,
  uploadType: 'file',
  limit: undefined,
  maxSize: undefined,
  tip: '',
  accept: '',
  separator: ','
})

const emit = defineEmits<{
  (e: 'update:modelValue', value: string): void
  (e: 'change', value: string[]): void
}>()

const baseUrl = import.meta.env.VITE_API_BASE_URL || ''
const authStore = useAuthStore()

const limitProp = computed(() => {
  if (props.limit !== undefined && props.limit !== null) {
    return props.limit
  }
  return props.uploadType === 'image' ? 1 : 0
})

const multiple = computed(() => {
  if (!limitProp.value || limitProp.value <= 0) return true
  return limitProp.value > 1
})

const isImage = computed(() => props.uploadType === 'image')
const listType = computed(() => (isImage.value ? 'picture-card' : 'text'))
const acceptComputed = computed(() => {
  if (props.accept) return props.accept
  return props.uploadType === 'image' ? 'image/*' : ''
})
const maxSizeBytes = computed(() => {
  const size = props.maxSize ?? (props.uploadType === 'image' ? 5 : 20)
  return size * 1024 * 1024
})

const separator = computed(() => props.separator || ',')

const parseValue = (value?: string | null): string[] =>
  (value || '')
    .split(separator.value)
    .map((item) => item.trim())
    .filter((item) => item.length > 0)

const fileValues = ref<string[]>(parseValue(props.modelValue))

watch(
  () => props.modelValue,
  (val) => {
    const next = parseValue(val)
    if (next.join(separator.value) !== fileValues.value.join(separator.value)) {
      fileValues.value = next
    }
  }
)

const uploadFiles = ref<(UploadUserFile & { rawPath: string })[]>([])

const resolveUrl = (path: string) => {
  if (!path) return ''
  if (/^https?:\/\//i.test(path)) return path
  if (!baseUrl) {
    return path.startsWith('/') ? path : `/${path}`
  }
  const prefix = baseUrl.endsWith('/') ? baseUrl.slice(0, -1) : baseUrl
  return `${prefix}${path.startsWith('/') ? '' : '/'}${path}`
}

const action = computed(() => {
  if (!baseUrl) return '/file/upload'
  return `${baseUrl}${baseUrl.endsWith('/') ? '' : '/'}file/upload`
})

const headers = computed(() => {
  const token = authStore.token
  if (!token) return {}
  return {
    Token: token,
    token
  }
})

const displayName = (path: string, index: number) => {
  const segments = path.split('/')
  const name = segments[segments.length - 1] || `文件${index + 1}`
  return decodeURIComponent(name)
}

const toUploadUserFile = (path: string, index: number): UploadUserFile & { rawPath: string } => ({
  name: displayName(path, index),
  url: resolveUrl(path),
  status: 'success',
  rawPath: path
})

const refreshUploadFiles = () => {
  uploadFiles.value = fileValues.value.map((path, index) => toUploadUserFile(path, index))
}

watch(fileValues, refreshUploadFiles, { immediate: true })

const uploadDisabled = computed(() => {
  if (props.disabled || props.readonly) return true
  // 使用 uploadFiles 而不是 fileValues,确保删除后能立即解除禁用
  if (limitProp.value && limitProp.value > 0 && uploadFiles.value.length >= limitProp.value) {
    return true
  }
  return false
})

const syncValue = (values: string[]) => {
  const filtered = values.filter((item) => item && item.length > 0)
  fileValues.value = filtered
  emit('update:modelValue', filtered.join(separator.value))
  emit('change', [...filtered])
}

const extractPaths = (response: any): string[] => {
  if (!response) return []
  const candidate =
    response.files ??
    response.file ??
    response.url ??
    response.data ??
    response.path ??
    response.payload
  if (!candidate) return []
  if (Array.isArray(candidate)) {
    return candidate.map((item: any) => String(item)).filter((item: string) => item.length > 0)
  }
  if (typeof candidate === 'string') {
    return candidate
      .split(/[,\s]+/)
      .map((item) => item.trim())
      .filter((item) => item.length > 0)
  }
  return [String(candidate)]
}

const handleBeforeUpload: UploadProps['beforeUpload'] = (rawFile) => {
  if (!rawFile) return false

  // 检查是否禁用
  if (uploadDisabled.value) {
    ElMessage.warning('已达到上传数量限制')
    return false
  }

  if (props.uploadType === 'image' && rawFile.type && !rawFile.type.startsWith('image/')) {
    ElMessage.error('只能上传图片文件')
    return false
  }
  if (maxSizeBytes.value > 0 && rawFile.size > maxSizeBytes.value) {
    const sizeLabel = props.maxSize ?? (props.uploadType === 'image' ? 5 : 20)
    ElMessage.error(`文件大小不能超过 ${sizeLabel}MB`)
    return false
  }
  return true
}

const handleSuccess: UploadProps['onSuccess'] = (response, uploadFile) => {
  if (response && typeof response.code !== 'undefined' && Number(response.code) !== 0) {
    ElMessage.error(response.msg || '上传失败')
    return
  }
  const paths = extractPaths(response)
  if (!paths.length) {
    ElMessage.error('上传结果解析失败')
    return
  }
  let next: string[] = []
  if (limitProp.value === 1) {
    next = [paths[paths.length - 1]]
  } else {
    next = [...fileValues.value]
    paths.forEach((path) => {
      if (!path) return
      if (limitProp.value && limitProp.value > 0 && next.length >= limitProp.value) {
        return
      }
      next.push(path)
    })
  }
  syncValue(next)
  refreshUploadFiles()
  ElMessage.success('上传成功')
}

const handleRemove: UploadProps['onRemove'] = (uploadFile: UploadFile) => {
  const rawPath = (uploadFile as UploadFile & { rawPath?: string }).rawPath
  if (!rawPath && !uploadFile.url) return
  const target = rawPath || uploadFile.url || ''
  const next = fileValues.value.filter((item) => {
    if (rawPath) {
      return item !== rawPath
    }
    return resolveUrl(item) !== target
  })
  syncValue(next)
  refreshUploadFiles()
}

const handlePreview: UploadProps['onPreview'] = (uploadFile) => {
  const url = resolveUrl((uploadFile as UploadFile & { rawPath?: string }).rawPath || uploadFile.url || '')
  if (!url) return
  const win = window.open(url, '_blank')
  if (win) {
    win.focus()
  }
}

const readonly = computed(() => props.readonly || props.disabled)
const previewList = computed(() => fileValues.value.map((item) => resolveUrl(item)))
</script>

<style scoped>
.upload-field {
  width: 100%;
}

.upload-readonly {
  display: flex;
  flex-direction: column;
  gap: 6px;
}

.upload-readonly-images {
  display: flex;
  flex-wrap: wrap;
  gap: 12px;
}

.upload-readonly-image {
  width: 96px;
  height: 96px;
  border-radius: 6px;
  object-fit: cover;
  overflow: hidden;
}

.upload-readonly-files {
  display: flex;
  flex-direction: column;
  gap: 6px;
}

.upload-readonly-item {
  display: flex;
  align-items: center;
}

.upload-empty {
  color: #999;
  font-size: 13px;
}

.upload-picture-trigger {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  width: 104px;
  height: 104px;
  color: #909399;
  cursor: pointer;
  transition: all 0.3s;
}

.upload-picture-trigger:hover {
  color: #409eff;
}

.upload-picture-trigger.is-disabled {
  opacity: 0.5;
  cursor: not-allowed;
  pointer-events: none;
}

.upload-picture-trigger span {
  font-size: 12px;
  margin-top: 4px;
}
</style>
