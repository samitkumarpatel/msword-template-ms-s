<template>
  <div class="propertyInput">
    <div class="row">
      <div class="col-3 form__inputs">
        <div class="form-group">
          <label for="template">Select template to generate report</label>
          <select class="form-control" id="selectTemplateToGenerateReport" v-model="selected">
            <option disabled value>Please select one</option>
            <option :value="option.name" v-for="(option,opIdx) in options" :key="opIdx">{{option.name}}</option>
          </select>
        </div>
        <hr v-if="templateChanged"/>
        <small class="form-text text-muted" v-if="templateChanged">
         <largemodal label="template preview" :fileName="selected" path="TEMPLATE" :modalHeader="selected" />
        </small>
        <hr/>
        <small class="form-text text-muted">
          template not in the list?<a href="#" @click="$router.push('admin')">click</a> to create
        </small>
        <hr/>
        <div v-if="templateChanged">
          <forminput :tableProperties="tableProperties" v-on:add-data="add($event)"/>
        </div>
      </div><!-- inputs -->
      <div class="col-9">
        <table class="table table-striped">
          <tbody>
            <tr v-for="(tRow,tRowI) in tableRows" :key="tRowI">
              <td v-for="(rCol,rColI) in Object.keys(tRow)" :key="rColI">
                <small><u>{{rCol}}</u></small><br/>
                {{tRow[rCol]}}
              </td>
              <td>
                <button type="button" class="btn btn-danger" @click="removeRow(tRowI,tableRows[tRowI])" v-if="dataRemoveLink">-</button>
              </td>
              <td>
                <div :class="'download_'+tRowI">
                  <Spinner v-if="spinner[tRowI]"/>
                </div>
                <div :class="'view_'+tRowI">
                  
                </div>
              </td>
            </tr>
          </tbody>
        </table>
        <div v-if="tableRows.length > 0">
          <button type="button" class="btn btn-info" @click="process()" :disabled="processbtn">Process data</button>          
        </div>
      </div><!-- added view-->
    </div>
    
  </div>
</template>
<script>
import largemodal from "@/views/LargeModal.vue";
import Spinner from "@/views/Spinner.vue"
import JavaApi from "@/services/api/JavaApi.js"
import PythonApi from "@/services/api/PythonApi.js"
import download from "@/views/Download.vue";
import forminput from "@/views/FormInput.vue";
import axios from 'axios';
export default {
  name: "PropertyInput",
  components: {
    largemodal,Spinner,download,forminput
  },
  props: {},
  data() {
    return {
      selected: "",
      templateChanged: false,
      tableProperties: ["fileName"],
      options: [],
      tableValueAgainstTableProperty: {},
      tableRows: [],
      processbtn:false,
      previewData: "",
      spinner:{},
      dataRemoveLink : true,
      fileRef : {}
    };
  },
  methods: {
    add(data){
      this.tableRows.push(data);
    },
    removeRow(rowIdx, removedData) {
      this.tableRows.splice(rowIdx, 1);
    },
    process() {
      this.dataRemoveLink=false
      this.processbtn=true
      this.templateChanged=false
      for(let i=0;i < this.tableRows.length;i++) {
        this.spinner[i] = true
        let data = this.tableRows[i]
        let keys = Object.keys(data)        
        PythonApi.docxConvertor(data,this.selected,data.fileName)
        .then(res=> {
          this.fileRef[i] = res.reference
          //download link
          let dL;
          if(axios.defaults.baseURL!=undefined){
            dL = axios.defaults.baseURL+"/java/api/download?fileName="+res.reference
          } else {
            dL = "/java/api/download?fileName="+res.reference
          }
          $('.download_'+i).html("<a href="+dL+">download</a>");
        })
        .catch(error=> {
          $('.download_'+i).html("ERROR");
        })
      }//for loop
    },
    saveForLater(){
    }
  },
  watch: {
    selected: function(newSelectedTemplate, oldSelectedTemplate) {
      if(newSelectedTemplate!= oldSelectedTemplate) {
        this.templateChanged = true;
        this.tableRows=[]
        JavaApi.getTemplateProperties(newSelectedTemplate)
        .then(props=> {
          this.tableProperties = this.tableProperties.concat(props)
        })
      }
    }
  },
  created:function(){
    JavaApi.getTemplate()
    .then(template=> {
      this.options = template
    })
    .catch(error=> {
      console.log(error)
    })
  }
};
</script>
<style scope>
.form__inputs {
  border-right: 1px solid gray; 
}
</style>