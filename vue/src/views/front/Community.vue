<template>
  <div>
    <section class="community-hero">
      <div class="page-shell community-head">
        <div>
          <div class="eyebrow light">IDEAS WORTH SHARING</div>
          <h1>思想在这里相遇</h1>
          <p>读书、观察、提问与表达。让每一个真实的思考，都拥有被看见的机会。</p>
        </div>
        <el-button size="large" round @click="write">分享我的思考 <el-icon><EditPen /></el-icon></el-button>
      </div>
    </section>
    <div class="page-shell">
      <div class="community-tabs">
        <button v-for="c in categories" :key="c" :class="{active: category === c}" @click="change(c)">{{ c }}</button>
      </div>
      <article v-if="featured.id" class="featured-article" @click="open(featured.id)">
        <div class="featured-cover"><img :src="featured.cover" alt=""><span>编辑推荐</span></div>
        <div class="featured-copy">
          <div class="eyebrow">FEATURED STORY</div>
          <h2>{{ featured.title }}</h2>
          <p>{{ featured.summary }}</p>
          <div class="author-line"><img :src="featured.authorAvatar || '/images/avatars/teacher.jpg'" alt=""><span>{{ featured.authorName }}</span><i></i><span>{{ featured.views }} 阅读</span><i></i><span>{{ featured.likes }} 喜欢</span></div>
          <span class="read-more">继续阅读 <el-icon><ArrowRight /></el-icon></span>
        </div>
      </article>

      <div class="section-title"><div><div class="eyebrow">LATEST VOICES</div><h2>最新文章</h2></div></div>
      <div class="article-grid">
        <article v-for="a in articles" :key="a.id" class="article-card hover-lift" @click="open(a.id)">
          <div class="article-cover"><img :src="a.cover" alt=""><span>{{ a.category }}</span></div>
          <div class="article-body">
            <h3 class="line-clamp-2">{{ a.title }}</h3>
            <p class="line-clamp-2">{{ a.summary }}</p>
            <div class="article-meta"><span>{{ a.authorName || '书院作者' }}</span><span>{{ a.views }} 阅读 · {{ a.likes }} 喜欢</span></div>
          </div>
        </article>
      </div>
      <div class="pagination-wrap"><el-pagination background layout="prev, pager, next" :total="total" :page-size="8" v-model:current-page="pageNum" @current-change="load"/></div>
    </div>
  </div>
</template>

<script setup>
import { reactive, computed, onMounted, ref } from 'vue'
import { useRouter } from 'vue-router'
import request from '@/utils/request'
import { ElMessage } from 'element-plus'

const router = useRouter()
const categories = ['全部', '阅读方法', '效率工具', '主题书单', '教学随笔']
const category = ref('全部')
const pageNum = ref(1)
const total = ref(0)
const data = reactive({ articles: [] })
const articles = computed(() => data.articles)
const featured = reactive({})
const load = () => {
  request.get('/article/selectPage', { params: { pageNum: pageNum.value, pageSize: 8, category: category.value === '全部' ? '' : category.value } }).then(res => {
    if (res.code === '200') { data.articles = res.data.list || []; total.value = res.data.total || 0; if (!featured.id && data.articles.length) Object.assign(featured, data.articles[0]) }
  })
}
const change = (c) => { category.value = c; pageNum.value = 1; load() }
const open = (id) => router.push(`/front/article/${id}`)
const write = () => {
  const user = JSON.parse(localStorage.getItem('system-user') || '{}')
  if (!user.id) { ElMessage.warning('请先登录后发布文章'); router.push('/login'); return }
  router.push('/front/publish')
}
onMounted(load)
</script>


<style scoped>
.community-hero { background: #102536; color: #fff; padding: 58px 0 70px; position: relative; overflow: hidden; }
.community-hero::after { content: ''; position: absolute; width: 420px; height: 420px; border-radius: 50%; right: 5%; top: -250px; background: radial-gradient(circle, rgba(195,154,84,.25), transparent 70%); }
.community-head { display: flex; align-items: end; justify-content: space-between; gap: 30px; position: relative; z-index: 1; }
.community-head h1 { font-family: var(--font-serif); font-size: 48px; margin: 12px 0; }
.community-head p { color: rgba(255,255,255,.58); max-width: 600px; line-height: 1.9; }
.community-head .el-button { color: var(--primary-deep); }
.community-tabs { display: flex; gap: 10px; flex-wrap: wrap; margin: 30px 0 26px; }
.community-tabs button { border: 1px solid var(--line); background: #fff; padding: 9px 18px; border-radius: 999px; color: var(--muted); cursor: pointer; }
.community-tabs button.active { background: var(--primary-deep); color: #fff; border-color: var(--primary-deep); }
.featured-article { display: grid; grid-template-columns: 1.1fr .9fr; background: #fff; border: 1px solid var(--line); border-radius: 24px; overflow: hidden; cursor: pointer; box-shadow: var(--shadow); }
.featured-cover { height: 350px; position: relative; }
.featured-cover img { width: 100%; height: 100%; object-fit: cover; }
.featured-cover span { position: absolute; left: 22px; top: 22px; background: rgba(10,26,38,.65); color: #fff; border-radius: 999px; padding: 8px 13px; font-size: 11px; backdrop-filter: blur(10px); }
.featured-copy { padding: 44px; display: flex; flex-direction: column; justify-content: center; }
.featured-copy h2 { font-family: var(--font-serif); font-size: 33px; line-height: 1.4; margin: 15px 0; }
.featured-copy > p { color: var(--muted); line-height: 1.9; }
.author-line { display: flex; align-items: center; gap: 9px; color: var(--muted); font-size: 11px; margin-top: 18px; }
.author-line img { width: 30px; height: 30px; border-radius: 50%; }
.author-line i { width: 3px; height: 3px; background: #c8c1b7; border-radius: 50%; }
.read-more { display: inline-flex; align-items: center; gap: 7px; color: var(--primary); font-weight: 700; margin-top: 24px; font-size: 13px; }
.article-grid { display: grid; grid-template-columns: repeat(3, 1fr); gap: 22px; }
.article-card { background: #fff; border: 1px solid var(--line); border-radius: 20px; overflow: hidden; cursor: pointer; }
.article-cover { height: 205px; position: relative; }
.article-cover img { width: 100%; height: 100%; object-fit: cover; }
.article-cover span { position: absolute; left: 14px; top: 14px; background: rgba(10,26,38,.62); color: #fff; padding: 6px 11px; border-radius: 999px; font-size: 10px; }
.article-body { padding: 20px; }
.article-body h3 { font-family: var(--font-serif); font-size: 20px; line-height: 1.45; margin: 0 0 10px; }
.article-body p { color: var(--muted); line-height: 1.7; font-size: 13px; min-height: 43px; }
.article-meta { display: flex; justify-content: space-between; border-top: 1px solid #f1ede6; padding-top: 14px; margin-top: 16px; color: #8b949e; font-size: 10px; }
@media(max-width:900px){.featured-article{grid-template-columns:1fr}.article-grid{grid-template-columns:1fr 1fr}.community-head{flex-direction:column;align-items:flex-start}} @media(max-width:600px){.article-grid{grid-template-columns:1fr}.community-head h1{font-size:36px}}
</style>


