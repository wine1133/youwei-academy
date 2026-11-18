<template>
  <div class="page-shell detail-page" v-if="book.id">
    <div class="crumbs"><router-link to="/front/library">馆藏</router-link><span>/</span><span>{{ book.category }}</span></div>
    <section class="book-hero">
      <div class="cover-panel"><img :src="book.cover" :alt="book.name"><span class="edition">YOUWEI COLLECTION</span></div>
      <div class="info-panel">
        <div class="eyebrow">BOOK DETAIL</div>
        <h1>{{ book.name }}</h1>
        <p class="author">{{ book.author }}</p>
        <p class="description">{{ book.description }}</p>
        <div class="meta-grid">
          <div><span>分类</span><b>{{ book.category }}</b></div>
          <div><span>出版社</span><b>{{ book.publisher }}</b></div>
          <div><span>评分</span><b class="gold">★ {{ book.score }}</b></div>
          <div><span>可借</span><b>{{ book.stock }} / {{ book.total }} 本</b></div>
        </div>
        <div class="actions">
          <el-button type="primary" size="large" round :disabled="book.stock <= 0" @click="borrow">立即借阅</el-button>
          <el-button size="large" round plain @click="router.push(`/front/reader/${book.id}`)">在线试读</el-button>
        </div>
        <div class="isbn">ISBN {{ book.isbn }}</div>
      </div>
    </section>

    <section class="content-grid">
      <div class="reading-list card">
        <div class="block-title"><div><div class="eyebrow">CONTENTS</div><h2>章节目录</h2></div><span>{{ chapters.length }} 章</span></div>
        <div v-for="(c, index) in chapters" :key="c.id" class="chapter-row" @click="read(c.id)">
          <span class="num">{{ String(index + 1).padStart(2, '0') }}</span>
          <b>{{ c.title }}</b>
          <el-icon><ArrowRight /></el-icon>
        </div>
        <div v-if="!chapters.length" class="empty-state">该图书暂未配置在线章节</div>
      </div>
      <aside class="detail-aside">
        <div class="aside-card quote">
          <span class="quote-mark">“</span>
          <p>书籍并不是把外在的东西给予我们，而是促使我们内在的东西苏醒。</p>
        </div>
        <div class="aside-card tips">
          <h3>借阅须知</h3>
          <p><el-icon><Clock /></el-icon> 借期 30 天</p>
          <p><el-icon><Collection /></el-icon> 最多同时借阅 5 本</p>
          <p><el-icon><Refresh /></el-icon> 到期前请在个人中心归还</p>
        </div>
      </aside>
    </section>
  </div>
</template>

<script setup>
import { reactive, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import request from '@/utils/request'
import { ElMessage } from 'element-plus'

const route = useRoute()
const router = useRouter()
const book = reactive({})
const chapters = reactive([])

const load = () => {
  request.get(`/book/selectById/${route.params.id}`).then(res => { if (res.code === '200') Object.assign(book, res.data || {}) })
  request.get(`/chapter/selectByBookId/${route.params.id}`).then(res => { if (res.code === '200') chapters.splice(0, chapters.length, ...(res.data || [])) })
}
const borrow = () => {
  const user = JSON.parse(localStorage.getItem('system-user') || '{}')
  if (!user.id) { ElMessage.warning('请先登录'); router.push('/login'); return }
  request.post('/borrow/add', { bookId: book.id, userId: user.id }).then(res => {
    if (res.code === '200') { ElMessage.success('借阅成功'); load() } else ElMessage.error(res.msg)
  })
}
const read = (chapterId) => router.push({ path: `/front/reader/${book.id}`, query: { chapterId } })
onMounted(load)
</script>

<style scoped>
.detail-page { padding-top: 26px; }
.crumbs { display: flex; gap: 8px; color: var(--muted); font-size: 12px; margin-bottom: 20px; }
.crumbs a { color: var(--primary); }
.book-hero { display: grid; grid-template-columns: 390px 1fr; gap: 60px; align-items: center; }
.cover-panel { background: #ece6db; border-radius: 24px; padding: 34px; min-height: 540px; display: flex; justify-content: center; align-items: center; flex-direction: column; gap: 22px; position: relative; }
.cover-panel img { width: 72%; max-height: 440px; object-fit: cover; border-radius: 15px; box-shadow: 0 30px 50px rgba(20,33,61,.24); }
.edition { font-size: 9px; letter-spacing: .24em; color: #8d8274; }
.info-panel h1 { font-family: var(--font-serif); font-size: 48px; margin: 14px 0 8px; }
.author { color: var(--muted); font-size: 16px; margin: 0 0 25px; }
.description { line-height: 2; color: #5f6c79; font-size: 15px; max-width: 680px; }
.meta-grid { display: grid; grid-template-columns: repeat(4, 1fr); border-top: 1px solid var(--line); border-bottom: 1px solid var(--line); margin: 30px 0; padding: 20px 0; }
.meta-grid div { display: flex; flex-direction: column; gap: 6px; }
.meta-grid span { color: var(--muted); font-size: 11px; }
.meta-grid b { font-size: 14px; }
.meta-grid .gold { color: #ae7b24; }
.actions { display: flex; gap: 12px; }
.isbn { color: #a1a9b2; font-size: 11px; margin-top: 18px; }
.content-grid { display: grid; grid-template-columns: 1fr 300px; gap: 24px; margin-top: 64px; }
.block-title { display: flex; justify-content: space-between; align-items: center; margin-bottom: 18px; }
.block-title h2 { font-family: var(--font-serif); margin: 5px 0 0; }
.block-title > span { color: var(--muted); font-size: 12px; }
.chapter-row { display: flex; align-items: center; gap: 16px; padding: 17px 12px; border-top: 1px solid #f0ece5; cursor: pointer; transition: background .2s; }
.chapter-row:hover { background: #faf8f4; color: var(--primary); }
.chapter-row .num { font-family: var(--font-serif); color: var(--gold); font-size: 17px; width: 30px; }
.chapter-row b { flex: 1; font-weight: 500; font-size: 14px; }
.detail-aside { display: flex; flex-direction: column; gap: 18px; }
.aside-card { border-radius: 20px; padding: 24px; background: #fff; border: 1px solid var(--line); }
.quote { background: var(--primary-deep); color: #fff; position: relative; overflow: hidden; }
.quote .quote-mark { font-size: 70px; color: rgba(195,154,84,.5); line-height: .7; }
.quote p { font-family: var(--font-serif); line-height: 1.9; margin: 12px 0 0; }
.tips h3 { font-family: var(--font-serif); margin: 0 0 18px; }
.tips p { display: flex; align-items: center; gap: 9px; color: var(--muted); font-size: 12px; margin: 12px 0; }
@media (max-width: 950px) { .book-hero { grid-template-columns: 1fr; } .cover-panel { min-height: 420px; } .content-grid { grid-template-columns: 1fr; } .meta-grid { grid-template-columns: 1fr 1fr; gap: 18px; } }
@media (max-width: 600px) { .info-panel h1 { font-size: 34px; } .detail-page { padding: 18px 16px 40px; } }
</style>
