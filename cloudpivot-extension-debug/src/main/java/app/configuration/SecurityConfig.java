package app.configuration;

import com.authine.cloudpivot.web.sso.filter.DingTalkAuthenticationFilter;
import com.authine.cloudpivot.web.sso.filter.DingTalkMobileAjaxAuthenticationFilter;
import com.authine.cloudpivot.web.sso.filter.MobilePhoneAuthenticationFilter;
import com.authine.cloudpivot.web.sso.filter.RASUsernamePasswordFilter;
import com.authine.cloudpivot.web.sso.filter.SetThreadLocalValueFilter;
import com.authine.cloudpivot.web.sso.filter.TemplateFilter;
import com.authine.cloudpivot.web.sso.filter.UsernamePasswordAjaxAuthenticationFilter;
import com.authine.cloudpivot.web.sso.handler.BaseAuthenticationHandler;
import com.authine.cloudpivot.web.sso.handler.CustomAccessDeniedHandler;
import com.authine.cloudpivot.web.sso.handler.DefaultAuthenticationFailureHandler;
import com.authine.cloudpivot.web.sso.handler.DefaultAuthenticationSuccessHandler;
import com.authine.cloudpivot.web.sso.handler.DingTalkAjaxAuthenticationHandler;
import com.authine.cloudpivot.web.sso.handler.DingTalkAuthenticationHandler;
import com.authine.cloudpivot.web.sso.handler.LogoutAndRemoveTokenHandler;
import com.authine.cloudpivot.web.sso.handler.UsernamePasswordAjaxAuthenticationHandler;
import com.authine.cloudpivot.web.sso.security.BaseAuthenticationProvider;
import com.authine.cloudpivot.web.sso.security.DingTalkAuthenticationProvider;
import com.authine.cloudpivot.web.sso.security.DingTalkMobileAjaxAuthenticationProvider;
import com.authine.cloudpivot.web.sso.security.DingTalkMobileAuthenticationProvider;
import com.authine.cloudpivot.web.sso.security.UserDetailsCheckerImpl;
import com.authine.cloudpivot.web.sso.security.UserDetailsServiceImpl;
import com.authine.cloudpivot.web.sso.security.UsernamePasswordAjaxAuthenticationProvider;
import com.authine.cloudpivot.web.sso.template.BaseSimpleTemplate;
import com.google.common.collect.ImmutableList;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.CollectionUtils;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import javax.servlet.Filter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Configuration
@EnableWebSecurity
@Order(2)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
    @Autowired
    private UserDetailsServiceImpl userDetailsService;
    @Autowired
    private CustomAccessDeniedHandler customAccessDeniedHandler;
    @Autowired
    private LogoutAndRemoveTokenHandler logoutAndRemoveTokenHandler;
    @Autowired
    private DingTalkAuthenticationHandler dingTalkAuthenticationHandler;
    @Autowired
    private DingTalkAuthenticationProvider dingTalkAuthenticationProvider;
    @Autowired
    private DingTalkMobileAuthenticationProvider dingTalkMobileAuthenticationProvider;
    @Autowired
    private UsernamePasswordAjaxAuthenticationProvider usernamePasswordAuthenticationProvider;
    @Autowired
    private DingTalkMobileAjaxAuthenticationProvider dingTalkMobileAjaxAuthenticationProvider;
    @Autowired
    private DingTalkAjaxAuthenticationHandler dingTalkAjaxAuthenticationHandler;

    @Autowired
    private UsernamePasswordAjaxAuthenticationHandler usernamePasswordAuthenticationHandler;

    @Autowired
    private DefaultAuthenticationSuccessHandler defaultAuthenticationSuccessHandler;

    @Autowired
    private DefaultAuthenticationFailureHandler defaultAuthenticationFailureHandler;

    @Value("#{'${cloudpivot.webmvc.corsAllowedOrigins}'.split(',')}")
    private String[] corsAllowedOrigins;

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private RedisTemplate redisTemplate;


//    @Value("${cloudpivot.webmvc.corsmappings:true}")
//    private boolean corsMappings;

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() {
//        return super.authenticationManagerBean();
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userDetailsService);
        //认证后用户信息检查
        UserDetailsCheckerImpl userDetailsChecker = new UserDetailsCheckerImpl();
        daoAuthenticationProvider.setPostAuthenticationChecks(userDetailsChecker);
        return new ProviderManager(ImmutableList.of(daoAuthenticationProvider));
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED);
        http.sessionManagement().sessionFixation().migrateSession();
        http.cors().and().csrf().requireCsrfProtectionMatcher(request -> false).and().headers().frameOptions().disable();
        http.requestMatchers().antMatchers("/login/dingtalk", "login/mobile", "login/mobile/ajax", "login/password", "/oauth/authorize", "/login/**", "/oauth/**", "/login", "/oauth", "/logout/**", "/logout", "login/wx/ajax")
                .and()
                .authorizeRequests()
                .antMatchers("/oauth/**", "/logout").authenticated()
                .antMatchers(HttpMethod.OPTIONS).permitAll()
                .antMatchers("/actuator/**", "/monitor/**", "/login/dingtalk", "login/mobile", "login/mobile/ajax", "login/password", "login/wx/ajax").permitAll()
                .and().formLogin().loginPage("/login").permitAll().failureUrl("/login?error")
                .failureHandler(defaultAuthenticationFailureHandler).and().rememberMe()
                .and().logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .addLogoutHandler(logoutAndRemoveTokenHandler).permitAll()
                .and().exceptionHandling().accessDeniedHandler(customAccessDeniedHandler).accessDeniedPage("/access?error");


        http.addFilterAt(loginAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
        http.addFilterBefore(new SetThreadLocalValueFilter(), UsernamePasswordAuthenticationFilter.class);
        http.addFilterBefore(phoneAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
        http.addFilterBefore(dingTalkAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
        http.addFilterBefore(dingTalkMobileAjaxAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
        http.addFilterAfter(usernamePasswordAjaxAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
        setTemplateFilter(http);
    }

    private void setTemplateFilter(HttpSecurity http) {
        //获取所有被Spring管理的继承BaseSimpleTemplate的类
        Map<String, BaseSimpleTemplate> allTemplate =
                BeanFactoryUtils.beansOfTypeIncludingAncestors(applicationContext, BaseSimpleTemplate.class, true, false);
        if (!CollectionUtils.isEmpty(allTemplate)) {
            for (Map.Entry<String, BaseSimpleTemplate> entry : allTemplate.entrySet()) {
                //初始化过滤器
                BaseSimpleTemplate simpleOAuth2Template = entry.getValue();
                TemplateFilter filter = new TemplateFilter(simpleOAuth2Template);
                BaseAuthenticationProvider provider = applicationContext.getBean(BaseAuthenticationProvider.class);
                BaseAuthenticationHandler handler = applicationContext.getBean(BaseAuthenticationHandler.class);
                handler.setConfig(simpleOAuth2Template.getConfig());
                filter.setCodeString(simpleOAuth2Template.getConfig().getCodeName());
                filter.setAuthenticationManager(new ProviderManager(Collections.singletonList(provider)));
                filter.setAuthenticationSuccessHandler(handler);
                filter.setAuthenticationFailureHandler(handler);
                //添加到过滤器链中
                http.addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);
            }
        }
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
    }

    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers("/vendor/**", "/fonts/**", "/themes/**");
    }

    @Bean
    @ConditionalOnProperty(name = "cloudpivot.webmvc.corsmappings", havingValue = "true")
    public FilterRegistrationBean<Filter> corsFilterRegistrationBean() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(Boolean.TRUE);
        List<String> allowedOrigins = Arrays.asList(corsAllowedOrigins);
        if (allowedOrigins.contains("*")) {
            config.addAllowedOriginPattern("*");
        } else {
            config.setAllowedOrigins(allowedOrigins);
        }
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        source.registerCorsConfiguration("/**", config);
        FilterRegistrationBean<Filter> bean = new FilterRegistrationBean<Filter>(new CorsFilter(source));
        bean.setOrder(-100);
        return bean;
    }

//    @Bean
//    public FilterRegistrationBean<Filter> customCacheControlFilter() {
//        FilterRegistrationBean<Filter> bean = new FilterRegistrationBean<>();
//        bean.setFilter(new OncePerRequestFilter() {
//            @Override
//            protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
//                String uri = request.getRequestURI();
//                if ((request.getContextPath() + "/api/aliyun/download").equals(uri)) {
//                    response.setHeader("Cache-Control", "max-age=600");
//                } else {
//                    response.setHeader("Cache-Control", "no-cache, no-store, max-age=0, must-revalidate");
//                }
//                filterChain.doFilter(request, response);
//            }
//        });
//        bean.setOrder(102);
//        bean.addUrlPatterns("/*");
//        bean.setName("cCustomNoCacheControlFilter");
//        return bean;
//    }


    public RASUsernamePasswordFilter loginAuthenticationFilter() {
        RASUsernamePasswordFilter loginAuthenticationFilter = new RASUsernamePasswordFilter(redisTemplate);
        loginAuthenticationFilter.setAuthenticationManager(authenticationManagerBean());
        loginAuthenticationFilter.setAuthenticationSuccessHandler(defaultAuthenticationSuccessHandler);
        loginAuthenticationFilter.setAuthenticationFailureHandler(defaultAuthenticationFailureHandler);
        return loginAuthenticationFilter;
    }

    /**
     * 内置扩展用户名密码登录模式
     */
    public UsernamePasswordAjaxAuthenticationFilter usernamePasswordAjaxAuthenticationFilter() {
        UsernamePasswordAjaxAuthenticationFilter filter = new UsernamePasswordAjaxAuthenticationFilter();
        ProviderManager providerManager = new ProviderManager(Collections.singletonList(usernamePasswordAuthenticationProvider));
        filter.setAuthenticationManager(providerManager);
        filter.setAuthenticationSuccessHandler(usernamePasswordAuthenticationHandler);
        filter.setAuthenticationFailureHandler(usernamePasswordAuthenticationHandler);
        return filter;
    }

    /**
     * 钉钉应用内免登,废弃
     */
    public MobilePhoneAuthenticationFilter phoneAuthenticationFilter() {
        MobilePhoneAuthenticationFilter filter = new MobilePhoneAuthenticationFilter();
        ProviderManager providerManager = new ProviderManager(Collections.singletonList(dingTalkMobileAuthenticationProvider));
        filter.setAuthenticationManager(providerManager);
        filter.setAuthenticationSuccessHandler(dingTalkAuthenticationHandler);
        filter.setAuthenticationFailureHandler(dingTalkAuthenticationHandler);
        return filter;
    }

    /**
     * 钉钉扫码登录
     */
    public DingTalkAuthenticationFilter dingTalkAuthenticationFilter() {
        DingTalkAuthenticationFilter filter = new DingTalkAuthenticationFilter();
        ProviderManager providerManager = new ProviderManager(Collections.singletonList(dingTalkAuthenticationProvider));
        filter.setAuthenticationManager(providerManager);
        filter.setAuthenticationSuccessHandler(dingTalkAuthenticationHandler);
        filter.setAuthenticationFailureHandler(dingTalkAuthenticationHandler);
        return filter;
    }

    /**
     * 钉钉应用内免登
     */
    public DingTalkMobileAjaxAuthenticationFilter dingTalkMobileAjaxAuthenticationFilter() {
        DingTalkMobileAjaxAuthenticationFilter filter = new DingTalkMobileAjaxAuthenticationFilter();
        ProviderManager providerManager = new ProviderManager(Collections.singletonList(dingTalkMobileAjaxAuthenticationProvider));
        filter.setAuthenticationManager(providerManager);
        filter.setAuthenticationSuccessHandler(dingTalkAjaxAuthenticationHandler);
        filter.setAuthenticationFailureHandler(dingTalkAjaxAuthenticationHandler);
        return filter;
    }

}
