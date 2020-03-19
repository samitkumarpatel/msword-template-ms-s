<template>
  <div class="dashboard">
    <PropertyInput2/>
  </div>
</template>

<script>
import PropertyInput from '@/components/PropertyInput.vue'
import PropertyInput2 from '@/components/PropertyInput.2.vue'
export default {
  name: "Dashboard",
  components: {
    PropertyInput,PropertyInput2
  },
  props: {
    msg: String
  },
  data() {
    return {
      
    };
  },
  methods: {
    
  },
  created: function(){ 
  }
};
</script>

<style scoped>
</style>
