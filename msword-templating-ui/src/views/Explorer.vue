<template>
    <div class="explorer">
        <p>click the link to expand/collapse</p>
        <p v-if="folders.length == 0">nothing is available on the traget target</p>

        <div class="accordion" id="accordionExample">
            <div class="card" v-for="(folder,folderIdx) in folders" :key="folderIdx">
                <div class="card-header" :id="'heading_'+folderIdx">
                    <h2 class="mb-0">
                        <button class="btn btn-link" type="button" data-toggle="collapse" :data-target="'#collapse'+folderIdx" aria-expanded="true" :aria-controls="'collapse'+folderIdx" @click="fileForFolder(folder.name)">
                        {{folder.name}}
                        </button>
                    </h2>
                    
                </div>
                <div :id="'collapse'+folderIdx" class="collapse" :aria-labelledby="'heading_'+folderIdx" data-parent="#accordionExample">
                    <div class="card-body">
                        <p>Last modified : {{folder.lastModified}}</p> 
                        <p v-if="files.length == 0">0 files available</p>
                        <table class="table">
                            <tbody>
                                <tr v-for="(file,fileIndex) in files" :key="fileIndex">
                                    <td v-for="(filekey,filekeyIndex) in Object.keys(file)" :key="filekeyIndex" v-show="filekey!='path'"> 
                                        <small><u>{{filekey}}</u></small> <br/> 
                                        {{file[filekey]}}
                                    </td>
                                    <td>
                                        <download :fileName="file['path']"/>
                                    </td>
                                    <td>
                                        <largemodal label="view" :fileName="file['path']" path="DOWNLOAD" :modalHeader="file['path']" :index="folderIdx+'-'+fileIndex" />
                                    </td>
                                    <td>
                                        <a href="#" class="disabled">delete</a>
                                    </td>
                                </tr>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>
  </div>
</template>
<script>
import JavaApi from "@/services/api/JavaApi.js"
import download from "@/views/Download.vue";
import largemodal from "@/views/LargeModal.vue";
export default {
    name: 'explorer',
    components: {
        download,largemodal
    },
    data() {
        return {
            folders:[],
            files:[],
            error: ''
        }
    },
    methods: {
        fileForFolder(folderName){
            JavaApi.explorer('FILE',folderName)
            .then(files=> {
                this.files = files
            })
            .catch(error=> {
                console.log(error)
            }) 
        }
    },
    created:function(){
        JavaApi.explorer('FOLDER',undefined)
        .then(folder=> {
            this.folders = folder
        })
        .catch(error=> {
            this.error=error    
        })
    }
}
</script>
<style scoped>
section {
    margin-top: 10px;
}
.disabled {
  pointer-events: none;
  cursor: default;
  opacity: 0.6;
}
</style>

