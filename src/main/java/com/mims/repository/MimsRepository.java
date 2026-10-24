package com.mims.repository;

import com.mims.model.Domain;
import com.mims.model.Invention;
import com.mims.model.InventionQuery;
import com.mims.model.Opportunity;
import com.mims.model.Request;
import com.mims.model.Review;
import com.mims.model.UserAccount;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class MimsRepository {

    private final JdbcTemplate jdbcTemplate;

    public MimsRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Domain> findDomains() {
        return jdbcTemplate.query("""
                SELECT id, name, description, advisors_count, experts_count
                FROM domains ORDER BY id
                """, this::mapDomain);
    }

    public Optional<Domain> findDomain(Long id) {
        return queryOne("""
                SELECT id, name, description, advisors_count, experts_count
                FROM domains WHERE id = ?
                """, this::mapDomain, id);
    }

    public Domain insertDomain(Domain domain) {
        jdbcTemplate.update("""
                INSERT INTO domains (id, name, description, advisors_count, experts_count)
                VALUES (?, ?, ?, ?, ?)
                """, domain.id(), domain.name(), domain.description(),
                valueOrZero(domain.advisorsCount()), valueOrZero(domain.expertsCount()));
        return domain;
    }

    public int updateDomain(Domain domain) {
        return jdbcTemplate.update("""
                UPDATE domains SET name = ?, description = ?, advisors_count = ?, experts_count = ?
                WHERE id = ?
                """, domain.name(), domain.description(), valueOrZero(domain.advisorsCount()),
                valueOrZero(domain.expertsCount()), domain.id());
    }

    public int deleteDomain(Long id) {
        return jdbcTemplate.update("DELETE FROM domains WHERE id = ?", id);
    }

    public List<UserAccount> findUsers() {
        return findUsers(null, null);
    }

    public List<UserAccount> findUsers(String userType, String status) {
        StringBuilder sql = new StringBuilder("""
                SELECT u.id, u.name, u.phonenumber, u.mail_id, u.address, l.username,
                       l.usertype, l.usertypeid, l.startdate, l.enddate, l.status
                FROM userdetails u JOIN logindetails l ON l.id = u.id
                WHERE 1 = 1
                """);
        List<Object> parameters = new ArrayList<>();
        appendFilter(sql, parameters, "l.usertype", userType);
        appendFilter(sql, parameters, "l.status", status);
        sql.append(" ORDER BY u.id");
        return jdbcTemplate.query(sql.toString(), this::mapUser, parameters.toArray());
    }

    public Optional<UserAccount> findUser(Long id) {
        return queryOne("""
                SELECT u.id, u.name, u.phonenumber, u.mail_id, u.address, l.username,
                       l.usertype, l.usertypeid, l.startdate, l.enddate, l.status
                FROM userdetails u JOIN logindetails l ON l.id = u.id
                WHERE u.id = ?
                """, this::mapUser, id);
    }

    public Long insertUserDetails(String name, String mobile, String email, String address) {
        Long id = nextId("MIMS_USERS_SEQ");
        jdbcTemplate.update("""
                INSERT INTO userdetails (id, name, phonenumber, mail_id, address)
                VALUES (?, ?, ?, ?, ?)
                """, id, name, mobile, email, address);
        return id;
    }

    public void insertLoginDetails(Long id, String username, String encodedPassword,
                                   String userType, Long userTypeId, String startDate, String endDate) {
        jdbcTemplate.update("""
                INSERT INTO logindetails
                    (id, username, password, initpassword, keys, usertype, usertypeid,
                     startdate, enddate, status)
                VALUES (?, ?, ?, NULL, NULL, ?, ?, ?, ?, 'Active')
                """, id, username, encodedPassword, userType, userTypeId, startDate, endDate);
    }

    public int updateUserDetails(Long id, String name, String mobile, String email, String address) {
        return jdbcTemplate.update("""
                UPDATE userdetails SET name = ?, phonenumber = ?, mail_id = ?, address = ?
                WHERE id = ?
                """, name, mobile, email, address, id);
    }

    public int updateUserTypeId(Long id, Long userTypeId) {
        return jdbcTemplate.update("UPDATE logindetails SET usertypeid = ? WHERE id = ?", userTypeId, id);
    }

    public int updateUsername(Long id, String username) {
        return jdbcTemplate.update("UPDATE logindetails SET username = ? WHERE id = ?", username, id);
    }

    public int updateUserStatus(Long id, String status) {
        return jdbcTemplate.update("UPDATE logindetails SET status = ? WHERE id = ?", status, id);
    }

    public int updatePassword(Long id, String encodedPassword) {
        return jdbcTemplate.update("UPDATE logindetails SET password = ? WHERE id = ?",
                encodedPassword, id);
    }

    public int adjustDomainCount(Long domainId, String countColumn, int adjustment) {
        if (!List.of("advisors_count", "experts_count").contains(countColumn)
                || (adjustment != -1 && adjustment != 1)) {
            throw new IllegalArgumentException("Invalid domain count adjustment");
        }
        return jdbcTemplate.update("UPDATE domains SET " + countColumn
                + " = " + countColumn + " + ? WHERE id = ? AND " + countColumn + " + ? >= 0",
                adjustment, domainId, adjustment);
    }

    public List<Invention> findInventions() {
        return findInventions(null, null);
    }

    public List<Invention> findInventionsByDomain(Long domainId) {
        return findInventions(domainId, null);
    }

    public List<Invention> findInventionsByUser(Long userId) {
        return findInventions(null, userId);
    }

    public List<Invention> findInventions(Long domainId, Long userId) {
        StringBuilder sql = new StringBuilder("""
                SELECT id, domainid, title, thesis, finaldoc, registereddate, patentdate, status, userid
                FROM inventions WHERE 1 = 1
                """);
        List<Object> parameters = new ArrayList<>();
        appendFilter(sql, parameters, "domainid", domainId);
        appendFilter(sql, parameters, "userid", userId);
        sql.append(" ORDER BY id");
        return jdbcTemplate.query(sql.toString(), this::mapInvention, parameters.toArray());
    }

    public Optional<Invention> findInvention(Long id) {
        return queryOne("""
                SELECT id, domainid, title, thesis, finaldoc, registereddate, patentdate, status, userid
                FROM inventions WHERE id = ?
                """, this::mapInvention, id);
    }

    public Invention insertInvention(Invention invention) {
        jdbcTemplate.update("""
                INSERT INTO inventions
                    (id, domainid, title, thesis, finaldoc, registereddate, patentdate, status, userid)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
                """, invention.id(), invention.domainId(), invention.title(), invention.thesis(),
                invention.finalDocument(), invention.registeredDate(), invention.patentDate(),
                invention.status(), invention.userId());
        return invention;
    }

    public int updateInvention(Invention invention) {
        return jdbcTemplate.update("""
                UPDATE inventions SET domainid = ?, title = ?, thesis = ?, finaldoc = ?,
                    registereddate = ?, patentdate = ?, status = ?, userid = ?
                WHERE id = ?
                """, invention.domainId(), invention.title(), invention.thesis(),
                invention.finalDocument(), invention.registeredDate(), invention.patentDate(),
                invention.status(), invention.userId(), invention.id());
    }

    public int deleteInvention(Long id) {
        return jdbcTemplate.update("DELETE FROM inventions WHERE id = ?", id);
    }

    public List<Request> findRequests() {
        return findRequests(null, null);
    }

    public List<Request> findRequestsByStatus(String status) {
        return findRequests(status, null);
    }

    public List<Request> findRequests(String status, Long reviewerId) {
        StringBuilder sql = new StringBuilder("""
                SELECT id, name, phonenumber, mail_id, address, domainid, reviewdate,
                       reviewerid, comments, status, userid
                FROM requests WHERE 1 = 1
                """);
        List<Object> parameters = new ArrayList<>();
        appendFilter(sql, parameters, "status", status);
        appendFilter(sql, parameters, "reviewerid", reviewerId);
        sql.append(" ORDER BY id");
        return jdbcTemplate.query(sql.toString(), this::mapRequest, parameters.toArray());
    }

    public Optional<Request> findRequest(Long id) {
        return queryOne("""
                SELECT id, name, phonenumber, mail_id, address, domainid, reviewdate,
                       reviewerid, comments, status, userid
                FROM requests WHERE id = ?
                """, this::mapRequest, id);
    }

    public Request insertRequest(Request request) {
        jdbcTemplate.update("""
                INSERT INTO requests
                    (id, name, phonenumber, mail_id, address, domainid, reviewdate,
                     reviewerid, comments, status, userid)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                """, request.id(), request.name(), request.phoneNumber(), request.email(),
                request.address(), request.domainId(), request.reviewDate(), request.reviewerId(),
                request.comments(), request.status(), request.userId());
        return request;
    }

    public int updateRequest(Request request) {
        return jdbcTemplate.update("""
                UPDATE requests SET name = ?, phonenumber = ?, mail_id = ?, address = ?,
                    domainid = ?, reviewdate = ?, reviewerid = ?, comments = ?, status = ?, userid = ?
                WHERE id = ?
                """, request.name(), request.phoneNumber(), request.email(), request.address(),
                request.domainId(), request.reviewDate(), request.reviewerId(), request.comments(),
                request.status(), request.userId(), request.id());
    }

    public int deleteRequest(Long id) {
        return jdbcTemplate.update("DELETE FROM requests WHERE id = ?", id);
    }

    public List<Review> findReviews() {
        return findReviews(null, null);
    }

    public List<Review> findReviewsByReviewer(Long reviewerId) {
        return findReviews(null, reviewerId);
    }

    public List<Review> findReviews(Long inventionId, Long reviewerId) {
        StringBuilder sql = new StringBuilder("""
                SELECT id, inventionid, reviewdate, reviewerid, comments
                FROM reviews WHERE 1 = 1
                """);
        List<Object> parameters = new ArrayList<>();
        appendFilter(sql, parameters, "inventionid", inventionId);
        appendFilter(sql, parameters, "reviewerid", reviewerId);
        sql.append(" ORDER BY id");
        return jdbcTemplate.query(sql.toString(), this::mapReview, parameters.toArray());
    }

    public Review insertReview(Review review) {
        jdbcTemplate.update("""
                INSERT INTO reviews (id, inventionid, reviewdate, reviewerid, comments)
                VALUES (?, ?, ?, ?, ?)
                """, review.id(), review.inventionId(), review.reviewDate(),
                review.reviewerId(), review.comments());
        return review;
    }

    public Optional<Review> findReview(Long id) {
        return queryOne("""
                SELECT id, inventionid, reviewdate, reviewerid, comments
                FROM reviews WHERE id = ?
                """, this::mapReview, id);
    }

    public int updateReview(Review review) {
        return jdbcTemplate.update("""
                UPDATE reviews SET inventionid = ?, reviewdate = ?, reviewerid = ?, comments = ?
                WHERE id = ?
                """, review.inventionId(), review.reviewDate(), review.reviewerId(),
                review.comments(), review.id());
    }

    public int deleteReview(Long id) {
        return jdbcTemplate.update("DELETE FROM reviews WHERE id = ?", id);
    }

    public List<InventionQuery> findQueries() {
        return findQueries(null, null);
    }

    public List<InventionQuery> findQueriesByInvention(Long inventionId) {
        return findQueries(inventionId, null);
    }

    public List<InventionQuery> findQueries(Long inventionId, Long replyId) {
        StringBuilder sql = new StringBuilder("""
                SELECT id, inventionid, query, replyid, reply
                FROM queries WHERE 1 = 1
                """);
        List<Object> parameters = new ArrayList<>();
        appendFilter(sql, parameters, "inventionid", inventionId);
        appendFilter(sql, parameters, "replyid", replyId);
        sql.append(" ORDER BY id");
        return jdbcTemplate.query(sql.toString(), this::mapQuery, parameters.toArray());
    }

    public InventionQuery insertQuery(InventionQuery query) {
        jdbcTemplate.update("""
                INSERT INTO queries (id, inventionid, query, replyid, reply)
                VALUES (?, ?, ?, ?, ?)
                """, query.id(), query.inventionId(), query.query(), query.replyId(), query.reply());
        return query;
    }

    public Optional<InventionQuery> findQuery(Long id) {
        return queryOne("""
                SELECT id, inventionid, query, replyid, reply
                FROM queries WHERE id = ?
                """, this::mapQuery, id);
    }

    public int updateQuery(InventionQuery query) {
        return jdbcTemplate.update("""
                UPDATE queries SET inventionid = ?, query = ?, replyid = ?, reply = ?
                WHERE id = ?
                """, query.inventionId(), query.query(), query.replyId(), query.reply(), query.id());
    }

    public int deleteQuery(Long id) {
        return jdbcTemplate.update("DELETE FROM queries WHERE id = ?", id);
    }

    public List<Opportunity> findOpportunities() {
        return findOpportunities(null, null);
    }

    public List<Opportunity> findOpportunitiesByDomain(Long domainId) {
        return findOpportunities(domainId, null);
    }

    public List<Opportunity> findOpportunities(Long domainId, String status) {
        StringBuilder sql = new StringBuilder("""
                SELECT id, domainid, opportuinity, description, status
                FROM opportuinities WHERE 1 = 1
                """);
        List<Object> parameters = new ArrayList<>();
        appendFilter(sql, parameters, "domainid", domainId);
        appendFilter(sql, parameters, "status", status);
        sql.append(" ORDER BY id");
        return jdbcTemplate.query(sql.toString(), this::mapOpportunity, parameters.toArray());
    }

    public Opportunity insertOpportunity(Opportunity opportunity) {
        jdbcTemplate.update("""
                INSERT INTO opportuinities (id, domainid, opportuinity, description, status)
                VALUES (?, ?, ?, ?, ?)
                """, opportunity.id(), opportunity.domainId(), opportunity.offer(),
                opportunity.description(), opportunity.status());
        return opportunity;
    }

    public Optional<Opportunity> findOpportunity(Long id) {
        return queryOne("""
                SELECT id, domainid, opportuinity, description, status
                FROM opportuinities WHERE id = ?
                """, this::mapOpportunity, id);
    }

    public int updateOpportunity(Opportunity opportunity) {
        return jdbcTemplate.update("""
                UPDATE opportuinities SET domainid = ?, opportuinity = ?, description = ?, status = ?
                WHERE id = ?
                """, opportunity.domainId(), opportunity.offer(), opportunity.description(),
                opportunity.status(), opportunity.id());
    }

    public int deleteOpportunity(Long id) {
        return jdbcTemplate.update("DELETE FROM opportuinities WHERE id = ?", id);
    }

    public Long nextId(String sequenceName) {
        if (!List.of("MIMS_USERS_SEQ", "MIMS_DOMAINS_SEQ", "MIMS_INVENTIONS_SEQ", "MIMS_REQUESTS_SEQ",
                "MIMS_REVIEWS_SEQ", "MIMS_QUERIES_SEQ", "MIMS_OPPORTUNITIES_SEQ")
                .contains(sequenceName)) {
            throw new IllegalArgumentException("Unknown identifier sequence");
        }
        return jdbcTemplate.queryForObject("SELECT " + sequenceName + ".NEXTVAL FROM dual", Long.class);
    }

    private <T> Optional<T> queryOne(String sql, org.springframework.jdbc.core.RowMapper<T> mapper,
                                     Object... parameters) {
        List<T> rows = jdbcTemplate.query(sql, mapper, parameters);
        return rows.stream().findFirst();
    }

    private void appendFilter(StringBuilder sql, List<Object> parameters, String column, Object value) {
        if (value != null && (!(value instanceof String string) || !string.isBlank())) {
            sql.append(" AND ").append(column).append(" = ?");
            parameters.add(value);
        }
    }

    private Domain mapDomain(ResultSet rs, int row) throws SQLException {
        return new Domain(rs.getLong("id"), rs.getString("name"), rs.getString("description"),
                nullableLong(rs, "advisors_count"), nullableLong(rs, "experts_count"));
    }

    private UserAccount mapUser(ResultSet rs, int row) throws SQLException {
        return new UserAccount(rs.getLong("id"), rs.getString("name"), rs.getString("phonenumber"),
                rs.getString("mail_id"), rs.getString("address"), rs.getString("username"),
                rs.getString("usertype"), nullableLong(rs, "usertypeid"),
                rs.getString("startdate"), rs.getString("enddate"), rs.getString("status"));
    }

    private Invention mapInvention(ResultSet rs, int row) throws SQLException {
        return new Invention(rs.getLong("id"), nullableLong(rs, "domainid"), rs.getString("title"),
                rs.getString("thesis"), rs.getString("finaldoc"), rs.getString("registereddate"),
                rs.getString("patentdate"), rs.getString("status"), nullableLong(rs, "userid"));
    }

    private Request mapRequest(ResultSet rs, int row) throws SQLException {
        return new Request(rs.getLong("id"), rs.getString("name"), rs.getString("phonenumber"),
                rs.getString("mail_id"), rs.getString("address"), nullableLong(rs, "domainid"),
                rs.getString("reviewdate"), nullableLong(rs, "reviewerid"),
                rs.getString("comments"), rs.getString("status"), nullableLong(rs, "userid"));
    }

    private Review mapReview(ResultSet rs, int row) throws SQLException {
        return new Review(rs.getLong("id"), nullableLong(rs, "inventionid"),
                rs.getString("reviewdate"), nullableLong(rs, "reviewerid"),
                rs.getString("comments"));
    }

    private InventionQuery mapQuery(ResultSet rs, int row) throws SQLException {
        return new InventionQuery(rs.getLong("id"), nullableLong(rs, "inventionid"),
                rs.getString("query"), nullableLong(rs, "replyid"), rs.getString("reply"));
    }

    private Opportunity mapOpportunity(ResultSet rs, int row) throws SQLException {
        return new Opportunity(rs.getLong("id"), nullableLong(rs, "domainid"),
                rs.getString("opportuinity"), rs.getString("description"), rs.getString("status"));
    }

    private Long nullableLong(ResultSet rs, String column) throws SQLException {
        long value = rs.getLong(column);
        return rs.wasNull() ? null : value;
    }

    private long valueOrZero(Long value) {
        return value == null ? 0 : value;
    }
}
