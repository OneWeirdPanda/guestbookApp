$(document).ready(function() {
	//	alert("333");
	var location_countries_url_docker = "http://192.168.99.100:9000/location/countriesMock";
	var location_countries_url_kube = "http://192.168.99.102:32157/location/countriesMock";
	
	var location_cities_url_docker = "http://192.168.99.100:9000/location/citiesMock?geonameId=";
	var location_cities_url_kube = "http://192.168.99.102:32157/location/citiesMock?geonameId=";
    $.ajax({
	   url: location_countries_url_kube,
	   dataType : 'json'
    }).then(function(data) {
	   console.log(" then called := "+data);
	   //alert(data);
	   var resultStringfyData = JSON.stringify(data);
	   console.log(resultStringfyData);
	   var responseJson = $.parseJSON(resultStringfyData);
	   
	   console.log(responseJson.length);
	   for (var x = 0; x < responseJson.length; x++) {
		   console.log(responseJson[x].geonameId);
		   console.log(responseJson[x].countryName);
		   var code = responseJson[x].geonameId + "-" + responseJson[x].countryCode;
		   //var div_data = "<option value="+responseJson[x].geonameId+">"+responseJson[x].countryName+"</option>";
		   var div_data = "<option value="+code+">"+responseJson[x].countryName+"</option>";
		   $(div_data).appendTo('#country');
	   }
    });
	
	$("#country").change(function(){
	//alert("333");	
	$("#citydiv").show();
	$("#city").empty();
	var code = this.value;
	var idCodeArr = code.split("-");
     console.log("geonameId = "+idCodeArr[0]);
	 console.log("countryCode = "+idCodeArr[1]);

    $.ajax({
	   url: location_cities_url_kube + idCodeArr[0],
	   dataType : 'json'
    }).then(function(data) {
	   console.log(" then called := "+data);
	   //alert(data);
	   var resultStringfyData = JSON.stringify(data);
	   console.log(resultStringfyData);
	   var responseJson = $.parseJSON(resultStringfyData);
	   
	   console.log(responseJson.length);
	   
	   for (var x = 0; x < responseJson.length; x++) {
		   console.log(responseJson[x].geonameId);
		   console.log(responseJson[x].name);
		   if (idCodeArr[1] == responseJson[x].countryCode) {
 		       var div_data = "<option value="+responseJson[x].geonameId+">"+responseJson[x].name+"</option>";
		       $(div_data).appendTo('#city');
		   }
	   }
    });

	 
	});
});

