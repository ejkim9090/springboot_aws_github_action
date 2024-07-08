package mclass.store.tripant.config;

import java.io.IOException;
import java.util.Map;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import com.nimbusds.jose.shaded.gson.Gson;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class OAuth2FailureHandler extends SimpleUrlAuthenticationFailureHandler {

	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException exception) throws IOException, ServletException {
		if(exception instanceof UsernameNotFoundException) {
			String msg = exception.getMessage();
			Map<String, Object> map = new Gson().fromJson(msg, Map.class);
			request.getSession().setAttribute("memEmail", map.get("memEmail"));
			request.getSession().setAttribute("memType", map.get("memType"));
			response.setContentType("text/html; charset=utf-8");
			response.getWriter().append("<script type=\"text/javascript\">"
					+ "location.href = \"/exception?code=4011\";"
					+ "</script>").close();
		}
		if(exception instanceof OAuth2AuthenticationException) {
			response.setContentType("text/html; charset=utf-8");
			response.getWriter().append("<script type=\"text/javascript\">"
					+ "location.href = \"/exception?code=4012\";"
					+ "</script>").close();
		}
		if(exception.getMessage().equals("4021")) {
			response.setContentType("text/html; charset=utf-8");
			response.getWriter().append("<script type=\"text/javascript\">"
					+ "location.href = \"/exception?code=4021\";"
					+ "</script>").close();
		}
	}
}
