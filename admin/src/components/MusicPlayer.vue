<script setup lang="ts">
import { computed, nextTick, onMounted, onUnmounted, ref, watch } from 'vue'

export interface MusicInfo {
  id?: string | number
  name?: string
  cover?: string
  artist?: string
  url: string
  lyrics?: string
}

interface LyricLine {
  time: number
  text: string
}

const props = defineProps<{
  music: MusicInfo | null
}>()

const isOpen = ref(false)
const isPlaying = ref(false)
const isFullscreen = ref(false)
const currentTime = ref(0)
const duration = ref(0)
const volume = ref(1)
const showVolumeSlider = ref(false)

const audioRef = ref<HTMLAudioElement | null>(null)
const playerRef = ref<HTMLDivElement | null>(null)

const lyrics = ref<LyricLine[]>([])
const currentLyricIndex = ref(0)

const formatTime = (time: number): string => {
  if (Number.isNaN(time)) return '00:00'
  const minutes = Math.floor(time / 60)
  const seconds = Math.floor(time % 60)
  return `${minutes.toString().padStart(2, '0')}:${seconds.toString().padStart(2, '0')}`
}

const progressPercent = computed(() => {
  if (!duration.value) return 0
  return (currentTime.value / duration.value) * 100
})

const parseLyrics = (lyricsText?: string | null): LyricLine[] => {
  if (!lyricsText) return []

  const result: LyricLine[] = []
  const timeRegex = /[\[\【\(\（\{](\d{1,2}):(\d{2})(?:\.(\d{1,3}))?[\]\】\)\）\}]/g
  let lines = lyricsText.split('\n')

  if (lines.length === 1) {
    const tempLines: string[] = []
    let lastIndex = 0
    let match
    while ((match = timeRegex.exec(lyricsText)) !== null) {
      if (match.index > lastIndex) {
        const prevText = lyricsText.substring(lastIndex, match.index).trim()
        if (prevText && tempLines.length > 0) {
          tempLines[tempLines.length - 1] += prevText
        }
      }
      tempLines.push(lyricsText.substring(match.index, match.index + match[0].length))
      lastIndex = match.index + match[0].length
    }

    if (lastIndex < lyricsText.length) {
      const lastText = lyricsText.substring(lastIndex).trim()
      if (lastText && tempLines.length > 0) {
        tempLines[tempLines.length - 1] += lastText
      }
    }

    if (tempLines.length > 1) {
      lines = tempLines
    }
  }

  lines.forEach(line => {
    timeRegex.lastIndex = 0
    const match = timeRegex.exec(line)
    if (match) {
      const minutes = parseInt(match[1])
      const seconds = parseInt(match[2])
      const milliseconds = match[3] ? parseInt(match[3].padEnd(3, '0')) : 0
      const time = minutes * 60 + seconds + milliseconds / 1000
      const text = line.substring(match.index + match[0].length).trim()
      if (text) {
        result.push({ time, text })
      }
    }
  })

  return result.sort((a, b) => a.time - b.time)
}

const updateCurrentLyric = () => {
  if (!lyrics.value.length) return
  for (let i = lyrics.value.length - 1; i >= 0; i--) {
    if (currentTime.value >= lyrics.value[i].time) {
      currentLyricIndex.value = i
      break
    }
  }
}

const openPlayer = () => {
  isOpen.value = true
  nextTick(() => {
    if (!isPlaying.value) {
      togglePlay()
    }
  })
}

const closePlayer = () => {
  isOpen.value = false
  if (isPlaying.value) {
    togglePlay()
  }
}

const togglePlay = () => {
  const audio = audioRef.value
  if (!audio) return
  if (isPlaying.value) {
    audio.pause()
  } else {
    audio.play().catch(err => {
      console.warn('播放失败', err)
    })
  }
  isPlaying.value = !isPlaying.value
}

const updateProgress = (e: MouseEvent) => {
  const audio = audioRef.value
  if (!audio) return
  const progressBar = e.currentTarget as HTMLElement
  const rect = progressBar.getBoundingClientRect()
  const percent = (e.clientX - rect.left) / rect.width
  audio.currentTime = percent * duration.value
}

const updateVolume = (e: Event) => {
  const target = e.target as HTMLInputElement
  volume.value = parseFloat(target.value)
  if (audioRef.value) {
    audioRef.value.volume = volume.value
  }
}

const toggleFullscreen = async () => {
  if (!playerRef.value) return
  try {
    if (!isFullscreen.value) {
      await playerRef.value.requestFullscreen?.()
    } else {
      await document.exitFullscreen?.()
    }
  } catch (error) {
    console.error('全屏切换失败:', error)
  }
}

const handleFullscreenChange = () => {
  isFullscreen.value = !!document.fullscreenElement
}

const handleTimeUpdate = () => {
  if (!audioRef.value) return
  currentTime.value = audioRef.value.currentTime
  updateCurrentLyric()
}

const handleLoadedMetadata = () => {
  if (!audioRef.value) return
  duration.value = audioRef.value.duration
}

const handleEnded = () => {
  isPlaying.value = false
  currentTime.value = 0
}

watch(
  () => props.music,
  music => {
    const lyricSource = music?.lyrics ?? ''
    lyrics.value = parseLyrics(lyricSource)
    currentLyricIndex.value = 0
    currentTime.value = 0
    isPlaying.value = false
    if (music) {
      nextTick(() => {
        if (audioRef.value) {
          audioRef.value.currentTime = 0
          audioRef.value.pause()
        }
        isOpen.value = false
        openPlayer()
      })
    }
  },
  { immediate: true }
)

onMounted(() => {
  document.addEventListener('fullscreenchange', handleFullscreenChange)
})

onUnmounted(() => {
  document.removeEventListener('fullscreenchange', handleFullscreenChange)
})

defineExpose({
  open: openPlayer,
  close: closePlayer
})
</script>

<template>
  <div class="music-player-wrapper" v-if="music">
    <div v-if="!isOpen" class="music-icon" @click="openPlayer">
      <svg viewBox="0 0 24 24" fill="currentColor">
        <path d="M12 3v10.55c-.59-.34-1.27-.55-2-.55-2.21 0-4 1.79-4 4s1.79 4 4 4 4-1.79 4-4V7h4V3h-6z" />
      </svg>
    </div>

    <Transition name="overlay">
      <div v-if="isOpen && !isFullscreen" class="overlay" @click="closePlayer"></div>
    </Transition>

    <Transition name="player">
      <div v-if="isOpen" ref="playerRef" class="music-player" :class="{ fullscreen: isFullscreen }">
        <button class="close-btn" @click="closePlayer">
          <svg viewBox="0 0 24 24" fill="currentColor">
            <path d="M19 6.41 17.59 5 12 10.59 6.41 5 5 6.41 10.59 12 5 17.59 6.41 19 12 13.41 17.59 19 19 17.59 13.41 12z" />
          </svg>
        </button>

        <button class="fullscreen-btn" @click="toggleFullscreen">
          <svg v-if="!isFullscreen" viewBox="0 0 24 24" fill="currentColor">
            <path d="M7 14H5v5h5v-2H7v-3zm-2-4h2V7h3V5H5v5zm12 7h-3v2h5v-5h-2v3zM14 5v2h3v3h2V5h-5z" />
          </svg>
          <svg v-else viewBox="0 0 24 24" fill="currentColor">
            <path d="M5 16h3v3h2v-5H5v2zm3-8H5v2h5V5H8v3zm6 11h2v-3h3v-2h-5v5zm2-11V5h-2v5h5V8h-3z" />
          </svg>
        </button>

        <div class="player-header">
          <div class="cover-wrapper">
            <img v-if="music.cover" :src="music.cover" :alt="music.name || '音乐封面'" class="cover" :class="{ rotating: isPlaying }" />
            <div v-else class="cover-placeholder" :class="{ rotating: isPlaying }">
              <svg viewBox="0 0 24 24" fill="currentColor">
                <path d="M12 3v10.55c-.59-.34-1.27-.55-2-.55-2.21 0-4 1.79-4 4s1.79 4 4 4 4-1.79 4-4V7h4V3h-6z" />
              </svg>
            </div>
          </div>
          <div class="music-info">
            <h3 class="music-name">{{ music.name || '未知歌曲' }}</h3>
            <p class="music-artist">{{ music.artist || '未知艺术家' }}</p>
          </div>
        </div>

        <div v-if="lyrics.length" class="lyrics-container">
          <div class="lyrics-scroll">
            <div v-for="(lyric, index) in lyrics" :key="index" class="lyric-line" :class="{ active: index === currentLyricIndex }">
              {{ lyric.text }}
            </div>
          </div>
        </div>
        <div v-else class="no-lyrics">暂无歌词</div>

        <div class="progress-section">
          <span class="time">{{ formatTime(currentTime) }}</span>
          <div class="progress-bar" @click="updateProgress">
            <div class="progress-bg">
              <div class="progress-fill" :style="{ width: progressPercent + '%' }">
                <div class="progress-thumb"></div>
              </div>
            </div>
          </div>
          <span class="time">{{ formatTime(duration) }}</span>
        </div>

        <div class="controls">
          <div class="volume-control" @mouseenter="showVolumeSlider = true" @mouseleave="showVolumeSlider = false">
            <button class="control-btn">
              <svg v-if="volume > 0.5" viewBox="0 0 24 24" fill="currentColor">
                <path d="M3 9v6h4l5 5V4L7 9H3zm13.5 3c0-1.77-1.02-3.29-2.5-4.03v8.05c1.48-.73 2.5-2.25 2.5-4.02zM14 3.23v2.06c2.89.86 5 3.54 5 6.71s-2.11 5.85-5 6.71v2.06c4.01-.91 7-4.49 7-8.77s-2.99-7.86-7-8.77z" />
              </svg>
              <svg v-else-if="volume > 0" viewBox="0 0 24 24" fill="currentColor">
                <path d="M7 9v6h4l5 5V4l-5 5H7z" />
              </svg>
              <svg v-else viewBox="0 0 24 24" fill="currentColor">
                <path d="M4.27 3 3 4.27 7.73 9H3v6h4l5 5v-6.73l4.25 4.25c-.67.52-1.42.93-2.25 1.18v2.06c1.38-.31 2.63-.95 3.69-1.81L19.73 21 21 19.73l-9-9L4.27 3zM12 4 9.91 6.09 12 8.18V4z" />
              </svg>
            </button>
            <Transition name="volume">
              <input v-show="showVolumeSlider" type="range" min="0" max="1" step="0.01" :value="volume" @input="updateVolume" class="volume-slider" />
            </Transition>
          </div>

          <button class="play-btn" @click="togglePlay">
            <svg v-if="!isPlaying" viewBox="0 0 24 24" fill="currentColor">
              <path d="M8 5v14l11-7z" />
            </svg>
            <svg v-else viewBox="0 0 24 24" fill="currentColor">
              <path d="M6 19h4V5H6v14zm8-14v14h4V5h-4z" />
            </svg>
          </button>

          <div class="placeholder"></div>
        </div>

        <audio
          ref="audioRef"
          :src="music.url"
          @timeupdate="handleTimeUpdate"
          @loadedmetadata="handleLoadedMetadata"
          @ended="handleEnded"
        ></audio>
      </div>
    </Transition>
  </div>
</template>

<style scoped>
.music-player-wrapper {
  position: fixed;
  right: 24px;
  bottom: 24px;
  z-index: 1000;
}

.music-icon {
  width: 44px;
  height: 44px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #fff;
  cursor: pointer;
  box-shadow: 0 8px 20px rgba(102, 126, 234, 0.35);
  transition: transform 0.2s ease, box-shadow 0.2s ease;
}

.music-icon:hover {
  transform: translateY(-2px);
  box-shadow: 0 12px 28px rgba(102, 126, 234, 0.45);
}

.overlay {
  position: fixed;
  inset: 0;
  background: rgba(0, 0, 0, 0.45);
  z-index: 999;
}

.music-player {
  position: fixed;
  right: 24px;
  bottom: 24px;
  width: 360px;
  max-width: calc(100vw - 40px);
  background: rgba(15, 18, 24, 0.9);
  backdrop-filter: blur(18px);
  border-radius: 16px;
  box-shadow: 0 24px 48px rgba(17, 18, 32, 0.35);
  color: #fff;
  padding: 20px;
  z-index: 1001;
  overflow: hidden;
}

.music-player.fullscreen {
  position: fixed;
  inset: 0;
  width: 100vw;
  max-width: none;
  height: 100vh;
  border-radius: 0;
  display: flex;
  flex-direction: column;
}

.close-btn,
.fullscreen-btn {
  position: absolute;
  top: 12px;
  background: transparent;
  border: none;
  color: #fff;
  cursor: pointer;
  padding: 6px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.close-btn {
  right: 12px;
}

.fullscreen-btn {
  right: 48px;
}

.player-header {
  display: flex;
  align-items: center;
  gap: 16px;
}

.cover-wrapper {
  width: 96px;
  height: 96px;
  border-radius: 16px;
  overflow: hidden;
  background: rgba(255, 255, 255, 0.08);
  display: flex;
  align-items: center;
  justify-content: center;
}

.cover {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.cover-placeholder {
  width: 72px;
  height: 72px;
  border-radius: 50%;
  background: rgba(255, 255, 255, 0.08);
  display: flex;
  align-items: center;
  justify-content: center;
}

.rotating {
  animation: spin 8s linear infinite;
}

@keyframes spin {
  from {
    transform: rotate(0deg);
  }
  to {
    transform: rotate(360deg);
  }
}

.music-info {
  flex: 1;
}

.music-name {
  font-size: 18px;
  margin: 0 0 4px;
}

.music-artist {
  margin: 0;
  opacity: 0.75;
}

.lyrics-container {
  margin-top: 18px;
  max-height: 160px;
  overflow: hidden;
  border-radius: 12px;
  background: rgba(255, 255, 255, 0.05);
  padding: 12px;
}

.lyrics-scroll {
  max-height: 136px;
  overflow-y: auto;
}

.lyric-line {
  padding: 4px 0;
  opacity: 0.65;
  transition: color 0.2s ease, opacity 0.2s ease;
}

.lyric-line.active {
  opacity: 1;
  color: #8ba6ff;
}

.no-lyrics {
  margin: 16px 0;
  text-align: center;
  opacity: 0.6;
}

.progress-section {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-top: 18px;
}

.progress-bar {
  flex: 1;
  cursor: pointer;
}

.progress-bg {
  width: 100%;
  height: 6px;
  border-radius: 999px;
  background: rgba(255, 255, 255, 0.1);
  overflow: hidden;
}

.progress-fill {
  height: 100%;
  background: linear-gradient(135deg, #8ba6ff 0%, #6c5ce7 100%);
  border-radius: 999px;
  position: relative;
}

.progress-thumb {
  position: absolute;
  right: 0;
  top: 50%;
  transform: translateY(-50%);
  width: 12px;
  height: 12px;
  border-radius: 50%;
  background: #fff;
}

.controls {
  margin-top: 18px;
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.volume-control {
  position: relative;
  display: flex;
  align-items: center;
}

.volume-slider {
  position: absolute;
  bottom: 36px;
  left: 50%;
  transform: translateX(-50%);
  width: 120px;
}

.control-btn,
.play-btn {
  width: 44px;
  height: 44px;
  border-radius: 50%;
  background: rgba(255, 255, 255, 0.08);
  border: none;
  color: #fff;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
}

.placeholder {
  width: 44px;
  height: 44px;
}

.time {
  font-size: 12px;
  opacity: 0.8;
}

.overlay-enter-active,
.overlay-leave-active,
.player-enter-active,
.player-leave-active,
.volume-enter-active,
.volume-leave-active {
  transition: all 0.2s ease;
}

.overlay-enter-from,
.overlay-leave-to {
  opacity: 0;
}

.player-enter-from,
.player-leave-to {
  opacity: 0;
  transform: translateY(12px);
}

.volume-enter-from,
.volume-leave-to {
  opacity: 0;
  transform: translate(-50%, 8px);
}

@media (max-width: 600px) {
  .music-player {
    right: 12px;
    left: 12px;
    width: auto;
    bottom: 12px;
  }

  .music-player-wrapper {
    right: 12px;
    bottom: 12px;
  }
}
</style>
