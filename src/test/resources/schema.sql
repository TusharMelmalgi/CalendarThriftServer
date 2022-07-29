CREATE TABLE meeting(
	meet_id int AUTO_INCREMENT,
	description varchar(200),
	agenda varchar(100) NOT NULL,
	owner_id varchar(10) NOT NULL,
	date_of_meeting date NOT NULL,
	start_time time NOT  NULL,
	end_time time NOT NULL,
	is_available boolean NOT NULL DEFAULT TRUE,
	room_id int ,
	created_date timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
	PRIMARY KEY (meet_id),
	INDEX (room_id, date_of_meeting, is_available)
);

CREATE TABLE employee_meeting(
  employee_id varchar(10) ,
  meeting_id int,
  status varchar(10) NOT NULL,
  date date NOT NULL,
  PRIMARY KEY ( employee_id , meeting_id),
  INDEX(date, status)
);


