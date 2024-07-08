package mclass.store.tripant.config;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.zaxxer.hikari.HikariDataSource;

import lombok.RequiredArgsConstructor;
import mclass.store.tripant.apikeys.KeysJaewon;

@Configuration
@RequiredArgsConstructor
public class HikariConfig{

	private final KeysJaewon keysJaewon;
	
	@Bean
	DataSource dataSource() {
		com.zaxxer.hikari.HikariConfig hikariConfig = new com.zaxxer.hikari.HikariConfig();

		hikariConfig.setJdbcUrl(keysJaewon.getHikariDriverClassName());
		hikariConfig.setJdbcUrl(keysJaewon.getHikariUrl());
		hikariConfig.setUsername(keysJaewon.getHikariUsername());
		hikariConfig.setPassword(keysJaewon.getHikariPassword());

		return new HikariDataSource(hikariConfig);
    }
}