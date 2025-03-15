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
INSERT INTO shopping_list (id, item_id, quantity) VALUES
(1, 1, 4),
(2, 2, 3),
(3, 3, 0.5),
(4, 4, 300),
(5, 5, 5);
