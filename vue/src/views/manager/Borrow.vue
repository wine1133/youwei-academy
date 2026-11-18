<template>
  <div class="manager-page">
    <div class="page-heading"><div><div class="eyebrow">BORROWING</div><h1>借阅管理</h1><p>查看借阅动态，处理归还与到期记录。</p></div></div>
    <div class="card table-card"><div class="toolbar"><el-input v-model="query.bookName" clearable placeholder="搜索图书" style="width:240px" @keyup.enter="search"><template #prefix><el-icon><Search /></el-icon></template></el-input><el-select v-model="query.status" clearable placeholder="全部状态" style="width:150px" @change="search"><el-option label="借阅中" value="BORROWED"/><el-option label="已归还" value="RETURNED"/></el-select><div class="grow"></div><el-tag type="warning" effect="plain">当前借阅 {{ activeCount }} 本</el-tag></div>
      <el-table :data="rows" stripe><el-table-column label="图书" min-width="220"><template #default="{row}"><div class="book-cell"><img :src="row.bookCover"/><div><b>{{row.bookName}}</b><span>借阅编号 #{{row.id}}</span></div></div></template></el-table-column><el-table-column label="借阅人" min-width="150"><template #default="{row}"><div class="user-cell"><img :src="row.userAvatar || '/images/avatars/student.svg'"/><span>{{row.userName}}</span></div></template></el-table-column><el-table-column label="借阅时间" width="150"><template #default="{row}">{{date(row.borrowTime)}}</template></el-table-column><el-table-column label="应还时间" width="150"><template #default="{row}"><span :class="{overdue:isOverdue(row)}">{{date(row.dueTime)}}</span></template></el-table-column><el-table-column label="状态" width="100"><template #default="{row}"><el-tag :type="row.status==='RETURNED'?'info':isOverdue(row)?'danger':'success'">{{row.status==='RETURNED'?'已归还':isOverdue(row)?'已逾期':'借阅中'}}</el-tag></template></el-table-column><el-table-column label="操作" width="110" align="center"><template #default="{row}"><el-button v-if="row.status!=='RETURNED'" type="primary" plain round @click="returnBook(row.id)">归还</el-button><span v-else class="text-muted">已完成</span></template></el-table-column></el-table>
      <div class="pagination-wrap"><el-pagination background layout="prev,pager,next" :total="total" :page-size="query.pageSize" v-model:current-page="query.pageNum" @current-change="load"/></div>
    </div>
  </div>
</template>
<script setup>
import { reactive,computed,onMounted } from 'vue';import request from '@/utils/request';import {ElMessage,ElMessageBox} from 'element-plus'
const query=reactive({bookName:'',status:'',pageNum:1,pageSize:8});const data=reactive({rows:[],total:0,activeCount:0});const rows=computed(()=>data.rows),total=computed(()=>data.total),activeCount=computed(()=>data.activeCount)
const date=v=>v?String(v).replace('T',' ').slice(0,16):'';const isOverdue=r=>r.status==='BORROWED'&&r.dueTime&&new Date(r.dueTime)<new Date()
const load=()=>request.get('/borrow/selectPage',{params:query}).then(res=>{if(res.code==='200'){data.rows=res.data.list||[];data.total=res.data.total||0}})
const loadStats=()=>request.get('/dashboard/stats').then(res=>{if(res.code==='200')data.activeCount=res.data.activeBorrow||0})
const search=()=>{query.pageNum=1;load()};const returnBook=id=>ElMessageBox.confirm('确认归还该图书？','归还确认').then(()=>request.put('/borrow/return/'+id).then(res=>{if(res.code==='200'){ElMessage.success('归还成功');load();loadStats()}else ElMessage.error(res.msg)}));onMounted(()=>{load();loadStats()})
</script>
<style scoped>.book-cell,.user-cell{display:flex;align-items:center;gap:10px}.book-cell img{width:34px;height:48px;border-radius:5px;object-fit:cover}.book-cell div{display:flex;flex-direction:column;gap:4px}.book-cell span{font-size:10px;color:var(--muted)}.user-cell img{width:30px;height:30px;border-radius:50%;object-fit:cover}.overdue{color:#c14c4c;font-weight:600}</style>

