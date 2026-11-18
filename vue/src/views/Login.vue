<template>
  <div class="auth-page">
    <section class="auth-visual">
      <div class="visual-grid"></div>
      <div class="visual-copy">
        <router-link to="/front/home" class="auth-brand"><span><el-icon><Reading /></el-icon></span><div><b>有为书院</b><small>YOUWEI ACADEMY</small></div></router-link>
        <div class="visual-main"><div class="eyebrow light">READ · THINK · CREATE</div><h1>让阅读成为<br><em>理解世界</em>的方式</h1><p>一座为学习者而生的数字书院。<br>在这里，书籍、思想和伙伴彼此连接。</p></div>
        <div class="visual-quote">“ 书籍并不是把外在的东西给予我们，而是促使我们内在的东西苏醒。 ”</div>
      </div>
      <div class="visual-book"><img src="/images/covers/book-01.svg" alt=""></div>
    </section>
    <section class="auth-panel">
      <div class="auth-box">
        <div class="auth-heading"><div class="eyebrow">WELCOME BACK</div><h2>登录有为书院</h2><p>继续你的阅读与学习旅程。</p></div>
        <el-form :model="form" ref="formRef" :rules="rules" label-position="top">
          <el-form-item label="身份" prop="role"><el-select v-model="form.role" size="large" style="width:100%"><el-option value="USER" label="书院读者"/><el-option value="TEACHER" label="书院教师"/><el-option value="ADMIN" label="系统管理员"/></el-select></el-form-item>
          <el-form-item label="账号" prop="username"><el-input v-model="form.username" size="large" placeholder="请输入账号" :prefix-icon="User"/></el-form-item>
          <el-form-item label="密码" prop="password"><el-input v-model="form.password" size="large" show-password placeholder="请输入密码" :prefix-icon="Lock" @keyup.enter="login"/></el-form-item>
          <el-button type="primary" size="large" round class="login-btn" :loading="loading" @click="login">进入书院</el-button>
        </el-form>
        <div class="auth-switch">还没有账号？<router-link to="/register">创建一个</router-link></div>
        <div class="demo-accounts"><span>DEMO</span><button @click="useDemo('admin','admin','ADMIN')">管理员</button><button @click="useDemo('teacher','123456','TEACHER')">教师</button><button @click="useDemo('user','123456','USER')">读者</button></div>
      </div>
      <div class="auth-footer">© 2026 有为书院 · Youwei Academy</div>
    </section>
  </div>
</template>
<script setup>
import {reactive,ref} from 'vue';import {useRouter} from 'vue-router';import {User,Lock} from '@element-plus/icons-vue';import request from '@/utils/request';import {ElMessage} from 'element-plus'
const router=useRouter(),formRef=ref(),loading=ref(false);const form=reactive({username:'',password:'',role:'USER'});const rules={username:[{required:true,message:'请输入账号',trigger:'blur'}],password:[{required:true,message:'请输入密码',trigger:'blur'}]};const useDemo=(u,p,r)=>{form.username=u;form.password=p;form.role=r};const login=()=>formRef.value.validate(valid=>{if(!valid)return;loading.value=true;request.post('/login',form).then(res=>{if(res.code==='200'&&res.data){localStorage.setItem('system-user',JSON.stringify(res.data));ElMessage.success('欢迎回来，'+res.data.name);router.push(res.data.role==='USER'?'/front/home':'/manager/home')}else ElMessage.error(res.msg)}).finally(()=>loading.value=false)})
</script>
<style scoped>
.auth-page{min-height:100vh;display:grid;grid-template-columns:1.1fr .9fr;background:#f7f4ee}.auth-visual{position:relative;overflow:hidden;background:linear-gradient(135deg,#091d2d,#113e50 58%,#27665f);color:#fff;padding:48px 60px;display:flex;flex-direction:column}.visual-grid{position:absolute;inset:0;background-image:linear-gradient(rgba(255,255,255,.07) 1px,transparent 1px),linear-gradient(90deg,rgba(255,255,255,.07) 1px,transparent 1px);background-size:72px 72px;mask-image:linear-gradient(to bottom right,black,transparent 80%)}.auth-visual:after{content:'';position:absolute;width:500px;height:500px;border-radius:50%;right:-180px;top:-220px;background:radial-gradient(circle,rgba(195,154,84,.3),transparent 70%)}.visual-copy{position:relative;z-index:2;display:flex;flex-direction:column;height:100%}.auth-brand{display:flex;align-items:center;gap:12px}.auth-brand>span{width:44px;height:44px;border-radius:14px;background:rgba(255,255,255,.12);border:1px solid rgba(255,255,255,.18);display:grid;place-items:center;font-size:22px}.auth-brand div{display:flex;flex-direction:column;gap:5px}.auth-brand b{font-family:var(--font-serif);font-size:20px;letter-spacing:.08em}.auth-brand small{font-size:8px;letter-spacing:.18em;color:rgba(255,255,255,.38)}.visual-main{margin:auto 0;max-width:600px;padding-bottom:60px}.visual-main h1{font-family:var(--font-serif);font-size:58px;line-height:1.28;margin:17px 0 22px;font-weight:600}.visual-main em{font-style:normal;color:#e1c78f}.visual-main p{color:rgba(255,255,255,.57);line-height:2}.visual-quote{font-family:var(--font-serif);color:rgba(255,255,255,.44);font-size:13px;max-width:520px;border-top:1px solid rgba(255,255,255,.12);padding-top:22px}.visual-book{position:absolute;right:5%;bottom:12%;width:190px;transform:rotate(8deg);filter:drop-shadow(0 30px 40px rgba(0,0,0,.35));z-index:1;opacity:.9}.visual-book img{width:100%;border-radius:15px}.auth-panel{display:flex;flex-direction:column;justify-content:center;align-items:center;padding:40px;position:relative}.auth-box{width:min(410px,100%)}.auth-heading{margin-bottom:30px}.auth-heading h2{font-family:var(--font-serif);font-size:34px;margin:10px 0}.auth-heading p{color:var(--muted);font-size:13px}.login-btn{width:100%;margin-top:8px}.auth-switch{text-align:center;color:var(--muted);font-size:12px;margin-top:22px}.auth-switch a{color:var(--primary);font-weight:700}.demo-accounts{display:flex;align-items:center;gap:7px;justify-content:center;margin-top:26px;padding-top:20px;border-top:1px solid var(--line)}.demo-accounts span{font-size:8px;letter-spacing:.16em;color:#a4abb4}.demo-accounts button{border:1px solid var(--line);background:#fff;border-radius:999px;padding:5px 10px;font-size:10px;color:var(--muted);cursor:pointer}.demo-accounts button:hover{color:var(--primary);border-color:#b9cedb}.auth-footer{position:absolute;bottom:24px;color:#a8afb8;font-size:9px;letter-spacing:.08em}@media(max-width:900px){.auth-page{grid-template-columns:1fr}.auth-visual{display:none}.auth-panel{min-height:100vh;background:linear-gradient(160deg,#f7f4ee,#e9efe9)}}
</style>
