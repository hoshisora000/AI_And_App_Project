import os
import torch
import cv2
from google.cloud import vision

#---------------模型路徑----------需修改
model = torch.hub.load('ultralytics/yolov5', 'custom', path='best.pt')
#model = torch.hub.load('ultralytics/yolov5', 'custom', path='D:/AI_And_App_Project/yoloV5/exp2/weights/best.pt')

img_width=800
img_height=480

frame = cv2.imread('image02.jpg')


frame = cv2.resize(frame,(800,480))
results = model(frame)
# print(np.array(results.render()).shape)

 # 獲取標籤(label)和座標
labels = results.xyxyn[0][:, -1].cpu().numpy()  # 標籤(label)
boxes = results.xyxyn[0][:, :-1].cpu().numpy()  # 座標

# 印出標籤(label)和座標
for label, box in zip(labels, boxes):
    x, y, w, h ,i = box
    if(i>=0.70):
        print("Label:", label, "Coordinates:", (x, y, w, h,i))

        left = x * img_width
        top = y * img_height
        right = w*img_width+5
        bottom = h*img_height
        cropped_image = frame[int(top):int(bottom), int(left):int(right)]
        cv2.imwrite('cropped_image.jpg', cropped_image)
    else:
        break
#---------------金鑰路徑----------需修改
"""
-------本機測試------
key_path="D:\Python\key.json"
os.environ['GOOGLE_APPLICATION_CREDENTIALS'] = key_path
"""
#-------------調整-----------------
key_path="key.json"
os.environ['GOOGLE_APPLICATION_CREDENTIALS'] = key_path
#detect_text(sys.argv[1])cropped_image
img_path="cropped_image.jpg"


client = vision.ImageAnnotatorClient()

with open(img_path, 'rb') as image_file:
    content = image_file.read()

image = vision.Image(content=content)

response = client.text_detection(image=image)

show=response.full_text_annotation.text.replace('\n', '').replace('\r', '').replace(' ', '')

print(show)

if response.error.message:
    raise Exception(
        '{}\nFor more info on error messages, check: '
        'https://cloud.google.com/apis/design/errors'.format(
            response.error.message))