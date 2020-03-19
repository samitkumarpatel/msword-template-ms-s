<template>
  <div class="admin">
    <ul class="nav nav-tabs">
        <li class="nav-item">
            <a class="nav-link active" data-toggle="tab" href="#upload">upload</a>
        </li>
        <li class="nav-item">
            <a class="nav-link" data-toggle="tab" href="#manageTemplate">Manage Template</a>
        </li>
        <li class="nav-item">
            <a class="nav-link" data-toggle="tab" href="#cleanUp">Clean Up</a>
        </li>
    </ul>

    <div class="tab-content">
        <div id="upload" class="tab-pane fade in show active">
            <div class="form-group">
                <label for="uploadTemplate">Upload Template</label>
                <input type="file" class="form-control-file" id="uploadTemplate" ref="file" v-on:change="handleFileUpload()">
            </div>
            <div class="form-group">
                <button type="button" class="btn btn-info" @click="submitFile()">Upload</button>
            </div>
            <div class="alert" role="alert" :class="status == 500? 'alert-danger':'alert-info' " v-show="status!=''">
                {{result}}
            </div>
        </div>
        <div id="manageTemplate" class="tab-pane fade">
            <p>Under Construction</p>
        </div>
        <div id="cleanUp" class="tab-pane fade">
            <p>Under Construction</p>
        </div>
    </div>

  </div>
</template>
<script>
import JavaApi from "@/services/api/JavaApi.js"
export default {
    name : "admin",
    data() {
        return {
            file : '',
            status: '',
            result : ''
        }
    },
    methods:{
        handleFileUpload(){
            this.status=''
            this.file = this.$refs.file.files[0];              
        },
        submitFile(){
            let formData = new FormData();
            formData.append('file', this.file);
            JavaApi.upload(formData).then(res=> {
                this.status = res.status
                this.result = res.result
            }).catch(err =>{
                this.status = err.response.data.status
                this.result = err.response.data.message
            })
        }
    }
}
</script>
<style scoped>
    div #upload {
        margin-top:30px
    }
    div #manageTemplate {
        margin-top:30px
    }
    div #cleanUp {
        margin-top:30px
    }
</style>

