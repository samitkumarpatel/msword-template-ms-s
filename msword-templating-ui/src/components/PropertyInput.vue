<template>
  <div class="propertyInput">
    <div class="form-row">
      <div class="col-md-6 mb-3">
        <label
          class="my-1 mr-2"
          for="selectTemplateToGenerateReport"
        >Select template to generate report</label>
        <select
          class="custom-select my-1 mr-sm-2"
          id="selectTemplateToGenerateReport"
          v-model="selected"
        >
          <option disabled value>Please select one</option>
          <option :value="option" v-for="(option,opIdx) in options" :key="opIdx">{{option}}</option>
        </select>
        <label class="my-1 mr-2" for="templatePrev" v-if="templateChanged">
          <largemodal label="To preview the template "/>
        </label>
        <label class="my-1 mr-2" for="forNewTemplate">expected template not in the list?,
          <a href="/admin">click</a> to upload one
        </label>
      </div>
    </div>
    <div v-if="templateChanged">
      <table class="table">
        <tbody>
          <tr v-for="(r,rowIdx) in tableRows" :key="rowIdx">
            <td v-for="(col,colIdx) in tableProperties" :key="colIdx">
              <small>{{col}}</small>
              <input
                type="text"
                class="form-control"
                :id="colIdx"
                aria-describedby="inputHelp"
                placeholder
                v-model="tableRows[rowIdx][col]"
              >
            </td>
            <td v-if="incrementButton">
             <br/>
              <a href="#" @click="insertRow()" v-if="rowIdx == tableRows.length-1">+</a>
              <a href="#" v-if="tableRows.length > 1" @click="removeRow(rowIdx,tableRows[rowIdx])">-</a>
            </td>
            <td>
              <small v-if="!incrementButton">{{processingText}}</small>
              <div :class="'download_'+rowIdx">
                <spinner v-if="!incrementButton"/>
              </div>
            </td>
          </tr>
        </tbody>
      </table>
      <p>{{tableRows}}</p>
      <button type="button" class="btn btn-info" @click="process()" :disabled="!incrementButton">Process data</button>
      <button type="button" class="btn btn-info gap" @click="saveForLater()">Save the data for later use</button>
    </div>
  </div>
</template>
<script>
import largemodal from "@/views/LargeModal.vue";
import spinner from "@/views/Spinner.vue"
export default {
  name: "PropertyInput",
  components: {
    largemodal,spinner
  },
  props: {},
  data() {
    return {
      selected: "",
      templateChanged: false,
      tableProperties: ['a','b','c','d','e','f','g','h'],
      options: ['one','two','three','four'],
      tableValueAgainstTableProperty: {},
      tableRows: [],
      incrementButton:true,
      processingText: '',
      r:{}

    };
  },
  methods: {
    init(){
      this.tableRows.push(this.tableValueAgainstTableProperty);
    },
    insertRow() {
      this.tableValueAgainstTableProperty = {};
      this.tableRows.push(this.tableValueAgainstTableProperty);
    },
    removeRow(rowIdx, removedData) {
      this.tableRows.splice(rowIdx, 1);
    },
    process(){
      this.incrementButton=false
      this.processingText='processing ..'
      for(let i=0;i < this.tableRows.length;i++){
        this.downloadLink(i);
      }
    },
    saveForLater(){
    },
    downloadLink(index){
      this.processingText='docx is ready to downlaod & view'
      $('.download_'+index).html('<a href="#">download</a>')    
    } 
  },
  watch: {
    selected: function(newSelectedTemplate, oldSelectedTemplate) {
      if(newSelectedTemplate!= oldSelectedTemplate) {
        this.templateChanged = true;
      }
    }
  },
  created: function() {
    this.init()
  }
};
</script>
<style scope>
a {
  margin-left: 10px;
}
button {
  margin-left: 10px;
}
</style>