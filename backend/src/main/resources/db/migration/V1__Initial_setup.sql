CREATE TABLE category (
    id INTEGER PRIMARY KEY,
    category TEXT UNIQUE
);
CREATE TABLE unit (
    id INTEGER PRIMARY KEY,
    unit TEXT UNIQUE NOT NULL
);
CREATE TABLE recipe (
    id INTEGER PRIMARY KEY,
    name TEXT UNIQUE,
    serves INTEGER NOT NULL
);
CREATE TABLE item (
    id INTEGER PRIMARY KEY,
    name TEXT UNIQUE,
    category TEXT,
    unit TEXT,
    FOREIGN KEY (category) REFERENCES category(category),
    FOREIGN KEY (unit) REFERENCES unit(unit)
);

CREATE TABLE recipe_item (
    id INTEGER PRIMARY KEY,
    recipe_id INTEGER,
    item_id INTEGER,
    amount REAL,
    serves INTEGER,
    FOREIGN KEY (recipe_id) REFERENCES recipe(id),
    FOREIGN KEY (item_id) REFERENCES item(id),
    FOREIGN KEY (serves) REFERENCES recipe(serves)
);
CREATE TABLE shopping_list (
    id INTEGER PRIMARY KEY,
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