INSERT INTO numbers (num)
SELECT a.N + b.N * 10 + 1 AS num
FROM
    (SELECT 0 AS N UNION ALL SELECT 1 UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4 UNION ALL SELECT 5 UNION ALL SELECT 6 UNION ALL SELECT 7 UNION ALL SELECT 8 UNION ALL SELECT 9) AS a
        CROSS JOIN
    (SELECT 0 AS N UNION ALL SELECT 1 UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4 UNION ALL SELECT 5 UNION ALL SELECT 6 UNION ALL SELECT 7 UNION ALL SELECT 8 UNION ALL SELECT 9) AS b
WHERE a.N + b.N * 10 + 1 <= 300;




INSERT INTO user_roles (role)
VALUES ('ADMIN'),
       ('USER');




INSERT INTO users (username, first_name, last_name, email, password, role_id)
VALUES ('VeselinPetranchev', 'Veselin', 'Petranchev', 'veselin.petranchev@gmail.com', '1234', 1);

INSERT INTO users (username, first_name, last_name, email, password, role_id)
SELECT
    CONCAT('user', num) AS username,
    CONCAT('FirstName', num) AS first_name,
    CONCAT('LastName', num) AS last_name,
    CONCAT('user', num, '@example.com') AS email,
    'password' AS password,
    2 AS role_id
FROM numbers
LIMIT 30;




INSERT INTO advanced_user_details (user_id, phone_number, address, city, state, postal_code, country,
                                   has_previous_experience_with_pets, has_other_pets, household_type, employment_status,
                                   reason_for_adoption, has_children, has_fenced_yard, reference_contact, background_check_status)
SELECT
    id AS user_id,
    CONCAT('123-456-', LPAD(FLOOR(RAND()*9999), 4, '0')) AS phone_number,
    CONCAT('Address ', id) AS address,
    'City' AS city,
    'State' AS state,
    LPAD(FLOOR(RAND()*99999), 5, '0') AS postal_code,
    'Country' AS country,
    RAND() > 0.5 AS has_previous_experience_with_pets,
    RAND() > 0.5 AS has_other_pets,
    IF(RAND() > 0.5, 'House', 'Apartment') AS household_type,
    IF(RAND() > 0.5, 'Employed', 'Unemployed') AS employment_status,
    'Because I love animals' AS reason_for_adoption,
    RAND() > 0.5 AS has_children,
    RAND() > 0.5 AS has_fenced_yard,
    CONCAT('Reference ', id) AS reference_contact,
    IF(RAND() > 0.5, 'Cleared', 'Pending') AS background_check_status
FROM users
LIMIT 32;




INSERT INTO animals (name, type, availability, breed, age, description, rating, slogan, img, user_id)
SELECT
    (SELECT name FROM animal_names ORDER BY RAND() LIMIT 1) AS name,
    IF(RAND() > 0.5, 'Dog', 'Cat') AS type,
    RAND() > 0.5 AS availability,
    (SELECT breed FROM animal_breed ORDER BY RAND() LIMIT 1) AS breed,
    FLOOR(RAND() * 15) AS age,
    (SELECT description FROM animal_descriptions ORDER BY RAND() LIMIT 1) as description,
    ROUND(RAND() * 5, 2) AS rating,
    (SELECT slogan FROM animal_slogans ORDER BY RAND() LIMIT 1) as slogan,
    NULL AS img,
    (SELECT id FROM users ORDER BY RAND() LIMIT 1) AS user_id
FROM numbers
LIMIT 250;

INSERT INTO comments (content, created_at, animal_id, user_id, likes, dislikes, rating)
SELECT
    CONCAT('Comment content ', num) AS content,
    NOW() - INTERVAL (RAND() * 30) DAY AS created_at,
    (SELECT id FROM animals ORDER BY RAND() LIMIT 1) AS animal_id,
    (SELECT id FROM users ORDER BY RAND() LIMIT 1) AS user_id,
    FLOOR(RAND() * 100) AS likes,
    FLOOR(RAND() * 100) AS dislikes,
    FLOOR(RAND() * 5) AS rating
FROM numbers
LIMIT 250;




INSERT INTO comment_reactions (user_id, comment_id, reaction_type)
SELECT
    (SELECT id FROM users ORDER BY RAND() LIMIT 1) AS user_id,
    (SELECT id FROM comments ORDER BY RAND() LIMIT 1) AS comment_id,
    IF(RAND() > 0.5, 'Like', 'Dislike') AS reaction_type
FROM numbers
LIMIT 250;




CREATE TEMPORARY TABLE temp_animals AS
SELECT id, name, type
FROM animals;

INSERT INTO walk_histories (user_id, animal_name, animal_type, local_date)
SELECT
    u.id AS user_id,
    a.name AS animal_name,
    a.type AS animal_type,
    CURDATE() - INTERVAL FLOOR(RAND() * 365) DAY AS local_date
FROM
    (SELECT id FROM users ORDER BY RAND() LIMIT 1) u
        CROSS JOIN (SELECT name, type FROM temp_animals ORDER BY RAND() LIMIT 1) a
LIMIT 250;




DROP TEMPORARY TABLE numbers;