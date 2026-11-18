<template>
  <div>
    <section class="library-hero">
      <div class="page-shell">
        <div class="eyebrow light">THE COLLECTION</div>
        <h1>在书页之间，遇见更辽阔的世界</h1>
        <p>馆藏涵盖文学、历史、计算机、设计、艺术与更多领域。每一次借阅，都是与一位思想者的相遇。</p>
        <div class="search-box">
          <el-icon><Search /></el-icon>
          <input v-model="query.name" placeholder="搜索书名、作者或关键词" @keyup.enter="search">
          <button @click="search">搜索馆藏</button>
        </div>
      </div>
    </section>

    <div class="page-shell">
      <div class="filter-row">
        <button v-for="c in categories" :key="c" :class="{active: query.category === c}" @click="selectCategory(c)">{{ c }}</button>
      </div>

      <div v-if="books.length" class="book-grid">
        <article v-for="book in books" :key="book.id" class="library-card hover-lift" @click="router.push(`/front/book/${book.id}`)">
          <div class="cover-wrap">
            <img :src="book.cover" :alt="book.name">
            <div class="cover-shade"></div>
            <span class="category-pill">{{ book.category }}</span>
            <button class="quick-borrow" @click.stop="borrow(book)">借阅</button>
          </div>
          <div class="card-copy">
            <div class="title-row"><h3>{{ book.name }}</h3><span>★ {{ book.score }}</span></div>
            <p class="author">{{ book.author }}</p>
            <p class="desc line-clamp-2">{{ book.description }}</p>
            <div class="card-foot"><span>{{ book.publisher || '有为书院馆藏' }}</span><b :class="book.stock > 0 ? 'ok' : 'no'">{{ book.stock > 0 ? `余 ${book.stock} 本` : '暂不可借' }}</b></div>
          </div>
        </article>
      </div>
      <div v-else class="empty-state"><el-icon size="42"><Reading /></el-icon><p>没有找到符合条件的图书</p></div>

      <div class="pagination-wrap"><el-pagination background layout="prev, pager, next" :total="total" :page-size="query.pageSize" v-model:current-page="query.pageNum" @current-change="load"/></div>
    </div>
  </div>
</template>

<script setup>
import { reactive, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import request from '@/utils/request'
import { ElMessage } from 'element-plus'

const router = useRouter()
const categories = ['全部', '文学', '历史', '计算机', '设计', '艺术']
const query = reactive({ name: '', category: '', pageNum: 1, pageSize: 12 })
const data = reactive({ books: [], total: 0 })
const books = computed(() => data.books)

const load = () => {
  request.get('/book/selectPage', { params: query }).then(res => {
    if (res.code === '200') { data.books = res.data.list || []; data.total = res.data.total || 0 }
  })
}
const search = () => { query.pageNum = 1; load() }
const selectCategory = (c) => { query.category = c === '全部' ? '' : c; search() }
const borrow = (book) => {
  const user = JSON.parse(localStorage.getItem('system-user') || '{}')
  if (!user.id) { ElMessage.warning('请先登录后再借阅'); router.push('/login'); return }
  request.post('/borrow/add', { bookId: book.id, userId: user.id }).then(res => {
    if (res.code === '200') { ElMessage.success('借阅成功，请在个人中心查看归还日期'); load() }
    else ElMessage.error(res.msg)
  })
}
onMounted(load)
</script>

<style scoped>
.library-hero { background: linear-gradient(120deg, #0b2132, #1c5058); color: #fff; padding: 64px 0 76px; position: relative; overflow: hidden; }
.library-hero::after { content: ''; position: absolute; width: 500px; height: 500px; border-radius: 50%; background: radial-gradient(circle, rgba(195,154,84,.22), transparent 68%); right: -100px; top: -200px; }
.library-hero h1 { font-family: var(--font-serif); font-size: 43px; margin: 13px 0 16px; font-weight: 600; }
.library-hero p { color: rgba(255,255,255,.62); max-width: 660px; line-height: 1.9; font-size: 15px; }
.search-box { display: flex; align-items: center; gap: 12px; margin-top: 30px; max-width: 620px; height: 56px; border-radius: 16px; background: rgba(255,255,255,.95); padding: 6px 7px 6px 18px; box-shadow: 0 20px 40px rgba(0,0,0,.18); }
.search-box input { border: 0; outline: 0; flex: 1; font-size: 14px; color: var(--ink); background: transparent; }
.search-box button { height: 44px; border: 0; border-radius: 12px; padding: 0 22px; color: #fff; background: var(--primary); cursor: pointer; font-weight: 600; }
.filter-row { display: flex; gap: 10px; flex-wrap: wrap; margin: 30px 0; }
.filter-row button { border: 1px solid var(--line); background: rgba(255,255,255,.8); color: var(--muted); border-radius: 999px; padding: 9px 18px; cursor: pointer; transition: all .2s; }
.filter-row button.active, .filter-row button:hover { background: var(--primary-deep); color: #fff; border-color: var(--primary-deep); }
.book-grid { display: grid; grid-template-columns: repeat(4, 1fr); gap: 24px; }
.library-card { background: #fff; border: 1px solid var(--line); border-radius: 20px; overflow: hidden; cursor: pointer; box-shadow: 0 10px 30px rgba(20,33,61,.05); }
.cover-wrap { height: 300px; position: relative; background: #eae5dc; display: flex; justify-content: center; padding: 20px; overflow: hidden; }
.cover-wrap img { height: 100%; width: 75%; object-fit: cover; border-radius: 11px; box-shadow: 0 18px 30px rgba(20,33,61,.2); transition: transform .3s; }
.library-card:hover img { transform: translateY(-4px) scale(1.02); }
.cover-shade { position: absolute; inset: 0; background: linear-gradient(transparent 45%, rgba(3,15,24,.52)); opacity: 0; transition: opacity .3s; }
.library-card:hover .cover-shade { opacity: 1; }
.category-pill { position: absolute; top: 16px; left: 16px; background: rgba(9,26,38,.55); color: #fff; padding: 6px 11px; border-radius: 999px; font-size: 11px; backdrop-filter: blur(10px); }
.quick-borrow { position: absolute; left: 50%; bottom: 18px; transform: translate(-50%, 14px); border: 0; border-radius: 999px; padding: 10px 24px; background: #fff; color: var(--primary); font-weight: 700; opacity: 0; transition: all .3s; cursor: pointer; z-index: 2; }
.library-card:hover .quick-borrow { opacity: 1; transform: translate(-50%, 0); }
.card-copy { padding: 20px; }
.title-row { display: flex; justify-content: space-between; gap: 10px; align-items: flex-start; }
.title-row h3 { margin: 0; font-family: var(--font-serif); font-size: 21px; line-height: 1.4; }
.title-row span { color: #ae7b24; font-size: 12px; white-space: nowrap; }
.author { color: var(--muted); font-size: 12px; margin: 7px 0 12px; }
.desc { color: #7c8794; font-size: 13px; line-height: 1.7; min-height: 44px; }
.card-foot { margin-top: 16px; padding-top: 14px; border-top: 1px solid #f1ede6; display: flex; justify-content: space-between; color: var(--muted); font-size: 11px; }
.card-foot .ok { color: var(--teal); }
.card-foot .no { color: #ad6868; }
@media (max-width: 1050px) { .book-grid { grid-template-columns: repeat(2,1fr); } }
@media (max-width: 600px) { .book-grid { grid-template-columns: 1fr; } .library-hero h1 { font-size: 32px; } .search-box { height: auto; flex-wrap: wrap; padding: 10px; } .search-box input { width: 100%; padding: 8px; } }
</style>

