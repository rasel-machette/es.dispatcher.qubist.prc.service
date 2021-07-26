package com.dispatcher.service;

import java.io.IOException;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.dispatcher.model.Reponse;
import com.dispatcher.entity.EventDemo;
import com.dispatcher.Application;
import com.dispatcher.entity.EmailConfig;
import com.dispatcher.model.MessageParser;

@Service
public class EmailService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(Application.class);
	
	@Value("${dispatcher.url}")
	String ocrUrl;

	private RestTemplate restTemplate = new RestTemplate();

	@Autowired
	private EmailConfig emailConfig;

	
	Folder emailFolder;
	Store store;
	Properties properties = new Properties();
	String email = "";
	String result;

	@PostConstruct
	public void setupEmail() {
		String server = emailConfig.getHost();
		properties.put("mail.pop3.host", server);
		properties.put("mail.pop3.port", emailConfig.getPort());
		Session emailSession = Session.getDefaultInstance(properties);
		Store store = null;
		try {
			store = emailSession.getStore("pop3s");
			store.connect(server, emailConfig.getUsername(), emailConfig.getPassword());
			emailFolder = store.getFolder("INBOX");
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}

	@Scheduled(fixedRate = 5000)
	public synchronized String readEmail() throws MessagingException, IOException {
		

		try {

			emailFolder.open(Folder.READ_ONLY);
			Message[] messages = emailFolder.getMessages();
			for (int i = 0; i < messages.length; i++) {
				Message message = messages[i];

				{
					email += "Subject: " + message.getSubject() + "\n" + "Body:"
							+ MessageParser.getMessageBody(message);

					EventDemo event = new EventDemo();
					event.setSubject(message.getSubject());
					event.setBody(MessageParser.getMessageBody(message));
					
					
					Reponse resp = new Reponse();
					resp.setNombre("Adding Email");
					resp.setRegistros_status("SUCCESS");
					LOGGER.info("sending email");
					HttpHeaders headers = new HttpHeaders();
					headers.setContentType(MediaType.APPLICATION_JSON);
					HttpEntity<EventDemo> entity = new HttpEntity<EventDemo>(event, headers);
					
					try {
						
						restTemplate.exchange(ocrUrl, HttpMethod.POST, entity, EventDemo.class);
						LOGGER.info("success");
						result = new ResponseEntity<Reponse>(resp, HttpStatus.CREATED).toString();
						
					}

					catch (Exception e) {
						LOGGER.error("The requested resource could not be found");
						resp.setRegistros_status("FAILED");
						resp.setRegistros_fallidos(resp.getRegistros_fallidos() + 1);
						result = new ResponseEntity<Reponse>(resp, HttpStatus.NOT_FOUND).toString();
						

					}

				}

			}

			emailFolder.close();

		} catch (MessagingException me) {
			me.printStackTrace();

		}
		return email;
	}
}
