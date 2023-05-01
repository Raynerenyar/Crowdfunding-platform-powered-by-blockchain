USE crowdfunding; 

CREATE TABLE ProjectCreators (
    username VARCHAR(42) NOT NULL,
    name VARCHAR(20),
    password VARCHAR(120) not null,
    PRIMARY KEY (username)
);

CREATE TABLE Projects (
    project_address VARCHAR(42) NOT NULL,
    creator_address VARCHAR(42) NOT NULL,
    title TEXT NOT NULL,
    description TEXT NOT NULL,
    goal INT NOT NULL,
    deadline INT NOT NULL,
    raised_amount INT,
    completed BOOLEAN NOT NULL,
    expired BOOLEAN NOT NULL,
    num_of_requests	INT NOT NULL,
    accepting_token VARCHAR(42) NOT NULL,
    PRIMARY KEY (project_address),
    CONSTRAINT fk_username FOREIGN KEY (creator_address) REFERENCES ProjectCreators(username)
);

CREATE TABLE ProjectRequests (
    request_id INT NOT NULL AUTO_INCREMENT,
    project_address VARCHAR(42) NOT NULL,
    title TEXT NOT NULL,
    description TEXT NOT NULL,
    recipient_address VARCHAR(42) NOT NULL,
    amount INT NOT NULL,
    num_of_votes INT,
    completed BOOLEAN NOT NULL,
    value_of_votes DOUBLE,
    PRIMARY KEY (request_id),
    CONSTRAINT fk_requests_project_address FOREIGN KEY (project_address)
        REFERENCES Projects (project_address)
);

CREATE TABLE Contributors (
    contributor_address VARCHAR(42) NOT NULL,
    PRIMARY KEY (contributor_address)
);

CREATE TABLE Contributions (
	contribution_id INT NOT NULL AUTO_INCREMENT,
    contributor_address VARCHAR(42) NOT NULL,
    contribution_amount INT NOT NULL,
    project_address VARCHAR(42) NOT NULL,
    refunded BOOLEAN NOT NULL,
    PRIMARY KEY (contribution_id),
    CONSTRAINT fk_contributions_contributor_address FOREIGN KEY (contributor_address) REFERENCES Contributors(contributor_address),
    CONSTRAINT fk_contributions_project_address FOREIGN KEY (project_address) REFERENCES Projects(project_address)
);

CREATE TABLE Votes (
	vote_id INT NOT NULL AUTO_INCREMENT,
    request_id INT NOT NULL,
    contributor_address VARCHAR(42) NOT NULL,
    value_of_vote DOUBLE NOT NULL,
    PRIMARY KEY (vote_id),
    CONSTRAINT fk_votes_contributor_address FOREIGN KEY (contributor_address) REFERENCES Contributors (contributor_address)
);

create table roles (
	id int not null auto_increment,
    name varchar(20),
    primary key(id)
);

create table userRoles(
	user_address varchar(42) not null,
	role_id int not null,
	primary key(user_address,role_id)
);