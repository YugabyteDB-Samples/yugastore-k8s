
CREATE KEYSPACE IF NOT EXISTS cronos;


// Table that stores all the static attributes of a product, queries are done by sku.
//
CREATE TABLE cronos.products (
    sku text PRIMARY KEY,
    title text,
    description text,
    price double,
    imurl text,
    also_bought frozen<list<text>>,
    also_viewed frozen<list<text>>,
    bought_together frozen<list<text>>,
    buy_after_viewing frozen<list<text>>,
    brand text,
    categories set<text>,
    num_reviews int,
    num_stars int,
    avg_stars double
) WITH default_time_to_live = 0
    AND transactions = {'enabled': 'false'};

// Missing description and price




// Table to store the rankings for the various categories of a product.
//
CREATE TABLE cronos.product_rankings (
    sku text,
    category text,
    sales_rank int,
    title text,
    price double,
    imurl text,
    num_reviews int,
    num_stars int,
    avg_stars double,
    PRIMARY KEY (sku, category)
) WITH CLUSTERING ORDER BY (category ASC)
    AND default_time_to_live = 0
    AND transactions = {'enabled': 'false'};

//
// Index to retrieve the top products in a given category.
//
CREATE INDEX top_products_in_category
    ON cronos.product_rankings (category, sales_rank)
    WITH transactions = { 'enabled' : false, 'consistency_level' : 'user_enforced' };

// Orders Table

CREATE TABLE cronos.orders (
    order_id text PRIMARY KEY,
    user_id text,
    order_details text,
    order_time text,
    order_total double
) WITH default_time_to_live = 0
    AND transactions = {'enabled': 'true'};

// Product Inventory Table
CREATE TABLE cronos.product_inventory (
    sku text PRIMARY KEY,
    quantity int
) WITH default_time_to_live = 0
    AND transactions = {'enabled': 'true'};
