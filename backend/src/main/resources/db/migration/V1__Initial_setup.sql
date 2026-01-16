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
-- Insert categories
INSERT INTO category (id, category_name) VALUES
(1, 'Fruits'),
(2, 'Vegetables'),
(3, 'Dairy'),
(4, 'Grains'),
(5, 'Meat'),
(6, 'Seafood'),
(7, 'Herbs'),
(8, 'Spices'),
(9, 'Oils'),
(10, 'Condiments'),
(11, 'Sauces'),
(12, 'Sweeteners'),
(13, 'Canned Goods'),
(14, 'Broths'),
(15, 'Legumes'),
(16, 'Baking'),
(17, 'Liquids'),
(18, 'Soups'),
(19, 'Seasonings'),
(20, 'Sweets'),
(21, 'Pantry'),
(22, 'Misc'),
(23, 'Bakery'),
(24, 'Beverages'),
(25, 'Deli'),
(26, 'Frozen'),
(27, 'Household'),
(28, 'Pasta & Noodles'),
(29, 'Personal Care'),
(30, 'Produce'),
(31, 'Snacks');

-- Insert units
INSERT INTO unit (id, unit_name) VALUES
(1, 'Gram'),
(2, 'Kilogram'),
(3, 'Milliliter'),
(4, 'Liter'),
(5, 'Piece'),
(6, 'Pieces'),
(7, 'Tablespoon'),
(8, 'Teaspoon'),
(9, 'Cup'),
(10, 'Pinch'),
(11, 'Handful'),
(12, 'Clove'),
(13, 'Bunch'),
(14, 'Can'),
(15, 'Bottle'),
(16, 'Packet'),
(17, 'Sachet'),
(18, 'Spray'),
(19, 'Sprays'),
(20, 'Wedge'),
(21, 'Rib'),
(22, 'Square'),
(23, 'Ounce'),
(24, 'Pound'),
(25, 'Medium-size'),
(26, 'Large'),
(28, 'Tin'),
(29, 'To Taste'),
(30, 'Juice of 1'),
(31, '1/4 Cup'),
(32, '1/2 Cup'),
(33, '150g Packet'),
(34, '250g Pouch'),
(35, 'CM'),
(36, 'Lime'),
(37, 'kg'),
(38, 'g'),
(39, 'l'),
(40, 'pack'),
(41, 'box'),
(42, 'jar'),
(43, 'bar'),
(44, 'tub'),
(45, 'roll'),
(46, 'loaf'),
(47, 'block'),
(48, 'cube'),
(49, 'fillet'),
(50, 'tube');


-- Insert recipes
INSERT INTO recipe (id, name, servings) VALUES
(6, 'Black & Blue Soup', 8),
(7, 'White Chicken Chili', 6),
(8, 'Stuffed Peppers', 4),
(9, 'Crispy Chilli Turkey and Udon Noodles', 4),
(10, 'Chipotle Beefy Noodles', 4),
(11, 'Beefy Fried Rice', 4),
(12, 'Sweet Potato Shepherd’s Pie', 4),
(13, 'Double-Decker Chicken and Bacon Pie', 6),
(14, 'Creamy King Prawn Pasta', 4),
(15, 'Gnocchi Bolognese', 4),
(16, 'Creamy Halloumi Linguine', 4),
(17, 'Mental Lentil Soup', 5),
(18, 'Chicken, Chorizo and Sweet Potato Soup', 6),
(19, 'Chicken Sausage, Garlic and Herb Pasta', 6),
(20, 'Barbecue Chicken Wraps', 4),
(21, 'Peri-Peri Chicken Salad', 2),
(22, 'Slow Cooker Beef Stew', 4),
(23, 'Thai Red Curry Traybake', 4),
(24, 'Chicken Curry Traybake', 4),
(25, 'Katsu Curry Traybake', 4);

-- Insert items
INSERT INTO item (id, name, category_id, unit_id) VALUES
(1, 'extra-virgin olive oil', 21, 7),
(2, 'shallots', 2, 26),
(3, 'celery', 2, 21),
(4, 'red bell pepper', 2, 26),
(5, 'garlic', 2, 12),
(6, 'low-sodium vegetable broth', 17, 9),
(7, 'lime juice', 17, 30),
(8, 'fresh cilantro', 2, 31),
(9, 'seasoned salt', 8, 8),
(11, 'bay leaves', 8, 5),
(13, 'ground cumin', 8, 7),
(14, 'chili powder', 8, 8),
(15, 'blue cheese crumbles', 3, 32),
(16, 'reduced-fat sour cream', 3, 44),
(17, 'ghee', 3, 7),
(19, 'chicken', 5, 24),
(22, 'salsa verde', 11, 9),
(23, 'cumin', 8, 8),
(24, 'black pepper', 8, 8),
(25, 'lime', 1, 5),
(26, 'green chiles', 2, 5),
(27, 'maple syrup', 12, 7),
(30, 'beans', 15, 14),
(31, 'cornstarch', 16, 7),
(33, 'frozen corn', 2, 40),
(35, 'cheese', 3, 9),
(36, 'cilantro', 7, 13),
(37, 'poblano peppers', 2, 26),
(38, 'olive oil', 9, 7),
(39, 'Italian seasoning', 8, 8),
(40, 'dried oregano', 8, 8),
(41, 'Worcestershire sauce', 11, 7),
(42, 'sugar-free steak sauce', 11, 7),
(45, 'cauliflower rice', 4, 9),
(46, 'bell peppers', 2, 25),
(47, 'shredded low-fat cheese', 3, 9),
(48, '90% lean ground beef', 5, 24),
(49, 'ground turkey', 5, 24),
(50, 'ground chicken', 5, 24),
(51, 'yellow onion', 2, 5),
(52, 'garlic powder', 8, 8),
(53, 'vegetable oil', 9, 7),
(54, 'lean turkey mince', 5, 1),
(55, 'garlic cloves', 2, 12),
(56, 'fresh root ginger', 2, 35),
(57, 'honey', 12, 7),
(58, 'light soy sauce', 10, 7),
(59, 'sweet chilli sauce', 10, 7),
(61, 'juice of limes', 1, 36),
(62, 'carrots', 2, 5),
(63, 'spring onions', 2, 5),
(65, 'lime wedges', 1, 20),
(66, 'lean beef mince', 5, 1),
(67, 'paprika', 8, 8),
(68, 'onion powder', 8, 8),
(69, 'garlic paste', 10, 7),
(70, 'ginger paste', 10, 7),
(71, 'chipotle paste', 10, 7),
(73, 'red chillies', 2, 5),
(74, 'medium noodles', 4, 33),
(78, 'basmati rice', 4, 34),
(79, 'hoisin sauce', 10, 7),
(81, 'sriracha sauce', 10, 7),
(82, 'sweet potatoes', 2, 1),
(83, 'cooking oil', 10, 19),
(84, 'lamb mince', 5, 1),
(85, 'red onion', 2, 6),
(87, 'tomato purée', 10, 7),
(88, 'fresh rosemary leaves', 19, 8),
(89, 'fresh thyme leaves', 19, 8),
(90, 'red wine', 17, 3),
(92, 'frozen garden peas', 2, 1),
(94, 'cheddar', 3, 1),
(95, 'parmesan', 3, 1),
(96, 'potatoes', 2, 2),
(98, 'bacon medallions', 5, 6),
(99, 'onion', 2, 6),
(100, 'condensed cream of chicken soup', 13, 1),
(101, 'onion gravy granules', 19, 8),
(102, 'rigatoni pasta', 4, 1),
(104, 'lazy garlic', 2, 8),
(105, 'lazy ginger', 2, 8),
(106, 'lazy chillies', 2, 8),
(107, 'raw king prawns', 6, 1),
(109, 'baby spinach leaves', 2, 11),
(110, 'sun-dried tomatoes', 2, 1),
(111, 'mushrooms', 2, 1),
(113, 'chilli flakes', 8, 8),
(114, 'chopped tomatoes', 2, 1),
(115, 'single cream', 3, 3),
(116, 'gnocchi', 4, 1),
(119, 'linguine pasta', 4, 1),
(120, 'light halloumi', 3, 1),
(122, 'dried Italian herbs', 7, 7),
(124, 'hot vegetable stock', 14, 3),
(125, 'chilli powder', 19, 7),
(126, 'parsley', 7, 11),
(127, 'chorizo', 5, 1),
(129, 'curry powder', 19, 7),
(130, 'salt', 19, 29),
(132, 'dried split red lentils', 15, 1),
(134, 'chicken sausages', 5, 6),
(135, 'condensed cream of tomato soup', 13, 3),
(136, 'pepper', 19, 29),
(137, 'low calorie wraps', 4, 5),
(140, 'sliced jalapeños', 2, 11),
(141, 'dark chocolate', 20, 22),
(142, 'crème fraîche', 3, 7),
(144, 'steak', 5, 1),
(145, 'beef stock pot', 18, 5),
(146, 'microwave mash', 4, 1),
(147, 'Tenderstem broccoli', 2, 1),
(148, 'chicken breast', 5, 1),
(149, 'jasmine rice', 4, 1),
(151, 'frozen peas', 2, 1),
(152, 'chicken stock', 17, 3),
(153, 'Thai red curry paste', 10, 7),
(154, 'light coconut milk', 17, 28),
(155, 'curry paste', 10, 7),
(156, 'katsu curry sauce mix', 11, 17),
(158, 'kidney beans', 15, 1),
(159, 'fajita seasoning', 8, 1),
(160, 'fajita cooking sauce', 11, 1),
(161, 'orzo', 4, 1),
(162, 'soft cheese', 3, 7),
(163, 'Cheddar with chillies', 3, 1),
(164, 'Apple', 1, 5),
(165, 'Salad', 30, 40),
(166, 'Floss', 29, 40),
(167, 'Udon noodles', 14, 38),
(168, 'Cabanossi', 9, 40),
(169, 'Tape', 27, 45),
(170, 'Soap', 29, 43),
(171, 'Coriander', 30, 13),
(173, 'Fries', 26, 40),
(174, 'Burger bread', 23, 40),
(175, 'Cheese', 3, 47),
(176, 'Cucumber', 30, 5),
(177, 'Rigatoni', 14, 41),
(178, 'Sun dried tomatoes', 13, 42),
(179, 'Leaf spinach (Blattspinat)', 30, 40),
(182, 'Rice', 4, 37),
(183, 'Bread', 23, 46),
(184, 'Eggs', 3, 41),
(185, 'Toilet paper', 27, 40),
(186, 'Milk', 3, 39),
(187, 'Banana', 30, 13),
(188, 'Mandarinen', 30, 40),
(189, 'Coffee', 24, 40),
(190, 'Laundry detergent', 27, 15),
(191, 'Cebolinha (Chives)', 30, 13),
(192, 'Bacon', 9, 40),
(193, 'Swedish bread', 23, 40),
(194, 'Lentils', 15, 40),
(195, 'Leek (Alho poro)', 30, 5),
(197, 'Turkey breast (Putenbrust)', 5, 40),
(198, 'Hummus', 9, 44),
(199, 'Coke', 24, 15),
(200, 'Sesame oil', 21, 15),
(201, 'Sesame', 21, 40),
(202, 'Black beans', 13, 14),
(203, 'Date & peanut bar', 31, 43),
(204, 'Wasa biscuit', 31, 40),
(205, 'Butter', 3, 47),
(206, 'Chicken broth', 21, 48),
(207, 'Beef', 5, 38),
(208, 'Red lentil', 15, 40),
(209, 'Carne panela', 5, 40),
(210, 'Coca-Cola', 24, 15),
(211, 'Macarrão (Pasta)', 14, 40),
(212, 'Tomato', 30, 5),
(213, 'Strawberries', 30, 41),
(214, 'Trash bags', 27, 45),
(215, 'Cherry tomatoes', 30, 41),
(216, 'Salmon', 6, 49),
(217, 'Feta', 3, 47),
(218, 'Pasta', 14, 40),
(219, 'Chilies', 30, 40),
(220, 'Spring onion', 30, 13),
(221, 'Dumplings', 26, 40),
(222, 'Bell pepper (Spitzpaprika)', 30, 5),
(223, 'Potato', 30, 37),
(224, 'Mango chutney', 21, 42),
(225, 'Zucchini', 30, 5),
(226, 'Kimchi', 9, 42),
(227, 'Green beans (Buschbohnen)', 30, 38),
(228, 'Smoked salmon', 6, 38),
(229, 'Philadelphia', 3, 44),
(230, 'Frozen raspberry', 26, 41),
(231, 'Detergent', 27, 15),
(232, 'Clothes softener', 27, 15);

INSERT INTO recipe_item (id, recipe_id, item_id, quantity) VALUES
(1, 6, 1, 3),    -- extra-virgin olive oil, 3 tbsp (ok)
(2, 6, 2, 2),    -- shallots, 2 large (ok)
(3, 6, 3, 2),    -- celery, 2 ribs (ok)
(4, 6, 4, 1),    -- red bell pepper, 1 large (ok)
(5, 6, 5, 6),    -- garlic, 6 cloves (ok)
(6, 6, 6, 6),    -- low-sodium vegetable broth, 6 cups (ok)
(7, 6, 7, 1),    -- lime juice, juice of 1 (ok)
(8, 6, 8, 1),    -- fresh cilantro, 1/4 cup (ok)
(9, 6, 9, 2),    -- seasoned salt, 2 tsp (ok)
(10, 6, 202, 1), -- dried black beans, 1 pound (item_id=202 'Black beans' - best fit)
(11, 6, 11, 2),  -- bay leaves, 2 leaves (ok)
(12, 6, 27, 1),  -- pure maple syrup, 1 tbsp (item_id=27 'maple syrup')
(13, 6, 13, 1),  -- ground cumin, 1 tbsp (ok)
(14, 6, 14, 1),  -- chili powder, 1 tsp (ok)
(15, 6, 15, 1),  -- blue cheese crumbles, 1/2 cup (ok)
(16, 6, 16, 1),  -- reduced-fat sour cream, optional (ok)
(17, 7, 17, 2),    -- ghee, 2 tbsp (ok)
(18, 7, 99, 1),    -- onion, 1 large (item_id=99 'onion' for general onion)
(19, 7, 5, 3),     -- garlic, 3 cloves (ok)
(20, 7, 148, 2.5), -- chicken, 2.5 pounds (item_id=148 'chicken breast' - best general fit)
(22, 7, 148, 4),   -- chicken, 4 cups (again use 'chicken breast')
(23, 7, 22, 1),    -- salsa verde, 1 cup (ok)
(24, 7, 23, 2),    -- cumin, 2 tsp (ok)
(25, 7, 14, 1),    -- chili powder, 1 tsp (should be 14)
(26, 7, 24, 1),    -- black pepper, 1 tsp (ok)
(27, 7, 25, 1),    -- lime, 1 unit (ok)
(28, 7, 26, 1),    -- green chiles, 1 unit (ok)
(29, 7, 27, 1),    -- maple syrup, 1 tbsp (ok)
(31, 7, 30, 1),    -- great, 1 unit (unclear, skip or leave null)
(32, 7, 30, 1),    -- beans, 1 unit (item_id=30 'beans')
(33, 7, 31, 3),    -- cornstarch, 3 tbsp (ok)
(35, 7, 33, 1),    -- frozen corn, 1 unit (ok)
(36, 7, 35, 10.25),-- here), 10.25 cups (item_id=35 'cheese' -- but comment unclear, likely an error)
(37, 7, 9, 2),     -- seasoned salt, 2 tsp (ok)
(38, 7, 36, 1),    -- cheese, 1 unit (item_id=36 'cilantro', but likely should be item_id=35 'cheese')
(39, 7, 8, 1),     -- cilantro, 1 unit (use item_id=8 'fresh cilantro')
(40, 7, 37, 2),    -- poblano peppers, 2 large (ok)
(41, 7, 38, 2),    -- olive oil, 2 tbsp (ok)
(42, 8, 1, 2),     -- extra-virgin olive oil, 2 tbsp (ok)
(43, 8, 48, 1),    -- 90% lean ground beef, 1 pound (ok)
(44, 8, 51, 1),    -- yellow onion, 1 piece (ok)
(45, 8, 5, 2),     -- garlic, 2 cloves (ok)
(46, 8, 9, 1),     -- seasoned salt, 1 tsp (ok)
(47, 8, 52, 1),    -- garlic powder, 1 tsp (ok)
(48, 8, 39, 1),    -- Italian seasoning, 1 tsp (ok)
(49, 8, 40, 1),    -- dried oregano, 1 tsp (ok)
(50, 8, 41, 2),    -- Worcestershire sauce, 2 tbsp (ok)
(51, 8, 114, 8),   -- no-salt-added tomato sauce, 8 oz (closest: 114, 'chopped tomatoes')
(52, 8, 87, 6),    -- no-salt-added tomato paste, 6 oz (87, 'tomato purée')
(53, 8, 45, 1),    -- cauliflower rice, 1 cup (ok)
(54, 8, 46, 4),    -- bell peppers, 4 medium-size (ok)
(55, 8, 47, 0.5),  -- shredded low-fat cheese, 0.5 cup (ok)
(56, 9, 53, 1),    -- vegetable oil, 1 tbsp (ok)
(57, 9, 54, 500),  -- lean turkey mince, 500 g (ok)
(58, 9, 55, 2),    -- garlic cloves, 2 cloves (ok)
(59, 9, 56, 5),    -- fresh root ginger, 5 cm (ok)
(60, 9, 57, 3),    -- honey, 3 tbsp (ok)
(61, 9, 58, 3),    -- light soy sauce, 3 tbsp (ok)
(62, 9, 59, 2),    -- sweet chilli sauce, 2 tbsp (ok)
(63, 9, 167, 3),   -- udon noodles, 3 x 150g packet (167, 'Udon noodles')
(64, 9, 61, 2),    -- juice of limes, 2 limes (ok)
(65, 9, 62, 2),    -- carrots, 2 pieces (ok)
(66, 9, 63, 4),    -- spring onions, 4 pieces (ok)
(67, 9, 171, 1),   -- fresh coriander, 1 bunch (171, 'Coriander')
(68, 9, 65, 4),    -- lime wedges, 4 wedges (ok)
(69, 10, 66, 500),    -- lean beef mince (ok)
(70, 10, 67, 1),      -- paprika (ok)
(71, 10, 52, 1),      -- garlic powder (ok)
(72, 10, 68, 1),      -- onion powder (ok)
(73, 10, 38, 1),      -- olive oil (ok)
(74, 10, 69, 1),      -- garlic paste (ok)
(75, 10, 70, 1),      -- ginger paste (ok)
(76, 10, 71, 1),      -- chipotle paste (ok)
(77, 10, 58, 80),     -- light soy sauce (ml) (ok)
(78, 10, 57, 1),      -- honey (ok)
(79, 10, 73, 2),      -- red chillies (ok)
(80, 10, 63, 4),      -- spring onions (ok)
(81, 10, 74, 3),      -- medium noodles (ok)
(82, 11, 66, 500),   -- lean beef mince (ok)
(83, 11, 13, 1),     -- ground cumin (use item_id=13 'ground cumin')
(84, 11, 52, 1),     -- garlic powder (ok)
(85, 11, 83, 1),     -- light cooking oil (spray) (closest: 83 'cooking oil')
(86, 11, 99, 1),     -- onion (piece) (99, 'onion')
(87, 11, 78, 2),     -- microwave basmati rice (250g pouch) (78, 'basmati rice')
(88, 11, 69, 1),     -- garlic paste (ok)
(89, 11, 58, 3),     -- light soy sauce (ok)
(90, 11, 79, 2),     -- hoisin sauce (ok)
(91, 11, 126, 1),    -- flat-leaf parsley (126, 'parsley')
(92, 11, 59, 3),     -- sweet chilli sauce (ok)
(93, 11, 57, 2),     -- honey (ok)
(94, 11, 81, 1),     -- sriracha sauce (ok)
(95, 15, 38, 1),     -- olive oil (ok)
(96, 15, 99, 1),     -- onion (piece) (99, 'onion')
(97, 15, 62, 2),     -- carrot (pieces) (62, 'carrots')
(98, 15, 3, 2),      -- celery sticks (item_id=3 'celery')
(99, 15, 66, 500),   -- lean beef mince (ok)
(100, 15, 111, 200), -- mushrooms (ok)
(101, 15, 87, 3),    -- tomato purée (ok)
(102, 15, 55, 3),    -- garlic cloves (ok)
(103, 15, 113, 1),   -- chilli flakes (ok)
(104, 15, 114, 400), -- chopped tomatoes (ok)
(105, 15, 115, 200), -- single cream (ok)
(106, 15, 116, 800), -- gnocchi (ok)
(107, 15, 95, 4),    -- Parmesan (95, 'parmesan')
(108, 15, 126, 1),   -- fresh parsley (126, 'parsley')
(109, 16, 119, 260), -- linguine pasta (ok)
(110, 16, 38, 1),    -- olive oil (ok)
(111, 16, 85, 1),    -- red onion (piece) (85, 'red onion')
(112, 16, 55, 3),    -- garlic cloves (ok)
(113, 16, 120, 225), -- light halloumi (ok)
(114, 16, 67, 1.5),  -- paprika (ok)
(115, 16, 122, 1.5), -- dried Italian herbs (ok)
(116, 16, 113, 1),   -- chilli flakes (ok)
(117, 16, 87, 4),    -- tomato purée (ok)
(118, 16, 124, 200), -- hot vegetable stock (ok)
(119, 16, 162, 200); -- light soft cheese (162, 'soft cheese')
