CREATE TABLE user_roles (
                            id BIGINT AUTO_INCREMENT PRIMARY KEY,
                            role VARCHAR(255) NOT NULL
);

CREATE TABLE users (
                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                       username VARCHAR(255) NOT NULL UNIQUE,
                       first_name VARCHAR(255) NOT NULL,
                       last_name VARCHAR(255) NOT NULL,
                       email VARCHAR(255) NOT NULL UNIQUE,
                       password VARCHAR(255) NOT NULL,
                       role_id BIGINT,
                       animal_id BIGINT,
                       img MEDIUMBLOB,
                       FOREIGN KEY (role_id) REFERENCES user_roles(id)
);

CREATE TABLE advanced_user_details (
                                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                       user_id BIGINT NOT NULL,
                                       phone_number VARCHAR(255) NOT NULL,
                                       address VARCHAR(255) NOT NULL,
                                       city VARCHAR(255) NOT NULL,
                                       state VARCHAR(255) NOT NULL,
                                       postal_code VARCHAR(255) NOT NULL,
                                       country VARCHAR(255) NOT NULL,
                                       has_previous_experience_with_pets BOOLEAN NOT NULL,
                                       has_other_pets BOOLEAN NOT NULL,
                                       household_type VARCHAR(255) NOT NULL,
                                       employment_status VARCHAR(255) NOT NULL,
                                       reason_for_adoption VARCHAR(255) NOT NULL,
                                       has_children BOOLEAN NOT NULL,
                                       has_fenced_yard BOOLEAN NOT NULL,
                                       reference_contact VARCHAR(255) NOT NULL,
                                       background_check_status VARCHAR(255) NOT NULL,
                                       FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE animals (
                         id BIGINT AUTO_INCREMENT PRIMARY KEY,
                         name VARCHAR(255),
                         type VARCHAR(255),
                         availability BOOLEAN,
                         breed VARCHAR(255),
                         age INT DEFAULT 0,
                         description TEXT,
                         rating DOUBLE DEFAULT 0.0,
                         slogan VARCHAR(255),
                         img MEDIUMBLOB,
                         user_id BIGINT,
                         FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE comments (
                          id BIGINT AUTO_INCREMENT PRIMARY KEY,
                          content VARCHAR(255) NOT NULL,
                          created_at DATETIME NOT NULL,
                          animal_id BIGINT NOT NULL,
                          user_id BIGINT NOT NULL,
                          likes INT DEFAULT 0,
                          dislikes INT DEFAULT 0,
                          rating INT DEFAULT 0,
                          FOREIGN KEY (animal_id) REFERENCES animals(id),
                          FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE comment_reactions (
                                   id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                   user_id BIGINT NOT NULL,
                                   comment_id BIGINT NOT NULL,
                                   reaction_type VARCHAR(255),
                                   FOREIGN KEY (user_id) REFERENCES users(id),
                                   FOREIGN KEY (comment_id) REFERENCES comments(id)
);

CREATE TABLE walk_histories (
                                id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                user_id BIGINT,
                                animal_name VARCHAR(255),
                                animal_type VARCHAR(255),
                                local_date DATE,
                                FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TEMPORARY TABLE numbers (num INT);


CREATE TABLE animal_names (
                              id INT AUTO_INCREMENT PRIMARY KEY,
                              name VARCHAR(255) NOT NULL
);


CREATE TABLE animal_descriptions (
                                     id INT AUTO_INCREMENT PRIMARY KEY,
                                     description TEXT
);


CREATE TABLE animal_slogans (
                                id INT AUTO_INCREMENT PRIMARY KEY,
                                slogan TEXT
);


CREATE TABLE animal_breed (
                              id INT AUTO_INCREMENT PRIMARY KEY,
                              breed VARCHAR(255) NOT NULL
);
