package mclass.store.tripant;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;



@SpringBootTest
@WebAppConfiguration
class TripantApplicationTests {
	@Autowired
	private WebApplicationContext wac;
	private MockMvc mockMvc;
	@Test
	void contextLoads() {
	}
	@BeforeEach //JUnit 4의 @before
	void setUp() {
//		this.mockMvc = MockMvcBuilders.standaloneSetup(new Demo8Controller()).build();//Demo8Controller 가지고 기본 생성
		this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
		
		// 주로 service 있는 객체를 만들어주는 역할
//		this.service = new EmpService();
		System.out.println("============@BeforeEach=====@test 돌릴 준비 완료");
	}
	@Test
	void gethome() {
		try {
			//	mockMvc.perform(get("/emp")).andDo(print());
			mockMvc.perform(get("/fhone").param("findArea", "서울").param("page", "12") )
			.andDo(print()).andExpect(status().isNotFound());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	@Test
	void findarea() {
		try {
		//	mockMvc.perform(get("/emp")).andDo(print());
		mockMvc.perform(post("/find/area").param("findArea", "서울").param("page", "12") )
		.andDo(print()).andExpect(status().isOk());
		} catch (Exception e) {
						e.printStackTrace();
		}
	}
}
