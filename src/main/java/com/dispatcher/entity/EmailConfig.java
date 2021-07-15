package com.dispatcher.entity;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "spring.mail")
public class EmailConfig {
	private String host;
	private Integer port;
	private String username;
	private String password;
	private String protocol;

	public EmailConfig() {
		super();
	}

	public EmailConfig(String host, Integer port, String username, String password, String protocol) {
		super();
		this.host = host;
		this.port = port;
		this.username = username;
		this.password = password;
		this.protocol = protocol;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public Integer getPort() {
		return port;
	}

	public void setPort(Integer port) {
		this.port = port;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getProtocol() {
		return protocol;
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	@Override
	public String toString() {
		return "EmailConfig [host=" + host + ", port=" + port + ", username=" + username + ", password=" + password
				+ ", protocol=" + protocol + "]";
	}

}
