USE projet_java;

-- =========================================================
-- SEED : TABLES PRINCIPALES
-- =========================================================

INSERT INTO Category (id, name) VALUES
(1, 'Boissons'),
(2, 'Desserts'),
(3, 'Plats');

INSERT INTO Lot (id, quantity, price, dateDelivered, dateDue) VALUES
(100001, 50, 1.20, '2026-03-01 08:00:00', '2026-04-01 23:59:59'),
(100002, 30, 2.50, '2026-03-05 09:00:00', '2026-03-20 23:59:59'),
(100003, 20, 5.00, '2026-03-10 10:00:00', '2026-03-18 23:59:59');

INSERT INTO Product (id, name, price, lot) VALUES
(1, 'Coca-Cola', 3.50, 100001),
(2, 'Tiramisu', 6.00, 100002),
(3, 'Steak frites', 18.50, 100003);

INSERT INTO `Constraint` (id, name) VALUES
(1, 'Interdit aux mineurs'),
(2, 'Halal'),
(3, 'Végétarien');

INSERT INTO `Table` (id, positionX, positionY, floor, capacity, isActive) VALUES
(1, 10, 20, 0, 4, TRUE),
(2, 25, 20, 0, 2, TRUE),
(3, 40, 30, 1, 6, FALSE);

INSERT INTO Menu (name, timePeriodStart, timePeriodEnd) VALUES
('Menu Midi', '2026-03-01 12:00:00', '2026-03-31 14:00:00'),
('Menu Soir', '2026-03-01 18:00:00', '2026-03-31 22:00:00'),
('Carte Permanente', '2026-01-01 00:00:00', NULL);

INSERT INTO Storage (id, isRefrigerated) VALUES
(1, TRUE),
(2, FALSE),
(3, TRUE);

INSERT INTO `Order` (id, dateOrdered, dateCompleted, status, `table`, dateDelivered) VALUES
(10000001, '2026-03-15 12:05:00', '2026-03-15 12:20:00', 'DELIVERED', 1, '2026-03-15 12:25:00'),
(10000002, '2026-03-15 12:10:00', '2026-03-15 12:30:00', 'READY', 2, NULL),
(10000003, '2026-03-15 12:15:00', NULL, 'ORDERED', 1, NULL);

INSERT INTO Payment (id, `date`, amount, method, `order`, status, transactionRef, currency) VALUES
(20000001, '2026-03-15 12:26:00', 25.50, 'card', 10000001, 'COMPLETED', 'TXN-DELIV-001', 'EUR'),
(20000002, '2026-03-15 12:31:00', 6.00, 'cash', 10000002, 'PENDING', 'TXN-READY-002', 'EUR'),
(20000003, '2026-03-15 12:16:00', 18.50, 'check', 10000003, 'FAILED', 'TXN-ORD-003', 'EUR');

INSERT INTO OrderLine (number, `order`, nameSnapshot, priceSnapshot, product, quantity) VALUES
(1, 10000001, 'Steak frites', 18.50, 3, 1),
(2, 10000001, 'Coca-Cola', 3.50, 1, 2),
(1, 10000002, 'Tiramisu', 6.00, 2, 1),
(1, 10000003, 'Steak frites', 18.50, 3, 1);

-- =========================================================
-- SEED : TABLES D’ASSOCIATION
-- =========================================================

INSERT INTO CategoryProduct (category, product) VALUES
(1, 1), -- Boissons -> Coca-Cola
(2, 2), -- Desserts -> Tiramisu
(3, 3); -- Plats -> Steak frites

INSERT INTO ProductConstraint (product, `constraint`) VALUES
(1, 1), -- Coca-Cola -> Interdit aux mineurs (exemple purement technique, pas réaliste)
(2, 3), -- Tiramisu -> Végétarien
(3, 2); -- Steak frites -> Halal

INSERT INTO LotStorage (lot, storage) VALUES
(100001, 1),
(100002, 3),
(100003, 2);

INSERT INTO MenuLine (menu, product) VALUES
('Menu Midi', 3),
('Menu Midi', 1),
('Menu Soir', 3),
('Menu Soir', 2),
('Carte Permanente', 1),
('Carte Permanente', 2),
('Carte Permanente', 3);