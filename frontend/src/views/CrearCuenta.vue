<script setup>
import { onBeforeUnmount, onMounted, ref } from 'vue'

/**
 * ============================================================
 * CONFIGURATION
 * ============================================================
 */

const ANIMATION_DURATION = 1000
const INACTIVITY_DELAY = 10000

/**
 * ============================================================
 * STATE
 * ============================================================
 */

const isActive = ref(false)

/**
 * ============================================================
 * TIMERS
 * ============================================================
 */

let animationTimer = null
let inactivityTimer = null

/**
 * ============================================================
 * HELPERS
 * ============================================================
 */

function clearAnimationTimer() {
  if (!animationTimer) return

  window.clearTimeout(animationTimer)
  animationTimer = null
}

function clearInactivityTimer() {
  if (!inactivityTimer) return

  window.clearTimeout(inactivityTimer)
  inactivityTimer = null
}

function stopAnimation() {
  clearAnimationTimer()
  isActive.value = false
}

function playAnimation() {
  clearAnimationTimer()

  isActive.value = true

  animationTimer = window.setTimeout(() => {
    isActive.value = false
    animationTimer = null
  }, ANIMATION_DURATION)
}

function resetInactivityTimer() {
  clearInactivityTimer()

  inactivityTimer = window.setTimeout(() => {
    playAnimation()
  }, INACTIVITY_DELAY)
}

/**
 * ============================================================
 * POINTER / TOUCH INTERACTION
 * ============================================================
 *
 * Pointer Events permiten manejar mouse, touch y stylus
 * con una única API.
 */

function handlePointerEnter() {
  isActive.value = true
  resetInactivityTimer()
}

function handlePointerLeave() {
  isActive.value = false
  resetInactivityTimer()
}

function handlePointerDown() {
  playAnimation()
  resetInactivityTimer()
}

function handlePointerUp() {
  resetInactivityTimer()
}

function handlePointerCancel() {
  stopAnimation()
  resetInactivityTimer()
}

/**
 * Cualquier interacción con la tarjeta reinicia el contador
 * de inactividad.
 */
function handleInteraction() {
  resetInactivityTimer()
}

/**
 * ============================================================
 * LIFECYCLE
 * ============================================================
 */

onMounted(() => {
  resetInactivityTimer()
})

onBeforeUnmount(() => {
  clearAnimationTimer()
  clearInactivityTimer()
})
</script>

<template>
  <div
    class="book-scene"
    :class="{ 'book-scene--active': isActive }"
    @pointerenter="handlePointerEnter"
    @pointerleave="handlePointerLeave"
    @pointerdown="handlePointerDown"
    @pointerup="handlePointerUp"
    @pointercancel="handlePointerCancel"
    @pointermove="handleInteraction"
    @touchstart.passive="handleInteraction"
    @touchend.passive="handlePointerUp"
  >
    <article
      class="book"
      :class="{ 'book--active': isActive }"
      aria-label="Tarjeta de gestión documental"
    >
      <!-- =====================================================
           CARÁTULA
      ====================================================== -->

      <div class="book__cover">
        <div class="book__cover-bg" />

        <!-- Formas decorativas -->
        <div class="shape shape--semicircle" />
        <div class="shape shape--red-block" />
        <div class="shape shape--yellow-circle" />
        <div class="shape shape--red-circle" />
        <div class="shape shape--teal-square" />
        <div class="shape shape--red-square" />

        <!-- Puntos decorativos -->
        <div class="dot-grid dot-grid--top" aria-hidden="true">
          <span
            v-for="n in 24"
            :key="`top-${n}`"
            class="dot-grid__item"
          />
        </div>

        <div class="dot-grid dot-grid--bottom" aria-hidden="true">
          <span
            v-for="n in 20"
            :key="`bottom-${n}`"
            class="dot-grid__item"
          />
        </div>

        <!-- Líneas animadas -->
        <div class="animated-lines" aria-hidden="true">
          <span
            class="animated-lines__line animated-lines__line--diagonal"
          />
          <span
            class="animated-lines__line animated-lines__line--horizontal"
          />
          <span
            class="animated-lines__line animated-lines__line--right"
          />
          <span
            class="animated-lines__line animated-lines__line--vertical"
          />
        </div>

        <!-- ===================================================
             HEADER
        ==================================================== -->

        <header class="cover-header">
          <div class="brand">
            <span>GESTIÓN</span>
            <span>DOCUMENTAL</span>
          </div>

          <div class="header-line">
            <span class="header-line__dot" />
          </div>
        </header>

        <!-- ===================================================
             CONTENIDO PRINCIPAL
        ==================================================== -->

        <main class="cover-main">
          <!-- Carpeta -->
          <div class="folder-circle">
            <div class="folder">
              <div class="folder__tab" />
              <div class="folder__paper" />
            </div>
          </div>

          <!-- Título -->
          <div class="title-group">
            <h1 class="title">
              <span class="title__word title__word--accent">
                EXPLORAR
              </span>

              <span class="title__word title__word--accent">
                UNA
              </span>

              <span class="title__word">
                ORGANIZACIÓN
              </span>

              <span class="title__word">
                DE EJEMPLO
              </span>
            </h1>

            <div class="title__accent" />
          </div>

          <!-- Descripción -->
          <p class="description">
            Información precargada para conocer el sistema de gestión
            documental.
          </p>
        </main>

        <!-- ===================================================
             FEATURES
        ==================================================== -->

        <aside
          class="features-aside"
          aria-label="Características"
        >
          <ul class="features" role="list">
            <!-- Documentos -->
            <li class="feature">
              <div class="feature__icon feature__icon--red">
                <svg
                  viewBox="0 0 24 24"
                  aria-hidden="true"
                >
                  <path
                    d="M14 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V8l-6-6zm4 18H6V4h7v5h5v11z"
                  />
                </svg>
              </div>

              <span class="feature__label">
                DOCUMENTOS
              </span>
            </li>

            <!-- Carpetas -->
            <li class="feature">
              <div class="feature__icon feature__icon--teal">
                <svg
                  viewBox="0 0 24 24"
                  aria-hidden="true"
                >
                  <path
                    d="M20 6h-8l-2-2H4c-1.1 0-2 .9-2 2v12c0 1.1.9 2 2 2h16c1.1 0 2-.9 2-2V8c0-1.1-.9-2-2-2zm0 12H4V6h5.17l2 2H20v10z"
                  />
                </svg>
              </div>

              <span class="feature__label">
                CARPETAS
              </span>
            </li>

            <!-- Usuarios -->
            <li class="feature">
              <div class="feature__icon feature__icon--yellow">
                <svg
                  viewBox="0 0 24 24"
                  aria-hidden="true"
                >
                  <path
                    d="M16 11c1.66 0 3-1.34 3-3s-1.34-3-3-3-3 1.34-3 3 1.34 3 3 3zm-8 0c1.66 0 3-1.34 3-3S9.66 5 8 5 5 6.34 5 8s1.34 3 3 3zm0 2c-2.33 0-7 1.17-7 3.5V19h14v-2.5c0-2.33-4.67-3.5-7-3.5zm8 0c-.29 0-.62.02-.97.05 1.16.84 1.97 1.97 1.97 3.45V19h6v-2.5c0-2.33-4.67-3.5-7-3.5z"
                  />
                </svg>
              </div>

              <span class="feature__label">
                USUARIOS
              </span>
            </li>

            <!-- Permisos -->
            <li class="feature">
              <div class="feature__icon feature__icon--green">
                <svg
                  viewBox="0 0 24 24"
                  aria-hidden="true"
                >
                  <path
                    d="M12 1L3 5v6c0 5.55 3.84 10.74 9 12 5.16-1.26 9-6.45 9-12V5l-9-4zm0 10.99h7c-.53 4.12-3.28 7.79-7 8.94V12H5V6.3l7-3.11v8.8z"
                  />
                </svg>
              </div>

              <span class="feature__label">
                PERMISOS
              </span>
            </li>
          </ul>
        </aside>

        <!-- ===================================================
             EDICIÓN
        ==================================================== -->

        <aside class="edition">
          <strong class="edition__number">
            01
          </strong>

          <span class="edition__label">
            DEMO
          </span>

          <div class="edition-line">
            <span class="edition-line__bar" />
            <span class="edition-line__dot" />
          </div>

          <p class="edition__text">
            Recorré documentos, carpetas, usuarios y permisos con
            <b class="text-accent">
              información precargada.
            </b>
          </p>
        </aside>
      </div>

      <!-- =====================================================
           LOMO
      ====================================================== -->

      <div class="book__spine">
        <span class="book__spine-text">
          GESTIÓN DOCUMENTAL
        </span>
      </div>
    </article>
  </div>
</template>

<style scoped>
/* ============================================================
   DESIGN TOKENS
   ============================================================ */

.book-scene {
  --book-red: var(--color-brand, #8b1e2d);
  --book-red-bright: var(--color-brand-accent, #e63946);
  --book-teal: var(--color-teal, #44a1a4);
  --book-yellow: var(--color-yellow, #ffc349);
  --book-green: var(--color-green, #b2d959);

  --book-bg: var(--color-bg, #f7f3ea);
  --book-surface: var(--color-surface, #fffdf8);
  --book-surface-secondary: var(--color-surface-secondary, #efe9de);
  --book-text: var(--color-text-primary, #242424);
  --book-white: #fff;

  --ease-out: cubic-bezier(0.16, 1, 0.3, 1);
  --ease-smooth: cubic-bezier(0.2, 0.8, 0.2, 1);

  --book-width: min(390px, 100%);
  --book-height: auto;
  --book-aspect-ratio: 390 / 570;
  --book-spine: clamp(24px, 3vw, 35px);
  --book-depth: 12px;

  width: min(100%, 420px);
  aspect-ratio: 420 / 620;

  display: grid;
  place-items: center;

  perspective: 1800px;
  margin-inline: auto;

  touch-action: manipulation;
  -webkit-tap-highlight-color: transparent;
}

/* ============================================================
   BOOK
   ============================================================ */

.book {
  position: relative;

  width: var(--book-width);
  max-width: 100%;
  aspect-ratio: var(--book-aspect-ratio);

  transform-style: preserve-3d;
  transform: rotateY(-8deg) rotateX(2deg);

  transition:
    transform 700ms var(--ease-smooth),
    filter 500ms ease;

  filter: drop-shadow(
    25px 30px 25px rgba(0, 0, 0, 0.18)
  );

  user-select: none;
}

/*
 * Estado activo.
 *
 * Este estado reemplaza la antigua dependencia de :hover.
 * Vue controla cuándo debe ejecutarse la animación.
 */
.book--active {
  transform:
    rotateY(-16deg)
    rotateX(3deg)
    translateY(-8px)
    translateZ(15px);
}

/* ============================================================
   COVER
   ============================================================ */

.book__cover {
  position: absolute;
  inset: 0;

  overflow: hidden;

  background: var(--book-bg);

  border-radius: 3px 7px 7px 3px;

  transform: translateZ(var(--book-depth));

  box-shadow: inset -8px 0 12px rgba(0, 0, 0, 0.08);

  font-family:
    var(--font-sans),
    Inter,
    system-ui,
    -apple-system,
    BlinkMacSystemFont,
    "Segoe UI",
    sans-serif;
}

.book__cover-bg {
  position: absolute;
  inset: 0;

  background: var(--book-bg);
}

/* ============================================================
   DECORATIVE SHAPES
   ============================================================ */

.shape {
  position: absolute;
  pointer-events: none;
}

/* Semicírculo superior */

.shape--semicircle {
  width: 310px;
  height: 310px;

  top: -170px;
  right: -120px;

  border-radius: 50%;

  background: var(--book-red);

  transform: scale(0.3) translateY(-30px);
  opacity: 0;

  transition:
    transform 700ms var(--ease-out),
    opacity 350ms ease;
}

.book--active .shape--semicircle {
  opacity: 1;
  transform: scale(1) translateY(0);
}

/* Bloque rojo principal */

.shape--red-block {
  left: -50px;
  bottom: -70px;

  width: 310px;
  height: 250px;

  background: var(--book-red);

  clip-path: polygon(
    0 0,
    100% 0,
    100% 100%,
    35% 100%
  );
}

/* Círculo amarillo */

.shape--yellow-circle {
  left: -110px;
  bottom: -170px;

  width: 280px;
  height: 280px;

  border-radius: 50%;

  background: var(--book-yellow);

  transform: translateX(-60px);

  transition: transform 800ms var(--ease-out);
}

.book--active .shape--yellow-circle {
  transform: translateX(0);
}

/* Círculo rojo pequeño */

.shape--red-circle {
  width: 55px;
  height: 55px;

  left: 160px;
  bottom: 75px;

  border-radius: 50%;

  background: var(--book-red);

  opacity: 0;
  transform: scale(0.3);

  transition:
    opacity 300ms ease,
    transform 500ms var(--ease-out);
}

.book--active .shape--red-circle {
  opacity: 1;
  transform: scale(1);
}

/* Cuadrado teal */

.shape--teal-square {
  width: 48px;
  height: 48px;

  right: 20px;
  bottom: 105px;

  background: var(--book-teal);

  opacity: 0;
  transform: translateY(20px);

  transition:
    opacity 300ms ease 150ms,
    transform 500ms ease 150ms;
}

.book--active .shape--teal-square {
  opacity: 1;
  transform: translateY(0);
}

/* Cuadrado rojo pequeño */

.shape--red-square {
  width: 26px;
  height: 26px;

  right: 55px;
  bottom: 140px;

  background: var(--book-red-bright);

  opacity: 0;

  transition: opacity 300ms ease 250ms;
}

.book--active .shape--red-square {
  opacity: 1;
}

/* ============================================================
   DOT GRID
   ============================================================ */

.dot-grid {
  position: absolute;

  display: grid;
  grid-template-columns: repeat(6, 4px);
  gap: 9px;

  z-index: 1;
}

.dot-grid__item {
  width: 3px;
  height: 3px;

  border-radius: 50%;

  transform: scale(0);

  transition: transform 350ms ease;
}

/* Header dots */

.dot-grid--top {
  top: 22px;
  right: 30px;
}

.dot-grid--top .dot-grid__item {
  background: var(--book-white);
}

/* Bottom dots */

.dot-grid--bottom {
  right: 125px;
  bottom: 45px;

  z-index: 2;
}

.dot-grid--bottom .dot-grid__item {
  background: var(--book-bg);
}

.book--active .dot-grid__item {
  transform: scale(1);
}

.book--active .dot-grid__item:nth-child(2n) {
  transition-delay: 40ms;
}

.book--active .dot-grid__item:nth-child(3n) {
  transition-delay: 80ms;
}

/* ============================================================
   ANIMATED LINES
   ============================================================ */

.animated-lines {
  position: absolute;
  inset: 0;

  pointer-events: none;

  z-index: 4;
}

.animated-lines__line {
  position: absolute;

  background: var(--book-text);

  transform-origin: left;

  transform: scaleX(0);

  transition: transform 650ms var(--ease-out);
}

/* Diagonal */

.animated-lines__line--diagonal {
  left: 0;
  bottom: 190px;

  width: 210px;

  transform:
    rotate(45deg)
    scaleX(0);
}

.book--active .animated-lines__line--diagonal {
  transform:
    rotate(45deg)
    scaleX(1);
}

/* Horizontal */

.animated-lines__line--horizontal {
  left: 0;
  bottom: 190px;

  width: 190px;
}

.book--active .animated-lines__line--horizontal {
  transform: scaleX(1);
}

/* Right */

.animated-lines__line--right {
  right: 0;
  top: 280px;

  width: 130px;

  transform-origin: right;
}

.book--active .animated-lines__line--right {
  transform: scaleX(1);
}

/* Vertical */

.animated-lines__line--vertical {
  right: 70px;
  top: 280px;

  width: 1px;
  height: 170px;

  transform-origin: top;

  transform: scaleY(0);
}

.book--active .animated-lines__line--vertical {
  transform: scaleY(1);
}

/* ============================================================
   HEADER
   ============================================================ */

.cover-header {
  position: absolute;

  top: 35px;
  left: 35px;
  right: 35px;

  display: flex;
  align-items: flex-start;
  justify-content: space-between;

  z-index: 10;
}

.brand {
  display: flex;
  flex-direction: column;

  font-size: 12px;
  font-weight: 700;

  line-height: 1.25;

  color: var(--book-text);

  letter-spacing: 0.3px;
}

.header-line {
  position: relative;
  top: 8px;

  width: 125px;
  height: 1px;

  background: var(--book-text);

  transform-origin: left;
  transform: scaleX(0);

  transition:
    transform 600ms var(--ease-smooth);

  z-index: 3;
}

.book--active .header-line {
  transform: scaleX(1);
}

.header-line__dot {
  position: absolute;

  right: -5px;
  top: -4px;

  width: 9px;
  height: 9px;

  border-radius: 50%;

  background: var(--book-red);
}

/* ============================================================
   MAIN CONTENT
   ============================================================ */

.cover-main {
  position: absolute;

  top: 90px;
  left: 35px;
  right: 35px;
  bottom: 220px;

  z-index: 5;
}

/* ============================================================
   FOLDER
   ============================================================ */

.folder-circle {
  position: absolute;

  right: 0;
  top: 75px;

  width: 140px;
  height: 140px;

  border-radius: 50%;

  background: var(--book-surface-secondary);

  display: flex;
  align-items: center;
  justify-content: center;

  transition:
    transform 700ms var(--ease-smooth);

  z-index: 2;
}

.book--active .folder-circle {
  transform:
    translateY(5px)
    scale(1.04);
}

.folder {
  position: relative;

  width: 82px;
  height: 64px;

  background: var(--book-red);
}

.folder__tab {
  position: absolute;

  width: 36px;
  height: 14px;

  top: -10px;
  left: 0;

  background: var(--book-red);
}

.folder__paper {
  position: absolute;

  width: 80px;
  height: 52px;

  right: -4px;
  bottom: -3px;

  background:
    linear-gradient(
      145deg,
      var(--book-surface) 0%,
      var(--book-surface) 48%,
      #202124 49%,
      #202124 100%
    );
}

/* ============================================================
   TITLE
   ============================================================ */

.title-group {
  position: relative;

  max-width: 210px;

  z-index: 3;
}

.title {
  display: flex;
  flex-direction: column;

  margin: 0;

  font-size: clamp(28px, 5vw, 38px);
  line-height: 0.95;

  font-weight: 900;
  letter-spacing: -1.5px;

  color: var(--book-text);
}

.title__word {
  display: block;

  transform: translateX(-8px);
  opacity: 0.96;

  transition:
    transform 500ms var(--ease-smooth);
}

.book--active .title__word {
  transform: translateX(0);
}

.title__word--accent {
  color: var(--book-red);
}

.title__accent {
  width: 55px;
  height: 5px;

  margin-top: 14px;

  background: var(--book-red);

  transform-origin: left;
  transform: scaleX(0);

  transition:
    transform 500ms var(--ease-smooth) 100ms;
}

.book--active .title__accent {
  transform: scaleX(1);
}

/* ============================================================
   DESCRIPTION
   ============================================================ */

.description {
  position: relative;

  max-width: 200px;

  margin: 18px 0 0;

  font-size: 14px;
  line-height: 1.5;

  color: var(--book-text);
  font-weight: 400;

  z-index: 3;
}

/* ============================================================
   FEATURES
   ============================================================ */

.features-aside {
  position: absolute;

  left: 12px;
  top: 392px;

  width: 240px;
  height: 68px;

  overflow: hidden;

  z-index: 20;

  transform-origin: left top;
  transform:
    translate(-10px, -10px)
    scale(0.96);

  opacity: 0;

  transition:
    transform 600ms var(--ease-out),
    opacity 400ms ease;
}

.book--active .features-aside {
  transform:
    translate(0, 0)
    scale(1);

  opacity: 1;
}

.features {
  width: 100%;
  height: 100%;

  display: flex;
  align-items: flex-start;
  justify-content: flex-start;

  gap: 4px;

  padding: 6px 4px 0 6px;
  margin: 0;

  box-sizing: border-box;

  list-style: none;
}

.feature {
  width: 54px;
  min-width: 54px;
  flex: 0 0 54px;

  display: flex;
  flex-direction: column;

  align-items: center;
  justify-content: flex-start;

  gap: 4px;

  text-align: center;

  transform: translateX(-10px);
  opacity: 0;

  transition:
    transform 400ms var(--ease-out),
    opacity 350ms ease;
}

.book--active .feature:nth-child(1) {
  transition-delay: 80ms;
}

.book--active .feature:nth-child(2) {
  transition-delay: 150ms;
}

.book--active .feature:nth-child(3) {
  transition-delay: 220ms;
}

.book--active .feature:nth-child(4) {
  transition-delay: 290ms;
}

.book--active .feature {
  transform: translateX(0);
  opacity: 1;
}

/* ============================================================
   FEATURE ICON
   ============================================================ */

.feature__icon {
  width: 30px;
  height: 30px;

  border: 2px solid var(--book-white);
  border-radius: 50%;

  display: flex;
  align-items: center;
  justify-content: center;

  box-sizing: border-box;

  color: var(--book-white);

  flex-shrink: 0;
}

.feature__icon svg {
  width: 15px;
  height: 15px;

  display: block;
}

.feature__icon--red {
  background: var(--book-red);
}

.feature__icon--teal {
  background: var(--book-teal);
}

.feature__icon--yellow {
  background: var(--book-yellow);
  color: var(--book-text);
}

.feature__icon--green {
  background: var(--book-green);
  color: var(--book-text);
}

/* ============================================================
   FEATURE LABEL
   ============================================================ */

.feature__label {
  display: block;

  width: 100%;

  font-size: 10px;
  line-height: 1.05;
  font-weight: 700;

  color: var(--book-white);

  white-space: nowrap;
  text-align: center;

  letter-spacing: 0.1px;

  overflow: hidden;
  text-overflow: clip;
}

/* ============================================================
   EDITION
   ============================================================ */

.edition {
  position: absolute;

  right: 25px;
  bottom: 18px;

  width: 105px;

  color: var(--book-text);

  z-index: 15;
}

.edition__number {
  display: block;

  font-size: 46px;
  line-height: 0.9;

  font-weight: 700;

  color: var(--book-red);
}

.edition__label {
  display: block;

  margin-top: 8px;

  font-size: 15px;
  font-weight: 600;

  letter-spacing: 0.5px;
}

/* ============================================================
   EDITION LINE
   ============================================================ */

.edition-line {
  display: flex;
  align-items: center;

  margin: 10px 0;
}

.edition-line__bar {
  height: 4px;
  width: 0;

  background: var(--book-red);
  border-radius: 2px;

  transform-origin: left;

  transition:
    width 500ms var(--ease-smooth);
}

.edition-line__dot {
  width: 8px;
  height: 8px;
  flex: 0 0 8px;

  border-radius: 50%;

  background: var(--book-red);

  margin-left: 2px;

  opacity: 0;
  transform: scale(0);

  transition:
    opacity 300ms ease 200ms,
    transform 400ms var(--ease-out) 200ms;
}

.book--active .edition-line__bar {
  width: 35px;
}

.book--active .edition-line__dot {
  opacity: 1;
  transform: scale(1);
}

/* ============================================================
   EDITION TEXT
   ============================================================ */

.edition__text {
  margin: 0;

  font-size: 10.5px;
  line-height: 1.45;

  font-weight: 400;
}

.text-accent {
  color: var(--book-red);
  font-weight: 700;
}

/* ============================================================
   SPINE
   ============================================================ */

.book__spine {
  position: absolute;

  left: calc(var(--book-spine) * -1);
  top: 0;

  width: var(--book-spine);
  height: 100%;

  background: var(--book-red);

  transform:
    rotateY(-90deg)
    translateZ(var(--book-depth));

  transform-origin: right center;

  display: flex;
  align-items: center;
  justify-content: center;
}

.book__spine-text {
  color: var(--book-bg);

  font-size: clamp(7px, 1.8vw, 9px);
  font-weight: 600;

  writing-mode: vertical-rl;
  transform: rotate(180deg);

  letter-spacing: 1.5px;
}

/* ============================================================
   DEPTH
   ============================================================ */

.book__cover::after {
  content: "";

  position: absolute;

  top: 0;
  right: -20px;

  width: 20px;
  height: 100%;

  background:
    linear-gradient(
      to right,
      rgba(0, 0, 0, 0.12),
      rgba(0, 0, 0, 0.02)
    );

  transform: translateZ(-6px);

  pointer-events: none;
}

/* ============================================================
   RESPONSIVE
   ============================================================ */

/*
 * Tablet
 */

@media (max-width: 768px) {
  .book-scene {
    perspective: 1400px;
  }

  .book {
    width: min(390px, 92vw);
  }
}

/*
 * Mobile
 *
 * No usamos transform: scale() sobre .book-scene.
 * El tamaño se adapta mediante width/aspect-ratio.
 */

@media (max-width: 480px) {
  .book-scene {
    width: 100%;
    max-width: 390px;

    aspect-ratio: 390 / 570;

    perspective: 1200px;
  }

  .book {
    width: min(100%, 360px);
  }

  .cover-header {
    top: clamp(24px, 7vw, 35px);
    left: clamp(24px, 7vw, 35px);
    right: clamp(24px, 7vw, 35px);
  }

  .brand {
    font-size: clamp(9px, 2.7vw, 12px);
  }

  .header-line {
    width: clamp(80px, 25vw, 125px);
  }

  .cover-main {
    top: clamp(70px, 18vw, 90px);
    left: clamp(24px, 7vw, 35px);
    right: clamp(24px, 7vw, 35px);
    bottom: 220px;
  }

  .folder-circle {
    width: clamp(105px, 32vw, 140px);
    height: clamp(105px, 32vw, 140px);

    top: clamp(60px, 18vw, 75px);
  }

  .folder {
    width: clamp(64px, 20vw, 82px);
    height: clamp(50px, 16vw, 64px);
  }

  .folder__paper {
    width: clamp(62px, 20vw, 80px);
    height: clamp(40px, 13vw, 52px);
  }

  .title-group {
    max-width: 58%;
  }

  .title {
    font-size: clamp(24px, 8vw, 32px);
  }

  .description {
    max-width: 48%;
    margin-top: 14px;

    font-size: clamp(10px, 3vw, 12px);
  }

  .features-aside {
    left: 10px;
    top: 392px;

    width: 230px;
    height: 68px;
  }

  .features {
    gap: 3px;
    padding: 6px 4px 0 6px;
  }

  .feature {
    width: 52px;
    min-width: 52px;
    flex-basis: 52px;
  }

  .feature__icon {
    width: 28px;
    height: 28px;
  }

  .feature__icon svg {
    width: 14px;
    height: 14px;
  }

  .feature__label {
    font-size: 7px;
  }

  .edition {
    right: 18px;
    bottom: 14px;

    width: 90px;
  }

  .edition__number {
    font-size: 36px;
  }

  .edition__label {
    font-size: 13px;
  }

  .edition__text {
    font-size: 9px;
  }

  .dot-grid--top {
    right: 20px;
  }

  .dot-grid--bottom {
    right: 100px;
  }
}

/*
 * Pantallas muy pequeñas.
 */

@media (max-width: 360px) {
  .book {
    width: 100%;
  }

  .cover-main {
    left: 28px;
    right: 28px;
  }

  .features-aside {
    left: 8px;
    width: 220px;
  }

  .feature {
    width: 50px;
    min-width: 50px;
    flex-basis: 50px;
  }

  .edition {
    right: 14px;
    width: 82px;
  }

  .edition__number {
    font-size: 32px;
  }
}

/* ============================================================
   REDUCED MOTION
   ============================================================ */

@media (prefers-reduced-motion: reduce) {
  .book,
  .shape,
  .dot-grid__item,
  .animated-lines__line,
  .header-line,
  .title__word,
  .title__accent,
  .folder-circle,
  .features-aside,
  .feature,
  .edition-line__bar,
  .edition-line__dot {
    transition: none !important;
  }

  .book--active {
    transform: rotateY(-8deg) rotateX(2deg);
  }
}
</style>
