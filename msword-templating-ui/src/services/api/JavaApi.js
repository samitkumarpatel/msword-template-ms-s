import axios from 'axios'

export default {
    getTemplate() {
        return axios.get('/java/api/templates/all')
        .then(responce=> {
            return responce.data
        })
    },
    getTemplateProperties(fileName) {
        return axios.get('/java/api/templates/prop/'+fileName)
        .then(responce=> {
            return responce.data
        })
    },
    preview(fileName,path) {
        let url;
        if(path!=undefined) {
            url= "/java/api/read?fileName="+fileName+"&path="+path
        }else {
            url= "/java/api/read?fileName="+fileName
        }
        return axios.get(url)
        .then(responce=> {
            return responce.data
        })
    },
    explorer(type,fromFolder){
        let url;
        if(fromFolder==undefined){
            url = '/java/api/filesystem?type='+type
        }else{
            url = '/java/api/filesystem?type='+type+'&fromFolder='+fromFolder
        }
        return axios.get(url)
        .then(responce=> {
            return responce.data
        })
    },
    upload(formData) {
        let header = {
            'Content-Type': 'multipart/form-data'
        }
        return axios.post('/java/api/upload',formData,header)
        .then(responce=> {
            return responce.data
        })
    },
    download(fileName) {
        if(axios.defaults.baseURL!=undefined){
            return axios.defaults.baseURL+'/java/api/download?fileName='+fileName
        } else {
            return '/java/api/download?fileName='+fileName
        }
    }
}