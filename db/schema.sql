DROP DATABASE IF EXISTS projet_java;
CREATE DATABASE projet_java;
USE projet_java;

-- =========================================================
-- TABLES PRINCIPALES
-- =========================================================

CREATE TABLE Category (
    id INT(3) NOT NULL,
    name VARCHAR(50) NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT chk_category_id_positive CHECK (id > 0),
    CONSTRAINT chk_category_name_not_empty CHECK (CHAR_LENGTH(TRIM(name)) > 0)
);

CREATE TABLE Lot (
    id INT(8) NOT NULL,
    quantity INT(4) NOT NULL,
    price DECIMAL(5,2) NOT NULL,
    dateDelivered DATETIME NOT NULL,
    dateDue DATETIME NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT chk_lot_id_positive CHECK (id > 0),
    CONSTRAINT chk_lot_quantity_positive CHECK (quantity > 0),
    CONSTRAINT chk_lot_price_non_negative CHECK (price >= 0),
    CONSTRAINT chk_lot_dates CHECK (dateDue >= dateDelivered)
);

CREATE TABLE Product (
    id INT(3) NOT NULL,
    name VARCHAR(50) NOT NULL,
    price DECIMAL(5,2) NOT NULL,
    lot INT(8) NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_product_lot FOREIGN KEY (lot) REFERENCES Lot(id),
    CONSTRAINT chk_product_id_positive CHECK (id > 0),
    CONSTRAINT chk_product_name_not_empty CHECK (CHAR_LENGTH(TRIM(name)) > 0),
    CONSTRAINT chk_product_price_non_negative CHECK (price >= 0)
);

CREATE TABLE `Constraint` (
    id INT(3) NOT NULL,
    name VARCHAR(50) NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT chk_constraint_id_positive CHECK (id > 0),
    CONSTRAINT chk_constraint_name_not_empty CHECK (CHAR_LENGTH(TRIM(name)) > 0)
);

CREATE TABLE `Table` (
    id INT(4) NOT NULL,
    positionX INT(4) NOT NULL,
    positionY INT(4) NOT NULL,
    floor INT(3),
    capacity INT(3) NOT NULL,
    isActive BOOLEAN NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT chk_table_id_positive CHECK (id > 0),
    CONSTRAINT chk_table_positionX_non_negative CHECK (positionX >= 0),
    CONSTRAINT chk_table_positionY_non_negative CHECK (positionY >= 0),
    CONSTRAINT chk_table_floor_non_negative CHECK (floor IS NULL OR floor >= 0),
    CONSTRAINT chk_table_capacity_positive CHECK (capacity > 0)
);

CREATE TABLE Menu (
    name VARCHAR(30) NOT NULL,
    timePeriodStart DATETIME NOT NULL,
    timePeriodEnd DATETIME,
    PRIMARY KEY (name),
    CONSTRAINT chk_menu_name_not_empty CHECK (CHAR_LENGTH(TRIM(name)) > 0),
    CONSTRAINT chk_menu_period CHECK (
        timePeriodEnd IS NULL OR timePeriodEnd >= timePeriodStart
    )
);

CREATE TABLE Storage (
    id INT(8) NOT NULL,
    isRefrigerated BOOLEAN NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT chk_storage_id_positive CHECK (id > 0)
);

CREATE TABLE `Order` (
     id INT(8) NOT NULL AUTO_INCREMENT,
     dateOrdered DATETIME NOT NULL,
     dateCompleted DATETIME NULL,
     status ENUM('ORDERED', 'IN_PREPARATION', 'READY', 'DELIVERED', 'CANCELLED') NOT NULL,
     isPaid BOOLEAN NOT NULL DEFAULT FALSE,
     `table` INT(4) NOT NULL,
     dateDelivered DATETIME NULL,
     PRIMARY KEY (id),
     CONSTRAINT fk_order_table FOREIGN KEY (`table`) REFERENCES `Table`(id),
     CONSTRAINT chk_order_completed_after_ordered CHECK (
         dateCompleted IS NULL OR dateCompleted >= dateOrdered
         ),
     CONSTRAINT chk_order_delivered_after_ordered CHECK (
         dateDelivered IS NULL OR dateDelivered >= dateOrdered
         ),
     CONSTRAINT chk_order_delivered_after_completed CHECK (
         dateCompleted IS NULL OR dateDelivered IS NULL OR dateDelivered >= dateCompleted
         )
);
CREATE TABLE Payment (
    id INT(8) NOT NULL,
    `date` DATETIME NOT NULL,
    amount DECIMAL(5,2) NOT NULL,
    method ENUM('card', 'cash', 'check') NOT NULL,
    `order` INT(8) NOT NULL,
    status ENUM('PENDING', 'COMPLETED', 'FAILED', 'CANCELLED', 'REFUNDED') NOT NULL,
    transactionRef VARCHAR(30) NOT NULL,
    currency ENUM('EUR', 'USD') NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_payment_order FOREIGN KEY (`order`) REFERENCES `Order`(id),
    CONSTRAINT uq_payment_transactionRef UNIQUE (transactionRef),
    CONSTRAINT chk_payment_id_positive CHECK (id > 0),
    CONSTRAINT chk_payment_amount_non_negative CHECK (amount >= 0),
    CONSTRAINT chk_payment_transactionRef_not_empty CHECK (CHAR_LENGTH(TRIM(transactionRef)) > 0)
);

CREATE TABLE OrderLine (
    number INT(3) NOT NULL,
    `order` INT(8) NOT NULL,
    nameSnapshot VARCHAR(50) NOT NULL,
    priceSnapshot DECIMAL(5,2) NOT NULL,
    product INT(3) NOT NULL,
    quantity INT(3) NOT NULL,
    PRIMARY KEY (number, `order`),
    CONSTRAINT fk_orderline_order FOREIGN KEY (`order`) REFERENCES `Order`(id),
    CONSTRAINT fk_orderline_product FOREIGN KEY (product) REFERENCES Product(id),
    CONSTRAINT chk_orderline_number_positive CHECK (number > 0),
    CONSTRAINT chk_orderline_nameSnapshot_not_empty CHECK (CHAR_LENGTH(TRIM(nameSnapshot)) > 0),
    CONSTRAINT chk_orderline_priceSnapshot_non_negative CHECK (priceSnapshot >= 0),
    CONSTRAINT chk_orderline_quantity_positive CHECK (quantity > 0)
);

-- =========================================================
-- TABLES D'ASSOCIATION
-- =========================================================

CREATE TABLE CategoryProduct (
    category INT(3) NOT NULL,
    product INT(3) NOT NULL,
    PRIMARY KEY (category, product),
    CONSTRAINT fk_categoryproduct_category FOREIGN KEY (category) REFERENCES Category(id),
    CONSTRAINT fk_categoryproduct_product FOREIGN KEY (product) REFERENCES Product(id),
    CONSTRAINT chk_categoryproduct_category_positive CHECK (category > 0),
    CONSTRAINT chk_categoryproduct_product_positive CHECK (product > 0)
);

CREATE TABLE ProductConstraint (
    product INT(3) NOT NULL,
    `constraint` INT(3) NOT NULL,
    PRIMARY KEY (product, `constraint`),
    CONSTRAINT fk_productconstraint_product FOREIGN KEY (product) REFERENCES Product(id),
    CONSTRAINT fk_productconstraint_constraint FOREIGN KEY (`constraint`) REFERENCES `Constraint`(id),
    CONSTRAINT chk_productconstraint_product_positive CHECK (product > 0),
    CONSTRAINT chk_productconstraint_constraint_positive CHECK (`constraint` > 0)
);

CREATE TABLE LotStorage (
    lot INT(8) NOT NULL,
    storage INT(8) NOT NULL,
    PRIMARY KEY (lot, storage),
    CONSTRAINT fk_lotstorage_lot FOREIGN KEY (lot) REFERENCES Lot(id),
    CONSTRAINT fk_lotstorage_storage FOREIGN KEY (storage) REFERENCES Storage(id),
    CONSTRAINT chk_lotstorage_lot_positive CHECK (lot > 0),
    CONSTRAINT chk_lotstorage_storage_positive CHECK (storage > 0)
);

CREATE TABLE MenuLine (
    menu VARCHAR(30) NOT NULL,
    product INT(3) NOT NULL,
    PRIMARY KEY (menu, product),
    CONSTRAINT fk_menuline_menu FOREIGN KEY (menu) REFERENCES Menu(name),
    CONSTRAINT fk_menuline_product FOREIGN KEY (product) REFERENCES Product(id),
    CONSTRAINT chk_menuline_menu_not_empty CHECK (CHAR_LENGTH(TRIM(menu)) > 0),
    CONSTRAINT chk_menuline_product_positive CHECK (product > 0)
);