INSERT INTO credential(id, application, label) VALUES
(1, 'INTRANET', 'CREATE'),
(2, 'INTRANET', 'VIEW'),
(3, 'INTRANET', 'UPDATE'),
(4, 'INTRANET', 'DELETE');

INSERT INTO profile(id, label, active) VALUES
(1, 'ROLE_ADMIN', true),    
(2, 'ROLE_USER', true); 

INSERT INTO profile_credential(id, profile_id, credential_id) VALUES 
(1, 1, 1),
(2, 1, 2),
(3, 1, 3),
(4, 1, 4),
(5, 2, 2);

INSERT INTO account(id, email, username, password, signedup_date, enabled) VALUES 
(1, 'admin@iocean.fr', 'Admin', '$2a$10$ggIHKT/gYkYk0Bt2yP4xvOybahPn7GfSwC0T3fYhCzrZ9ta9LAYt6', '2015-06-02 11:00:00', true),
(2, 'john@iocean.fr', 'John', '$2a$10$dJ9dhL7FqCDXBb1kKj5y.ub4ohRm7VARPLlg5H.AHcd/T/XVEgpA.', '2015-05-10 14:30:00', true),
(3, 'inactive@iocean.fr', 'Inactive', '$2a$10$dJ9dhL7FqCDXBb1kKj5y.ub4ohRm7VARPLlg5H.AHcd/T/XVEgpA.', '2015-05-10 14:30:00', false);

INSERT INTO profile_account(id, account_id, profile_id) VALUES 
(1, 1, 1),
(2, 2, 2),
(3, 3, 2);