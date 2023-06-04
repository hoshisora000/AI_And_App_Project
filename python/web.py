# -*- coding: utf-8 -*-
"""
Created on Wed May 31 10:51:11 2023

@author: GF75
"""
import os
from flask import Flask, request, redirect, url_for
from werkzeug.utils import secure_filename
import vision_ocr

UPLOAD_FOLDER = ''
ALLOWED_EXTENSIONS = set(['png', 'jpg', 'jpeg']) # 可接受的檔案格式

app = Flask(__name__)
# 設定圖片暫存路徑
app.config['UPLOAD_FOLDER'] =  './upload'
# 設定圖片大小限制
app.config['MAX_CONTENT_LENGTH'] = 25 * 1024 * 1024  # 25MB

# 檢查檔案格式是否正確
def allowed_file(filename):
    return '.' in filename and \
           filename.rsplit('.', 1)[1] in ALLOWED_EXTENSIONS

# 伺服器設定 允許兩種方法POST跟GET
@app.route('/', methods=['GET', 'POST'])
def upload_file():
    if request.method == 'POST': # POST為上傳檔案
        file = request.files['file'] 
        if file and allowed_file(file.filename):
            filename = secure_filename(file.filename)
            # 將接受到的檔案存到設定的路徑
            file.save(os.path.join(app.config['UPLOAD_FOLDER']+"/"+
                                   filename))
            # 呼叫辨識的method
            return vision_ocr.ocr(os.path.join(app.config['UPLOAD_FOLDER']+"/"+
                                   filename))
    # GET提供測試使用       
    return '''
    <!doctype html>
    <title>Upload new File</title>
    <h1>伺服器測試</h1>
    '''

if __name__ == '__main__':
    #使用https連線
    #app.run(ssl_context=('cert.pem', 'key.pem'),debug=True,host="0.0.0.0", port=3000)
    app.run(debug=True,host="0.0.0.0", port=3000)
    #app.run(ssl_context='adhoc',debug=True,host="0.0.0.0", port=3000)