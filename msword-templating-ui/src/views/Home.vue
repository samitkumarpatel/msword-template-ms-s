<template>
  <div class="home">
    <Dashboard />
  </div>
</template>

<script>
// @ is an alias to /src
import HelloWorld from '@/components/HelloWorld.vue'
import Dashboard from '@/components/Dashboard.vue'
export default {
  name: 'home',
  components: {
    HelloWorld,Dashboard
  }
}
</script>
