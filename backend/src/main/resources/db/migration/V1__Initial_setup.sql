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
INSERT INTO category_seq (next_val) VALUES (1);
INSERT INTO unit_seq (next_val) VALUES (1);
INSERT INTO recipe_seq (next_val) VALUES (1);
INSERT INTO item_seq (next_val) VALUES (1);
INSERT INTO recipe_item_seq (next_val) VALUES (1);
INSERT INTO shopping_list_seq (next_val) VALUES (1);

