CREATE TABLE category (
    id INTEGER PRIMARY KEY,
    category_name TEXT UNIQUE
);
CREATE TABLE unit (
    id INTEGER PRIMARY KEY,
    unit_name TEXT UNIQUE NOT NULL
);
CREATE TABLE recipe (
    id INTEGER PRIMARY KEY,
    name TEXT UNIQUE,
    servings INTEGER NOT NULL
);
CREATE TABLE item (
    id INTEGER PRIMARY KEY,
    name TEXT,
    category_id INTEGER,
    unit_id INTEGER,
    UNIQUE (name, unit_id),
    FOREIGN KEY (category_id) REFERENCES category(id),
    FOREIGN KEY (unit_id) REFERENCES unit(id)
);

CREATE TABLE recipe_item (
    id INTEGER PRIMARY KEY,
    recipe_id INTEGER,
    item_id INTEGER,
    quantity FLOAT,
    FOREIGN KEY (recipe_id) REFERENCES recipe(id),
    FOREIGN KEY (item_id) REFERENCES item(id)
);
CREATE TABLE shopping_list (
    id INTEGER PRIMARY KEY,
    item_id INTEGER,
    quantity FLOAT,
    deleted INTEGER,
    FOREIGN KEY (item_id) REFERENCES item(id)
);
CREATE TABLE category_seq (
    hibernate_sequence TEXT PRIMARY KEY,
    next_val INTEGER
);
CREATE TABLE unit_seq (
    hibernate_sequence TEXT PRIMARY KEY,
    next_val INTEGER
);
CREATE TABLE recipe_seq (
    hibernate_sequence TEXT PRIMARY KEY,
    next_val INTEGER
);
CREATE TABLE item_seq (
    hibernate_sequence TEXT PRIMARY KEY,
    next_val INTEGER
);
CREATE TABLE recipe_item_seq (
    hibernate_sequence TEXT PRIMARY KEY,
    next_val INTEGER
);
CREATE TABLE shopping_list_seq (
    hibernate_sequence TEXT PRIMARY KEY,
    next_val INTEGER
);

-- Insert categories
INSERT INTO category (id, category_name) VALUES
(1, 'Fruits'),
(2, 'Vegetables'),
(3, 'Dairy'),
(4, 'Grains'),
(5, 'Meat');

-- Insert units
INSERT INTO unit (id, unit_name) VALUES
(1, 'Piece'),
(2, 'Kilogram'),
(3, 'Liter'),
(4, 'Gram'),
(5, 'Milliliter');

-- Insert recipes
INSERT INTO recipe (id, name, servings) VALUES
(1, 'Fruit Salad', 4),
(2, 'Vegetable Soup', 6),
(3, 'Cheese Sandwich', 2),
(4, 'Pasta', 3),
(5, 'Chicken Stir Fry', 5);

-- Insert items
INSERT INTO item (id, name, category_id, unit_id) VALUES
(1, 'Apple', 1, 1),
(2, 'Carrot', 2, 1),
(3, 'Cheese', 3, 2),
(4, 'Pasta', 4, 2),
(5, 'Chicken Breast', 5, 2);

-- Insert recipe items
INSERT INTO recipe_item (id, recipe_id, item_id, quantity) VALUES
(1, 1, 1, 2),
(2, 1, 2, 1),
(3, 2, 2, 3),
(4, 3, 3, 0.5),
(5, 4, 4, 200);

-- Insert shopping list items
INSERT INTO shopping_list (id, item_id, quantity, deleted) VALUES
(1, 1, 4, 0),
(2, 2, 3, 0),
(3, 3, 0.5, 0),
(4, 4, 300, 0),
(5, 5, 5, 0),
(6, 2, 3, 0);

INSERT INTO category_seq (next_val) VALUES (100);
INSERT INTO unit_seq (next_val) VALUES (100);
INSERT INTO recipe_seq (next_val) VALUES (100);
INSERT INTO item_seq (next_val) VALUES (100);
INSERT INTO recipe_item_seq (next_val) VALUES (100);
INSERT INTO shopping_list_seq (next_val) VALUES (100);
