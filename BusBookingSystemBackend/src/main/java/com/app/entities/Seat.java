package com.app.entities;

import java.util.List;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Seat extends Base{

	private long userId;
	private long busId;
//	private String sessionId;

	@ElementCollection(fetch = FetchType.EAGER)
	private List<Integer> seatNos;
	
}