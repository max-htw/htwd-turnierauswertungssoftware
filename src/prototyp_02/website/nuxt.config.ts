// https://nuxt.com/docs/api/configuration/nuxt-config
import { defineNuxtConfig } from 'nuxt/config'

export default defineNuxtConfig({
  compatibilityDate: '2024-11-01',
  devtools: { enabled: true },
  css:['~/assets/css/main.css'],
  modules: ['@vite-pwa/nuxt', '@nuxtjs/tailwindcss'],
  pwa: {

  },
  nitro: {
    routeRules: {
      "/api/**": {
        proxy: 'http://api:8080/**'
      }
    }
  }
})
