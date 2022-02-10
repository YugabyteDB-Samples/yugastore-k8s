
--YSQL
--Schema: retail
Create schema retail;

--Tables to support Microservices
--1. Products: Product Catalog Publishing Microservice
Drop table if exists retail.products cascade;
CREATE TABLE retail.products 
(
   sku UUID PRIMARY KEY,
   title VARCHAR(64),
   author VARCHAR(64),
   description VARCHAR(200),
   brand VARCHAR(64),
   categories VARCHAR(64),
   price numeric,
   imurl VARCHAR(200),
   discount numeric,
   num_reviews int,
   num_stars int,
   avg_stars double
);   

CREATE UNIQUE INDEX product_idx1 ON retail.products(title);

--2. Product Inventory: Inventory Service
Drop table if exists retail.inventory cascade;
CREATE TABLE retail.inventory 
(
     sku UUID,
     store_num  int,
     store_region VARCHAR(64), --east, west, etc
     onhand int,
     available_to_promise int,
     allocated int,
     reserved int,   
     virtual_hold int,
     PRIMARY KEY (sku, store_num)   
);
 
 CREATE INDEX inventory_idx1 ON retail.inventory(sku,store_num);
 
--2. Transactions: Shopping Service / POS Service
 Drop table if exists retail.orders cascade;
 CREATE TABLE retail.orders 
 (
      order_id  UUID PRIMARY KEY,
      sku       UUID NOT NULL,
      user_id CHAR(10) NOT NULL,
      order_details jsonb,
      order_time TIMESTAMP,
      order_total double,
      store_num int,
      store_region VARCHAR(64)
);
 
--User
Drop table if exists retail.shopusers cascade;
create table shopusers
(
  user_id UUID PRIMARY KEY,
  username VARCHAR(64) UNIQUE NOT NULL,
  passhash BYTEA NOT NULL,
  firstname VARCHAR(64) NOT NULL,
  lastname VARCHAR(64) NOT NULL,
  state VARCHAR(2) NOT NULL
);

--Shopping cart
Drop table if exists retail.shopping_cart cascade;
CREATE TABLE retail.shopping_cart(
  cart_key TEXT NOT NULL,
  user_id TEXT NOT NULL,
  asin TEXT NOT NULL,
  time_added TEXT NOT NULL,
  quantity INT NOT NULL,
  PRIMARY KEY (cart_key)
);
