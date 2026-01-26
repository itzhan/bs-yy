<template>
  <div class="rich-editor">
    <el-upload
      ref="uploadRef"
      class="hidden-upload"
      :action="uploadUrl"
      :headers="uploadHeaders"
      :show-file-list="false"
      :before-upload="beforeUpload"
      :on-success="uploadSuccess"
      :on-error="uploadError"
      name="file"
    />

    <QuillEditor
      ref="quillRef"
      v-model:content="content"
      :options="editorOptions"
      content-type="html"
      theme="snow"
      @ready="onEditorReady"
    />
  </div>
</template>

<script setup lang="ts">
import { computed, ref, watch } from 'vue'
import { ElMessage, type UploadInstance } from 'element-plus'
import { QuillEditor } from '@vueup/vue-quill'
import '@vueup/vue-quill/dist/vue-quill.snow.css'

interface Props {
  modelValue?: string
  placeholder?: string
  height?: string
  disabled?: boolean
  maxSize?: number
}

const props = withDefaults(defineProps<Props>(), {
  modelValue: '',
  placeholder: '请输入内容...',
  height: '300px',
  disabled: false,
  maxSize: 4096
})

const emit = defineEmits<{ (e: 'update:modelValue', value: string): void }>()

const baseUrl = import.meta.env.VITE_API_BASE_URL || ''
const uploadUrl = baseUrl
  ? `${baseUrl}${baseUrl.endsWith('/') ? '' : '/'}file/upload`
  : '/file/upload'

const uploadRef = ref<UploadInstance>()
const quillRef = ref()
const content = ref(props.modelValue)
const editorHeight = computed(() => props.height || '300px')

const uploadHeaders = computed(() => ({
  Token: localStorage.getItem('frontToken') || ''
}))

const editorOptions = computed(() => ({
  modules: {
    toolbar: {
      container: [
        ['bold', 'italic', 'underline', 'strike'],
        ['blockquote', 'code-block'],
        [{ header: 1 }, { header: 2 }],
        [{ list: 'ordered' }, { list: 'bullet' }],
        [{ script: 'sub' }, { script: 'super' }],
        [{ indent: '-1' }, { indent: '+1' }],
        [{ direction: 'rtl' }],
        [{ size: ['small', false, 'large', 'huge'] }],
        [{ header: [1, 2, 3, 4, 5, 6, false] }],
        [{ color: [] }, { background: [] }],
        [{ font: [] }],
        [{ align: [] }],
        ['clean'],
        ['link', 'image']
      ],
      handlers: {
        image: () => {
          const input: HTMLInputElement | null = uploadRef.value?.$el?.querySelector('input[type="file"]') ?? null
          input?.click()
        }
      }
    }
  },
  placeholder: props.placeholder,
  readOnly: props.disabled
}))

watch(
  () => props.modelValue,
  (val) => {
    if (val !== content.value) {
      content.value = val
    }
  },
  { immediate: true }
)

watch(content, (val) => {
  emit('update:modelValue', val || '')
})

const onEditorReady = () => {}

const beforeUpload = (file: File) => {
  const isImage = file.type.startsWith('image/')
  const isLtMax = file.size / 1024 <= props.maxSize
  if (!isImage) {
    ElMessage.error('只能上传图片文件')
    return false
  }
  if (!isLtMax) {
    ElMessage.error(`图片大小不能超过 ${props.maxSize}KB`)
    return false
  }
  return true
}

const uploadSuccess = (response: any) => {
  if (!response) {
    ElMessage.error('上传失败')
    return
  }
  const quill = quillRef.value?.getQuill?.()
  if (!quill) return

  let imageUrl = response.data || response.url || response.file
  if (imageUrl && !/^https?:\/\//.test(imageUrl)) {
    imageUrl = baseUrl + (String(imageUrl).startsWith('/') ? imageUrl : `/${imageUrl}`)
  }
  const range = quill.getSelection(true)
  const index = range ? range.index : quill.getLength()
  quill.insertEmbed(index, 'image', imageUrl)
  quill.setSelection(index + 1)
  ElMessage.success('图片上传成功')
}

const uploadError = () => {
  ElMessage.error('图片上传失败')
}

const setContent = (val: string) => {
  content.value = val || ''
}

defineExpose({
  getQuill: () => quillRef.value?.getQuill?.(),
  setContent
})
</script>

<style scoped>
.rich-editor {
  border: 1px solid #dcdfe6;
  border-radius: 4px;
  overflow: hidden;
}

.hidden-upload {
  display: none;
}

.rich-editor :deep(.ql-container) {
  min-height: v-bind(editorHeight);
}

.rich-editor :deep(.ql-toolbar) {
  border-bottom: 1px solid #dcdfe6;
}

.rich-editor :deep(.ql-editor) {
  min-height: v-bind(editorHeight);
}

.rich-editor :deep(.ql-editor img) {
  max-width: 240px;
  max-height: 240px;
  height: auto;
  width: auto;
}
</style>
