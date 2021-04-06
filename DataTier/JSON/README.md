# Raw JSON files

This is a series of JSON files that contain the data from the DataSynthesis databases. 

## Usage

These files can be used directly in any Node.js project by using 

```javascript
require("<tablename>.json");
```

## Extraction method

You can either use the JSON files directly or generate them manually by using the following instructions.

Create a Docker network
```bash
docker network create datasynthesis
```

Start a MySQL container (non-persistent data)
```bash
docker run -d --rm --name datasynthesis_db --network datasynthesis -e MYSQL_ROOT_PASSWORD=root -e MYSQL_DATABASE=datasynthesis -e MYSQL_USER=datasynthesis -e MYSQL_PASSWORD=datasynthesis mysql:8.0
```

Import the latest data set (saved as _dbdump.sql_ in the current folder):
```bash
docker exec -i datasynthesis_db sh -c 'exec mysql -uroot -proot datasynthesis' < ./dbdump.sql
```

(Optional) Start a phpMyAdmin server to connect to the MySQL server
```bash
docker run -d --rm --name datasynthesis-phpmyadmin --network datasynthesis -e MYSQL_ROOT_PASSWORD=root -e PMA_HOST=datasynthesis_db -p 8888:80 phpmyadmin/phpmyadmin
```

Start the data scraper
```bash
docker run --rm --name datasynthesis-generator --network datasynthesis -e MYSQL_USER=root -e MYSQL_PASSWORD=root -e MYSQL_DATABASE=datasynthesis -e MYSQL_HOST=datasynthesis_db -v ./scraper:/opt/app:z -w /opt/app node:14 node .
```
