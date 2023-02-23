create table UserPrincipal (
  	UserId BIGINT NOT NULL IDENTITY PRIMARY KEY,
  	Username varchar2(36) NOT NULL,
  	HashedPassword nvarchar2(128) NOT NULL,
  	AccountNonExpired boolean NOT NULL,
  	AccountNonLocked boolean NOT NULL,
  	CredentialsNonExpired boolean NOT NULL,
  	Enabled boolean NOT NULL,
  	CreatedDate timestamp NOT NULL,
  	CreatedBy BIGINT DEFAULT 0 NOT NULL,
  	UpdatedDate timestamp,
  	UpdatedBy BIGINT,
  	DeletedDate timestamp,
  	constraint UC_UserPrincipal_Username unique (Username)
);

create table UserPrincipalAuthority (
  	UserId BIGINT NOT NULL,
  	Authority varchar2(100) NOT NULL,
  	constraint UC_UserPrincipalAuthority_User_Authority unique (UserId, Authority),
  	constraint FK_UserPrincipalAuthority_UserId foreign key (UserId) 
	references UserPrincipal (UserId) on delete cascade
);

