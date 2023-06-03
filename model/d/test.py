from flask import Flask, request

app = Flask(__name__)

@app.route('/upload', methods=['POST'])
def upload_file():
    # 检查请求中是否包含文件
    if 'file' not in request.files:
        return 'No file found', 400

    file = request.files['file']

    # 检查文件是否存在
    if file.filename == '':
        return 'No file selected', 400

    # 可选：指定保存文件的目录和文件名
    # 根据您的需求进行相应更改
    # file.save('/path/to/save/directory/' + file.filename)

    # 处理文件，例如保存到磁盘或进行其他操作
    # 这里只是简单地打印文件名
    print('Received file:', file.filename)

    return 'File uploaded successfully'

if __name__ == '__main__':
    app.run(port=8000)