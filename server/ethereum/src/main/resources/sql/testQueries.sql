INSERT INTO Contributors (
	contributor_address)
VALUES ("0x0");

select * from contributors;
select * from ProjectCreators;
select * from Projects;
select * from ProjectRequests;
select * from contributions;
select * from votes;

DELETE FROM `crowdfunding`.`ProjectRequests` WHERE (`request_id` = '1');
DELETE FROM `crowdfunding`.`contributions` WHERE (`contribution_id` = '1');
DELETE FROM `crowdfunding`.`Projects` WHERE (`project_address` = '0x0');
DELETE FROM `crowdfunding`.`votes` WHERE (`vote_id` = '1');
DELETE FROM `crowdfunding`.`ProjectCreators` WHERE (`creator_address` = '0x0');
DELETE FROM `crowdfunding`.`contributors` WHERE (`contributor_address` = '0x0');



INSERT INTO ProjectCreators (
	creator_address,
	name)
VALUES ("0x0","john");

INSERT INTO Projects (
	project_address,
	creator_address,
	description,
	goal,
	deadline,
	raised_amount,
	completed,
	expired,
	num_of_requests,
	accepting_token)
VALUES ("0x0","0x0","test",100,1000,100,false,false,0,"0x0");

INSERT INTO roles(name) VALUES('ROLE_USER');
INSERT INTO roles(name) VALUES('ROLE_MODERATOR');
INSERT INTO roles(name) VALUES('ROLE_ADMIN');

SELECT *
FROM ProjectCreators
WHERE creator_address = "0x0";