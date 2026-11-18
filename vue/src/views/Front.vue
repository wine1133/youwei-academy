<template>
  <div class="front-root">
    <div class="announcement">
      <span class="dot"></span>
      <span>有为书院 · 2026 阅读季</span>
      <span class="announcement-message">让每一次阅读，都成为理解世界的新入口</span>
    </div>

    <header class="front-header">
      <router-link to="/front/home" class="brand">
        <span class="brand-mark"><el-icon><Reading /></el-icon></span>
        <span class="brand-copy"><b>有为书院</b><small>YOUWEI ACADEMY</small></span>
      </router-link>

      <nav class="front-nav">
        <router-link to="/front/home" :class="{active: isActive('/front/home')}">首页</router-link>
        <router-link to="/front/library" :class="{active: isActive('/front/library') || isActive('/front/book')}">馆藏</router-link>
        <router-link to="/front/map" :class="{active: isActive('/front/map')}">书院地图</router-link>
        <router-link to="/front/community" :class="{active: isActive('/front/community') || isActive('/front/article')}">思想社区</router-link>
        <router-link to="/front/homework" :class="{active: isActive('/front/homework')}">学习任务</router-link>
        <router-link v-if="data.user.id" to="/front/person" :class="{active: isActive('/front/person')}">个人中心</router-link>
      </nav>

      <div class="header-actions">
        <button v-if="data.user.id" class="message-bell" title="消息中心" @click="router.push('/front/message')">
          <el-icon><Bell /></el-icon>
          <span v-if="messageCount">{{ messageCount > 99 ? '99+' : messageCount }}</span>
        </button>
        <template v-if="!data.user.id">
          <el-button text @click="router.push('/login')">登录</el-button>
          <el-button type="primary" round @click="router.push('/register')">加入书院</el-button>
        </template>
        <el-dropdown v-else trigger="click">
          <button class="user-pill">
            <img :src="data.user.avatar || '/images/avatars/student.svg'" alt="">
            <span>{{ data.user.name }}</span>
            <el-icon><ArrowDown /></el-icon>
          </button>
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item @click="router.push('/front/person')">个人中心</el-dropdown-item>
              <el-dropdown-item @click="router.push('/front/password')">修改密码</el-dropdown-item>
              <el-dropdown-item divided @click="logout">退出登录</el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </div>
    </header>

    <main class="front-main">
      <router-view @updateUser="updateUser" />
    </main>

    <footer class="front-footer">
      <div class="footer-inner">
        <div>
          <div class="footer-brand">有为书院 <span>YOUWEI ACADEMY</span></div>
          <p>阅读经典，连接思想，让学习真实发生。</p>
        </div>
        <div class="footer-links">
          <router-link to="/front/library">馆藏图书</router-link>
          <router-link to="/front/map">书院地图</router-link>
          <router-link to="/front/community">思想社区</router-link>
          <router-link to="/front/homework">学习任务</router-link>
        </div>
        <div class="footer-note">© 2026 Youwei Academy</div>
      </div>
    </footer>
  </div>
</template>

<script setup>
import { reactive, ref, onMounted, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import request from '@/utils/request'

const router = useRouter()
const route = useRoute()
const data = reactive({
  user: JSON.parse(localStorage.getItem('system-user') || '{}')
})

const messageCount = ref(0)
const isActive = (path) => route.path === path
const loadUnread = () => { if (!data.user.id) return; request.get('/message/unreadCount',{params:{userId:data.user.id,role:data.user.role}}).then(res=>{if(res.code==='200')messageCount.value=res.data.count||0}) }
 onMounted(loadUnread)
watch(() => route.path, loadUnread)
const updateUser = () => { data.user = JSON.parse(localStorage.getItem('system-user') || '{}') }
const logout = () => {
  localStorage.removeItem('system-user')
  data.user = {}
  ElMessage.success('已退出登录')
  router.push('/login')
}
</script>

<style scoped>
.front-root { min-height: 100vh; display: flex; flex-direction: column; }
.announcement { height: 34px; color: rgba(255,255,255,.82); background: var(--primary-deep); display: flex; align-items: center; justify-content: center; gap: 10px; font-size: 12px; letter-spacing: .03em; }
.dot { width: 6px; height: 6px; border-radius: 50%; background: var(--gold); box-shadow: 0 0 0 4px rgba(195,154,84,.18); }
.announcement-message { opacity: .6; }
.front-header { height: 78px; position: sticky; top: 0; z-index: 50; display: flex; align-items: center; padding: 0 max(28px, calc((100vw - 1240px)/2)); gap: 50px; background: rgba(247,244,238,.86); backdrop-filter: blur(18px); border-bottom: 1px solid rgba(232,227,218,.9); }
.brand { display: flex; align-items: center; gap: 12px; min-width: 190px; }
.brand-mark { width: 44px; height: 44px; border-radius: 14px; background: linear-gradient(145deg, var(--primary), var(--teal)); color: #fff; display: grid; place-items: center; font-size: 22px; box-shadow: 0 10px 24px rgba(30,92,131,.24); }
.brand-copy { display: flex; flex-direction: column; line-height: 1.1; }
.brand-copy b { font-family: var(--font-serif); font-size: 20px; letter-spacing: .08em; }
.brand-copy small { margin-top: 5px; color: var(--muted); font-size: 8px; letter-spacing: .18em; }
.front-nav { display: flex; align-items: center; gap: 32px; flex: 1; }
.front-nav a { position: relative; color: var(--ink-2); font-size: 14px; padding: 28px 0; }
.front-nav a::after { content: ''; position: absolute; left: 50%; bottom: 17px; width: 0; height: 2px; border-radius: 2px; background: var(--gold); transform: translateX(-50%); transition: width .22s ease; }
.front-nav a:hover, .front-nav a.active { color: var(--primary); }
.front-nav a.active::after { width: 22px; }
.header-actions { display: flex; align-items: center; gap: 8px; }.message-bell{position:relative;border:0;background:transparent;color:var(--ink-2);font-size:18px;width:36px;height:36px;border-radius:10px;cursor:pointer}.message-bell:hover{background:#fff;color:var(--primary)}.message-bell span{position:absolute;right:-2px;top:-1px;min-width:16px;height:16px;padding:0 4px;border-radius:999px;background:#bd4b4b;color:#fff;font-size:9px;line-height:16px}
.user-pill { border: 0; background: transparent; display: flex; align-items: center; gap: 9px; cursor: pointer; color: var(--ink); padding: 4px; }
.user-pill img { width: 38px; height: 38px; border-radius: 50%; object-fit: cover; border: 2px solid #fff; box-shadow: 0 4px 14px rgba(20,33,61,.12); }
.user-pill span { max-width: 100px; overflow: hidden; text-overflow: ellipsis; white-space: nowrap; font-size: 14px; }
.front-main { flex: 1; }
.front-footer { background: #0b1b2b; color: rgba(255,255,255,.68); margin-top: 60px; }
.footer-inner { max-width: 1240px; margin: 0 auto; padding: 46px 28px; display: flex; justify-content: space-between; gap: 40px; align-items: center; }
.footer-brand { color: #fff; font-family: var(--font-serif); font-size: 23px; letter-spacing: .08em; }
.footer-brand span { margin-left: 10px; color: rgba(255,255,255,.35); font-family: var(--font-sans); font-size: 9px; letter-spacing: .16em; }
.footer-inner p { margin: 10px 0 0; font-size: 13px; }
.footer-links { display: flex; gap: 28px; font-size: 13px; }
.footer-links a:hover { color: var(--gold); }
.footer-note { color: rgba(255,255,255,.35); font-size: 12px; }
@media (max-width: 900px) {
  .front-header { padding: 0 18px; gap: 16px; }
  .front-nav { display: none; }
  .brand { flex: 1; }
  .announcement-message { display: none; }
  .footer-inner { flex-direction: column; align-items: flex-start; }
}
</style>


