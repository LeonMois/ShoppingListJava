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
    serves INTEGER NOT NULL
);
CREATE TABLE item (
    item_id INTEGER PRIMARY KEY,
    item_name TEXT,
    category TEXT,
    unit TEXT,
    UNIQUE (item_name, unit)
    FOREIGN KEY (category) REFERENCES category(category),
    FOREIGN KEY (unit) REFERENCES unit(unit)
);

CREATE TABLE recipe_item (
    recipe_item_id INTEGER PRIMARY KEY,
    recipe_id INTEGER,
    item_id INTEGER,
    amount REAL,
    serves INTEGER,
    FOREIGN KEY (recipe_id) REFERENCES recipe(id),
    FOREIGN KEY (item_id) REFERENCES item(id),
    FOREIGN KEY (serves) REFERENCES recipe(serves)
);
CREATE TABLE shopping_list (
    shopping_list_id INTEGER PRIMARY KEY,
    recipe_item_id INTEGER,
    servings INTEGER,
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