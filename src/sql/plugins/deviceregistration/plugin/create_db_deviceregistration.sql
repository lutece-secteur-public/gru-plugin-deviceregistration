
--
-- Structure for table deviceregistration_deviceregistration
--

DROP TABLE IF EXISTS deviceregistration_deviceregistration;
CREATE TABLE deviceregistration_deviceregistration (
id_device_registration int AUTO_INCREMENT,
customer_id varchar(50),
connection_id varchar(50),
registration_token varchar(255) NOT NULL UNIQUE,
token_issuer varchar(50) NOT NULL,
PRIMARY KEY (id_device_registration)
);

--
-- Structure for table deviceregistration_deviceregistration_history
--
DROP TABLE IF EXISTS deviceregistration_deviceregistration_history;
CREATE TABLE deviceregistration_deviceregistration_history
(
    id_history int AUTO_INCREMENT,
    created_at TIMESTAMP(3) DEFAULT CURRENT_TIMESTAMP NOT NULL,
    change_type varchar (50) NOT NULL,
    customer_id varchar(50),
    connection_id varchar(50),
    registration_token varchar(255) NOT NULL,
    token_issuer varchar(50),

    PRIMARY KEY (id_history)
);
