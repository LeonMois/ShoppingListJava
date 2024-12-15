CREATE TABLE category (
    category_id INTEGER PRIMARY KEY,
    category_name TEXT UNIQUE
);
CREATE TABLE unit (
    unit_id INTEGER PRIMARY KEY,
    unit_name TEXT UNIQUE NOT NULL
);
CREATE TABLE recipe (
    recipe_id INTEGER PRIMARY KEY,
    recipe_name TEXT UNIQUE,
    servings INTEGER NOT NULL
);
CREATE TABLE item (
    item_id INTEGER PRIMARY KEY,
    item_name TEXT,
    category TEXT,
    unit TEXT,
    UNIQUE (item_name, unit),
    FOREIGN KEY (category) REFERENCES category(category_name),
    FOREIGN KEY (unit) REFERENCES unit(unit_name)
);

CREATE TABLE recipe_item (
    recipe_item_id INTEGER PRIMARY KEY,
    recipe_name TEXT,
    item_id INTEGER,
    quantity FLOAT,
    FOREIGN KEY (recipe_name) REFERENCES recipe(recipe_name),
    FOREIGN KEY (item_id) REFERENCES item(item_id)
);
CREATE TABLE shopping_list (
    shopping_list_id INTEGER PRIMARY KEY,
    recipe_item_id INTEGER,
    quantity FLOAT,
    FOREIGN KEY (recipe_item_id) REFERENCES recipe_item(id)
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
INSERT INTO category_seq (next_val) VALUES (0);
INSERT INTO unit_seq (next_val) VALUES (0);
INSERT INTO recipe_seq (next_val) VALUES (0);
INSERT INTO item_seq (next_val) VALUES (0);
INSERT INTO recipe_item_seq (next_val) VALUES (0);
INSERT INTO shopping_list_seq (next_val) VALUES (0);

INSERT INTO category (category_id, category_name) VALUES (1, 'Vegetables');
INSERT INTO unit (unit_id, unit_name) VALUES (1, 'Kilograms');
INSERT INTO recipe (recipe_id, recipe_name, servings) VALUES (1, 'Vegetable Soup', 4);
INSERT INTO item (item_id, item_name, category, unit) VALUES (1, 'Carrot', 'Vegetables', 'Kilograms');
INSERT INTO recipe_item (recipe_item_id, recipe_name, item_id, quantity) VALUES (1, 'Vegetable Soup', 1, 0.5);
INSERT INTO shopping_list (shopping_list_id, recipe_item_id, quantity) VALUES (1, 1, 1.0);

-- Update sequence tables to match the current state
UPDATE category_seq SET next_val = 1;
UPDATE unit_seq SET next_val = 1;
UPDATE recipe_seq SET next_val = 1;
UPDATE item_seq SET next_val = 1;
UPDATE recipe_item_seq SET next_val = 1;
UPDATE shopping_list_seq SET next_val = 1;
