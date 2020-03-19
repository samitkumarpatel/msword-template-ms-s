<template>
    <div class="forminput">
        <button type="button" class="btn btn-primary" data-toggle="modal" data-target=".bd-example-modal-xl">Add Data</button>
        <div class="modal fade bd-example-modal-xl" tabindex="-1" role="dialog" aria-labelledby="myExtraLargeModalLabel" aria-hidden="true">
        <div class="modal-dialog modal-xl">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="exampleModalLabel">New message</h5>
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                    </button>
                </div>
                <div class="modal-body">
                    <div class="form-row align-items-center">
                        <div class="col-auto" v-for="(col,colIdx) in tableProperties" :key="colIdx">
                            <label :for="'labelFor'+col">{{col}}</label>
                            <input type="text" class="form-control" :id="col+'_'+colIdx" :aria-describedby="col+'Help'" placeholder="" v-model="tableValueAgainstTableProperty[col]">
                        </div>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
                    <button type="button" class="btn btn-primary" @click="add()" :disabled="Object.keys(this.tableValueAgainstTableProperty).length <= 0" data-dismiss="modal">Add</button>
                </div>
            </div>
        </div>
        </div>
    </div>
</template>
<script>
export default {
    name : "forminput",
    props : {
        tableProperties: Array,

    },
    data() {
        return {
            tableValueAgainstTableProperty: {}
        }
    },
    methods : {
        add() {
            this.$emit('add-data', this.tableValueAgainstTableProperty);
            this.tableValueAgainstTableProperty = {}
        }
    }
}
</script>