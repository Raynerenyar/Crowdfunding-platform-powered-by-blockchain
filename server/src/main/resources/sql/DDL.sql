CREATE DATABASE IF NOT EXISTS crowdfunding;

USE crowdfunding;

CREATE TABLE IF NOT EXISTS project_creators (
    username VARCHAR(42) NOT NULL,
    name VARCHAR(20),
    password VARCHAR(120) NOT NULL,
    PRIMARY KEY (username)
);

CREATE TABLE IF NOT EXISTS tokens(
	token_id INT NOT NULL AUTO_INCREMENT,
	token_address VARCHAR(42) NOT NULL,
    token_symbol VARCHAR(10) NOT NULL,
    token_name VARCHAR(15) NOT NULL,
    PRIMARY KEY (token_id,token_address)
);

CREATE TABLE IF NOT EXISTS projects (
    project_address VARCHAR(42) NOT NULL,
    creator_address VARCHAR(42) NOT NULL,
    title TEXT NOT NULL,
    description TEXT NOT NULL,
    imageUrl TEXT NOT NULL,
    goal BIGINT NOT NULL,
    deadline TIMESTAMP NOT NULL,
    -- raised_amount INT, 
    completed BOOLEAN NOT NULL,
    expired BOOLEAN NOT NULL,
    -- num_of_requests	INT NOT NULL,
    accepting_token VARCHAR(42) NOT NULL,
    -- token_name VARCHAR(15) NOT NULL,
    -- token_symbol VARCHAR(10) NOT NULL,
    token_id INT NOT NULL,
    created_date TIMESTAMP NOT NULL,
    PRIMARY KEY (project_address),
    CONSTRAINT fk_username FOREIGN KEY (creator_address) REFERENCES project_creators(username)
    ,CONSTRAINT fk_token FOREIGN KEY (token_id) REFERENCES tokens(token_id)
);

CREATE TABLE IF NOT EXISTS project_requests (
    request_id INT NOT NULL AUTO_INCREMENT,
    request_no INT NOT NULL,
    project_address VARCHAR(42) NOT NULL,
    title TEXT NOT NULL,
    description TEXT NOT NULL,
    recipient_address VARCHAR(42) NOT NULL,
    amount INT NOT NULL,
    completed BOOLEAN NOT NULL,
    PRIMARY KEY (request_id),
    CONSTRAINT fk_requests_project_address FOREIGN KEY (project_address)
        REFERENCES projects (project_address)
);

CREATE TABLE IF NOT EXISTS contributors (
    contributor_address VARCHAR(42) NOT NULL,
    PRIMARY KEY (contributor_address)
);

CREATE TABLE IF NOT EXISTS contributions (
	contribution_id INT NOT NULL AUTO_INCREMENT,
    contributor_address VARCHAR(42) NOT NULL,
    contribution_amount INT NOT NULL,
    project_address VARCHAR(42) NOT NULL,
    refunded BOOLEAN NOT NULL,
    PRIMARY KEY (contribution_id),
    CONSTRAINT fk_contributions_contributor_address FOREIGN KEY (contributor_address) REFERENCES contributors(contributor_address),
    CONSTRAINT fk_contributions_project_address FOREIGN KEY (project_address) REFERENCES projects(project_address)
);

CREATE TABLE IF NOT EXISTS votes (
	vote_id INT NOT NULL AUTO_INCREMENT,
    request_id INT NOT NULL,
    contributor_address VARCHAR(42) NOT NULL,
    value_of_vote DOUBLE NOT NULL,
    PRIMARY KEY (vote_id),
    CONSTRAINT fk_votes_contributor_address FOREIGN KEY (contributor_address) REFERENCES contributors (contributor_address)
);

CREATE TABLE IF NOT EXISTS roles (
	id INT NOT NULL AUTO_INCREMENT,
    name VARCHAR(20),
    PRIMARY KEY(id)
);

CREATE TABLE IF NOT EXISTS user_roles(
	user_address VARCHAR(42) NOT NULL,
	role_id INT NOT NULL,
	PRIMARY KEY(user_address,role_id)
);