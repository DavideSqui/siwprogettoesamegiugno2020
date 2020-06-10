package com.example.demo.autentication;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static com.example.demo.model.Credentials.ADMIN_ROLE;

@Configuration
@EnableWebSecurity
public class AuthConfiguration extends WebSecurityConfigurerAdapter{
	//Questa utilizza un datasource

	@Autowired
	private DataSource datasource;

	@Override
	public void  configure(HttpSecurity http) throws Exception {
		http
		//who can pass determined pages
		.authorizeRequests()
		.antMatchers(HttpMethod.GET,"/","/index","/login","/users/register").permitAll()
		//comandi per chi utilizza la registrazione
		.antMatchers(HttpMethod.POST,"/login","/users/register").permitAll()
		//permessi admin
		//rejects qualsiasi indirizzo con admin.. acceduto esclusivamente da ruolo admin
		.antMatchers(HttpMethod.GET,"/admin/**").hasAnyAuthority(ADMIN_ROLE)
		.antMatchers(HttpMethod.POST,"/admin/**").hasAnyAuthority(ADMIN_ROLE)
		//qualsiasi altra è autenticata 
		.anyRequest()
		.authenticated()
		//paragrafo2
		//performa il login
		.and().formLogin()
		.defaultSuccessUrl("/home")

		//parte 3 di logout
		.and().logout().logoutUrl("/logout")
		//ritornati a index d'inizio benvenuto
		.logoutSuccessUrl("/index")

		.invalidateHttpSession(true)
		.clearAuthentication(true).permitAll();
		//interrompere sessione dopo il logout
	}

	@Override
	public void configure(AuthenticationManagerBuilder auth) throws Exception{
		auth.jdbcAuthentication()
		//access savecredentials
		.dataSource(this.datasource)
		//ricerca
		.authoritiesByUsernameQuery("SELECT username, role FROM credentials WHERE username=?")
		.usersByUsernameQuery("SELECT username, password, 1 as enabled FROM credentials WHERE username=?");
	}

	//per non permettere che le password siano in chiaro utilizziam questo
	@Bean
	PasswordEncoder passwordEncoder() {return new BCryptPasswordEncoder();}

}
