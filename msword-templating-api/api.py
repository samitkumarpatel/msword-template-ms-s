import os
from flask import Flask, request
from flask_restful import reqparse, abort, Api, Resource
from docxtpl import DocxTemplate
from datetime import date
from flask_cors import CORS

app = Flask(__name__)
CORS(app)
api = Api(app)
parser = reqparse.RequestParser()
parser.add_argument('data')
parser.add_argument('template')
parser.add_argument('fileNameToBeGenerated')

template_path = os.environ['TEMPLATE_PATH_']
destination_path = os.environ['DOWNLOAD_PATH_']


class WordFileGen(Resource):
    def get(self):
        return {'status': 'SUCCESS'}, 200
    
    def post(self):        
        args = parser.parse_args()
        # format today date with format yyyy-mm-dd
        today_date = str(date.today().strftime("%d-%m-%Y"))+"/"

        # folder creation if not exist
        dirname = destination_path + today_date
        if not os.path.exists(dirname):
            os.mkdir(dirname)

        cmplt_template_path = template_path + args["template"]
        cmplt_destination_path = destination_path + today_date + args["fileNameToBeGenerated"] + '.docx'
        path_without_root_destination = today_date + args["fileNameToBeGenerated"] + '.docx'
        data = args["data"]

        print('********************************')
        print('cmplt_template_path = ', cmplt_template_path)
        print('cmplt_destination_path = ', cmplt_destination_path)
        print('path_without_root_destination = ', path_without_root_destination)
        print('data = ', data)
        print('********************************')
        try:
            # documents generation
            doc = DocxTemplate(cmplt_template_path)
            _dict_object = eval(data)
            if type(_dict_object) is dict:
                doc.render(_dict_object)
                doc.save(cmplt_destination_path)
                return {'status': 'SUCCESS', 'reference': path_without_root_destination, 'input': data}, 201
            else:
                raise Exception('invalid json input')
        except Exception as e:
            return {'status': 'ERROR', 'message': 'check server log', 'debug': "{0}".format(e)}, 500


api.add_resource(WordFileGen, '/python/api/process/docxgen')

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5000, debug=False)
