<template>
  <div>
    <section class="profile-hero">
      <div class="page-shell profile-inner">
        <div class="avatar-wrap">
          <img :src="user.avatar || '/images/avatars/student.svg'" alt="">
          <el-upload :action="uploadUrl" :show-file-list="false" :on-success="avatarSuccess"><button class="avatar-edit"><el-icon><Camera /></el-icon></button></el-upload>
        </div>
        <div class="profile-copy">
          <div class="eyebrow light">{{ roleText }}</div>
          <h1>{{ user.name }}</h1>
          <p>@{{ user.username }} · 在阅读中保持好奇，在思考中持续生长。</p>
          <div class="profile-stats"><div><b>{{ borrows.length }}</b><span>借阅记录</span></div><div><b>{{ favorites.length }}</b><span>我的收藏</span></div><div><b>{{ myArticles.length }}</b><span>我的文章</span></div></div>
        </div>
        <el-button round class="edit-btn" @click="editProfile">编辑资料</el-button>
      </div>
    </section>
    <div class="page-shell profile-body">
      <el-tabs v-model="activeTab" class="profile-tabs">
        <el-tab-pane label="我的借阅" name="borrow">
          <div v-if="borrows.length" class="record-list">
            <div v-for="b in borrows" :key="b.id" class="record-card card">
              <img :src="b.bookCover" alt=""><div class="record-main"><b>{{ b.bookName }}</b><span>借阅于 {{ date(b.borrowTime) }} · 应还 {{ date(b.dueTime) }}</span></div><el-tag :type="b.status==='RETURNED'?'info':'warning'">{{ b.status==='RETURNED'?'已归还':'借阅中' }}</el-tag><el-button v-if="b.status!=='RETURNED'" type="primary" plain round @click="returnBook(b.id)">归还</el-button></div>
          </div>
          <div v-else class="empty-state">还没有借阅记录，去馆藏里发现一本好书吧。</div>
        </el-tab-pane>
        <el-tab-pane label="我的收藏" name="collect">
          <div class="mini-article-grid"><article v-for="a in favorites" :key="a.id" class="mini-article" @click="router.push(`/front/article/${a.id}`)"><img :src="a.cover" alt=""><div><h3>{{ a.title }}</h3><p>{{ a.summary }}</p></div></article></div>
          <div v-if="!favorites.length" class="empty-state">还没有收藏文章。</div>
        </el-tab-pane>
        <el-tab-pane label="我的文章" name="article">
          <div class="mini-article-grid"><article v-for="a in myArticles" :key="a.id" class="mini-article" @click="router.push(`/front/article/${a.id}`)"><img :src="a.cover" alt=""><div><h3>{{ a.title }}</h3><p>{{ a.views }} 阅读 · {{ a.likes }} 喜欢</p></div></article></div>
          <div v-if="!myArticles.length" class="empty-state">还没有发布文章。<el-button text type="primary" @click="router.push('/front/publish')">写一篇</el-button></div>
        </el-tab-pane>
        <el-tab-pane label="作业提交" name="submit">
          <el-table :data="submits" stripe><el-table-column prop="homeworkTitle" label="作业"/><el-table-column prop="submitTime" label="提交时间" :formatter="(r,c,v)=>date(v)"/><el-table-column prop="score" label="分数"/><el-table-column prop="status" label="状态" :formatter="(r)=>r.status==='GRADED'?'已批改':'待批改'"/><el-table-column prop="comment" label="教师评语"/></el-table>
          <div v-if="!submits.length" class="empty-state">暂无作业提交记录。</div>
        </el-tab-pane>
      </el-tabs>
    </div>
    <el-dialog v-model="dialogVisible" title="编辑个人资料" width="480px"><el-form label-position="top"><el-form-item label="账号"><el-input v-model="form.username" disabled/></el-form-item><el-form-item label="姓名"><el-input v-model="form.name"/></el-form-item></el-form><template #footer><el-button @click="dialogVisible=false">取消</el-button><el-button type="primary" @click="saveProfile">保存</el-button></template></el-dialog>
  </div>
</template>

<script setup>
import { reactive, ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import request from '@/utils/request'
import { ElMessage, ElMessageBox } from 'element-plus'

const router = useRouter()
const user = reactive(JSON.parse(localStorage.getItem('system-user') || '{}'))
const uploadUrl = import.meta.env.VITE_BASE_URL + '/files/upload'
const activeTab = ref('borrow')
const data = reactive({ borrows:[], favorites:[], myArticles:[], submits:[] })
const dialogVisible = ref(false)
const form = reactive({ username:user.username, name:user.name })
const borrows = computed(() => data.borrows), favorites = computed(() => data.favorites), myArticles = computed(() => data.myArticles), submits = computed(() => data.submits)
const roleText = computed(() => user.role === 'TEACHER' ? 'TEACHER · 书院教师' : 'READER · 书院读者')
const api = () => user.role === 'TEACHER' ? '/teacher' : '/user'
const date = (v) => v ? String(v).replace('T',' ').slice(0,10) : ''
const load = () => {
  request.get('/borrow/selectAll',{params:{userId:user.id}}).then(res=>{if(res.code==='200')data.borrows=res.data||[]})
  request.get(`/article/myCollect/${user.id}`).then(res=>{if(res.code==='200')data.favorites=res.data||[]})
  request.get('/article/selectAll',{params:{userId:user.id}}).then(res=>{if(res.code==='200')data.myArticles=res.data||[]})
  request.get('/submit/selectAll',{params:{userId:user.id}}).then(res=>{if(res.code==='200')data.submits=res.data||[]})
}
const returnBook = (id) => ElMessageBox.confirm('归还后将增加该书可借库存，确认归还吗？','归还确认').then(()=>request.put(`/borrow/return/${id}`).then(res=>{if(res.code==='200'){ElMessage.success('归还成功');load()}else ElMessage.error(res.msg)}))
const avatarSuccess = (res) => { if(res.code==='200'){ user.avatar=res.data; saveProfile() } }
const editProfile = () => { form.name=user.name; dialogVisible.value=true }
const saveProfile = () => request.put(`${api()}/update`,{...user,name:form.name,password:''}).then(res=>{if(res.code==='200'){user.name=form.name;localStorage.setItem('system-user',JSON.stringify(user));ElMessage.success('资料已更新');dialogVisible.value=false}else ElMessage.error(res.msg)})
onMounted(load)
</script>

<style scoped>
.profile-hero { background:linear-gradient(120deg,#102638,#1c5459); color:#fff; padding:55px 0; }
.profile-inner{display:flex;align-items:center;gap:28px}.avatar-wrap{position:relative}.avatar-wrap>img{width:120px;height:120px;border-radius:50%;object-fit:cover;border:4px solid rgba(255,255,255,.25);box-shadow:0 20px 40px rgba(0,0,0,.25)}.avatar-edit{position:absolute;right:0;bottom:0;width:34px;height:34px;border-radius:50%;border:0;background:var(--gold);color:white;cursor:pointer}.profile-copy{flex:1}.profile-copy h1{font-family:var(--font-serif);font-size:38px;margin:8px 0}.profile-copy p{color:rgba(255,255,255,.58)}.profile-stats{display:flex;gap:34px;margin-top:22px}.profile-stats div{display:flex;flex-direction:column;gap:3px}.profile-stats b{font-size:22px;font-family:var(--font-serif)}.profile-stats span{color:rgba(255,255,255,.45);font-size:10px}.edit-btn{color:var(--primary-deep);background:#fff;border:0}.profile-tabs{margin-top:6px}.record-list{display:flex;flex-direction:column;gap:12px}.record-card{display:flex;align-items:center;gap:16px;padding:16px}.record-card img{width:58px;height:78px;border-radius:7px;object-fit:cover}.record-main{flex:1;display:flex;flex-direction:column;gap:7px}.record-main span{color:var(--muted);font-size:11px}.mini-article-grid{display:grid;grid-template-columns:1fr 1fr;gap:14px}.mini-article{display:flex;gap:14px;background:#fff;border:1px solid var(--line);border-radius:16px;padding:14px;cursor:pointer}.mini-article img{width:100px;height:64px;border-radius:9px;object-fit:cover}.mini-article h3{font-family:var(--font-serif);font-size:16px;margin:0 0 7px}.mini-article p{color:var(--muted);font-size:11px;margin:0}.profile-body{padding-top:32px}@media(max-width:700px){.profile-inner{align-items:flex-start;flex-wrap:wrap}.mini-article-grid{grid-template-columns:1fr}.record-card{flex-wrap:wrap}}
</style>

