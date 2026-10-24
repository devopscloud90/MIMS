package com.mims;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
class MimsApplicationIntegrationTest {

    @Container
    static final PostgreSQLContainer<?> database = new PostgreSQLContainer<>("postgres:17-alpine");

    @DynamicPropertySource
    static void databaseProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", database::getJdbcUrl);
        registry.add("spring.datasource.username", database::getUsername);
        registry.add("spring.datasource.password", database::getPassword);
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private com.mims.repository.MimsRepository repository;

    @BeforeEach
    void seedAccount() {
        jdbcTemplate.execute("DROP TABLE IF EXISTS logindetails");
        jdbcTemplate.execute("DROP TABLE IF EXISTS userdetails");
        jdbcTemplate.execute("DROP TABLE IF EXISTS domains");
        jdbcTemplate.execute("DROP TABLE IF EXISTS requests");
        jdbcTemplate.execute("DROP TABLE IF EXISTS inventions");
        jdbcTemplate.execute("DROP TABLE IF EXISTS reviews");
        jdbcTemplate.execute("DROP TABLE IF EXISTS queries");
        jdbcTemplate.execute("DROP TABLE IF EXISTS opportuinities");
        jdbcTemplate.execute("DROP SEQUENCE IF EXISTS mims_domains_seq");
        jdbcTemplate.execute("DROP SEQUENCE IF EXISTS mims_inventions_seq");
        jdbcTemplate.execute("DROP SEQUENCE IF EXISTS mims_requests_seq");
        jdbcTemplate.execute("DROP SEQUENCE IF EXISTS mims_reviews_seq");
        jdbcTemplate.execute("DROP SEQUENCE IF EXISTS mims_queries_seq");
        jdbcTemplate.execute("DROP SEQUENCE IF EXISTS mims_opportunities_seq");
        jdbcTemplate.execute("DROP SEQUENCE IF EXISTS mims_users_seq");
        new ResourceDatabasePopulator(new ClassPathResource("db/schema-postgresql.sql"))
                .execute(jdbcTemplate.getDataSource());
        jdbcTemplate.execute("ALTER SEQUENCE mims_users_seq RESTART WITH 2");
        jdbcTemplate.execute("ALTER SEQUENCE mims_domains_seq RESTART WITH 2");
        jdbcTemplate.update("""
                INSERT INTO domains (id, name, description, advisors_count, experts_count)
                VALUES (1, 'Engineering', 'Engineering domain', 0, 0)
                """);
        jdbcTemplate.update("DELETE FROM logindetails");
        jdbcTemplate.update("DELETE FROM userdetails");
        jdbcTemplate.update("""
                INSERT INTO userdetails (id, name, phonenumber, mail_id, address)
                VALUES (1, 'Test Advisor', '555-0100', 'advisor@example.test', 'Test address')
                """);
        jdbcTemplate.update("""
                INSERT INTO logindetails
                    (id, username, password, usertype, status)
                VALUES (1, 'test-advisor', 'legacy-password', 'Advisor', 'Active')
                """);
    }

    @Test
    void postgresSequencesAllocateIdsAndRejectUnknownNames() {
        for (String sequence : java.util.List.of("MIMS_USERS_SEQ", "MIMS_DOMAINS_SEQ",
                "MIMS_INVENTIONS_SEQ", "MIMS_REQUESTS_SEQ", "MIMS_REVIEWS_SEQ",
                "MIMS_QUERIES_SEQ", "MIMS_OPPORTUNITIES_SEQ")) {
            long first = repository.nextId(sequence);
            org.junit.jupiter.api.Assertions.assertEquals(first + 1, repository.nextId(sequence));
        }
        org.junit.jupiter.api.Assertions.assertThrows(IllegalArgumentException.class,
                () -> repository.nextId("unknown_sequence"));
    }

    @Test
    void domainRoundTripPreservesEmptyStringsAndNullableFields() {
        long id = repository.nextId("MIMS_DOMAINS_SEQ");
        repository.insertDomain(new com.mims.model.Domain(id, "PostgreSQL", "", null, null));
        org.junit.jupiter.api.Assertions.assertEquals("", repository.findDomain(id).orElseThrow().description());
        repository.updateDomain(new com.mims.model.Domain(id, "Updated", null, 1L, 2L));
        org.junit.jupiter.api.Assertions.assertNull(repository.findDomain(id).orElseThrow().description());
        org.junit.jupiter.api.Assertions.assertEquals(1, repository.deleteDomain(id));
        org.junit.jupiter.api.Assertions.assertTrue(repository.findDomain(id).isEmpty());
    }

    @Test
    void loginPageLoadsAndLegacyLoginUpgradesStoredPassword() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Sign in to your account")));

        MvcResult login = mockMvc.perform(post("/login")
                        .with(csrf())
                        .param("userId", "test-advisor")
                        .param("password", "legacy-password"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/home"))
                .andReturn();

        String storedPassword = jdbcTemplate.queryForObject(
                "SELECT password FROM logindetails WHERE id = 1", String.class);
        org.junit.jupiter.api.Assertions.assertTrue(
                new BCryptPasswordEncoder().matches("legacy-password", storedPassword));

        mockMvc.perform(get("/home").session((org.springframework.mock.web.MockHttpSession)
                        login.getRequest().getSession(false)))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("test-advisor")));
    }

    @Test
    void businessApiRequiresAuthentication() throws Exception {
        mockMvc.perform(get("/api/domains"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/login"));
    }

    @Test
    void authenticatedAdvisorCanReadInventionsAndPublicVisitorsCanSubmitRequests() throws Exception {
        MvcResult login = mockMvc.perform(post("/login")
                        .with(csrf())
                        .param("userId", "test-advisor")
                        .param("password", "legacy-password"))
                .andExpect(status().is3xxRedirection())
                .andReturn();

        mockMvc.perform(get("/api/inventions").session((org.springframework.mock.web.MockHttpSession)
                        login.getRequest().getSession(false)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());

        mockMvc.perform(get("/api/profile").session((org.springframework.mock.web.MockHttpSession)
                        login.getRequest().getSession(false)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));

        mockMvc.perform(get("/request"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Submit an invention request")));

        mockMvc.perform(post("/request")
                        .with(csrf())
                        .param("name", "Public applicant")
                        .param("phoneNumber", "5551234567")
                        .param("email", "applicant@example.test")
                        .param("address", "Test address")
                        .param("domainId", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/request"));

        org.junit.jupiter.api.Assertions.assertEquals(1,
                jdbcTemplate.queryForObject("SELECT COUNT(*) FROM requests", Integer.class));
        org.junit.jupiter.api.Assertions.assertEquals("New",
                jdbcTemplate.queryForObject("SELECT status FROM requests", String.class));
    }

    @Test
    void domainExpertCannotReadAnotherReview() throws Exception {
        jdbcTemplate.update("""
                INSERT INTO userdetails (id, name, phonenumber, mail_id, address)
                VALUES (2, 'Test Expert', '555-0101', 'expert@example.test', 'Test address')
                """);
        jdbcTemplate.update("""
                INSERT INTO logindetails (id, username, password, usertype, usertypeid, status)
                VALUES (2, 'test-expert', 'legacy-password', 'Domain Expert', 1, 'Active')
                """);
        jdbcTemplate.update("""
                INSERT INTO reviews (id, inventionid, reviewdate, reviewerid, comments)
                VALUES (1, 1, '2026-01-01', 3, 'Private review')
                """);

        MvcResult login = mockMvc.perform(post("/login")
                        .with(csrf())
                        .param("userId", "test-expert")
                        .param("password", "legacy-password"))
                .andExpect(status().is3xxRedirection())
                .andReturn();

        mockMvc.perform(get("/api/reviews/1")
                        .session((org.springframework.mock.web.MockHttpSession)
                                login.getRequest().getSession(false)))
                .andExpect(status().isForbidden());
    }
}

