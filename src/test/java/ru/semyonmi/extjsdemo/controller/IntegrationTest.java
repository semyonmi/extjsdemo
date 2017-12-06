package ru.semyonmi.extjsdemo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.http.MediaType;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.util.AssertionErrors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.Optional;

import ru.semyonmi.extjsdemo.config.properties.CoreProperties;
import ru.semyonmi.extjsdemo.domain.Access;
import ru.semyonmi.extjsdemo.domain.Role;
import ru.semyonmi.extjsdemo.domain.User;
import ru.semyonmi.extjsdemo.dto.CompanyDto;
import ru.semyonmi.extjsdemo.dto.CountryDto;
import ru.semyonmi.extjsdemo.dto.RoleDto;
import ru.semyonmi.extjsdemo.dto.UserDto;
import ru.semyonmi.extjsdemo.repository.RoleRepository;
import ru.semyonmi.extjsdemo.repository.UserRepository;
import ru.semyonmi.extjsdemo.service.UserService;
import ru.semyonmi.extjsdemo.util.CacheUtils;

import static org.hamcrest.core.Is.is;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class IntegrationTest extends AbstractTestNGSpringContextTests {

    private static final String READER_LOGIN = "TEST_READER_USER";
    private static final String WRITER_LOGIN = "TEST_WRITER_USER";
    private static final String ADMIN_LOGIN = "TEST_ADMIN";
    private static final String ACCESS_TOKEN = "accessToken";

    @Autowired
    private CoreProperties coreProperties;
    @Autowired
    private WebApplicationContext wac;
    @Autowired
    private FilterChainProxy springSecurityFilterChain;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private ObjectMapper objectMapper;

    private SimpleCacheManager cacheManager;
    private JacksonJsonParser jsonParser;
    private MockMvc mockMvc;
    private RoleDto roleDto;

    @BeforeClass
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac)
                .addFilter(springSecurityFilterChain).build();

        jsonParser = new JacksonJsonParser();
        cacheManager = new SimpleCacheManager();
        cacheManager.setCaches(Arrays.asList(
                new ConcurrentMapCache(ACCESS_TOKEN)));
        cacheManager.afterPropertiesSet();

        Role adminRole = roleRepository.findByIdent("ADMIN");
        AssertionErrors.assertTrue("Role \"ADMIN\" not found", adminRole != null);
        User admin = new User(ADMIN_LOGIN, ADMIN_LOGIN, ADMIN_LOGIN, ADMIN_LOGIN,
                              false, adminRole , new Access(3));
        User savedUser = userService.save(Optional.of(admin));
        AssertionErrors.assertTrue("User should not be null", savedUser != null);
        AssertionErrors.assertTrue("User Id should not be null", savedUser.getId() != null);

        Role userRole = roleRepository.findByIdent("USER");
        AssertionErrors.assertTrue("Role \"USER\" not found", userRole != null);
        roleDto = modelMapper.map(userRole, RoleDto.class);
    }

    @Test
    public void testACrateReaderUser_valid() throws Exception {
        postUser(ADMIN_LOGIN, READER_LOGIN, 1);
    }

    @Test
    public void testBCrateWriterUser_valid() throws Exception {
        postUser(ADMIN_LOGIN, WRITER_LOGIN, 3);
    }

    @Test
    public void testCCrateUser_invalid() throws Exception {
        String accessToken = obtainAccessToken(WRITER_LOGIN);

        UserDto user = new UserDto(null, "Forbidden", "Forbidden", "Forbidden", "Forbidden",
                                   false, roleDto, 3);
        mockMvc.perform(post("/api/v1/user")
                                .header("Authorization", "Bearer " + accessToken)
                                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                                .content(objectMapper.writeValueAsString(user))
                                .accept(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(status().isForbidden());
    }

    @Test
    public void testDCountry_get() throws Exception {
        String writerAccessToken = obtainAccessToken(WRITER_LOGIN);

        mockMvc.perform(get("/api/v1/country")
                                .header("Authorization", "Bearer " + writerAccessToken))
                .andExpect(status().isOk());
    }

    @Test
    public void testECompany_get() throws Exception {
        String writerAccessToken = obtainAccessToken(WRITER_LOGIN);

        mockMvc.perform(get("/api/v1/company")
                                .header("Authorization", "Bearer " + writerAccessToken))
                .andExpect(status().isOk());
    }

    private CountryDto countryDto;
    private CompanyDto companyDto;

    @Test
    public void testFCountry_created() throws Exception {
        String writerAccessToken = obtainAccessToken(WRITER_LOGIN);

        countryDto = new CountryDto(null, "New_Zealand");

        MvcResult result = mockMvc.perform(post("/api/v1/country")
                                                   .header("Authorization", "Bearer " + writerAccessToken)
                                                   .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                                                   .content(objectMapper.writeValueAsString(countryDto))
                                                   .accept(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.name", is(countryDto.getName())))
                .andReturn();
        countryDto = objectMapper.readValue(result.getResponse().getContentAsByteArray(), CountryDto.class);
    }

    @Test
    public void testGCompany_created() throws Exception {
        String writerAccessToken = obtainAccessToken(WRITER_LOGIN);

        companyDto = new CompanyDto(null, "Uber", 6700, countryDto.getId(), null);

        MvcResult result = mockMvc.perform(post("/api/v1/company")
                                                   .header("Authorization", "Bearer " + writerAccessToken)
                                                   .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                                                   .content(objectMapper.writeValueAsString(companyDto))
                                                   .accept(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.name", is(companyDto.getName())))
                .andReturn();
        companyDto = objectMapper.readValue(result.getResponse().getContentAsByteArray(), CompanyDto.class);
    }

    @Test
    public void testHCountry_forbidden() throws Exception {
        String readerAccessToken = obtainAccessToken(READER_LOGIN);

        countryDto.setName("Forbidden");

        mockMvc.perform(post("/api/v1/country")
                                .header("Authorization", "Bearer " + readerAccessToken)
                                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                                .content(objectMapper.writeValueAsString(countryDto))
                                .accept(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(status().isForbidden());
    }

    @Test
    public void testICompany_delete() throws Exception {
        String writerAccessToken = obtainAccessToken(WRITER_LOGIN);

        mockMvc.perform(delete("/api/v1/company/{id}", companyDto.getId())
                                .header("Authorization", "Bearer " + writerAccessToken))
                .andExpect(status().isOk());
    }

    @Test
    public void testJCountry_delete() throws Exception {
        String writerAccessToken = obtainAccessToken(WRITER_LOGIN);

        mockMvc.perform(delete("/api/v1/country/{id}", countryDto.getId())
                                .header("Authorization", "Bearer " + writerAccessToken))
                .andExpect(status().isOk());
    }

    @AfterClass
    public void clean() throws Exception {
        CacheUtils.clearAll(cacheManager);
        User user = userRepository.findByLogin(READER_LOGIN);
        userRepository.delete(user);
        user = userRepository.findByLogin(WRITER_LOGIN);
        userRepository.delete(user);
        user = userRepository.findByLogin(ADMIN_LOGIN);
        userRepository.delete(user);
    }

    private void postUser(String tokenLogin, String userLogin, int mask) throws Exception {
        String accessToken = obtainAccessToken(tokenLogin);

        UserDto user = new UserDto(null, userLogin, userLogin, userLogin, userLogin,
                                   false, roleDto, mask);
        mockMvc.perform(post("/api/v1/user")
                                .header("Authorization", "Bearer " + accessToken)
                                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                                .content(objectMapper.writeValueAsString(user))
                                .accept(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(status().isCreated());
    }

    private String obtainAccessToken(String login) throws Exception  {
        return CacheUtils.computeIfAbsent(cacheManager, ACCESS_TOKEN, login, String.class,
                                          ()->fetchAccessToken(login));
    }

    private String fetchAccessToken(String login) {
        CoreProperties.OAuthClient oAuthClient = coreProperties.getOauthClient();
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "password");
        params.add("client_id", oAuthClient.getClientId());
        params.add("username", login);
        params.add("password", login);

        try {
            ResultActions result
                    = mockMvc.perform(post("/oauth/token")
                                              .params(params)
                                              .with(httpBasic(oAuthClient.getClientId(), oAuthClient.getClientSecret()))
                                              .accept(MediaType.APPLICATION_JSON_UTF8_VALUE))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE));

            String resultString = result.andReturn().getResponse().getContentAsString();

            return jsonParser.parseMap(resultString).get("access_token").toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
