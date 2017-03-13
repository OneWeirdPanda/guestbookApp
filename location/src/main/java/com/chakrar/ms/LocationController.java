package com.chakrar.ms;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

@RestController
@CrossOrigin(origins = "http://localhost:8080")
@RequestMapping("/location")
public class LocationController {

	private static final Logger log = LoggerFactory.getLogger(LocationController.class);
	
	
	
	static Multimap<String, List<CountryElement>> countyToCityMap = ArrayListMultimap.create();
	
	static {
		getCountryCityMap() ;
	}

	@RequestMapping(value = "/countries", method = RequestMethod.GET)
	public @ResponseBody List<CountryElement> getCountries(
			@RequestParam(value = "username", required = false, defaultValue = "demo") String username) {

		RestTemplate restTemplate = new RestTemplate();

		String url = "http://api.geonames.org/countryInfoJSON";

		// URI (URL) parameters
		Map<String, String> uriParams = new HashMap<String, String>();
		uriParams.put("username", username);

		// Query parameters
		UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url)
				// Add query parameter
				.queryParam("username", username);

		System.out.println(builder.buildAndExpand(uriParams).toUri());

		ResponseEntity<CountryWrapper> countryList = restTemplate.exchange(builder.buildAndExpand(uriParams).toUri(),
				HttpMethod.GET, null, CountryWrapper.class);

		// CountryWrapper countryList =
		// restTemplate.getForObject(builder.buildAndExpand(uriParams).toUri(),
		// CountryWrapper.class);

		System.out.println("countryList = " + countryList.getBody().getGeonames().size());

		/*
		 * ResponseEntity<List<CountryElement>> rateResponse =
		 * restTemplate.exchange( builder.buildAndExpand(uriParams).toUri(),
		 * HttpMethod.GET, null, new
		 * ParameterizedTypeReference<List<CountryElement>>() { });
		 * List<CountryElement> countries = rateResponse.getBody();
		 */

		return countryList.getBody().getGeonames();

	}

	@RequestMapping(value = "/cities", method = RequestMethod.GET)
	public @ResponseBody List<CountryElement> getCities(
			@RequestParam(value = "geonameId", required = false, defaultValue = "6252001") String geonameId,
			@RequestParam(value = "username", required = false, defaultValue = "demo") String username) {

		log.info(" getCities Start ");
		SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
		factory.setBufferRequestBody(false);
		RestTemplate restTemplate = new RestTemplate(factory);

		String url = "http://api.geonames.org/childrenJSON";

		// URI (URL) parameters
		Map<String, String> uriParams = new HashMap<String, String>();
		uriParams.put("geonameId", geonameId);
		uriParams.put("username", username);

		// Query parameters
		UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url)
				// Add query parameter
				.queryParam("geonameId", geonameId).queryParam("username", username);

		System.out.println(builder.buildAndExpand(uriParams).toUri());

		ResponseEntity<CountryWrapper> countryList = restTemplate.exchange(builder.buildAndExpand(uriParams).toUri(),
				HttpMethod.GET, null, CountryWrapper.class);

		System.out.println("countryList = " + countryList.getBody().getGeonames().size());
		List<CountryElement> stateList = countryList.getBody().getGeonames();

		ResponseEntity<CountryWrapper> areaList = null;
		List<CountryElement> areaCodeFullList = new ArrayList<CountryElement>();
		Map<String, String> uriParamsAreaQuery = new HashMap<String, String>();
		UriComponentsBuilder builderAreaQuery = UriComponentsBuilder.fromUriString(url);
		for (CountryElement c : stateList) {

			uriParamsAreaQuery.put("geonameId", c.getGeonameId());
			uriParamsAreaQuery.put("username", username);

			builderAreaQuery.queryParam("geonameId", c.getGeonameId()).queryParam("username", username);
			// System.out.println(" area code query URI =
			// "+builderAreaQuery.buildAndExpand(uriParamsAreaQuery).toUri());
			areaList = restTemplate.exchange(builderAreaQuery.buildAndExpand(uriParamsAreaQuery).toUri(),
					HttpMethod.GET, null, CountryWrapper.class);
			// System.out.println("areaList = "+
			// areaList.getBody().getGeonames().size());
			areaCodeFullList.addAll(areaList.getBody().getGeonames());
		}
		// System.out.println("areaCodeFullList size = "+
		// areaCodeFullList.size());

		List<CountryElement> areaCodeTruncatedList = new ArrayList<CountryElement>();
		for (int i = 0; i < 300; i++) {
			areaCodeTruncatedList.add(areaCodeFullList.get(i));
		}

		ResponseEntity<CountryWrapper> cityList = null;
		List<CountryElement> cityFullList = new ArrayList<CountryElement>();

		for (CountryElement ce : areaCodeFullList) {
			UriComponentsBuilder builderCityQuery = UriComponentsBuilder.fromUriString(url);
			Map<String, String> uriParamsCityQuery = new HashMap<String, String>();
			uriParamsCityQuery.put("geonameId", ce.getGeonameId());
			uriParamsCityQuery.put("username", username);

			builderCityQuery.queryParam("geonameId", ce.getGeonameId()).queryParam("username", username);
			// System.out.println(" city query URI =
			// "+builderCityQuery.buildAndExpand(uriParamsCityQuery).toUri());
			cityList = restTemplate.exchange(builderCityQuery.buildAndExpand(uriParamsCityQuery).toUri(),
					HttpMethod.GET, null, CountryWrapper.class);
			if (null != cityList.getBody() && null != cityList.getBody().getGeonames()) {
				// System.out.println("cityList = "+
				// cityList.getBody().getGeonames().size());
				cityFullList.addAll(cityList.getBody().getGeonames());
			}

		}
		System.out.println("cityFullList size = " + cityFullList.size());

		Set<CountryElement> finalCityList = new LinkedHashSet<CountryElement>();
		finalCityList.addAll(cityFullList);
		System.out.println("finalCityList size = " + finalCityList.size());
		cityFullList.clear();
		cityFullList.addAll(finalCityList);
		log.info(" getCities end ");
		return cityFullList;

	}

	/**
	 * 
	 * @param username
	 * @return
	 */
	
	@GetMapping("/countriesMock")
	public @ResponseBody List<CountryElement> getCountriesMock(
			@RequestParam(value = "username", required = false, defaultValue = "demo") String username) {
		System.out.println(" countriesMock called ");
		List<CountryElement> lce = new ArrayList<CountryElement>();
		CountryElement ce1 = new CountryElement();
		ce1.setCountryCode("US");
		ce1.setGeonameId("6252001");
		ce1.setCountryName("United States");

		CountryElement ce2 = new CountryElement();
		ce2.setCountryCode("SE");
		ce2.setGeonameId("2661886");
		ce2.setCountryName("Sweden");

		CountryElement ce3 = new CountryElement();
		ce3.setCountryCode("DE");
		ce3.setGeonameId("2921044");
		ce3.setCountryName("Germany");
		
		CountryElement ce4 = new CountryElement();
		ce4.setCountryCode("CA");
		ce4.setGeonameId("6251999");
		ce4.setCountryName("Canada");

		lce.add(ce1);
		lce.add(ce2);
		lce.add(ce3);
		lce.add(ce4);
		System.out.println(" countriesMock exit ");
		return lce;
	}

	/**
	 * 
	 * @param username
	 * @return
	 */
	@RequestMapping(value = "/citiesMock", method = RequestMethod.GET)
	public @ResponseBody List<CountryElement> getCitiesMock(
			@RequestParam(value = "geonameId", required = false, defaultValue = "6252001") String geonameId) {

		Collection<List<CountryElement>> citiesVal = null;
		
		
        System.out.println("Key = " + geonameId);
        System.out.println("Values = " + countyToCityMap.get(geonameId) + "\n");
            
        citiesVal = countyToCityMap.get(geonameId);
        System.out.println(citiesVal.size());
        
        List<CountryElement> l = new ArrayList<>();
		for (List<CountryElement> lce : citiesVal) {
			l = lce;
		}
		
		
		
		
		System.out.println(" mock city list := "+ l);

		return l;
		
	}

	private static void getCountryCityMap() {

		
		List<CountryElement> usCities = new LinkedList<CountryElement>();
		List<CountryElement> germanyCities = new LinkedList<CountryElement>();
		List<CountryElement> canadaCities = new LinkedList<CountryElement>();
		
		CountryElement gc1 = new CountryElement();
		gc1.setCountryCode("DE");
		gc1.setGeonameId("24083805");
		gc1.setCountryName("Germany");
		gc1.setName("Berlin");

		CountryElement gc2 = new CountryElement();
		gc2.setCountryCode("DE");
		gc2.setGeonameId("2058069");
		gc2.setCountryName("Germany");
		gc2.setName("Munich");
		
		germanyCities.add(gc1);
		germanyCities.add(gc2);
		
		CountryElement ce1 = new CountryElement();
		ce1.setCountryCode("US");
		ce1.setGeonameId("4083805");
		ce1.setCountryName("United States");
		ce1.setName("Plano");

		CountryElement ce2 = new CountryElement();
		ce2.setCountryCode("US");
		ce2.setGeonameId("4058069");
		ce2.setCountryName("United States");
		ce2.setName("Dallas");

		CountryElement ce3 = new CountryElement();
		ce3.setCountryCode("US");
		ce3.setGeonameId("4063993");
		ce3.setCountryName("United States");
		ce3.setName("Georgetown");

		CountryElement ce4 = new CountryElement();
		ce4.setCountryCode("US");
		ce4.setGeonameId("4085986");
		ce4.setCountryName("United States");
		ce4.setName("Richardson");
		
		CountryElement ce5 = new CountryElement();
		ce5.setCountryCode("US");
		ce5.setGeonameId("4058738");
		ce5.setCountryName("United States");
		ce5.setName("Detroit");
		
		CountryElement ce6 = new CountryElement();
		ce6.setCountryCode("US");
		ce6.setGeonameId("4096463");
		ce6.setCountryName("United States");
		ce6.setName("Washington");
		
		CountryElement ce7 = new CountryElement();
		ce7.setCountryCode("US");
		ce7.setGeonameId("Francisco");
		ce7.setCountryName("United States");
		ce7.setName("San Francisco");

		usCities.add(ce1);
		usCities.add(ce2);
		usCities.add(ce3);
		usCities.add(ce4);
		usCities.add(ce5);
		usCities.add(ce6);
		usCities.add(ce7);
		
		CountryElement ca1 = new CountryElement();
		ca1.setCountryCode("CA");
		ca1.setGeonameId("7870876");
		ca1.setCountryName("Canada");
		ca1.setName("Toronto");
		
		CountryElement ca2 = new CountryElement();
		ca2.setCountryCode("CA");
		ca2.setGeonameId("7870896");
		ca2.setCountryName("Canada");
		ca2.setName("Montreol");
		
		canadaCities.add(ca1);
		canadaCities.add(ca2);

		countyToCityMap.put("6252001", usCities);
		countyToCityMap.put("2921044", germanyCities);
		countyToCityMap.put("6251999", canadaCities);
		

	}

	private <T> List<List<T>> getChunkList(List<T> largeList, int chunkSize) {
		List<List<T>> chunkList = new ArrayList<>();
		for (int i = 0; i < largeList.size(); i += chunkSize) {
			chunkList.add(largeList.subList(i, i + chunkSize >= largeList.size() ? largeList.size() : i + chunkSize));
		}
		return chunkList;
	}

}
