<template>
  <div>
    <section class="hero">
      <div class="hero-glow glow-a"></div>
      <div class="hero-glow glow-b"></div>
      <div class="hero-inner">
        <div class="hero-copy">
          <div class="eyebrow light">YOUWEI ACADEMY · 2026</div>
          <h1>让阅读，成为你<br><em>理解世界</em>的方式</h1>
          <p>在经典与当代之间，在知识与行动之间，有为书院为每一位学习者提供一座安静、开放、持续生长的精神空间。</p>
          <div class="hero-actions">
            <el-button type="primary" size="large" round @click="router.push('/front/library')">探索馆藏 <el-icon><Right /></el-icon></el-button>
            <el-button size="large" round plain class="hero-ghost" @click="router.push('/front/community')">进入思想社区</el-button>
          </div>
          <div class="hero-metrics">
            <div><b>{{ stats.bookCount || 8 }}</b><span>馆藏图书</span></div>
            <div><b>{{ stats.userCount || 2 }}</b><span>书院成员</span></div>
            <div><b>{{ stats.articleCount || 4 }}</b><span>思想文章</span></div>
          </div>
        </div>
        <div class="hero-visual">
          <div class="orbital-ring ring-one"></div>
          <div class="orbital-ring ring-two"></div>
          <div class="hero-book">
            <img :src="featured.cover || '/images/covers/book-01.svg'" alt="">
            <div class="book-caption">
              <span>本周推荐</span>
              <b>{{ featured.name || '百年孤独' }}</b>
              <small>{{ featured.author || '加西亚·马尔克斯' }}</small>
            </div>
          </div>
          <div class="floating-note note-one"><el-icon><Reading /></el-icon><span>深度阅读</span></div>
          <div class="floating-note note-two"><el-icon><ChatDotRound /></el-icon><span>思想共鸣</span></div>
        </div>
      </div>
    </section>

    <div class="page-shell home-body">
      <section class="quote-band">
        <span class="quote-mark">“</span>
        <p>读书不是为了雄辩和驳斥，也不是为了轻信和盲从，而是为了思考和权衡。</p>
        <span class="quote-author">— 弗朗西斯·培根</span>
      </section>

      <div class="section-title">
        <div><div class="eyebrow">CURATED READING</div><h2>今日精选</h2><p>让值得反复阅读的书，被更多人看见。</p></div>
        <router-link to="/front/library">查看全部 <el-icon><ArrowRight /></el-icon></router-link>
      </div>
      <div class="book-grid">
        <article v-for="book in books" :key="book.id" class="book-card hover-lift" @click="router.push(`/front/book/${book.id}`)">
          <div class="book-cover"><img :src="book.cover" :alt="book.name"></div>
          <div class="book-info">
            <div class="book-meta"><span>{{ book.category }}</span><span class="rating">★ {{ book.score }}</span></div>
            <h3>{{ book.name }}</h3>
            <p>{{ book.author }}</p>
            <div class="book-foot"><span :class="book.stock > 0 ? 'available' : 'unavailable'">{{ book.stock > 0 ? `可借 ${book.stock} 本` : '暂不可借' }}</span><el-icon><ArrowRight /></el-icon></div>
          </div>
        </article>
      </div>

      <div class="section-title">
        <div><div class="eyebrow">IDEAS & STORIES</div><h2>思想现场</h2><p>来自老师、学生和每一位阅读者的真实思考。</p></div>
        <router-link to="/front/community">发现更多 <el-icon><ArrowRight /></el-icon></router-link>
      </div>
      <div class="article-grid">
        <article v-for="article in articles" :key="article.id" class="article-card hover-lift" @click="router.push(`/front/article/${article.id}`)">
          <div class="article-cover"><img :src="article.cover" :alt="article.title"><span class="article-category">{{ article.category }}</span></div>
          <div class="article-body">
            <h3 class="line-clamp-2">{{ article.title }}</h3>
            <p class="line-clamp-2">{{ article.summary }}</p>
            <div class="article-author"><img :src="article.authorAvatar || '/images/avatars/student.svg'" alt=""><span>{{ article.authorName || '有为书院' }}</span><span class="dot-sep">·</span><span>{{ article.views }} 阅读</span></div>
          </div>
        </article>
      </div>

      <section class="homework-banner">
        <div class="homework-copy">
          <div class="eyebrow light">LEARNING IN ACTION</div>
          <h2>学习不止于阅读<br>更在于思考与表达</h2>
          <p>完成老师布置的学习任务，把你的理解写下来，让知识真正成为自己的能力。</p>
          <el-button round @click="router.push('/front/homework')">查看学习任务 <el-icon><ArrowRight /></el-icon></el-button>
        </div>
        <div class="homework-list">
          <div v-for="task in homeworks" :key="task.id" class="homework-item">
            <span class="task-index">0{{ task.id }}</span>
            <div><b class="line-clamp-1">{{ task.title }}</b><small>{{ task.teacherName }} · {{ formatDate(task.deadline) }} 截止</small></div>
            <el-icon><ArrowRight /></el-icon>
          </div>
        </div>
      </section>
    </div>
  </div>
</template>

<script setup>
import { reactive, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import request from '@/utils/request'

const router = useRouter()
const data = reactive({ stats: {}, books: [], articles: [], homeworks: [] })
const stats = computed(() => data.stats)
const books = computed(() => data.books)
const articles = computed(() => data.articles)
const homeworks = computed(() => data.homeworks)
const featured = reactive({ cover: '', name: '', author: '' })
const formatDate = (v) => v ? String(v).slice(0, 10) : '长期'

onMounted(() => {
  request.get('/dashboard/stats').then(res => { if (res.code === '200') data.stats = res.data }).catch(() => {})
  request.get('/book/selectPage', { params: { pageNum: 1, pageSize: 4 } }).then(res => {
    if (res.code === '200') { data.books = res.data.list || []; Object.assign(featured, data.books[0] || {}) }
  })
  request.get('/article/selectPage', { params: { pageNum: 1, pageSize: 3 } }).then(res => { if (res.code === '200') data.articles = res.data.list || [] })
  request.get('/homework/selectPage', { params: { pageNum: 1, pageSize: 2 } }).then(res => { if (res.code === '200') data.homeworks = res.data.list || [] })
})
</script>

<style scoped>
.hero { position: relative; overflow: hidden; min-height: 620px; background: linear-gradient(135deg, #0b2033 0%, #103b52 52%, #2c6b68 100%); color: #fff; }
.hero::before { content: ''; position: absolute; inset: 0; opacity: .14; background-image: linear-gradient(rgba(255,255,255,.13) 1px, transparent 1px), linear-gradient(90deg, rgba(255,255,255,.13) 1px, transparent 1px); background-size: 70px 70px; mask-image: linear-gradient(to right, black, transparent 75%); }
.hero-glow { position: absolute; border-radius: 50%; filter: blur(6px); }
.glow-a { width: 500px; height: 500px; right: -160px; top: -150px; background: radial-gradient(circle, rgba(195,154,84,.32), transparent 65%); }
.glow-b { width: 430px; height: 430px; left: 30%; bottom: -300px; background: radial-gradient(circle, rgba(79,193,175,.22), transparent 65%); }
.hero-inner { position: relative; z-index: 2; max-width: 1240px; margin: 0 auto; padding: 80px 28px 70px; display: grid; grid-template-columns: 1.1fr .9fr; gap: 70px; align-items: center; }
.hero-copy h1 { font-family: var(--font-serif); font-size: clamp(44px, 5vw, 68px); line-height: 1.25; letter-spacing: .01em; margin: 18px 0 24px; font-weight: 600; }
.hero-copy h1 em { color: #e3c88f; font-style: normal; position: relative; }
.hero-copy h1 em::after { content: ''; position: absolute; left: 0; right: 0; bottom: 4px; height: 8px; background: rgba(195,154,84,.22); z-index: -1; }
.hero-copy > p { max-width: 570px; color: rgba(255,255,255,.7); font-size: 16px; line-height: 2; }
.eyebrow.light { color: #d7bd88; }
.hero-actions { display: flex; gap: 14px; margin-top: 34px; }
.hero-ghost { color: #fff !important; border-color: rgba(255,255,255,.36) !important; background: rgba(255,255,255,.04) !important; }
.hero-metrics { display: flex; gap: 48px; margin-top: 44px; padding-top: 26px; border-top: 1px solid rgba(255,255,255,.14); }
.hero-metrics div { display: flex; flex-direction: column; }
.hero-metrics b { font-family: var(--font-serif); font-size: 28px; color: #fff; }
.hero-metrics span { color: rgba(255,255,255,.5); font-size: 12px; margin-top: 4px; }
.hero-visual { min-height: 450px; display: flex; justify-content: center; align-items: center; position: relative; }
.hero-book { position: relative; width: 270px; transform: rotate(4deg); filter: drop-shadow(0 40px 50px rgba(0,0,0,.34)); animation: float 6s ease-in-out infinite; }
.hero-book img { width: 100%; border-radius: 20px; display: block; }
.book-caption { position: absolute; left: 18px; right: 18px; bottom: 18px; padding: 16px; border-radius: 14px; background: rgba(8,22,33,.58); backdrop-filter: blur(14px); display: flex; flex-direction: column; }
.book-caption span, .book-caption small { color: rgba(255,255,255,.62); font-size: 11px; }
.book-caption b { margin: 6px 0 3px; font-family: var(--font-serif); font-size: 22px; }
.orbital-ring { position: absolute; border: 1px solid rgba(255,255,255,.15); border-radius: 50%; }
.ring-one { width: 450px; height: 450px; }
.ring-two { width: 560px; height: 560px; border-style: dashed; opacity: .6; animation: spin 28s linear infinite; }
.floating-note { position: absolute; display: flex; align-items: center; gap: 8px; padding: 12px 16px; border-radius: 14px; color: #fff; background: rgba(255,255,255,.1); border: 1px solid rgba(255,255,255,.18); backdrop-filter: blur(16px); font-size: 13px; box-shadow: 0 14px 40px rgba(0,0,0,.15); }
.note-one { top: 40px; left: 0; animation: float 5s ease-in-out infinite; }
.note-two { bottom: 60px; right: -5px; animation: float 6s 1s ease-in-out infinite; }
.quote-band { margin-top: -34px; position: relative; z-index: 3; background: #fff; border: 1px solid var(--line); border-radius: 18px; padding: 25px 34px; display: flex; align-items: center; gap: 20px; box-shadow: var(--shadow); }
.quote-mark { font-family: Georgia, serif; font-size: 52px; color: var(--gold); line-height: .8; }
.quote-band p { margin: 0; font-family: var(--font-serif); font-size: 18px; flex: 1; }
.quote-author { color: var(--muted); font-size: 12px; white-space: nowrap; }
.book-grid { display: grid; grid-template-columns: repeat(4, 1fr); gap: 22px; }
.book-card { cursor: pointer; border-radius: 20px; background: #fff; overflow: hidden; border: 1px solid var(--line); box-shadow: 0 10px 30px rgba(20,33,61,.06); }
.book-cover { height: 300px; padding: 18px; background: #f1ede5; display: flex; justify-content: center; }
.book-cover img { width: 78%; height: 100%; object-fit: cover; border-radius: 12px; box-shadow: 0 16px 28px rgba(20,33,61,.18); transition: transform .3s ease; }
.book-card:hover .book-cover img { transform: scale(1.035) rotate(-1deg); }
.book-info { padding: 18px 20px 20px; }
.book-meta { display: flex; justify-content: space-between; color: var(--muted); font-size: 11px; text-transform: uppercase; letter-spacing: .08em; }
.book-info h3 { font-family: var(--font-serif); font-size: 21px; margin: 12px 0 6px; }
.book-info > p { margin: 0; color: var(--muted); font-size: 13px; }
.book-foot { display: flex; justify-content: space-between; align-items: center; margin-top: 18px; padding-top: 14px; border-top: 1px solid #f0ece5; font-size: 12px; color: var(--primary); }
.book-foot .available { color: var(--teal); }
.book-foot .unavailable { color: #a36c6c; }
.article-grid { display: grid; grid-template-columns: repeat(3, 1fr); gap: 22px; }
.article-card { border-radius: 20px; overflow: hidden; background: #fff; border: 1px solid var(--line); cursor: pointer; }
.article-cover { height: 200px; position: relative; }
.article-cover img { width: 100%; height: 100%; object-fit: cover; }
.article-category { position: absolute; left: 16px; top: 16px; padding: 7px 11px; border-radius: 999px; background: rgba(9,25,37,.62); backdrop-filter: blur(10px); color: #fff; font-size: 11px; }
.article-body { padding: 22px; }
.article-body h3 { margin: 0 0 12px; font-family: var(--font-serif); font-size: 21px; line-height: 1.45; }
.article-body > p { color: var(--muted); line-height: 1.7; font-size: 13px; min-height: 44px; }
.article-author { display: flex; align-items: center; gap: 7px; margin-top: 18px; color: var(--muted); font-size: 11px; }
.article-author img { width: 26px; height: 26px; border-radius: 50%; }
.dot-sep { opacity: .4; }
.homework-banner { margin-top: 74px; background: linear-gradient(115deg, #132e42, #1d575a); border-radius: 26px; padding: 50px; display: grid; grid-template-columns: 1.1fr .9fr; gap: 50px; color: #fff; overflow: hidden; position: relative; }
.homework-banner::after { content: '学'; position: absolute; right: 20px; bottom: -80px; font-family: var(--font-serif); font-size: 280px; color: rgba(255,255,255,.035); }
.homework-copy h2 { font-family: var(--font-serif); font-size: 36px; line-height: 1.4; margin: 14px 0 16px; }
.homework-copy p { color: rgba(255,255,255,.62); line-height: 1.9; margin-bottom: 25px; }
.homework-copy .el-button { color: var(--primary-deep); }
.homework-list { display: flex; flex-direction: column; gap: 12px; position: relative; z-index: 1; }
.homework-item { display: flex; align-items: center; gap: 16px; background: rgba(255,255,255,.08); border: 1px solid rgba(255,255,255,.12); padding: 18px; border-radius: 15px; }
.task-index { font-family: var(--font-serif); color: #d8bd88; font-size: 20px; }
.homework-item > div { display: flex; flex-direction: column; flex: 1; }
.homework-item b { font-size: 14px; }
.homework-item small { color: rgba(255,255,255,.48); margin-top: 6px; }
@keyframes float { 0%,100% { transform: translateY(0) rotate(4deg); } 50% { transform: translateY(-14px) rotate(3deg); } }
@keyframes spin { to { transform: rotate(360deg); } }
@media (max-width: 1050px) { .hero-inner { grid-template-columns: 1fr; } .hero-visual { display: none; } .book-grid { grid-template-columns: repeat(2,1fr); } .article-grid { grid-template-columns: 1fr 1fr; } .homework-banner { grid-template-columns: 1fr; } }
@media (max-width: 650px) { .hero-inner { padding: 60px 18px; } .hero-copy h1 { font-size: 38px; } .hero-metrics { gap: 24px; } .book-grid, .article-grid { grid-template-columns: 1fr; } .quote-band { flex-wrap: wrap; } .homework-banner { padding: 30px; } }
</style>




