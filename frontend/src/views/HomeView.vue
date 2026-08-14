<script setup lang="ts">
import { ref, computed, onMounted, onUnmounted } from 'vue'
import prueba from '../json/prueba.json'
import CrearEntidad from './CrearEntidad.vue'
import VerTodasCategorias from './VerTodasCategorias.vue'
/* =========================================================
   TIPOS
   ========================================================= */

interface Documento {
  id: number
  nombre: string
  categoria: string
  resumen: string
}

interface Empresa {
  id: number
  nombre: string
  correo: string
  categoria: string
  resumen?: string
}

interface Contacto {
  id: number
  nombre: string
  correo: string
  funcion: string
}

/* =========================================================
   DATOS DEL JSON
   ========================================================= */

const documentos = (prueba.Documentos ?? []) as Documento[]

const empresas = [
  ...(prueba.empresas ?? []),
  ...(prueba.Empresas ?? []),
] as Empresa[]

const contactos = (prueba.contactos ?? []) as Contacto[]

/* =========================================================
   RESPONSIVE
   ========================================================= */

const windowWidth = ref(window.innerWidth)

const isMobile = computed(() => windowWidth.value < 768)

function onResize() {
  windowWidth.value = window.innerWidth
}

onMounted(() => {
  window.addEventListener('resize', onResize)
})

onUnmounted(() => {
  window.removeEventListener('resize', onResize)
})

/* =========================================================
   TEMA OSCURO / CLARO
   ========================================================= */

const isDark = ref(false)

function toggleTheme() {
  isDark.value = !isDark.value
  document.documentElement.classList.toggle('dark', isDark.value)
}

/* =========================================================
   DRAWER MOBILE
   ========================================================= */

const isDrawerOpen = ref(false)

function openDrawer()  { isDrawerOpen.value = true }
function closeDrawer() { isDrawerOpen.value = false }
/* =========================================================
   MODAL: CREAR ENTIDAD (carpeta / categoria / etiqueta)
   ========================================================= */

const modalTipo = ref<'carpeta' | 'categoria' | 'etiqueta'>('carpeta')
const isModalOpen = ref(false)

function abrirModal(tipo: 'carpeta' | 'categoria' | 'etiqueta') {
  modalTipo.value = tipo
  isModalOpen.value = true
}


function onCrearEntidad(payload: { nombre: string; tipo: string; color: string; icono: string }) {
  console.log(`[${payload.tipo}] Creado:`, payload.nombre, 'Color:', payload.color, 'Icono:', payload.icono)
  // Aca tu logica: API, store, etc.
  isModalOpen.value = false
}

/* =========================================================
   MODAL: VER TODAS LAS CATEGORIAS
   ========================================================= */

const isCategoriesModalOpen = ref(false)

function openCategoriesModal() {
  isCategoriesModalOpen.value = true
}


/* =========================================================
   CATEGORIAS
   ========================================================= */


interface CategoryConfig {
  colorClass: string
  icon: string
  colorVar: string
}
const categoryConfig: Record<string, CategoryConfig> = {
  'Publicos/Oficiales':        { colorClass: 'cat-publicos',    icon: '◼', colorVar: 'var(--color-blue)' },
  'Privados/Contractuales':    { colorClass: 'cat-privados',    icon: '◼', colorVar: 'var(--color-purple)' },
  'Internos/Gestion':          { colorClass: 'cat-internos',    icon: '◼', colorVar: 'var(--color-teal)' },
  'Regulatorios/Cumplimiento': { colorClass: 'cat-regulatorios',icon: '◼', colorVar: 'var(--color-green)' },
  'Operativos/Transaccionales':{ colorClass: 'cat-operativos',  icon: '◼', colorVar: 'var(--color-yellow)' },
}
/* =========================================================
   NAVEGACION
   ========================================================= */

interface NavItem {
  label: string
  shape: string
  colorVar: string
  active: boolean
}

const navItems = ref<NavItem[]>([
  { label: 'Dashboard',   shape: 'square',  colorVar: 'var(--color-brand-accent)', active: true  },
  { label: 'Documentos',  shape: 'circle',  colorVar: 'var(--color-blue)',         active: false },
  { label: 'Empresas',    shape: 'triangle',colorVar: 'var(--color-yellow)',       active: false },
  { label: 'Contactos',   shape: 'diamond', colorVar: 'var(--color-teal)',         active: false },
  { label: 'Buscador',    shape: 'ring',    colorVar: 'var(--color-green)',        active: false },
])

function setActive(label: string) {
  navItems.value.forEach((item) => { item.active = item.label === label })
  closeDrawer()
}

/* =========================================================
   ESTADISTICAS
   ========================================================= */

const totalDocumentos = computed(() => documentos.length)
const totalEmpresas   = computed(() => empresas.length)
const totalContactos  = computed(() => contactos.length)
const totalPendientes = computed(() => 0) // el JSON no tiene campo estado

const stats = computed(() => [
  { label: 'Documentos', value: totalDocumentos.value, colorVar: 'var(--color-brand-accent)', shape: 'square',  icon: 'nf-oct-book'        },
  { label: 'Empresas',   value: totalEmpresas.value,   colorVar: 'var(--color-yellow)',       shape: 'triangle',icon: 'nf-fa-building'     },
  { label: 'Contactos',  value: totalContactos.value,  colorVar: 'var(--color-blue)',         shape: 'circle',  icon: 'nf-md-contacts'     },
  { label: 'Pendientes', value: totalPendientes.value, colorVar: 'var(--color-yellow)',       shape: 'diamond', icon: 'nf-md-account_alert'},
])




const categories = computed(() => {
  const counts: Record<string, number> = {}
  documentos.forEach((d) => { counts[d.categoria] = (counts[d.categoria] || 0) + 1 })
  return Object.entries(counts).map(([name, count]) => ({
    name,
    count,
    colorClass: categoryConfig[name]?.colorClass || 'bg-gray-400 text-white',
    icon:       categoryConfig[name]?.icon       || '◼',
    colorVar:   categoryConfig[name]?.colorVar   || 'var(--color-blue)',
  }))
})

/* =========================================================
   DOCUMENTOS RECIENTES
   ========================================================= */

const recentDocs = computed(() =>
  [...documentos].sort((a, b) => b.id - a.id).slice(0, 5)
)

function categoryClass(category: string): string {
  return categoryConfig[category]?.colorClass || 'bg-gray-400 text-white'
}
</script>

<template>
  <div class="min-h-screen bg-app text-primary font-sans">

    <!-- =====================================================
         CABECERA
         ===================================================== -->
    <header class="sticky top-0 z-50 bg-surface border-b border-theme shadow-sm">
      <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div class="flex items-center justify-between h-16">

          <!-- Logo + menu mobile -->
          <div class="flex items-center gap-3">
            <button
              v-show="isMobile"
              class="btn btn-ghost w-11 h-11 p-0 rounded-xl text-lg"
              aria-label="Abrir menu"
              @click="openDrawer"
            >
              <i class="nf nf-md-menu"></i>
            </button>

            <div class="flex items-center gap-3">
              <div class="w-9 h-9 rounded-lg bg-brand flex items-center justify-center text-white font-bold text-lg">
                D
              </div>
              <div class="hidden sm:flex flex-col">
                <span class="text-lg font-bold tracking-tight text-primary leading-none">Documenti</span>
                <span class="text-[10px] text-secondary uppercase tracking-widest leading-none mt-0.5">
                  Gestion Documental
                </span>
              </div>
            </div>
          </div>

          <!-- Navegacion escritorio -->
          <nav v-show="!isMobile" class="flex items-center gap-1">
            <button
              v-for="item in navItems"
              :key="item.label"
              class="nav-btn btn btn-ghost px-4 py-2 text-sm relative overflow-hidden"
              :class="{ 'bg-surface-secondary text-primary font-semibold': item.active }"
              :style="{ color: item.active ? undefined : item.colorVar }"
              @click="setActive(item.label)"
            >
              <span :class="['geo-shape mr-2', item.shape]" :style="{ color: item.colorVar }"></span>
              {{ item.label }}
              <span class="bauhaus-lines" aria-hidden="true">
                <span class="line line-1"></span>
                <span class="line line-2"></span>
                <span class="line line-3"></span>
              </span>
            </button>
          </nav>

          <!-- Acciones -->
          <div class="flex items-center gap-2">
            <!-- Tema -->
            <button
              class="btn btn-ghost w-9 h-9 p-0 rounded-full text-base"
              :aria-label="isDark ? 'Modo claro' : 'Modo oscuro'"
              :title="isDark ? 'Modo claro' : 'Modo oscuro'"
              @click="toggleTheme"
            >
              <i :class="['nf', isDark ? 'nf-fa-sun' : 'nf-fae-moon_cloud']"></i>
            </button>

            <!-- Notificaciones -->
            <button class="btn btn-ghost w-9 h-9 p-0 rounded-full relative text-base" aria-label="Notificaciones">
              <i class="nf nf-md-bell"></i>
              <span class="absolute top-1.5 right-1.5 w-2 h-2 bg-brand-accent rounded-full"></span>
            </button>

            <!-- Usuario -->
            <button class="flex items-center gap-2 btn btn-ghost pl-2 pr-3 py-1.5 rounded-full">
              <div class="w-7 h-7 rounded-full bg-brand text-white text-xs font-bold flex items-center justify-center">
                AM
              </div>
              <span class="hidden lg:inline text-sm font-medium">Adrian Montel</span>
            </button>
          </div>

        </div>
      </div>
    </header>

    <!-- =====================================================
         DRAWER MOBILE
         ===================================================== -->
    <Transition
      enter-active-class="transition-opacity duration-200"
      enter-from-class="opacity-0"
      enter-to-class="opacity-100"
      leave-active-class="transition-opacity duration-200"
      leave-from-class="opacity-100"
      leave-to-class="opacity-0"
    >
      <div
        v-if="isDrawerOpen"
        class="fixed inset-0 z-[60] bg-black/40 backdrop-blur-sm"
        aria-hidden="true"
        @click="closeDrawer"
      ></div>
    </Transition>

    <Transition
      enter-active-class="transition-transform duration-300 ease-out"
      enter-from-class="-translate-x-full"
      enter-to-class="translate-x-0"
      leave-active-class="transition-transform duration-200 ease-in"
      leave-from-class="translate-x-0"
      leave-to-class="-translate-x-full"
    >
      <aside
        v-if="isDrawerOpen"
        class="fixed top-0 left-0 z-[70] h-full w-72 bg-surface border-r border-theme shadow-xl flex flex-col"
        role="dialog"
        aria-modal="true"
        aria-label="Menu de navegacion"
      >
        <!-- Header drawer -->
        <div class="flex items-center justify-between px-5 h-16 border-b border-theme">
          <div class="flex items-center gap-3">
            <div class="w-8 h-8 rounded-lg bg-brand flex items-center justify-center text-white font-bold text-sm">D</div>
            <span class="text-base font-bold text-primary">Documenti</span>
          </div>
          <button
            class="btn btn-ghost w-10 h-10 p-0 rounded-xl text-lg"
            aria-label="Cerrar menu"
            @click="closeDrawer"
          >
            <i class="nf nf-md-close"></i>
          </button>
        </div>

        <!-- Navegacion -->
        <nav class="flex-1 p-4 space-y-1 overflow-y-auto">
          <button
            v-for="item in navItems"
            :key="item.label"
            class="w-full flex items-center gap-3 px-4 py-3 rounded-lg text-sm font-medium transition-colors"
            :class="item.active
              ? 'bg-surface-secondary text-primary'
              : 'text-secondary hover:bg-surface-secondary/50 hover:text-primary'"
            :style="{ color: item.active ? undefined : item.colorVar }"
            @click="setActive(item.label)"
          >
            <span :class="['geo-shape', item.shape]" :style="{ color: item.colorVar }"></span>
            {{ item.label }}
          </button>
        </nav>

        <!-- Usuario -->
        <div class="p-4 border-t border-theme">
          <div class="flex items-center gap-3 px-4 py-3">
            <div class="w-8 h-8 rounded-full bg-brand text-white text-xs font-bold flex items-center justify-center">AM</div>
            <div class="flex flex-col">
              <span class="text-sm font-medium text-primary">Adrian Montel</span>
              <span class="text-xs text-secondary">Administrador</span>
            </div>
          </div>
        </div>
      </aside>
    </Transition>

    <!-- =====================================================
         CONTENIDO PRINCIPAL
         ===================================================== -->
    <main class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8 space-y-8">

      <!-- ===================================================
           BIENVENIDA
           =================================================== -->
      <section class="fade-in">
        <h1 class="text-2xl sm:text-3xl font-bold text-primary tracking-tight">
          Buenos dias, Adrian
        </h1>
        <p class="text-secondary mt-1">
          Tenes <strong class="text-brand-accent">{{ totalDocumentos }} documentos</strong> disponibles en el sistema
        </p>
      </section>

      <!-- ===================================================
           ACCIONES RAPIDAS
           =================================================== -->
      <section class="flex flex-wrap gap-3 fade-in">
        <button class="btn btn-primary shadow-md hover-lift">
          <i class="nf nf-md-plus text-base"></i> Subir documento
        </button>
        <button class="btn btn-secondary hover-lift" @click="abrirModal('carpeta')">
          <i class="nf nf-md-folder text-base"></i> Nueva carpeta
        </button>
        <button class="btn btn-secondary hover-lift" @click="abrirModal('categoria')">
          <i class="nf nf-md-shape_outline text-base"></i> Nueva categoria
        </button>
        <button class="btn btn-secondary hover-lift" @click="abrirModal('etiqueta')">
          <i class="nf nf-md-tag text-base"></i> Crear etiqueta
        </button>
      </section>
      <!-- ===================================================
           ESTADISTICAS
           =================================================== -->
      <section class="grid grid-cols-2 lg:grid-cols-4 gap-4 fade-in">
        <article
          v-for="(stat, idx) in stats"
          :key="stat.label"
          class="card p-5 hover-lift cursor-pointer relative overflow-hidden"
          :style="{ animationDelay: `${idx * 50}ms` }"
        >
          <div class="flex items-start justify-between">
            <div>
              <p class="text-sm font-medium" :style="{ color: stat.colorVar }">{{ stat.label }}</p>
              <p class="text-3xl font-bold text-primary mt-1">{{ stat.value }}</p>
            </div>
            <div class="w-10 h-10 flex items-center justify-center text-lg relative">
              <span
                :class="['geo-shape-lg absolute', stat.shape]"
                :style="{ color: stat.colorVar}"
              ></span>
              <i class="nf relative z-10 text-white text-xl" :class="stat.icon"></i>
            </div>
          </div>
          <span class="bauhaus-lines" aria-hidden="true">
            <span class="line line-1"></span>
            <span class="line line-2"></span>
            <span class="line line-3"></span>
          </span>
        </article>
      </section>

      <!-- ===================================================
           CATEGORIAS
           =================================================== -->
      <section class="fade-in">
        <div class="flex items-center justify-between mb-4">
          <h2 class="text-lg font-bold text-primary">Categorias</h2>
              <button class="btn btn-ghost text-sm" @click="openCategoriesModal">
                Ver todas &rarr;
              </button>
        </div>
        <div class="grid grid-cols-2 md:grid-cols-3 lg:grid-cols-5 gap-3">
          <article
            v-for="cat in categories"
            :key="cat.name"
            class="card p-4 hover-lift cursor-pointer group"
          >
            <div :class="['w-10 h-10 rounded-lg flex items-center justify-center text-lg mb-3', cat.colorClass]">
              {{ cat.icon }}
            </div>
            <h3 class="text-sm font-semibold text-primary leading-tight">{{ cat.name }}</h3>
            <p class="text-secondary text-xs mt-1">{{ cat.count }} documentos</p>
          </article>
        </div>
      </section>

      <!-- ===================================================
           DOCUMENTOS RECIENTES
           =================================================== -->
      <section class="fade-in">
        <div class="flex items-center justify-between mb-4">
          <h2 class="text-lg font-bold text-primary">Documentos recientes</h2>
          <button class="btn btn-ghost text-sm">Ver todos &rarr;</button>
        </div>
        <div class="card overflow-hidden">
          <div class="overflow-x-auto">
            <table class="w-full text-sm text-left">
              <thead class="bg-surface-secondary text-secondary text-xs uppercase tracking-wider">
                <tr>
                  <th class="px-6 py-3 font-semibold">Documento</th>
                  <th class="px-6 py-3 font-semibold">Categoria</th>
                  <th class="px-6 py-3 font-semibold">ID</th>
                  <th class="px-6 py-3 font-semibold">Estado</th>
                  <th class="px-6 py-3 font-semibold text-right">Acciones</th>
                </tr>
              </thead>
              <tbody class="divide-y divide-[var(--border)]">
                <tr
                  v-for="doc in recentDocs"
                  :key="doc.id"
                  class="hover:bg-surface-secondary/50 transition-colors cursor-pointer"
                >
                  <td class="px-6 py-4 font-medium text-primary">
                    <div class="flex items-center gap-3 min-w-[260px]">
                      <span class="text-lg flex-shrink-0">&#128196;</span>
                      <div class="flex flex-col min-w-0">
                        <span class="truncate">{{ doc.nombre }}</span>
                        <span class="text-xs text-secondary mt-1 line-clamp-1">{{ doc.resumen }}</span>
                      </div>
                    </div>
                  </td>
                  <td class="px-6 py-4 text-secondary whitespace-nowrap">
                    <span :class="['badge', categoryClass(doc.categoria)]">{{ doc.categoria }}</span>
                  </td>
                  <td class="px-6 py-4 text-secondary whitespace-nowrap">#{{ doc.id }}</td>
                  <td class="px-6 py-4 whitespace-nowrap">
                    <span class="badge bg-surface-secondary text-secondary">Disponible</span>
                  </td>
                  <td class="px-6 py-4 text-right">
                    <button
                      class="btn btn-ghost p-1.5 text-secondary hover:text-primary text-lg"
                      aria-label="Mas opciones"
                    >
                      <i class="nf nf-md-dots-vertical"></i>
                    </button>
                  </td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>
      </section>
    <!-- Modal generico -->
    <CrearEntidad
      v-model="isModalOpen"
      :tipo="modalTipo"
      @crear="onCrearEntidad"
    />

    <VerTodasCategorias
      v-model="isCategoriesModalOpen"
      :categories="categories"
      :documentos="documentos"
    />

    </main>
  </div>
</template>

<style scoped>
/* =========================================================
   FORMAS GEOMETRICAS
   ========================================================= */

.geo-shape {
  display: inline-block;
  width: 0.625rem;
  height: 0.625rem;
  flex-shrink: 0;
  vertical-align: middle;
}

.geo-shape.square  { background-color: currentColor; border-radius: 0.125rem; }
.geo-shape.circle  { background-color: currentColor; border-radius: 50%; }
.geo-shape.triangle{
  width: 0; height: 0;
  border-left: 0.3125rem solid transparent;
  border-right: 0.3125rem solid transparent;
  border-bottom: 0.625rem solid currentColor;
  background: none;
}
.geo-shape.diamond {
  background-color: currentColor;
  border-radius: 0.125rem;
  transform: rotate(45deg);
  width: 0.5625rem; height: 0.5625rem;
}
.geo-shape.ring {
  width: 0.625rem; height: 0.625rem;
  border: 0.15625rem solid currentColor;
  border-radius: 50%;
  background: transparent;
}

/* -- Grandes -- */
.geo-shape-lg {
  display: block;
  width: 100%; height: 100%;
}
.geo-shape-lg.square  { background-color: currentColor; border-radius: 0.25rem; }
.geo-shape-lg.circle  { background-color: currentColor; border-radius: 50%; }
.geo-shape-lg.triangle{
  width: 2rem; height: 1rem;
  border-left: 1.45rem solid transparent;
  border-right: 1.45rem solid transparent;
  border-bottom: 2.7rem solid currentColor;
  background: none;
}
.geo-shape-lg.diamond {
  background-color: currentColor;
  border-radius: 0.25rem;
  transform: rotate(45deg) scale(0.85);
}

/* =========================================================
   ANIMACION BAUHAUS — NAVEGACION
   ========================================================= */

.nav-btn {
  position: relative;
  overflow: hidden;
}

.nav-btn .bauhaus-lines {
  position: absolute;
  inset: 0;
  pointer-events: none;
  overflow: hidden;
  border-radius: inherit;
}

.nav-btn .bauhaus-lines .line {
  position: absolute;
  right: -3rem;
  width: 7rem;
  height: 0.475rem;
  border-radius: 0.125rem;
  transform: rotate(55deg) translateX(120%);
  opacity: 0;
  transition:
    transform 650ms cubic-bezier(0.4, 0, 0.2, 1),
    opacity 500ms ease;
}

.nav-btn .line-1 { background-color: var(--color-purple);      top: 0.25rem; transition-delay: 100ms; }
.nav-btn .line-2 { background-color: var(--color-brand-dark);  top: 1rem;    transition-delay: 180ms; }
.nav-btn .line-3 { background-color: var(--color-yellow);      top: 1.75rem; transition-delay: 260ms; }

.nav-btn:hover  .bauhaus-lines .line,
.nav-btn:focus-visible .bauhaus-lines .line {
  transform: rotate(55deg) translateX(0);
  opacity: 1;
}

/* =========================================================
   ANIMACION BAUHAUS — TARJETAS
   ========================================================= */

.card {
  position: relative;
  overflow: hidden;
}

.card .bauhaus-lines {
  position: absolute;
  inset: 0;
  pointer-events: none;
  overflow: hidden;
  border-radius: inherit;
}

.card .bauhaus-lines .line {
  position: absolute;
  right: -2rem;
  width: 5rem;
  height: 0.70rem;
  border-radius: 1.125rem;
  transform: rotate(55deg) translateX(320%);
  opacity: 0;
  transition:
    transform 450ms cubic-bezier(0.5, 0, 0.2, 1),
    opacity 300ms ease;
}

.card .line-1 { background-color: var(--color-brand-accent); top: -0.15rem; transition-delay: 150ms; }
.card .line-2 { background-color: var(--color-purple);       top: 1rem;     transition-delay: 90ms;  }
.card .line-3 { background-color: var(--color-yellow);       top: 1.65rem;  transition-delay: 260ms; }

.card:hover  .bauhaus-lines .line,
.card:focus-visible .bauhaus-lines .line {
  transform: rotate(55deg) translateX(0);
  opacity: 1;
}
</style>
