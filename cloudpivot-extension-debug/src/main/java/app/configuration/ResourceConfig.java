package app.configuration;

import com.authine.cloudpivot.web.api.filter.PermitAuthenticationFilter;
import com.authine.cloudpivot.web.api.handler.AccessDeniedHandlerImpl;
import com.authine.cloudpivot.web.api.security.CustomAccessTokenConverter;
import com.authine.cloudpivot.web.api.security.CustomOAuth2AuthExceptionEntryPoint;
import com.authine.cloudpivot.web.sso.security.JksLoader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.web.header.HeaderWriterFilter;


@Slf4j
@Configuration
@EnableResourceServer
@SuppressWarnings("unused")
@Order(6)
public class ResourceConfig extends ResourceServerConfigurerAdapter {

    @Value("${cloudpivot.api.oauth.enabled:true}")
    private boolean oauthEnabled;

    @Autowired
    private AccessDeniedHandlerImpl accessDeniedHandler;

    @Autowired
    private CustomOAuth2AuthExceptionEntryPoint point;

    @Autowired
    private PermitAuthenticationFilter permitAuthenticationFilter;

    @Autowired
    private JksLoader jksLoader;

    @Bean
    public TokenStore tokenStore() {
        return new JwtTokenStore(accessTokenConverter());
    }

    @Bean
    public JwtAccessTokenConverter accessTokenConverter() {
        final JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        converter.setKeyPair(jksLoader.getKeyPair());
        converter.setAccessTokenConverter(new CustomAccessTokenConverter());
        return converter;
    }

    @Bean
    public ResourceServerTokenServices tokenServices() {
        final DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
        defaultTokenServices.setTokenStore(tokenStore());
        defaultTokenServices.setTokenEnhancer(accessTokenConverter());
        defaultTokenServices.setSupportRefreshToken(true);
        return defaultTokenServices;
    }

    @Override
    public void configure(final HttpSecurity http) throws Exception {
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.sessionManagement().sessionFixation().none();

        //开启权限校验
        if (oauthEnabled) {
//            http.headers().cacheControl().disable();
            http.authorizeRequests()
                    // swagger start
                    .antMatchers("/swagger-ui/**").permitAll()
                    .antMatchers("/doc.html").permitAll()
                    .antMatchers("/swagger-resources/**").permitAll()
                    .antMatchers("/swagger/api/**").permitAll()
                    .antMatchers("/images/**").permitAll()
                    .antMatchers("/webjars/**").permitAll()
                    .antMatchers("/v2/api-docs").permitAll()
                    .antMatchers("/v3/api-docs").permitAll()
                    .antMatchers("/app/modules/meeting/configuration/ui").permitAll()
                    .antMatchers("/app/modules/meeting/configuration/security").permitAll()
                    .antMatchers("/public/**").permitAll()
                    .antMatchers("/externalLink/**").permitAll()
                    .antMatchers("/login/**").permitAll()
                    .antMatchers("/actuator/**").permitAll()
                    //openapi
                    .antMatchers("/openapi/**").hasAuthority("openapi")
                    //生成二维码
                    .antMatchers("/api/qrcode/**").permitAll()
                    .antMatchers("/api/runtime/convert/download").permitAll()
                    // swagger end
                    .antMatchers("/monitor/**").permitAll()
                    .antMatchers("/oauth/**").permitAll()
                    .antMatchers("/login").permitAll()
                    .antMatchers("/oauth").permitAll()
                    .antMatchers("/logout").permitAll()
                    .antMatchers("/css/**").permitAll()
                    .antMatchers("/js/**").permitAll()
                    .antMatchers("/fonts/**").permitAll()
                    .antMatchers("/favicon.*").permitAll()
                    .antMatchers("/api/dingtalk/**").permitAll()
                    .antMatchers("/api/auth/**").permitAll()
                    //test
                    .antMatchers("/api/licenseExt/**").permitAll()
                    //report integrate
                    .antMatchers("/v1/dashboard/**").permitAll()
                    .antMatchers("/v1/advanced/**").permitAll()
                    //download
                    .antMatchers("/api/aliyun/download").permitAll()
                    .antMatchers("/api/aliyun/download_img/**").anonymous()
                    //客户端接口测试
                    .antMatchers("/api/client/**").hasAuthority("AUTH_SYSTEM_MANAGE")
                    .antMatchers("/api/fileServer/attachment").permitAll()
                    .antMatchers("/api/app/market/file/download").permitAll()

                    //api/**
                    .antMatchers(HttpMethod.OPTIONS, "/api/**").permitAll()
                    .antMatchers("/api/**").hasAuthority("AUTH_SYSTEM_MANAGE")
                    .anyRequest().authenticated()
                    .and().exceptionHandling().authenticationEntryPoint(point).accessDeniedHandler(accessDeniedHandler);
        } else {
            http.authorizeRequests()
                    .antMatchers("/public/**").permitAll()
                    .antMatchers("/actuator/**").permitAll()
                    .antMatchers("/externalLink/**").permitAll()
                    .antMatchers("/api/fileServer/attachment").permitAll()
                    .antMatchers(HttpMethod.OPTIONS, "/api/**").permitAll()
                    .antMatchers("/api/**").permitAll()
                    .and().exceptionHandling().accessDeniedHandler(accessDeniedHandler);
        }

        http.addFilterBefore(permitAuthenticationFilter, HeaderWriterFilter.class);
    }

    @Override
    public void configure(final ResourceServerSecurityConfigurer config) {
        config.resourceId("api").tokenServices(tokenServices()).tokenStore(tokenStore())
                .authenticationEntryPoint(point).accessDeniedHandler(accessDeniedHandler);
    }

}
