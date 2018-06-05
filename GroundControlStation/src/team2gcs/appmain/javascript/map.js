var map = {
	googlemap: null,

	gotoMake: false,
	missionMake: false,
	roiMake: false,
	fenceMake: false,
	armMake: false,
	landMake: false,
	
	
	uav: null,
	
	init: function() {
		try {			
			map.googlemap = new google.maps.Map(document.getElementById('map'), {
				zoom: 3,
				center: {lat:37.313778, lng:127.109004},
				//mapTypeId : "roadmap",
				mapTypeId : "satellite",
				//지도&위성 버튼 옵션
//				mapTypeControlOptions: {
//			        style: google.maps.MapTypeControlStyle.HORIZONTAL_BAR,
//			        position: google.maps.ControlPosition.TOP_CENTER,
//			    },
//			    zoomControlOptions: {
//			        position: google.maps.ControlPosition.LEFT_BOTTOM
//			    },
				// night Mode
				styles: [
			            {elementType: 'geometry', stylers: [{color: '#242f3e'}]},
			            {elementType: 'labels.text.stroke', stylers: [{color: '#242f3e'}]},
			            {elementType: 'labels.text.fill', stylers: [{color: '#746855'}]},
			            {
			              featureType: 'administrative.locality',
			              elementType: 'labels.text.fill',
			              stylers: [{color: '#d59563'}]
			            },
			            {
			              featureType: 'poi',
			              elementType: 'labels.text.fill',
			              stylers: [{color: '#d59563'}]
			            },
			            {
			              featureType: 'poi.park',
			              elementType: 'geometry',
			              stylers: [{color: '#263c3f'}]
			            },
			            {
			              featureType: 'poi.park',
			              elementType: 'labels.text.fill',
			              stylers: [{color: '#6b9a76'}]
			            },
			            {
			              featureType: 'road',
			              elementType: 'geometry',
			              stylers: [{color: '#38414e'}]
			            },
			            {
			              featureType: 'road',
			              elementType: 'geometry.stroke',
			              stylers: [{color: '#212a37'}]
			            },
			            {
			              featureType: 'road',
			              elementType: 'labels.text.fill',
			              stylers: [{color: '#9ca5b3'}]
			            },
			            {
			              featureType: 'road.highway',
			              elementType: 'geometry',
			              stylers: [{color: '#746855'}]
			            },
			            {
			              featureType: 'road.highway',
			              elementType: 'geometry.stroke',
			              stylers: [{color: '#1f2835'}]
			            },
			            {
			              featureType: 'road.highway',
			              elementType: 'labels.text.fill',
			              stylers: [{color: '#f3d19c'}]
			            },
			            {
			              featureType: 'transit',
			              elementType: 'geometry',
			              stylers: [{color: '#2f3948'}]
			            },
			            {
			              featureType: 'transit.station',
			              elementType: 'labels.text.fill',
			              stylers: [{color: '#d59563'}]
			            },
			            {
			              featureType: 'water',
			              elementType: 'geometry',
			              stylers: [{color: '#17263c'}]
			            },
			            {
			              featureType: 'water',
			              elementType: 'labels.text.fill',
			              stylers: [{color: '#515c6d'}]
			            },
			            {
			              featureType: 'water',
			              elementType: 'labels.text.stroke',
			              stylers: [{color: '#17263c'}]
			            }
			          ],
			    disableDefaultUI:true,
//				zoomControl: true,
				streetViewControl: false,
				rotateControl: false,
				fullscreenControl: false
			});
			
			//지도 상에서 마우스 휠도 확대/축소할 경우 MapViewController.java의 zoomSlider의 value 변경
			//Slider 제거 -> zoom value 바꾸는 걸로 바꿈
			document.getElementById('map').addEventListener("wheel", function() {
				jsproxy.setZoomSliderValue(map.googlemap.getZoom());
			});				
			
			map.uav = new UAV();
			
			map.googlemap.addListener('click', function(e) {
				try {
					if(map.gotoMake == true) {
						var clickLocation = {lat:e.latLng.lat(), lng:e.latLng.lng()};
						map.uav.gotoStart(clickLocation);
					} else if(map.missionMake == true) {
						map.uav.makeMissionMark("waypoint", e.latLng.lat(), e.latLng.lng());
					} else if(map.roiMake == true) {
						map.uav.makeMissionMark("roi", e.latLng.lat(), e.latLng.lng(), map.uav.roiIndex);
						jsproxy.addROI({lat:e.latLng.lat(), lng:e.latLng.lng()});
						map.roiMake = false;
					} else if(map.fenceMake == true) {
						map.uav.makeFenceMark(e.latLng.lat(), e.latLng.lng());
					} else if(map.armMake == true){
						map.uav.makeMissionMark("arm", e.latLng.lat(), e.latLng.lng());
						map.armMake = false;
					} else if(map.landMake == true){
						map.uav.makeMissionMark("land", e.latLng.lat(), e.latLng.lng());
						map.landMake = false;
					}
				} catch(err) {
					console.log(">> [map.googlemap.click_function()] " + err);
				}
			});	
			
			map.googlemap.addListener('maptypeid_changed', function() {
				if(map.googlemap.getMapTypeId() == "roadmap") {
					map.uav.setUavColor("#00a421", "#f15f5f");
				} else if(map.googlemap.getMapTypeId() == "satellite") {
					map.uav.setUavColor("#ffff00", "#f15f5f");
				}
			});
			
			map.animation.start();
		} catch(err) {
			console.log(">> [map.init()] " + err);
		}
	},

	animation: {
		count: 1,
		start: function() {
			setInterval(function() {
				try {
					if(map.uav.currLocation != null) {
						map.uav.drawUav();	
						map.uav.drawHeadingLine();
						map.uav.drawDestLine();
						
						if(map.animation.count >= 3) { 
							map.googlemap.panTo(map.uav.currLocation);
							map.animation.count = 1;
						} else {
							map.animation.count += 1;
						}
					}
					//window.requestAnimationFrame(map.uav.animation.start);
				} catch(err) {
					console.log(">> [map.animation.start()] " + err);
				}
			}, 1000);
		}
	},
};
