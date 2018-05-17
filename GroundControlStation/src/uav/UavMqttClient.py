# -*- coding:utf-8 -*-
from __future__ import print_function

import cmd
import threading
import time

from droneapi.lib import Vehicle
from dronekit import connect, VehicleMode, LocationGlobalRelative, Command
from pip._vendor.html5lib._trie import py
import pymavlink
import simplejson
import paho.mqtt.client as mqtt

# 버츄얼박스 실행 : cd ardupilot/ArduCopter  -  . ~/.bashrc   -  sh start.sh
# 콜백함수 - 어떠한 사건이 발생했을때 자동으로 실행되는 것
# 예외 발생시 예외 내용 출력을 위해 True로 설정
debug = True

#Autopilot(FC-펌웨어)과 연결----------------------------------------------------------------
vehicle = connect('udp:192.168.3.217:14560', wait_ready=True) #컴퓨터에서 테스트 실행시
# vehicle = connect('/dev/ttyS0',wait_ready = True,baud57600) #라즈베리파이에서 실행시 

#MQTT_Broker와 연결하기 위한 정보------------------------------------------------------------
mqtt_ip = "localhost"
mqtt_port = 1883
uav_pub_topic = "/uav2/pub"
uav_sub_topic = "/uav2/sub"

#MQTT Broker와 연결 ------------------------------------------------------------------------
mqtt_client = None


def connect_mqtt():
    try:
        global mqtt_client
        global mqtt_ip
        global mqtt_port
        
        # 기존의 mqtt_client가 생성되었을 경우 정상적으로 접속 종료
        if mqtt_client != None:
            mqtt_client.disconnect()
        
        # mqtt_client 객체 생성
        mqtt_client = mqtt.Client()
        # 접속이 성공 되었을 경우 실행할 콜백 함수 등록
        mqtt_client.on_connect = on_connect  # on = 이벤트 처리
        # 접속이 끊어 졌을대 실행할 콜백 함수 등록
        mqtt_client.on_disconnect = on_disconnect
        # MQTT Broker 연결
        mqtt_client.connect(mqtt_ip, mqtt_port)  # localhost, 1883
        # 접속 유지
        mqtt_client.loop_forever()  # 무한반복 = 연결을 계속 유지
        
    except Exception as e:
        if debug: print(">>>", type(e), "connect_mqtt():", e)
        fail_safe("connect_mqtt_fail")


#연결 성공 되었을대 실행하는 콜백 함수 --------------------------------------------------------     
mqtt_client_connected = False
pub_thread = None

   
def on_connect(client, userdata, flags, rc):
    
    try:
        global mqtt_client_connected
        global pub_thread
        if debug : print(">>> UavMqttClient : MQTT Broker connected")
        mqtt_client_connected = True
        
        # pub_thread가 없거나 죽었을 경우 다시 pub_thread 생성 후 시작
        if pub_thread == None or not pub_thread.is_alive():
            pub_thread = threading.Thread(target=send_data)
            pub_thread.setDaemon(True)
            pub_thread.start()
        
        # MQTT 메세지를 수신했을 대 실행할 콜백 함수 등록
        mqtt_client.on_message = on_message
        # MQTT 메세지 수신
        mqtt_client.subscribe(uav_sub_topic)
            
    except Exception as e :
        if debug : print(">>>", type(e), "connect_mqtt():", e)
        fail_safe("connect_mqtt_fail")


#연결이 끊어 졌을때 실행하는 콜백 함수-----------------------------------------------------------
def on_disconnect(client, userdata, rc):
    global mqtt_client_connected
    if debug : print(">>> UavMqttClient : MQTT Broker Disconnected")
    mqtt_client_connected = False
    fail_safe("connect_mqtt_fail")


#예외 발생시 안전장치---------------------------------------------------------------------------    
def fail_safe(message):
    if message == "connect_mqtt_fail" : 
        if debug: print(">>> fail_safe: connect_mqtt()")
        time.sleep(1)
        connect_mqtt()


#백그라운드로 MQTT Broker에게 드론의 정보를 보내주는 함수-------------------------------------------
def send_data():
    # 딕셔너리 생성
    data = {}
    while True:
        try:
            if mqtt_client_connected == True :
                
                # 딕셔너리 초기화
                data.clear()
                
                # Autopilot의 종류 및 버전 정보 보내기
                send_autopilot_version_info(data)
                # 시스템(FC)의 상태 정보 보내기
                send_system_status_info(data)
                # 베터리 정보 보내기
                send_battery_info(data)
                # GPS가 잡혔는지 여부, 몇개의 위성을 사용하는지 정보 보내기
                send_gps_info(data)
                # 비행모드 정보 보내기 (rtl,land,auto 등등)
                send_mode_info(data)
                # 시동 여부 보내기
                send_armed_info(data)
                # 위치정보 보내기 (위도, 경도, 고도)
                send_global_position_int_info(data)  # global_position_int : 절대 위치라는 뜻 (cf relative : 상대 위치) , 위도경도는 같으나 고도가 다름( 절대:해수면 기준, 상대: 현위치 지면기준)
                # 드론 머리 방향(heading), 지상속도(ground speed), 비행속도(air speed)
                send_vfr_hud_info(data)
                # Roll, Pitch, Yaw
                send_attitude_info(data)
                # 홈 위치 보내기
                send_home_position_info(data)
                # 상태 문자열 보내기(웨이포인트 도착 등등)
                send_statustext_info(data)
                # 드론과 지면간의 거리정보 보내기(착륙할때, loiter mode(현위치고정) 등 에서 쓰임
                send_rangfinder_info(data)
                # 미션 정보 보내기
                send_mission_info(data)
                # 펜스 정보 보내기
                send_fence_info(data)
                                          
                # 파이썬의 딕셔너리를 문자열 JSON으로 변환
                json = simplejson.JSONEncoder().encode(data)
                # print(json)
                
                # MQTT Broker로 메세지 전송
                mqtt_client.publish(uav_pub_topic, json) 
                
            time.sleep(0.1)
        except Exception as e : 
            if debug : print(">>>", type(e), "connect_mqtt():", e)

#AUtoPilot의 종류 및 버전 정보 보내기----------------------------------------------------------     


'''
@vehicle.on_message('HEARTBEAT')
def listener(self, name, m):
    print(m)

@vehicle.on_message('AUTOPILOT_VERSION')
def listener(vehicle, name, m):
    print(m)
'''


def send_autopilot_version_info(data):
    # HEARTBEAT, AUTOPILOT_VERSION
    data["autopilot_version"] = str(vehicle.version)

#시스템(FC) 상태 정보 보내기-------------------------------------------------------------------


'''
@vehicle.on_message('HEARTBEAT')
def listener(self, name, m):
    print(m)
'''


def send_system_status_info(data):
    data["system_status"] = vehicle.system_status.state

#베터리 상태 정보 보내기------------------------------------------------------------------------


'''
@vehicle.on_message('SYS_STATUS')
def listener(self, name, m):
    print(m)
'''

    
def send_battery_info(data):   
    data["battery_voltage"] = vehicle.battery.voltage
    data["battery_current"] = vehicle.battery.current
    data["battery_level"] = vehicle.battery.level
    
#GPS사용 여부와 위성 수 보내기 --------------------------------------------------------------------


'''
@vehicle.on_message('GPS_RAW_INT')
def listener(self, name, m):
    print(m)
'''

    
def send_gps_info(data):
    data["gps_fix_sype"] = vehicle.gps_0.fix_type
    data["gps_satellites_visible"] = vehicle.gps_0.satellites_visible

#비행 모드 정보 보내기 --------------------------------------------------------------------------


'''
@vehicle.on_message('HEARTBEAT')  #HEARTBEAT의 메세지 안의 모드 정보 보고싶음
def listener(self, name, m):
    #LOITER , custom모드는 ardupilot만 존재 나머진 base모드(mavlink에서 제공)
    print(m.custom_mode) #코드정보는 ardupilotmega.xml 안의 COPTET_MODE에 존재
    print(m)
'''


def send_mode_info(data):
    data["mode"] = vehicle.mode.name
    
#시동 여부 정보 보내기 --------------------------------------------------------------------------


'''
@vehicle.on_message('HEARTBEAT') 
def listener(self, name, m):
    print(m)
    if m.base_mode&pymavlink.mavutil.mavlink.MAV_MODE_FLAG_SAFETY_ARMED != 0 :
        print("시동 됨")
    else :
        print("시동 꺼짐")
'''


def send_armed_info(data):
    data["armed"] = vehicle.armed
    
#위치 정보 보내기(상대좌표)----------------------------------------------------------------------


'''
@vehicle.on_message('GLOBAL_POSITION_INT')
def listener(vehicle, name, m):
    print(m)
'''

    
def send_global_position_int_info(data):
    data["latitude"] = vehicle.location.global_relative_frame.lat
    data["longitude"] = vehicle.location.global_relative_frame.lon
    data["altitude"] = vehicle.location.global_relative_frame.alt

#드론의 머리방향, 지상속도, 비행속도 -------------------------------------------------------------


'''
@vehicle.on_message('VFR_HUD')
def listener(self, name, m):
    print(m)
'''

    
def send_vfr_hud_info(data):
    data["heading"] = vehicle.heading
    data["groundspeed"] = vehicle.groundspeed
    data["airspeed"] = vehicle.airspeed
    
#Roll, Pitch, Yaw 정보 보내기 ------------------------------------------------------------------


'''
@vehicle.on_message('ATTITUDE')
def listener(self, name, m):
    print(m)
'''

    
def send_attitude_info(data):
    data["roll"] = vehicle.attitude.roll * 57.2958
    data["pitch"] = vehicle.attitude.pitch * 57.2958
    data["yaw"] = vehicle.attitude.yaw * 57.2958
    
#홈 위치 보내기(절대)--------------------------------------------------------------------------


'''
@vehicle.on_message(['HOME_POSITION']) # 암드되었을때 딱 그위치 보냄
def listener(vehicle, name, msg):
    print(msg)
'''


def send_home_position_info(data):
    try:
        data["home_lat"] = vehicle.home_location.lat
        data["home_lng"] = vehicle.home_location.lon
        data["home_alt"] = vehicle.home_location.alt
    except Exception as e:
        data["homelat"] = 0
        data["home_lng"] = 0
        data["home_alt"] = 0

        
#Autopilot에 상태 문자열 보내기-----------------------------------------------------------------
statustext = ""


@vehicle.on_message("STATUSTEXT")  # 웨이포인트 위치에 도착하면 메세지가 날라옴
def listener(vehicle, name, msg):
    global statustext
    statustext = msg.text

    
def send_statustext_info(data):
    global statustext
    data["statustext"] = statustext
    statustext = ""


#드론과 지면간의 거리 및 Optical Flow(해상도 하 0~255 상) 보내기 -----------------------------------------
optical_flow_quality = 0


@vehicle.on_message("OPTICAL_FLOW")  # 화질정보(0~255)
def listener(vehicle, name, msg):
    global optical_flow_quality
    optical_flow_quality = msg.quality


def send_rangfinder_info(data):
    data["rangfinder_distance"] = vehicle.rangefinder.distance
    data["rangfinder_voltage"] = vehicle.rangefinder.voltage
    data["optical_flow_quality"] = optical_flow_quality
    
#미션 정보 보내기-------------------------------------------------------------------------------


'''
#미션 경유점들의 정보 얻기
[Request]
vehicle.commands.download();
common.xml -> <message id="43" name="MISSION_REQUEST_LIST">

[Response]
<message id="39" name="MISSION_ITEM">

#다음 경유점들의 정보 얻기
@vehicle.on_message(["MISSION_CURRENT"])
def listener(vehicle, name, m):
    print(m)
    
'''

    
mission_download_request = False


def send_mission_info(data):
    global mission_download_request
    # 다음 경유점에 대한 정보
    data["next_waypoint_no"] = vehicle.commands.next
    # 모든 경유점에 대한 정보 얻기
    if mission_download_request == True:
        vehicle.commands.download()
        vehicle.commands.wait_ready()  # 대기
        waypoints = []
        for cmd in vehicle.commands:
            waypoint = {}
            if cmd.command == 22:  # takeoff
                waypoint["kind"] = "takeoff"
                waypoint["alt"] = cmd.z
            elif cmd.command == 16:  # waypoint
                waypoint["kind"] = "waypoint"
                waypoint["lat"] = cmd.x
                waypoint["lng"] = cmd.y
                waypoint["alt"] = cmd.z
            elif cmd.command == 20:  # rtl
                waypoint["kind"] = "rtl"
            elif cmd.command == 177:  # jump
                waypoint["kind"] = "jump"
                waypoint["to"] = cmd.param1
                waypoint["repeat"] = cmd.param2
            elif cmd.command == 201:  # roi(카메라 고정지역)
                waypoint["kind"] = "roi"
                waypoint["lat"] = cmd.x
                waypoint["lng"] = cmd.y
                waypoint["alt"] = cmd.z
            waypoints.append(waypoint)  # 리스트 추가
        data["waypoints"] = waypoints
        mission_download_request = False
    else:
        data["waypoints"] = []

        
#펜스 정보 보내기-------------------------------------------------------------------------------
fence_points = []
fence_download_request = False


@vehicle.on_message("FENCE_POINT")
def listener(vehicle, name, m):
    global fence_points
    try:
        fence_point = {}
        fence_point["idx"] = m.idx
        fence_point["count"] = m.count  # 펜스 총 갯수
        fence_point["lat"] = m.lat
        fence_point["lng"] = m.lng
        fence_points.append(fence_point)
    except Exception as e:
        if debug: print(">>>", type(e), "fence_points_listener():", e)


def send_fence_info(data):
    global fence_points
    global fence_download_request
    try:
        if fence_download_request == True :
            # 전체 Fence Point의 갯수
            fence_total = int(vehicle.parameters["FENCE_TOTAL"])  # parameters = ardupilot의 상태정보, "FENCE_TOTAL" : 펜스 총 갯수
            # print(fence_total)
            # 전체 Fence Point 정보 요청
            for idx in range(fence_total) :
                msg = vehicle.message_factory.fence_fetch_point_encode(0, 0, idx)  # 1번째 펜스 정보를 메세지에 저장
                vehicle.send_mavlink(msg)  # 메세지 보내기
                # vehicle.send_mavlink = vehicle.message_factory.fence_fetch_point_send(0,0,idx) 위의 두줄이랑 같음
                
            # 대기효과 (모든 Fence Point가 다운로드 될 때 까지 기다림)
            while len(fence_points) != fence_total : pass
            
            fence_info = {}
            fence_info["fence_enable"] = vehicle.parameters["FENCE_ENABLE"]
            fence_info["fence_type"] = vehicle.parameters["FENCE_TYPE"]
            fence_info["fence_action"] = vehicle.parameters["FENCE_ACTION"]
            fence_info["fence_radius"] = vehicle.parameters["FENCE_RADIUS"]
            fence_info["fence_alt_max"] = vehicle.parameters["FENCE_ALT_MAX"]
            fence_info["fence_margin"] = vehicle.parameters["FENCE_MARGIN"]
            fence_info["fence_total"] = vehicle.parameters["FENCE_TOTAL"]
            fence_info["fence_points"] = fence_points
            
            data["fence_info"] = fence_info
            fence_download_request = False            

        else :
            fence_info = {}
            fence_info["fence_enable"] = vehicle.parameters["FENCE_ENABLE"]
            data["fence_info"] = fence_info
    except Exception as e :
        if debug: print(">>>", type(e), "send_fence_info():", e)
        fence_info = {}
        fence_info["fence_enable"] = vehicle.parameters["FENCE_ENABLE"]
        data["fence_info"] = fence_info
    fence_points = []
    # print(data["fence_info"])

    
#MQTT 메세지가 수신했을 때 콜백되는 함수----------------------------------------------------------
def on_message(client, userdata, msg):
    json = msg.payload
    json_dict = simplejson.loads(json)
    command = json_dict["command"]
    if command == "arm":arm(json_dict)
    elif command == "disarm":disarm(json_dict)
    elif command == "takeoff":takeoff(json_dict)
    elif command == "land":land(json_dict)
    elif command == "rtl":rtl(json_dict)
    elif command == "goto":goto(json_dict)
    elif command == "mission_upload":mission_upload(json_dict)
    elif command == "mission_download":mission_download(json_dict)
    elif command == "mission_start":mission_start(json_dict)
    elif command == "mission_stop":mission_stop(json_dict)
    elif command == "mission_clear":mission_clear(json_dict)
    elif command == "fence_enable":fence_enable(json_dict)
    elif command == "fence_disable":fence_disable(json_dict)
    elif command == "fence_upload":fence_upload(json_dict)
    elif command == "fence_download":fence_download(json_dict)
    elif command == "fence_clear":fence_clear(json_dict)

#command----------------------------------------------------------------------------------------

        
def arm(json_dict):
    if vehicle.armed: return 
    vehicle.mode = VehicleMode("GUIDED")  # 모드선택
    vehicle.armed = True  # 암드


def disarm(json_dict):
    if not vehicle.armed: return
    vehicle.mode = VehicleMode("GUIDED")
    vehicle.armed = False  # 디스암드

    
def takeoff(json_dict):
    if not vehicle.armed: return
    vehicle.mode = VehicleMode("GUIDED")
    alt = json_dict["alt"]
    vehicle.simple_takeoff(alt)
    # vehicle.message_factory.command_long_send(0, 0, pymavlink.mavutil.mavlink.MAV_CMD_NAV_TAKEOFF, 0, 0, 0, 0, 0, 0, 0, 30)

    
def land(json_dict):
    if not vehicle.armed: return
    vehicle.mode = VehicleMode("LAND")


def rtl(json_dict):
    if not vehicle.armed: return
    rtl_alt = json_dict["rtl_alt"]
    vehicle.parameters["RTL_ALT"] = rtl_alt
    vehicle.mode = VehicleMode("RTL")

    
def goto(json_dict):
    if not vehicle.armed: return
    if vehicle.location.global_relative_frame.alt < 3 :return  # 고도 최저 높이 지정 
    vehicle.mode = VehicleMode("GUIDED")
    lat = json_dict["lat"]
    lng = json_dict["lng"]
    alt = json_dict["alt"]
    targetLocation = LocationGlobalRelative(lat, lng, alt)  # Relative 상대좌표
    vehicle.simple_goto(targetLocation)
    # vehicle.message_factory.mission_item_send(0,0,0,pymavlink.mavutil.mavlink.MAV_FRAME_GLOBAL_RELATIVE_ALT,pymavlink.mavutil.mavlink.MAV_CMD_NAV_WAYPOINT,2,0,0,0,0,0,lat,lng,alt)


def mission_upload(json_dict):
    waypoints = json_dict["waypoints"]
    
    while True:
        vehicle.commands.clear()  # 기존 미션정보 지우기
        for waypoint in waypoints:
            kind = waypoint["kind"]
            if kind == "takeoff":
                alt = waypoint["alt"]
                cmd = Command(0, 0, 0, pymavlink.mavutil.mavlink.MAV_FRAME_GLOBAL_RELATIVE_ALT, pymavlink.mavutil.mavlink.MAV_CMD_NAV_TAKEOFF, 0, 0, 0, 0, 0, 0, 0, 0, alt)
                vehicle.commands.add(cmd)
            elif kind == "waypoint":
                lat = waypoint["lat"]
                lng = waypoint["lng"]
                alt = waypoint["alt"]
                cmd = Command(0, 0, 0, pymavlink.mavutil.mavlink.MAV_FRAME_GLOBAL_RELATIVE_ALT, pymavlink.mavutil.mavlink.MAV_CMD_NAV_WAYPOINT, 0, 0, 0, 0, 0, 0, lat, lng, alt)
                vehicle.commands.add(cmd)
            elif kind == "rtl":
                cmd = Command(0, 0, 0, pymavlink.mavutil.mavlink.MAV_FRAME_GLOBAL_RELATIVE_ALT, pymavlink.mavutil.mavlink.MAV_CMD_NAV_RETURN_TO_LAUNCH, 0, 0, 0, 0, 0, 0, 0, 0, 0)
                vehicle.commands.add(cmd)
            elif kind == "jump":
                next = waypoint["next"]
                repeat = waypoint["repeat"]
                cmd = Command(0, 0, 0, pymavlink.mavutil.mavlink.MAV_FRAME_GLOBAL_RELATIVE_ALT, pymavlink.mavutil.mavlink.MAV_CMD_DO_JUMP, 0, 0, next, repeat, 0, 0, 0, 0, 0)
                vehicle.commands.add(cmd)
            elif kind == "roi":
                pass
        # 가끔 for문이 waypoints 길이만큼 Command가 가끔 추가되지 않음, 이경우 while문 실행(버그)
        # 전부 추가 되었는지 확인후 while문 빠져나감
        if len(waypoints) == vehicle.commands.count: 
            break
    # FC(APM)로 미션정보 업로드
    vehicle.commands.upload()

    
#미션 다운로드 ---------------------------------------------------------------------------------------------
def mission_download(json_dict):
    global mission_download_request
    mission_download_request = True

    
#미션 다운로드 ---------------------------------------------------------------------------------------------
def mission_start(json_dict):
    if not vehicle.armed: return 
    if vehicle.location.global_relative_frame.alt < 3 : return  # 사람이 다칠수 있어서 3미터 아래 미션 금지
    vehicle.mode = VehicleMode("AUTO")  # 미션시작


#미션 다운로드 ---------------------------------------------------------------------------------------------
def mission_stop(json_dict):
    if not vehicle.armed: return 
    vehicle.mode = VehicleMode("GUIDED")  # 미션중지:제자리 정지


#미션 클리어 ----------------------------------------------------------------------------------------------
def mission_clear(json_dict):
    vehicle.commands.clear()
    vehicle.commands.upload()


#펜스 활성화----------------------------------------------------------------------------------------------
def fence_enable(json_dict):
    vehicle.parameters["FENCE_ENABLE"] = 1


#펜스 비활성화---------------------------------------------------------------------------------------------
def fence_disable(json_dict):
    vehicle.parameters["FENCE_ENABLE"] = 0


#펜스 업로드 -----------------------------------------------------------------------------------------------
def fence_upload(json_dict):
    vehicle.parameters["FENCE_TYPE"] = json_dict["fence_type"]
    vehicle.parameters["FENCE_ACTION"] = json_dict["fence_action"]
    vehicle.parameters["FENCE_RADIUS"] = json_dict["fence_radius"]
    vehicle.parameters["FENCE_ALT_MAX"] = json_dict["fence_alt_max"]
    vehicle.parameters["FENCE_MARGIN"] = json_dict["fence_margin"]
    vehicle.parameters["FENCE_TOTAL"] = json_dict["fence_total"]
    fence_points = json_dict["fence_points"]
    for fence_point in fence_points:  # for문에서 인덱스번호가 필요할때 in enumerate(fence_points)사용, 첫번째 변수에 인덱스번호 0부터 들어감
        idx = fence_point["idx"]
        lat = fence_point["lat"]
        lng = fence_point["lng"]
        vehicle.message_factory.fence_point_send(0, 0, idx, json_dict["fence_total"], lat, lng)  # 메세지 바로 보내기

#펜스 다운로드 -------------------------------------------------------------------------------------------------
def fence_download(json_dict):
    global fence_download_request
    fence_download_request = True

#펜스 클리어 -------------------------------------------------------------------------------------------------
def fence_clear(json_dict):
    vehicle.parameters["FENCE_TOTAL"] = 0 #펜스 갯수 0개
    vehicle.parameters["FENCE_ENABLE"] = 0 #펜스 비활성화
    
if __name__ == '__main__':


    connect_mqtt()
