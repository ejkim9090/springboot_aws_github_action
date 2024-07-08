package mclass.store.tripant.trip.domain;

import java.util.Date;

import lombok.Data;

@Data
public class TripListEntity {
	private String areaShortName;
	private String areaFileName;
	private Integer planId ;
	private Integer planAreaCode ;
	private String planTitle ;
	private Date planStartDay ;
	private Date planEndDay ;
	private String planMakeDay ;
	private String planDeleteDay ;
	private Integer planMemRole;
}
