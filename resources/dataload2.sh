for i in products.json;
do
  python parse_metadata_json.py $i; 
  ./cassandra-loader -port 9444 -f cronos_products.csv -host localhost -schema "cronos.products(sku, title, description, price, imUrl, also_bought, also_viewed, bought_together, buy_after_viewing, brand, categories,num_reviews,num_stars,avg_stars)" -maxInsertErrors 10000 -maxErrors 10000 -charsPerColumn 256000;
  ./cassandra-loader -port 9444 -f cronos_product_rankings.csv -host localhost -schema "cronos.product_rankings(sku, category, sales_rank, title, price, imurl, num_reviews, num_stars, avg_stars)" -maxInsertErrors 10000 -maxErrors 10000 -charsPerColumn 256000;
  ./cassandra-loader -port 9444 -f cronos_product_inventory.csv -host localhost -schema "cronos.product_inventory(sku, quantity)";
  rm *.csv*
done
