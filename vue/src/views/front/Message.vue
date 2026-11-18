<template>
  <div :class="isManager ? 'manager-page' : ''">
    <section v-if="!isManager" class="message-hero"><div class="page-shell"><div class="eyebrow light">NOTIFICATIONS</div><h1>消息中心</h1><p>借阅、学习任务与批改结果，都会在这里与你相遇。</p></div></section>
    <div :class="isManager ? '' : 'page-shell message-body'">
      <div class="message-head"><div><div class="eyebrow">INBOX</div><h2>通知与提醒</h2><p>{{ unread }} 条未读消息</p></div><div class="message-actions"><el-button :type="tab==='all'?'primary':'default'" round @click="tab='all';load()">全部消息</el-button><el-button :type="tab==='unread'?'primary':'default'" round @click="tab='unread';load()">未读</el-button><el-button text @click="readAll">全部标为已读</el-button></div></div>
      <div v-if="messages.length" class="message-list"><article v-for="m in messages" :key="m.id" :class="['message-card',{unread:m.isRead===0}]" @click="openMessage(m)"><div class="message-icon" :class="'type-'+m.type?.toLowerCase()"><el-icon><Bell v-if="m.type==='SYSTEM'"/><Notebook v-else-if="m.type==='HOMEWORK'"/><Tickets v-else-if="m.type==='BORROW'"/><Finished v-else/></el-icon></div><div class="message-content"><div class="message-top"><h3>{{m.title}}</h3><span>{{date(m.createTime)}}</span></div><p>{{m.content}}</p><div class="message-foot"><span class="type-tag">{{typeText(m.type)}}</span><span v-if="m.isRead===0" class="unread-dot">未读</span></div></div><button class="delete-btn" @click.stop="del(m.id)"><el-icon><Delete /></el-icon></button></article></div>
      <div v-else class="empty-state"><el-icon size="42"><Bell /></el-icon><p>暂时没有消息</p></div>
    </div>
  </div>
</template>

<script setup>
import { reactive, ref, computed, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import request from '@/utils/request'
import { ElMessage, ElMessageBox } from 'element-plus'
const route = useRoute(), router = useRouter()
const isManager = computed(() => route.path.startsWith('/manager'))
const user = reactive(JSON.parse(localStorage.getItem('system-user') || '{}'))
const messages = reactive([]), unread = ref(0), tab = ref('all')
const date = v => v ? String(v).replace('T',' ').slice(0,16) : ''
const typeText = t => ({SYSTEM:'系统',HOMEWORK:'学习任务',SUBMIT:'作业提交',GRADE:'批改结果',BORROW:'借阅通知'}[t] || '通知')
const load = () => {
  request.get('/message/selectAll', { params: { userId:user.id, role:user.role, isRead:tab.value==='unread'?0:undefined } }).then(res => { if (res.code === '200') messages.splice(0,messages.length,...(res.data||[])) })
  request.get('/message/unreadCount', { params: { userId:user.id, role:user.role } }).then(res => { if (res.code === '200') unread.value = res.data.count || 0 })
}
const openMessage = (m) => {
  if (m.isRead === 0) request.put(`/message/read/${m.id}`).then(() => { m.isRead=1; unread.value=Math.max(0,unread.value-1) })
  if (m.type === 'HOMEWORK') router.push('/front/homework')
  else if (m.type === 'SUBMIT') router.push('/manager/submit')
  else if (m.type === 'GRADE' || m.type === 'BORROW') router.push('/front/person')
}
const readAll = () => request.put('/message/readAll', null, { params:{userId:user.id,role:user.role} }).then(() => { ElMessage.success('已全部标为已读'); load() })
const del = (id) => ElMessageBox.confirm('确认删除该消息？','提示').then(() => request.delete(`/message/delete/${id}`).then(() => { load(); ElMessage.success('已删除') }))
onMounted(load)
</script>

<style scoped>
.message-hero{background:linear-gradient(120deg,#102638,#1c5459);color:#fff;padding:55px 0 66px}.message-hero h1{font-family:var(--font-serif);font-size:44px;margin:12px 0}.message-hero p{color:rgba(255,255,255,.58)}.message-body{padding-top:32px}.message-head{display:flex;justify-content:space-between;align-items:end;margin-bottom:20px}.message-head h2{font-family:var(--font-serif);font-size:28px;margin:6px 0}.message-head p{color:var(--muted);font-size:12px}.message-actions{display:flex;gap:8px}.message-list{display:flex;flex-direction:column;gap:12px}.message-card{position:relative;display:flex;gap:16px;padding:20px;background:#fff;border:1px solid var(--line);border-radius:18px;box-shadow:0 8px 24px rgba(20,33,61,.04);cursor:pointer;transition:all .2s}.message-card:hover{transform:translateY(-2px);box-shadow:var(--shadow)}.message-card.unread{border-left:4px solid var(--gold);background:#fffdf8}.message-icon{width:48px;height:48px;border-radius:14px;display:grid;place-items:center;font-size:22px;background:#e8f1f5;color:var(--primary);flex-shrink:0}.message-icon.type-homework{background:#f3eadb;color:#a57427}.message-icon.type-borrow{background:#e5f2ed;color:var(--teal)}.message-icon.type-grade{background:#efe8f6;color:#7a5da0}.message-content{flex:1}.message-top{display:flex;justify-content:space-between;gap:12px}.message-top h3{font-family:var(--font-serif);font-size:17px;margin:0}.message-top span{font-size:10px;color:#98a1aa;white-space:nowrap}.message-content p{color:var(--muted);font-size:13px;line-height:1.7;margin:9px 0 12px}.message-foot{display:flex;gap:8px;align-items:center}.type-tag{font-size:9px;padding:4px 8px;border-radius:999px;background:#f1f4f6;color:#6b7b88}.unread-dot{font-size:9px;color:#b57d28}.delete-btn{position:absolute;right:12px;bottom:12px;border:0;background:transparent;color:#aab1b8;cursor:pointer}.delete-btn:hover{color:var(--danger)}@media(max-width:700px){.message-head{align-items:flex-start;flex-direction:column;gap:16px}.message-actions{flex-wrap:wrap}.message-top{flex-direction:column;gap:5px}}
</style>
