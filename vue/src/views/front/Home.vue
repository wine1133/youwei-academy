<template>
  <div>
    <section class="hero-carousel" @mouseenter="pauseCarousel" @mouseleave="resumeCarousel">
      <div
        v-for="(slide, index) in heroSlides"
        :key="slide.titleA"
        class="hero-slide"
        :class="{ active: activeSlide === index }"
        :style="{ backgroundImage: `url(${slide.image})` }"
      ></div>
      <div class="hero-overlay"></div>

      <button class="carousel-arrow arrow-left" aria-label="上一张" @click="prevSlide"><el-icon><ArrowLeft /></el-icon></button>
      <button class="carousel-arrow arrow-right" aria-label="下一张" @click="nextSlide"><el-icon><ArrowRight /></el-icon></button>

      <div class="hero-inner">
        <div :key="activeSlide" class="hero-copy">
          <div class="eyebrow light">{{ activeContent.eyebrow }}</div>
          <h1>{{ activeContent.titleA }}<br><em>{{ activeContent.titleB }}</em></h1>
          <p>{{ activeContent.description }}</p>
          <div class="hero-actions">
            <el-button type="primary" size="large" round @click="router.push(activeContent.primaryPath)">{{ activeContent.primaryText }} <el-icon><Right /></el-icon></el-button>
            <el-button size="large" round plain class="hero-ghost" @click="router.push(activeContent.secondaryPath)">{{ activeContent.secondaryText }}</el-button>
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
          <div class="hero-book" @click="router.push(`/front/book/${heroBook.id || activeContent.bookId}`)">
            <div class="cover-frame"><img :src="heroBook.cover || activeContent.bookCover" :alt="heroBook.name || activeContent.bookName"></div>
            <div class="book-caption">
              <span>本期推荐</span>
              <b>{{ heroBook.name || activeContent.bookName }}</b>
              <small>{{ heroBook.author || activeContent.bookAuthor }}</small>
            </div>
          </div>
          <div class="floating-note note-one"><el-icon><Reading /></el-icon><span>{{ activeContent.noteOne }}</span></div>
          <div class="floating-note note-two"><el-icon><ChatDotRound /></el-icon><span>{{ activeContent.noteTwo }}</span></div>
        </div>
      </div>

      <div class="carousel-footer">
        <div class="carousel-dots">
          <button v-for="(slide, index) in heroSlides" :key="slide.titleA" :class="{ active: activeSlide === index }" :aria-label="`切换到第 ${index + 1} 张`" @click="goSlide(index)"><i></i></button>
        </div>
        <div class="carousel-counter"><b>0{{ activeSlide + 1 }}</b><span>/ 0{{ heroSlides.length }}</span></div>
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
            <div class="article-author"><img :src="article.authorAvatar || '/images/avatars/student.jpg'" alt=""><span>{{ article.authorName || '有为书院' }}</span><span class="dot-sep">·</span><span>{{ article.views }} 阅读</span></div>
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
import { reactive, ref, computed, onMounted, onBeforeUnmount } from 'vue'
import { useRouter } from 'vue-router'
import request from '@/utils/request'

const router = useRouter()
const data = reactive({ stats: {}, books: [], articles: [], homeworks: [] })
const stats = computed(() => data.stats)
const books = computed(() => data.books.slice(0, 4))
const articles = computed(() => data.articles)
const homeworks = computed(() => data.homeworks)
const activeSlide = ref(0)
let carouselTimer

const heroSlides = [
  {
    eyebrow: 'YOUWEI ACADEMY · 2026',
    titleA: '让阅读，成为你',
    titleB: '理解世界的方式',
    description: '在经典与当代之间，在知识与行动之间，有为书院为每一位学习者提供一座安静、开放、持续生长的精神空间。',
    image: '/images/real/hero/hero-01.jpg',
    primaryText: '探索馆藏', primaryPath: '/front/library',
    secondaryText: '进入思想社区', secondaryPath: '/front/community',
    bookId: 8, bookName: '艺术的故事', bookAuthor: '贡布里希', bookCover: '/images/real/books/book-08.jpg',
    noteOne: '深度阅读', noteTwo: '思想共鸣'
  },
  {
    eyebrow: 'READ · THINK · GROW',
    titleA: '在文字与思想之间',
    titleB: '遇见更大的世界',
    description: '从一部经典出发，穿过历史、艺术与科学，让阅读成为理解现实、建立判断力的起点。',
    image: '/images/real/hero/hero-02.jpg',
    primaryText: '发现好书', primaryPath: '/front/library',
    secondaryText: '浏览思想社区', secondaryPath: '/front/community',
    bookId: 2, bookName: '人类简史', bookAuthor: '尤瓦尔·赫拉利', bookCover: '/images/real/books/book-02.jpg',
    noteOne: '经典共读', noteTwo: '跨学科视野'
  },
  {
    eyebrow: 'LEARNING IN ACTION',
    titleA: '把每一次思考',
    titleB: '写成自己的答案',
    description: '阅读不止于翻页。和同学讨论，向老师请教，把理解写下来，让知识真正进入你的能力结构。',
    image: '/images/real/hero/hero-03b.jpg',
    primaryText: '查看学习任务', primaryPath: '/front/homework',
    secondaryText: '探索馆藏', secondaryPath: '/front/library',
    bookId: 7, bookName: '代码整洁之道', bookAuthor: 'Robert C. Martin', bookCover: '/images/real/books/book-07.jpg',
    noteOne: '协作学习', noteTwo: '持续表达'
  },
  {
    eyebrow: 'CURATED SPACES',
    titleA: '从一本书出发',
    titleB: '连接真实的学习',
    description: '找到一间适合阅读的空间，规划一次到访路线，让书籍、城市与真实的人发生连接。',
    image: '/images/real/hero/hero-04.jpg',
    primaryText: '探索书院地图', primaryPath: '/front/map',
    secondaryText: '进入思想社区', secondaryPath: '/front/community',
    bookId: 1, bookName: '百年孤独', bookAuthor: '加西亚·马尔克斯', bookCover: '/images/real/books/book-01.jpg',
    noteOne: '城市书房', noteTwo: '到访路线'
  }
]

const activeContent = computed(() => heroSlides[activeSlide.value])
const heroBook = computed(() => {
  const slide = activeContent.value
  const liveBook = data.books.find(book => book.id === slide.bookId)
  return liveBook || { id: slide.bookId, name: slide.bookName, author: slide.bookAuthor, cover: slide.bookCover }
})

const goSlide = (index) => {
  activeSlide.value = (index + heroSlides.length) % heroSlides.length
  restartCarousel()
}
const nextSlide = () => goSlide(activeSlide.value + 1)
const prevSlide = () => goSlide(activeSlide.value - 1)
const stopCarousel = () => { if (carouselTimer) { clearInterval(carouselTimer); carouselTimer = undefined } }
const startCarousel = () => {
  stopCarousel()
  carouselTimer = setInterval(() => { activeSlide.value = (activeSlide.value + 1) % heroSlides.length }, 6500)
}
const restartCarousel = () => { if (carouselTimer) startCarousel() }
const pauseCarousel = () => stopCarousel()
const resumeCarousel = () => startCarousel()
const formatDate = (v) => v ? String(v).slice(0, 10) : '长期'

onMounted(() => {
  startCarousel()
  request.get('/dashboard/stats').then(res => { if (res.code === '200') data.stats = res.data }).catch(() => {})
  request.get('/book/selectAll').then(res => { if (res.code === '200') data.books = res.data || [] })
  request.get('/article/selectPage', { params: { pageNum: 1, pageSize: 3 } }).then(res => { if (res.code === '200') data.articles = res.data.list || [] })
  request.get('/homework/selectPage', { params: { pageNum: 1, pageSize: 2 } }).then(res => { if (res.code === '200') data.homeworks = res.data.list || [] })
})
onBeforeUnmount(stopCarousel)
</script>

<style scoped>
.hero-carousel { position: relative; min-height: 650px; overflow: hidden; color: #fff; background: #071c2c; }
.hero-slide { position: absolute; inset: 0; background-position: center; background-size: cover; opacity: 0; transform: scale(1.07); transition: opacity .9s ease, transform 7s ease; }
.hero-slide.active { opacity: 1; transform: scale(1); }
.hero-overlay { position: absolute; inset: 0; background: linear-gradient(90deg, rgba(5,21,34,.96) 0%, rgba(6,25,40,.84) 45%, rgba(6,25,40,.28) 100%), linear-gradient(0deg, rgba(5,20,32,.82) 0%, transparent 48%); }
.hero-overlay::after { content: ''; position: absolute; inset: 0; opacity: .16; background-image: linear-gradient(rgba(255,255,255,.13) 1px, transparent 1px), linear-gradient(90deg, rgba(255,255,255,.13) 1px, transparent 1px); background-size: 72px 72px; mask-image: linear-gradient(to right, black, transparent 72%); }
.hero-inner { position: relative; z-index: 2; max-width: 1240px; min-height: 650px; margin: 0 auto; padding: 78px 90px 80px 28px; display: grid; grid-template-columns: 1.08fr .92fr; gap: 56px; align-items: center; }
.hero-copy { animation: heroReveal .65s ease both; }
.hero-copy h1 { font-family: var(--font-serif); font-size: clamp(44px, 5vw, 68px); line-height: 1.23; letter-spacing: .01em; margin: 18px 0 24px; font-weight: 600; text-shadow: 0 8px 30px rgba(0,0,0,.3); }
.hero-copy h1 em { color: #e3c88f; font-style: normal; position: relative; }
.hero-copy h1 em::after { content: ''; position: absolute; left: 0; right: 0; bottom: 4px; height: 8px; background: rgba(195,154,84,.24); z-index: -1; }
.hero-copy > p { max-width: 590px; color: rgba(255,255,255,.76); font-size: 16px; line-height: 2; text-shadow: 0 4px 20px rgba(0,0,0,.28); }
.eyebrow.light { color: #d7bd88; }
.hero-actions { display: flex; gap: 14px; margin-top: 34px; }
.hero-ghost { color: #fff !important; border-color: rgba(255,255,255,.42) !important; background: rgba(255,255,255,.07) !important; backdrop-filter: blur(10px); }
.hero-metrics { display: flex; gap: 48px; margin-top: 44px; padding-top: 26px; border-top: 1px solid rgba(255,255,255,.2); }
.hero-metrics div { display: flex; flex-direction: column; }
.hero-metrics b { font-family: var(--font-serif); font-size: 28px; color: #fff; }
.hero-metrics span { color: rgba(255,255,255,.55); font-size: 12px; margin-top: 4px; }
.hero-visual { min-height: 470px; display: flex; justify-content: center; align-items: center; position: relative; }
.hero-book { position: relative; width: 300px; z-index: 2; filter: drop-shadow(0 38px 45px rgba(0,0,0,.38)); animation: float 6s ease-in-out infinite; cursor: pointer; }
.cover-frame { padding: 9px; border-radius: 20px; background: rgba(255,255,255,.96); transform: rotate(3deg); }
.cover-frame img { width: 100%; aspect-ratio: 3 / 4; object-fit: contain; display: block; border-radius: 14px; background: #fff; }
.book-caption { position: absolute; left: 26px; right: 26px; bottom: -18px; padding: 15px 17px; border-radius: 14px; background: rgba(7,23,36,.86); border: 1px solid rgba(255,255,255,.16); backdrop-filter: blur(16px); display: flex; flex-direction: column; box-shadow: 0 16px 34px rgba(0,0,0,.25); }
.book-caption span, .book-caption small { color: rgba(255,255,255,.62); font-size: 11px; }
.book-caption b { margin: 6px 0 3px; font-family: var(--font-serif); font-size: 22px; }
.orbital-ring { position: absolute; border: 1px solid rgba(255,255,255,.22); border-radius: 50%; }
.ring-one { width: 440px; height: 440px; }
.ring-two { width: 570px; height: 570px; border-style: dashed; opacity: .58; animation: spin 28s linear infinite; }
.floating-note { position: absolute; display: flex; align-items: center; gap: 8px; padding: 12px 16px; border-radius: 14px; color: #fff; background: rgba(255,255,255,.11); border: 1px solid rgba(255,255,255,.22); backdrop-filter: blur(16px); font-size: 13px; box-shadow: 0 14px 40px rgba(0,0,0,.16); z-index: 3; }
.note-one { top: 36px; left: -4px; animation: float 5s ease-in-out infinite; }
.note-two { bottom: 56px; right: -8px; animation: float 6s 1s ease-in-out infinite; }
.carousel-arrow { position: absolute; z-index: 5; top: 50%; transform: translateY(-50%); width: 42px; height: 42px; border-radius: 50%; border: 1px solid rgba(255,255,255,.26); background: rgba(7,24,38,.32); color: #fff; display: grid; place-items: center; cursor: pointer; backdrop-filter: blur(12px); transition: all .2s; }
.carousel-arrow:hover { background: rgba(255,255,255,.18); transform: translateY(-50%) scale(1.06); }
.arrow-left { left: 18px; } .arrow-right { right: 18px; }
.carousel-footer { position: absolute; z-index: 4; left: max(28px, calc((100vw - 1240px)/2 + 28px)); right: max(28px, calc((100vw - 1240px)/2 + 28px)); bottom: 24px; display: flex; justify-content: space-between; align-items: center; }
.carousel-dots { display: flex; gap: 9px; } .carousel-dots button { border: 0; padding: 0; background: transparent; width: 34px; height: 18px; cursor: pointer; }
.carousel-dots i { display: block; height: 3px; border-radius: 3px; background: rgba(255,255,255,.3); transition: all .3s; }
.carousel-dots button.active i { background: #d8bd88; width: 34px; }
.carousel-counter { display: flex; align-items: baseline; gap: 6px; color: rgba(255,255,255,.5); font-size: 12px; }
.carousel-counter b { font-family: var(--font-serif); font-size: 24px; color: #fff; }
.quote-band { margin-top: -34px; position: relative; z-index: 3; background: #fff; border: 1px solid var(--line); border-radius: 18px; padding: 25px 34px; display: flex; align-items: center; gap: 20px; box-shadow: var(--shadow); }
.quote-mark { font-family: Georgia, serif; font-size: 52px; color: var(--gold); line-height: .8; }
.quote-band p { margin: 0; font-family: var(--font-serif); font-size: 18px; flex: 1; }
.quote-author { color: var(--muted); font-size: 12px; white-space: nowrap; }
.book-grid { display: grid; grid-template-columns: repeat(4, 1fr); gap: 22px; }
.book-card { cursor: pointer; border-radius: 20px; background: #fff; overflow: hidden; border: 1px solid var(--line); box-shadow: 0 10px 30px rgba(20,33,61,.06); }
.book-cover { height: 310px; padding: 14px; background: #f8f5ef; display: flex; justify-content: center; }
.book-cover img { width: 100%; height: 100%; object-fit: contain; border-radius: 10px; box-shadow: 0 16px 28px rgba(20,33,61,.14); transition: transform .3s ease; }
.book-card:hover .book-cover img { transform: scale(1.035); }
.book-info { padding: 18px 20px 20px; }
.book-meta { display: flex; justify-content: space-between; color: var(--muted); font-size: 11px; text-transform: uppercase; letter-spacing: .08em; }
.book-info h3 { font-family: var(--font-serif); font-size: 21px; margin: 12px 0 6px; }
.book-info > p { margin: 0; color: var(--muted); font-size: 13px; }
.book-foot { display: flex; justify-content: space-between; align-items: center; margin-top: 18px; padding-top: 14px; border-top: 1px solid #f0ece5; font-size: 12px; color: var(--primary); }
.book-foot .available { color: var(--teal); }
.book-foot .unavailable { color: #a36c6c; }
.article-grid { display: grid; grid-template-columns: repeat(3, 1fr); gap: 22px; }
.article-card { border-radius: 20px; overflow: hidden; background: #fff; border: 1px solid var(--line); cursor: pointer; }
.article-cover { height: 200px; position: relative; overflow: hidden; }
.article-cover img { width: 100%; height: 100%; object-fit: cover; transition: transform .5s ease; }
.article-card:hover .article-cover img { transform: scale(1.04); }
.article-category { position: absolute; left: 16px; top: 16px; padding: 7px 11px; border-radius: 999px; background: rgba(9,25,37,.68); backdrop-filter: blur(10px); color: #fff; font-size: 11px; }
.article-body { padding: 22px; }
.article-body h3 { margin: 0 0 12px; font-family: var(--font-serif); font-size: 21px; line-height: 1.45; }
.article-body > p { color: var(--muted); line-height: 1.7; font-size: 13px; min-height: 44px; }
.article-author { display: flex; align-items: center; gap: 7px; margin-top: 18px; color: var(--muted); font-size: 11px; }
.article-author img { width: 26px; height: 26px; border-radius: 50%; object-fit: cover; }
.dot-sep { opacity: .4; }
.homework-banner { margin-top: 74px; background: linear-gradient(115deg, rgba(14,44,62,.97), rgba(22,78,79,.9)), url('/images/real/hero/hero-03b.jpg') center/cover; border-radius: 26px; padding: 50px; display: grid; grid-template-columns: 1.1fr .9fr; gap: 50px; color: #fff; overflow: hidden; position: relative; }
.homework-banner::after { content: '学'; position: absolute; right: 20px; bottom: -80px; font-family: var(--font-serif); font-size: 280px; color: rgba(255,255,255,.045); }
.homework-copy, .homework-list { position: relative; z-index: 1; }
.homework-copy h2 { font-family: var(--font-serif); font-size: 36px; line-height: 1.4; margin: 14px 0 16px; }
.homework-copy p { color: rgba(255,255,255,.7); line-height: 1.9; margin-bottom: 25px; }
.homework-copy .el-button { color: var(--primary-deep); }
.homework-list { display: flex; flex-direction: column; gap: 12px; }
.homework-item { display: flex; align-items: center; gap: 16px; background: rgba(255,255,255,.1); border: 1px solid rgba(255,255,255,.16); padding: 18px; border-radius: 15px; backdrop-filter: blur(14px); }
.task-index { font-family: var(--font-serif); color: #d8bd88; font-size: 20px; }
.homework-item > div { display: flex; flex-direction: column; flex: 1; }
.homework-item b { font-size: 14px; }
.homework-item small { color: rgba(255,255,255,.56); margin-top: 6px; }
@keyframes float { 0%,100% { transform: translateY(0) rotate(3deg); } 50% { transform: translateY(-14px) rotate(2deg); } }
@keyframes spin { to { transform: rotate(360deg); } }
@keyframes heroReveal { from { opacity: 0; transform: translateY(16px); } to { opacity: 1; transform: translateY(0); } }
@media (max-width: 1050px) { .hero-inner { grid-template-columns: 1fr; padding-right: 28px; } .hero-visual { display: none; } .hero-copy { max-width: 760px; } .book-grid { grid-template-columns: repeat(2,1fr); } .article-grid { grid-template-columns: 1fr 1fr; } .homework-banner { grid-template-columns: 1fr; } }
@media (max-width: 650px) { .hero-carousel, .hero-inner { min-height: 660px; } .hero-inner { padding: 60px 20px 90px; } .hero-copy h1 { font-size: 38px; } .hero-metrics { gap: 24px; } .book-grid, .article-grid { grid-template-columns: 1fr; } .quote-band { flex-wrap: wrap; } .homework-banner { padding: 30px; } .carousel-arrow { display: none; } .carousel-footer { left: 20px; right: 20px; } .arrow-left { left: 5px; } .arrow-right { right: 5px; } }
</style>