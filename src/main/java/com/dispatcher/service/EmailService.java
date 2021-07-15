package com.dispatcher.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.dispatcher.exception.ResourceNotFoundException;
import com.dispatcher.repository.EventRepository;
import com.dispatcher.model.Reponse;
import com.dispatcher.entity.Event;
import com.dispatcher.entity.EventDemo;
import com.dispatcher.entity.EmailConfig;
import com.dispatcher.model.MessageParser;

@Service
public class EmailService {

	private String OcrUrl = "http://localhost:8084/ocr/v1.0/callback";

	private RestTemplate restTemplate = new RestTemplate();

	@Autowired
	private EmailConfig emailConfig;

	@Autowired
	private EventRepository eventRepository;

	Folder emailFolder;
	Store store;
	Properties properties = new Properties();
	String email = "";

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
					HttpHeaders headers = new HttpHeaders();
					headers.setContentType(MediaType.APPLICATION_JSON);
					HttpEntity<EventDemo> entity = new HttpEntity<EventDemo>(event, headers);
					String result;
					try {
						restTemplate.exchange(OcrUrl, HttpMethod.POST, entity, EventDemo.class);

						result = new ResponseEntity<Reponse>(resp, HttpStatus.CREATED).toString();
						System.out.println(result);
					}

					catch (Exception e) {

						resp.setRegistros_status("FAILED");
						resp.setRegistros_fallidos(resp.getRegistros_fallidos() + 1);
						result = new ResponseEntity<Reponse>(resp, HttpStatus.NOT_FOUND).toString();
						System.out.println(result);

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
