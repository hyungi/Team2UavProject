var map = {
	googlemap: null,
	
	gotoMake:false,
	missionMake:false,
	
	uav: null,
	init: function() {
		try {
			map.googlemap = new google.maps.Map(
				document.getElementById('map'), 
				{
					center: {lat: 37.495064, lng: 127.122280},
					zoom: 17,
					mapTypeId : "roadmap",
					//mapTypeId: "satellite", //위성사진
					zoomControl: false, //줌
					fullscreenControl: false, //풀스크린버튼
					streetViewControl: false // 스트리뷰
				}
			);


			//맵에서 마우스휠로 확대/축소 했을 경우
			document.getElementById('map').addEventListener("wheel", function() {  //(이벤트 이름, 함수)
				jsproxy.setZoomSliderValue(map.googlemap.getZoom());
			});
			
			//맵에 보여줄 UAV 생성
			map.uav = new UAV();
			
			//맵에서 클릭했을 경우
			map.googlemap.addListener("click", function(event){
				try {
					if(map.gotoMake==true){
						var clickLocation = event.latLng.toJSON();
						map.uav.gotoStart(clickLocation);
					} else if(map.missionMake == true){
						map.uav.makeMissionMarker("waypoint", event.latLng.lat(), event.latLng.lng());
					}
					
				} catch (err) {
					console.log(">>[map.googlemap.clickHandler()] "+err);
				}
			});
			
			//맵 애니메이션 드로잉 시작
			map.animation.start();
			
		} catch(err) {
			console.log(">> [map.init()] " + err);
		}
	},

		
		animation: {
			value: 0,
			start: function() {
				var self = this;
				setInterval(function() { //주기적 호출  cf)setTimeout 예약시간때 실행
					try {
						self.value += 10;
						if(self.value>360) {
							self.value = self.value - 360;
						}
						map.uav.heading = self.value;
						map.uav.drawUav();
						map.uav.drawHeadingLine();
						map.uav.drawDestLine();
					} catch(err) {
						console.log(">> [map.animation.start()] " + err);
					}
				}, 1000);
			}
		}
	};






