<template>
  <div class="manager-page">
    <div class="page-heading"><div><div class="eyebrow">COLLECTION MANAGEMENT</div><h1>馆藏管理</h1><p>维护书院馆藏、库存与在线阅读章节。</p></div><el-button type="primary" round @click="handleAdd"><el-icon><Plus /></el-icon>新增图书</el-button></div>
    <div class="card table-card">
      <div class="toolbar"><el-input v-model="query.name" clearable placeholder="搜索书名或作者" style="width:280px" @keyup.enter="search"><template #prefix><el-icon><Search /></el-icon></template></el-input><el-select v-model="query.category" clearable placeholder="全部分类" style="width:140px" @change="search"><el-option v-for="c in categories" :key="c" :label="c" :value="c"/></el-select><div class="grow"></div><span class="text-muted">共 {{ total }} 本馆藏</span></div>
      <el-table :data="rows" stripe>
        <el-table-column label="图书" min-width="260"><template #default="{row}"><div class="book-cell"><img :src="row.cover"/><div><b>{{row.name}}</b><span>{{row.author}}</span></div></div></template></el-table-column>
        <el-table-column prop="category" label="分类" width="90"/><el-table-column prop="publisher" label="出版社" min-width="140"/><el-table-column label="库存" width="100"><template #default="{row}"><b>{{row.stock}}</b><span class="muted"> / {{row.total}}</span></template></el-table-column><el-table-column label="评分" width="80"><template #default="{row}"><span class="gold">★ {{row.score}}</span></template></el-table-column><el-table-column label="状态" width="90"><template #default="{row}"><el-tag :type="row.stock>0?'success':'danger'" effect="plain">{{row.stock>0?'可借':'无库存'}}</el-tag></template></el-table-column>
        <el-table-column label="操作" width="230" align="center"><template #default="{row}"><el-button text type="primary" @click="manageChapters(row)">章节</el-button><el-button text type="primary" @click="handleEdit(row)">编辑</el-button><el-button text type="danger" @click="del(row.id)">删除</el-button></template></el-table-column>
      </el-table>
      <div class="pagination-wrap"><el-pagination background layout="prev,pager,next" :total="total" :page-size="query.pageSize" v-model:current-page="query.pageNum" @current-change="load"/></div>
    </div>

    <el-dialog v-model="formVisible" :title="form.id?'编辑图书':'新增图书'" width="680px">
      <el-form :model="form" label-position="top"><div class="form-grid">
        <el-form-item label="书名"><el-input v-model="form.name"/></el-form-item><el-form-item label="作者"><el-input v-model="form.author"/></el-form-item>
        <el-form-item label="分类"><el-select v-model="form.category" style="width:100%"><el-option v-for="c in categories" :key="c" :label="c" :value="c"/></el-select></el-form-item><el-form-item label="出版社"><el-input v-model="form.publisher"/></el-form-item>
        <el-form-item label="ISBN"><el-input v-model="form.isbn"/></el-form-item><el-form-item label="评分"><el-input-number v-model="form.score" :min="0" :max="10" :step="0.1" style="width:100%"/></el-form-item>
        <el-form-item label="馆藏总数"><el-input-number v-model="form.total" :min="1" style="width:100%"/></el-form-item><el-form-item label="可借库存"><el-input-number v-model="form.stock" :min="0" :max="form.total||999" style="width:100%"/></el-form-item>
        <el-form-item label="封面" class="full-span"><div class="cover-upload"><img v-if="form.cover" :src="form.cover"/><el-upload :action="uploadUrl" :show-file-list="false" :on-success="uploadSuccess"><el-button><el-icon><Upload /></el-icon>上传封面</el-button></el-upload></div></el-form-item>
        <el-form-item label="简介" class="full-span"><el-input v-model="form.description" type="textarea" :rows="4"/></el-form-item>
      </div></el-form><template #footer><el-button @click="formVisible=false">取消</el-button><el-button type="primary" @click="save">保存</el-button></template>
    </el-dialog>

    <el-drawer v-model="chapterVisible" :title="`章节管理 · ${currentBook.name||''}`" size="560px">
      <div class="chapter-toolbar"><el-button type="primary" @click="newChapter"><el-icon><Plus /></el-icon>新增章节</el-button></div>
      <div class="chapter-list"><div v-for="c in chapters" :key="c.id" class="chapter-item"><span>{{String(c.sortNo).padStart(2,'0')}}</span><div><b>{{c.title}}</b><small>{{(c.content||'').slice(0,50)}}...</small></div><el-button text type="primary" @click="editChapter(c)">编辑</el-button><el-button text type="danger" @click="deleteChapter(c.id)">删除</el-button></div></div>
      <el-divider/>
      <el-form :model="chapterForm" label-position="top" v-if="chapterForm.bookId"><el-form-item label="章节标题"><el-input v-model="chapterForm.title"/></el-form-item><el-form-item label="排序"><el-input-number v-model="chapterForm.sortNo" :min="1"/></el-form-item><el-form-item label="章节内容"><el-input v-model="chapterForm.content" type="textarea" :rows="8"/></el-form-item><el-button type="primary" @click="saveChapter">保存章节</el-button></el-form>
    </el-drawer>
  </div>
</template>

<script setup>
import { reactive, ref, computed, onMounted } from 'vue'
import request from '@/utils/request'
import { ElMessage, ElMessageBox } from 'element-plus'
const categories=['文学','历史','计算机','设计','艺术','哲学','经济']
const uploadUrl=import.meta.env.VITE_BASE_URL+'/files/upload'
const query=reactive({name:'',category:'',pageNum:1,pageSize:8})
const data=reactive({rows:[],total:0}); const rows=computed(()=>data.rows),total=computed(()=>data.total)
const formVisible=ref(false),form=reactive({}),chapterVisible=ref(false),currentBook=reactive({}),chapters=reactive([]),chapterForm=reactive({})
const load=()=>request.get('/book/selectPage',{params:query}).then(res=>{if(res.code==='200'){data.rows=res.data.list||[];data.total=res.data.total||0}})
const search=()=>{query.pageNum=1;load()}
const resetForm=()=>Object.assign(form,{id:null,name:'',author:'',cover:'/images/covers/book-01.svg',category:'文学',publisher:'',isbn:'',description:'',total:10,stock:10,score:5,status:'AVAILABLE'})
const handleAdd=()=>{resetForm();formVisible.value=true}
const handleEdit=(row)=>{resetForm();Object.assign(form,row);formVisible.value=true}
const save=()=>{const method=form.id?'put':'post';request[method](form.id?'/book/update':'/book/add',form).then(res=>{if(res.code==='200'){ElMessage.success('保存成功');formVisible.value=false;load()}else ElMessage.error(res.msg)})}
const del=(id)=>ElMessageBox.confirm('删除后不可恢复，确认删除该图书？','删除确认').then(()=>request.delete('/book/delete/'+id).then(res=>{if(res.code==='200'){ElMessage.success('已删除');load()}else ElMessage.error(res.msg)}))
const uploadSuccess=(res)=>{if(res.code==='200')form.cover=res.data}
const clearChapter=()=>{Object.keys(chapterForm).forEach(k=>delete chapterForm[k])};const newChapter=()=>{clearChapter();Object.assign(chapterForm,{bookId:currentBook.id,sortNo:chapters.length+1,title:"",content:""})};const editChapter=(c)=>{clearChapter();Object.assign(chapterForm,c)};const manageChapters=(row)=>{Object.assign(currentBook,row);clearChapter();loadChapters();chapterVisible.value=true}
const loadChapters=()=>request.get('/chapter/selectByBookId/'+currentBook.id).then(res=>{if(res.code==='200')chapters.splice(0,chapters.length,...(res.data||[]))})
const saveChapter=()=>request[chapterForm.id?'put':'post'](chapterForm.id?'/chapter/update':'/chapter/add',{...chapterForm}).then(res=>{if(res.code==='200'){ElMessage.success('章节已保存');clearChapter();loadChapters()}else ElMessage.error(res.msg)})
const deleteChapter=(id)=>ElMessageBox.confirm('确认删除该章节？','提示').then(()=>request.delete('/chapter/delete/'+id).then(()=>loadChapters()))
onMounted(load)
</script>

<style scoped>
.book-cell{display:flex;gap:11px;align-items:center}.book-cell img{width:38px;height:52px;border-radius:5px;object-fit:cover}.book-cell div{display:flex;flex-direction:column;gap:4px}.book-cell b{font-size:13px}.book-cell span,.muted{font-size:10px;color:var(--muted)}.gold{color:#b27a1b;font-weight:600}.cover-upload{display:flex;align-items:center;gap:16px}.cover-upload img{width:68px;height:92px;object-fit:cover;border-radius:8px}.chapter-toolbar{display:flex;justify-content:flex-end}.chapter-list{margin-top:16px}.chapter-item{display:flex;align-items:center;gap:12px;padding:12px 0;border-bottom:1px solid #f0f2f5}.chapter-item>span{font-family:var(--font-serif);color:var(--gold)}.chapter-item>div{flex:1;display:flex;flex-direction:column;gap:4px}.chapter-item small{color:var(--muted);font-size:10px}
</style>


