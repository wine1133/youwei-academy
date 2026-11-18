<template>
  <div>
    <section class="homework-hero">
      <div class="page-shell">
        <div class="eyebrow light">LEARNING TASKS</div>
        <h1>把知识变成能力</h1>
        <p>阅读之后的思考、练习和表达，才是真正属于你的学习成果。</p>
        <div class="hero-stats"><div><b>{{ total }}</b><span>学习任务</span></div><div><b>{{ submittedCount }}</b><span>已提交</span></div><div><b>{{ pendingCount }}</b><span>待完成</span></div></div>
      </div>
    </section>
    <div class="page-shell">
      <div class="homework-cards">
        <article v-for="task in tasks" :key="task.id" class="task-card card">
          <div class="task-top"><span class="task-no">TASK {{ String(task.id).padStart(2,'0') }}</span><el-tag :type="submitted(task.id) ? 'success':'warning'" effect="plain">{{ submitted(task.id) ? '已提交':'待完成' }}</el-tag></div>
          <h2>{{ task.title }}</h2>
          <p>{{ task.content }}</p>
          <div class="task-info"><span><el-icon><User /></el-icon>{{ task.teacherName || '书院教师' }}</span><span><el-icon><Clock /></el-icon>{{ formatDate(task.deadline) }} 截止</span><span><el-icon><Upload /></el-icon>{{ task.submitCount || 0 }} 人提交</span></div>
          <div class="task-bottom">
            <div class="mini-progress"><i :style="{width: submitted(task.id) ? '100%' : '12%'}"></i></div>
            <el-button v-if="!submitted(task.id) && user.role === 'USER'" type="primary" round @click="openSubmit(task)">提交作业</el-button>
            <el-button v-else-if="submitted(task.id)" text type="success">已完成</el-button>
            <el-button v-else type="primary" round @click="router.push('/manager/submit')">进入批改</el-button>
          </div>
        </article>
      </div>
    </div>
    <el-dialog v-model="dialogVisible" title="提交作业" width="560px">
      <div class="submit-task-name">{{ selected.title }}</div>
      <el-form label-position="top">
        <el-form-item label="作业说明"><el-input v-model="form.content" type="textarea" :rows="5" placeholder="写下你的思路、收获或补充说明"></el-input></el-form-item>
        <el-form-item label="附件"><el-upload :action="uploadUrl" :on-success="uploadSuccess" :limit="1"><el-button>上传文件</el-button></el-upload></el-form-item>
      </el-form>
      <template #footer><el-button @click="dialogVisible=false">取消</el-button><el-button type="primary" @click="submit">确认提交</el-button></template>
    </el-dialog>
  </div>
</template>

<script setup>
import { reactive, ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import request from '@/utils/request'
import { ElMessage } from 'element-plus'

const router = useRouter()
const user = reactive(JSON.parse(localStorage.getItem('system-user') || '{}'))
const data = reactive({ tasks: [], submits: [], total: 0 })
const tasks = computed(() => data.tasks)
const dialogVisible = ref(false)
const selected = reactive({})
const form = reactive({ content:'', file:'' })
const uploadUrl = import.meta.env.VITE_BASE_URL + '/files/upload'
const submittedCount = computed(() => new Set(data.submits.map(s=>s.homeworkId)).size)
const pendingCount = computed(() => Math.max(0, data.total - submittedCount.value))
const submitted = (id) => data.submits.some(s=>s.homeworkId===id)
const formatDate = (v) => v ? String(v).replace('T',' ').slice(0,10) : '长期'
const load = () => {
  request.get('/homework/selectPage',{params:{pageNum:1,pageSize:50}}).then(res=>{if(res.code==='200'){data.tasks=res.data.list||[];data.total=res.data.total||0}})
  if(user.id) request.get('/submit/selectAll',{params:{userId:user.id}}).then(res=>{if(res.code==='200')data.submits=res.data||[]})
}
const openSubmit = (task) => { Object.assign(selected,task); form.content=''; form.file=''; dialogVisible.value=true }
const uploadSuccess = (res) => { if(res.code==='200') form.file=res.data }
const submit = () => { request.post('/submit/add',{homeworkId:selected.id,userId:user.id,content:form.content,file:form.file}).then(res=>{if(res.code==='200'){ElMessage.success('作业提交成功');dialogVisible.value=false;load()}else ElMessage.error(res.msg)}) }
onMounted(load)
</script>


<style scoped>
.homework-hero { background: linear-gradient(120deg,#132d3f,#1e5957); color:#fff; padding:55px 0 64px; }
.homework-hero h1 { font-family:var(--font-serif); font-size:44px; margin:12px 0; }
.homework-hero > .page-shell > p { color:rgba(255,255,255,.58); max-width:650px; line-height:1.9; }
.hero-stats { display:flex; gap:50px; margin-top:30px; }
.hero-stats div { display:flex; flex-direction:column; gap:5px; }
.hero-stats b { font-family:var(--font-serif); font-size:28px; }.hero-stats span { color:rgba(255,255,255,.45); font-size:11px; }
.homework-cards { display:grid; grid-template-columns:1fr 1fr; gap:22px; margin-top:32px; }
.task-card { padding:28px; }
.task-top { display:flex; justify-content:space-between; align-items:center; }
.task-no { color:var(--gold); font-size:10px; letter-spacing:.16em; }
.task-card h2 { font-family:var(--font-serif); font-size:25px; margin:16px 0 12px; }
.task-card > p { color:var(--muted); line-height:1.8; font-size:13px; min-height:68px; }
.task-info { display:flex; gap:18px; flex-wrap:wrap; color:#7d8995; font-size:11px; margin-top:18px; }
.task-info span { display:flex; align-items:center; gap:5px; }
.task-bottom { display:flex; align-items:center; gap:16px; margin-top:22px; padding-top:18px; border-top:1px solid #f0ece5; }
.mini-progress { flex:1; height:5px; border-radius:5px; background:#eee9e1; overflow:hidden; }.mini-progress i { display:block; height:100%; background:linear-gradient(90deg,var(--gold),var(--teal)); transition:width .4s; }
.submit-task-name { font-family:var(--font-serif); font-size:20px; margin-bottom:18px; color:var(--primary-deep); }
@media(max-width:800px){.homework-cards{grid-template-columns:1fr}.homework-hero h1{font-size:35px}}
</style>


