package com.dispatcher;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import com.dispatcher.controller.EmailController;
import com.dispatcher.entity.Event;
import com.dispatcher.exception.ResourceNotFoundException;
import com.dispatcher.repository.EventRepository;
import com.dispatcher.service.EmailService;

import junit.framework.Assert;

@RunWith(SpringRunner.class)
@SpringBootTest
public class EmailControllerTest {
	
	@Autowired
	private EmailService emailService;
	
	@Autowired
	private EmailController emailController;

	@Autowired
	private EventRepository eventRepository;
	

}
