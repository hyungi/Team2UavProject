import urllib 
import numpy as np
import paho.mqtt.client as mqtt
import time

mqttClient = mqtt.Client()
stream = None

while True:
    try:
        stream  =urllib.urlopen('http://localhost:50001/?action=stream')    
        mqttClient.connect('106.253.56.122', 1883);
        
        # Front
        bytes=''

        while True:
            temp  =stream.read(1024)
            
            if temp=='':
                raise Exception('mjpeg streamer stop')
            bytes+=temp
            # Front
            a = bytes.find('\xff\xd8')
            b = bytes.find('\xff\xd9')
            
            if a!=-1 and b!=-1:
                jpg = bytes[a:b+2]
                bytes= bytes[b+2:]
                
                #MQTT Publishing-------------------------------
                mqttClient.publish('/uav2/cameraBottom', jpg)
                
    except:
        if stream is not None: stream.close()
        mqttClient.disconnect()
        time.sleep(1)