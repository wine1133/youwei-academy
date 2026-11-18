<template>
  <div v-if="article.id">
    <section class="article-hero">
      <img :src="article.cover" alt="">
      <div class="hero-overlay"></div>
      <div class="page-shell article-head">
        <div class="article-tag">{{ article.category }}</div>
        <h1>{{ article.title }}</h1>
        <p>{{ article.summary }}</p>
        <div class="author"><img :src="article.authorAvatar || '/images/avatars/teacher.svg'" alt=""><div><b>{{ article.authorName }}</b><span>{{ formatDate(article.createTime) }} · {{ article.views }} 阅读</span></div></div>
      </div>
    </section>
    <div class="page-shell article-layout">
      <article class="article-main card">
        <div class="article-content" v-html="article.content"></div>
        <div class="article-actions">
          <button :class="{active: liked}" @click="like"><el-icon><Pointer /></el-icon> {{ article.likes }} 喜欢</button>
          <button :class="{active: collected}" @click="collect"><el-icon><Collection /></el-icon> {{ article.collects }} 收藏</button>
          <button @click="share"><el-icon><Share /></el-icon> 分享</button>
        </div>
      </article>
      <aside class="article-side">
        <div class="side-card">
          <h3>关于作者</h3>
          <div class="side-author"><img :src="article.authorAvatar || '/images/avatars/teacher.svg'" alt=""><div><b>{{ article.authorName }}</b><span>有为书院创作者</span></div></div>
          <p>持续阅读，持续思考，也持续把思考写下来。</p>
        </div>
        <div class="side-card">
          <h3>文章标签</h3>
          <div class="tag-list"><span class="pill blue" v-for="t in tags" :key="t">{{ t }}</span></div>
        </div>
      </aside>
    </div>

    <section class="page-shell comments-section">
      <div class="comment-title"><h2>思想回声</h2><span>{{ comments.length }} 条评论</span></div>
      <div class="comment-compose card">
        <img :src="user.avatar || '/images/avatars/student.svg'" alt="">
        <textarea v-model="commentText" placeholder="写下你的理解、疑问或共鸣……"></textarea>
        <el-button type="primary" round @click="submitComment">发布评论</el-button>
      </div>
      <div class="comments-list">
        <div v-for="c in comments" :key="c.id" class="comment-item">
          <img :src="c.avatar || '/images/avatars/student.svg'" alt="">
          <div><div class="comment-meta"><b>{{ c.userName }}</b><span>{{ formatDate(c.createTime) }}</span></div><p>{{ c.content }}</p></div>
        </div>
        <div v-if="!comments.length" class="empty-state">还没有评论，来留下第一句思考吧。</div>
      </div>
    </section>
  </div>
</template>

<script setup>
import { reactive, ref, computed, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import request from '@/utils/request'
import { ElMessage } from 'element-plus'

const route = useRoute()
const router = useRouter()
const article = reactive({})
const comments = reactive([])
const articleId = route.params.id
const user = reactive(JSON.parse(localStorage.getItem('system-user') || '{}'))
const liked = ref(false), collected = ref(false), commentText = ref('')
const tags = computed(() => (article.tags || '').split(',').filter(Boolean))
const formatDate = (v) => v ? String(v).replace('T',' ').slice(0,16) : ''
const load = () => {
  request.get(`/article/selectById/${articleId}`).then(res => { if (res.code === '200') Object.assign(article, res.data || {}) })
  request.get(`/article/comments/${articleId}`).then(res => { if (res.code === '200') comments.splice(0,comments.length,...(res.data||[])) })
  if (user.id) {
    request.get('/article/liked',{params:{articleId,userId:user.id}}).then(res=>liked.value=!!res.data)
    request.get('/article/collected',{params:{articleId,userId:user.id}}).then(res=>collected.value=!!res.data)
  }
}
const requireLogin = () => { if (!user.id) { ElMessage.warning('请先登录'); router.push('/login'); return false } return true }
const like = () => { if (!requireLogin()) return; request.post('/article/like',null,{params:{articleId,userId:user.id}}).then(res=>{liked.value=res.data; load()}) }
const collect = () => { if (!requireLogin()) return; request.post('/article/collect',null,{params:{articleId,userId:user.id}}).then(res=>{collected.value=res.data; load()}) }
const share = async () => { await navigator.clipboard?.writeText(location.href); ElMessage.success('链接已复制') }
const submitComment = () => {
  if (!requireLogin()) return
  if (!commentText.value.trim()) { ElMessage.warning('请输入评论'); return }
  request.post('/article/comment',{articleId:Number(articleId),userId:user.id,content:commentText.value.trim()}).then(res=>{ if(res.code==='200'){commentText.value='';load();ElMessage.success('评论成功')}else ElMessage.error(res.msg) })
}
onMounted(load)
</script>


<style scoped>
.article-hero { height: 530px; position: relative; color: #fff; overflow: hidden; }
.article-hero > img { width: 100%; height: 100%; object-fit: cover; }
.hero-overlay { position: absolute; inset: 0; background: linear-gradient(90deg, rgba(5,18,29,.9), rgba(5,18,29,.42), rgba(5,18,29,.2)); }
.article-head { position: absolute; left: 0; right: 0; bottom: 58px; }
.article-tag { display: inline-flex; background: rgba(255,255,255,.14); border: 1px solid rgba(255,255,255,.2); padding: 7px 13px; border-radius: 999px; font-size: 11px; backdrop-filter: blur(10px); }
.article-head h1 { font-family: var(--font-serif); max-width: 780px; font-size: 47px; line-height: 1.35; margin: 18px 0 15px; }
.article-head > p { max-width: 700px; color: rgba(255,255,255,.65); line-height: 1.8; }
.author { display: flex; align-items: center; gap: 10px; margin-top: 22px; }
.author img { width: 40px; height: 40px; border-radius: 50%; }
.author div { display: flex; flex-direction: column; gap: 4px; }
.author b { font-size: 13px; }
.author span { color: rgba(255,255,255,.55); font-size: 10px; }
.article-layout { display: grid; grid-template-columns: 1fr 300px; gap: 24px; margin-top: 34px; }
.article-main { padding: 52px 64px; }
.article-content { color: #374151; font-family: var(--font-serif); font-size: 17px; line-height: 2.2; }
.article-content :deep(p) { margin: 0 0 1.4em; }
.article-content :deep(blockquote) { margin: 30px 0; padding: 20px 26px; border-left: 3px solid var(--gold); background: #faf6ee; color: var(--ink); font-size: 20px; }
.article-actions { display: flex; gap: 10px; justify-content: center; margin-top: 44px; padding-top: 24px; border-top: 1px solid #eee9e0; }
.article-actions button { border: 1px solid var(--line); background: #fff; border-radius: 999px; padding: 9px 17px; color: var(--muted); cursor: pointer; display: flex; align-items: center; gap: 6px; }
.article-actions button.active { color: var(--primary); border-color: rgba(30,92,131,.3); background: #edf4f8; }
.article-side { display: flex; flex-direction: column; gap: 18px; }
.side-card { background: #fff; border: 1px solid var(--line); border-radius: 18px; padding: 22px; }
.side-card h3 { font-family: var(--font-serif); margin: 0 0 16px; }
.side-author { display: flex; align-items: center; gap: 10px; }
.side-author img { width: 46px; height: 46px; border-radius: 50%; }
.side-author div { display: flex; flex-direction: column; gap: 4px; }
.side-author span { color: var(--muted); font-size: 11px; }
.side-card p { color: var(--muted); line-height: 1.7; font-size: 12px; }
.comments-section { max-width: 850px; margin-top: 50px; }
.comment-title { display: flex; align-items: baseline; gap: 10px; }
.comment-title h2 { font-family: var(--font-serif); margin: 0; }
.comment-title span { color: var(--muted); font-size: 12px; }
.comment-compose { display: flex; gap: 12px; align-items: flex-start; margin-top: 18px; }
.comment-compose img, .comment-item img { width: 38px; height: 38px; border-radius: 50%; object-fit: cover; }
.comment-compose textarea { flex: 1; height: 70px; resize: vertical; border: 1px solid var(--line); border-radius: 12px; padding: 12px; outline: none; }
.comments-list { margin-top: 22px; }
.comment-item { display: flex; gap: 12px; padding: 18px 0; border-bottom: 1px solid var(--line); }
.comment-meta { display: flex; gap: 10px; align-items: baseline; }
.comment-meta b { font-size: 13px; }
.comment-meta span { color: var(--muted); font-size: 10px; }
.comment-item p { margin: 8px 0 0; line-height: 1.7; font-size: 13px; color: #4b5563; }
@media(max-width:900px){.article-layout{grid-template-columns:1fr}.article-side{display:none}.article-head h1{font-size:34px}.article-main{padding:34px 24px}}
</style>

