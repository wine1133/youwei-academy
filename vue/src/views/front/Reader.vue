<template>
  <div class="reader-page">
    <aside class="reader-side">
      <router-link :to="`/front/book/${route.params.id}`" class="back-link"><el-icon><ArrowLeft /></el-icon> 返回图书</router-link>
      <div class="reader-book"><img :src="book.cover" alt=""><div><b>{{ book.name }}</b><span>{{ book.author }}</span></div></div>
      <div class="chapter-nav">
        <button v-for="(c, i) in chapters" :key="c.id" :class="{active: current.id === c.id}" @click="selectChapter(c)">
          <span>{{ String(i + 1).padStart(2, '0') }}</span>{{ c.title }}
        </button>
      </div>
    </aside>
    <main class="reader-main" v-if="current.id">
      <div class="reader-toolbar"><span>{{ book.name }}</span><div><button @click="fontSize = Math.max(15, fontSize - 1)">A-</button><button @click="fontSize = Math.min(24, fontSize + 1)">A+</button></div></div>
      <article class="reading-paper" :style="{fontSize: fontSize + 'px'}">
        <div class="chapter-kicker">CHAPTER {{ current.sortNo || 1 }}</div>
        <h1>{{ current.title }}</h1>
        <div class="reading-content" v-html="formatContent(current.content)"></div>
        <div class="reader-pager">
          <el-button :disabled="currentIndex <= 0" @click="selectChapter(chapters[currentIndex - 1])">上一章</el-button>
          <el-button type="primary" :disabled="currentIndex >= chapters.length - 1" @click="selectChapter(chapters[currentIndex + 1])">下一章</el-button>
        </div>
      </article>
    </main>
  </div>
</template>

<script setup>
import { reactive, computed, onMounted, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import request from '@/utils/request'

const route = useRoute()
const router = useRouter()
const book = reactive({})
const chapters = reactive([])
const current = reactive({})
const fontSize = ref(18)
const currentIndex = computed(() => chapters.findIndex(x => x.id === current.id))
const formatContent = (text) => (text || '本章内容正在整理中。').split(/\n+/).map(p => `<p>${p}</p>`).join('')
const selectChapter = (chapter) => { if (!chapter) return; Object.assign(current, chapter); router.replace({ path: `/front/reader/${route.params.id}`, query: { chapterId: chapter.id } }); window.scrollTo({top:0,behavior:'smooth'}) }
onMounted(() => {
  request.get(`/book/selectById/${route.params.id}`).then(res => { if (res.code === '200') Object.assign(book, res.data || {}) })
  request.get(`/chapter/selectByBookId/${route.params.id}`).then(res => {
    if (res.code === '200') {
      chapters.splice(0, chapters.length, ...(res.data || []))
      const target = chapters.find(x => String(x.id) === String(route.query.chapterId)) || chapters[0]
      if (target) Object.assign(current, target)
    }
  })
})
</script>

<style scoped>
.reader-page { min-height: 100vh; background: #ebe7df; display: grid; grid-template-columns: 290px 1fr; }
.reader-side { background: #102536; color: #fff; padding: 30px 22px; position: sticky; top: 0; height: 100vh; overflow: auto; }
.back-link { display: inline-flex; align-items: center; gap: 6px; color: rgba(255,255,255,.58); font-size: 12px; margin-bottom: 28px; }
.reader-book { display: flex; gap: 13px; align-items: center; padding-bottom: 24px; border-bottom: 1px solid rgba(255,255,255,.12); }
.reader-book img { width: 52px; height: 72px; object-fit: cover; border-radius: 7px; }
.reader-book div { display: flex; flex-direction: column; gap: 5px; }
.reader-book b { font-family: var(--font-serif); font-size: 17px; }
.reader-book span { color: rgba(255,255,255,.45); font-size: 11px; }
.chapter-nav { display: flex; flex-direction: column; margin-top: 20px; gap: 4px; }
.chapter-nav button { display: flex; gap: 12px; text-align: left; border: 0; background: transparent; color: rgba(255,255,255,.6); padding: 12px; border-radius: 10px; cursor: pointer; font-size: 12px; }
.chapter-nav button span { color: var(--gold); }
.chapter-nav button.active, .chapter-nav button:hover { background: rgba(255,255,255,.09); color: #fff; }
.reader-main { min-width: 0; }
.reader-toolbar { height: 56px; display: flex; align-items: center; justify-content: space-between; padding: 0 34px; background: rgba(255,255,255,.62); backdrop-filter: blur(12px); color: var(--muted); font-size: 12px; position: sticky; top: 0; z-index: 2; }
.reader-toolbar button { border: 1px solid #ddd7cc; background: #fff; border-radius: 8px; margin-left: 6px; padding: 6px 10px; cursor: pointer; }
.reading-paper { background: #fffdf9; max-width: 780px; margin: 34px auto 60px; padding: 66px 74px; min-height: 80vh; box-shadow: 0 20px 60px rgba(37,35,30,.08); }
.chapter-kicker { font-size: 10px; letter-spacing: .2em; color: var(--gold); }
.reading-paper h1 { font-family: var(--font-serif); font-size: 36px; margin: 14px 0 40px; }
.reading-content { color: #374151; line-height: 2.2; font-family: var(--font-serif); }
.reading-content :deep(p) { margin: 0 0 1.4em; text-indent: 2em; }
.reader-pager { display: flex; justify-content: space-between; margin-top: 60px; padding-top: 24px; border-top: 1px solid #eee8df; }
@media (max-width: 800px) { .reader-page { grid-template-columns: 1fr; } .reader-side { display: none; } .reading-paper { padding: 38px 24px; } }
</style>


