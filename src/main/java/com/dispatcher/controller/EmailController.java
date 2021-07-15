package com.dispatcher.controller;

import java.io.IOException;
import java.util.List;
import javax.mail.Folder;
import javax.mail.MessagingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dispatcher.Application;
import com.dispatcher.exception.ResourceNotFoundException;
import com.dispatcher.service.EmailService;
import com.dispatcher.model.Reponse;
import com.dispatcher.entity.Event;

@RestController
public class EmailController {

	@Value("${server.servlet.context-path}")
	String serviceUri;

	private static final Logger LOGGER = LoggerFactory.getLogger(Application.class);

	@Autowired
	private EmailService emailService;

	@GetMapping(value = "/email", produces = MediaType.APPLICATION_JSON_VALUE)
	synchronized String readEmail() throws MessagingException, IOException {

		LOGGER.info("Start process");

		return emailService.readEmail();
	}

}
