<script setup lang="ts">
import { ref, computed, watch } from 'vue'

/* =========================================================
   TIPOS
   ========================================================= */
interface Documento {
  id: number
  nombre: string
  categoria: string
  resumen: string
}

interface Categoria {
  name: string
  count: number
  colorClass: string
  icon: string
  colorVar: string
}

/* =========================================================
   PROPS
   ========================================================= */
const props = withDefaults(defineProps<{
  modelValue: boolean
  categories: Categoria[]
  documentos: Documento[]
}>(), {
  documentos: () => []
})

const emit = defineEmits<{
  (e: 'update:modelValue', value: boolean): void
}>()

/* =========================================================
   ESTADO
   ========================================================= */
const activeCategory  = ref<string | null>(null)
const hoveredCategory = ref<string | null>(null)
const searchQuery     = ref('')
const currentPage     = ref(1)
const itemsPerPage    = 24

/* =========================================================
   WATCHERS
   ========================================================= */
watch(() => props.modelValue, (open) => {
  if (!open) {
    activeCategory.value  = null
    hoveredCategory.value = null
    searchQuery.value     = ''
    currentPage.value     = 1
  }
})

watch(searchQuery, () => {
  currentPage.value = 1
})

/* =========================================================
   COMPUTED — CARGA BAJO DEMANDA
   ========================================================= */
const totalDocs = computed(() => props.documentos.length)

const docsForCategory = computed(() => {
  if (!activeCategory.value) return []
  return props.documentos.filter((d) => d.categoria === activeCategory.value)
})

const filteredDocs = computed(() => {
  let docs = docsForCategory.value
  const q = searchQuery.value.trim().toLowerCase()
  if (q) {
    docs = docs.filter(
      (d) =>
        d.nombre.toLowerCase().includes(q) ||
        d.resumen.toLowerCase().includes(q)
    )
  }
  return docs
})

const totalPages = computed(() =>
  Math.max(1, Math.ceil(filteredDocs.value.length / itemsPerPage))
)

const paginatedDocs = computed(() => {
  const start = (currentPage.value - 1) * itemsPerPage
  return filteredDocs.value.slice(start, start + itemsPerPage)
})

const activeColorVar = computed(() => {
  if (!activeCategory.value) return 'var(--color-blue)'
  return props.categories.find(c => c.name === activeCategory.value)?.colorVar || 'var(--color-blue)'
})

/* =========================================================
   ACCIONES
   ========================================================= */
function close() {
  emit('update:modelValue', false)
}

function selectCategory(name: string) {
  if (activeCategory.value === name) {
    activeCategory.value = null
    return
  }
  activeCategory.value = name
  searchQuery.value = ''
  currentPage.value = 1
}

function goPage(n: number) {
  if (n >= 1 && n <= totalPages.value) currentPage.value = n
}

function isEngaged(catName: string) {
  return activeCategory.value === catName || hoveredCategory.value === catName
}

/* =========================================================
   ESTILOS DINAMICOS (reemplazan v-bind en CSS)
   ========================================================= */
function docCardHoverStyle() {
  return {
    '--doc-hover-bg': `color-mix(in srgb, ${activeColorVar.value} 8%, var(--surface))`,
    '--doc-hover-border': `color-mix(in srgb, ${activeColorVar.value} 35%, var(--border))`,
    '--doc-hover-title': activeColorVar.value,
  } as Record<string, string>
}
</script>

<template>
  <Teleport to="body">
    <Transition
      enter-active-class="transition-opacity duration-200"
      enter-from-class="opacity-0"
      enter-to-class="opacity-100"
      leave-active-class="transition-opacity duration-150"
      leave-from-class="opacity-100"
      leave-to-class="opacity-0"
    >
      <div
        v-if="modelValue"
        class="fixed inset-0 z-[80] bg-black/60 backdrop-blur-sm flex items-start justify-center p-3 sm:p-6 overflow-y-auto"
        @click.self="close"
      >
        <Transition
          enter-active-class="transition-all duration-300 ease-out"
          enter-from-class="opacity-0 scale-95 translate-y-6"
          enter-to-class="opacity-100 scale-100 translate-y-0"
          leave-active-class="transition-all duration-200 ease-in"
          leave-from-class="opacity-100 scale-100 translate-y-0"
          leave-to-class="opacity-0 scale-95 translate-y-6"
        >
          <div
            v-if="modelValue"
            class="bg-surface border border-theme rounded-2xl shadow-2xl w-full max-w-6xl overflow-hidden my-2 sm:my-6"
          >
            <!-- HEADER -->
            <div class="flex items-center justify-between px-5 sm:px-7 py-4 sm:py-5 border-b border-theme">
              <div class="flex items-center gap-3 sm:gap-4">
                <div class="w-10 h-10 sm:w-11 sm:h-11 rounded-xl bg-brand flex items-center justify-center text-white shrink-0">
                  <i class="nf nf-md-shape_outline text-xl"></i>
                </div>
                <div>
                  <h3 class="text-lg sm:text-xl font-bold text-primary">Todas las categorías</h3>
                  <p class="text-xs sm:text-sm text-secondary">
                    {{ categories.length }} categorías · {{ totalDocs }} documentos totales
                  </p>
                </div>
              </div>
              <button
                class="btn btn-ghost w-9 h-9 sm:w-10 sm:h-10 p-0 rounded-full text-lg"
                aria-label="Cerrar"
                @click="close"
              >
                <i class="nf nf-md-close"></i>
              </button>
            </div>

            <!-- BODY -->
            <div class="p-4 sm:p-6 space-y-5">
              <!-- GRILLA DE CATEGORÍAS -->
              <div class="grid grid-cols-2 sm:grid-cols-3 md:grid-cols-4 lg:grid-cols-5 gap-3 sm:gap-4">
                <button
                  v-for="cat in categories"
                  :key="cat.name"
                  class="relative flex flex-col items-start gap-2.5 p-3.5 sm:p-4 rounded-xl border text-left transition-all duration-200 focus:outline-none"
                  :class="activeCategory === cat.name ? 'shadow-md' : 'hover:shadow-sm'"
                  :style="{
                    backgroundColor: isEngaged(cat.name) ? `color-mix(in srgb, ${cat.colorVar} 10%, transparent)` : undefined,
                    borderColor: isEngaged(cat.name) ? `color-mix(in srgb, ${cat.colorVar} 40%, transparent)` : undefined,
                  }"
                  @mouseenter="hoveredCategory = cat.name"
                  @mouseleave="hoveredCategory = null"
                  @click="selectCategory(cat.name)"
                >
                  <div :class="['w-9 h-9 sm:w-10 sm:h-10 rounded-lg flex items-center justify-center text-lg sm:text-xl shrink-0', cat.colorClass]">
                    {{ cat.icon }}
                  </div>
                  <div class="w-full">
                    <h4
                      class="text-xs sm:text-sm font-semibold leading-tight"
                      :class="isEngaged(cat.name) ? '' : 'text-primary'"
                      :style="isEngaged(cat.name) ? { color: cat.colorVar } : undefined"
                    >
                      {{ cat.name }}
                    </h4>
                    <p class="text-[10px] sm:text-xs text-secondary mt-0.5">{{ cat.count }} documentos</p>
                  </div>

                  <!-- Indicador activo -->
                  <div
                    v-if="activeCategory === cat.name"
                    class="absolute top-3 right-8 w-2 h-2 rounded-full"
                    :style="{ backgroundColor: cat.colorVar }"
                  ></div>

                  <!-- Flecha -->
                  <i
                    class="nf absolute top-3 right-3 text-xs sm:text-sm transition-transform duration-200"
                    :class="activeCategory === cat.name ? 'nf-md-chevron_up' : 'nf-md-chevron_down'"
                    :style="isEngaged(cat.name) ? { color: cat.colorVar } : undefined"
                  ></i>
                </button>
              </div>

              <!-- DESPLIEGUE DE DOCUMENTOS -->
              <Transition
                enter-active-class="transition-all duration-400 ease-out"
                enter-from-class="opacity-0 max-h-0"
                enter-to-class="opacity-100 max-h-[3000px]"
                leave-active-class="transition-all duration-300 ease-in"
                leave-from-class="opacity-100 max-h-[3000px]"
                leave-to-class="opacity-0 max-h-0"
              >
                <div v-if="activeCategory" class="overflow-hidden space-y-4">
                  <!-- Separador -->
                  <div class="flex items-center gap-3">
                    <div class="h-px flex-1 bg-theme"></div>
                    <span
                      class="text-xs sm:text-sm font-semibold"
                      :style="{ color: activeColorVar }"
                    >
                      {{ activeCategory }}
                    </span>
                    <span class="text-xs text-secondary">({{ filteredDocs.length }})</span>
                    <div class="h-px flex-1 bg-theme"></div>
                  </div>

                  <!-- Buscador -->
                  <div class="relative m-2">
                    <i class="nf nf-md-magnify absolute left-3 top-1/2 -translate-y-1/2 text-secondary"></i>
                    <input
                      v-model="searchQuery"
                      type="text"
                      placeholder="Buscar documentos en esta categoría..."
                      class="w-full pl-10 pr-4 py-2.5 rounded-xl border bg-surface text-sm text-primary placeholder:text-secondary focus:outline-none transition-all duration-200 doc-title-hover"
                    />
                  </div>
                  <!-- Grilla documentos: responsive 1/2/3/4 cols -->
                  <div
                    v-if="paginatedDocs.length"
                    class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-3 sm:gap-4"
                    :style="docCardHoverStyle()"
                  >
                    <article
                      v-for="doc in paginatedDocs"
                      :key="doc.id"
                      class="doc-card flex flex-col gap-2 p-3.5 sm:p-4 rounded-xl border border-theme bg-surface cursor-pointer"
                    >
                      <div class="flex items-start justify-between gap-2">
                        <span class="text-xl sm:text-2xl flex-shrink-0">&#128196;</span>
                        <span class="text-[10px] text-secondary font-mono bg-surface-secondary px-1.5 py-0.5 rounded">#{{ doc.id }}</span>
                      </div>
                      <h5 class="doc-title text-xs sm:text-sm font-semibold text-primary leading-tight line-clamp-2">
                        {{ doc.nombre }}
                      </h5>
                      <p class="text-[10px] sm:text-xs text-secondary line-clamp-2 leading-relaxed">
                        {{ doc.resumen }}
                      </p>
                    </article>
                  </div>

                  <!-- Vacío -->
                  <div v-else class="text-center py-10">
                    <div class="w-14 h-14 rounded-full bg-surface-secondary flex items-center justify-center mx-auto mb-3">
                      <i class="nf nf-md-file_document_outline text-2xl text-secondary"></i>
                    </div>
                    <p class="text-sm text-secondary">No se encontraron documentos</p>
                  </div>

                  <!-- Paginación -->
                  <div v-if="totalPages > 1" class="flex items-center justify-center gap-2 pt-2">
                    <button
                      class="btn btn-ghost px-3 py-2 rounded-lg text-sm"
                      :disabled="currentPage === 1"
                      @click="goPage(currentPage - 1)"
                    >
                      <i class="nf nf-md-chevron_left"></i>
                    </button>

                    <span class="text-xs sm:text-sm text-secondary font-medium px-2">
                      Página {{ currentPage }} de {{ totalPages }}
                    </span>

                    <button
                      class="btn btn-ghost px-3 py-2 rounded-lg text-sm"
                      :disabled="currentPage === totalPages"
                      @click="goPage(currentPage + 1)"
                    >
                      <i class="nf nf-md-chevron_right"></i>
                    </button>
                  </div>
                </div>
              </Transition>
            </div>

            <!-- FOOTER -->
            <div class="px-5 sm:px-7 py-4 border-t border-theme bg-surface-secondary/30 flex items-center justify-between">
              <span class="text-xs text-secondary hidden sm:inline">
                Seleccioná una categoría para explorar sus documentos
              </span>
              <button class="btn btn-primary w-full sm:w-auto" @click="close">
                <i class="nf nf-md-check text-base"></i> Cerrar
              </button>
            </div>
          </div>
        </Transition>
      </div>
    </Transition>
  </Teleport>
</template>

<style scoped>
.doc-card {
  transition: background-color 200ms ease, border-color 200ms ease;
}
.doc-card:hover {
  background-color: var(--doc-hover-bg);
  border-color: var(--doc-hover-border);
}
.doc-title {
  transition: color 200ms ease;
}
.doc-card:hover .doc-title {
  color: var(--doc-hover-title);
}
</style>
