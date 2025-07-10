<template>
  <div style="max-width: 900px; margin: 40px auto;">
    <h2>AI 打字机流式演示</h2>
    <div style="margin-bottom: 12px;">
      <input v-model="userInput" placeholder="请输入内容" style="width: 60%; padding: 6px;" :disabled="isStreaming" />
    </div>
    <div style="margin-bottom: 12px;">
      <input v-model="conversationId" placeholder="会话ID" style="width: 60%; padding: 6px;" :disabled="isStreaming" />
    </div>
    <button @click="startStream" :disabled="isStreaming" style="padding: 6px 18px;">
      {{ isStreaming ? '对话中...' : '开始对话' }}
    </button>
    <div class="output markdown-body" v-html="renderedOutput"></div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import MarkdownIt from 'markdown-it'

const userInput = ref('')
const conversationId = ref('')
const output = ref('')
const isStreaming = ref(false)
let eventSource = null

const md = new MarkdownIt()
// 自动识别图片链接并转为markdown图片语法
const imageUrlPattern = /(https?:\/\/[^\s]+?\.(?:png|jpg|jpeg|gif|webp))/gi
const renderedOutput = computed(() => {
  const withImages = output.value.replace(imageUrlPattern, '![]($1)')
  return md.render(withImages)
})

function startStream() {
  output.value = ''
  isStreaming.value = true
  if (eventSource) {
    eventSource.close()
  }
  const params = new URLSearchParams({
    userInput: userInput.value,
    conversationId: conversationId.value
  })
  eventSource = new EventSource(`/ai/stream?${params.toString()}`)
  eventSource.onmessage = (event) => {
    output.value += event.data
  }
  eventSource.onerror = () => {
    isStreaming.value = false
    eventSource.close()
  }
  setTimeout(() => {
    isStreaming.value = false
    if (eventSource) eventSource.close()
  }, 180000)
}
</script>

<style>
.output.markdown-body {
  margin-top: 24px;
  min-height: 120px;
  border: 1px solid #eee;
  background: #fafbfc;
  padding: 24px;
  font-size: 1.25em;
  line-height: 1.8;
}
.markdown-body h1, .markdown-body h2, .markdown-body h3 {
  font-weight: bold;
  margin: 1em 0 0.5em 0;
}
.markdown-body ul, .markdown-body ol {
  margin-left: 1.5em;
}
.markdown-body code {
  background: #f4f4f4;
  padding: 2px 4px;
  border-radius: 3px;
  font-family: 'Fira Mono', monospace;
}
.markdown-body pre {
  background: #f4f4f4;
  padding: 8px;
  border-radius: 4px;
  overflow-x: auto;
}
</style> 