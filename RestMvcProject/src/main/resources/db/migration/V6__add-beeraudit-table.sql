CREATE TABLE beer_audit(
audit_id VARCHAR(36) NOT NULL,
id  VARCHAR(36) NOT NULL,
version INTEGER NOT NULL,
beer_name VARCHAR(50) NOT NULL,
beer_style SMALLINT,
upc VARCHAR(250) NOT NULL,
quantity_on_hand INTEGER,
price DECIMAL(38, 2) NOT NULL,
created_date TIMESTAMP(6),
update_date TIMESTAMP(6),
creation_date_audit TIMESTAMP(6),
principal_name VARCHAR(255),
audit_event_type VARCHAR(255),
PRIMARY KEY (audit_id)
) ENGINE = InnoDB;