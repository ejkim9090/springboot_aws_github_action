package mclass.store.tripant.plan.domain;

import java.util.List;


import lombok.Data;

@Data
public class CalendarPlanEntity {
	private List<PlanDate> dateArr;
	private List<Spot> spotArr;
	private String timeRange;
}
