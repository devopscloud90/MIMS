-- Run once against an existing MIMS schema to support Spring Boot-generated IDs.
-- Connect as the MIMS schema owner before running this script.

DECLARE
  next_id NUMBER;
BEGIN
  SELECT NVL(MAX(id), 0) + 1 INTO next_id FROM userdetails;
  EXECUTE IMMEDIATE 'CREATE SEQUENCE mims_users_seq START WITH ' || next_id || ' INCREMENT BY 1 NOCACHE';
END;
/

DECLARE
  next_id NUMBER;
BEGIN
  SELECT NVL(MAX(id), 0) + 1 INTO next_id FROM domains;
  EXECUTE IMMEDIATE 'CREATE SEQUENCE mims_domains_seq START WITH ' || next_id || ' INCREMENT BY 1 NOCACHE';
END;
/

DECLARE
  next_id NUMBER;
BEGIN
  SELECT NVL(MAX(id), 0) + 1 INTO next_id FROM inventions;
  EXECUTE IMMEDIATE 'CREATE SEQUENCE mims_inventions_seq START WITH ' || next_id || ' INCREMENT BY 1 NOCACHE';
END;
/

DECLARE
  next_id NUMBER;
BEGIN
  SELECT NVL(MAX(id), 0) + 1 INTO next_id FROM requests;
  EXECUTE IMMEDIATE 'CREATE SEQUENCE mims_requests_seq START WITH ' || next_id || ' INCREMENT BY 1 NOCACHE';
END;
/

DECLARE
  next_id NUMBER;
BEGIN
  SELECT NVL(MAX(id), 0) + 1 INTO next_id FROM reviews;
  EXECUTE IMMEDIATE 'CREATE SEQUENCE mims_reviews_seq START WITH ' || next_id || ' INCREMENT BY 1 NOCACHE';
END;
/

DECLARE
  next_id NUMBER;
BEGIN
  SELECT NVL(MAX(id), 0) + 1 INTO next_id FROM queries;
  EXECUTE IMMEDIATE 'CREATE SEQUENCE mims_queries_seq START WITH ' || next_id || ' INCREMENT BY 1 NOCACHE';
END;
/

DECLARE
  next_id NUMBER;
BEGIN
  SELECT NVL(MAX(id), 0) + 1 INTO next_id FROM opportuinities;
  EXECUTE IMMEDIATE 'CREATE SEQUENCE mims_opportunities_seq START WITH ' || next_id || ' INCREMENT BY 1 NOCACHE';
END;
/
