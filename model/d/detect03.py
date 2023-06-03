# detect03.py
import torch
import numpy as np
import cv2
import sys

#model = torch.hub.load('C:/Users/766st/Desktop/Git/d/yolov5', 'custom', path='C:/Users/766st/Desktop/Git/yolov5/runs/train/exp2/weights/best.pt',force_reload=True)


model_path = 'C:/Users/766st/Desktop/Git/d/ultralytics'
sys.path.append(model_path)

from models.experimental import *
from utils.datasets import *
from utils.general import *

model = attempt_load('path/to/weights.pt', map_location=torch.device('cpu'))

cap = cv2.VideoCapture(0)

while cap.isOpened():
    success, frame = cap.read()
    if not success:
      print("Ignoring empty camera frame.")
      continue
    frame = cv2.resize(frame,(800,480))
    results = model(frame)
    # print(np.array(results.render()).shape)

     # 獲取標籤(label)和座標
    labels = results.xyxyn[0][:, -1].cpu().numpy()  # 標籤(label)
    boxes = results.xyxyn[0][:, :-1].cpu().numpy()  # 座標

    # 印出標籤(label)和座標
    for label, box in zip(labels, boxes):
        x, y, w, h ,i = box
        print("Label:", label, "Coordinates:", (x, y, w, h,i))
        
    cv2.imshow('YOLO COCO 03 mask detection', np.squeeze(results.render()))
    if cv2.waitKey(1) & 0xFF == 27:
        break
cap.release()
cv2.destroyAllWindows()
