<template>
    <div class="download">
        <a :href="downloadLink">download</a>
    </div>
</template>
<script>
import JavaApi from "@/services/api/JavaApi.js"
export default {
    name : "download",
    data() {
        return {
            downloadLink : ''
        }
    },
    props:{
        fileName : String
    },
    method : {
    },
    created: function(){
        this.downloadLink = JavaApi.download(this.fileName)
    }
}
</script>
