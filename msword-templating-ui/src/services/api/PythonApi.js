import axios from 'axios'
var baseUrl = "/python/api/process/docxgen" 
export default {
    docxConvertor(data,template,fileNameToBeGenerated) {
        let header = {
            'Content-Type': 'application/x-www-form-urlencoded'
        }
        let apiData = {
            "data" : data,
            "template" : template,
            "fileNameToBeGenerated" : fileNameToBeGenerated
        }
        return axios.post(baseUrl,apiData,header)
        .then(responce => {
            return responce.data
        })
    }
}