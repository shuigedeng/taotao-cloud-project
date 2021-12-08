IF NOT EXISTS (SELECT * FROM SYSOBJECTS WHERE NAME = 'oauth2_registered_client' AND XTYPE='U')
BEGIN TRY
	-- SCHEMA
	CREATE TABLE oauth2_registered_client (
	    id varchar(100) NOT NULL,
	    client_id VARCHAR(100) NOT NULL,
	    client_id_issued_at DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL,
	    client_secret VARCHAR(200) DEFAULT NULL,
	    client_secret_expires_at DATETIME DEFAULT NULL,
	    client_name VARCHAR(200) NOT NULL,
	    client_authentication_methods VARCHAR(1000) NOT NULL,
	    authorization_grant_types VARCHAR(1000) NOT NULL,
	    redirect_uris VARCHAR(1000) DEFAULT NULL,
	    scopes VARCHAR(1000) NOT NULL,
	    client_settings VARCHAR(2000) NOT NULL,
	    token_settings VARCHAR(2000) NOT NULL,
	    PRIMARY KEY (id)
	);
END TRY	
BEGIN CATCH
	SELECT ERROR_MESSAGE() AS 'oauth2_registered_client Message'
END CATCH

IF NOT EXISTS (SELECT * FROM SYSOBJECTS WHERE NAME = 'oauth2_authorization' AND XTYPE='U')
BEGIN TRY
	-- SCHEMA
	CREATE TABLE oauth2_authorization (
	    id VARCHAR(100) NOT NULL,
	    registered_client_id VARCHAR(100) NOT NULL,
	    principal_name VARCHAR(200) NOT NULL,
	    authorization_grant_type VARCHAR(100) NOT NULL,
	    attributes VARCHAR(4000) DEFAULT NULL,
	    state VARCHAR(500) DEFAULT NULL,
	    authorization_code_value VARBINARY(MAX) DEFAULT NULL,
	    authorization_code_issued_at DATETIME DEFAULT NULL,
	    authorization_code_expires_at DATETIME DEFAULT NULL,
	    authorization_code_metadata VARCHAR(2000) DEFAULT NULL,
	    access_token_value VARBINARY(MAX) DEFAULT NULL,
	    access_token_issued_at DATETIME DEFAULT NULL,
	    access_token_expires_at DATETIME DEFAULT NULL,
	    access_token_metadata VARCHAR(2000) DEFAULT NULL,
	    access_token_type VARCHAR(100) DEFAULT NULL,
	    access_token_scopes VARCHAR(1000) DEFAULT NULL,
	    oidc_id_token_value VARBINARY(MAX) DEFAULT NULL,
	    oidc_id_token_issued_at DATETIME DEFAULT NULL,
	    oidc_id_token_expires_at DATETIME DEFAULT NULL,
	    oidc_id_token_metadata VARCHAR(2000) DEFAULT NULL,
	    refresh_token_value VARBINARY(MAX) DEFAULT NULL,
	    refresh_token_issued_at DATETIME DEFAULT NULL,
	    refresh_token_expires_at DATETIME DEFAULT NULL,
	    refresh_token_metadata VARCHAR(2000) DEFAULT NULL,
	    PRIMARY KEY (id)
	);
END TRY	
BEGIN CATCH
	SELECT ERROR_MESSAGE() AS 'oauth2_authorization Message'
END CATCH

IF NOT EXISTS (SELECT * FROM SYSOBJECTS WHERE NAME = 'oauth2_authorization_consent' AND XTYPE='U')
BEGIN TRY
	-- SCHEMA
	CREATE TABLE oauth2_authorization_consent (
	    registered_client_id VARCHAR(100) NOT NULL,
	    principal_name VARCHAR(200) NOT NULL,
	    authorities VARCHAR(1000) NOT NULL,
	    PRIMARY KEY (registered_client_id, principal_name)
	);
END TRY	
BEGIN CATCH
	SELECT ERROR_MESSAGE() AS 'oauth2_authorization_consent Message'
END CATCH
