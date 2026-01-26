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
    >
    </el-upload>
    
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
import { ref, computed, watch } from 'vue'
import { QuillEditor } from '@vueup/vue-quill'
import { ElUpload, ElMessage } from 'element-plus'
import type { UploadInstance } from 'element-plus'
import '@vueup/vue-quill/dist/vue-quill.snow.css'

interface Props {
  modelValue?: string
  placeholder?: string
  height?: string
  disabled?: boolean
  maxSize?: number
}

interface Emits {
  (e: 'update:modelValue', value: string): void
}

const baseUrl = import.meta.env.VITE_API_BASE_URL || ''
const uploadUrl = baseUrl + '/file/upload'

const props = withDefaults(defineProps<Props>(), {
  modelValue: '',
  placeholder: '请输入内容...',
  height: '300px',
  disabled: false,
  maxSize: 4000
})

const emit = defineEmits<Emits>()

const uploadRef = ref<UploadInstance>()
const quillRef = ref()
const content = ref(props.modelValue)

const uploadHeaders = computed(() => ({
  'Authorization': localStorage.getItem('token') || ''
}))

const editorOptions = computed(() => ({
  modules: {
    toolbar: {
      container: [
        ['bold', 'italic', 'underline', 'strike'],
        ['blockquote', 'code-block'],
        [{ 'header': 1 }, { 'header': 2 }],
        [{ 'list': 'ordered' }, { 'list': 'bullet' }],
        [{ 'script': 'sub' }, { 'script': 'super' }],
        [{ 'indent': '-1' }, { 'indent': '+1' }],
        [{ 'direction': 'rtl' }],
        [{ 'size': ['small', false, 'large', 'huge'] }],
        [{ 'header': [1, 2, 3, 4, 5, 6, false] }],
        [{ 'color': [] }, { 'background': [] }],
        [{ 'font': [] }],
        [{ 'align': [] }],
        ['clean'],
        ['link', 'image', 'video']
      ],
      handlers: {
        image: () => {
          const input = uploadRef.value?.$el?.querySelector('input[type="file"]')
          if (input) {
            input.click()
          }
        }
      }
    }
  },
  placeholder: props.placeholder,
  readOnly: props.disabled
}))

watch(
  () => props.modelValue,
  (newVal) => {
    if (newVal !== content.value) {
      content.value = newVal
    }
  },
  { immediate: true }
)

watch(
  content,
  (newVal) => {
    emit('update:modelValue', newVal)
  }
)

const onEditorReady = (quill: any) => {
  console.log('Editor ready:', quill)
}

const beforeUpload = (file: File) => {
  const isValidType = file.type.startsWith('image/')
  const isValidSize = file.size / 1024 < props.maxSize

  if (!isValidType) {
    ElMessage.error('只能上传图片文件!')
    return false
  }
  if (!isValidSize) {
    ElMessage.error(`图片大小不能超过 ${props.maxSize}KB!`)
    return false
  }

  return true
}

const uploadSuccess = (response: any) => {
  if (response.code === 0 || response.success) {
    const quill = quillRef.value?.getQuill()
    if (quill) {
      const range = quill.getSelection()
      const index = range ? range.index : quill.getLength()
      
      let imageUrl = response.data || response.url || response.file
      // 如果返回的是相对路径，添加 baseUrl
      if (imageUrl && !imageUrl.startsWith('http') && !imageUrl.startsWith(baseUrl)) {
        imageUrl = baseUrl + imageUrl
      }
      
      quill.insertEmbed(index, 'image', imageUrl)
      quill.setSelection(index + 1)
    }
    ElMessage.success('图片上传成功!')
  } else {
    ElMessage.error(response.message || '图片上传失败!')
  }
}

const uploadError = () => {
  ElMessage.error('图片上传失败!')
}

defineExpose({
  getQuill: () => quillRef.value?.getQuill()
})
</script>

<style scoped>
.rich-editor {
  border: 1px solid #dcdfe6;
  border-radius: 4px;
}

.rich-editor :deep(.ql-editor) {
  min-height: v-bind(height);
}

.hidden-upload {
  display: none;
}

.rich-editor :deep(.ql-snow .ql-tooltip[data-mode="link"]::before) {
  content: "请输入链接地址:";
}

.rich-editor :deep(.ql-snow .ql-tooltip.ql-editing a.ql-action::after) {
  border-right: 0px;
  content: "保存";
  padding-right: 0px;
}

.rich-editor :deep(.ql-snow .ql-tooltip[data-mode="video"]::before) {
  content: "请输入视频地址:";
}

.rich-editor :deep(.ql-snow .ql-picker.ql-size .ql-picker-label::before),
.rich-editor :deep(.ql-snow .ql-picker.ql-size .ql-picker-item::before) {
  content: "14px";
}

.rich-editor :deep(.ql-snow .ql-picker.ql-size .ql-picker-label[data-value="small"]::before),
.rich-editor :deep(.ql-snow .ql-picker.ql-size .ql-picker-item[data-value="small"]::before) {
  content: "10px";
}

.rich-editor :deep(.ql-snow .ql-picker.ql-size .ql-picker-label[data-value="large"]::before),
.rich-editor :deep(.ql-snow .ql-picker.ql-size .ql-picker-item[data-value="large"]::before) {
  content: "18px";
}

.rich-editor :deep(.ql-snow .ql-picker.ql-size .ql-picker-label[data-value="huge"]::before),
.rich-editor :deep(.ql-snow .ql-picker.ql-size .ql-picker-item[data-value="huge"]::before) {
  content: "32px";
}

.rich-editor :deep(.ql-snow .ql-picker.ql-header .ql-picker-label::before),
.rich-editor :deep(.ql-snow .ql-picker.ql-header .ql-picker-item::before) {
  content: "文本";
}

.rich-editor :deep(.ql-snow .ql-picker.ql-header .ql-picker-label[data-value="1"]::before),
.rich-editor :deep(.ql-snow .ql-picker.ql-header .ql-picker-item[data-value="1"]::before) {
  content: "标题1";
}

.rich-editor :deep(.ql-snow .ql-picker.ql-header .ql-picker-label[data-value="2"]::before),
.rich-editor :deep(.ql-snow .ql-picker.ql-header .ql-picker-item[data-value="2"]::before) {
  content: "标题2";
}

.rich-editor :deep(.ql-snow .ql-picker.ql-header .ql-picker-label[data-value="3"]::before),
.rich-editor :deep(.ql-snow .ql-picker.ql-header .ql-picker-item[data-value="3"]::before) {
  content: "标题3";
}

.rich-editor :deep(.ql-snow .ql-picker.ql-header .ql-picker-label[data-value="4"]::before),
.rich-editor :deep(.ql-snow .ql-picker.ql-header .ql-picker-item[data-value="4"]::before) {
  content: "标题4";
}

.rich-editor :deep(.ql-snow .ql-picker.ql-header .ql-picker-label[data-value="5"]::before),
.rich-editor :deep(.ql-snow .ql-picker.ql-header .ql-picker-item[data-value="5"]::before) {
  content: "标题5";
}

.rich-editor :deep(.ql-snow .ql-picker.ql-header .ql-picker-label[data-value="6"]::before),
.rich-editor :deep(.ql-snow .ql-picker.ql-header .ql-picker-item[data-value="6"]::before) {
  content: "标题6";
}

.rich-editor :deep(.ql-snow .ql-picker.ql-font .ql-picker-label::before),
.rich-editor :deep(.ql-snow .ql-picker.ql-font .ql-picker-item::before) {
  content: "标准字体";
}

.rich-editor :deep(.ql-snow .ql-picker.ql-font .ql-picker-label[data-value="serif"]::before),
.rich-editor :deep(.ql-snow .ql-picker.ql-font .ql-picker-item[data-value="serif"]::before) {
  content: "衬线字体";
}

.rich-editor :deep(.ql-snow .ql-picker.ql-font .ql-picker-label[data-value="monospace"]::before),
.rich-editor :deep(.ql-snow .ql-picker.ql-font .ql-picker-item[data-value="monospace"]::before) {
  content: "等宽字体";
}
</style>