<template>
  <div class="manager-layout">
    <aside class="sidebar">
      <div class="side-brand"><span><el-icon><Reading /></el-icon></span><div><b>有为书院</b><small>ACADEMY CONSOLE</small></div></div>
      <div class="side-section">工作台</div>
      <el-menu router :default-active="route.path" class="side-menu">
        <el-menu-item index="/manager/home"><el-icon><DataBoard /></el-icon><span>数据总览</span></el-menu-item>
        <el-menu-item index="/manager/message"><el-icon><Bell /></el-icon><span>消息中心</span></el-menu-item>
        <el-menu-item index="/manager/book"><el-icon><Collection /></el-icon><span>馆藏管理</span></el-menu-item>
        <el-menu-item index="/manager/borrow"><el-icon><Tickets /></el-icon><span>借阅管理</span></el-menu-item>
        <el-menu-item index="/manager/article"><el-icon><Document /></el-icon><span>文章管理</span></el-menu-item>
        <el-menu-item index="/manager/comments"><el-icon><ChatLineRound /></el-icon><span>评论管理</span></el-menu-item>
        <el-menu-item index="/manager/homework"><el-icon><Notebook /></el-icon><span>作业管理</span></el-menu-item>
        <el-menu-item index="/manager/submit"><el-icon><Finished /></el-icon><span>提交批改</span></el-menu-item>
        <template v-if="isAdmin"><div class="side-section">组织与权限</div><el-menu-item index="/manager/user"><el-icon><Avatar /></el-icon><span>用户管理</span></el-menu-item><el-menu-item index="/manager/teacher"><el-icon><UserFilled /></el-icon><span>教师管理</span></el-menu-item><el-menu-item index="/manager/admin"><el-icon><Setting /></el-icon><span>管理员</span></el-menu-item></template>
        <div class="side-section">个人设置</div>
        <el-menu-item index="/manager/person"><el-icon><User /></el-icon><span>个人资料</span></el-menu-item>
        <el-menu-item index="/manager/password"><el-icon><Lock /></el-icon><span>修改密码</span></el-menu-item>
      </el-menu>
      <div class="side-bottom"><div class="side-user"><img :src="user.avatar || '/images/avatars/admin.jpg'" alt=""><div><b>{{ user.name }}</b><span>{{ roleText }}</span></div></div><button @click="logout"><el-icon><SwitchButton /></el-icon></button></div>
    </aside>
    <section class="manager-main">
      <header class="manager-top"><div><span class="crumb">有为书院</span><b>/</b><span>{{ currentTitle }}</span></div><div class="top-right"><button class="top-message" @click="router.push('/manager/message')"><el-icon><Bell /></el-icon><span v-if="messageCount">{{messageCount}}</span></button><span>{{ today }}</span><el-dropdown><button class="top-user"><img :src="user.avatar || '/images/avatars/admin.jpg'" alt=""><span>{{ user.name }}</span><el-icon><ArrowDown /></el-icon></button><template #dropdown><el-dropdown-menu><el-dropdown-item @click="router.push('/manager/person')">个人资料</el-dropdown-item><el-dropdown-item @click="router.push('/manager/password')">修改密码</el-dropdown-item><el-dropdown-item divided @click="logout">退出登录</el-dropdown-item></el-dropdown-menu></template></el-dropdown></div></header>
      <main class="manager-content"><router-view @updateUser="updateUser"/></main>
    </section>
  </div>
</template>

<script setup>
import { reactive, computed, watch, ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import request from '@/utils/request'
const route = useRoute(); const router = useRouter()
const user = reactive(JSON.parse(localStorage.getItem('system-user') || '{}'))
const isAdmin = computed(() => user.role === 'ADMIN')
const roleText = computed(() => user.role === 'ADMIN' ? '系统管理员' : user.role === 'TEACHER' ? '书院教师' : '书院读者')
const titles = { '/manager/home':'数据总览','/manager/book':'馆藏管理','/manager/borrow':'借阅管理','/manager/article':'文章管理','/manager/comments':'评论管理','/manager/homework':'作业管理','/manager/submit':'提交批改','/manager/user':'用户管理','/manager/teacher':'教师管理','/manager/admin':'管理员','/manager/person':'个人资料','/manager/password':'修改密码' }
const currentTitle = computed(() => titles[route.path] || '工作台')
const today = new Intl.DateTimeFormat('zh-CN',{year:'numeric',month:'long',day:'numeric',weekday:'long'}).format(new Date())
if (!user.id) { ElMessage.error('请先登录'); router.replace('/login') }
if (user.role === 'USER') router.replace('/front/home')
const messageCount = ref(0)
const loadUnread = () => { if (!user.id) return; request.get('/message/unreadCount',{params:{userId:user.id,role:user.role}}).then(res=>{if(res.code==='200')messageCount.value=res.data.count||0}) }
const updateUser = () => Object.assign(user, JSON.parse(localStorage.getItem('system-user') || '{}'))
watch(() => route.path, loadUnread)
onMounted(loadUnread)
const logout = () => { localStorage.removeItem('system-user'); router.push('/login') }
</script>

<style scoped>
.manager-layout{min-height:100vh;display:flex;background:#f2f4f7}.sidebar{width:248px;background:#102536;color:#fff;position:sticky;top:0;height:100vh;display:flex;flex-direction:column;padding:22px 14px;overflow:auto}.side-brand{display:flex;gap:11px;align-items:center;padding:0 10px 24px}.side-brand>span{width:40px;height:40px;border-radius:12px;background:linear-gradient(145deg,#d0ad70,#8f6d32);display:grid;place-items:center;font-size:19px}.side-brand div{display:flex;flex-direction:column;gap:4px}.side-brand b{font-family:var(--font-serif);font-size:18px;letter-spacing:.06em}.side-brand small{font-size:7px;color:rgba(255,255,255,.32);letter-spacing:.16em}.side-section{padding:17px 14px 7px;color:rgba(255,255,255,.27);font-size:9px;letter-spacing:.14em}.side-menu{border:0;background:transparent;flex:1}.side-menu :deep(.el-menu-item){height:44px;color:rgba(255,255,255,.6);border-radius:11px;margin:3px 0;padding-left:16px!important}.side-menu :deep(.el-menu-item:hover){background:rgba(255,255,255,.06);color:#fff}.side-menu :deep(.el-menu-item.is-active){background:linear-gradient(90deg,rgba(195,154,84,.24),rgba(255,255,255,.05));color:#fff}.side-menu :deep(.el-menu-item.is-active):before{content:'';position:absolute;left:0;width:3px;height:20px;border-radius:3px;background:var(--gold)}.side-menu :deep(.el-icon){margin-right:12px}.side-bottom{border-top:1px solid rgba(255,255,255,.08);padding:17px 4px 0;display:flex;align-items:center;gap:8px}.side-user{display:flex;align-items:center;gap:9px;flex:1;min-width:0}.side-user img{width:34px;height:34px;border-radius:50%;object-fit:cover}.side-user div{display:flex;flex-direction:column;gap:3px;min-width:0}.side-user b{font-size:12px;white-space:nowrap;overflow:hidden;text-overflow:ellipsis}.side-user span{font-size:9px;color:rgba(255,255,255,.4)}.side-bottom button{border:0;background:rgba(255,255,255,.06);color:rgba(255,255,255,.55);width:30px;height:30px;border-radius:8px;cursor:pointer}.manager-main{flex:1;min-width:0}.manager-top{height:66px;background:rgba(255,255,255,.92);border-bottom:1px solid #e8ebef;display:flex;justify-content:space-between;align-items:center;padding:0 28px;position:sticky;top:0;z-index:20;backdrop-filter:blur(12px)}.manager-top>div{display:flex;align-items:center;gap:9px}.crumb{color:#9099a5;font-size:12px}.manager-top b{color:#c3c8ce;font-weight:400}.manager-top div>span:last-child{font-size:13px;color:#334155}.top-right{gap:22px!important}.top-message{position:relative;border:0;background:#f4f6f8;color:#52677a;width:34px;height:34px;border-radius:10px;cursor:pointer}.top-message span{position:absolute;right:-5px;top:-5px;min-width:16px;height:16px;padding:0 4px;border-radius:999px;background:#bd4b4b;color:#fff;font-size:9px;line-height:16px}.top-right>span{font-size:11px;color:#8b95a1}.top-user{border:0;background:transparent;display:flex;align-items:center;gap:8px;cursor:pointer;color:#334155}.top-user img{width:34px;height:34px;border-radius:50%;object-fit:cover}.top-user span{font-size:12px}.manager-content{padding:24px 28px 48px;max-width:1600px;margin:0 auto}@media(max-width:800px){.sidebar{width:70px;padding:20px 8px}.side-brand div,.side-section,.side-menu :deep(.el-menu-item span),.side-user div,.side-bottom button{display:none}.side-menu :deep(.el-menu-item){justify-content:center;padding:0!important}.side-menu :deep(.el-icon){margin:0}.manager-content{padding:18px 14px}.top-right>span{display:none}}
</style>

