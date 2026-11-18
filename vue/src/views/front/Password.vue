<template>
  <div class="page-shell password-page">
    <section class="password-intro"><div class="eyebrow light">SECURITY</div><h1>守护你的<br>阅读空间</h1><p>定期更新密码，保护账号与学习记录安全。修改成功后需要重新登录。</p></section>
    <section class="password-card card">
      <div class="page-heading"><div><h1>修改密码</h1><p>请输入原密码并设置一个新密码。</p></div></div>
      <el-form :model="form" label-position="top">
        <el-form-item label="原密码"><el-input v-model="form.password" show-password size="large" placeholder="请输入原密码"/></el-form-item>
        <el-form-item label="新密码"><el-input v-model="form.newPassword" show-password size="large" placeholder="至少 6 位字符"/></el-form-item>
        <el-form-item label="确认新密码"><el-input v-model="form.confirmPassword" show-password size="large" placeholder="再次输入新密码"/></el-form-item>
        <el-button type="primary" size="large" round style="width:100%" @click="save">确认修改</el-button>
      </el-form>
    </section>
  </div>
</template>

<script setup>
import { reactive } from 'vue'
import { useRouter } from 'vue-router'
import request from '@/utils/request'
import { ElMessage } from 'element-plus'
const router = useRouter()
const user = JSON.parse(localStorage.getItem('system-user') || '{}')
const form = reactive({ password:'', newPassword:'', confirmPassword:'' })
const save = () => {
  if(!form.password||!form.newPassword) return ElMessage.warning('请完整填写密码')
  if(form.newPassword.length<6) return ElMessage.warning('新密码至少 6 位')
  if(form.newPassword!==form.confirmPassword) return ElMessage.error('两次密码不一致')
  request.put('/updatePassword',{...user,password:form.password,newPassword:form.newPassword}).then(res=>{
    if(res.code==='200'){ElMessage.success('密码修改成功，请重新登录');localStorage.removeItem('system-user');router.push('/login')}else ElMessage.error(res.msg)
  })
}
</script>

<style scoped>
.password-page{display:grid;grid-template-columns:1fr 520px;gap:60px;align-items:center;padding-top:60px}.password-intro{background:linear-gradient(135deg,#102638,#1c5459);color:#fff;border-radius:26px;padding:54px;min-height:420px;display:flex;flex-direction:column;justify-content:center}.password-intro h1{font-family:var(--font-serif);font-size:46px;line-height:1.35;margin:15px 0 20px}.password-intro p{color:rgba(255,255,255,.58);line-height:1.9;max-width:430px}.password-card{padding:36px}.password-card .page-heading h1{font-size:30px}@media(max-width:850px){.password-page{grid-template-columns:1fr}.password-intro{min-height:280px;padding:32px}}
</style>
