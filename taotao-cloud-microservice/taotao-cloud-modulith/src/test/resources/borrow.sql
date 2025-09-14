INSERT INTO borrow_books (id, version, title, barcode, isbn, status)
VALUES ('018dc771-7b96-776b-980d-caf7c6b2c00b', 0, 'Sapiens', '13268510', '9780062316097', 'AVAILABLE'),
       ('018dc771-6e03-7f3b-adc1-0b9f9810bde4', 0, 'Moby-Dick', '64321704', '9780763630188', 'AVAILABLE'),
       ('018dc771-97e4-7e1e-921f-50d3397d6b32', 0, 'To Kill a Mockingbird', '49031878', '9780446310789', 'ON_HOLD'),
       ('018dc771-bd5f-71c5-b481-e9b9e8268c6c', 0, '1984', '37040952', '9780451520500', 'ISSUED');

INSERT INTO borrow_holds (id, version, book_barcode, date_of_hold, status)
VALUES ('018dc74a-4830-75cf-a194-5e9815727b02', 0, '49031878', '2023-03-11', 'HOLDING'),
       ('018dc74a-8b3d-732e-806f-d210f079c0cc', 0, '37040952', '2023-03-24', 'ACTIVE');

-- INSERT INTO loan (id, version, barcode, patron_id, date_of_hold, date_of_checkout,
--                   hold_duration_in_days,
--                   loan_duration_in_days, date_of_checkin, status)
-- VALUES (10, 0, '49031878', 800, '2023-03-11', NULL, 3, 14, NULL, 'HOLDING'),
--        (11, 0, '37040952', 800, '2023-03-24', '2023-03-25', 3, 14, NULL, 'ACTIVE');

-- INSERT INTO patron (id, status)
-- VALUES (800, 'ACTIVE');
