<script setup>
import { onMounted, onBeforeUnmount, ref } from 'vue'
import * as THREE from 'three'
import { OrbitControls } from 'three/examples/jsm/controls/OrbitControls.js'
import { RoundedBoxGeometry } from 'three/examples/jsm/geometries/RoundedBoxGeometry.js'
import coverTextureImage from '../asset/bookCoverRed.jpg'
const container = ref(null)

let scene
let camera
let renderer
let controls
let animationId

let book
let frontCoverPivot

let isOpen = false
let isAnimating = false

const raycaster = new THREE.Raycaster()
const mouse = new THREE.Vector2()

// =====================================================
// LIBRO
// =====================================================

const BOOK = {
  width: 2.5,
  height: 3.5,

  pagesDepth: 0.50,

  coverThickness: 0.11,

  coverMargin: 0.12,

  spineWidth: 0.12,

  spineDepth: 0.70
}

// =====================================================
// MATERIALES
// =====================================================
const createMaterials = () => {
  const textureLoader = new THREE.TextureLoader()

  const coverTexture =
    textureLoader.load(coverTextureImage)

  coverTexture.colorSpace =
    THREE.SRGBColorSpace

  coverTexture.wrapS =
    THREE.RepeatWrapping

  coverTexture.wrapT =
    THREE.RepeatWrapping

  // La textura es horizontal, así que repetimos
  // un poco en X para evitar deformarla.
  coverTexture.repeat.set(
    1.15,
    1.0
  )

  coverTexture.anisotropy =
    renderer.capabilities.getMaxAnisotropy()

  return {
    pages: new THREE.MeshStandardMaterial({
      color: 0xf3eddb,
      roughness: 0.92
    }),

    pageFront: new THREE.MeshStandardMaterial({
      color: 0xfffdf7,
      roughness: 0.86
    }),

    // ==========================================
    // TAPA CON TEXTURA
    // ==========================================

    cover: new THREE.MeshStandardMaterial({
      map: coverTexture,

      // Mantiene el rojo de la imagen
      color: 0xffffff,

      roughness: 0.72,

      metalness: 0
    }),

    // ==========================================
    // LOMO
    // ==========================================

    spine: new THREE.MeshStandardMaterial({
      map: coverTexture,

      color: 0xffffff,

      roughness: 0.68,

      metalness: 0
    }),

    spineHighlight:
      new THREE.MeshStandardMaterial({
        map: coverTexture,

        color: 0xffffff,

        roughness: 0.72,

        metalness: 0
      })
  }
}
// =====================================================
// CREAR LIBRO
// =====================================================

const createBook = () => {
  const materials = createMaterials()

  book = new THREE.Group()

  scene.add(book)

  // ===================================================
  // BLOQUE DE PÁGINAS
  // ===================================================

  const pagesGeometry =
    new RoundedBoxGeometry(
      BOOK.width,
      BOOK.height,
      BOOK.pagesDepth,
      8,
      0.035
    )

  const pages = new THREE.Mesh(
    pagesGeometry,
    materials.pages
  )

  pages.castShadow = true
  pages.receiveShadow = true

  book.add(pages)

  // ===================================================
  // TAPA TRASERA
  // ===================================================

  const coverGeometry =
    new RoundedBoxGeometry(
      BOOK.width + BOOK.coverMargin,
      BOOK.height + BOOK.coverMargin,
      BOOK.coverThickness,
      8,
      0.055
    )

  const backCover = new THREE.Mesh(
    coverGeometry,
    materials.cover
  )

  backCover.position.set(
    0,
    0,
    -(
      BOOK.pagesDepth / 2 +
      BOOK.coverThickness / 2
    )
  )

  backCover.castShadow = true
  backCover.receiveShadow = true

  book.add(backCover)

  // ===================================================
  // LOMO PRINCIPAL
  // ===================================================

  /*
   * IMPORTANTE:
   *
   * RoundedBoxGeometry ya viene orientado:
   *
   * X = ancho
   * Y = alto
   * Z = profundidad
   *
   * Por eso NO rotamos el lomo.
   *
   * El lomo queda vertical.
   */

  const spineGeometry =
    new RoundedBoxGeometry(
      BOOK.spineWidth,
      BOOK.height + 0.10,
      BOOK.spineDepth,
      12,
      0.12
    )

  const spine = new THREE.Mesh(
    spineGeometry,
    materials.spine
  )

  spine.position.set(
    -BOOK.width / 2,
    0,
    0
  )

  spine.castShadow = true
  spine.receiveShadow = true

  book.add(spine)

  // ===================================================
  // CURVA DEL HOMBRO DEL LOMO
  // ===================================================

  /*
   * Estas dos piezas suavizan la unión entre:
   *
   * páginas → lomo
   *
   * y hacen que el perfil no parezca una caja.
   */

  const shoulderGeometry =
    new THREE.CylinderGeometry(
      0.06,
      0.06,
      BOOK.height - 0.1,
      24
    )

  const leftShoulder =
    new THREE.Mesh(
      shoulderGeometry,
      materials.spineHighlight
    )

  leftShoulder.position.set(
    -BOOK.width / 2 + 0.09,
    0,
    BOOK.pagesDepth / 2
  )

  leftShoulder.castShadow = true

  book.add(leftShoulder)

  // ===================================================
  // PIVOT DE LA TAPA DELANTERA
  // ===================================================

  /*
   * El pivot está exactamente sobre
   * el borde izquierdo del libro.
   */

  frontCoverPivot =
    new THREE.Group()

  frontCoverPivot.position.set(
    -BOOK.width / 2,
    0,
    BOOK.pagesDepth / 2 +
      BOOK.coverThickness / 2
  )

  book.add(frontCoverPivot)

  // ===================================================
  // TAPA DELANTERA
  // ===================================================

  const frontCover = new THREE.Mesh(
    coverGeometry,
    materials.cover
  )

  /*
   * El centro de la tapa está a la mitad
   * de su ancho respecto al pivot.
   */

  frontCover.position.set(
    BOOK.width / 2,
    0,
    0
  )

  frontCover.castShadow = true
  frontCover.receiveShadow = true

  frontCoverPivot.add(frontCover)

  // ===================================================
  // BORDE INTERIOR DE LA TAPA
  // ===================================================

  const innerCoverGeometry =
    new RoundedBoxGeometry(
      BOOK.width - 0.10,
      BOOK.height - 0.10,
      0.025,
      6,
      0.025
    )

  const innerCover =
    new THREE.Mesh(
      innerCoverGeometry,
      materials.spine
    )

  innerCover.position.set(
    BOOK.width / 2,
    0,
    -BOOK.coverThickness / 2 -
      0.018
  )

  frontCoverPivot.add(
    innerCover
  )

  // ===================================================
  // PRIMERA PÁGINA
  // ===================================================

  const firstPage =
    new THREE.Mesh(
      new THREE.PlaneGeometry(
        BOOK.width - 0.20,
        BOOK.height - 0.20
      ),
      materials.pageFront
    )

  firstPage.position.set(
    0,
    0,
    BOOK.pagesDepth / 2 + 0.012
  )

  book.add(firstPage)

  // ===================================================
  // TEXTO
  // ===================================================

  const canvas =
    document.createElement('canvas')

  canvas.width = 1024
  canvas.height = 512

  const ctx =
    canvas.getContext('2d')

  ctx.clearRect(
    0,
    0,
    canvas.width,
    canvas.height
  )

  ctx.fillStyle = '#171717'

  ctx.font =
    '600 86px Arial'

  ctx.textAlign = 'center'
  ctx.textBaseline = 'middle'

  ctx.fillText(
    'Hola Pulo',
    canvas.width / 2,
    canvas.height / 2
  )

  const texture =
    new THREE.CanvasTexture(
      canvas
    )

  texture.colorSpace =
    THREE.SRGBColorSpace

  const textMaterial =
    new THREE.MeshBasicMaterial({
      map: texture,
      transparent: true
    })

  const text =
    new THREE.Mesh(
      new THREE.PlaneGeometry(
        1.8,
        0.8
      ),
      textMaterial
    )

  text.position.set(
    0,
    0.25,
    BOOK.pagesDepth / 2 + 0.024
  )

  book.add(text)

  // ===================================================
  // POSICIÓN
  // ===================================================

  book.position.set(
    0,
    0,
    0
  )

  book.rotation.set(
    0,
    0,
    0
  )
}

// =====================================================
// ABRIR / CERRAR
// =====================================================

const toggleBook = () => {
  if (isAnimating) return

  isOpen = !isOpen
  isAnimating = true

  const startRotation =
    frontCoverPivot.rotation.y

  const targetRotation =
    isOpen
      ? -Math.PI * 0.84
      : 0

  const duration = 850

  const startTime =
    performance.now()

  const animateCover = (time) => {
    const progress =
      Math.min(
        (time - startTime) /
          duration,
        1
      )

    const eased =
      progress < 0.5
        ? 2 * progress * progress
        : 1 -
          Math.pow(
            -2 * progress + 2,
            2
          ) /
            2

    frontCoverPivot.rotation.y =
      startRotation +
      (
        targetRotation -
        startRotation
      ) *
        eased

    if (progress < 1) {
      requestAnimationFrame(
        animateCover
      )
    } else {
      isAnimating = false
    }
  }

  requestAnimationFrame(
    animateCover
  )
}

// =====================================================
// CLICK
// =====================================================

const handlePointerDown = (event) => {
  const rect =
    renderer.domElement.getBoundingClientRect()

  mouse.x =
    ((event.clientX - rect.left) /
      rect.width) *
      2 -
    1

  mouse.y =
    -(
      (event.clientY - rect.top) /
      rect.height
    ) *
      2 +
    1

  raycaster.setFromCamera(
    mouse,
    camera
  )

  const intersections =
    raycaster.intersectObject(
      book,
      true
    )

  if (
    intersections.length > 0 &&
    !isAnimating
  ) {
    toggleBook()
  }
}

// =====================================================
// INIT
// =====================================================

onMounted(() => {
  // ===================================================
  // SCENE
  // ===================================================

  scene = new THREE.Scene()

  scene.background =
    new THREE.Color(0xffffff)

  // ===================================================
  // CAMERA
  // ===================================================

  camera =
    new THREE.PerspectiveCamera(
      45,
      container.value.clientWidth /
        container.value.clientHeight,
      0.1,
      100
    )

  camera.position.set(
    0,
    0,
    7
  )

  camera.lookAt(
    0,
    0,
    0
  )

  // ===================================================
  // RENDERER
  // ===================================================

  renderer =
    new THREE.WebGLRenderer({
      antialias: true,
      alpha: true
    })

  renderer.setPixelRatio(
    Math.min(
      window.devicePixelRatio,
      2
    )
  )

  renderer.setSize(
    container.value.clientWidth,
    container.value.clientHeight
  )

  renderer.outputColorSpace =
    THREE.SRGBColorSpace

  renderer.toneMapping =
    THREE.ACESFilmicToneMapping

  renderer.toneMappingExposure =
    1.05

  renderer.shadowMap.enabled =
    true

  renderer.shadowMap.type =
    THREE.PCFSoftShadowMap

  container.value.appendChild(
    renderer.domElement
  )

  // ===================================================
  // CONTROLS
  // ===================================================

  controls =
    new OrbitControls(
      camera,
      renderer.domElement
    )

  controls.enableDamping = true
  controls.dampingFactor = 0.08

  controls.minDistance = 4
  controls.maxDistance = 9

  controls.minPolarAngle =
    Math.PI * 0.28

  controls.maxPolarAngle =
    Math.PI * 0.72

  // ===================================================
  // LUCES
  // ===================================================

  const ambient =
    new THREE.AmbientLight(
      0xffffff,
      1.8
    )

  scene.add(ambient)

  const key =
    new THREE.DirectionalLight(
      0xffffff,
      3.2
    )

  key.position.set(
    4,
    5,
    7
  )

  key.castShadow = true

  key.shadow.mapSize.set(
    2048,
    2048
  )

  scene.add(key)

  const fill =
    new THREE.DirectionalLight(
      0xffffff,
      1.3
    )

  fill.position.set(
    -5,
    2,
    5
  )

  scene.add(fill)

  const rim =
    new THREE.DirectionalLight(
      0xffffff,
      1
    )

  rim.position.set(
    -2,
    -2,
    -5
  )

  scene.add(rim)

  // ===================================================
  // CREAR LIBRO
  // ===================================================

  createBook()

  // ===================================================
  // INTERACCIÓN
  // ===================================================

  renderer.domElement.addEventListener(
    'pointerdown',
    handlePointerDown
  )

  container.value._handlePointerDown =
    handlePointerDown

  // ===================================================
  // LOOP
  // ===================================================

  const animate = () => {
    animationId =
      requestAnimationFrame(
        animate
      )

    controls.update()

    renderer.render(
      scene,
      camera
    )
  }

  animate()

  // ===================================================
  // RESIZE
  // ===================================================

  const handleResize = () => {
    if (!container.value) return

    camera.aspect =
      container.value.clientWidth /
      container.value.clientHeight

    camera.updateProjectionMatrix()

    renderer.setSize(
      container.value.clientWidth,
      container.value.clientHeight
    )
  }

  window.addEventListener(
    'resize',
    handleResize
  )

  container.value._handleResize =
    handleResize
})

// =====================================================
// CLEANUP
// =====================================================

onBeforeUnmount(() => {
  cancelAnimationFrame(
    animationId
  )

  if (
    container.value?._handleResize
  ) {
    window.removeEventListener(
      'resize',
      container.value._handleResize
    )
  }

  if (
    container.value?._handlePointerDown
  ) {
    renderer.domElement.removeEventListener(
      'pointerdown',
      container.value._handlePointerDown
    )
  }

  controls?.dispose()

  renderer?.dispose()
})
</script>

<template>
  <div
    ref="container"
    class="book-container"
  />
</template>

<style scoped>
.book-container {
  width: 100%;
  height: 100vh;
  overflow: hidden;
  background: #ffffff;
}

.book-container canvas {
  display: block;
  width: 100%;
  height: 100%;
}
</style>

