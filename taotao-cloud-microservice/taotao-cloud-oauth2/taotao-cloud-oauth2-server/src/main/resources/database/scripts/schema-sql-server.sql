IF NOT EXISTS (SELECT * FROM SYSOBJECTS WHERE NAME = 'UserPrincipal' AND XTYPE='U')
BEGIN TRY
	-- SCHEMA
	CREATE TABLE UserPrincipal (
  		UserId BIGINT NOT NULL IDENTITY PRIMARY KEY,
  		Username VARCHAR(36) NOT NULL,
  		HashedPassword VARCHAR(128) NOT NULL,
  		AccountNonExpired BIT NOT NULL,
  		AccountNonLocked BIT NOT NULL,
  		CredentialsNonExpired BIT NOT NULL,
  		Enabled BIT NOT NULL,
  		CreatedDate DATETIME NOT NULL,
  		CreatedBy BIGINT DEFAULT 0 NOT NULL,
  		UpdatedDate DATETIME,
  		UpdatedBy BIGINT,
  		DeletedDate DATETIME,
  		CONSTRAINT UC_UserPrincipal_Username UNIQUE (Username)
	)
END TRY	
BEGIN CATCH
	SELECT ERROR_MESSAGE() AS 'UserPrincipal Message'
END CATCH

IF NOT EXISTS (SELECT * FROM SYSOBJECTS WHERE NAME = 'UserPrincipalAuthority' AND XTYPE='U')
BEGIN TRY
	-- SCHEMA
	CREATE TABLE UserPrincipalAuthority (
  		UserId BIGINT NOT NULL,
  		Authority VARCHAR(100) NOT NULL,
  		CONSTRAINT UC_UserPrincipalAuthority_User_Authority UNIQUE (UserId, Authority),
  		CONSTRAINT FK_UserPrincipalAuthority_UserId FOREIGN KEY (UserId) 
		REFERENCES UserPrincipal (UserId) ON DELETE CASCADE
	)
END TRY	
BEGIN CATCH
	SELECT ERROR_MESSAGE() AS 'UserPrincipalAuthority Message'
END CATCH
