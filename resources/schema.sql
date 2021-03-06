-- Following tables are being migrated from ycql to ysql
CREATE TABLE products (
      sku text PRIMARY KEY,
      title text,
      description text,
      price decimal,
      imurl text,
      also_bought text,
      also_viewed text,
      bought_together text,
      buy_after_viewing text,
      brand text,
      categories text,
      num_reviews int,
      num_stars int,
      avg_stars decimal
) ;

-- Missing description and price

-- Table to store the rankings for the various categories of a product.
CREATE TABLE product_rankings (
     sku text,
     category text,
     sales_rank int,
     title text,
     price decimal,
     imurl text,
     num_reviews int,
     num_stars int,
     avg_stars decimal,
     PRIMARY KEY (sku, category)
) ;

--
-- Index to retrieve the top products in a given category.
--
CREATE INDEX top_products_in_category
    ON product_rankings (category, sales_rank)
    ;

-- Product Inventory Table
CREATE TABLE product_inventory (
  sku text PRIMARY KEY,
  quantity int
) ;


-- We don't need separate inventory table. We can add the additional columns to product_inventory
-- CREATE TABLE inventory
-- (
--      sku UUID,
--      store_num  int,
--      store_region VARCHAR(64), --east, west, etc
--      onhand int,
--      available_to_promise int,
--      allocated int,
--      reserved int,
--      virtual_hold int,
--      PRIMARY KEY (sku, store_num)
-- );
 
--  CREATE INDEX inventory_idx1 ON inventory(sku,store_num);


CREATE TABLE orders (
       order_id text PRIMARY KEY,
       user_id text,
       order_details jsonb,
       order_time text,
       order_total decimal,
       store_num int
);

--  we already have orders table which we brought from ycql. Add additional columns to it if needed.
-- CREATE TABLE orders
--  (
--       order_id  UUID PRIMARY KEY,
--       sku       UUID NOT NULL,
--       accountid CHAR(10) NOT NULL,
--       order_details jsonb,
--       order_date TIMESTAMP,
--       order_total int,
--       store_num int,
--       store_region VARCHAR(64)
-- );
 
-- shopping_cart
create table shopping_cart
(
    cart_key text PRIMARY KEY,
    user_id text,
    sku text,
    time_added text,
    quantity int
);
