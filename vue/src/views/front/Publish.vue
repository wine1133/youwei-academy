<template>
  <div class="page-shell publish-page">
    <div class="publish-aside">
      <div class="eyebrow">WRITE & SHARE</div>
      <h1>把思考<br>写下来</h1>
      <p>写作不是把答案交给别人，而是让自己真正理解。分享一个观点、一段阅读体验，或一个仍在生长中的问题。</p>
      <div class="writing-tips">
        <div><span>01</span>从一个真实的问题开始</div>
        <div><span>02</span>用具体经验和细节支撑</div>
        <div><span>03</span>给读者留下继续思考的空间</div>
      </div>
    </div>
    <div class="publish-card card">
      <div class="page-heading"><div><h1>发布文章</h1><p>让你的文字成为一次真诚的交流。</p></div></div>
      <el-form :model="form" label-position="top">
        <el-form-item label="文章标题"><el-input v-model="form.title" size="large" placeholder="给文章一个有力量的标题"></el-input></el-form-item>
        <div class="form-grid">
          <el-form-item label="分类"><el-select v-model="form.category" style="width:100%"><el-option v-for="c in categories" :key="c" :label="c" :value="c"/></el-select></el-form-item>
          <el-form-item label="标签"><el-input v-model="form.tags" placeholder="用逗号分隔，例如：阅读,成长"></el-input></el-form-item>
        </div>
        <el-form-item label="摘要"><el-input v-model="form.summary" type="textarea" :rows="2" maxlength="150" show-word-limit placeholder="用一两句话介绍文章"></el-input></el-form-item>
        <el-form-item label="选择封面">
          <div class="cover-options"><button v-for="(cover,i) in covers" :key="cover" type="button" :class="{selected: form.cover === cover}" @click="form.cover = cover"><img :src="cover" alt=""><span>封面 {{ i+1 }}</span></button></div>
        </el-form-item>
        <el-form-item label="正文"><el-input v-model="form.content" type="textarea" :rows="12" placeholder="从你的问题、感受或故事开始……"></el-input></el-form-item>
        <div class="form-actions"><el-button @click="router.back()">取消</el-button><el-button type="primary" size="large" round @click="submit">发布文章</el-button></div>
      </el-form>
    </div>
  </div>
</template>

<script setup>
import { reactive } from 'vue'
import { useRouter } from 'vue-router'
import request from '@/utils/request'
import { ElMessage } from 'element-plus'

const router = useRouter()
const user = JSON.parse(localStorage.getItem('system-user') || '{}')
const categories = ['阅读方法','效率工具','主题书单','教学随笔','技术思考']
const covers = ['/images/covers/article-01.svg','/images/covers/article-02.svg','/images/covers/article-03.svg','/images/covers/article-04.svg']
const form = reactive({ title:'', category:'阅读方法', tags:'', summary:'', content:'', cover:covers[0], userId:user.id })
const submit = () => {
  if (!user.id) { ElMessage.warning('请先登录'); router.push('/login'); return }
  if (!form.title.trim() || !form.content.trim()) { ElMessage.warning('标题和正文不能为空'); return }
  request.post('/article/add', { ...form, content: form.content.split(/\n+/).map(p=>`<p>${p}</p>`).join('') }).then(res=>{
    if(res.code==='200'){ElMessage.success('文章发布成功');router.push('/front/community')}else ElMessage.error(res.msg)
  })
}
</script>

<style scoped>
.publish-page { display: grid; grid-template-columns: 320px 1fr; gap: 48px; align-items: start; padding-top: 50px; }
.publish-aside { position: sticky; top: 120px; padding: 24px 6px; }
.publish-aside h1 { font-family: var(--font-serif); font-size: 52px; line-height: 1.3; margin: 16px 0 20px; }
.publish-aside > p { color: var(--muted); line-height: 1.9; font-size: 14px; }
.writing-tips { display: flex; flex-direction: column; gap: 14px; margin-top: 40px; color: #687785; font-size: 12px; }
.writing-tips div { display: flex; gap: 12px; align-items: center; }
.writing-tips span { color: var(--gold); font-family: var(--font-serif); }
.publish-card { padding: 34px; }
.publish-card .page-heading h1 { font-size: 28px; }
.cover-options { display: grid; grid-template-columns: repeat(4,1fr); gap: 10px; width: 100%; }
.cover-options button { border: 2px solid transparent; background: #eee9e0; border-radius: 12px; padding: 0; overflow: hidden; cursor: pointer; position: relative; }
.cover-options button.selected { border-color: var(--gold); box-shadow: 0 0 0 2px rgba(195,154,84,.2); }
.cover-options img { width: 100%; height: 86px; object-fit: cover; display: block; }
.cover-options span { display: block; font-size: 10px; color: var(--muted); padding: 5px; }
.form-actions { display: flex; justify-content: flex-end; gap: 12px; }
@media(max-width:900px){.publish-page{grid-template-columns:1fr}.publish-aside{position:static}.cover-options{grid-template-columns:1fr 1fr}}
</style>
