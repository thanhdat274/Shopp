$(document).ready(function() {
	$('.menu-item-wrap').click(function(e) {
		var element = $(this);
		element.siblings().children().removeClass('active');

		element.children(":first").toggleClass("active");
		element.children('.menu-sub').toggle("slow", function() {
		});
		element.siblings().children('.menu-sub').hide("slow", function() {
		});
	});


	$('.cinema-item').click(function(e) {
		var element = $(this).children('.inner').children(':first').children('.cinema-checkbox');
		if (element.is(":checked")) {
			element.prop("checked", false);
			$(this).children('.inner').removeClass('activeborder');
		}
		else {
			element.prop("checked", true);
			$(this).siblings().children('.inner').removeClass('activeborder');
			$(this).children('.inner').addClass('activeborder');
		}
	});




	function doFilter(input) {
		var filter, table, tr, td, i, txtValue;
		filter = input.toUpperCase();
		table = document.getElementById("list-all");
		tr = table.getElementsByTagName("tr");
		for (i = 0; i < tr.length; i++) {
			td = tr[i].getElementsByTagName("td")[5];
			if (td) {
				txtValue = td.textContent || td.innerText;
				if (txtValue.toUpperCase().indexOf(filter) > -1) {
					tr[i].style.display = "";
				} else {
					tr[i].style.display = "none";
				}
			}
		}
	}

	$('#statistic-back').click(function(e) {
		document.getElementById("statistic-back").style.visibility="hidden";
		//$("#filter-form").css("visibility", "visible");
		document.getElementById("myChart").style.display = "block";
		var chartDetail = Chart.getChart('chartDetail')
		chartDetail.destroy();
		$('#page-title').text('Thống kê số lượng đặt phòng');
	});
	
	$('#btn-toggle-form').click(function(e) {
		$('#hotel-form-wraper').toggle("slow", function() {
		});
		$('#btn-toggle-form').hide();
	});	
	
	$('#btn-cancel-form').click(function(e) {
		$('#btn-toggle-form').toggle("slow", function() {});
		$('#hotel-form-wraper').toggle("slow", function() {
			
		});
		
	});	
	
	//$('select').selectpicker();
		
});


Date.prototype.addDays = function(days) {
    var date = new Date(this.valueOf());
    date.setDate(date.getDate() + days);
    return date;
}



function checkDate(){
	var date_in =$('#date_in').val();
	var date_out = $('#date_out').val(); console.log(date_out);
	if(date_out < date_in && date_in != '' && date_out != '') {
		console.log(true);
	$('#date_in').val(date_out);
	$('#date_out').val(date_in);
	}else if(date_out == date_in && date_in != '' && date_out != ''){
		var date = new Date(date_in);
		date = date.addDays(1)
		$('#date_out').val(dateToYMD(date));
		console.log(date);
	}
	
}

function onTimeChange(id) {
	var inputEle = document.getElementById(id);
	var timeSplit = inputEle.value.split(':'),
		hours,
		minutes,
		meridian;
	hours = timeSplit[0];
	minutes = timeSplit[1];
	return hours + ":" + minutes;
	/*if (hours > 12) {
	  meridian = 'PM';
	  hours -= 12;
	} else if (hours < 12) {
	  meridian = 'AM';
	  if (hours == 0) {
		hours = 12;
	  }
	} else {
	  meridian = 'PM';
	}
	alert(hours + ':' + minutes + ' ' + meridian);*/
}



function numberWithCommas(x) {
	var parts = x.toString().split(",");
	parts[0] = parts[0].replace(/\B(?=(\d{3})+(?!\d))/g, ",");
	return parts.join(",");
}

function currency_format() {
	var x = $('#showtime-curency').val();

	var temp2 = x.replace(/[^a-z0-9\s]/gi, '').replace(/[_\s]/g, '-').replace(/\D/g, '');
	var temp = temp2.replace(/\./g, '');
	$('#showtime-curency').val(numberWithCommas(temp));
}

function bookingDetail(data, labels) {
	const data1 = {
		labels: labels,
		datasets: [{
			label: 'Số phòng được đặt theo ngày',
			data: data,
			fill: false,
			borderColor: 'rgb(75, 192, 192)',
			tension: 0.1
		}]
	};
	const config1 = {
		type: 'line',
		data: data1,
		options: {
			scales: {
				y: {
					beginAtZero: true
				}
			}
		},
	};
	document.getElementById("statistic-back").style.visibility="visible" ;
	document.getElementById("myChart").style.display = "none";
	const chartDetail = new Chart(document.getElementById('chartDetail'), config1);
	var hotel_name=localStorage.getItem("hotel_name");
	$('#page-title').text('Thống kê số lượng đặt phòng của '+hotel_name);
}



function dynamicColors() {
	var r = Math.floor(Math.random() * 255);
	var g = Math.floor(Math.random() * 255);
	var b = Math.floor(Math.random() * 255);
	return "rgb(" + r + "," + g + "," + b + ")";
}


function initBookingModal(hotel_id, hotel_name, date) {
	console.log(hotel_id);
	$('#bookingModal').modal('show');
	document.getElementById("ModalLongTitle").innerHTML = hotel_name;
	document.getElementById("modal-sub-title").innerHTML = "Thông tin đặt phòng ngày " + date;
	$.ajax({
		url: "http://localhost:8080/admin/booking/detail",
		type: 'GET',
		data: {
			hotel_id: hotel_id,
			date: date
		},
		success: function(res) {
			console.log("detail-success");
			/*for (var i= 0; i<res.length; i++) {
                console.log(res[i]["checkIn"]);
            }*/
			initBookingDetailTable(res);
		},
		error: function(xhr, status, errorThrown) {
			console.log("failed");
			console.log(status);
			console.log(errorThrown);
		}
	});
}

function initBookingDetailTable(data) {
	var tb = document.getElementById('booking-detail-tbl');
	while (tb.rows.length > 1) {
		tb.deleteRow(1);
	}
	var tbodyRef = document.getElementById('booking-detail-tbl').getElementsByTagName('tbody')[0];
	for (var i = 0; i < data.length; i++) {
		var newRow = tbodyRef.insertRow();
		var newCell = newRow.insertCell();

		var stt = document.createTextNode(i + 1);
		var customer = document.createTextNode(data[i]["customer"]["firstName"] + ' ' + data[i]["customer"]["lastName"]);
		var roomType = document.createTextNode(data[i]["type"]['name']);
		var checkIn = document.createTextNode(dateToYMD(data[i]["dateFrom"]) + ' ' + data[i]["checkIn"]);
		var checkOut = document.createTextNode(dateToYMD(data[i]["dateTo"]) + ' ' + data[i]["checkOut"]);
		newCell.appendChild(stt);
		newRow.insertCell().appendChild(customer);
		newRow.insertCell().appendChild(roomType);
		newRow.insertCell().appendChild(checkIn);
		newRow.insertCell().appendChild(checkOut);
	}
}

function dateToYMD(date1) {
	var date = new Date(date1);
	var d = date.getDate();
	var m = date.getMonth() + 1; //Month from 0 to 11
	var y = date.getFullYear();
	return '' + y + '-' + (m <= 9 ? '0' + m : m) + '-' + (d <= 9 ? '0' + d : d);
}

var getUrlParameter = function getUrlParameter(sParam) {
	var sPageURL = window.location.search.substring(1),
		sURLVariables = sPageURL.split('&'),
		sParameterName,
		i;

	for (i = 0; i < sURLVariables.length; i++) {
		sParameterName = sURLVariables[i].split('=');

		if (sParameterName[0] === sParam) {
			return sParameterName[1] === undefined ? true : decodeURIComponent(sParameterName[1]);
		}
	}
	return null;
};

function initDistrictSelector(district){
	$('#quan').find('option').remove().end();
	cityId = document.getElementById('thanhPho').value;
	select1 = document.getElementById('quan');
	//$('#quan').find('option').remove().end();
	$.ajax({
				        url: "https://api.mysupership.vn/v1/partner/areas/district",
				        type: 'GET',
				        data :{
							province: cityId
						},
				        success: function(res) {
				           var data = res["results"];
				           for(var i=0; i< data.length;i++){
					           	var opt = document.createElement('option');
							    opt.value = data[i]["name"];
							    opt.innerHTML = data[i]["name"];
							    select1.appendChild(opt);
							    if(data[i]["name"] == district){
									select1[i].selected= true;
							}
				           } 
				        },
				    	error: function(xhr,status, errorThrown) {
				    		console.log("failed");
				    		console.log(status);
				            console.log(errorThrown);
				      }
				    });
}

function submitStatusForm(){
	$('#statusFilter').submit();
}


