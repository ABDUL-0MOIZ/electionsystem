create database ES;
use ES;
CREATE TABLE users (
    user_id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    cnic VARCHAR(15) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL
);

-- 2. Table: elections
CREATE TABLE elections (
    election_id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    sector VARCHAR(100) NOT NULL,
    election_date DATE NOT NULL,
    election_time TIME NOT NULL
);

-- 3. Table: parties
CREATE TABLE parties (
    party_id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL UNIQUE,
    leader_name VARCHAR(100) NOT NULL
);

-- 4. Table: candidates
CREATE TABLE candidates (
    candidate_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    party_id INT NOT NULL,
    election_id INT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (party_id) REFERENCES parties(party_id) ON DELETE CASCADE,
    FOREIGN KEY (election_id) REFERENCES elections(election_id) ON DELETE CASCADE
);
drop table results;
-- 5. Table: results
CREATE TABLE results (
    result_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    candidate_id int  NOT NULL,
    total_votes INT NOT NULL DEFAULT 0,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (candidate_id) REFERENCES candidates(candidate_id) ON DELETE CASCADE
);
DELIMITER $$

CREATE PROCEDURE add_candidate_by_details (
    IN p_sector VARCHAR(100),
    IN p_party_name VARCHAR(100),
    IN p_cnic VARCHAR(15)
)
BEGIN
    DECLARE v_user_id INT;
    DECLARE v_party_id INT;
    DECLARE v_election_id INT;

    -- Find user ID from CNIC
    SELECT user_id INTO v_user_id
    FROM users
    WHERE cnic = p_cnic;

    -- Find party ID from party name
    SELECT party_id INTO v_party_id
    FROM parties
    WHERE name = p_party_name;

    -- Find election ID from sector
    SELECT election_id INTO v_election_id
    FROM elections
    WHERE sector = p_sector;

    -- Insert into candidates
    INSERT INTO candidates (user_id, party_id, election_id)
    VALUES (v_user_id, v_party_id, v_election_id);
END $$

DELIMITER ;
DELIMITER $$

CREATE PROCEDURE add_user (
    IN p_name VARCHAR(100),
    IN p_cnic VARCHAR(15),
    IN p_password VARCHAR(255)
)
BEGIN
    INSERT INTO users (name, cnic, password)
    VALUES (p_name, p_cnic, p_password);
END $$

DELIMITER ;
DELIMITER $$

CREATE PROCEDURE add_party (
    IN p_name VARCHAR(100),
    IN p_leader_name VARCHAR(100)
)
BEGIN
    INSERT INTO parties (name, leader_name)
    VALUES (p_name, p_leader_name);
END $$

DELIMITER ;
CREATE TABLE candidate_totals (
    candidate_id INT PRIMARY KEY,
    total_votes INT DEFAULT 0,
    FOREIGN KEY (candidate_id) REFERENCES candidates(candidate_id) ON DELETE CASCADE
);
DELIMITER $$

CREATE TRIGGER trg_update_candidate_votes
AFTER INSERT ON results
FOR EACH ROW
BEGIN
    -- If the candidate already has a record, update it
    IF EXISTS (SELECT 1 FROM candidate_totals WHERE candidate_id = NEW.candidate_id) THEN
        UPDATE candidate_totals
        SET total_votes = total_votes + NEW.total_votes
        WHERE candidate_id = NEW.candidate_id;
    ELSE
        -- Else, insert a new record
        INSERT INTO candidate_totals (candidate_id, total_votes)
        VALUES (NEW.candidate_id, NEW.total_votes);
    END IF;
END $$

DELIMITER ;
DELIMITER $$

CREATE TRIGGER trg_after_update_results
AFTER UPDATE ON results
FOR EACH ROW
BEGIN
    IF OLD.candidate_id = NEW.candidate_id THEN
        -- Same candidate, adjust difference in votes
        UPDATE candidate_totals
        SET total_votes = total_votes + (NEW.total_votes - OLD.total_votes)
        WHERE candidate_id = NEW.candidate_id;
    ELSE
        -- Candidate changed: subtract from old, add to new
        UPDATE candidate_totals
        SET total_votes = total_votes - OLD.total_votes
        WHERE candidate_id = OLD.candidate_id;

        IF EXISTS (SELECT 1 FROM candidate_totals WHERE candidate_id = NEW.candidate_id) THEN
            UPDATE candidate_totals
            SET total_votes = total_votes + NEW.total_votes
            WHERE candidate_id = NEW.candidate_id;
        ELSE
            INSERT INTO candidate_totals (candidate_id, total_votes)
            VALUES (NEW.candidate_id, NEW.total_votes);
        END IF;
    END IF;
END $$

DELIMITER ;

DELIMITER $$

CREATE PROCEDURE get_election_results_by_sector (
    IN p_sector VARCHAR(100)
)
BEGIN
    SELECT 
        c.candidate_id,
        u.name AS candidate_name,
        p.name AS party_name,
        e.name AS election_name,
        e.sector,
        ct.total_votes
    FROM 
        candidate_totals ct
    JOIN 
        candidates c ON ct.candidate_id = c.candidate_id
    JOIN 
        users u ON c.user_id = u.user_id
    JOIN 
        parties p ON c.party_id = p.party_id
    JOIN 
        elections e ON c.election_id = e.election_id
    WHERE 
        e.sector = p_sector
    ORDER BY 
        ct.total_votes DESC;
END $$

DELIMITER ;
DELIMITER $$

CREATE PROCEDURE get_all_users()
BEGIN
    SELECT 
        user_id,
        name,
        cnic
    FROM 
        users;
END $$

DELIMITER ;
DELIMITER $$

CREATE PROCEDURE get_all_parties()
BEGIN
    SELECT 
        party_id,
        name,
        leader_name
    FROM 
        parties;
END $$

DELIMITER ;
DELIMITER $$

DELIMITER $$

CREATE PROCEDURE get_all_elections()
BEGIN
    SELECT 
        election_id,
        name,
        sector,
        election_date,
        election_time
    FROM 
        elections;
END $$

DELIMITER ;
DELIMITER $$



DELIMITER ;
DELIMITER $$

CREATE PROCEDURE get_party_candidate_counts()
BEGIN
    SELECT 
        p.name AS party_name,
        COUNT(c.candidate_id) AS total_candidates
    FROM 
        parties p
    LEFT JOIN 
        candidates c ON p.party_id = c.party_id
    GROUP BY 
        p.party_id, p.name
    ORDER BY 
        total_candidates DESC;
END $$

DELIMITER ;
use es;
select * from users;
select * from parties;
select * from elections;
select * from candidates;
call get_all_elections();
call get_all_candidate_details();
DELIMITER $$

CREATE PROCEDURE get_all_candidate_details()
BEGIN
    SELECT 
        u.cnic AS CNIC,
        u.name AS Candidate_Name,
        p.name AS Party_Name,
        e.name AS Election_Name,
        e.sector AS Sector
    FROM 
        candidates c
    JOIN users u ON c.user_id = u.user_id
    JOIN parties p ON c.party_id = p.party_id
    JOIN elections e ON c.election_id = e.election_id;
END $$

DELIMITER ;
DELIMITER $$

CREATE PROCEDURE cast_vote (
    IN p_user_cnic VARCHAR(15),
    IN p_candidate_id INT
)
BEGIN
    DECLARE v_user_id INT;

    -- Get user_id from CNIC
    SELECT user_id INTO v_user_id
    FROM users
    WHERE cnic = p_user_cnic;

    -- Check if the user has already voted
    IF EXISTS (
        SELECT 1 FROM results WHERE user_id = v_user_id
    ) THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'User has already voted.';
    ELSE
        -- Insert vote (1 vote by default)
        INSERT INTO results (user_id, candidate_id, total_votes)
        VALUES (v_user_id, p_candidate_id, 1);
    END IF;
END $$

DELIMITER ;
drop PROCEDURE cast_vote;
DELIMITER $$

CREATE PROCEDURE cast_vote_by_candidate_cnic (
    IN p_user_cnic VARCHAR(15),
    IN p_candidate_cnic VARCHAR(15)
)
BEGIN
    DECLARE v_user_id INT;
    DECLARE v_candidate_id INT;

    -- Get user_id
    SELECT user_id INTO v_user_id
    FROM users
    WHERE cnic = p_user_cnic;

    -- Get candidate_id by candidate's CNIC
    SELECT c.candidate_id INTO v_candidate_id
    FROM candidates c
    JOIN users u ON c.user_id = u.user_id
    WHERE u.cnic = p_candidate_cnic;

    -- Check if already voted
    IF EXISTS (SELECT 1 FROM results WHERE user_id = v_user_id) THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'User has already voted.';
    ELSE
        INSERT INTO results (user_id, candidate_id, total_votes)
        VALUES (v_user_id, v_candidate_id, 1);
    END IF;
END $$

DELIMITER ;
use es;

alter table elections add column endtime text ;
use es;
call cast_vote_by_candidate_cnic ("126","124");
select * from results;
DROP PROCEDURE IF EXISTS add_election;
DELIMITER $$

CREATE PROCEDURE add_election (
    IN p_name VARCHAR(100),
    IN p_sector VARCHAR(100),
    IN p_election_date text,
    IN p_election_time text,
    IN p_lasttime text
)
BEGIN
    INSERT INTO elections (name, sector, election_date, election_time, lasttime)
    VALUES (p_name, p_sector, p_election_date, p_election_time, p_lasttime);
END $$

DELIMITER ;
DROP PROCEDURE IF EXISTS cast_vote_by_candidate_cnic;
DELIMITER $$

CREATE PROCEDURE cast_vote_by_candidate_cnic (
    IN p_user_cnic VARCHAR(15),
    IN p_candidate_cnic VARCHAR(15)
)
BEGIN
    DECLARE v_user_id INT;
    DECLARE v_candidate_id INT;
    DECLARE v_endtime TIME;
    DECLARE v_election_id INT;

    -- Get user_id
    SELECT user_id INTO v_user_id
    FROM users
    WHERE cnic = p_user_cnic;

    -- Get candidate_id and election_id
    SELECT c.candidate_id, c.election_id INTO v_candidate_id, v_election_id
    FROM candidates c
    JOIN users u ON c.user_id = u.user_id
    WHERE u.cnic = p_candidate_cnic;

    -- Get election end time
    SELECT lasttime INTO v_endtime
    FROM elections
    WHERE election_id = v_election_id;

    -- Check if current time is after end time
    IF CURRENT_TIME() > v_endtime THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Voting time is over.';
    END IF;

    -- Check if user has already voted
    IF EXISTS (SELECT 1 FROM results WHERE user_id = v_user_id) THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'User has already voted.';
    ELSE
        -- Cast vote
        INSERT INTO results (user_id, candidate_id, total_votes)
        VALUES (v_user_id, v_candidate_id, 1);
    END IF;
END $$

DELIMITER ;
DELIMITER $$

CREATE PROCEDURE get_results_by_election_name (
    IN p_election_name VARCHAR(100)
)
BEGIN
    SELECT 
        c.candidate_id,
        u.name AS candidate_name,
        p.name AS party_name,
        e.name AS election_name,
        e.sector,
        ct.total_votes
    FROM 
        candidate_totals ct
    JOIN 
        candidates c ON ct.candidate_id = c.candidate_id
    JOIN 
        users u ON c.user_id = u.user_id
    JOIN 
        parties p ON c.party_id = p.party_id
    JOIN 
        elections e ON c.election_id = e.election_id
    WHERE 
        e.name = p_election_name
    ORDER BY 
        ct.total_votes DESC;
END $$

DELIMITER ;
ALTER TABLE electionselectionselectionselection_timeelections ADD COLUMN lasttime TEXT;
CALL get_results_by_election_name('zawar');
DELIMITER $$

CREATE TRIGGER trg_check_lasttime_before_vote
BEFORE INSERT ON restrg_after_update_resultsults
FOR EACH ROW
BEGIN
    DECLARE v_end_time TEXT;
    DECLARE v_current_time TIME;

    -- Get the current time
    SET v_current_time = CURTIME();

    -- Get the election end time (as stored in 'lasttime' TEXT)
    SELECT e.lasttime
    INTO v_end_time
    FROM elections e
    JOIN candidates c ON e.election_id = c.election_id
    WHERE c.candidate_id = NEW.candidate_id;

    -- Compare current time with end time
    IF v_current_time > STR_TO_DATE(v_end_time, '%H:%i:%s') THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Voting is closed. Election end time has passed.';
    END IF;
END $$

DELIMITER ;

DELIMITER $$

CREATE PROCEDURE get_party_winner_counts()
BEGIN
    SELECT 
        p.name AS party_name,
        COUNT(*) AS winning_candidates
    FROM 
        candidates c
    JOIN 
        candidate_totals ct ON c.candidate_id = ct.candidate_id
    JOIN 
        elections e ON c.election_id = e.election_id
    JOIN 
        parties p ON c.party_id = p.party_id
    WHERE 
        c.candidate_id IN (
            SELECT 
                ct2.candidate_id
            FROM 
                candidate_totals ct2
            JOIN 
                candidates c2 ON ct2.candidate_id = c2.candidate_id
            JOIN 
                elections e2 ON c2.election_id = e2.election_id
            WHERE 
                ct2.total_votes = (
                    SELECT MAX(ct3.total_votes)
                    FROM candidates c3
                    JOIN candidate_totals ct3 ON c3.candidate_id = ct3.candidate_id
                    WHERE c3.election_id = e2.election_id
                )
        )
    GROUP BY 
        p.party_id, p.name
    ORDER BY 
        winning_candidates DESC;
END $$

DELIMITER ;
select * from users;
alter table elections drop column endtime;
SELECT * FROM elections;
INSERT INTO elections (name, sector, election_date, election_time, lasttime)
VALUES ('NA-115', 'Lahore', '2025-07-10', '09:00:00', '17:00:00');

INSERT INTO parties (name, leader_name) VALUES

('PPP', 'Bilawal Bhutto'),
('MQM', 'Khalid Maqbool Siddiqui');
INSERT INTO elections (name, sector, election_date, election_time, lastime)
VALUES ('NA-115', 'Lahore', '2025-07-10', '09:00:00', '17:00:00');
INSERT INTO candidates (user_id, party_id, election_id) VALUES
(1, 1, 1),

(2, 2, 1);
SELECT * FROM users;
CALL add_candidate_by_details('Lahore', 'PTI', '35201-1234567-1', 'NA-115');
CALL add_candidate_by_details('Lahore', 'PMLN', '33100-7654321-2', 'NA-115');
CALL add_candidate_by_details('Lahore', 'PPP', '37405-9988776-3', 'NA-115');
DELIMITER $$

CREATE PROCEDURE get_party_winner_summary_after_endtime()
BEGIN
    -- Step 1: Create temp winners table
    CREATE TEMPORARY TABLE temp_winners AS
    SELECT 
        c.candidate_id,
        c.party_id,
        e.election_id,
        ct.total_votes
    FROM 
        elections e
    JOIN 
        candidates c ON e.election_id = c.election_id
    JOIN 
        candidate_totals ct ON c.candidate_id = ct.candidate_id
    WHERE 
        TIME(NOW()) > STR_TO_DATE(e.lasttime, '%H:%i:%s')  -- lasttime check
        AND e.election_date <= CURDATE()
        AND ct.total_votes = (
            SELECT 
                MAX(ct2.total_votes)
            FROM 
                candidates c2
            JOIN candidate_totals ct2 ON c2.candidate_id = ct2.candidate_id
            WHERE c2.election_id = e.election_id
        );

    -- Step 2: Group by party
    SELECT 
        p.name AS party_name,
        COUNT(*) AS total_wins
    FROM 
        temp_winners tw
    JOIN 
        parties p ON tw.party_id = p.party_id
    GROUP BY 
        p.name
    ORDER BY 
        total_wins DESC;

    -- Step 3: Drop temp table
    DROP TEMPORARY TABLE IF EXISTS temp_winners;
END $$

DELIMITER ;

CALL get_party_winner_summary_after_endtime();
