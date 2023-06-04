# -*- coding: utf-8 -*-
"""
Created on Wed May 31 10:51:11 2023

@author: GF75
"""

import os
from flask import Flask, request, redirect, url_for
from werkzeug.utils import secure_filename
import vision_ocr

#UPLOAD_FOLDER = '/home/ai_app_project_ncue/up_img'
UPLOAD_FOLDER = ''
ALLOWED_EXTENSIONS = set(['png', 'jpg', 'jpeg'])

app = Flask(__name__)
# 設定圖片暫存路徑
app.config['UPLOAD_FOLDER'] =  './upload'
app.config['MAX_CONTENT_LENGTH'] = 25 * 1024 * 1024  # 25MB

def allowed_file(filename):
    return '.' in filename and \
           filename.rsplit('.', 1)[1] in ALLOWED_EXTENSIONS

@app.route('/', methods=['GET', 'POST'])
def upload_file():
    if request.method == 'POST':
        file = request.files['file']
        if file and allowed_file(file.filename):
            filename = secure_filename(file.filename)
            # 將接受到的檔案存到設定的路徑
            file.save(os.path.join(app.config['UPLOAD_FOLDER']+"/"+
                                   filename))
            print(os.path.join(app.config['UPLOAD_FOLDER']+"/"+
                                   filename))
            return vision_ocr.ocr(os.path.join(app.config['UPLOAD_FOLDER']+"/"+
                                   filename))
            
            
    return '''
    <!doctype html>
    <title>Upload new File</title>
    <h1>伺服器測試</h1>
    '''
from flask import send_from_directory

@app.route('/uploads/<filename>')
def uploaded_file(filename):
    return send_from_directory(app.config['UPLOAD_FOLDER'],
                               filename)
if __name__ == '__main__':
    
    app.run(ssl_context=('cert.pem', 'key.pem'),debug=True,host="0.0.0.0", port=3000)
    #app.run(ssl_context='adhoc',debug=True,host="0.0.0.0", port=3000)