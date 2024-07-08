package mclass.store.tripant.plan.domain;

import org.springframework.stereotype.Component;
import lombok.Data;

@Data
@Component
public class PlanEntity {
	//일정
//	PLAN_ID         NOT NULL NUMBER        
//	PLAN_AREA_CODE  NOT NULL NUMBER        
//	PLAN_TITLE      NOT NULL VARCHAR2(100) 
//	PLAN_START_DAY  NOT NULL DATE          
//	PLAN_END_DAY             DATE          
//	PLAN_MAKE_DAY   NOT NULL DATE          
//	PLAN_DELETE_DAY          DATE  
	private Integer planId;
	private Integer planAreaCode;
	private String planTitle;
	private String planStartDay;
	private String planEndDay;
	private String planMakeDay;
	private String planDeleteDay;
}
