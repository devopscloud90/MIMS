-- Oracle XE schema for the Micro-Inventions Management System.
-- Run while connected as the MIMS database user.

CREATE TABLE domains (
  id NUMBER(10) PRIMARY KEY,
  name VARCHAR2(200) NOT NULL,
  description VARCHAR2(4000),
  advisors_count NUMBER(10) DEFAULT 0 NOT NULL,
  experts_count NUMBER(10) DEFAULT 0 NOT NULL
);

CREATE TABLE userdetails (
  id NUMBER(10) PRIMARY KEY,
  name VARCHAR2(200) NOT NULL,
  phonenumber VARCHAR2(30),
  mail_id VARCHAR2(254),
  address VARCHAR2(4000)
);

CREATE TABLE logindetails (
  id NUMBER(10) PRIMARY KEY,
  username VARCHAR2(200) NOT NULL UNIQUE,
  password VARCHAR2(4000) NOT NULL,
  initpassword VARCHAR2(4000),
  keys VARCHAR2(4000),
  usertype VARCHAR2(30) NOT NULL,
  usertypeid NUMBER(10),
  startdate VARCHAR2(20),
  enddate VARCHAR2(20),
  status VARCHAR2(20) NOT NULL
);

CREATE TABLE requests (
  id NUMBER(10) PRIMARY KEY,
  name VARCHAR2(200) NOT NULL,
  phonenumber VARCHAR2(30),
  mail_id VARCHAR2(254),
  address VARCHAR2(4000),
  domainid NUMBER(10),
  reviewdate VARCHAR2(20),
  reviewerid NUMBER(10),
  comments VARCHAR2(4000),
  status VARCHAR2(30),
  userid NUMBER(10)
);

CREATE TABLE inventions (
  id NUMBER(10) PRIMARY KEY,
  domainid NUMBER(10),
  title VARCHAR2(500),
  thesis VARCHAR2(4000),
  finaldoc VARCHAR2(4000),
  registereddate VARCHAR2(20),
  patentdate VARCHAR2(20),
  status VARCHAR2(30),
  userid NUMBER(10)
);

CREATE TABLE reviews (
  id NUMBER(10) PRIMARY KEY,
  inventionid NUMBER(10),
  reviewdate VARCHAR2(20),
  reviewerid NUMBER(10),
  comments VARCHAR2(4000)
);

CREATE TABLE queries (
  id NUMBER(10) PRIMARY KEY,
  inventionid NUMBER(10),
  query VARCHAR2(4000),
  replyid NUMBER(10),
  reply VARCHAR2(4000)
);

-- The legacy application spells this table and its offer column this way.
CREATE TABLE opportuinities (
  id NUMBER(10) PRIMARY KEY,
  domainid NUMBER(10),
  opportuinity VARCHAR2(500),
  description VARCHAR2(4000),
  status VARCHAR2(30)
);

CREATE SEQUENCE mims_users_seq START WITH 1 INCREMENT BY 1 NOCACHE;
CREATE SEQUENCE mims_domains_seq START WITH 1 INCREMENT BY 1 NOCACHE;
CREATE SEQUENCE mims_inventions_seq START WITH 1 INCREMENT BY 1 NOCACHE;
CREATE SEQUENCE mims_requests_seq START WITH 1 INCREMENT BY 1 NOCACHE;
CREATE SEQUENCE mims_reviews_seq START WITH 1 INCREMENT BY 1 NOCACHE;
CREATE SEQUENCE mims_queries_seq START WITH 1 INCREMENT BY 1 NOCACHE;
CREATE SEQUENCE mims_opportunities_seq START WITH 1 INCREMENT BY 1 NOCACHE;
