<template>
    <div class="largeModal">
        <a href="#" data-toggle="modal" :data-target="'.bd-example-modal-lg-'+index" @click="readApiData">{{label}}</a>
        <div class="modal fade" :class="'bd-example-modal-lg-'+index" tabindex="-1" role="dialog" aria-labelledby="myLargeModalLabel" aria-hidden="true">
            <div class="modal-dialog modal-lg">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title">{{modalHeader}}</h5>
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                            <span aria-hidden="true">×</span>
                        </button>
                        
                    </div>
                    <div class="modal-body">
                        <pre>{{prevData}}</pre>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>
<script>
import JavaApi from "@/services/api/JavaApi.js"
export default {
  name: 'largeModal',
  data() {
    return {
        prevData: Object
    }
  },
  props:{
      label : String,
      fileName : String,
      path : String,
      modalHeader : String,
      index : String
  },
  methods:{
      readApiData(){
        JavaApi.preview(this.fileName,this.path)
        .then(prevData=> {
            this.prevData = prevData
        })
        .catch(error=>{
            this.prevData = "an error occured"   
        })
      }
  }
}
</script>
