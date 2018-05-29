###########################################
# pip install paho-mqtt
# 'import cv2' need when it run in x-window
###########################################

#import cv2
import urllib 
import numpy as np
import paho.mqtt.client as mqtt
import time

mqttClient = mqtt.Client()
streamFront = None
streamBottom = None

while True:
    try:
        streamFront  =urllib.urlopen('http://localhost:50005/?action=stream')
        streamBottom =urllib.urlopen('http://localhost:50001/?action=stream')        
        mqttClient.connect('106.253.56.122', 1883);
        
        # Front
        bytes=''
        # Bottom
        bytes2=''
        while True:
            temp  =streamFront.read(1024)
            temp2 =streamBottom.read(1024)
            
            if temp=='':
                raise Exception('mjpeg streamer stop')
            if temp2=='':
                raise Exception('mjpeg streamer stop') 
            bytes+=temp
            bytes2+=temp2
            # Front
            a = bytes.find('\xff\xd8')
            b = bytes.find('\xff\xd9')
            # Bottom
            c = bytes2.find('\xff\xd8')
            d = bytes2.find('\xff\xd9')
            
            if a!=-1 and b!=-1:
                jpg = bytes[a:b+2]
                bytes= bytes[b+2:]
                
                #MQTT Publishing-------------------------------
                mqttClient.publish('/uav2/cameraFront', jpg)
                
                #X Window--------------------------------------
                #i = cv2.imdecode(np.fromstring(jpg, dtype=np.uint8),cv2.CV_LOAD_IMAGE_COLOR)
                #cv2.imshow('i',i)
                #if cv2.waitKey(1) ==27:
                    #exit(0)
            if c!=-1 and d!=-1:
                jpg2 = bytes2[c:d+2]
                bytes2= bytes2[d+2:]
                
                #MQTT Publishing-------------------------------
                mqttClient.publish('/uav2/cameraBottom', jpg2)
                
    except:
        if streamFront is not None: streamFront.close()
        if streamBottom is not None: streamBottom.close()
        mqttClient.disconnect()
        time.sleep(1)
