package br.com.agrego.core.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled=true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.cors().and().csrf().disable().authorizeRequests().antMatchers("/*").permitAll().and();
	}
	
//	@Autowired
//	private UsuarioService customUserDetailService;
//	
//    private final static String API_BASE_PATH = "api";
//	
//	@Override
//	protected void configure(HttpSecurity http) throws Exception {
//		http.cors().and().csrf().disable().authorizeRequests()
//			.antMatchers(HttpMethod.GET, SecurityConstants.SIGN_UP_URL).permitAll()
//			.antMatchers(HttpMethod.POST, SecurityConstants.SIGN_UP_URL).permitAll()
////			.antMatchers("/teste/t2").hasRole("ADMIN")
////			.antMatchers("/teste/t4").permitAll()
////			.antMatchers("/controle").permitAll()
////			.antMatchers("/api/**").authenticated()
//			.antMatchers("/*").permitAll()
//			.anyRequest().authenticated()
////			.and().httpBasic()
//			.and()
//			.addFilter(new JWTAuthenticationFilter(authenticationManager()))
//			.addFilter(new JWTAuthorizationFilter(authenticationManager(), customUserDetailService));
//	}
//
//	@Override
//	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//		auth.userDetailsService(customUserDetailService).passwordEncoder(new BCryptPasswordEncoder());
//	}
	
	/**
	 * foi depreciado para esta versao do spring boot
	 */
//    @Bean
//    public WebMvcRegistrationsAdapter webMvcRegistrationsHandlerMapping() {
//        return new WebMvcRegistrationsAdapter() {
//            @Override
//            public RequestMappingHandlerMapping getRequestMappingHandlerMapping() {
//                return new RequestMappingHandlerMapping() {
// 
//                    @Override
//                    protected void registerHandlerMethod(Object handler, Method method, RequestMappingInfo mapping) {
//                        Class<?> beanType = method.getDeclaringClass();
//                        if (AnnotationUtils.findAnnotation(beanType, RestController.class) != null) {
//                            PatternsRequestCondition apiPattern = new PatternsRequestCondition(API_BASE_PATH)
//                                    .combine(mapping.getPatternsCondition());
// 
//                            mapping = new RequestMappingInfo(mapping.getName(), apiPattern,
//                                    mapping.getMethodsCondition(), mapping.getParamsCondition(),
//                                    mapping.getHeadersCondition(), mapping.getConsumesCondition(),
//                                    mapping.getProducesCondition(), mapping.getCustomCondition());
//                        }
// 
//                        super.registerHandlerMethod(handler, method, mapping);
//                    }
//                };
//            }
//        };
//    }
	
}
