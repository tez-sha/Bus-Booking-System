package com.app.controller;

import java.util.List;

import javax.annotation.security.PermitAll;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.dto.GetStationsDto;
import com.app.dto.StationNameDto;
import com.app.entities.Station;
import com.app.service.StationService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping("/station")

public class StationController {

	@Autowired
	private StationService stationService;
	
	
	@SecurityRequirement(name = "bearerAuth")
	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/addstation")
	public ResponseEntity<?> addStation(@RequestBody StationNameDto stationDto){
		
		Station station=new Station();
		station.setStationName(stationDto.getStation_name());
		return ResponseEntity.ok(stationService.addStation(station));
	}
	
	@PermitAll
	@GetMapping("/getstations")
	public List<GetStationsDto> getStations(){
		return stationService.getStations();
	}
	
}
