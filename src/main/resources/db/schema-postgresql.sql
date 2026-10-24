-- PostgreSQL schema for the Micro-Inventions Management System.
-- Run while connected as the MIMS database user.

CREATE TABLE domains (
  id BIGINT PRIMARY KEY,
  name VARCHAR(200) NOT NULL,
  description VARCHAR(4000),
  advisors_count BIGINT DEFAULT 0 NOT NULL,
  experts_count BIGINT DEFAULT 0 NOT NULL
);

CREATE TABLE userdetails (
  id BIGINT PRIMARY KEY,
  name VARCHAR(200) NOT NULL,
  phonenumber VARCHAR(30),
  mail_id VARCHAR(254),
  address VARCHAR(4000)
);

CREATE TABLE logindetails (
  id BIGINT PRIMARY KEY,
  username VARCHAR(200) NOT NULL UNIQUE,
  password VARCHAR(4000) NOT NULL,
  initpassword VARCHAR(4000),
  keys VARCHAR(4000),
  usertype VARCHAR(30) NOT NULL,
  usertypeid BIGINT,
  startdate VARCHAR(20),
  enddate VARCHAR(20),
  status VARCHAR(20) NOT NULL
);

CREATE TABLE requests (
  id BIGINT PRIMARY KEY,
  name VARCHAR(200) NOT NULL,
  phonenumber VARCHAR(30),
  mail_id VARCHAR(254),
  address VARCHAR(4000),
  domainid BIGINT,
  reviewdate VARCHAR(20),
  reviewerid BIGINT,
  comments VARCHAR(4000),
  status VARCHAR(30),
  userid BIGINT
);

CREATE TABLE inventions (
  id BIGINT PRIMARY KEY,
  domainid BIGINT,
  title VARCHAR(500),
  thesis VARCHAR(4000),
  finaldoc VARCHAR(4000),
  registereddate VARCHAR(20),
  patentdate VARCHAR(20),
  status VARCHAR(30),
  userid BIGINT
);

CREATE TABLE reviews (
  id BIGINT PRIMARY KEY,
  inventionid BIGINT,
  reviewdate VARCHAR(20),
  reviewerid BIGINT,
  comments VARCHAR(4000)
);

CREATE TABLE queries (
  id BIGINT PRIMARY KEY,
  inventionid BIGINT,
  query VARCHAR(4000),
  replyid BIGINT,
  reply VARCHAR(4000)
);

-- The legacy application spells this table and its offer column this way.
CREATE TABLE opportuinities (
  id BIGINT PRIMARY KEY,
  domainid BIGINT,
  opportuinity VARCHAR(500),
  description VARCHAR(4000),
  status VARCHAR(30)
);

CREATE SEQUENCE mims_users_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE mims_domains_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE mims_inventions_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE mims_requests_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE mims_reviews_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE mims_queries_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE mims_opportunities_seq START WITH 1 INCREMENT BY 1;

