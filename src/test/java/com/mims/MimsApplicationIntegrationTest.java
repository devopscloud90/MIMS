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
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:h2:mem:mims;MODE=Oracle;DB_CLOSE_DELAY=-1",
        "spring.datasource.username=sa",
        "spring.datasource.password=",
        "spring.datasource.driver-class-name=org.h2.Driver",
        "spring.sql.init.mode=never"
})
class MimsApplicationIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JdbcTemplate jdbcTemplate;

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
        jdbcTemplate.execute("CREATE TABLE domains (id BIGINT PRIMARY KEY, name VARCHAR(200) NOT NULL, description VARCHAR(4000), advisors_count BIGINT DEFAULT 0 NOT NULL, experts_count BIGINT DEFAULT 0 NOT NULL)");
        jdbcTemplate.execute("CREATE TABLE userdetails (id BIGINT PRIMARY KEY, name VARCHAR(200) NOT NULL, phonenumber VARCHAR(30), mail_id VARCHAR(254), address VARCHAR(4000))");
        jdbcTemplate.execute("CREATE TABLE logindetails (id BIGINT PRIMARY KEY, username VARCHAR(200) NOT NULL UNIQUE, password VARCHAR(4000) NOT NULL, initpassword VARCHAR(4000), keys VARCHAR(4000), usertype VARCHAR(30) NOT NULL, usertypeid BIGINT, startdate VARCHAR(20), enddate VARCHAR(20), status VARCHAR(20) NOT NULL)");
        jdbcTemplate.execute("CREATE TABLE requests (id BIGINT PRIMARY KEY, name VARCHAR(200) NOT NULL, phonenumber VARCHAR(30), mail_id VARCHAR(254), address VARCHAR(4000), domainid BIGINT, reviewdate VARCHAR(20), reviewerid BIGINT, comments VARCHAR(4000), status VARCHAR(30), userid BIGINT)");
        jdbcTemplate.execute("CREATE TABLE inventions (id BIGINT PRIMARY KEY, domainid BIGINT, title VARCHAR(500), thesis VARCHAR(4000), finaldoc VARCHAR(4000), registereddate VARCHAR(20), patentdate VARCHAR(20), status VARCHAR(30), userid BIGINT)");
        jdbcTemplate.execute("CREATE TABLE reviews (id BIGINT PRIMARY KEY, inventionid BIGINT, reviewdate VARCHAR(20), reviewerid BIGINT, comments VARCHAR(4000))");
        jdbcTemplate.execute("CREATE TABLE queries (id BIGINT PRIMARY KEY, inventionid BIGINT, query VARCHAR(4000), replyid BIGINT, reply VARCHAR(4000))");
        jdbcTemplate.execute("CREATE TABLE opportuinities (id BIGINT PRIMARY KEY, domainid BIGINT, opportuinity VARCHAR(500), description VARCHAR(4000), status VARCHAR(30))");
        jdbcTemplate.execute("CREATE SEQUENCE mims_domains_seq START WITH 1");
        jdbcTemplate.execute("CREATE SEQUENCE mims_inventions_seq START WITH 1");
        jdbcTemplate.execute("CREATE SEQUENCE mims_requests_seq START WITH 1");
        jdbcTemplate.execute("CREATE SEQUENCE mims_reviews_seq START WITH 1");
        jdbcTemplate.execute("CREATE SEQUENCE mims_queries_seq START WITH 1");
        jdbcTemplate.execute("CREATE SEQUENCE mims_opportunities_seq START WITH 1");
        jdbcTemplate.execute("CREATE SEQUENCE mims_users_seq START WITH 2");
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
