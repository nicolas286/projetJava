USE projet_java;

-- =========================================================
-- TESTS SIMPLES : SELECT *
-- =========================================================

SELECT * FROM Category;
SELECT * FROM Product;
SELECT * FROM `Constraint`;
SELECT * FROM Lot;
SELECT * FROM Storage;
SELECT * FROM `Table`;
SELECT * FROM Menu;
SELECT * FROM `Order`;
SELECT * FROM OrderLine;
SELECT * FROM Payment;
SELECT * FROM CategoryProduct;
SELECT * FROM ProductConstraint;
SELECT * FROM LotStorage;
SELECT * FROM MenuLine;

-- =========================================================
-- TESTS DE BASE : FILTRES ET TRI
-- =========================================================

-- Produits triés par prix croissant
SELECT *
FROM Product
ORDER BY price ASC;

-- Produits à plus de 5€
SELECT *
FROM Product
WHERE price > 5.00;

-- Tables actives uniquement
SELECT *
FROM `Table`
WHERE isActive = TRUE;

-- Commandes livrées
SELECT *
FROM `Order`
WHERE status = 'DELIVERED';

-- Paiements complétés
SELECT *
FROM Payment
WHERE status = 'COMPLETED';

-- Lots dont la date limite est proche
SELECT *
FROM Lot
ORDER BY dateDue ASC;

-- =========================================================
-- JOINTURES SIMPLES
-- =========================================================

-- Produits avec leur lot
SELECT
    p.id AS product_id,
    p.name AS product_name,
    p.price AS selling_price,
    l.id AS lot_id,
    l.quantity AS lot_quantity,
    l.price AS purchase_price,
    l.dateDelivered,
    l.dateDue
FROM Product p
JOIN Lot l ON p.lot = l.id;

-- Commandes avec numéro de table
SELECT
    o.id AS order_id,
    o.dateOrdered,
    o.status,
    t.id AS table_id,
    t.capacity,
    t.floor
FROM `Order` o
JOIN `Table` t ON o.`table` = t.id;

-- Paiements avec infos de commande
SELECT
    p.id AS payment_id,
    p.`date`,
    p.amount,
    p.method,
    p.status AS payment_status,
    o.id AS order_id,
    o.status AS order_status
FROM Payment p
JOIN `Order` o ON p.`order` = o.id;

-- Lignes de commande avec nom actuel du produit
SELECT
    ol.`order`,
    ol.number,
    ol.nameSnapshot,
    ol.priceSnapshot,
    ol.quantity,
    p.name AS current_product_name,
    p.price AS current_product_price
FROM OrderLine ol
JOIN Product p ON ol.product = p.id
ORDER BY ol.`order`, ol.number;

-- =========================================================
-- JOINTURES MANY-TO-MANY
-- =========================================================

-- Catégories et produits
SELECT
    c.name AS category_name,
    p.name AS product_name,
    p.price
FROM CategoryProduct cp
JOIN Category c ON cp.category = c.id
JOIN Product p ON cp.product = p.id
ORDER BY c.name, p.name;

-- Contraintes par produit
SELECT
    p.name AS product_name,
    c.name AS constraint_name
FROM ProductConstraint pc
JOIN Product p ON pc.product = p.id
JOIN `Constraint` c ON pc.`constraint` = c.id
ORDER BY p.name, c.name;

-- Zones de stockage de chaque lot
SELECT
    l.id AS lot_id,
    s.id AS storage_id,
    s.isRefrigerated
FROM LotStorage ls
JOIN Lot l ON ls.lot = l.id
JOIN Storage s ON ls.storage = s.id
ORDER BY l.id;

-- Produits présents dans chaque menu
SELECT
    m.name AS menu_name,
    p.name AS product_name,
    p.price
FROM MenuLine ml
JOIN Menu m ON ml.menu = m.name
JOIN Product p ON ml.product = p.id
ORDER BY m.name, p.name;

-- =========================================================
-- TESTS MÉTIER PLUS INTÉRESSANTS
-- =========================================================

-- Détail complet des commandes
SELECT
    o.id AS order_id,
    o.dateOrdered,
    o.status,
    t.id AS table_id,
    ol.number AS line_number,
    ol.nameSnapshot,
    ol.priceSnapshot,
    ol.quantity,
    (ol.priceSnapshot * ol.quantity) AS line_total
FROM `Order` o
JOIN `Table` t ON o.`table` = t.id
JOIN OrderLine ol ON o.id = ol.`order`
ORDER BY o.id, ol.number;

-- Total théorique par commande
SELECT
    ol.`order` AS order_id,
    SUM(ol.priceSnapshot * ol.quantity) AS order_total
FROM OrderLine ol
GROUP BY ol.`order`
ORDER BY ol.`order`;

-- Comparaison total commande vs paiement
SELECT
    o.id AS order_id,
    SUM(ol.priceSnapshot * ol.quantity) AS computed_total,
    p.amount AS payment_amount,
    p.status AS payment_status
FROM `Order` o
JOIN OrderLine ol ON o.id = ol.`order`
LEFT JOIN Payment p ON o.id = p.`order`
GROUP BY o.id, p.amount, p.status
ORDER BY o.id;

-- Nombre de produits par catégorie
SELECT
    c.name AS category_name,
    COUNT(cp.product) AS number_of_products
FROM Category c
LEFT JOIN CategoryProduct cp ON c.id = cp.category
GROUP BY c.id, c.name
ORDER BY number_of_products DESC, c.name;

-- Nombre de produits par menu
SELECT
    m.name AS menu_name,
    COUNT(ml.product) AS number_of_products
FROM Menu m
LEFT JOIN MenuLine ml ON m.name = ml.menu
GROUP BY m.name
ORDER BY m.name;

-- Quantité totale commandée par produit
SELECT
    p.name AS product_name,
    SUM(ol.quantity) AS total_quantity_ordered
FROM Product p
LEFT JOIN OrderLine ol ON p.id = ol.product
GROUP BY p.id, p.name
ORDER BY total_quantity_ordered DESC, p.name;

-- =========================================================
-- TESTS SUR LES DATES
-- =========================================================

-- Commandes classées de la plus récente à la plus ancienne
SELECT
    id,
    dateOrdered,
    status
FROM `Order`
ORDER BY dateOrdered DESC;

-- Menus encore valides à une date donnée
SELECT *
FROM Menu
WHERE timePeriodEnd IS NULL
   OR timePeriodEnd >= '2026-03-15 12:00:00';

-- Lots bientôt périmés
SELECT
    id,
    quantity,
    dateDue
FROM Lot
WHERE dateDue <= '2026-03-25 23:59:59'
ORDER BY dateDue ASC;

-- =========================================================
-- TESTS D’AGRÉGATION
-- =========================================================

-- Prix moyen des produits
SELECT AVG(price) AS average_product_price
FROM Product;

-- Capacité totale des tables actives
SELECT SUM(capacity) AS total_active_capacity
FROM `Table`
WHERE isActive = TRUE;

-- Montant total des paiements complétés
SELECT SUM(amount) AS total_completed_payments
FROM Payment
WHERE status = 'COMPLETED';

-- Nombre de commandes par statut
SELECT
    status,
    COUNT(*) AS nb_orders
FROM `Order`
GROUP BY status
ORDER BY nb_orders DESC, status;

-- Nombre de paiements par méthode
SELECT
    method,
    COUNT(*) AS nb_payments
FROM Payment
GROUP BY method
ORDER BY nb_payments DESC, method;

-- =========================================================
-- TESTS AVEC LEFT JOIN
-- =========================================================

-- Tous les menus, même ceux sans produits
SELECT
    m.name AS menu_name,
    p.name AS product_name
FROM Menu m
LEFT JOIN MenuLine ml ON m.name = ml.menu
LEFT JOIN Product p ON ml.product = p.id
ORDER BY m.name, p.name;

-- Tous les produits, même ceux sans contrainte
SELECT
    p.name AS product_name,
    c.name AS constraint_name
FROM Product p
LEFT JOIN ProductConstraint pc ON p.id = pc.product
LEFT JOIN `Constraint` c ON pc.`constraint` = c.id
ORDER BY p.name, c.name;

-- Toutes les catégories, même sans produit
SELECT
    c.name AS category_name,
    p.name AS product_name
FROM Category c
LEFT JOIN CategoryProduct cp ON c.id = cp.category
LEFT JOIN Product p ON cp.product = p.id
ORDER BY c.name, p.name;

-- =========================================================
-- TESTS DE COHÉRENCE
-- =========================================================

-- Vérifier s'il existe des commandes sans lignes
SELECT
    o.id AS order_id
FROM `Order` o
LEFT JOIN OrderLine ol ON o.id = ol.`order`
WHERE ol.`order` IS NULL;

-- Vérifier s'il existe des paiements sans commande liée
SELECT
    p.id AS payment_id
FROM Payment p
LEFT JOIN `Order` o ON p.`order` = o.id
WHERE o.id IS NULL;

-- Vérifier les lignes dont le snapshot diffère du prix actuel
SELECT
    ol.`order`,
    ol.number,
    ol.nameSnapshot,
    p.name AS current_name,
    ol.priceSnapshot,
    p.price AS current_price
FROM OrderLine ol
JOIN Product p ON ol.product = p.id
WHERE ol.nameSnapshot <> p.name
   OR ol.priceSnapshot <> p.price;

-- =========================================================
-- BONUS : VUE GLOBALE LISIBLE DES COMMANDES
-- =========================================================

SELECT
    o.id AS order_id,
    o.status AS order_status,
    o.dateOrdered,
    t.id AS table_id,
    ol.number AS line_no,
    ol.nameSnapshot AS product_name,
    ol.quantity,
    ol.priceSnapshot,
    (ol.quantity * ol.priceSnapshot) AS line_total,
    pay.status AS payment_status,
    pay.amount AS payment_amount,
    pay.method AS payment_method
FROM `Order` o
JOIN `Table` t ON o.`table` = t.id
JOIN OrderLine ol ON o.id = ol.`order`
LEFT JOIN Payment pay ON o.id = pay.`order`
ORDER BY o.id, ol.number;