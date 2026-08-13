<script setup lang="ts">
import { ref, computed, onMounted, onUnmounted } from 'vue'
import prueba from '../json/prueba.json'

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

  document.documentElement.classList.toggle(
    'dark',
    isDark.value
  )
}

/* =========================================================
   DRAWER MOBILE
   ========================================================= */

const isDrawerOpen = ref(false)

function openDrawer() {
  isDrawerOpen.value = true
}

function closeDrawer() {
  isDrawerOpen.value = false
}

/* =========================================================
   NAVEGACIÓN
   ========================================================= */

interface NavItem {
  label: string
  shape: string
  color: string
  active: boolean
}

const navItems = ref<NavItem[]>([
  {
    label: 'Dashboard',
    shape: 'square',
    color: 'nav-red',
    active: true,
  },
  {
    label: 'Documentos',
    shape: 'circle',
    color: 'nav-blue',
    active: false,
  },
  {
    label: 'Empresas',
    shape: 'triangle',
    color: 'nav-yellow',
    active: false,
  },
  {
    label: 'Contactos',
    shape: 'diamond',
    color: 'nav-teal',
    active: false,
  },
  {
    label: 'Buscador',
    shape: 'ring',
    color: 'nav-green',
    active: false,
  },
])

function setActive(label: string) {
  navItems.value.forEach((item) => {
    item.active = item.label === label
  })

  closeDrawer()
}

/* =========================================================
   ESTADÍSTICAS
   ========================================================= */

const totalDocumentos = computed(() => documentos.length)

const totalEmpresas = computed(() => empresas.length)

const totalContactos = computed(() => contactos.length)

/*
 * El JSON actual no contiene un campo "estado"
 * para los documentos.
 *
 * Por eso no inventamos pendientes.
 */
const totalPendientes = computed(() => 0)

const stats = computed(() => [
  {
    label: 'Documentos',
    value: totalDocumentos.value,
    textColor: 'nav-red',
    shape: 'square',
    icon: 'nf-oct-book',
  },
  {
    label: 'Empresas',
    value: totalEmpresas.value,
    textColor: 'nav-yellow',
    shape: 'triangle',
    icon: 'nf-fa-building',
  },
  {
    label: 'Contactos',
    value: totalContactos.value,
    textColor: 'nav-blue',
    shape: 'circle',
    icon: 'nf-md-contacts',
  },
  {
    label: 'Pendientes',
    value: totalPendientes.value,
    textColor: 'nav-yellow',
    shape: 'diamond',
    icon: 'nf-md-account_alert',
  },
])

/* =========================================================
   CATEGORÍAS
   ========================================================= */

interface CategoryConfig {
  color: string
  icon: string
}

const categoryConfig: Record<string, CategoryConfig> = {
  'Públicos/Oficiales': {
    color: 'cat-publicos',
    icon: '◼',
  },

  'Privados/Contractuales': {
    color: 'cat-privados',
    icon: '◼',
  },

  'Internos/Gestión': {
    color: 'cat-internos',
    icon: '◼',
  },

  'Regulatorios/Cumplimiento': {
    color: 'cat-regulatorios',
    icon: '◼',
  },

  'Operativos/Transaccionales': {
    color: 'cat-operativos',
    icon: '◼',
  },
}

const categories = computed(() => {
  const counts: Record<string, number> = {}

  documentos.forEach((documento) => {
    const categoria = documento.categoria

    counts[categoria] = (counts[categoria] || 0) + 1
  })

  return Object.entries(counts).map(([name, count]) => ({
    name,
    count,
    color:
      categoryConfig[name]?.color ||
      'bg-gray-400 text-white',
    icon:
      categoryConfig[name]?.icon ||
      '◼',
  }))
})

/* =========================================================
   DOCUMENTOS RECIENTES
   ========================================================= */

/*
 * El JSON no tiene fecha.
 *
 * Para las pruebas mostramos los últimos
 * documentos según su ID.
 */
const recentDocs = computed(() => {
  return [...documentos]
    .sort((a, b) => b.id - a.id)
    .slice(0, 5)
})

/* =========================================================
   CLASES DE CATEGORÍA
   ========================================================= */

function categoryClass(category: string): string {
  return (
    categoryConfig[category]?.color ||
    'bg-gray-400 text-white'
  )
}
</script>

<template>
  <div class="min-h-screen bg-app text-primary font-sans">

    <!-- =====================================================
         CABECERA
         ===================================================== -->

    <header
      class="sticky top-0 z-50 bg-surface border-b border-theme shadow-sm"
    >
      <div
        class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8"
      >
        <div
          class="flex items-center justify-between h-16"
        >

          <!-- Logo + menú mobile -->

          <div class="flex items-center gap-3">

            <button
              v-show="isMobile"
              class="btn btn-ghost w-11 h-11 p-0 rounded-xl text-lg"
              aria-label="Abrir menú"
              @click="openDrawer"
            >
              <i class="nf nf-md-menu"></i>
            </button>

            <div class="flex items-center gap-3">

              <div
                class="w-9 h-9 rounded-lg bg-brand flex items-center justify-center text-white font-bold text-lg"
              >
                D
              </div>

              <div class="hidden sm:flex flex-col">

                <span
                  class="text-lg font-bold tracking-tight text-primary leading-none"
                >
                  Documenti
                </span>

                <span
                  class="text-[10px] text-secondary uppercase tracking-widest leading-none mt-0.5"
                >
                  Gestión Documental
                </span>

              </div>
            </div>
          </div>

          <!-- Navegación escritorio -->

          <nav
            v-show="!isMobile"
            class="flex items-center gap-1"
          >

            <button
              v-for="item in navItems"
              :key="item.label"
              class="nav-btn btn btn-ghost px-4 py-2 text-sm relative overflow-hidden"
              :class="[
                item.color,
                {
                  'bg-surface-secondary text-primary font-semibold':
                    item.active,
                },
              ]"
              @click="setActive(item.label)"
            >

              <span
                :class="[
                  'geo-shape mr-2',
                  item.shape,
                ]"
              ></span>

              {{ item.label }}

              <span
                class="bauhaus-lines"
                aria-hidden="true"
              >
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
              :aria-label="
                isDark
                  ? 'Modo claro'
                  : 'Modo oscuro'
              "
              :title="
                isDark
                  ? 'Modo claro'
                  : 'Modo oscuro'
              "
              @click="toggleTheme"
            >
              <i
                :class="[
                  'nf',
                  isDark
                    ? 'nf-fa-sun'
                    : 'nf-fae-moon_cloud',
                ]"
              ></i>
            </button>

            <!-- Notificaciones -->

            <button
              class="btn btn-ghost w-9 h-9 p-0 rounded-full relative text-base"
              aria-label="Notificaciones"
            >

              <i class="nf nf-md-bell"></i>

              <span
                class="absolute top-1.5 right-1.5 w-2 h-2 bg-brand-accent rounded-full"
              ></span>

            </button>

            <!-- Usuario -->

            <button
              class="flex items-center gap-2 btn btn-ghost pl-2 pr-3 py-1.5 rounded-full"
            >

              <div
                class="w-7 h-7 rounded-full bg-brand text-white text-xs font-bold flex items-center justify-center"
              >
                AM
              </div>

              <span
                class="hidden lg:inline text-sm font-medium"
              >
                Adrián Montel
              </span>

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
        aria-label="Menú de navegación"
      >

        <!-- Header drawer -->

        <div
          class="flex items-center justify-between px-5 h-16 border-b border-theme"
        >

          <div class="flex items-center gap-3">

            <div
              class="w-8 h-8 rounded-lg bg-brand flex items-center justify-center text-white font-bold text-sm"
            >
              D
            </div>

            <span
              class="text-base font-bold text-primary"
            >
              Documenti
            </span>

          </div>

          <button
            class="btn btn-ghost w-10 h-10 p-0 rounded-xl text-lg"
            aria-label="Cerrar menú"
            @click="closeDrawer"
          >
            <i class="nf nf-md-close"></i>
          </button>

        </div>

        <!-- Navegación -->

        <nav
          class="flex-1 p-4 space-y-1 overflow-y-auto"
        >

          <button
            v-for="item in navItems"
            :key="item.label"
            class="w-full flex items-center gap-3 px-4 py-3 rounded-lg text-sm font-medium transition-colors"
            :class="[
              item.color,
              item.active
                ? 'bg-surface-secondary text-primary'
                : 'text-secondary hover:bg-surface-secondary/50 hover:text-primary',
            ]"
            @click="setActive(item.label)"
          >

            <span
              :class="[
                'geo-shape',
                item.shape,
              ]"
            ></span>

            {{ item.label }}

          </button>

        </nav>

        <!-- Usuario -->

        <div
          class="p-4 border-t border-theme"
        >

          <div
            class="flex items-center gap-3 px-4 py-3"
          >

            <div
              class="w-8 h-8 rounded-full bg-brand text-white text-xs font-bold flex items-center justify-center"
            >
              AM
            </div>

            <div class="flex flex-col">

              <span
                class="text-sm font-medium text-primary"
              >
                Adrián Montel
              </span>

              <span
                class="text-xs text-secondary"
              >
                Administrador
              </span>

            </div>

          </div>

        </div>

      </aside>

    </Transition>

    <!-- =====================================================
         CONTENIDO PRINCIPAL
         ===================================================== -->

    <main
      class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8 space-y-8"
    >

      <!-- ===================================================
           BIENVENIDA
           =================================================== -->

      <section class="fade-in">

        <h1
          class="text-2xl sm:text-3xl font-bold text-primary tracking-tight"
        >
          Buenos días, Adrián
        </h1>

        <p class="text-secondary mt-1">

          Tenés

          <strong class="text-brand-accent">
            {{ totalDocumentos }} documentos
          </strong>

          disponibles en el sistema

        </p>

      </section>

      <!-- ===================================================
           ACCIONES RÁPIDAS
           =================================================== -->

      <section
        class="flex flex-wrap gap-3 fade-in"
      >

        <button
          class="btn btn-primary shadow-md hover-lift"
        >
          <i class="nf nf-md-plus text-base"></i>
          Subir documento
        </button>

        <button
          class="btn btn-secondary hover-lift"
        >
          <i class="nf nf-md-folder text-base"></i>
          Nueva carpeta
        </button>

        <button
          class="btn btn-secondary hover-lift"
        >
          <i class="nf nf-md-lightning-bolt text-base"></i>
          Clasificación IA
        </button>

        <button
          class="btn btn-secondary hover-lift"
        >
          <i class="nf nf-md-tag text-base"></i>
          Crear etiqueta
        </button>

      </section>

      <!-- ===================================================
           ESTADÍSTICAS
           =================================================== -->

      <section
        class="grid grid-cols-2 lg:grid-cols-4 gap-4 fade-in"
      >

        <article
          v-for="stat in stats"
          :key="stat.label"
          class="card p-5 hover-lift cursor-pointer relative overflow-hidden"
          :class="stat.textColor"
        >

          <div
            class="flex items-start justify-between"
          >

            <div>

              <p
                class="text-sm font-medium"
              >
                {{ stat.label }}
              </p>

              <p
                class="text-3xl font-bold text-primary mt-1"
              >
                {{ stat.value }}
              </p>

            </div>

            <div
              class="w-10 h-10 flex items-center justify-center text-lg relative"
            >

              <span
                :class="[
                  'geo-shape-lg absolute',
                  stat.shape,
                  stat.textColor,
                ]"
              ></span>

              <i
                class="nf relative z-10 text-white text-xl"
                :class="stat.icon"
              ></i>

            </div>

          </div>

          <!-- Líneas Bauhaus -->

          <span
            class="bauhaus-lines"
            aria-hidden="true"
          >

            <span class="line line-1"></span>
            <span class="line line-2"></span>
            <span class="line line-3"></span>

          </span>

        </article>

      </section>

      <!-- ===================================================
           CATEGORÍAS
           =================================================== -->

      <section class="fade-in">

        <div
          class="flex items-center justify-between mb-4"
        >

          <h2
            class="text-lg font-bold text-primary"
          >
            Categorías
          </h2>

          <button
            class="btn btn-ghost text-sm"
          >
            Ver todas →
          </button>

        </div>

        <div
          class="grid grid-cols-2 md:grid-cols-3 lg:grid-cols-5 gap-3"
        >

          <article
            v-for="cat in categories"
            :key="cat.name"
            class="card p-4 hover-lift cursor-pointer group"
          >

            <div
              :class="[
                'w-10 h-10 rounded-lg flex items-center justify-center text-lg mb-3',
                cat.color,
              ]"
            >
              {{ cat.icon }}
            </div>

            <h3
              class="text-sm font-semibold text-primary leading-tight"
            >
              {{ cat.name }}
            </h3>

            <p
              class="text-secondary text-xs mt-1"
            >
              {{ cat.count }} documentos
            </p>

          </article>

        </div>

      </section>

      <!-- ===================================================
           DOCUMENTOS RECIENTES
           =================================================== -->

      <section class="fade-in">

        <div
          class="flex items-center justify-between mb-4"
        >

          <h2
            class="text-lg font-bold text-primary"
          >
            Documentos recientes
          </h2>

          <button
            class="btn btn-ghost text-sm"
          >
            Ver todos →
          </button>

        </div>

        <div
          class="card overflow-hidden"
        >

          <div class="overflow-x-auto">

            <table
              class="w-full text-sm text-left"
            >

              <thead
                class="bg-surface-secondary text-secondary text-xs uppercase tracking-wider"
              >

                <tr>

                  <th
                    class="px-6 py-3 font-semibold"
                  >
                    Documento
                  </th>

                  <th
                    class="px-6 py-3 font-semibold"
                  >
                    Categoría
                  </th>

                  <th
                    class="px-6 py-3 font-semibold"
                  >
                    ID
                  </th>

                  <th
                    class="px-6 py-3 font-semibold"
                  >
                    Estado
                  </th>

                  <th
                    class="px-6 py-3 font-semibold text-right"
                  >
                    Acciones
                  </th>

                </tr>

              </thead>

              <tbody
                class="divide-y divide-[var(--border)]"
              >

                <tr
                  v-for="doc in recentDocs"
                  :key="doc.id"
                  class="hover:bg-surface-secondary/50 transition-colors cursor-pointer"
                >

                  <!-- Documento -->

                  <td
                    class="px-6 py-4 font-medium text-primary"
                  >

                    <div
                      class="flex items-center gap-3 min-w-[260px]"
                    >

                      <span
                        class="text-lg flex-shrink-0"
                      >
                        📄
                      </span>

                      <div
                        class="flex flex-col min-w-0"
                      >

                        <span
                          class="truncate"
                        >
                          {{ doc.nombre }}
                        </span>

                        <span
                          class="text-xs text-secondary mt-1 line-clamp-1"
                        >
                          {{ doc.resumen }}
                        </span>

                      </div>

                    </div>

                  </td>

                  <!-- Categoría -->

                  <td
                    class="px-6 py-4 text-secondary whitespace-nowrap"
                  >

                    <span
                      :class="[
                        'badge',
                        categoryClass(doc.categoria),
                      ]"
                    >
                      {{ doc.categoria }}
                    </span>

                  </td>

                  <!-- ID -->

                  <td
                    class="px-6 py-4 text-secondary whitespace-nowrap"
                  >
                    #{{ doc.id }}
                  </td>

                  <!-- Estado -->

                  <td
                    class="px-6 py-4 whitespace-nowrap"
                  >

                    <span
                      class="badge bg-surface-secondary text-secondary"
                    >
                      Disponible
                    </span>

                  </td>

                  <!-- Acciones -->

                  <td
                    class="px-6 py-4 text-right"
                  >

                    <button
                      class="btn btn-ghost p-1.5 text-secondary hover:text-primary text-lg"
                      aria-label="Más opciones"
                    >
                      <i
                        class="nf nf-md-dots-vertical"
                      ></i>
                    </button>

                  </td>

                </tr>

              </tbody>

            </table>

          </div>

        </div>

      </section>

    </main>

  </div>
</template>

<style scoped>

/* =========================================================
   FORMAS GEOMÉTRICAS — NAVEGACIÓN
   ========================================================= */

.geo-shape {
  display: inline-block;
  width: 0.625rem;
  height: 0.625rem;
  flex-shrink: 0;
  vertical-align: middle;
}

.geo-shape.square {
  background-color: currentColor;
  border-radius: 0.125rem;
}

.geo-shape.circle {
  background-color: currentColor;
  border-radius: 50%;
}

.geo-shape.triangle {
  width: 0;
  height: 0;
  border-left: 0.3125rem solid transparent;
  border-right: 0.3125rem solid transparent;
  border-bottom: 0.625rem solid currentColor;
  background: none;
}

.geo-shape.diamond {
  background-color: currentColor;
  border-radius: 0.125rem;
  transform: rotate(45deg);
  width: 0.5625rem;
  height: 0.5625rem;
}

.geo-shape.ring {
  width: 0.625rem;
  height: 0.625rem;
  border: 0.15625rem solid currentColor;
  border-radius: 50%;
  background: transparent;
}

/* =========================================================
   FORMAS GEOMÉTRICAS — ESTADÍSTICAS
   ========================================================= */

.geo-shape-lg {
  display: block;
  width: 100%;
  height: 100%;
}

.geo-shape-lg.square {
  background-color: currentColor;
  border-radius: 0.25rem;
}

.geo-shape-lg.circle {
  background-color: currentColor;
  border-radius: 50%;
}

.geo-shape-lg.triangle {
  width: 2rem;
  height: 1rem;
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
   COLORES DE NAVEGACIÓN
   ========================================================= */

.nav-red {
  color: #E63946;
}

.nav-blue {
  color: #457B9D;
}

.nav-yellow {
  color: #FFC349;
}

.nav-teal {
  color: #44A1A4;
}

.nav-green {
  color: #B2D959;
}

.nav-red:hover {
  background-color: rgba(230, 57, 70, 0.08);
}

.nav-blue:hover {
  background-color: rgba(69, 123, 157, 0.08);
}

.nav-yellow:hover {
  background-color: rgba(255, 195, 73, 0.12);
}

.nav-teal:hover {
  background-color: rgba(68, 161, 164, 0.08);
}

.nav-green:hover {
  background-color: rgba(178, 217, 89, 0.12);
}

.dark .nav-red:hover {
  background-color: rgba(230, 57, 70, 0.15);
}

.dark .nav-blue:hover {
  background-color: rgba(69, 123, 157, 0.15);
}

.dark .nav-yellow:hover {
  background-color: rgba(255, 195, 73, 0.18);
}

.dark .nav-teal:hover {
  background-color: rgba(68, 161, 164, 0.15);
}

.dark .nav-green:hover {
  background-color: rgba(178, 217, 89, 0.18);
}

/* =========================================================
   ANIMACIÓN BAUHAUS — NAVEGACIÓN
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

.nav-btn .line-1 {
  background-color: var(--color-purple);
  top: 0.25rem;
  transition-delay: 100ms;
}

.nav-btn .line-2 {
  background-color: var(--color-brand-dark);
  top: 1rem;
  transition-delay: 180ms;
}

.nav-btn .line-3 {
  background-color: #FFC349;
  top: 1.75rem;
  transition-delay: 260ms;
}

.nav-btn:hover .bauhaus-lines .line,
.nav-btn:focus-visible .bauhaus-lines .line {
  transform: rotate(55deg) translateX(0);
  opacity: 1;
}

/* =========================================================
   ANIMACIÓN BAUHAUS — TARJETAS
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

.card .line-1 {
  background-color: #E63946;
  top: -0.15rem;
  transition-delay: 150ms;
}

.card .line-2 {
  background-color: var(--color-purple);
  top: 1rem;
  transition-delay: 90ms;
}

.card .line-3 {
  background-color: #FFC349;
  top: 1.65rem;
  transition-delay: 260ms;
}

.card:hover .bauhaus-lines .line,
.card:focus-visible .bauhaus-lines .line {
  transform: rotate(55deg) translateX(0);
  opacity: 1;
}

</style>
