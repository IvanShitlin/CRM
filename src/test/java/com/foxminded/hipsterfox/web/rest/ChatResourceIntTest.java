package com.foxminded.hipsterfox.web.rest;

import com.foxminded.hipsterfox.HipsterfoxApp;
import com.foxminded.hipsterfox.domain.EmailMessage;
import com.foxminded.hipsterfox.repository.EmailAddressRepository;
import com.foxminded.hipsterfox.repository.EmailMessageRepository;
import com.foxminded.hipsterfox.service.ChatService;
import com.foxminded.hipsterfox.service.dto.EmailAddressDTO;
import com.foxminded.hipsterfox.service.dto.EmailMessageDTO;
import com.foxminded.hipsterfox.service.mapper.EmailMessageMapper;
import com.foxminded.hipsterfox.web.rest.errors.ExceptionTranslator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import static com.foxminded.hipsterfox.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = HipsterfoxApp.class)
public class ChatResourceIntTest {

    @Autowired
    private EmailMessageRepository emailMessageRepository;

    @Autowired
    private EmailAddressRepository emailAddressRepository;

    @Autowired
    private EmailMessageMapper emailMessageMapper;

    @Autowired
    private ChatService chatService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restChatMockMvc;

    private EmailMessage emailMessage;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        final ChatResource chatResource = new ChatResource(chatService);
        this.restChatMockMvc = MockMvcBuilders.standaloneSetup(chatResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

}
