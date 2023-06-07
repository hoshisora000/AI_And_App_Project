import torch
import numpy as np
import cv2
from ultralytics import YOLO

model = YOLO('D:/yolo_v8/ultralytics/best_cpu500.pt')  # load a custom trained

cap = cv2.VideoCapture(0)
img_width=1000
img_height=1000
while cap.isOpened():
    success, frame = cap.read()
    if success:
        frame = cv2.resize(frame,(800,480))
        # Run YOLOv8 inference on the frame
        results = model(frame)

        # Visualize the results on the frame
        annotated_frame = results[0].plot()

        # Display the annotated frame
        cv2.imshow("YOLOv8 Inference", annotated_frame)

        # Break the loop if 'q' is pressed
        if cv2.waitKey(1) & 0xFF == ord("q"):
            break
    else:
        # Break the loop if the end of the video is reached
        break

        
cap.release()
cv2.destroyAllWindows()
