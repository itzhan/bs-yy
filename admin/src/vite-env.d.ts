/// <reference types="vite/client" />

declare module 'vue-json-excel' {
  import type { DefineComponent } from 'vue'
  const JsonExcel: DefineComponent<Record<string, unknown>, Record<string, unknown>, any>
  export default JsonExcel
}
