CREATE TABLE user_catalogue_permission(
    id BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    user_catalogue_id BIGINT UNSIGNED NOT NULL,
    permission_id BIGINT UNSIGNED NOT NULL,
    CONSTRAINT fk_user_catalogue FOREIGN KEY (user_catalogue_id) REFERENCES user_catalogues(id) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_permission FOREIGN KEY (permission_id) REFERENCES permissions(id) ON DELETE CASCADE ON UPDATE CASCADE
);  