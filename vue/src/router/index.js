import { createRouter, createWebHistory } from 'vue-router'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    { path: '/', redirect: '/login' },
    {
      path: '/manager',
      component: () => import('@/views/Manager.vue'),
      redirect: '/manager/home',
      children: [
        { path: 'home', component: () => import('@/views/manager/Home.vue') },
        { path: 'book', component: () => import('@/views/manager/Book.vue') },
        { path: 'borrow', component: () => import('@/views/manager/Borrow.vue') },
        { path: 'article', component: () => import('@/views/manager/Article.vue') },
        { path: 'message', component: () => import('@/views/front/Message.vue') },
        { path: 'comments', component: () => import('@/views/manager/Comments.vue') },
        { path: 'homework', component: () => import('@/views/manager/Homework.vue') },
        { path: 'submit', component: () => import('@/views/manager/Submit.vue') },
        { path: 'user', component: () => import('@/views/manager/User.vue') },
        { path: 'teacher', component: () => import('@/views/manager/Teacher.vue') },
        { path: 'admin', component: () => import('@/views/manager/Admin.vue') },
        { path: 'person', component: () => import('@/views/manager/Person.vue') },
        { path: 'password', component: () => import('@/views/manager/Password.vue') }
      ]
    },
    {
      path: '/front',
      component: () => import('@/views/Front.vue'),
      redirect: '/front/home',
      children: [
        { path: 'home', component: () => import('@/views/front/Home.vue') },
        { path: 'library', component: () => import('@/views/front/Library.vue') },
        { path: 'book/:id', component: () => import('@/views/front/BookDetail.vue') },
        { path: 'reader/:id', component: () => import('@/views/front/Reader.vue') },
        { path: 'community', component: () => import('@/views/front/Community.vue') },
        { path: 'map', component: () => import('@/views/front/Map.vue') },
        { path: 'message', component: () => import('@/views/front/Message.vue') },
        { path: 'article/:id', component: () => import('@/views/front/ArticleDetail.vue') },
        { path: 'publish', component: () => import('@/views/front/Publish.vue') },
        { path: 'homework', component: () => import('@/views/front/Homework.vue') },
        { path: 'person', component: () => import('@/views/front/Person.vue') },
        { path: 'password', component: () => import('@/views/front/Password.vue') }
      ]
    },
    { path: '/login', component: () => import('@/views/Login.vue') },
    { path: '/register', component: () => import('@/views/Register.vue') }
  ]
})

router.beforeEach((to) => {
  const user = JSON.parse(localStorage.getItem('system-user') || '{}')
  if ((to.path.startsWith('/manager') || ['/front/person', '/front/publish', '/front/password'].includes(to.path)) && !user.id) return '/login'
  if (to.path.startsWith('/manager') && user.role === 'USER') return '/front/home'
})

export default router

