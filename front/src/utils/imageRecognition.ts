import { http } from '@/utils/request'

export interface ImageRecognitionPayload {
  keyword: string
  score?: number
  raw?: Record<string, any>
}

export const recognizeImage = async (imagePath: string): Promise<ImageRecognitionPayload> => {
  const trimmed = (imagePath || '').trim()
  if (!trimmed) {
    throw new Error('图片路径不能为空')
  }
  const res: any = await http.post('/image/recognize', { imagePath: trimmed })
  const data = res?.data ?? res
  return {
    keyword: (data?.keyword || '').toString(),
    score: typeof data?.score === 'number' ? data.score : undefined,
    raw: (data?.raw as Record<string, any>) || {}
  }
}
