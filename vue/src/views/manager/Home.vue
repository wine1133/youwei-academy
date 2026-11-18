<template>
  <div class="manager-page dashboard">
    <div class="page-heading"><div><div class="eyebrow">OVERVIEW</div><h1>下午好，{{ user.name }}</h1><p>今天也让阅读与学习继续发生。</p></div><div class="head-actions"><el-button round @click="load"><el-icon><Refresh /></el-icon>刷新数据</el-button><el-button type="primary" round @click="router.push('/manager/book')">管理馆藏</el-button></div></div>

    <div class="kpi-grid">
      <div class="kpi-card kpi-blue"><span class="kpi-icon"><el-icon><Collection /></el-icon></span><div><small>馆藏图书</small><b>{{ stats.bookCount || 0 }}</b><em>本</em></div><i>COLLECTION</i></div>
      <div class="kpi-card kpi-gold"><span class="kpi-icon"><el-icon><Tickets /></el-icon></span><div><small>累计借阅</small><b>{{ stats.borrowCount || 0 }}</b><em>次</em></div><i>BORROWING</i></div>
      <div class="kpi-card kpi-teal"><span class="kpi-icon"><el-icon><Document /></el-icon></span><div><small>社区文章</small><b>{{ stats.articleCount || 0 }}</b><em>篇</em></div><i>COMMUNITY</i></div>
      <div class="kpi-card kpi-ink"><span class="kpi-icon"><el-icon><Notebook /></el-icon></span><div><small>学习任务</small><b>{{ stats.homeworkCount || 0 }}</b><em>项</em></div><i>LEARNING</i></div>
    </div>

    <div class="chart-grid">
      <div class="card chart-card"><div class="card-head"><div><small>借阅趋势</small><h3>近 7 天借阅活跃度</h3></div><span class="badge">实时数据</span></div><div ref="trendRef" class="chart"></div></div>
      <div class="card chart-card"><div class="card-head"><div><small>馆藏结构</small><h3>图书分类分布</h3></div></div><div ref="categoryRef" class="chart"></div></div>
    </div>

    <div class="bottom-grid">
      <div class="card recent-card"><div class="card-head"><div><small>新书上架</small><h3>最近馆藏</h3></div><el-button text type="primary" @click="router.push('/manager/book')">查看全部</el-button></div><div class="recent-list"><div v-for="b in stats.recentBooks || []" :key="b.id" class="recent-book"><img :src="b.cover" alt=""><div><b>{{ b.name }}</b><span>{{ b.author }} · {{ b.category }}</span></div><strong>★ {{ b.score }}</strong></div></div></div>
      <div class="card quick-card"><div class="card-head"><div><small>QUICK ACTIONS</small><h3>常用操作</h3></div></div><div class="quick-grid"><button @click="router.push('/manager/book')"><el-icon><Plus /></el-icon><span>新增图书</span></button><button @click="router.push('/manager/article')"><el-icon><EditPen /></el-icon><span>发布文章</span></button><button @click="router.push('/manager/homework')"><el-icon><Notebook /></el-icon><span>布置作业</span></button><button @click="router.push('/manager/borrow')"><el-icon><Tickets /></el-icon><span>借阅记录</span></button></div><div class="quick-note"><span>今日提示</span><p>目前有 <b>{{ stats.activeBorrow || 0 }}</b> 本图书处于借阅中，记得关注即将到期的记录。</p></div></div>
    </div>
  </div>
</template>

<script setup>
import { reactive, onMounted, onBeforeUnmount, nextTick, ref } from 'vue'
import { useRouter } from 'vue-router'
import * as echarts from 'echarts'
import request from '@/utils/request'
const router = useRouter()
const user = JSON.parse(localStorage.getItem('system-user') || '{}')
const stats = reactive({})
let trendChart, categoryChart
const trendRef = ref(null), categoryRef = ref(null)
const renderCharts = () => {
  if (trendRef.value) { trendChart?.dispose(); trendChart = echarts.init(trendRef.value); const trend = stats.borrowTrend || []; trendChart.setOption({grid:{left:38,right:18,top:25,bottom:28},tooltip:{trigger:'axis'},xAxis:{type:'category',data:trend.map(x=>x.day),axisLine:{lineStyle:{color:'#e6e9ee'}},axisLabel:{color:'#8b95a1',fontSize:10}},yAxis:{type:'value',splitLine:{lineStyle:{color:'#f0f2f5'}},axisLabel:{color:'#8b95a1',fontSize:10}},series:[{type:'line',smooth:true,data:trend.map(x=>x.value),symbol:'circle',symbolSize:7,lineStyle:{width:3,color:'#1e5c83'},itemStyle:{color:'#c39a54'},areaStyle:{color:new echarts.graphic.LinearGradient(0,0,0,1,[{offset:0,color:'rgba(30,92,131,.24)'},{offset:1,color:'rgba(30,92,131,0)'}])}}]}) }
  if (categoryRef.value) { categoryChart?.dispose(); categoryChart = echarts.init(categoryRef.value); const cats = stats.categories || []; categoryChart.setOption({tooltip:{trigger:'item'},legend:{bottom:0,itemWidth:8,itemHeight:8,textStyle:{fontSize:10,color:'#697586'}},series:[{type:'pie',radius:['50%','72%'],center:['50%','43%'],avoidLabelOverlap:true,itemStyle:{borderRadius:7,borderColor:'#fff',borderWidth:3},label:{show:false},data:cats.map((x,i)=>({name:x.name,value:x.value,itemStyle:{color:['#1e5c83','#2f7d6d','#c39a54','#7a6f9b','#b36b4f','#6b8e9e'][i%6]}}))}]}) }
}
const load = () => request.get('/dashboard/stats').then(res=>{if(res.code==='200'){Object.assign(stats,res.data);nextTick(renderCharts)}})
onMounted(load)
onBeforeUnmount(()=>{trendChart?.dispose();categoryChart?.dispose()})
</script>

<style scoped>
.head-actions{display:flex;gap:10px}.kpi-grid{display:grid;grid-template-columns:repeat(4,1fr);gap:16px}.kpi-card{position:relative;overflow:hidden;border-radius:18px;padding:22px;color:#fff;min-height:142px;display:flex;align-items:center;gap:16px;box-shadow:0 16px 32px rgba(20,33,61,.08)}.kpi-card:after{content:'';position:absolute;width:110px;height:110px;border-radius:50%;right:-40px;bottom:-50px;background:rgba(255,255,255,.08)}.kpi-icon{width:46px;height:46px;border-radius:14px;background:rgba(255,255,255,.15);display:grid;place-items:center;font-size:21px}.kpi-card div{display:flex;flex-wrap:wrap;align-items:baseline;gap:5px}.kpi-card small{display:block;width:100%;font-size:11px;opacity:.65;margin-bottom:4px}.kpi-card b{font-family:var(--font-serif);font-size:32px}.kpi-card em{font-style:normal;font-size:10px;opacity:.55}.kpi-card i{position:absolute;right:18px;top:18px;font-style:normal;font-size:8px;letter-spacing:.12em;opacity:.35}.kpi-blue{background:linear-gradient(135deg,#173b58,#236889)}.kpi-gold{background:linear-gradient(135deg,#8b6328,#c39a54)}.kpi-teal{background:linear-gradient(135deg,#245e57,#3d8a7d)}.kpi-ink{background:linear-gradient(135deg,#293241,#596579)}.chart-grid{display:grid;grid-template-columns:1.35fr .65fr;gap:16px}.chart-card{padding:20px 22px}.card-head{display:flex;justify-content:space-between;align-items:center;margin-bottom:8px}.card-head small{font-size:9px;letter-spacing:.12em;color:#9aa4ae}.card-head h3{font-family:var(--font-serif);font-size:18px;margin:5px 0 0}.badge{font-size:9px;color:#2f7d6d;background:#e8f4f0;border-radius:999px;padding:5px 9px}.chart{height:280px}.bottom-grid{display:grid;grid-template-columns:1fr 1fr;gap:16px}.recent-card,.quick-card{padding:20px 22px}.recent-list{display:flex;flex-direction:column}.recent-book{display:flex;align-items:center;gap:12px;padding:10px 0;border-bottom:1px solid #f0f2f5}.recent-book:last-child{border:0}.recent-book img{width:35px;height:48px;border-radius:5px;object-fit:cover}.recent-book div{display:flex;flex-direction:column;gap:4px;flex:1}.recent-book b{font-size:12px}.recent-book span{font-size:10px;color:#929ba5}.recent-book strong{font-size:11px;color:#b07a21}.quick-grid{display:grid;grid-template-columns:1fr 1fr;gap:10px;margin-top:10px}.quick-grid button{border:1px solid #e9ecf0;background:#fafbfc;border-radius:13px;padding:17px 10px;display:flex;flex-direction:column;align-items:center;gap:8px;color:#425466;cursor:pointer}.quick-grid button:hover{background:#f5f8fa;color:var(--primary);border-color:#d6e2ea}.quick-grid .el-icon{font-size:20px}.quick-grid span{font-size:11px}.quick-note{margin-top:14px;background:#f8f6f0;border-radius:12px;padding:13px 15px}.quick-note span{color:#a27a37;font-size:9px;letter-spacing:.1em}.quick-note p{margin:5px 0 0;font-size:11px;color:#75808a;line-height:1.6}@media(max-width:1100px){.kpi-grid{grid-template-columns:1fr 1fr}.chart-grid,.bottom-grid{grid-template-columns:1fr}}@media(max-width:600px){.kpi-grid{grid-template-columns:1fr}.head-actions{display:none}}
</style>
