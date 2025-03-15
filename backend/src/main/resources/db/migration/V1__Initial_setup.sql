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
INSERT INTO category_seq (next_val) VALUES (1);
INSERT INTO unit_seq (next_val) VALUES (1);
INSERT INTO recipe_seq (next_val) VALUES (1);
INSERT INTO item_seq (next_val) VALUES (1);
INSERT INTO recipe_item_seq (next_val) VALUES (1);
INSERT INTO shopping_list_seq (next_val) VALUES (1);

