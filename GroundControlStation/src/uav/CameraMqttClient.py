#-*- coding:utf-8 -*-
###########################################
# pip install paho-mqtt
###########################################

#import cv2
import urllib 
import paho.mqtt.client as mqtt
import time
import threading

#예외 발생시 예외 내용 출력을 위해 True로 설정----------------------
debug = True

#MjpegStreamer와 연결하기 위한 정보---------------------------
mjpeg_streamer_url = "http://localhost:50001/?action=stream"
mjpeg_stream = None
capture_thread = None
jpg = None

#MQTT Broker와 연결하기 위한 정보-----------------------------
mqtt_ip = "106.253.56.122"
# mqtt_ip = "192.168.3.16"
mqtt_port = 1883
uav_pub_topic = "/uav2/cameraFront/pub"
uav_sub_topic = "/uav2/cameraFront/sub"
mqtt_client = None

#MQTT Broker와 연결---------------------------------------   
def connect_mqtt():
    global mjpeg_stream
    global mqtt_client
    global mqtt_ip
    global mqtt_port
    try:        
        if mjpeg_stream != None: mjpeg_stream.close()
        if mqtt_client != None: mqtt_client.disconnect()
        
        mjpeg_stream = urllib.urlopen(mjpeg_streamer_url)
        
        mqtt_client = mqtt.Client()   
        mqtt_client.on_connect = on_connect
        mqtt_client.on_disconnect = on_disconnect
        mqtt_client.connect(mqtt_ip, mqtt_port)
        mqtt_client.loop_forever()
    except Exception as e:
        if debug: print(">>>", type(e), "connect_mqtt():", e)
        time.sleep(1)
        fail_safe()
#------------------------------------------------------
def on_connect(client, userdata, flags, rc):
    global capture_thread
    global mqtt_client_connected
    global pub_thread
    try:
        print(">>> UavMqttClient: MQTT Broker Connected")
        
        if capture_thread == None or not capture_thread.is_alive():
            capture_thread = threading.Thread(target=caputer_image)
            capture_thread.setDaemon(True)
            capture_thread.start()
        
        mqtt_client.publish(uav_pub_topic, "", 0 , True) # 버퍼 제거
        mqtt_client.publish(uav_pub_topic, "alive") # GCS가 켜진 상태에서 드론이 접속할 경우, 영상을 보낼 수 있도록 alive 메시지 발송

        mqtt_client.on_message = on_message
        mqtt_client.subscribe(uav_sub_topic)    
    except Exception as e:
        if debug: print(">>>", type(e), "on_connect():", e)
        fail_safe()        
#------------------------------------------------------
def on_disconnect(client, userdata, rc):
    global mqtt_client_connected
    print(">>> UavMqttClient: MQTT Broker Disconnected")
    mjpeg_stream = None
    fail_safe()
#------------------------------------------------------
def fail_safe():
    connect_mqtt() 
#------------------------------------------------------        
def caputer_image(): 
    global mjpeg_stream 
    global jpg      
    while True:
        try:
            bytes=''
            while True:
                temp =mjpeg_stream.read(1024)
                if temp=='':
                    raise Exception('mjpeg streamer stop') 
                bytes+=temp
                a = bytes.find('\xff\xd8')
                b = bytes.find('\xff\xd9')
                if a!=-1 and b!=-1:
                    jpg = bytes[a:b+2]
                    bytes= bytes[b+2:]
        except Exception as e:
            if debug: print(">>>", type(e), "caputer_image():", e)
            time.sleep(1)
            fail_safe()
#------------------------------------------------------  
def on_message(client, userdata, msg):
    global mqtt_client 
    try:
        command = msg.payload
        if command == 'next' and jpg is not None:
            mqtt_client.publish(uav_pub_topic, jpg, 0 , True) # 최신영상을 받도록 retain 조건을 True로 설정
    except Exception as e:
        if debug: print(">>>", type(e), "on_message():", e)
#------------------------------------------------------
if __name__ == "__main__":
    connect_mqtt()
     

