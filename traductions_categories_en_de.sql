-- =============================================================================
-- Traductions EN / DE des catégories et sous-catégories (base new_db_recordzv3)
--
-- À EXÉCUTER UNE SEULE FOIS (les DROP FOREIGN KEY échouent à la 2e exécution).
-- Faire une sauvegarde avant. Compatible MySQL 8.4 / 9.x et MariaDB 10.3+.
--
-- Principe :
--   * langue : ajout de l'anglais (id 3, clé EN) ; l'allemand (id 2, clé DE) existe déjà.
--   * libelle : une nouvelle ligne par traduction (EN = 10 000 000 + id d'origine,
--     DE = 20 000 000 + id d'origine). Chaque libellé français garde sa propre
--     traduction : "Casques" (moto, id 64) -> Helmets, "Casques" (DJ, id 252) -> DJ headphones.
--   * categorie_libelle_langue : la clé primaire était (ref_categorie) seule, ce qui
--     interdit plusieurs langues par catégorie. Elle devient (ref_categorie, ref_langue),
--     et une table maître `categorie` reprend le rôle de cible des clés étrangères.
--   * subcategorie_libelle_langue : lignes EN/DE ajoutées pour chaque couple
--     (ref_categorie, ref_subcategorie) existant en français.
--
-- Allemand : orthographe suisse (ss au lieu de ß, Velo, etc.).
-- =============================================================================

SET NAMES utf8mb4;

-- -----------------------------------------------------------------------------
-- 1. Langues
-- -----------------------------------------------------------------------------
INSERT INTO langue (id_langue, nom, `key`) VALUES (3, 'English', 'EN')
ON DUPLICATE KEY UPDATE nom = VALUES(nom), `key` = VALUES(`key`);

-- -----------------------------------------------------------------------------
-- 2. Schéma : plusieurs langues par catégorie
-- -----------------------------------------------------------------------------

-- 2a. Retirer les clés étrangères qui pointent vers categorie_libelle_langue(ref_categorie)
ALTER TABLE boutique_a_categorie        DROP FOREIGN KEY fk_bac_categorie_categorie;
ALTER TABLE recherche                   DROP FOREIGN KEY fk_recherche_categorie;
ALTER TABLE subcategorie_libelle_langue DROP FOREIGN KEY fk_subcat_ll_categorie;
ALTER TABLE wish                        DROP FOREIGN KEY fk_wish_categorie;

-- 2b. Nouvelle clé primaire (catégorie, langue)
ALTER TABLE categorie_libelle_langue
    DROP PRIMARY KEY,
    DROP INDEX uq_ref_categorie,
    ADD PRIMARY KEY (ref_categorie, ref_langue);

-- 2c. Table maître des catégories (une ligne par id, quelle que soit la langue)
CREATE TABLE IF NOT EXISTS categorie (
    id_categorie INT(11) NOT NULL,
    PRIMARY KEY (id_categorie)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT IGNORE INTO categorie (id_categorie)
SELECT ref_categorie FROM categorie_libelle_langue
UNION SELECT ref_categorie FROM subcategorie_libelle_langue
UNION SELECT ref_categorie FROM boutique_a_categorie
UNION SELECT ref_categorie FROM recherche
UNION SELECT ref_categorie FROM wish;

-- 2d. Recréer les clés étrangères, vers la table maître
ALTER TABLE categorie_libelle_langue
    ADD CONSTRAINT fk_catll_categorie FOREIGN KEY (ref_categorie) REFERENCES categorie (id_categorie),
    ADD CONSTRAINT fk_catll_langue    FOREIGN KEY (ref_langue)    REFERENCES langue (id_langue);

ALTER TABLE boutique_a_categorie
    ADD CONSTRAINT fk_bac_categorie_categorie FOREIGN KEY (ref_categorie) REFERENCES categorie (id_categorie);
ALTER TABLE recherche
    ADD CONSTRAINT fk_recherche_categorie FOREIGN KEY (ref_categorie) REFERENCES categorie (id_categorie);
ALTER TABLE subcategorie_libelle_langue
    ADD CONSTRAINT fk_subcat_ll_categorie FOREIGN KEY (ref_categorie) REFERENCES categorie (id_categorie);
ALTER TABLE wish
    ADD CONSTRAINT fk_wish_categorie FOREIGN KEY (ref_categorie) REFERENCES categorie (id_categorie);

-- -----------------------------------------------------------------------------
-- 3. Dictionnaire de traduction (clé = id du libellé français dans `libelle`)
-- -----------------------------------------------------------------------------
CREATE TEMPORARY TABLE tmp_trad (
    ref_libelle INT(11)      NOT NULL,
    en          VARCHAR(500) NOT NULL,
    de          VARCHAR(500) NOT NULL,
    PRIMARY KEY (ref_libelle)
) DEFAULT CHARSET=utf8mb4;

INSERT INTO tmp_trad (ref_libelle, en, de) VALUES
-- ---- Catégories principales ----
(1,     'Women''s clothing',                  'Damenbekleidung'),
(2,     'Men''s clothing',                    'Herrenbekleidung'),
(3,     'Children''s clothing',               'Kinderbekleidung'),
(5,     'Cars',                               'Autos'),
(6,     'Motorcycles',                        'Motorräder'),
(7,     'TV / Video',                         'TV / Video'),
(9,     'Books',                              'Bücher'),
(11,    'Game consoles',                      'Spielkonsolen'),
(12,    'Diet & nutrition',                   'Diät & Ernährung'),
(13,    'Perfumes',                           'Parfüms'),
(14,    'Kitchen accessories',                'Küchenzubehör'),
(16,    'Cosmetics',                          'Kosmetik'),
(17,    'DJ equipment',                       'DJ-Equipment'),
(18,    'Collectibles',                       'Sammlerstücke'),
(19,    'Toys',                               'Spielzeug'),
(20,    'Accessories and tools',              'Zubehör und Werkzeuge'),
(21,    'Women''s lingerie',                  'Damenwäsche'),
(22,    'Calendars',                          'Kalender'),
(23,    'Baby world',                         'Babywelt'),
(24,    'Home appliances',                    'Haushaltsgeräte'),
(25,    'Real estate',                        'Immobilien'),
(39,    'Slippers',                           'Hausschuhe'),
(10000, 'Wines',                              'Weine'),
(10020, 'Men''s underwear',                   'Herrenwäsche'),
(10021, 'Astrology',                          'Astrologie'),
(10024, 'Pets',                               'Tiere'),
(10029, 'Women''s watches and jewellery',     'Damenuhren und Damenschmuck'),
(10030, 'Men''s watches and jewellery',       'Herrenuhren und Herrenschmuck'),
(10031, 'Home and garden',                    'Haus und Garten'),
(10040, 'DVD',                                'DVD'),
(10049, 'Musical instruments',                'Musikinstrumente'),
(10054, 'Boats',                              'Boote'),
(10055, 'Video games',                        'Videospiele'),
(10056, 'Ice hockey',                         'Eishockey'),
(10057, 'Snowboarding',                       'Snowboard'),
(10058, 'Skiing',                             'Ski'),
(10059, 'Horse riding',                       'Reitsport'),
(188,   'Computing',                          'Informatik'),
(22222, 'Art and design',                     'Kunst und Design'),

-- ---- Habits : femmes / hommes / enfants ----
(26,    'Jeans',                              'Jeans'),
(27,    'T-shirts',                           'T-Shirts'),
(28,    'Skirts',                             'Röcke'),
(29,    'Dresses',                            'Kleider'),
(30,    'Bags',                               'Taschen'),
(31,    'Shoes',                              'Schuhe'),
(32,    'Socks',                              'Socken'),
(33,    'Sunglasses',                         'Sonnenbrillen'),
(34,    'Sweaters',                           'Pullover'),
(35,    '0 to 3 months',                      '0 bis 3 Monate'),
(36,    '4 to 6 months',                      '4 bis 6 Monate'),
(37,    '7 to 12 months',                     '7 bis 12 Monate'),

-- ---- Art et design ----
(41,    'Paintings',                          'Gemälde'),
(42,    'Photographs',                        'Fotografien'),
(43,    'Sculptures',                         'Skulpturen'),
(44,    'Miscellaneous',                      'Verschiedenes'),
(47,    'Beds',                               'Betten'),
(48,    'Sofas',                              'Sofas'),
(49,    'Wardrobes',                          'Schränke'),
(50,    'TV furniture',                       'TV-Möbel'),
(51,    'Armchairs',                          'Sessel'),

-- ---- Automobile ----
(52,    'Saloon',                             'Limousine'),
(53,    'Coupé',                              'Coupé'),
(54,    '4x4',                                '4x4'),
(55,    'Sports car',                         'Sportwagen'),
(56,    'Hybrid',                             'Hybrid'),
(57,    'Electric',                           'Elektro'),

-- ---- Motos ----
(58,    '50 cc',                              '50 ccm'),
(59,    '125 cc',                             '125 ccm'),
(60,    '250 cc',                             '250 ccm'),
(61,    '500 cc',                             '500 ccm'),
(62,    '750 cc',                             '750 ccm'),
(63,    '1000 cc',                            '1000 ccm'),
(64,    'Helmets',                            'Helme'),
(65,    'Gloves',                             'Handschuhe'),
(66,    'Accessories',                        'Zubehör'),
(67,    'Jackets',                            'Jacken'),
(68,    'Trousers',                           'Hosen'),

-- ---- TV / vidéo ----
(70,    'DVD players',                        'DVD-Player'),
(71,    'Hi-fi systems',                      'Hi-Fi-Anlagen'),
(72,    'Flat-screen TVs',                    'Flachbildfernseher'),
(73,    'MP3 players',                        'MP3-Player'),
(74,    'Headphones',                         'Kopfhörer'),

-- ---- Informatique ----
(77,    'PCs',                                'PCs'),
(78,    'Laptops',                            'Laptops'),
(79,    'Macs',                               'Macs'),
(80,    'Tablets',                            'Tablets'),
(81,    'Mice',                               'Mäuse'),
(82,    'Remote controls',                    'Fernbedienungen'),
(83,    'Software',                           'Software'),
(84,    'Mobile phones',                      'Mobiltelefone'),
(85,    'Webcams',                            'Webcams'),
(86,    'Cabling',                            'Verkabelung'),
(87,    'Motherboards',                       'Mainboards'),
(88,    'Scanners',                           'Scanner'),
(89,    'Photocopiers',                       'Fotokopierer'),
(90,    'Routers',                            'Router'),
(91,    'Satellite dishes',                   'Satellitenantennen'),
(92,    'Biometric readers',                  'Biometrische Lesegeräte'),
(93,    'Smart card readers',                 'Chipkartenleser'),
(94,    'Cash registers',                     'Registrierkassen'),

-- ---- Montres et bijoux ----
(97,    'Rings',                              'Ringe'),
(98,    'Lighters',                           'Feuerzeuge'),
(99,    'Chains',                             'Ketten'),
(100,   'Jewellery sets',                     'Schmucksets'),
(1000125, 'Watches',                          'Uhren'),
(1000126, 'Earrings',                         'Ohrringe'),

-- ---- Sport : snowboard, skate, hockey, chaussures, balles ----
(118,   'Girls'' snowboards',                 'Snowboards Mädchen'),
(119,   'Boys'' snowboards',                  'Snowboards Jungen'),
(120,   'Girls'' snowboard bindings',         'Snowboardbindungen Mädchen'),
(121,   'Boys'' snowboard bindings',          'Snowboardbindungen Jungen'),
(122,   'Skateboards',                        'Skateboards'),
(129,   'Skateboard trucks',                  'Skateboard-Achsen'),
(131,   'Ball bearings',                      'Kugellager'),
(132,   'Ice hockey jerseys',                 'Eishockeytrikots'),
(133,   'Pucks',                              'Pucks'),
(135,   'Girls'' running shoes',              'Laufschuhe Mädchen'),
(136,   'Boys'' running shoes',               'Laufschuhe Jungen'),
(138,   'Boys'' trekking shoes',              'Trekkingschuhe Jungen'),
(140,   'Boys'' dance shoes',                 'Tanzschuhe Jungen'),
(141,   'Girls'' basketball shoes',           'Basketballschuhe Mädchen'),
(142,   'Boys'' basketball shoes',            'Basketballschuhe Jungen'),
(143,   'Boys'' football boots',              'Fussballschuhe Jungen'),
(144,   'Girls'' football boots',             'Fussballschuhe Mädchen'),
(145,   'Girls'' rugby boots',                'Rugbyschuhe Mädchen'),
(146,   'Boys'' rugby boots',                 'Rugbyschuhe Jungen'),
(147,   'Girls'' surf shoes',                 'Surfschuhe Mädchen'),
(148,   'Boys'' surf shoes',                  'Surfschuhe Jungen'),
(149,   'Girls'' snowboard boots',            'Snowboardschuhe Mädchen'),
(150,   'Boys'' snowboard boots',             'Snowboardschuhe Jungen'),
(151,   'Girls'' training shoes',             'Trainingsschuhe Mädchen'),
(152,   'Boys'' training shoes',              'Trainingsschuhe Jungen'),
(153,   'Girls'' snowboard clothing',         'Snowboardbekleidung Mädchen'),
(154,   'Boys'' snowboard clothing',          'Snowboardbekleidung Jungen'),
(155,   'Girls'' ski clothing',               'Skibekleidung Mädchen'),
(156,   'Boys'' ski clothing',                'Skibekleidung Jungen'),
(157,   'Girls'' ski helmets',                'Skihelme Mädchen'),
(158,   'Boys'' ski helmets',                 'Skihelme Jungen'),
(159,   'Girls'' ski goggles',                'Skibrillen Mädchen'),
(160,   'Boys'' ski goggles',                 'Skibrillen Jungen'),
(161,   'Action cameras',                     'Actionkameras'),
(162,   'Basketballs',                        'Basketbälle'),
(163,   'Footballs',                          'Fussbälle'),
(164,   'Rugby balls',                        'Rugbybälle'),
(165,   'Girls'' tennis rackets',             'Tennisschläger Mädchen'),
(166,   'Boys'' tennis rackets',              'Tennisschläger Jungen'),
(167,   'Girls'' tennis bags',                'Tennistaschen Mädchen'),
(168,   'Boys'' tennis bags',                 'Tennistaschen Jungen'),
(169,   'Girls'' saddles',                    'Reitsättel Mädchen'),
(170,   'Boys'' saddles',                     'Reitsättel Jungen'),
(171,   'Girls'' riding boots',               'Reitstiefel Mädchen'),
(172,   'Boys'' riding boots',                'Reitstiefel Jungen'),
(173,   'Girls'' riding clothing',            'Reitbekleidung Mädchen'),
(174,   'Boys'' riding clothing',             'Reitbekleidung Jungen'),

-- ---- Ski ----
(554,   'Ice hockey jerseys',                 'Eishockeytrikots'),
(555,   'Pucks',                              'Pucks'),
(600,   'Girls'' skis',                       'Ski Mädchen'),
(601,   'Boys'' skis',                        'Ski Jungen'),
(602,   'Girls'' ski bindings',               'Skibindungen Mädchen'),
(603,   'Boys'' ski bindings',                'Skibindungen Jungen'),
(604,   'Girls'' ski boots',                  'Skischuhe Mädchen'),
(605,   'Boys'' ski boots',                   'Skischuhe Jungen'),
(1000124, 'Professional ice hockey sticks',   'Professionelle Eishockeyschläger'),

-- ---- DVD ----
(175,   'Action',                             'Action'),
(176,   'Suspense',                           'Spannung'),
(177,   'Crime',                              'Krimi'),
(178,   'Comedy',                             'Komödie'),
(179,   'Tragedy',                            'Tragödie'),
(180,   'Thriller',                           'Thriller'),
(181,   'Horror',                             'Horror'),
(182,   'Animation',                          'Animation'),
(183,   'TV series',                          'Serien'),
(185,   'Documentaries',                      'Dokumentarfilme'),
(186,   'Cartoons',                           'Zeichentrickfilme'),
(187,   'Adult',                              'Erotik'),

-- ---- Livres ----
(189,   'Medicine',                           'Medizin'),
(190,   'Law',                                'Recht'),
(191,   'Mathematics',                        'Mathematik'),
(192,   'Biology',                            'Biologie'),
(193,   'Physics',                            'Physik'),
(194,   'Languages',                          'Sprachen'),
(195,   'Biology',                            'Biologie'),
(196,   'Psychology',                         'Psychologie'),
(197,   'Psychiatry',                         'Psychiatrie'),
(198,   'Cognitive science',                  'Kognitionswissenschaft'),
(199,   'Architecture',                       'Architektur'),
(201,   'Art',                                'Kunst'),
(202,   'Health',                             'Gesundheit'),
(203,   'Nutrition',                          'Ernährung'),
(204,   'Criminology',                        'Kriminologie'),
(205,   'Political science',                  'Politikwissenschaft'),
(206,   'Urban planning',                     'Städtebau'),
(207,   'Children',                           'Kinder'),
(208,   'Comics',                             'Comics'),
(209,   'Mechanics',                          'Mechanik'),
(211,   'Essays',                             'Essays'),
(212,   'Literature (languages)',             'Literatur (Sprachen)'),
(213,   'Novels',                             'Romane'),
(214,   'Genetics',                           'Genetik'),

-- ---- Jeux vidéo / consoles ----
(215,   'War',                                'Krieg'),
(216,   'Casual',                             'Casual'),
(217,   'Shoot ''em up',                      'Shoot ''em up'),
(9000,  'Xbox',                               'Xbox'),
(9001,  'PlayStation',                        'PlayStation'),
(9002,  'DES',                                'DES'),
(9003,  'Wii',                                'Wii'),
(9005,  'Action',                             'Action'),
(9006,  'Sports',                             'Sport'),

-- ---- Diététique / parfums ----
(218,   'Medicinal plants',                   'Heilpflanzen'),
(219,   'Women''s perfumes',                  'Damenparfüms'),
(220,   'Men''s perfumes',                    'Herrenparfüms'),

-- ---- Instruments de musique ----
(222,   'Piano',                              'Klavier'),
(223,   'Trumpet',                            'Trompete'),
(224,   'Baritone horn',                      'Bariton'),
(225,   'Cello',                              'Violoncello'),
(226,   'Drums',                              'Schlagzeug'),
(227,   'Clarinet',                           'Klarinette'),
(228,   'Accordion',                          'Akkordeon'),
(229,   'Synthesizer',                        'Synthesizer'),
(230,   'Saxophone',                          'Saxophon'),
(231,   'Bass guitar',                        'Bassgitarre'),
(232,   'Harpsichord',                        'Cembalo'),
(233,   'Bagpipes',                           'Dudelsack'),
(234,   'Flute',                              'Flöte'),
(235,   'Harp',                               'Harfe'),
(236,   'Double bass',                        'Kontrabass'),
(237,   'Tuba',                               'Tuba'),
(238,   'Viola',                              'Bratsche'),
(239,   'Bassoon',                            'Fagott'),
(240,   'French horn',                        'Waldhorn'),
(241,   'Oboe',                               'Oboe'),
(242,   'Violin',                             'Violine'),
(243,   'Organs',                             'Orgeln'),

-- ---- Matériel DJ ----
(244,   'Mixing desks',                       'Mischpulte'),
(245,   'Mixers',                             'Mixer'),
(246,   'DJ CD / MP3 players',                'DJ-CD-/MP3-Player'),
(247,   'DJ controllers',                     'DJ-Controller'),
(248,   'Phono cartridges',                   'Tonabnehmersysteme'),
(249,   'DJ effects',                         'DJ-Effektgeräte'),
(250,   'DJ software',                        'DJ-Software'),
(251,   'DJ effects',                         'DJ-Effektgeräte'),
(252,   'DJ headphones',                      'DJ-Kopfhörer'),
(253,   'Flight cases',                       'Flightcases'),
(254,   'DJ accessories',                     'DJ-Zubehör'),
(255,   'DJ effects',                         'DJ-Effektgeräte'),

-- ---- Collections ----
(290,   'Aviation',                           'Luftfahrt'),
(291,   'Trains / Railways',                  'Eisenbahn'),
(292,   'Bistro',                             'Bistro'),
(293,   'Lighters / Matches',                 'Feuerzeuge / Streichhölzer'),
(294,   'Women''s calendars',                 'Damenkalender'),
(295,   'Capsules',                           'Kapseln'),
(296,   'Corks',                              'Korken'),

-- ---- Jouets ----
(314,   'Race tracks',                        'Rennbahnen'),
(315,   'Figurines, statues',                 'Figuren, Statuen'),
(316,   'Building sets, Lego',                'Bausätze, Lego'),
(317,   'Outdoor games',                      'Outdoor-Spiele'),
(318,   'Vintage toys and games',             'Alte Spielsachen und Spiele'),
(319,   'Musical toys, instruments',          'Musikspielzeug, Instrumente'),
(320,   'Magic',                              'Zauberei'),
(321,   'Models',                             'Modelle'),
(322,   'Puppets',                            'Marionetten'),
(323,   'Miniature worlds',                   'Miniaturwelten'),
(324,   'Plush toys, cuddly toys',            'Plüschtiere, Kuscheltiere'),
(325,   'Plush toys, cuddly toys',            'Plüschtiere, Kuscheltiere'),
(326,   'Toy soldiers',                       'Spielzeugsoldaten'),
(327,   'Poker, casino',                      'Poker, Casino'),
(328,   'Puzzles',                            'Puzzles'),
(329,   'Robots, automatons',                 'Roboter, Automaten'),
(330,   'Star Wars',                          'Star Wars'),
(331,   'Playing cards',                      'Spielkarten'),
(332,   'Playing cards',                      'Spielkarten'),
(333,   'Costumes, masks',                    'Verkleidungen, Masken'),
(334,   'Role-playing and miniature games',   'Rollenspiele, Figurenspiele'),
(335,   'Board games',                        'Gesellschaftsspiele'),
(336,   'Educational games, brain teasers',   'Lernspiele, Knobelspiele'),
(337,   'Educational games, brain teasers',   'Lernspiele, Knobelspiele'),
(338,   'Electronic games',                   'Elektronische Spiele'),
(339,   'Model railways',                     'Modelleisenbahnen'),
(340,   'Dolls',                              'Puppen'),
(341,   'Radio-controlled, model making',     'Ferngesteuert, Modellbau'),
(342,   'Miniature vehicles',                 'Miniaturfahrzeuge'),
(343,   'Pub games',                          'Wirtshausspiele'),

-- ---- Accessoires et outils / Habitat et jardin ----
(345,   'Hand tools',                         'Handwerkzeuge'),
(346,   'Garden tools',                       'Gartenwerkzeuge'),
(347,   'Power tools',                        'Elektrowerkzeuge'),
(348,   'Electronics, components',            'Elektronik, Bauteile'),
(349,   'Electrical installation',            'Elektroinstallation'),
(350,   'Materials',                          'Materialien'),
(351,   'Paint, accessories',                 'Farben, Zubehör'),
(6352,  'Floor coverings',                    'Bodenbeläge'),
(6353,  'Wall coverings',                     'Wandbeläge'),
(6354,  'Roofing, insulation',                'Dach, Dämmung'),
(6355,  'Building work',                      'Bauarbeiten'),
(6356,  'Workwear',                           'Arbeitskleidung'),
(6357,  'Pet accessories',                    'Tierzubehör'),
(6358,  'DIY',                                'Heimwerken'),
(6359,  'DIY: tools',                         'Heimwerken: Werkzeuge'),
(6360,  'Hardware, ironmongery',              'Eisenwaren, Beschläge'),
(6380,  'Heating, air conditioning',          'Heizung, Klimatisierung'),

-- ---- Accessoires de cuisine ----
(361,   'Kitchen: tableware, accessories',    'Küche: Tischkultur, Zubehör'),
(362,   'Kitchen: pots, pans and dishes',     'Küche: Kochgeschirr, Platten'),
(363,   'Kitchen: kitchen furniture',         'Küche: Küchenmöbel'),
(364,   'Kitchen: utensils',                  'Küche: Utensilien'),
(365,   'Maintenance, cleaning',              'Pflege, Reinigung'),

-- ---- Lingerie ----
(418,   'Sets',                               'Sets'),
(419,   'Bras',                               'BHs'),
(420,   'Tights, stockings',                  'Strumpfhosen, Strümpfe'),
(421,   'Two-piece swimsuits',                'Bikinis'),
(422,   'One-piece swimsuits',                'Badeanzüge'),
(423,   'Thongs',                             'Strings'),
(8424,  'Boxer shorts',                       'Boxershorts'),
(8425,  'Briefs',                             'Slips'),

-- ---- Calendrier ----
(426,   'Sexy calendars',                     'Sexy-Kalender'),
(427,   'Charity calendars',                  'Wohltätigkeitskalender'),

-- ---- Univers de bébé ----
(429,   'Baby carriers',                      'Babytragen'),
(430,   'Strollers, travel systems',          'Kinderwagen, Kombisysteme'),
(431,   'Changing bags',                      'Wickeltaschen'),
(432,   'Car seats, bike seats',              'Autositze, Velositze'),
(433,   'Toys 0 to 6 months, girls and boys',   'Spielzeug 0 bis 6 Monate, Mädchen und Jungen'),
(434,   'Toys 6 to 12 months, girls and boys',  'Spielzeug 6 bis 12 Monate, Mädchen und Jungen'),
(435,   'Toys 12 to 18 months, girls and boys', 'Spielzeug 12 bis 18 Monate, Mädchen und Jungen'),
(436,   'Toys 18 to 24 months, girls and boys', 'Spielzeug 18 bis 24 Monate, Mädchen und Jungen'),
(437,   'Complete nurseries',                 'Komplette Kinderzimmer'),
(438,   'Decorations, night lights',          'Dekoration, Nachtlichter'),
(439,   'Sleeping bags, baby nests',          'Schlafsäcke, Babynester'),
(440,   'Bedding',                            'Bettwaren'),
(441,   'Furniture',                          'Möbel'),
(442,   'Changing units',                     'Wickelkommoden'),
(443,   'Playpens',                           'Laufgitter'),
(444,   'Baby bouncers, rockers',             'Babywippen, Schaukelwippen'),
(445,   'Baby bottles',                       'Babyflaschen'),
(446,   'Bibs',                               'Lätzchen'),
(447,   'Pacifiers',                          'Schnuller'),
(448,   'Sterilisers',                        'Sterilisatoren'),
(449,   'Sterilisers',                        'Sterilisatoren'),
(450,   'Baby monitors',                      'Babyphones'),
(451,   'Safety gates',                       'Schutzgitter'),
(452,   'Video baby monitors',                'Babyphones mit Kamera'),
(453,   'Temperature sensors',                'Temperaturmesser'),
(454,   'Nappies',                            'Windeln'),
(455,   'Toiletries',                         'Pflegeprodukte'),
(456,   'Bath capes',                         'Badecapes'),
(458,   'Baby clothing',                      'Babykleidung'),

-- ---- Electroménager ----
(459,   'Dishwashers',                        'Geschirrspüler'),
(460,   'Washing machines',                   'Waschmaschinen'),
(461,   'Fridges',                            'Kühlschränke'),
(462,   'Vacuum cleaners',                    'Staubsauger'),
(463,   'Ovens',                              'Backöfen'),
(464,   'Built-in ovens',                     'Einbaubacköfen'),
(465,   'Thermomix',                          'Thermomix'),
(466,   'Washing machines',                   'Waschmaschinen'),
(467,   'Microwaves',                         'Mikrowellen'),
(468,   'Tumble dryers',                      'Wäschetrockner'),

-- ---- Immobilier ----
(469,   'Apartments',                         'Wohnungen'),
(470,   'Villas',                             'Villen'),
(471,   'Lofts',                              'Lofts'),
(473,   'Plots of land',                      'Grundstücke'),
(475,   'Chalets',                            'Chalets'),

-- ---- Cosmétiques ----
(771,   'Skin beautifying',                   'Haut verschönern'),
(772,   'Body contouring',                    'Körperformung'),
(773,   'Pregnancy',                          'Schwangerschaft'),
(774,   'Treatment waters',                   'Pflegewasser'),
(775,   'Slimming and firming',               'Schlankheit und Straffung'),
(776,   'Sun protection',                     'Sonnenschutz'),
(777,   'After-sun',                          'After-Sun'),
(778,   'Self-tanners',                       'Selbstbräuner'),
(779,   'Neo Pastels',                        'Neo Pastels'),
(780,   'Complexion',                         'Teint'),
(781,   'Eyes',                               'Augen'),
(782,   'Lips and nails',                     'Lippen und Nägel'),
(783,   'Instant glow: handy products',       'Sofort-Glanz: praktische Produkte'),
(784,   'Body',                               'Körper'),
(785,   'Cleansing',                          'Reinigung'),
(786,   'Shaving',                            'Rasur'),
(787,   'Hydration',                          'Feuchtigkeitspflege'),
(788,   'Anti-ageing',                        'Anti-Aging'),
(789,   'S.O.S. Express',                     'S.O.S. Express'),

-- ---- Vins ----
(10001, 'Red wine',                           'Rotwein'),
(10002, 'White wine',                         'Weisswein'),
(10003, 'Rosé wine',                          'Roséwein'),
(10004, 'Champagne',                          'Champagner'),

-- ---- Astrologie / Animal ----
(10022, 'Cards / Tarot',                      'Karten / Tarot'),
(10023, 'Books',                              'Bücher'),
(10025, 'Dogs',                               'Hunde'),
(10026, 'Cats',                               'Katzen'),
(10027, 'Horses',                             'Pferde'),
(10028, 'Snakes',                             'Schlangen'),

-- ---- Bateau ----
(10050, 'Yachts',                             'Jachten'),
(10051, 'Sailing boats',                      'Segelboote'),
(10053, 'Offshore boats',                     'Offshore-Boote');

-- -----------------------------------------------------------------------------
-- 4. Création des libellés EN / DE (tolère un relancement partiel)
-- -----------------------------------------------------------------------------
INSERT INTO libelle (id_libelle, libelle)
SELECT 10000000 + ref_libelle, en FROM tmp_trad
ON DUPLICATE KEY UPDATE libelle = VALUES(libelle);

INSERT INTO libelle (id_libelle, libelle)
SELECT 20000000 + ref_libelle, de FROM tmp_trad
ON DUPLICATE KEY UPDATE libelle = VALUES(libelle);

-- -----------------------------------------------------------------------------
-- 5. Catégories : lignes EN (langue 3) et DE (langue 2)
-- -----------------------------------------------------------------------------
INSERT INTO categorie_libelle_langue (ref_categorie, ref_libelle, ref_langue)
SELECT c.ref_categorie, 10000000 + c.ref_libelle, 3
FROM categorie_libelle_langue c
JOIN tmp_trad t ON t.ref_libelle = c.ref_libelle
WHERE c.ref_langue = 1
ON DUPLICATE KEY UPDATE ref_libelle = VALUES(ref_libelle);

INSERT INTO categorie_libelle_langue (ref_categorie, ref_libelle, ref_langue)
SELECT c.ref_categorie, 20000000 + c.ref_libelle, 2
FROM categorie_libelle_langue c
JOIN tmp_trad t ON t.ref_libelle = c.ref_libelle
WHERE c.ref_langue = 1
ON DUPLICATE KEY UPDATE ref_libelle = VALUES(ref_libelle);

-- -----------------------------------------------------------------------------
-- 6. Sous-catégories : une ligne EN et DE par couple (ref_categorie, ref_subcategorie)
-- -----------------------------------------------------------------------------
INSERT INTO subcategorie_libelle_langue (ref_subcategorie, ref_categorie, ref_libelle, ref_langue)
SELECT s.ref_subcategorie, s.ref_categorie, 10000000 + s.ref_libelle, 3
FROM subcategorie_libelle_langue s
JOIN tmp_trad t ON t.ref_libelle = s.ref_libelle
WHERE s.ref_langue = 1
ON DUPLICATE KEY UPDATE ref_libelle = VALUES(ref_libelle);

INSERT INTO subcategorie_libelle_langue (ref_subcategorie, ref_categorie, ref_libelle, ref_langue)
SELECT s.ref_subcategorie, s.ref_categorie, 20000000 + s.ref_libelle, 2
FROM subcategorie_libelle_langue s
JOIN tmp_trad t ON t.ref_libelle = s.ref_libelle
WHERE s.ref_langue = 1
ON DUPLICATE KEY UPDATE ref_libelle = VALUES(ref_libelle);

-- -----------------------------------------------------------------------------
-- 7. Cas où le même libellé français a un sens différent selon la catégorie
--    "Sport" : catégorie 26 (le sport) vs sous-catégorie 54 de l'automobile (voiture sport)
-- -----------------------------------------------------------------------------
INSERT INTO libelle (id_libelle, libelle) VALUES
    (90000001, 'Sports'),
    (90000002, 'Sport')
ON DUPLICATE KEY UPDATE libelle = VALUES(libelle);

UPDATE categorie_libelle_langue SET ref_libelle = 90000001 WHERE ref_categorie = 26 AND ref_langue = 3;
UPDATE categorie_libelle_langue SET ref_libelle = 90000002 WHERE ref_categorie = 26 AND ref_langue = 2;

DROP TEMPORARY TABLE tmp_trad;

-- -----------------------------------------------------------------------------
-- 8. Contrôles (à lancer après l'exécution)
-- -----------------------------------------------------------------------------

-- 8a. Sous-catégories françaises sans traduction anglaise : doit retourner 0 ligne
SELECT s.ref_categorie, s.ref_subcategorie, l.libelle AS libelle_fr
FROM subcategorie_libelle_langue s
JOIN libelle l ON l.id_libelle = s.ref_libelle
WHERE s.ref_langue = 1
  AND NOT EXISTS (
      SELECT 1 FROM subcategorie_libelle_langue t
      WHERE t.ref_categorie = s.ref_categorie
        AND t.ref_subcategorie = s.ref_subcategorie
        AND t.ref_langue = 3);

-- 8b. Aperçu : Habits hommes / Jeans dans les trois langues
SELECT g.`key` AS langue, l.libelle
FROM subcategorie_libelle_langue s
JOIN libelle l ON l.id_libelle = s.ref_libelle
JOIN langue  g ON g.id_langue  = s.ref_langue
WHERE s.ref_categorie = 2 AND s.ref_subcategorie = 2
ORDER BY g.id_langue;
