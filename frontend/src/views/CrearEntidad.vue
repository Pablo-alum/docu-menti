<script setup lang="ts">
import { ref, watch, computed } from 'vue'

/* =========================================================
   TIPOS
   ========================================================= */

interface ColorOption {
  var: string
  hex: string
  nombre: string
}

interface ConfigModal {
  titulo: string
  icono: string
  placeholder: string
}

interface CrearPayload {
  nombre: string
  tipo: string
  color: string
  icono: string
}

/* =========================================================
   PROPS & EMITS
   ========================================================= */

const props = defineProps<{
  modelValue: boolean
  tipo: 'carpeta' | 'categoria' | 'etiqueta'
}>()

const emit = defineEmits<{
  (e: 'update:modelValue', value: boolean): void
  (e: 'crear', payload: CrearPayload): void
}>()

/* =========================================================
   PALETA DE COLORES (del main.css)
   ========================================================= */

const coloresPredefinidos: ColorOption[] = [
  { var: 'var(--color-brand)',        hex: '#8B1E2D', nombre: 'Marca' },
  { var: 'var(--color-brand-accent)', hex: '#E63946', nombre: 'Rojo' },
  { var: 'var(--color-blue)',         hex: '#457B9D', nombre: 'Azul' },
  { var: 'var(--color-teal)',         hex: '#44A1A4', nombre: 'Teal' },
  { var: 'var(--color-yellow)',       hex: '#FFC349', nombre: 'Amarillo' },
  { var: 'var(--color-green)',        hex: '#B2D959', nombre: 'Verde' },
  { var: 'var(--color-purple)',       hex: '#AF719D', nombre: 'Morado' },
]

/* =========================================================
   CONFIGURACION POR TIPO
   ========================================================= */


type TipoEntidad = 'carpeta' | 'categoria' | 'etiqueta'
const config = computed<ConfigModal>(() => {
  const map: Record<TipoEntidad, ConfigModal> = {
    carpeta: {
      titulo: 'Crear nueva carpeta',
      icono: 'nf-md-folder',
      placeholder: 'Ej: Documentos 2024'
    },
    categoria: {
      titulo: 'Crear nueva categoria',
      icono: 'nf-md-shape_outline',
      placeholder: 'Ej: Publicos/Oficiales'
    },
    etiqueta: {
      titulo: 'Crear nueva etiqueta',
      icono: 'nf-md-tag',
      placeholder: 'Ej: Urgente'
    },
  }

  return map[props.tipo]
})
/* =========================================================
   ESTADO LOCAL
   ========================================================= */
/*--Revisar despues hex y color blue si son lo mismo*/
const COLOR_DEFAULT = 'var(--color-blue)'
const HEX_DEFAULT = '#457B9D'
const nombre = ref('')
const colorSeleccionado = ref<string>(COLOR_DEFAULT)
const colorCustom = ref<string>(HEX_DEFAULT)
const esColorCustom = ref(false)
/* =========================================================
   COMPUTED
   ========================================================= */

const colorFinal = computed<string>(() => {
  if (esColorCustom.value) return colorCustom.value
  return colorSeleccionado.value
})

/* =========================================================
   METODOS
   ========================================================= */

function cerrar() {
  emit('update:modelValue', false)
}

function seleccionarColor(valor: string) {
  colorSeleccionado.value = valor
  esColorCustom.value = !valor.startsWith('var(')
}

function confirmar() {
  const valor = nombre.value.trim()
  if (!valor) return
  emit('crear', {
    nombre: valor,
    tipo: props.tipo,
    color: colorFinal.value,
    icono: config.value.icono,
  })
  resetForm()
}

function resetForm() {
  nombre.value = ''
  colorSeleccionado.value = "var(--color-blue)"
  colorCustom.value = '#457B9D'
  esColorCustom.value = false
}

/* Resetear form cuando se abre */
watch(() => props.modelValue, (abierto) => {
  if (abierto) resetForm()
})
</script>

<template>
  <Transition
    enter-active-class="transition-opacity duration-200"
    enter-from-class="opacity-0"
    enter-to-class="opacity-100"
    leave-active-class="transition-opacity duration-200"
    leave-from-class="opacity-100"
    leave-to-class="opacity-0"
  >
    <div
      v-if="modelValue"
      class="fixed inset-0 bg-black/40 backdrop-blur-sm flex items-center justify-center p-4"
      style="z-index: 80;"
      aria-hidden="true"
      @click.self="cerrar"
    >
      <Transition
        enter-active-class="transition-all duration-300 ease-out"
        enter-from-class="opacity-0 scale-95"
        enter-to-class="opacity-100 scale-100"
        leave-active-class="transition-all duration-200 ease-in"
        leave-from-class="opacity-100 scale-100"
        leave-to-class="opacity-0 scale-95"
      >
        <div
          v-if="modelValue"
          class="card w-full max-w-md p-6 space-y-5"
          role="dialog"
          aria-modal="true"
          :aria-label="config.titulo"
        >
          <!-- Header -->
          <div class="flex items-center justify-between">
            <div class="flex items-center gap-2">
              <i
                :class="['nf text-xl', config.icono]"
                :style="{ color: colorFinal }"
              ></i>
              <h3 class="text-lg font-bold text-primary">{{ config.titulo }}</h3>
            </div>
            <button
              class="btn btn-ghost w-9 h-9 p-0 rounded-full"
              aria-label="Cerrar"
              @click="cerrar"
            >
              <i class="nf nf-md-close"></i>
            </button>
          </div>

          <!-- Formulario -->
          <div class="space-y-2">
            <label for="nombre-entidad" class="block text-sm font-medium text-primary">
              Nombre
            </label>
            <input
              id="nombre-entidad"
              v-model="nombre"
              type="text"
              class="input"
              :placeholder="config.placeholder"
              @keyup.enter="confirmar"
            >
          </div>

          <!-- Selector de color -->
          <div class="space-y-3">
            <label class="block text-sm font-medium text-primary">Color</label>

            <!-- Paleta predefinida -->
            <div class="flex items-center gap-2 flex-wrap">
              <button
                v-for="c in coloresPredefinidos"
                :key="c.var"
                type="button"
                class="w-9 h-9 rounded-full border-2 transition-all hover:scale-110 flex items-center justify-center"
                :class="colorSeleccionado === c.var && !esColorCustom
                  ? 'border-primary scale-110 shadow-md'
                  : 'border-transparent'"
                :style="{ backgroundColor: c.hex }"
                :title="c.nombre"
                @click="seleccionarColor(c.var)"
              >
                <i
                  v-if="colorSeleccionado === c.var && !esColorCustom"
                  class="nf nf-md-check text-white text-sm drop-shadow"
                ></i>
              </button>

              <!-- Color custom -->
              <div class="flex items-center gap-2 ml-1">
                <div
                  class="w-9 h-9 rounded-full border-2 transition-all overflow-hidden flex items-center justify-center"
                  :class="esColorCustom
                    ? 'border-primary scale-110 shadow-md'
                    : 'border-transparent'"
                  :style="{ backgroundColor: colorCustom }"
                >
                  <input
                    v-model="colorCustom"
                    type="color"
                    class="w-full h-full opacity-0 cursor-pointer"
                    @input="seleccionarColor(colorCustom)"
                  >
                </div>
                <span class="text-xs text-secondary">Custom</span>
              </div>
            </div>

            <!-- Preview -->
            <div class="flex items-center gap-3 p-3 bg-surface-secondary rounded-lg border border-theme">
              <span class="text-xs text-secondary uppercase tracking-wider">Preview</span>
              <div class="flex items-center gap-2">
                <i
                  :class="['nf text-2xl', config.icono]"
                  :style="{ color: colorFinal }"
                ></i>
                <span class="text-sm font-medium text-primary">{{ nombre || 'Sin nombre' }}</span>
              </div>
            </div>
          </div>

          <!-- Acciones -->
          <div class="flex justify-end gap-2 pt-1">
            <button class="btn btn-secondary" @click="cerrar">
              Cancelar
            </button>
            <button
              class="btn btn-primary"
              :disabled="!nombre.trim()"
              @click="confirmar"
            >
              <i class="nf nf-md-plus text-base"></i>
              Crear
            </button>
          </div>
        </div>
      </Transition>
    </div>
  </Transition>
</template>
