<template>
  <div>
    <section class="map-hero"><div class="page-shell"><div class="eyebrow light">YOUWEI SPACES</div><h1>在城市里，找到你的阅读坐标</h1><p>搜索书院空间，查看地点信息，并规划一条简单的到访路线。</p></div></section>
    <div class="page-shell map-layout">
      <section class="map-panel card">
        <div class="map-toolbar">
          <el-input v-model="keyword" clearable placeholder="搜索地点、书店或城市书房" @keyup.enter="search"><template #prefix><el-icon><Search /></el-icon></template></el-input>
          <el-button type="primary" :disabled="!mapReady" @click="search">地图搜索</el-button>
        </div>
        <div id="baidu-map" class="map-canvas">
          <div v-if="!akConfigured" class="map-fallback">
            <div class="fallback-icon"><el-icon><MapLocation /></el-icon></div>
            <h2>百度地图待配置 AK</h2>
            <p>地图代码和路线规划已经集成，但百度地图 JS API 需要有效 AK。请在项目环境变量中配置 <code>VITE_BAIDU_MAP_AK</code> 后重新构建。</p>
            <div class="fallback-steps"><span>1. 百度地图开放平台创建浏览器端 AK</span><span>2. 写入 .env.production</span><span>3. 重新执行 npm run build</span></div>
          </div>
        </div>
        <div class="route-panel">
          <div class="route-fields"><el-select v-model="startId" placeholder="起点" style="width:180px"><el-option v-for="s in spaces" :key="s.id" :label="s.name" :value="s.id"/></el-select><el-icon><Right /></el-icon><el-select v-model="endId" placeholder="终点" style="width:180px"><el-option v-for="s in spaces" :key="s.id" :label="s.name" :value="s.id"/></el-select><el-button type="primary" plain :disabled="!mapReady" @click="route('driving')">驾车路线</el-button><el-button plain :disabled="!mapReady" @click="route('walking')">步行路线</el-button></div>
          <div v-if="routeText" class="route-summary"><el-icon><Guide /></el-icon><span>{{routeText}}</span></div>
        </div>
      </section>
      <aside class="spaces-panel"><div class="spaces-head"><div><div class="eyebrow">CURATED SPACES</div><h2>附近书院空间</h2></div><span>{{spaces.length}} 个地点</span></div><div class="spaces-list"><article v-for="space in spaces" :key="space.id" :class="['space-card',{active:selectedId===space.id}]" @click="focusSpace(space)"><img :src="space.cover" alt=""><div class="space-copy"><div class="space-title"><h3>{{space.name}}</h3><span>{{space.openTime}}</span></div><p><el-icon><Location /></el-icon>{{space.address}}</p><small>{{space.description}}</small><div class="space-actions"><button @click.stop="setAsDestination(space)">设为终点</button><button @click.stop="router.push('/front/library')">查看馆藏</button></div></div></article></div></aside>
    </div>
  </div>
</template>

<script setup>
import { reactive, ref, onMounted, nextTick } from 'vue'
import { useRouter } from 'vue-router'
import request from '@/utils/request'
import { ElMessage } from 'element-plus'
const router = useRouter()
const akConfigured = ref(Boolean(import.meta.env.VITE_BAIDU_MAP_AK))
const mapReady = ref(false)
const keyword = ref('')
const spaces = reactive([])
const selectedId = ref(null)
const startId = ref(null)
const endId = ref(null)
const routeText = ref('')
let map, markers = [], routeInstance, searchInstance

const loadSpaces = () => request.get('/space/selectAll').then(res => {
  if (res.code === '200') {
    spaces.splice(0, spaces.length, ...(res.data || []))
    if (spaces[0]) { selectedId.value = spaces[0].id; startId.value = spaces[0].id; endId.value = spaces[1]?.id || spaces[0].id }
  }
})

const loadBaidu = () => new Promise((resolve, reject) => {
  if (!akConfigured.value) return reject(new Error('NO_AK'))
  if (window.BMapGL) return resolve(window.BMapGL)
  const callback = '__initYouweiMap'
  window[callback] = () => { delete window[callback]; resolve(window.BMapGL) }
  const script = document.createElement('script')
  script.src = `https://api.map.baidu.com/api?v=1.0&type=webgl&ak=${import.meta.env.VITE_BAIDU_MAP_AK}&callback=${callback}`
  script.onerror = () => reject(new Error('地图脚本加载失败'))
  document.head.appendChild(script)
})

const initMap = (BMapGL) => {
  const center = new BMapGL.Point(121.535, 38.883)
  map = new BMapGL.Map('baidu-map')
  map.centerAndZoom(center, 14)
  map.enableScrollWheelZoom(true)
  map.addControl(new BMapGL.ScaleControl())
  map.addControl(new BMapGL.ZoomControl())
  markers = spaces.map(space => {
    const point = new BMapGL.Point(Number(space.longitude), Number(space.latitude))
    const marker = new BMapGL.Marker(point)
    const label = new BMapGL.Label(space.name, { position: point, offset: new BMapGL.Size(15, -24) })
    label.setStyle({ color: '#173b58', border: '0', background: 'rgba(255,255,255,.92)', padding: '6px 10px', borderRadius: '8px', fontSize: '12px' })
    marker.setLabel(label)
    marker.addEventListener('click', () => { selectedId.value = space.id; map.panTo(point) })
    map.addOverlay(marker)
    return marker
  })
  mapReady.value = true
}

const focusSpace = (space) => {
  selectedId.value = space.id
  if (!mapReady.value) return
  map.panTo(new BMapGL.Point(Number(space.longitude), Number(space.latitude)))
}

const setAsDestination = (space) => { endId.value = space.id; selectedId.value = space.id; ElMessage.success('已设为路线终点') }

const search = () => {
  if (!mapReady.value) return
  if (!keyword.value.trim()) { ElMessage.warning('请输入搜索关键词'); return }
  searchInstance = new BMapGL.LocalSearch(map, { renderOptions: { map, autoViewport: true }, pageCapacity: 20 })
  searchInstance.search(keyword.value)
}

const route = (mode) => {
  if (!mapReady.value) return
  const start = spaces.find(x => x.id === startId.value)
  const end = spaces.find(x => x.id === endId.value)
  if (!start || !end) return
  if (routeInstance) { routeInstance.clearResults() }
  const startPoint = new BMapGL.Point(Number(start.longitude), Number(start.latitude))
  const endPoint = new BMapGL.Point(Number(end.longitude), Number(end.latitude))
  const onComplete = (result) => {
    try {
      const plan = result.getPlan(0)
      routeText.value = `${start.name} → ${end.name} · ${mode === 'driving' ? '驾车' : '步行'}约 ${Math.round(plan.getDuration() / 60)} 分钟 · ${(plan.getDistance() / 1000).toFixed(1)} 公里`
    } catch { routeText.value = `${start.name} → ${end.name}` }
  }
  routeInstance = mode === 'driving'
    ? new BMapGL.DrivingRoute(map, { renderOptions: { map, autoViewport: true }, onSearchComplete: onComplete })
    : new BMapGL.WalkingRoute(map, { renderOptions: { map, autoViewport: true }, onSearchComplete: onComplete })
  routeInstance.search(startPoint, endPoint)
}

onMounted(async () => {
  await loadSpaces()
  await nextTick()
  try { initMap(await loadBaidu()) } catch (e) { if (e.message !== 'NO_AK') ElMessage.error(e.message) }
})
</script>

<style scoped>
.map-hero{background:linear-gradient(120deg,#0b2132,#1b5558);color:#fff;padding:58px 0 68px}.map-hero h1{font-family:var(--font-serif);font-size:44px;margin:12px 0}.map-hero p{color:rgba(255,255,255,.6);line-height:1.9}.map-layout{display:grid;grid-template-columns:1fr 370px;gap:22px;margin-top:28px}.map-panel{padding:16px}.map-toolbar{display:flex;gap:10px;margin-bottom:12px}.map-canvas{height:560px;background:#e8ede9;border-radius:16px;overflow:hidden;position:relative}.map-fallback{height:100%;display:flex;flex-direction:column;justify-content:center;align-items:center;text-align:center;padding:40px;background:radial-gradient(circle at 50% 35%,rgba(47,125,109,.12),transparent 45%),#eef2ee}.fallback-icon{width:70px;height:70px;border-radius:22px;background:#fff;display:grid;place-items:center;font-size:32px;color:var(--primary);box-shadow:var(--shadow)}.map-fallback h2{font-family:var(--font-serif);margin:20px 0 10px}.map-fallback p{max-width:520px;color:var(--muted);line-height:1.8;font-size:13px}.map-fallback code{background:#fff;padding:2px 6px;border-radius:6px;color:var(--primary)}.fallback-steps{display:flex;gap:8px;flex-wrap:wrap;justify-content:center;margin-top:16px}.fallback-steps span{padding:6px 10px;border-radius:999px;background:#fff;border:1px solid var(--line);font-size:10px;color:var(--muted)}.route-panel{border-top:1px solid var(--line);margin-top:14px;padding-top:14px}.route-fields{display:flex;align-items:center;gap:8px;flex-wrap:wrap}.route-summary{display:flex;align-items:center;gap:8px;margin-top:12px;padding:11px 14px;border-radius:10px;background:#eef5f2;color:var(--teal);font-size:12px}.spaces-panel{background:#fff;border:1px solid var(--line);border-radius:20px;padding:20px;box-shadow:var(--shadow);max-height:760px;display:flex;flex-direction:column}.spaces-head{display:flex;justify-content:space-between;align-items:end;padding-bottom:14px;border-bottom:1px solid var(--line)}.spaces-head h2{font-family:var(--font-serif);margin:5px 0 0}.spaces-head>span{font-size:10px;color:var(--muted)}.spaces-list{overflow:auto;display:flex;flex-direction:column;gap:10px;padding-top:14px}.space-card{display:flex;gap:12px;padding:12px;border:1px solid var(--line);border-radius:14px;cursor:pointer;transition:all .2s}.space-card:hover,.space-card.active{border-color:#b8d1dd;background:#f5f9fa}.space-card img{width:72px;height:96px;border-radius:9px;object-fit:cover}.space-copy{min-width:0;flex:1}.space-title{display:flex;justify-content:space-between;gap:8px}.space-title h3{margin:0;font-family:var(--font-serif);font-size:16px}.space-title span{font-size:9px;color:var(--teal);white-space:nowrap}.space-copy p{display:flex;align-items:center;gap:4px;color:var(--muted);font-size:10px;margin:8px 0}.space-copy small{display:block;color:#88929c;font-size:10px;line-height:1.5;height:30px;overflow:hidden}.space-actions{display:flex;gap:8px;margin-top:8px}.space-actions button{border:0;background:transparent;color:var(--primary);font-size:10px;cursor:pointer;padding:0}@media(max-width:1000px){.map-layout{grid-template-columns:1fr}.spaces-panel{max-height:none}.spaces-list{display:grid;grid-template-columns:1fr 1fr}}@media(max-width:650px){.map-hero h1{font-size:34px}.map-canvas{height:430px}.spaces-list{grid-template-columns:1fr}.route-fields .el-select{width:100%!important}}
</style>
