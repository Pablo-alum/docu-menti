import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '@/views/HomeView.vue'
import CrearCuenta from '@/views/CrearCuenta.vue'
import Book3D from '@/views/Book3D.vue'
const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: HomeView,
    },
    {
      path: '/login',
      name: 'CrearCuenta',
      component: CrearCuenta,
    },
    {
      path: '/prueba',
      name: 'Book3D',
      component: Book3D,
    },

  ],
})

export default router
