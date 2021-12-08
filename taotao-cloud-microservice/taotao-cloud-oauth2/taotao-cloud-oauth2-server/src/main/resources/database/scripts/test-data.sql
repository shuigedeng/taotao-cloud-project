INSERT INTO UserPrincipal (Username, HashedPassword, AccountNonExpired, AccountNonLocked, CredentialsNonExpired, Enabled, 
    CreatedDate, CreatedBy, UpdatedDate, UpdatedBy, DeletedDate)
  VALUES ( -- password
  'user1', '{bcrypt}$2a$10$ofQ95D2nNs1JC.JiPaGo3O11.P7sP3TkcRyXBpyfskwBDJRAh0caG', 1, 1, 1, 1,
    CURRENT_TIMESTAMP, 0, CURRENT_TIMESTAMP, 0, null);

INSERT INTO UserPrincipalAuthority (UserId, Authority)
  VALUES (1, 'ADMIN'), (1, 'USER');
