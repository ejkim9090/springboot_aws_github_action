package mclass.store.tripant.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

//WebMvcConfigurer -> url 관련 설정
@Configuration
@EnableWebMvc
public class WebMvcConfig implements WebMvcConfigurer {
	
//	@Override
//	public void addInterceptors(InterceptorRegistry registry) {
//		registry.addInterceptor(new HandlerInterceptor() {
//			@Override
//			public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
//					throws Exception {
//				System.out.println("preHandler");
//				
//				HttpSession session = request.getSession();
//				
//				Object obj = session.getAttribute("sslogin");
//				
//				if(obj==null) {
//					response.sendRedirect(request.getContextPath()+"/login");
//					return false;
//				}else {
//					return true;
//				}
//			}
//		})
//		.addPathPatterns("/**")
//		.excludePathPatterns("/login")
//		.excludePathPatterns("/join")
//		.excludePathPatterns("/main/*", "/css/**", "/js/**", "/images/**","/fonts/**");
//		
//	}
	
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/**").addResourceLocations("classpath:/static/");
		registry.addResourceHandler("/images/**").addResourceLocations("classpath:/static/images/");
		registry.addResourceHandler("/css/**").addResourceLocations("classpath:/static/css/");
		registry.addResourceHandler("/js/**").addResourceLocations("classpath:/static/js/");
		registry.addResourceHandler("/fonts/**").addResourceLocations("classpath:/static/fonts/");
	}
}
